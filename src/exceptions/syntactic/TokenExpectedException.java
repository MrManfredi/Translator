package exceptions.syntactic;

public class TokenExpectedException extends SyntaxException {
    public TokenExpectedException(int line , String expected, String found) {
        super(line, expected + " expected. But '" + found + "' has been found.");
    }
}
