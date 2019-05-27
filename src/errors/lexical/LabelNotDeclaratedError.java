package errors.lexical;

public class LabelNotDeclaratedError extends LexicalError {
    public LabelNotDeclaratedError(int line, String label) {
        super(line, "Label \"" + label + "\" was not declared!");
    }
}
