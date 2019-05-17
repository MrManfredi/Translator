package grammar;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
    private List<Rule> rules;
    private List<String> nonterminals;

    Grammar() {
        rules = new ArrayList<>();
        nonterminals = new ArrayList<>();
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
}
