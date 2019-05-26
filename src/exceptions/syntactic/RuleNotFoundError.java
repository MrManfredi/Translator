package exceptions.syntactic;

import grammar.RightSide;

public class RuleNotFoundError extends SyntaxError{
    public RuleNotFoundError(Integer line, RightSide basis) {
        super(line, "Rule with \"" + basis.toString() + "\" basis not found.");
    }
}
