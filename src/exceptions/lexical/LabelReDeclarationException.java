package exceptions.lexical;

public class LabelReDeclarationException extends LexicalException {
    public LabelReDeclarationException(int line, String label) {
        super(line, "Label \"" + label + "\" was re-declared!");
    }
}
