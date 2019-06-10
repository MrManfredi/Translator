package parser;

import constants.Sharp;
import errors.syntactic.NoRatioBetweenTokensError;
import errors.syntactic.RuleNotFoundError;
import errors.syntactic.SyntacticAnalysisRejectedError;
import errors.syntactic.SyntaxError;
import grammar.Grammar;
import grammar.GrammarParser;
import grammar.RightSide;
import constants.IdentifierType;
import lexer.ScanTablesStorage;
import lexer.values.Lexeme;
import constants.LexemeType;
import lexer.Scanner;
import precedence.Precedence;
import precedence.PrecedenceStorage;
import precedence.RatioType;

import java.util.*;

public class Parser {
    // global
    private PrecedenceStorage precedenceStorage;
    private HashMap<RightSide, String> reverseRules;
    // for table
    private ArrayList<ParsingTableRow> parsingTable;
    private Integer currentId;
    private Stack<Unit> currentStack;
    private RatioType currentRatio;
    private Queue<Lexeme> sourceSequence;
    private Stack<String> currentBasisStack;
    // error
    private SyntaxError syntaxError;

    public Parser(ScanTablesStorage scanTables, Precedence precedence) {
        if (!scanTables.getScanErrorsTable().isEmpty()) {
            syntaxError = new SyntacticAnalysisRejectedError("There is errors in lexical analysis.");
        } else if (scanTables.getLexemeTable().isEmpty()) {
            syntaxError = new SyntacticAnalysisRejectedError("Source code is empty.");
        } else {
            precedenceStorage = precedence.getPrecedenceStorage();
            reverseRules = precedence.getGrammar().getReverseRules();
            //
            // for table
            //
            currentId = 1;
            currentStack = new Stack<>();
            currentRatio = null;
            // sharp in the beginning of the source sequence
            scanTables.getLexemeTable().add(0, new Lexeme(0, scanTables.getLexemeTable().get(0).getLine(), Sharp.name(), Sharp.code(), null));
            sourceSequence  = new LinkedList<>(scanTables.getLexemeTable());
            // sharp in the end of the source sequence
            Lexeme lastLexeme = scanTables.getLexemeTable().get(sourceSequence.size() - 1);
            sourceSequence.add(new Lexeme(lastLexeme.getId() + 1, lastLexeme.getLine(), Sharp.name(), Sharp.code(), null));
            currentBasisStack = new Stack<>();
            syntaxError = null;
        }
        parsingTable = new ArrayList<>();
    }

    public boolean run() {
        while (syntaxError == null && sourceSequence.size() > 1) {
            if (!readFromSequenceToStack()) {
                break;
            }

            parsingTable.add(new ParsingTableRow(currentId, currentStack, currentRatio, sourceSequence, new RightSide(currentBasisStack)));
            currentId++;

            compress();
        }
        return syntaxError == null;
    }

    private String getName(Lexeme lexeme) {
        if (lexeme.isIdentifier()) {
            return LexemeType.IDENT.getName();
        } else if (lexeme.isConstant()) {
            return LexemeType.CONST.getName();
        } else if (lexeme.isLabel()) {
            return LexemeType.LABEL.getName();
        } else if (lexeme.getCode() == IdentifierType.INT.getValue()) {
            return IdentifierType.INT.getName();
        } else if (lexeme.getName().equals("¶")) {
            return "NL";
        } else {
            return lexeme.getName();
        }
    }

    private boolean readFromSequenceToStack() {
        String currentLeftWord = getName(sourceSequence.remove());
        String currentRightWord = getName(sourceSequence.element());
        currentRatio = precedenceStorage.calculateRatio(currentLeftWord, currentRightWord);
        if (currentRatio == null) {
            syntaxError = new NoRatioBetweenTokensError(sourceSequence.element().getLine(), currentLeftWord, currentRightWord);
            return false;
        }
        currentStack.push(new Unit(currentLeftWord, currentRatio));
        return true;
    }

    private void compress() {
        String currentTransitionalWord;
        String currentRightWord = getName(sourceSequence.element());
        while(currentRatio == RatioType.MORE && !currentStack.peek().getName().equals("Program")) {
            while (currentStack.peek().getRatio() != RatioType.LESS) {
                currentBasisStack.push(currentStack.pop().getName());
            }
            RightSide currentBasis = new RightSide(currentBasisStack);
            currentTransitionalWord = reverseRules.get(currentBasis);
            if (currentTransitionalWord == null) {
                syntaxError = new RuleNotFoundError(sourceSequence.element().getLine(), currentBasis);
                return;
            }

            if (!currentTransitionalWord.equals("Program")) {
                currentRatio = precedenceStorage.calculateRatio(currentStack.peek().getName(), currentTransitionalWord);
                if (currentRatio == null) {
                    syntaxError = new NoRatioBetweenTokensError(sourceSequence.element().getLine(), currentStack.peek().getName(), currentTransitionalWord);
                    return;
                }
                currentStack.peek().setRatio(currentRatio);

                currentRatio = precedenceStorage.calculateRatio(currentTransitionalWord, currentRightWord);
                if (currentRatio == null) {
                    syntaxError = new NoRatioBetweenTokensError(sourceSequence.element().getLine(), currentTransitionalWord, currentRightWord);
                    return;
                }
            } else {
                currentRatio = RatioType.MORE;
            }

            currentStack.push(new Unit(currentTransitionalWord, currentRatio));
            parsingTable.add(new ParsingTableRow(currentId, currentStack, currentRatio, sourceSequence, currentBasis));
            currentId++;
        }
    }

    public ArrayList<ParsingTableRow> getParsingTable() {
        return parsingTable;
    }

    public SyntaxError getSyntaxError() {
        return syntaxError;
    }

    public static void main(String[] args) {
        Scanner lexer = new Scanner();
        ScanTablesStorage scanTablesStorage = lexer.run("int i\n{ i = 5\n}\n");
        Grammar grammar = GrammarParser.parse("res/grammar.json");
        Precedence precedence = new Precedence(grammar);
        Parser parser = new Parser(scanTablesStorage, precedence);
        parser.run();
        System.out.println();
    }
}
