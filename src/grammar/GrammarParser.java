package grammar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GrammarParser {

    public static void main(String[] args) {
        Grammar testGrammar = GrammarParser.parse("res/grammar.json");
        System.out.println(testGrammar.toString());
    }

    static public Grammar parse(String fileName) {
        Grammar grammar = new Grammar();
        try {
            File myJSON = new File(fileName);
            String textJSON = new String(Files.readAllBytes(Paths.get(myJSON.getPath())), Charset.defaultCharset());
            JSONObject grammarJSON = new JSONObject(textJSON);
            Iterator<String> leftSides = grammarJSON.keys();
            while (leftSides.hasNext())
            {
                String leftSide = leftSides.next();
                JSONArray rightSides = grammarJSON.getJSONArray(leftSide);
                Rule rule = new Rule(leftSide);
                for (int i = 0; i < rightSides.length(); i++) {
                    JSONArray rightSide = rightSides.getJSONArray(i);
                    List<String> words = new ArrayList<>();
                    for (int j = 0; j < rightSide.length(); j++) {
                        Object word = rightSide.get(j);
                        words.add(word.toString());
                    }
                    RightSide tempRightSide = new RightSide(words);
                    rule.addCase(tempRightSide);
                    grammar.addReverseRule(tempRightSide, leftSide);
                }
                grammar.addRule(rule);
            }
            return grammar;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
