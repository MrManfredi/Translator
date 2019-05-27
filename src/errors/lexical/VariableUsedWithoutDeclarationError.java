package errors.lexical;

public class VariableUsedWithoutDeclarationError extends LexicalError {
    public VariableUsedWithoutDeclarationError(int line, String variable) {
        super(line, "Variable \"" + variable + "\" used without declaration!");
    }
}
