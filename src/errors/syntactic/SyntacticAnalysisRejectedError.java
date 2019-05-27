package errors.syntactic;

public class SyntacticAnalysisRejectedError extends SyntaxError{
    public SyntacticAnalysisRejectedError(String message) {
        super(null, "Syntactic analysis was rejected. " + message);
    }
}
