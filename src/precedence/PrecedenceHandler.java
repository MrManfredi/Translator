package precedence;

import grammar.Grammar;
import grammar.GrammarParser;
import grammar.RightSide;
import grammar.Rule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrecedenceHandler {
    private Grammar grammar;
    private Precedence precedence;
    public PrecedenceHandler(Grammar grammar) {
        this.grammar = grammar;
        this.precedence = new Precedence();
    }

    public Precedence computePrecedences() {
        precedence = new Precedence();
        List<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            List<RightSide> rightSides = rule.getRightSides();
            for (RightSide rightSide : rightSides) {
                List<String> words = rightSide.getWords();

                for (int i = 0; i < words.size() - 1; i++) {
                    String leftWord = words.get(i);
                    String rightWord = words.get(i + 1);
                    List<String> firstPlusRight = new ArrayList<>();
                    List<String> lastPlusLeft = new ArrayList<>();
                    // 1) equal (first rule)
                    precedence.addPrecedence(leftWord, rightWord, RelationType.EQUAL);
                    // 2) less (second rule)
                    if (grammar.getNonterminals().contains(rightWord))
                    {
                        firstPlusRight = calculateFirstPlus(rightWord);
                        for (String firstPlusRightWord : firstPlusRight) {
                            precedence.addPrecedence(leftWord, firstPlusRightWord, RelationType.LESS);
                        }
                    }
                    // 3.1) more (third rule first part)
                    if (grammar.getNonterminals().contains(leftWord)) {
                        lastPlusLeft = calculateLastPlus(leftWord);
                        if (!grammar.getNonterminals().contains(rightWord)) {
                            for (String word : lastPlusLeft) {
                                precedence.addPrecedence(word, rightWord, RelationType.MORE);
                            }
                        }
                    }
                    // 3.2) more (third rule second part)
                    if (!firstPlusRight.isEmpty() && !lastPlusLeft.isEmpty()) {
                        for (String lastLeft : lastPlusLeft) {
                            for (String firstRight : firstPlusRight) {
                                precedence.addPrecedence(lastLeft, firstRight, RelationType.MORE);
                            }
                        }
                    }
                }
            }
        }
        return precedence;
    }

    private List<String> calculateFirstPlus(String word) {
        List<String> firstPlus = calculateFirst(word);
        for (int i = 0; i < firstPlus.size(); i++) {
            if (grammar.getNonterminals().contains(firstPlus.get(i))) {
                for (String tempWord : calculateFirst(firstPlus.get(i))) {
                    if (!firstPlus.contains(tempWord)) {
                        firstPlus.add(tempWord);
                    }
                }
            }
        }
        return firstPlus;
    }

    private List<String> calculateFirst(String word) {
        List<String> first = new ArrayList<>();
        for (RightSide rightSide : grammar.getRule(word).getRightSides()) {
            String temp = rightSide.getWords().get(0);      // the first word of each right side
            if (!first.contains(temp)) {
                first.add(temp);
            }
        }
        return first;
    }

    private List<String> calculateLastPlus(String word) {
        List<String> lastPlus = calculateLast(word);
        for (int i = 0; i < lastPlus.size(); i++) {
            if (grammar.getNonterminals().contains(lastPlus.get(i))) {
                for (String tempWord : calculateLast(lastPlus.get(i))) {
                    if (!lastPlus.contains(tempWord)) {
                        lastPlus.add(tempWord);
                    }
                }
            }
        }
        return lastPlus;
    }

    private List<String> calculateLast(String word) {
        List<String> first = new ArrayList<>();
        for (RightSide rightSide : grammar.getRule(word).getRightSides()) {
            String temp = rightSide.getWords().get(rightSide.getWords().size() - 1);      // the last word of each right side
            if (!first.contains(temp)) {
                first.add(temp);
            }
        }
        return first;
    }

    private static String getHtmlCode(Precedence precedence) {
        StringBuilder builder = new StringBuilder();

        builder.append("<html>\n");
        builder.append("\t<head>\n");
        builder.append("\t<title>Precedence table</title>\n");
        builder.append("\t<link rel=\"stylesheet\" type=\"text/css\" href=\"table_style.css\">\n");
        builder.append("\t</head>\n");
        builder.append("\t<body>\n");
        builder.append(getHtmlTable(precedence));
        builder.append("\t</body>\n");
        builder.append("</html>\n");

        return builder.toString();
    }

    private static String getHtmlTable(Precedence precedence) {
        StringBuilder builder = new StringBuilder();

        builder.append("<table class=\"table\">\n");
        builder.append("    <tr class=\"table-header\">\n");

        // first col
        builder.append("        <th class=\"first_col\">");
        builder.append("\\");
        builder.append("</th>\n");

        Set<String> keys = precedence.getPrecedence().keySet();

        // column names
        for (String key : keys) {
            builder.append("        <th class=\"header__item\">");
            builder.append(key);
            builder.append("</th>\n");
        }
        builder.append("    </tr>\n");
        Object[] keysArray = keys.toArray();
        Map<String, Connection> precedenceData = precedence.getPrecedence();
        // data
        for (int y = 0; y < keysArray.length; y++) {
            builder.append("    <tr class=\"table-row\">\n");
            // first col
            builder.append("        <td class=\"first_col\">");
            builder.append(keysArray[y]);
            builder.append("</td>\n");
            // data
            for (int x = 0; x < keysArray.length; x++) {
                Map<String, Relation> connectionData = precedenceData.get(keysArray[y]).getConnection();
                if (connectionData.containsKey(keysArray[x])) {
                    if (connectionData.get(keysArray[x]).hasConflict()) {
                        builder.append("        <td class=\"conflict-cell\">");
                    }
                    else {
                        builder.append("        <td class=\"table-data\">");
                    }
                    for (RelationType relation : connectionData.get(keysArray[x]).getRelations()) {
                        switch (relation) {
                            case LESS:
                                builder.append("<");
                                break;
                            case EQUAL:
                                builder.append("=");
                                break;
                            case MORE:
                                builder.append(">");
                                break;
                        }
                    }
                }
                else {
                    builder.append("\t\t<td>");
                }
                builder.append("</td>\n");
            }
            builder.append("    </tr>\n");
        }
        builder.append("</table>");

        return builder.toString();
    }

    public static void createHtmlFile(String htmlCode) {
        File fileHTML = new File("res/precedence_table.html");
        try {
            FileWriter writer = new FileWriter(fileHTML);
            writer.write(htmlCode);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Grammar grammar = GrammarParser.parse("res/grammar.json");
        PrecedenceHandler precedenceHandler = new PrecedenceHandler(grammar);
        Precedence precedence = precedenceHandler.computePrecedences();
        String htmlCode = getHtmlCode(precedence);
        createHtmlFile(htmlCode);
        System.out.println("Hi");
    }
}
