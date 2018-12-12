package exceptions.lexical;

public class UnknownSymbolException extends LexicalException{

    public UnknownSymbolException(char smb, int line) {
        super( line, "Unknown symbol \"" + smb +"\"!");
    }
}
