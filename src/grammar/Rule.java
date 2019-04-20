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

    public void show() {
        System.out.print(leftSide + " = ");
        for (int i = 0; i < rightSides.size(); i++) {
            rightSides.get(i).show();
            if (i + 1 < rightSides.size()) {
                System.out.print(" | ");
            }
            else {
                System.out.println(".");
            }
        }
    }

    public String getLeftSide() {
        return leftSide;
    }

    public List<RightSide> getRightSides() {
        return rightSides;
    }
}
