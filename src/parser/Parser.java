package parser;

import errors.syntactic.NoRatioBetweenTokensError;
import errors.syntactic.RuleNotFoundError;
import errors.syntactic.SyntacticAnalysisRejectedError;
import errors.syntactic.SyntaxError;
import grammar.Grammar;
import grammar.GrammarParser;
import grammar.RightSide;
import lexer.IdentifierType;
import lexer.Lexeme;
import lexer.LexemeType;
import lexer.LexicalAnalyzer;
import precedence.Precedence;
import precedence.PrecedenceStorage;
import precedence.RatioType;

import java.util.*;

public class Parser {
    // global
    private LexicalAnalyzer scanner;
    private Precedence precedence;
    private PrecedenceStorage precedenceStorage;
    private HashMap<RightSide, String> reverseRules;
    // for table
    private ParsingTable parsingTable;
    private Integer currentId;
    private Stack<Unit> currentStack;
    private RatioType currentRatio;
    private Queue<Lexeme> sourceSequence;
    private Stack<String> currentBasisStack;
    // error
    private SyntaxError syntaxError;

    public Parser(LexicalAnalyzer scanner, Precedence precedence) {
        this.scanner = scanner;

        if (!scanner.getLexicalErrors().isEmpty()) {
            syntaxError = new SyntacticAnalysisRejectedError("There is errors in lexical analysis.");
        } else if (scanner.getTokenTable().isEmpty()) {
            syntaxError = new SyntacticAnalysisRejectedError("Source code is empty.");
        } else {
            this.precedence = precedence;
            precedenceStorage = precedence.getPrecedenceStorage();
            reverseRules = precedence.getGrammar().getReverseRules();
            //
            // for table
            //
            currentId = 1;
            currentStack = new Stack<>();
            currentRatio = null;
            // sharp in the beginning of the source sequence
            scanner.getTokenTable().add(0, new Lexeme(0, scanner.getTokenTable().get(0).getLine(), Sharp.name(), Sharp.code(), null));
            sourceSequence  = new LinkedList<>(scanner.getTokenTable());
            // sharp in the end of the source sequence
            Lexeme lastLexeme = scanner.getTokenTable().get(sourceSequence.size() - 1);
            sourceSequence.add(new Lexeme(lastLexeme.getId() + 1, lastLexeme.getLine(), Sharp.name(), Sharp.code(), null));
            currentBasisStack = new Stack<>();
            syntaxError = null;
        }
        parsingTable = new ParsingTable();
    }

    public void run() {
        while (syntaxError == null && sourceSequence.size() > 1) {
            if (!readFromSequenceToStack()) {
                break;
            }

            parsingTable.addRow(new ParsingTableRow(currentId, currentStack, currentRatio, sourceSequence, new RightSide(currentBasisStack)));
            currentId++;

            compress();
        }
    }

    private String getName(Lexeme lexeme) {
        if (lexeme.getCode() == LexemeType.IDENT.getValue()) {
            return LexemeType.IDENT.getName();
        } else if (lexeme.getCode() == LexemeType.CONST.getValue()) {
            return LexemeType.CONST.getName();
        } else if (lexeme.getCode() == LexemeType.LABEL.getValue()) {
            return LexemeType.LABEL.getName();
        } else if (lexeme.getCode() == IdentifierType.INT.getValue()) {
            return IdentifierType.INT.getName();
        } else if (lexeme.getText().equals("¶")) {
            return "NL";
        } else {
            return lexeme.getText();
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
            parsingTable.addRow(new ParsingTableRow(currentId, currentStack, currentRatio, sourceSequence, currentBasis));
            currentId++;
        }
    }

    public ParsingTable getParsingTable() {
        return parsingTable;
    }

    public SyntaxError getSyntaxError() {
        return syntaxError;
    }

    public static void main(String[] args) {
        LexicalAnalyzer lexer = new LexicalAnalyzer();
        lexer.run("int i\n{ i = 5\n}\n");
        Grammar grammar = GrammarParser.parse("res/grammar.json");
        Precedence precedence = new Precedence(grammar);
        Parser parser = new Parser(lexer, precedence);
        parser.run();
        System.out.println();
    }
}
