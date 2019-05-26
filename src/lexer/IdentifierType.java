package lexer;

import static lexer.LexicalAnalyzer.getLexemeTypeIndex;

public enum IdentifierType {
    INT(getLexemeTypeIndex("int"), "Type");
    private int value;
    private String name;

    IdentifierType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
