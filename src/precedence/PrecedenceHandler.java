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

    public static Precedence computePrecedences(Grammar grammar) {
        Precedence precedence = new Precedence();
        List<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            List<RightSide> rightSides = rule.getRightSides();
            for (RightSide rightSide : rightSides) {
                List<String> words = rightSide.getWords();
                if (words.contains("")) {
                    System.out.println("lol");
                }
                for (int i = 0; i < words.size() - 1; i++) {
                    precedence.addPrecedence(words.get(i), words.get(i + 1), RelationType.EQUAL);

                    List<String> firstPlusRight = new ArrayList<>();
                    if (grammar.getNonterminals().contains(words.get(i + 1)))
                    {
                        firstPlusRight.add(words.get(i + 1));

                        int j = 0;
                        do {
                            if (grammar.getNonterminals().contains(firstPlusRight.get(j))) {
                                for (RightSide right : grammar.getRule(firstPlusRight.get(j)).getRightSides()) {
                                    String temp = right.getWords().get(0);      // the first word of each right side
                                    if (!firstPlusRight.contains(temp)) {
                                        firstPlusRight.add(temp);
                                    }
                                }
                            }
                            j++;
                        } while (j < firstPlusRight.size());

                        for (String word : firstPlusRight) {
                            precedence.addPrecedence(words.get(i), word, RelationType.LESS);
                        }
                    }

                    if (grammar.getNonterminals().contains(words.get(i))) {
                        List<String> lastPlusLeft = new ArrayList<>();
                        lastPlusLeft.add(words.get(i));

                        int j = 0;
                        do {
                            if (grammar.getNonterminals().contains(lastPlusLeft.get(j))) {
                                for (RightSide right : grammar.getRule(lastPlusLeft.get(j)).getRightSides()) {
                                    String temp = right.getWords().get(right.getWords().size() - 1);      // the last word of each right side
                                    if (!lastPlusLeft.contains(temp)) {
                                        lastPlusLeft.add(temp);
                                    }
                                }
                            }
                            j++;
                        } while (j < lastPlusLeft.size());

                        for (String word : lastPlusLeft) {
                            precedence.addPrecedence(word, words.get(i + 1), RelationType.MORE);
                        }

                        if (!firstPlusRight.isEmpty()) {
                            for (String lastLeft : lastPlusLeft) {
                                for (String firstRight : firstPlusRight) {
                                    precedence.addPrecedence(lastLeft, firstRight, RelationType.MORE);
                                }
                            }
                        }
                    }
                }
            }
        }
        return precedence;
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
        Precedence precedence = PrecedenceHandler.computePrecedences(grammar);
        String htmlCode = getHtmlCode(precedence);
        createHtmlFile(htmlCode);
        System.out.println("Hi");
    }
}
