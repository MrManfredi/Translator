package lexer;

public enum IdentifierType {
    INT(26);
    private int value;
    IdentifierType(int value)
    {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
