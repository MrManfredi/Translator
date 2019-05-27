package errors.lexical;

import errors.Error;

public abstract class LexicalError extends Error {
    LexicalError(int line, String message)
    {
        super(line, "Lexical", message);
    }
}
