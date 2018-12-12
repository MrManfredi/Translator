package exceptions.lexical;

public class ExpectedSymbolException extends LexicalException{
    public ExpectedSymbolException(int line, String after, String expected) {
        super(line, "After \"" + after + "\" expected \"" + expected + "\"!");
    }
}
