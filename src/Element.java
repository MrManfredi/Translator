public class Element {
    int line;
    String text;
    int code;
    int indexInTable;

    public Element(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Element(int line, String text, int code, int indexInTable) {
        this.line = line;
        this.text = text;
        this.code = code;
        this.indexInTable = indexInTable;
    }
}
