package exceptions.syntactic;

public abstract class SyntaxException {
    private String message;

    SyntaxException(int line, String message)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Syntactic exception");
        if (line != -1)
        {
            sb.append(" in line: ").append(line);
        }
        sb.append(". ").append(message);
        this.message = sb.toString();
    }

    public String getMessage() {
        return message;
    }
}
