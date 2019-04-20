package grammar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class GrammarParser {

    public static void main(String[] args) {
        GrammarParser grammarParser = new GrammarParser();
        Grammar grammar = new Grammar();
        grammarParser.parser(grammar);
    }

    public void parser(Grammar grammar) {

        try {
            File myJSON = new File("res/grammar.json");
            String textJSON = new String(Files.readAllBytes(Paths.get(myJSON.getPath())), Charset.defaultCharset());
            JSONObject grammarJSON = new JSONObject(textJSON);
            Iterator<String> leftSides = grammarJSON.keys();
            while (leftSides.hasNext())
            {
                String leftSide = leftSides.next();
                JSONArray rightSides = grammarJSON.getJSONArray(leftSide);
                Rule rule = new Rule(leftSide);
                for (int i = 0; i < rightSides.length(); i++) {
                    String rightSide = rightSides.get(i).toString();
                    rightSide = rightSide.replaceAll("[\\[\\]\"]", "");
                    List<String> words = Arrays.asList(rightSide.split("[,]"));
                    rule.addCase(new RightSide(words));
                }
                grammar.addRule(rule);
            }
            grammar.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
