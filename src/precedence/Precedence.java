package precedence;

import grammar.Grammar;
import grammar.RightSide;
import grammar.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Precedence {
    private Map<String, Connection> precedence;

    public Precedence() {
        precedence = new HashMap<>();
    }

    public void addPrecedence(String leftWord, String rightWord, RelationType relation) {
        if (precedence.containsKey(leftWord)) {
            precedence.get(leftWord).addConnection(rightWord, relation);
        }
        else {
            precedence.put(leftWord, new Connection(rightWord, relation));
        }
    }

    public Map<String, Connection> getPrecedence() {
        return precedence;
    }
}
