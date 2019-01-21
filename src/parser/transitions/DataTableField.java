package parser.transitions;

import java.util.ArrayList;

public class DataTableField {
    private int state;
    private String label;
    private ArrayList<Integer> stack;

    public DataTableField(int state, String label, ArrayList<Integer> stack) {
        this.state = state;
        this.label = label;
        this.stack = stack;
    }

    public int getState() {
        return state;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<Integer> getStack() {
        return stack;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("DTF{state: ").append(state).append(", ")
                .append("label: ").append(label).append(", ")
                .append("stack: ").append(stack).append("}")
                .toString();
    }
}