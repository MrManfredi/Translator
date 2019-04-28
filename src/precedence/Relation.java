package precedence;

import java.util.ArrayList;
import java.util.List;

public class Relation {
    private boolean less;
    private boolean equal;
    private boolean more;

    public Relation() {
    }

    public Relation(RelationType relation) {
        setRelation(relation);
    }

    public void setRelation(RelationType relation) {
        switch (relation)
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

    public List<RelationType> getRelations() {
        List<RelationType> list = new ArrayList<>();
        if (less) {
            list.add(RelationType.LESS);
        }
        if (equal) {
            list.add(RelationType.EQUAL);
        }
        if (more) {
            list.add(RelationType.MORE);
        }
        return list;
    }
}
