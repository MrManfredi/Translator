package errors.lexical;

public class ExpectedSymbolError extends LexicalError {
    public ExpectedSymbolError(int line, String after, String expected) {
        super(line, "After \"" + after + "\" expected \"" + expected + "\"!");
    }
}
