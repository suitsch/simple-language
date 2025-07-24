package edu.appstate.cs.analysis;

import edu.appstate.cs.analysis.analysis.ReachingDefs;
import edu.appstate.cs.analysis.ast.HelloWorld;
import edu.appstate.cs.analysis.ast.StmtList;
import edu.appstate.cs.analysis.cfg.CFG;
import edu.appstate.cs.analysis.parser.FileParser;
import edu.appstate.cs.analysis.visitor.PrettyPrinter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Analysis {
    public static void main(String[] args) {

        try {
            Terminal terminal = TerminalBuilder.terminal();
            terminal.writer().println("Welcome to the console!");
            terminal.writer().println("Type each command on its own line.");
            terminal.writer().println("Type #quit to exit, #help to get help, and #load to load a file.");

            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
            LineReaderImpl specificReader = reader instanceof LineReaderImpl ? (LineReaderImpl) reader : null;

            while (true) {
                String contents = reader.readLine(">");
                contents = contents.trim();
                if (contents.equalsIgnoreCase("#quit")) {
                    break;
                } else if (contents.startsWith("#load")) {
                    String fileToLoad = contents.split(" ")[1];
                    File file = new File(fileToLoad);
                    if (!file.exists()) {
                        terminal.writer().println("File " + fileToLoad + " does not exist!");
                    } else {
                        StmtList program = FileParser.parseFile(file.getAbsolutePath());
                        String prettyPrinted = program.accept(new PrettyPrinter());
                        terminal.writer().println("Loaded program:");
                        terminal.writer().println(prettyPrinted);
                    }
                } else if (contents.startsWith("#cfg")) {
                    String fileToLoad = contents.split(" ")[1];
                    File file = new File(fileToLoad);
                    if (!file.exists()) {
                        terminal.writer().println("File " + fileToLoad + " does not exist!");
                    } else {
                        StmtList program = FileParser.parseFile(file.getAbsolutePath());
                        terminal.writer().println("Loaded program");
                        CFG cfg = new CFG();
                        cfg.buildCFG(program);
                        String graph = cfg.toDot();
                        terminal.writer().println(graph);
                    }
                } else if (contents.startsWith("#defs")) {
                    String fileToLoad = contents.split(" ")[1];
                    File file = new File(fileToLoad);
                    if (!file.exists()) {
                        terminal.writer().println("File " + fileToLoad + " does not exist!");
                    } else {
                        StmtList program = FileParser.parseFile(file.getAbsolutePath());
                        terminal.writer().println("Loaded program");
                        CFG cfg = new CFG();
                        cfg.buildCFG(program);
                        String graph = cfg.toDot();
                        terminal.writer().println(graph);
                        ReachingDefs reachingDefs = new ReachingDefs(cfg);
                        Map<String, Set<ReachingDefs.Def>> defs = reachingDefs.computeDefs();
                        terminal.writer().println("Defs:");
                        for (String key : defs.keySet()) {
                            if (defs.get(key).size() > 0) {
                                for (ReachingDefs.Def def : defs.get(key)) {
                                    terminal.writer().println("Location " + key + ": " + def);
                                }
                            }
                        }
                    }
                } else if (contents.equalsIgnoreCase("#help")) {
                    terminal.writer().println("You can use the following command:");
                    terminal.writer().println("  #quit will quit this command shell");
                    terminal.writer().println("  #help will print this message");
                    terminal.writer().println("  #load followed by a file name will load a file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
