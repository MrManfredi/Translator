package exceptions.syntactic;

public class NoRatioBetweenTokensError extends SyntaxError {
    public NoRatioBetweenTokensError(Integer line, String leftWord, String rightWord) {
        super(line, "No ratio between tokens \"" + leftWord + "\" and \"" + rightWord + "\".");
    }
}
