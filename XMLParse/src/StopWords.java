import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class StopWords {
	
	public static Set<String> getStopWords(){
		Set<String> stopWordsList = new HashSet<>();
		stopWordsList.add("a");
		stopWordsList.add("about");
		stopWordsList.add("above");
		stopWordsList.add("across");
		stopWordsList.add("after");
		stopWordsList.add("afterwards");
		stopWordsList.add("again");
		stopWordsList.add("against");
		stopWordsList.add("all");
		stopWordsList.add("almost");
		stopWordsList.add("alone");
		stopWordsList.add("along");
		stopWordsList.add("already");
		stopWordsList.add("also");
		stopWordsList.add("although");
		stopWordsList.add("always");
		stopWordsList.add("am");
		stopWordsList.add("among");
		stopWordsList.add("amongst");
		stopWordsList.add("amount");
		stopWordsList.add("an");
		stopWordsList.add("and");
		stopWordsList.add("another");
		stopWordsList.add("any");
		stopWordsList.add("anyhow");
		stopWordsList.add("anyone");
		stopWordsList.add("anything");
		stopWordsList.add("anyway");
		stopWordsList.add("anywhere");
		stopWordsList.add("are");
		stopWordsList.add("around");
		stopWordsList.add("as");
		stopWordsList.add("at");
		stopWordsList.add("back");
		stopWordsList.add("be");
		stopWordsList.add("became");
		stopWordsList.add("because");
		stopWordsList.add("become");
		stopWordsList.add("becomes");
		stopWordsList.add("becoming");
		stopWordsList.add("been");
		stopWordsList.add("before");
		stopWordsList.add("beforehand");
		stopWordsList.add("behind");
		stopWordsList.add("being");
		stopWordsList.add("below");
		stopWordsList.add("beside");
		stopWordsList.add("besides");
		stopWordsList.add("between");
		stopWordsList.add("beyond");
		stopWordsList.add("bill");
		stopWordsList.add("both");
		stopWordsList.add("bottom");
		stopWordsList.add("but");
		stopWordsList.add("by");
		stopWordsList.add("call");
		stopWordsList.add("can");
		stopWordsList.add("cannot");
		stopWordsList.add("cant");
		stopWordsList.add("co");
		stopWordsList.add("computer");
		stopWordsList.add("con");
		stopWordsList.add("could");
		stopWordsList.add("couldnt");
		stopWordsList.add("cry");
		stopWordsList.add("de");
		stopWordsList.add("describe");
		stopWordsList.add("detail");
		stopWordsList.add("do");
		stopWordsList.add("done");
		stopWordsList.add("down");
		stopWordsList.add("due");
		stopWordsList.add("during");
		stopWordsList.add("each");
		stopWordsList.add("eg");
		stopWordsList.add("eight");
		stopWordsList.add("either");
		stopWordsList.add("eleven");
		stopWordsList.add("else");
		stopWordsList.add("elsewhere");
		stopWordsList.add("empty");
		stopWordsList.add("enough");
		stopWordsList.add("etc");
		stopWordsList.add("even");
		stopWordsList.add("ever");
		stopWordsList.add("every");
		stopWordsList.add("everyone");
		stopWordsList.add("everything");
		stopWordsList.add("everywhere");
		stopWordsList.add("except");
		stopWordsList.add("few");
		stopWordsList.add("fifteen");
		stopWordsList.add("fify");
		stopWordsList.add("fill");
		stopWordsList.add("find");
		stopWordsList.add("fire");
		stopWordsList.add("first");
		stopWordsList.add("five");
		stopWordsList.add("for");
		stopWordsList.add("former");
		stopWordsList.add("formerly");
		stopWordsList.add("forty");
		stopWordsList.add("found");
		stopWordsList.add("four");
		stopWordsList.add("from");
		stopWordsList.add("front");
		stopWordsList.add("full");
		stopWordsList.add("further");
		stopWordsList.add("get");
		stopWordsList.add("gif");
		stopWordsList.add("go");
		stopWordsList.add("had");
		stopWordsList.add("has");
		stopWordsList.add("hasnt");
		stopWordsList.add("have");
		stopWordsList.add("he");
		stopWordsList.add("hence");
		stopWordsList.add("her");
		stopWordsList.add("here");
		stopWordsList.add("hereafter");
		stopWordsList.add("hereby");
		stopWordsList.add("herein");
		stopWordsList.add("hereupon");
		stopWordsList.add("hers");
		stopWordsList.add("herself");
		stopWordsList.add("his");
		stopWordsList.add("how");
		stopWordsList.add("however");
		stopWordsList.add("hundred");
		
		stopWordsList.add("i");
		stopWordsList.add("ie");
		stopWordsList.add("if");
		stopWordsList.add("in");
		stopWordsList.add("inc");
		stopWordsList.add("indeed");
		
		stopWordsList.add("interest");
		stopWordsList.add("into");
		stopWordsList.add("is");
		stopWordsList.add("it");
		stopWordsList.add("its");
		stopWordsList.add("itself");
		
		stopWordsList.add("keep");
		stopWordsList.add("last");stopWordsList.add("later");stopWordsList.add("latterly");stopWordsList.add("least");stopWordsList.add("less");stopWordsList.add("ltd");stopWordsList.add("made");
		stopWordsList.add("many");stopWordsList.add("may");stopWordsList.add("me");stopWordsList.add("meanwhile");stopWordsList.add("might");stopWordsList.add("mill");stopWordsList.add("mine");stopWordsList.add("more");
		stopWordsList.add("moreover");stopWordsList.add("most");stopWordsList.add("mostly");stopWordsList.add("move");stopWordsList.add("much");stopWordsList.add("must");stopWordsList.add("my");stopWordsList.add("myself");
		stopWordsList.add("name");stopWordsList.add("namely");stopWordsList.add("neither");stopWordsList.add("never");stopWordsList.add("nevertheless");stopWordsList.add("next");stopWordsList.add("nine");stopWordsList.add("no");
		stopWordsList.add("nobody");stopWordsList.add("none");stopWordsList.add("noone");stopWordsList.add("nor");stopWordsList.add("not");stopWordsList.add("nothing");stopWordsList.add("now");stopWordsList.add("nowhere");
		stopWordsList.add("of");stopWordsList.add("off");stopWordsList.add("often");stopWordsList.add("on");stopWordsList.add("once");stopWordsList.add("one");stopWordsList.add("only");stopWordsList.add("onto");
		stopWordsList.add("or");stopWordsList.add("other");stopWordsList.add("others");stopWordsList.add("otherwise");stopWordsList.add("our");stopWordsList.add("ours");stopWordsList.add("ourselves");stopWordsList.add("out");
		stopWordsList.add("over");stopWordsList.add("own");stopWordsList.add("part");stopWordsList.add("per");stopWordsList.add("perhaps");stopWordsList.add("please");stopWordsList.add("put");stopWordsList.add("rather");stopWordsList.add("re");stopWordsList.add("same");
		stopWordsList.add("see");stopWordsList.add("seem");stopWordsList.add("seemed");stopWordsList.add("seeming");stopWordsList.add("seems");stopWordsList.add("serious");stopWordsList.add("several");stopWordsList.add("she");stopWordsList.add("should");stopWordsList.add("show");
		stopWordsList.add("side");stopWordsList.add("since");stopWordsList.add("sincere");stopWordsList.add("six");stopWordsList.add("sixty");stopWordsList.add("so");stopWordsList.add("somehow");stopWordsList.add("someone");stopWordsList.add("something");stopWordsList.add("sometime");stopWordsList.add("sometimes");stopWordsList.add("somewhere");
		stopWordsList.add("still");stopWordsList.add("such");stopWordsList.add("system");stopWordsList.add("take");stopWordsList.add("ten");stopWordsList.add("than");stopWordsList.add("that");stopWordsList.add("the");stopWordsList.add("their");stopWordsList.add("them");
		stopWordsList.add("themselves");stopWordsList.add("then");stopWordsList.add("thence");stopWordsList.add("there");stopWordsList.add("thereafter");stopWordsList.add("thereby");stopWordsList.add("therefore");stopWordsList.add("therein");stopWordsList.add("thereupon");stopWordsList.add("these");
		stopWordsList.add("they");stopWordsList.add("thick");stopWordsList.add("thin");stopWordsList.add("third");stopWordsList.add("this");stopWordsList.add("those");stopWordsList.add("though");stopWordsList.add("three");stopWordsList.add("through");stopWordsList.add("throughout");
		stopWordsList.add("thru");stopWordsList.add("thus");stopWordsList.add("to");stopWordsList.add("together");stopWordsList.add("too");stopWordsList.add("top");stopWordsList.add("toward");stopWordsList.add("towards");stopWordsList.add("twelve");stopWordsList.add("twenty");stopWordsList.add("two");
		stopWordsList.add("un");stopWordsList.add("under");stopWordsList.add("until");stopWordsList.add("up");stopWordsList.add("upon");stopWordsList.add("us");stopWordsList.add("very");stopWordsList.add("via");stopWordsList.add("was");stopWordsList.add("we");stopWordsList.add("well");
		stopWordsList.add("were");stopWordsList.add("what");stopWordsList.add("whatever");stopWordsList.add("when");stopWordsList.add("whence");stopWordsList.add("whenever");stopWordsList.add("where");stopWordsList.add("whereafter");stopWordsList.add("whereas");
		stopWordsList.add("whereby");stopWordsList.add("wherein");stopWordsList.add("whereupon");stopWordsList.add("whereever");stopWordsList.add("whether");stopWordsList.add("which");stopWordsList.add("while");stopWordsList.add("whither");stopWordsList.add("who");stopWordsList.add("whoever");
		stopWordsList.add("whole");stopWordsList.add("whom");stopWordsList.add("whose");stopWordsList.add("why");stopWordsList.add("will");stopWordsList.add("with");stopWordsList.add("within");stopWordsList.add("without");stopWordsList.add("would");stopWordsList.add("yet");
		stopWordsList.add("you");stopWordsList.add("yours");stopWordsList.add("yourself");stopWordsList.add("yourselves");
	
		return stopWordsList;
	}
	

}