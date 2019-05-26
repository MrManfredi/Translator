package parser;

import grammar.RightSide;
import lexer.Lexeme;
import precedence.RatioType;

import java.util.*;

public class ParsingTableRow {
    private Integer id;
    private List<Unit> stack;
    private RatioType ratio;
    private List<Lexeme> sourceSequence;
    private RightSide basis;

    public ParsingTableRow(Integer id, Stack<Unit> stack, RatioType ratio, Queue<Lexeme> sourceSequence, RightSide basis) {
        this.id = id;
        this.stack = new ArrayList<>(stack);
        this.ratio = ratio;
        this.sourceSequence = new ArrayList<>(sourceSequence);
        this.basis = basis;
    }

    public Integer getId() {
        return id;
    }

    public List<Unit> getStack() {
        return stack;
    }

    public RatioType getRatio() {
        return ratio;
    }

    public List<Lexeme> getSourceSequence() {
        return sourceSequence;
    }

    public RightSide getBasis() {
        return basis;
    }
}
