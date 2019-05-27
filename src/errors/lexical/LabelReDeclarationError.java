package errors.lexical;

public class LabelReDeclarationError extends LexicalError {
    public LabelReDeclarationError(int line, String label) {
        super(line, "Label \"" + label + "\" was re-declared!");
    }
}
