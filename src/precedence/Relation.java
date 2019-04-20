package precedence;

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
}
