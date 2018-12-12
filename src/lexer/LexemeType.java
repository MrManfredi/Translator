package lexer;

public enum LexemeType{
    IDENT(100),
    CONST(101),
    LABEL(102);

    private int value;

    LexemeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}