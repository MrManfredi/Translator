package errors.lexical;

public class LabelRepeatedCallError extends LexicalError {
    public LabelRepeatedCallError(int line, String label) {
        super(line, "The \"" + label +"\" label was re-called! Each label can only be called from one place.");
    }
}
