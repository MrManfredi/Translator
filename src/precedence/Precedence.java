package precedence;

import grammar.Grammar;
import grammar.RightSide;
import grammar.Rule;

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

    public void determinePrecedence(Grammar grammar) {
        Precedence precedences = new Precedence();
        List<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            List<RightSide> rightSides = rule.getRightSides();
            for (RightSide rightSide : rightSides) {
                List<String> words = rightSide.getWords();
                for (int i = 0; i < words.size() - 1; i++) {
                    precedences.addPrecedence(words.get(i), words.get(i + 1), RelationType.EQUAL);
                    if (grammar.getNonterminals().contains(words.get(i + 1)))
                    {
                        precedences.addPrecedence(words.get(i), words.get(i + 1), RelationType.LESS);
                        precedences.addPrecedence(words.get(i), rules.get(grammar.getNonterminals().indexOf(words.get(i + 1))).getRightSides().get(0).getWords().get(0), RelationType.LESS); // too bad
                        // TODO
                    }
                }
            }
        }
    }
}
