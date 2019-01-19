package parser.transitions;

import java.io.IOException;

public class AttributeNotFoundException extends IOException {
    private String tag;
    private String attribute;
    private String message;

    public AttributeNotFoundException(String tag, String attribute) {
        this.tag = tag;
        this.attribute = attribute;
        this.message = "Tag <" + tag + "> does not have attribute '" + attribute + "'";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
