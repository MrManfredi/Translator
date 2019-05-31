package lexer.values;

public class Label implements NameableValue{

    private String name;
    private int index;
    private int lineFrom;
    private int lineTo;

    public Label(String name, int index) {
        this.name = name;
        this.index = index;
        lineFrom = -1;
        lineTo = -1;
    }

    public String getName() {
        return name;
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
