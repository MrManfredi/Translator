package exceptions.syntactic;

public abstract class SyntaxError {
    private String message;

    SyntaxError(Integer line, String message)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Syntactic error");
        if (line != null)
        {
            builder.append(" in line: ").append(line);
        }
        builder.append(". ").append(message);
        this.message = builder.toString();
    }

    public String getMessage() {
        return message;
    }
}
