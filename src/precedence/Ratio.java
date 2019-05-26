package precedence;

import java.util.ArrayList;
import java.util.List;

public class Ratio {
    private boolean less;
    private boolean equal;
    private boolean more;

    public Ratio() {
    }

    public Ratio(RatioType ratio) {
        setRatio(ratio);
    }

    public void setRatio(RatioType ratio) {
        switch (ratio)
        {
            case LESS:
                less = true;
                break;
            case EQUAL:
                equal = true;
                break;
            case MORE:
                more = true;
                break;
        }
    }

    public boolean hasConflict(){
        return getRelations().size() < 2 ? false : true;
    }

    public List<RatioType> getRelations() {
        List<RatioType> list = new ArrayList<>();
        if (less) {
            list.add(RatioType.LESS);
        }
        if (equal) {
            list.add(RatioType.EQUAL);
        }
        if (more) {
            list.add(RatioType.MORE);
        }
        return list;
    }

    public RatioType getRatio() {
        if (!hasConflict()) {
            if (less) {
                return RatioType.LESS;
            }
            if (equal) {
                return RatioType.EQUAL;
            }
            if (more) {
                return RatioType.MORE;
            }
        }
        return null;
    }
}
