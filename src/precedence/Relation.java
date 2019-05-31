package precedence;

import java.util.HashMap;
import java.util.Map;

public class Relation {
    private Map<String, Ratio> relation;

    Relation(String rightWord, RatioType relation) {
        this.relation = new HashMap<>();
        this.relation.put(rightWord, new Ratio(relation));
    }

    void addRelation(String rightWord, RatioType ratio) {
        if (this.relation.containsKey(rightWord)){
            this.relation.get(rightWord).setRatio(ratio);
        }
        else {
            this.relation.put(rightWord, new Ratio(ratio));
        }
    }

    public Map<String, Ratio> getRelation() {
        return relation;
    }
}
