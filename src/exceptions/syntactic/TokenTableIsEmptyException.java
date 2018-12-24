package exceptions.syntactic;

public class TokenTableIsEmptyException extends SyntaxException{

    public TokenTableIsEmptyException() {
        super(-1, "Token table is empty!");
    }
}
