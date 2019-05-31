package constants;

public enum IdentifierType {
    INT(Keywords.getInstance().getIndex("int"));
    private int value;
    private final static String name = "Type";

    IdentifierType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
