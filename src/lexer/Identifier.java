package lexer;

public class Identifier {
    private String identifier;
    private int index;
    private String type;

    public Identifier(String identifier, int index, String type) {
        this.identifier = identifier;
        this.index = index;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }
}
