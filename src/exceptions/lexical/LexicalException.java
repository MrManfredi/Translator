package exceptions.lexical;

import java.util.ArrayList;
import java.util.List;

public abstract class LexicalException {

    private String message;

    LexicalException(int line, String message)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Lexical exception in line: ").append(line).append("! ").append(message);
        this.message = sb.toString();
    }

    public String getMessage() {
        return message;
    }
}
