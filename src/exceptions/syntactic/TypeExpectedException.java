package exceptions.syntactic;

public class TypeExpectedException extends TokenExpectedException {
    public TypeExpectedException(int line, String found) {
        super(line, "Type", found);
    }
}
