package constants;

import java.util.HashMap;

public class GrammarKeywords extends HashMap<String, GrammarKeywords.Value>{

    private static GrammarKeywords instance = new GrammarKeywords();


    public static GrammarKeywords getInstance() {
        return instance;
    }

    private GrammarKeywords() {
        int index = 1;
        put("int", new Value(index++, null));
        put("{", new Value(index++, 0));
        put("(", new Value(index++, 0));
        put(Statements.IF.getName(), new Value(index++, 0));
        put(Statements.REPEAT.getName(), new Value(index++, 0));
        put(Statements.UNTIL.getName(), new Value(index++, 1));
        put(")", new Value(index++, 1));
        put("}", new Value(index++, 1));
        put(Statements.NEWLINE.getName(), new Value(index++, 1));
        put(Statements.GOTO.getName(), new Value(index++, 1));
        put("?", new Value(index++, 2));
        put(":", new Value(index++, 2));
        put("=", new Value(index++, 2));
        put(">>", new Value(index++, 2));
        put("<<", new Value(index++, 2));
        put(Statements.IN.getName(), new Value(index++, 2));
        put(Statements.OUT.getName(), new Value(index++, 2));
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
        put(Statements.MINUS.getName(), new Value(index++, 7));
        put("/", new Value(index++, 8));
        put("*", new Value(index++, 8));
        put(Statements.UNARY_MINUS.getName(), new Value(null, 8));
        put(",", new Value(index, null));

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
