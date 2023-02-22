import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class TriviumPathFinder {
    @Option(name = "-min",
            aliases = {"--min-path-length"},
            usage = "minimal path length to search for. default is 246 as it is the minimal length for a doubling path (it's B to C)",
            required = false)
    protected int min=246;

    @Option(name = "-max",
            aliases = {"--max-path-length"},
            usage = "maximal path length to search for.",
            required = false)
    protected int max=250;

    @Option(name = "-o",
            aliases = {"--output"},
            usage = "output type cmdline/latex/java.",
            required = false)
    protected Output o=Output.cmdline;
    private enum Output{
        cmdline,latex,java;
    }

    public static void main(String... args) throws CmdLineException {
        new TriviumPathFinder(args);
    }
    public TriviumPathFinder(String... args) throws CmdLineException {
        CmdLineParser cmdparser = new CmdLineParser(this);
        try {
            cmdparser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("TriviumPathFinder -r <round> -i <instance> -b <bit>");
            cmdparser.printUsage(System.err);
            throw e;
        }
        if(o==Output.cmdline){
           triviumDoublePathFinder.exportToCmdline(min, max); //print ALL paths
        }else if(o==Output.java){
            triviumDoublePathFinder.exportToJava(min, max);//export only path if there is at least 2 of the same first node and last node.
        }else if(o==Output.latex){
            triviumDoublePathFinder.exportToLatex(min, max);//export only path if there is at least 2 of the same first node and last node.
        }
    }
}
