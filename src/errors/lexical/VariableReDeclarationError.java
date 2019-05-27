package errors.lexical;

public class VariableReDeclarationError extends LexicalError {

    public VariableReDeclarationError(int line, String variable) {
        super(line, "Variable \"" + variable + "\" was re-declared!");
    }
}
