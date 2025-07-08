package edu.appstate.cs.analysis.parser;

import beaver.Parser;
import edu.appstate.cs.analysis.LanguageScanner;
import edu.appstate.cs.analysis.ast.HelloWorld;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileParser {
    public static HelloWorld parseFile(String fileName) {
        Path path = Paths.get(fileName);
        try {
            String programText = Files.readString(path);
            LanguageParser parser = new LanguageParser();
            LanguageScanner scanner = new LanguageScanner(new StringReader(programText));
            HelloWorld helloWorld = (HelloWorld) parser.parse(scanner);
            return helloWorld;
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (Parser.Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }
}
