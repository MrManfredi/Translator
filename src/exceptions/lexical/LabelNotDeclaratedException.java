package exceptions.lexical;

public class LabelNotDeclaratedException extends LexicalException {
    public LabelNotDeclaratedException(int line, String label) {
        super(line, "Label \"" + label + "\" was not declared!");
    }
}
