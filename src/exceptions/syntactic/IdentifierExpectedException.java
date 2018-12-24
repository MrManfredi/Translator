package exceptions.syntactic;

public class IdentifierExpectedException extends TokenExpectedException {
    public IdentifierExpectedException(int line , String found) {
        super(line, "Identifier", found);
    }
}
