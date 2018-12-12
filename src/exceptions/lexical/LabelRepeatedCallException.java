package exceptions.lexical;

public class LabelRepeatedCallException extends LexicalException{
    public LabelRepeatedCallException(int line, String label) {
        super(line, "The \"" + label +"\" label was re-called! Each label can only be called from one place.");
    }
}
