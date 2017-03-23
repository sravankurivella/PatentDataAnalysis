/**
 * @author sravankurivella 
 * @author divyaravi
 * @author mythrimadhu
 * @author krishnaveni
 * @author amrutha
 * @author giridhar
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElements;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class PatentParser {
	public static String filename = new String();
    public static String output_folder = new String();
    public static String citations = new String();
    // The XMLInputFormat class is used to divide the XML into multiple XML files based on start and end tags.
    public static class XMLInputFormat extends TextInputFormat{
    	public static final String START_TAG_KEY = "xmlinput.start";
        public static final String END_TAG_KEY = "xmlinput.end";
        
        public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context)
        {
            return new XmlRecordReader();
        }
        
        public static class XmlRecordReader
        extends RecordReader<LongWritable, Text>
        {
            private byte[] startTag;
            private byte[] endTag;
            private long start;
            private long end;
            private FSDataInputStream fsin;
            private DataOutputBuffer buffer = new DataOutputBuffer();
            private LongWritable key = new LongWritable();
            private Text value = new Text();
            
            public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException
            {
                Configuration conf = context.getConfiguration();
                this.startTag = conf.get("xmlinput.start").getBytes("utf-8");
                this.endTag = conf.get("xmlinput.end").getBytes("utf-8");
                FileSplit fileSplit = (FileSplit)split;
                
                this.start = fileSplit.getStart();
                this.end = (this.start + fileSplit.getLength());
                Path file = fileSplit.getPath();
                FileSystem fs = file.getFileSystem(conf);
                this.fsin = fs.open(fileSplit.getPath());
                this.fsin.seek(this.start);
            }
            
            public boolean nextKeyValue()
            throws IOException, InterruptedException
            {
                if ((this.fsin.getPos() < this.end) &&
                    (readUntilMatch(this.startTag, false)))
                {
                    try
                    {
                        this.buffer.write(this.startTag);
                        if (readUntilMatch(this.endTag, true))
                        {
                            this.key.set(this.fsin.getPos());
                            this.value.set(this.buffer.getData(), 0,
                                           this.buffer.getLength());
                            return true;
                        }
                    }
                    finally
                    {
                        this.buffer.reset();
                    }
                    this.buffer.reset();
                }
                return false;
            }
            
            public LongWritable getCurrentKey()
            throws IOException, InterruptedException
            {
                return this.key;
            }
            
            public Text getCurrentValue()
            throws IOException, InterruptedException
            {
                return this.value;
            }
            
            public void close()
            throws IOException
            {
                this.fsin.close();
            }
            
            public float getProgress()
            throws IOException
            {
                return (float)(this.fsin.getPos() - this.start) / (float)(this.end - this.start);
            }
            
            private boolean readUntilMatch(byte[] match, boolean withinBlock)
            throws IOException
            {
                int i = 0;
                do
                {
                    int b = this.fsin.read();
                    if (b == -1) {
                        return false;
                    }
                    if (withinBlock) {
                        this.buffer.write(b);
                    }
                    if (b == match[i])
                    {
                        i++;
                        if (i >= match.length) {
                            return true;
                        }
                    }
                    else
                    {
                        i = 0;
                    }
                } while ((withinBlock) || (i != 0) || (this.fsin.getPos() < this.end));
                return false;
            }
        }
    	
    }
	
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{
    		    boolean bPublicationRef = false;
    		    boolean bAbstract = false;
    		    boolean bClassification = false;
    		    boolean bAssignee = false;
    		    boolean bDocumentID = false;
    		    boolean bReferencesParent = false;
    		    boolean bCitation = false;
    		    boolean bPatCit = false;
    		    boolean bCitDocumentID = false;
    		    boolean bApplicationReference = false;
    		    boolean bAppRefDocID = false;
    		    boolean bAppRefDate = false;
    		    boolean root = false;
    		
    		    @SuppressWarnings("rawtypes")
				MultipleOutputs mos;
    		    
    		    protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
    		      throws IOException, InterruptedException
    		    {
    		      this.mos.close();
    		      super.cleanup(context);
    		    }
    		    
    		    @SuppressWarnings({ "unchecked", "rawtypes" })
				protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
    		      throws IOException, InterruptedException
    		    {
    		      this.mos = new MultipleOutputs(context);
    		      super.setup(context);
    		    }
    		    
    		    @SuppressWarnings({ "unchecked", "rawtypes" })
				protected void map(LongWritable key, Text value, Mapper.Context context)
    		      throws IOException, InterruptedException
    		    {
    		      String sDocument = value.toString();
    		      String sID = new String();
    		      String sTitle = new String();
    		      String sClassName = new String();
    		      String sYear = new String();
    		      String sOYear = new String();
    		      String sOrgName = new String();
    		      String sAbstract = new String();
    		      String sWord1 = new String();
    		      String sWord2 = new String();
    		      String sWord3 = new String();
    		      String sCitDate = new String();
    		      String sCitDocNum = new String();
    		      String sCitCountry = new String();
    		      String sCitKind = new String();
    		      
    		      //String citations = new String();
    		      try
    		      {
    		        XMLStreamReader reader = XMLInputFactory.newInstance()
    		          .createXMLStreamReader(
    		          new ByteArrayInputStream(sDocument.getBytes()));
    		        String currentElement = "";
    		        String propValue = "";
    		        int count = 0;
    		        int citaiton_count = 0;
    		        while (reader.hasNext())
    		        {
    		          int code = reader.next();
    		          switch (code)
    		          {
    		          case 1: 
    		            currentElement = reader.getLocalName();
    		            break;
    		          case 4: 
    		            if (currentElement.equalsIgnoreCase("publication-reference"))
    		            {
    		              this.bPublicationRef = true;
    		            }
    		            else if (currentElement.equalsIgnoreCase("document-id") && this.bPublicationRef)
    		            {
    		              
    		                this.bDocumentID = true;
    		              
    		            }
    		            else if (currentElement.equalsIgnoreCase("doc-number") && this.bDocumentID)
    		            {
    		             
    		                this.bDocumentID = false;
    		                propValue = reader.getText().trim()
    		                  .toLowerCase();
    		                if (propValue.length() > 1) {
    		                  sID = propValue;
    		                }
    		              
    		            }
    		            else if (currentElement.equalsIgnoreCase("date") && this.bPublicationRef)
    		            {
    		              
    		                this.bPublicationRef = false;
    		                propValue = reader.getText().trim()
    		                  .toLowerCase();
    		                if (propValue.length() > 1) {
    		                  sYear = 
    		                    propValue.substring(0, 4).toString();
    		                }
    		              
    		            }
    		            
    		            else if(currentElement.equalsIgnoreCase("application-reference")){
    		            	bApplicationReference = true;
    		            }
    		            else if(currentElement.equalsIgnoreCase("document-id") && this.bApplicationReference){
    		            	this.bAppRefDocID = true;
    		            }
    		            else if (currentElement.equalsIgnoreCase("date") && this.bAppRefDocID){
    		            	sOYear = reader.getText().trim().substring(0, 4);
    		            	this.bApplicationReference = false;
    		            	this.bAppRefDocID = false;
    		            }
    		            else if (currentElement.equalsIgnoreCase("invention-title"))
    		            {
    		              propValue = 
    		                reader.getText().trim().toLowerCase();
    		              if (propValue.length() > 1) {
    		                sTitle = propValue;
    		                propValue = propValue.replaceAll("[^A-Za-z0-9]", " ");
    		              }
    		            }
    		            else if (currentElement.equalsIgnoreCase("classification-national"))
    		            {
    		              this.bClassification = true;
    		              count++;
    		            }
    		            else if (currentElement.equalsIgnoreCase("main-classification"))
    		            {
    		              if ((this.bClassification) && (count == 1))
    		              {
    		                this.bClassification = false;
    		                propValue = reader.getText().trim()
    		                  .toLowerCase();
    		                if (propValue.length() > 1) {
    		                  sClassName = propValue;
    		                }
    		              }
    		            }
    		            else if (currentElement.equalsIgnoreCase("abstract"))
    		            {
    		              this.bAbstract = true;
    		            }
    		            else if (currentElement.equalsIgnoreCase("p"))
    		            {
    		              if (this.bAbstract)
    		              {
    		                this.bAbstract = false;
    		                HashMap<String, Integer> hashMap = new HashMap();
    		                propValue = reader.getText().trim()
    		                  .toLowerCase();
    		                sAbstract = propValue.replaceAll("[^A-Za-z0-9]", 
    		                  " ").replaceAll(" +", " ");
    		                StringBuffer clean = new StringBuffer();
    						int index = 0;
    						while (index < sAbstract.length()) {
    							int nextIndex = sAbstract.indexOf(" ", index);
    							  if (nextIndex == -1) {
    							    nextIndex = sAbstract.length() - 1;
    							  }
    							  String word = sAbstract.substring(index, nextIndex);
    							  if (!StopWords.getStopWords().contains(word.toLowerCase())) {
    							    clean.append(word);
    							    if (nextIndex < sAbstract.length()) {
    							      // this adds the word delimiter, e.g. the following space
    							      clean.append(sAbstract.substring(nextIndex, nextIndex + 1)); 
    							    }
    							  }
    							  index = nextIndex + 1;
    						}
    						sAbstract = clean.toString();
    		                String[] abs_array = sAbstract.split(" ");
    		                for (int i = 0; i < abs_array.length; i++) {
    		                  if (!hashMap.containsKey(abs_array[i])) {
    		                    hashMap.put(abs_array[i], Integer.valueOf(1));
    		                  } else {
    		                    hashMap.put(abs_array[i], 
    		                      Integer.valueOf(((Integer)hashMap.get(abs_array[i])).intValue() + 1));
    		                  }
    		                }
    		                
							List<java.util.Map.Entry<String, Integer>> list = new LinkedList(hashMap.entrySet());
    		                
    		                Collections.sort(list, new Comparator()
    		                  {

								@Override
								public int compare(Object o1, Object o2) {
									// TODO Auto-generated method stub
									java.util.Map.Entry<String, Integer> a = (Entry<String, Integer>) o1;
									java.util.Map.Entry<String, Integer> b = (Entry<String, Integer>) o2;
									return b.getValue().compareTo(a.getValue());
								}
    		                    
    		                  });
    		                java.util.Map<String, Integer> result = new LinkedHashMap();
    		                for (java.util.Map.Entry<String, Integer> entry : list) {
    		                  result.put((String)entry.getKey(), (Integer)entry.getValue());
    		                }
    		                Iterator<String> iterator = result.keySet()
    		                  .iterator();
    		                if (iterator.hasNext()) {
    		                  sWord1 = (String)iterator.next();
    		                }
    		                if (iterator.hasNext()) {
    		                  sWord2 = (String)iterator.next();
    		                }
    		                if (iterator.hasNext()) {
    		                  sWord3 = (String)iterator.next();
    		                }
    		              }
    		            }
    		            else if (currentElement.equalsIgnoreCase("assignees"))
    		            {
    		              this.bAssignee = true;
    		            }
    		            else if ((currentElement.equalsIgnoreCase("orgname")) && 
    		              (this.bAssignee))
    		            {
    		              this.bAssignee = false;
    		              propValue = reader.getText().trim()
    		                .toLowerCase();
    		              propValue = propValue.replace("%ampr%", 
    		                "&");
    		              propValue = propValue.replaceAll("[^&a-z0-9 ]+", "");
    		              if (propValue.length() > 1) {
    		                sOrgName = propValue;
    		              }
    		            }
    		           
    		            //Patent Citations
    		            else if (currentElement.equalsIgnoreCase("us-bibliographic-data-grant")){
    		            	root = true; 
    		            //	cit_docnum += "root";
    		            }
    		            else if(currentElement.equalsIgnoreCase("us-references-cited") || currentElement.equalsIgnoreCase("references-cited") && root){
    		            	bReferencesParent = true;
    		            	//cit_docnum += "usref";
    		            }
    		            else if ((currentElement.equalsIgnoreCase("us-citation") || currentElement.equalsIgnoreCase("citation")) && bReferencesParent){
    		            	bCitation = true;
    		            	//cit_docnum += "usCit";
    		            }
    		            else if(currentElement.equalsIgnoreCase("patcit") && bCitation){
    		            	bPatCit = true;
    		            	citaiton_count = citaiton_count + 1;
    		            	//cit_docnum += "patcit";
    		            }
//    		            else if(currentElement.equalsIgnoreCase("document-id") && bPatCit){
//    		            	bCitDocumentID = true;
//    		            	//cit_docnum += "docid";
//    		            }
//    		            else if(currentElement.equalsIgnoreCase("country") && bCitDocumentID){
//    		            	sCitCountry = sCitCountry + reader.getText().trim() +"|";
//    		            }
//    		            else if(currentElement.equalsIgnoreCase("doc-number") && bCitDocumentID){
//    		            	//cit_docnum  += "loop";
//    		            	sCitDocNum = sCitDocNum  + reader.getText().trim()+ "|";
//    		            }
//    		            else if(currentElement.equalsIgnoreCase("kind") && bCitDocumentID){
//    		            	sCitKind = sCitKind + reader.getText().trim()+ "|" ;
//    		            }
//    		            else if(currentElement.equalsIgnoreCase("date") && bCitDocumentID){
//    		            	sCitDate = sCitDate  + reader.getText().trim()+ "|";
//    		            	bCitation = false;
//    		            	bPatCit = false;
//    		            	bCitDocumentID = false;
//    		            }
    		          
    		            break;
    		          }
    		        }
    		        if (sID.length() < 1) {
    		          sID = "null";
    		        }
    		        if (sTitle.length() < 1) {
    		          sTitle = "null";
    		        }
    		        if (sClassName.length() < 1) {
    		          sClassName = "null";
    		        }
    		        if (sYear.length() < 1) {
    		          sYear = "null";
    		        }
    		        if (sOrgName.length() < 1) {
    		          sOrgName = "null";
    		        }
    		        if (sWord1.length() < 1) {
    		          sWord1 = "null";
    		        }
    		        if (sWord2.length() < 1) {
    		          sWord2 = "null";
    		        }
    		        if (sWord3.length() < 1) {
    		          sWord3 = "null";
    		        }
    		        if (sCitCountry.length() < 1){
    		        	sCitCountry = "null";
    		        }
    		        if (sCitDocNum.length() < 1){
    		        	sCitDocNum = "null";
    		        }
    		        if (sCitKind.length() < 1){
    		        	sCitKind = "null";
    		        }
    		        if (sCitDate.length() < 1){
    		        	sCitDate = "null";
    		        }
    		        if(citaiton_count == 0 || String.valueOf(citaiton_count).equals(null)){
    		        	citaiton_count = 0;
    		        }
    		        else if(sOYear.length() < 1){
    		        	sOYear = "null";
    		        }
    		        //+sCitCountry+","+sCitKind+","+sCitDate+","+sCitDocNum+","
    		        this.mos.write("patentdata", sID + "," + sTitle + "," + sClassName + ","+sOYear+
    		          "," + sYear + "," + sOrgName + "," + sWord1 + "," + 
    		          sWord2 + "," + sWord3+","+String.valueOf(citaiton_count), NullWritable.get());
    		        Text att = new Text();
    		        att.set("doc-number,invention-title,classification-national,OYear,Fyear,orgname,word1,word2,word3,citations_count");
    		        context.write(att, new IntWritable(1));
    		        reader.close();
    		      }
    		      catch (Exception e)
    		      {
    		        throw new IOException(e);
    		      }
    		    }
    }
    
    public static class Reduce extends Reducer<Text, IntWritable, Text, NullWritable>{
    	@SuppressWarnings("rawtypes")
		MultipleOutputs mos;

		@Override
		protected void cleanup(Reducer<Text, IntWritable, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			this.mos.close();
			super.cleanup(context);
		}

		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected void setup(Reducer<Text, IntWritable, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			this.mos = new MultipleOutputs(context);
			super.setup(context);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void reduce(Text arg0, Iterable<IntWritable> arg1,
				Reducer<Text, IntWritable, Text, NullWritable>.Context arg2) throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			this.mos.write("patentdataattributes", arg0, NullWritable.get());
		}

    	
    }
    
    public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		// TODO Auto-generated method stub
    	GenericOptionsParser parser = new GenericOptionsParser(args);
        Configuration conf = parser.getConfiguration();
        String[] arguments = parser.getRemainingArgs();
        
        conf.set("xmlinput.start", "<us-patent-grant");
        conf.set("xmlinput.end", "</us-patent-grant>");
        
        Job job = Job.getInstance(conf);
        job.setJarByClass(PatentParser.class);
        
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        
        job.setInputFormatClass(XMLInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        MultipleOutputs.addNamedOutput(job, "patentdataattributes", 
                                       TextOutputFormat.class, Text.class, Text.class);
        MultipleOutputs.addNamedOutput(job, "patentdata", 
                                       TextOutputFormat.class, Text.class, Text.class);
        
        MultipleInputs.addInputPath(job, new Path(arguments[0]), XMLInputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(arguments[1]));
        
        job.waitForCompletion(true);
	}

}
