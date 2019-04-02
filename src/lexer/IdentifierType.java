package lexer;

import static lexer.LexicalAnalyzer.getLexemeTypeIndex;

public enum IdentifierType {
    INT(getLexemeTypeIndex("int"));
    private int value;
    IdentifierType(int value)
    {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
