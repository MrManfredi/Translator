package constants;

public enum Statements {
    IF ("if"),
    GOTO ("goto"),
    REPEAT ("repeat"),
    UNTIL ("until"),
    IN ("in"),
    OUT ("out"),
    NEWLINE ("¶"),
    MINUS ("-"),
    UNARY_MINUS ("@")
    ;
    private String name;

    Statements(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
