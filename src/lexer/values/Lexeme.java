package lexer.values;

import constants.LexemeType;

public class Lexeme implements NameableValue{
    private int id;
    private int line;
    private String name;
    private int code;
    private Integer specialCode;

    public Lexeme(int id, int line, String name, int code, Integer specialCode) {
        this.id = id;
        this.line = line;
        this.name = name;
        this.code = code;
        this.specialCode = specialCode;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public int getSpecialCode() {
        return specialCode;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSpCodeIdn() {
        return (code == LexemeType.IDENT.getValue()) ? specialCode : null;
    }

    public Integer getSpCodeCon() {
        return (code == LexemeType.CONST.getValue()) ? specialCode : null;
    }

    public Integer getSpCodeLbl() {
        return (code == LexemeType.LABEL.getValue()) ? specialCode : null;
    }

    public boolean isIdentifier() {
        return code == LexemeType.IDENT.getValue();
    }

    public boolean isConstant() {
        return code == LexemeType.CONST.getValue();
    }

    public boolean isLabel() {
        return code == LexemeType.LABEL.getValue();
    }

    @Override
    public String toString() {
        return name;
    }
}
