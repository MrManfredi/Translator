package lexer;

public enum LexemeType{
    IDENT(100, "Identifier"),
    CONST(101, "Constant"),
    LABEL(102, "Label");

    private int value;
    private String name;

    LexemeType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {return name;}
}