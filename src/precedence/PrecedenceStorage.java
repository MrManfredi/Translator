package precedence;

import java.util.HashMap;
import java.util.Map;

public class PrecedenceStorage {
    private Map<String, Relation> precedenceStorage;

    public PrecedenceStorage() {
        precedenceStorage = new HashMap<>();
    }

    public void addСorrelation(String leftWord, String rightWord, RatioType ratio) {
        if (precedenceStorage.containsKey(leftWord)) {
            precedenceStorage.get(leftWord).addRelation(rightWord, ratio);
        }
        else {
            precedenceStorage.put(leftWord, new Relation(rightWord, ratio));
        }
    }

    public Map<String, Relation> getPrecedenceStorage() {
        return precedenceStorage;
    }
}
