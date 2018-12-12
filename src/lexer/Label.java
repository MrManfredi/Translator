package lexer;

public class Label {

    private String label;
    private int index;
    private int lineFrom;
    private int lineTo;

    public Label(String label, int index) {
        this.label = label;
        this.index = index;
        lineFrom = -1;
        lineTo = -1;
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }

    public int getLineFrom() {
        return lineFrom;
    }

    public int getLineTo() {
        return lineTo;
    }

    public void setLineFrom(int lineFrom) {
        this.lineFrom = lineFrom;
    }

    public void setLineTo(int lineTo) {
        this.lineTo = lineTo;
    }
}
