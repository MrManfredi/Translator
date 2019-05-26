package parser;

import precedence.RatioType;

public class Unit {
    private String name;
    private RatioType ratio;

    public Unit(String name) {
        this.name = name;
    }

    public Unit(String name, RatioType ratio) {
        this.name = name;
        this.ratio = ratio;
    }

    public String getName() {
        return name;
    }

    public RatioType getRatio() {
        return ratio;
    }

    public void setRatio(RatioType ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return name;
    }
}
