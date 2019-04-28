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

    void show() {
        for (Rule rule : rules) {
            rule.show();
        }
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
