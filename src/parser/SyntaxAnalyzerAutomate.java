package parser;

import lexer.Element;
import lexer.LexicalAnalyzer;
import parser.transitions.DataTableField;
import parser.transitions.State;
import parser.transitions.TTReader;
import parser.transitions.TransitionElems;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class SyntaxAnalyzerAutomate {
    private LexicalAnalyzer la;
    private Map<Integer, State> stateTransitions;
    private ArrayList<DataTableField> dataTable;

    private Stack<Integer> stack;
    private int i;
    private int state;
    private String curLex;
    private String error;

//    public static void main(String[] args) {
//        SyntaxAnalyzerAutomate saa = new SyntaxAnalyzerAutomate();
//
//        boolean res = saa.run();
//        System.out.println("SA: " + res);
////        if (res)
//        System.out.println(saa.dataTable);
//    }

    public SyntaxAnalyzerAutomate() {}
    private void init(String text) {
        dataTable = new ArrayList<>() {
            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < this.size(); i++) {
                    builder.append(i + 1).append(": ").append(this.get(i)).append('\n');
                }
                return builder.toString();
            }
        };
        stack = new Stack<>();
        TTReader ttr = new TTReader("res/transition_table.xml");
        this.la = new LexicalAnalyzer();
        this.la.run(text);
        this.stateTransitions = ttr.getStates();
        this.i = 0;
        this.state = 1;
        this.curLex = getCurrentLexeme();
        error = null;
    }

    public boolean run(String text) {
        init(text);
        if (!this.la.getLexicalExceptions().isEmpty())
        {
            error = "The syntactic analysis was rejected because the lexical analysis ended with errors.";
            return false;
        }
        while (true) {    // || не последняя лксема
            TransitionElems elems;
            if (hasTransition(getCurrentLexeme())) {
                elems = stateTransitions.get(state).getTransition(getCurrentLexeme());
                nextState(elems);
                inc();
            } else {
                if (hasIncompatibilityTransition()) {
                    elems = stateTransitions.get(state).getIncomparability();
                    nextState(elems);
                } else {
                    error = stateTransitions.get(state).getIncomparabilityMsg();
                    if (error != null) {
                        if (error.equals("exit")) {
                            if (stack.empty()) return true;
                            addTableRecord();
                            state = stack.pop();
                            continue;
                        } else {
                            error = "line: " + getLexemeLine(i) + " - " + error + "! But found" + ((curLex.equals("")) ? " nothing" : ": " + curLex);
                        }
                        return false;
                    }
                }
            }
        }
//        return false;
    }

    private boolean hasTransition(String lex) {
        return stateTransitions.get(state).getTransition(lex) != null;
    }

    private boolean hasIncompatibilityTransition() {
        return stateTransitions.get(state).getIncomparability() != null;
    }

    private void addTableRecord() {
        dataTable.add(new DataTableField(state, curLex, stackCopy()));
    }

    private void nextState(TransitionElems elems) {
        addTableRecord();
        state = elems.getNextState();
        if (elems.getStackPush() != null)
            stack.push(elems.getStackPush());
    }

    private String getCurrentLexeme() {
        if (i >= la.getTokenTable().size()) return curLex = "";
        Element lexeme = la.getTokenTable().get(i);
        if (lexeme.getCode() > 99) {
            if (lexeme.getCode() == 100) {
                curLex = lexeme.getText();
                return "_IDN";
            } else if (lexeme.getCode() == 101) {
                curLex = lexeme.getText();
                return "_CON";
            } else if (lexeme.getCode() == 102) {
                curLex = lexeme.getText();
                return "_LBL";
            }
        }
        return curLex = lexeme.getText();
    }

    private int getLexemeLine(int i) {
        int index = (i < la.getTokenTable().size()) ? i : la.getTokenTable().size() - 1;
        return la.getTokenTable().get(index).getLine();
    }

    private boolean inc() {
        return ++i < la.getTokenTable().size();
    }

    private ArrayList<Integer> stackCopy() {
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(stack);
        return list;
    }

    public String getError() {
        return error;
    }

    public ArrayList<DataTableField> getDataTable() {
        return dataTable;
    }

    public Map<Integer, State> getTransitions() {
        return stateTransitions;
    }
}
