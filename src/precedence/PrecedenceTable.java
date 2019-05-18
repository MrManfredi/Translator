package precedence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class PrecedenceTable {

    private static String getHtmlCode(PrecedenceStorage precedenceStorage) {
        StringBuilder builder = new StringBuilder();

        builder.append("<html>\n");
        builder.append("\t<head>\n");
        builder.append("\t<title>Precedence table</title>\n");
        builder.append("\t<link rel=\"stylesheet\" type=\"text/css\" href=\"table_style.css\">\n");
        builder.append("\t</head>\n");
        builder.append("\t<body>\n");
        builder.append(generateHtmlTable(precedenceStorage));
        builder.append("\t</body>\n");
        builder.append("</html>\n");

        return builder.toString();
    }

    private static String generateHtmlTable(PrecedenceStorage precedenceStorage) {
        StringBuilder builder = new StringBuilder();

        builder.append("<table class=\"table\">\n");
        builder.append("    <tr class=\"table-header\">\n");

        // first col
        builder.append("        <th class=\"first_col\">");
        builder.append("\\");
        builder.append("</th>\n");

        Set<String> keys = precedenceStorage.getPrecedenceStorage().keySet();

        // column names
        for (String key : keys) {
            builder.append("        <th class=\"header__item\">");
            builder.append(key);
            builder.append("</th>\n");
        }
        builder.append("    </tr>\n");
        Object[] keysArray = keys.toArray();
        Map<String, Relation> precedenceData = precedenceStorage.getPrecedenceStorage();
        // data
        for (int y = 0; y < keysArray.length; y++) {
            builder.append("    <tr class=\"table-row\">\n");
            // first col
            builder.append("        <td class=\"first_col\">");
            builder.append(keysArray[y]);
            builder.append("</td>\n");
            // data
            for (int x = 0; x < keysArray.length; x++) {
                Map<String, Ratio> connectionData = precedenceData.get(keysArray[y]).getRelation();
                if (connectionData.containsKey(keysArray[x])) {
                    if (connectionData.get(keysArray[x]).hasConflict()) {
                        builder.append("        <td class=\"conflict-cell\">");
                    }
                    else {
                        builder.append("        <td class=\"table-data\">");
                    }
                    for (RatioType relation : connectionData.get(keysArray[x]).getRelations()) {
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

    public static void createHtmlFile(PrecedenceStorage precedenceStorage, String pathname) {
        File fileHTML = new File(pathname);
        try {
            FileWriter writer = new FileWriter(fileHTML);
            writer.write(getHtmlCode(precedenceStorage));
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
