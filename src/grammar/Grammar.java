package grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grammar {
    private List<Rule> rules;                   // grammar
    private List<String> nonterminals;
    private HashMap<RightSide, String> reverseRules;    // reverse grammar

    Grammar() {
        rules = new ArrayList<>();
        nonterminals = new ArrayList<>();
        reverseRules = new HashMap<>();
    }

    void addRule(Rule rule) {
        rules.add(rule);
        nonterminals.add(rule.getLeftSide());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Rule rule : rules) {
            builder.append(rule.toString());
        }
        return builder.toString();
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Rule getRule(String leftSide) {
        for (Rule rule : rules) {
            if (rule.getLeftSide().equals(leftSide)) {
                return rule;
            }
        }
        return null;
    }

    public List<String> getNonterminals() {
        return nonterminals;
    }

    public void addReverseRule(RightSide rightSide, String leftSide) {
        reverseRules.put(rightSide, leftSide);
    }

    public HashMap<RightSide, String> getReverseRules() {
        return reverseRules;
    }
}
