public class ErrorStatistics {
    private static ErrorStatistics ourInstance = new ErrorStatistics();
    private static String message;
    public static ErrorStatistics getInstance() {
        return ourInstance;
    }
    public static void addMessage(String message)
    {
        ourInstance.message += message + "\n";
    }
    public static String getMessage()
    {
        return message;
    }
    private ErrorStatistics() {
        message = new String();
    }
}
