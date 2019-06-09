package constants;

public enum Transitions {
    JNE("JNE"),
    JMP("JMP")
    ;

    private String name;

    Transitions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
