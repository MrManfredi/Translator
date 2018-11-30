
import java.util.ArrayList;

public class Parser {
    static final char[] op = {'<', '>', '=', '/', '*', '+',  '-',  '(', ')', '{', '}', '%', ','};
    static private ArrayList<String> oper;
    static private ArrayList<Element> tokenTable;
    static private ArrayList<Element> outTokenTable;
    static private ArrayList<Element> identTable;
    static private ArrayList<Element> constTable;
    static private ArrayList<Element> labelTable;
    private static int line;
    private static int indexInConstTable;
    private static int indexInIdentTable;
    private static boolean error;
    Parser() {
        oper = new ArrayList<String>();
        oper.add("<<");
        oper.add(">>");
        oper.add("<=");
        oper.add(">=");
        oper.add("!=");
        oper.add("==");
        oper.add("*");
        oper.add("/");
        oper.add("+");
        oper.add("-");
        oper.add("<");
        oper.add(">");
        oper.add("=");
        oper.add(":");
        oper.add("?");
        oper.add("(");
        oper.add(")");
        oper.add("{");
        oper.add("}");
        oper.add("%");

        tokenTable = new ArrayList<Element>();
        outTokenTable = new ArrayList<Element>();
        identTable = new ArrayList<Element>();
        constTable = new ArrayList<Element>();
        labelTable = new ArrayList<Element>();
        tokenTable.add(new Element(0, "="));
        tokenTable.add(new Element(1, "if"));
        tokenTable.add(new Element(2, "else"));
        tokenTable.add(new Element(3, "repeat"));
        tokenTable.add(new Element(4, "until"));
        tokenTable.add(new Element(5, "goto"));
        tokenTable.add(new Element(6, "true"));
        tokenTable.add(new Element(7, "false"));
        tokenTable.add(new Element(8, "in"));
        tokenTable.add(new Element(9, "out"));
        tokenTable.add(new Element(10, "char"));
        tokenTable.add(new Element(11, "int"));
        tokenTable.add(new Element(12, "double"));
        tokenTable.add(new Element(13, "?"));
        tokenTable.add(new Element(14, ":"));
        tokenTable.add(new Element(15, "+"));
        tokenTable.add(new Element(16, "-"));
        tokenTable.add(new Element(17, "*"));
        tokenTable.add(new Element(18, "/"));
        tokenTable.add(new Element(19, "^"));
        tokenTable.add(new Element(20, ">>"));
        tokenTable.add(new Element(21, "<<"));
        tokenTable.add(new Element(22, "<"));
        tokenTable.add(new Element(23, "<="));
        tokenTable.add(new Element(24, "=="));
        tokenTable.add(new Element(25, "!="));
        tokenTable.add(new Element(26, ">"));
        tokenTable.add(new Element(27, ">="));
        tokenTable.add(new Element(28, ","));
        tokenTable.add(new Element(29, "("));
        tokenTable.add(new Element(30, ")"));
        tokenTable.add(new Element(31, "{"));
        tokenTable.add(new Element(32, "}"));
        tokenTable.add(new Element(33, "boolean"));
        tokenTable.add(new Element(100, "ident"));
        tokenTable.add(new Element(101, "const"));
        tokenTable.add(new Element(102, "label"));
        error = false;
        indexInConstTable = -1;
        indexInIdentTable = -1;
    }

    public static void parse(String code)
    {
        String lines[] = code.split("\n");

        for (int i = 0; i < lines.length; i++)
        {
            line = i;
            String temp;
            if (!lines[i].isEmpty() && isRozd(lines[i].charAt(lines[i].length() - 1)))
            {
                temp = lines[i] + "\n";
            }
            else
            {
                temp = lines[i];
            }
            for (String substring : temp.split(" ")) { //\\s
                {
                    parse(substring, 0);
                }
            }
        }
    }

    // In first call set indexInTable = 0
    private static void parse(String expression, int index)
    {
        if (expression.equals("") || expression.equals("\n"))
        {
            return;
        }
//        if (index < op.length)
        if (index < oper.size())
        {
            //if (expression.equals(op[index]))
            if (oper.contains(expression))
            {
                outTokenTable.add(new Element(line, expression, tokenTable.get(indexOfToken(expression)).code, -1));
                return;
            }
            String regex = "["+oper.get(index)+"]";
//            String lineSubstrings[] = expression.split(oper.get(index));
            String lineSubstrings[] = expression.split(regex);
            int temp = lineSubstrings.length;
            for (String substring : lineSubstrings) {
                parse(substring, index + 1);
                if (temp > 1)
                {
                    outTokenTable.add(new Element(line, oper.get(index), tokenTable.get(indexOfToken(oper.get(index))).code, -1));
                    temp--;
                }
            }
        }
        else if (indexOfToken(expression) == -1)
        {
            try {
                Integer.parseInt(expression);
                int ind = indexOfConst(expression);
                if (ind != -1)
                {
                    outTokenTable.add(new Element(line, expression, 101, ind));
                }
                else
                {
                    constTable.add(new Element(line, expression, 101, ++indexInConstTable));
                    outTokenTable.add(new Element(line, expression, 101, indexInConstTable));
                }

            }
            catch (Exception e)
            {

                if(Character.isLetter(expression.charAt(0)))
                {
                    boolean flag = true;
                    for (char ch : expression.toCharArray()) {
                        if (!Character.isLetter(ch) && !Character.isDigit(ch) && ch != '_')
                        {
                            ErrorStatistics.getInstance().addMessage("Error! Line: " + line + "Invalid identifier: " + expression);
                            error = true;
                            flag = false;
                        }
                    }
                    if (flag)
                    {
                        int ind = indexOfIdent(expression);
                        if (ind != -1)
                        {
                            outTokenTable.add(new Element(line, expression, 100, ind));
                        }
                        else
                        {
                            identTable.add(new Element(line, expression, 100, ++indexInIdentTable));
                            outTokenTable.add(new Element(line, expression, 100, indexInIdentTable));
                        }
                    }
                }
                else
                {
                    ErrorStatistics.getInstance().addMessage("Error! Line: " + line + "Invalid identifier: " + expression);
                    error = true;
                }
            }
        }
        else
        {
            outTokenTable.add(new Element(line, expression, tokenTable.get(indexOfToken(expression)).code, -1));
        }
    }



    public static void show()
    {
        System.out.printf(" № |         Token       | code | index |\n");
        for (Element temp : outTokenTable) {
            if (temp.indexInTable == -1)
            {
                System.out.printf("%3d | %-20s|%5d |       |\n", temp.line, temp.text, temp.code);
            }
            else
            {
                System.out.printf("%3d | %-20s|%5d | %6d|\n", temp.line, temp.text, temp.code, temp.indexInTable );
            }
        }

    }

    private static int indexOfToken(String expression)
    {
        for (Element temp : tokenTable) {
            if (temp.text.equals(expression))
            {
                return temp.code;
            }
        }
        return -1;
    }

    private static int indexOfIdent(String expression)
    {
        for (Element temp : identTable) {
            if (temp.text.equals(expression))
            {
                return temp.indexInTable;
            }
        }
        return -1;
    }

    private static boolean isRozd(char ch)
    {
        for (char temp : op)
        {
            if (ch == temp)
            {
                return true;
            }
        }
        return false;
    }

    private static int indexOfConst(String expression) {
        for (Element temp : constTable) {
            if (temp.text.equals(expression))
            {
                return temp.indexInTable;
            }
        }
        return -1;
    }
}