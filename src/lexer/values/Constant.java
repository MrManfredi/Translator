package lexer.values;

public class Constant implements NameableValue{
    private String name;
    private int index;

    public Constant(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
