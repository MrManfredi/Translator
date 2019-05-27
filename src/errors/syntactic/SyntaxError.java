package errors.syntactic;

import errors.Error;

public abstract class SyntaxError extends Error {
    SyntaxError(Integer line, String message)
    {
        super(line, "Syntactic", message);
    }
}
