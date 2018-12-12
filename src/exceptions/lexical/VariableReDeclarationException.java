package exceptions.lexical;

public class VariableReDeclarationException extends LexicalException {

    public VariableReDeclarationException(int line, String variable) {
        super(line, "Variable \"" + variable + "\" was re-declared!");
    }
}
