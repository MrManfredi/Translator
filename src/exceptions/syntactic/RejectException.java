package exceptions.syntactic;

public class RejectException extends SyntaxException {
    public RejectException() {
        super(-1, "The syntactic analysis was rejected because the lexical analysis ended with errors.");
    }
}
