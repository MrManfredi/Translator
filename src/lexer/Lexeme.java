package lexer;

public class Lexeme {
    private static int id;
    private int line;
    private String text;
    private int code;
    private int specialCode;

    public Lexeme(int line, String text, int code, int specialCode) {
        this.line = line;
        this.text = text;
        this.code = code;
        this.specialCode = specialCode;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }

    public int getSpecialCode() {
        return specialCode;
    }

    public Integer getId() {
        return id++;
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

    public static void setId(int id) {
        Lexeme.id = id;
    }
}
