package edu.appstate.cs.analysis;

import edu.appstate.cs.analysis.ast.HelloWorld;
import edu.appstate.cs.analysis.parser.FileParser;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Analysis {
    public static void main(String[] args) {
        try {
            URL programFile = Analysis.class.getResource("/HelloWorld.pl");
            HelloWorld program = FileParser.parseFile(new File(programFile.toURI()).getAbsolutePath());
            System.out.println("Loaded program: " + program.toString());
        } catch (URISyntaxException e) {
            System.err.println("Could not load program from resource file: " + e.getMessage());
        }
    }
}
