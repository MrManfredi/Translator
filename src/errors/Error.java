package errors;

public abstract class Error {
    private String message;

    public Error(Integer line, String errorType, String message)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(errorType).append(" error");
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
