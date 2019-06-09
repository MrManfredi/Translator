package porn;

import constants.GrammarKeywords;
import constants.Parenthesis;
import grammar.Grammar;
import grammar.GrammarParser;
import lexer.ScanTablesStorage;
import lexer.Scanner;
import lexer.values.Lexeme;
import parser.Parser;
import precedence.Precedence;

import java.util.*;

import static constants.Statements.*;
import static constants.Transitions.JMP;
import static constants.Transitions.JNE;

public class ReversePolishNotationGenerator {
    private Queue<Lexeme> sourceSequence;
    private Stack<String> stack;
    private List<String> porn;  // Polish Reverse Notation
    private Integer labelCounter;
    private boolean isNextNewLineEndsLoop;
    private boolean isNextNewLineEndsConditionalTransition;
    private boolean isNextNewLineEndsTernaryStatement;
    private boolean isNextMinusWillBeUnary;

    public ReversePolishNotationGenerator(List<Lexeme> sourceSequence) {
        this.sourceSequence = new LinkedList<>(sourceSequence);
        stack = new Stack<>();
        porn = new ArrayList<>();
        labelCounter = 1;
        isNextNewLineEndsLoop = false;
        isNextNewLineEndsConditionalTransition = false;
        isNextNewLineEndsTernaryStatement = false;
        isNextMinusWillBeUnary = false;
    }

    public List<String> run() {

        while (!Parenthesis.isOpening(sourceSequence.element().getName())) {
            sourceSequence.remove();
        }
        while (!sourceSequence.isEmpty()) {
            doStep(sourceSequence.remove());
        }
        return porn;
    }

    private void doStep(Lexeme lexeme) {
        if (lexeme.isIdentifier() || lexeme.isConstant() || lexeme.isLabel()) {
            porn.add(lexeme.getName());
            isNextMinusWillBeUnary = false;
        } else {
            if (lexeme.getName().equals(MINUS.getName()) && isNextMinusWillBeUnary) {
                processStatement(UNARY_MINUS.getName());
            } else {
                processStatement(lexeme.getName());
            }
            isNextMinusWillBeUnary = true;
        }
    }

    private void processStatement(String statement) {
        if (Parenthesis.isOpening(statement)) {
            stack.push(statement);
        } else if (Parenthesis.isEnding(statement)) {
            popStatementsFromStackByOpeningParenthesis(Parenthesis.getOpening(statement));
        } else if (statement.equals(REPEAT.getName())) {
            porn.add("m" + labelCounter + ":");
            stack.push(REPEAT.getName() + " m" + labelCounter++);
        } else if (statement.equals(UNTIL.getName())) {
            popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
            isNextNewLineEndsLoop = true;
        } else if (statement.equals(IF.getName())) {
            processIF(statement);
        } else if (statement.equals(GOTO.getName())) {
            processGOTO(statement);
        } else if (statement.equals(IN.getName()) || statement.equals(OUT.getName())) {
            popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
        } else if (statement.equals("?")) {
            popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
            processQuery();
            isNextNewLineEndsTernaryStatement = true;
        } else if (statement.equals(":")) {
            popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
            processColon();
        } else if (statement.equals(NEWLINE.getName()) && isNextNewLineEndsTernaryStatement) {
            popColon();
            popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
            isNextNewLineEndsTernaryStatement = false;
        } else if (statement.equals(NEWLINE.getName())) {
            popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
            if (isNextNewLineEndsLoop) {
                popREPEAT();
                isNextNewLineEndsLoop = false;
            }
            else if (isNextNewLineEndsConditionalTransition) {
                popIF();
                isNextNewLineEndsConditionalTransition = false;
            }
        } else  {
            popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
            stack.push(statement);
        }
    }

    private void popColon() {
        porn.add(stack.pop().substring(2) + ":");     // pop "m_i+1" (: m_i+1)
    }

    private void processColon() {
        String label = "m" + labelCounter++;
        porn.add(label);
        porn.add(JMP.getName());
        porn.add(stack.pop().substring(2) + ":");   // pop "m_i" (? m_i)
        stack.push(": " + label);
    }

    private void processQuery() {
        String label = "m" + labelCounter++;
        porn.add(label);
        porn.add(JNE.getName());
        stack.push("? " + label);
    }

    private void popIF() {
        String label = stack.pop().substring(3);    // if m_i (ignore "if ")
        porn.add(label + ":");
    }

    private void popREPEAT() {
        String label = stack.pop().substring(7);    // repeat m_i (ignore "repeat ")
        porn.add(label);
        porn.add(JNE.getName());
    }

    private void processIF(String statement) {
        popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
        stack.push(IF.getName());
        isNextNewLineEndsConditionalTransition = true;
    }

    private void processGOTO(String statement) {
        popOperatorsFromStackByPriority(GrammarKeywords.getInstance().getPriority(statement));
        // change if to if m
        stack.pop();
        String label = "m" + labelCounter++;
        stack.push(IF.getName() + " " + label);
        porn.add(label);
        porn.add(JNE.getName());
        label = sourceSequence.remove().getName();
        label = label.substring(0, label.length() - 1);
        porn.add(label);
        porn.add(JMP.getName());
    }

    private void popStatementsFromStackByOpeningParenthesis(String opening) {
        if (opening == null) return;
        String temp = stack.pop();
        while (temp != null && !temp.equals(opening)) {
            porn.add(temp);
            temp = stack.pop();
        }
    }

    private void popOperatorsFromStackByPriority(Integer priority) {
        if (priority == null) return;
        Integer tempPriority;
        while (!stack.isEmpty()) {
            tempPriority = GrammarKeywords.getInstance().getPriority(stack.peek());
            if (tempPriority != null && priority < tempPriority) {
                porn.add(stack.pop());
            } else {
                return;
            }
        }
    }

    public static void main(String[] args) {
        lexer.Scanner lexer = new Scanner();
//        ScanTablesStorage scanTablesStorage = lexer.run("int i\n{ i = 5\n}\n");
        ScanTablesStorage scanTablesStorage = lexer.run(
                "int mark, plus\n" +
                "{       prettyPlace:\n" +
                "        in >> mark\n" +
                "        if (mark >= 60) goto nice:\n" +
                "        plus = 10\n" +
                "        repeat mark = -(2 + plus - 6)\n" +
                "        until (mark < 60)\n" +
                "        if (mark <= 85) goto prettyPlace:\n" +
                "        nice:\n" +
                "        mark = (plus <= 20) ? 1 : 100\n" +
                "}\n");
        Grammar grammar = GrammarParser.parse("res/grammar.json");
        Precedence precedence = new Precedence(grammar);
        Parser parser = new Parser(scanTablesStorage, precedence);
        if (parser.run()) {
            ReversePolishNotationGenerator pornGenerator = new ReversePolishNotationGenerator(scanTablesStorage.getLexemeTable());
            System.out.println(pornGenerator.run());
        }
    }
}
