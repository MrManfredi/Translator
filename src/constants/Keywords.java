package constants;

import java.util.HashMap;

public class Keywords extends HashMap<String, Keywords.Value>{

    private static Keywords instance = new Keywords();


    public static Keywords getInstance() {
        return instance;
    }

    private Keywords() {
        int index = 1;
        put("{", new Value(index++, null));
        put("}", new Value(index++, null));
        put("int", new Value(index++, null));
        put("(", new Value(index++, 0));
        put(")", new Value(index++, 1));
        put("=", new Value(index++, 2));
        put("or", new Value(index++, 3));
        put("and", new Value(index++, 4));
        put("not", new Value(index++, 5));
        put("<", new Value(index++, 6));
        put("<=", new Value(index++, 6));
        put("==", new Value(index++, 6));
        put("!=", new Value(index++, 6));
        put(">=", new Value(index++, 6));
        put(">", new Value(index++, 6));
        put("+", new Value(index++, 7));
        put("-", new Value(index++, 7));
        put("/", new Value(index++, 8));
        put("*", new Value(index++, 8));
        put("@", new Value(null, 8));  // unary minus
        put(">>", new Value(index++, null));
        put("<<", new Value(index++, null));
        put("in", new Value(index++, null));
        put("out", new Value(index++, null));
        put("repeat", new Value(index++, null));
        put("until", new Value(index++, null));
        put("if", new Value(index++, null));
        put("goto", new Value(index++, null));
        put(",", new Value(index++, null));
        put("?", new Value(index++, null));
        put(":", new Value(index++, null));
        put("\n", new Value(index, null));

    }

    public Integer getIndex(String key) {
        return get(key).index;
    }

    public Integer getPriority(String key) {
        if (!containsKey(key)) return null;
        return get(key).priority;
    }

    class Value {
        private Integer index;
        private Integer priority;

        public Value(Integer index, Integer priority) {
            this.index = index;
            this.priority = priority;
        }
    }
}
