package Parser;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    static final String domain = "leaking.via";

    static List<String> parse(String fileToRead) throws IOException {
        List<String> types = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // Separator is not used.
        CSVReader reader = new CSVReader(new FileReader(fileToRead), '^');
        List<String[]> records = reader.readAll();


        Pattern p = Pattern.compile(domain + "/(?<type>.+?)(\'|\"|<|>|\r|\n| |#|&|;|" + Pattern.quote(")") + ")");
        int i = 1;
        outer_loop:
        for(String[] URL : records) {
            Matcher m = p.matcher(URL[0]);
            boolean foundSomething = false;
            while(m.find()) {
                if (URL.length > 1)
                    throw new IllegalArgumentException("Line in " + fileToRead + "contains more than one entry.");
                types.add(URL[0].substring(m.start("type"), m.end("type")));
                indices.add(i);
                i++;
                foundSomething = true;
            }
            if (foundSomething) continue outer_loop;
            //if(indices.get(indices.size() - 1) != i) {
            //    types.add("NO MATCH FOUND");
            //    indices.add(i);
            //}
            // Exceptions:
            //     - javascript:atob
            if (URL[0].contains("javascript:atob(")) {
                types.add("iframe-javascript-src-3");
                indices.add(i);
            } else {
                types.add("NO MATCH FOUND");
                indices.add(i);
            }
            i++;
        }

        for(int j = 0; j < types.size(); j++) {
            System.out.println(indices.get(j) + ": " + types.get(j));
        }

        // TODO: check whether every leak has been parsed

        Collections.sort(types);

        reader.close();

        return types;
    }

    private static void write(List<String> types, String fileName) throws IOException {
        StringBuilder toWrite = new StringBuilder();
        for(String type : types) {
            toWrite.append(type + "\n");
        }

        PrintWriter writer = new PrintWriter(new File(fileName));
        writer.write(toWrite.toString());
        writer.close();
    }

    public static void run(String fileToRead, String fileToWrite) throws IOException {
        List<String> types = parse(fileToRead);
        write(types, fileToWrite);
    }

}