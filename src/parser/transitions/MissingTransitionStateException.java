package parser.transitions;

import java.io.IOException;

public class MissingTransitionStateException extends IOException {
    private int state;
    private String label;
    private String message;

    public MissingTransitionStateException(int state, String label) {
        this.state = state;
        this.label = label;
        this.message = "Tag <transition label=\"" + label + "\"> does not have <goto> value in <state name=" + state + "\">";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
