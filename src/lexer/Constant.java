package lexer;

public class Constant {
    private String constant;
    private int index;

    public Constant(String constant, int index) {
        this.constant = constant;
        this.index = index;
    }

    public String getConstant() {
        return constant;
    }

    public int getIndex() {
        return index;
    }
}
