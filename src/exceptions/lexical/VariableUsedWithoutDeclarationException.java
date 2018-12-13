package exceptions.lexical;

public class VariableUsedWithoutDeclarationException extends LexicalException{
    public VariableUsedWithoutDeclarationException(int line, String variable) {
        super(line, "Variable \"" + variable + "\" used without declaration!");
    }
}
