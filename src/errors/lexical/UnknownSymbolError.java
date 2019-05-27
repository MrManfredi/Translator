package errors.lexical;

public class UnknownSymbolError extends LexicalError {

    public UnknownSymbolError(char smb, int line) {
        super( line, "Unknown symbol \"" + smb +"\"!");
    }
}
