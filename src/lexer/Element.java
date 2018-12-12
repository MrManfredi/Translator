package lexer;

public class Element {
    private int line;
    private String text;
    private int code;
    private int indexInTable;

    public Element(int line, String text, int code, int indexInTable) {
        this.line = line;
        this.text = text;
        this.code = code;
        this.indexInTable = indexInTable;
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

    public void setText(String text) {
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getIndexInTable() {
        return indexInTable;
    }

    public void setIndexInTable(int indexInTable) {
        this.indexInTable = indexInTable;
    }
}
