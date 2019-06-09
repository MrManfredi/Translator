package constants;

import java.util.Arrays;

public enum Parenthesis {
    ROUND ("(", ")"),
    BRACE ("{", "}")
    ;
    private String opening;
    private String ending;

    Parenthesis(String opening, String ending) {
        this.opening = opening;
        this.ending = ending;
    }

    public static boolean isOpening(String parenthesis) {
        if (parenthesis.length() > 1){
            return false;
        }
        return Arrays.stream(Parenthesis.values()).anyMatch(e -> e.opening.equals(parenthesis));
    }

    public static boolean isEnding(String parenthesis) {
        if (parenthesis.length() > 1){
            return false;
        }
        return Arrays.stream(Parenthesis.values()).anyMatch(e -> e.ending.equals(parenthesis));
    }

    public static String getOpening(String ending) {
        if (isEnding(ending)) {
            for (Parenthesis temp : Parenthesis.values()) {
                if (temp.ending.equals(ending))
                    return temp.opening;
            }
        }
        return null;
    }
}
