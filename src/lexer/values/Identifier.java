package lexer.values;

public class Identifier implements NameableValue{
    private String name;
    private int index;
    private String type;

    public Identifier(String name, int index, String type) {
        this.name = name;
        this.index = index;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }
}
