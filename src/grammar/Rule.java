package grammar;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private String leftSide;
    private List<RightSide> rightSides;

    public Rule(String leftSide) {
        this.leftSide = leftSide;
        rightSides = new ArrayList<>();
    }

    public void addCase(RightSide rightSide) {
        rightSides.add(rightSide);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(leftSide).append(" = ");
        for (int i = 0; i < rightSides.size(); i++) {
            builder.append(rightSides.get(i).toString());
            if (i + 1 < rightSides.size()) {
                builder.append("  |  ");
            }
            else {
                builder.append(".\n");
            }
        }
        return builder.toString();
    }

    public String getLeftSide() {
        return leftSide;
    }

    public List<RightSide> getRightSides() {
        return rightSides;
    }
}
