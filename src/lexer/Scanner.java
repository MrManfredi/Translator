package lexer;

import constants.IdentifierType;
import constants.Keywords;
import constants.LexemeType;
import errors.lexical.*;
import lexer.values.Constant;
import lexer.values.Identifier;
import lexer.values.Label;
import lexer.values.Lexeme;

import java.util.*;

public class Scanner {
    static private ArrayList<Character> OP;

    private List<Lexeme> lexemeTable;
    private Table<Identifier> identifiersTable;
    private Table<Constant> constantsTable;
    private Table<Label> labelsTable;
    private ArrayList<LexicalError> scanErrorsTable;

    private StringBuilder builder;
    private String code;
    private int currentCharIndex;
    private int indexIdent;
    private int indexConst;
    private int indexLabel;
    private int currentId;
    private int line;
    private char tmp;
    private String lastType; // using only after isDeclaration() method

    public Scanner() {
        OP = new ArrayList<Character>(Arrays.asList('+', '*', '/', '^', ',', '?', ':', '(', ')', '{', '}'));
        builder = new StringBuilder();
    }

    public void init() {
        currentCharIndex = 0;
        indexIdent = 0;
        indexConst = 0;
        indexLabel = 0;
        currentId = 1;
        line = 1;

        lexemeTable = new ArrayList<>();
        identifiersTable = new Table<>();
        constantsTable = new Table<>();
        labelsTable = new Table<>();
        scanErrorsTable = new ArrayList<LexicalError>();
    }

    public ScanTablesStorage run(String text) {
        init();
        code = text + " ";
        state1();
        checkLabelErrors();
        if (scanErrorsTable.isEmpty()) {
            return new ScanTablesStorage(lexemeTable, identifiersTable, constantsTable, labelsTable, scanErrorsTable);
        } else {
            return new ScanTablesStorage(new ArrayList<Lexeme>(), new Table<>(), new Table<>(), new Table<>(), scanErrorsTable);
        }
    }

    private void state1() {
        tmp = code.charAt(currentCharIndex);
        while (hasNextChar()) {
            if (Character.isLetter(tmp)) {
                state2();   // identifier
            } else if (tmp == '-') {
                state3();   // -
            } else if (Character.isDigit(tmp)) {
                state4();   // constant
            } else if (OP.contains(tmp)) {
                state5();   // OP
            } else if (tmp == '=') {
                state6();   // =
            } else if (tmp == '<') {
                state7();   // <
            } else if (tmp == '>') {
                state8();   // >
            } else if (tmp == '!') {
                state9();   // !
            } else if (tmp == ' ' || tmp == '\t') {
                if (hasNextChar()) {
                    nextChar();
                }
            } else if (tmp == '\n') {
                state10();
                line++;
                if (hasNextChar()) {
                    nextChar();
                }
            } else {
                scanErrorsTable.add(new UnknownSymbolError(tmp, line));
                nextChar();
            }
            clearBuilder();
        }
    }

    private void state2()   // identifier
    {
        nextChar();
        if (Character.isLetter(tmp) || Character.isDigit(tmp)) {
            state2();
        } else if (tmp == ':') {
            state11();  // label
        } else {
            if (Keywords.getInstance().containsKey(builder.toString())) {
                addToken();
            } else {
                Identifier tempIdentifier;
                if (identifiersTable.containsItem(builder.toString())) {
                    // перевірка на повторну декларацію
                    if (isDeclaration()) {
                        scanErrorsTable.add(new VariableReDeclarationError(line, builder.toString()));
                    }
                    // уже задекларований ідентифікатор (індекс і тип беруться з таблиці)
                    tempIdentifier = identifiersTable.getItem(builder.toString());
                    tempIdentifier = new Identifier(builder.toString(), tempIdentifier.getIndex(), tempIdentifier.getType());
                } else {
                    if (isDeclaration()) {
                        tempIdentifier = new Identifier(builder.toString(), ++indexIdent, lastType);
                    } else {
                        scanErrorsTable.add(new VariableUsedWithoutDeclarationError(line, builder.toString()));
                        tempIdentifier = new Identifier(builder.toString(), ++indexIdent, "Not Declared");
                    }
                    // adding new identifier
                    identifiersTable.add(tempIdentifier);
                }
                addToken(tempIdentifier);
            }
        }
    }

    private void state3()   // -
    {
        nextChar();
        Lexeme lastLexeme = lexemeTable.get(lexemeTable.size() - 1);
        if (lastLexeme.isIdentifier() || lastLexeme.isConstant()) {
            addToken();
        } else if (Character.isDigit(tmp)) {
            state4();   // constant
        } else {
            addToken();
        }
    }

    private void state4()   // constant
    {
        nextChar();
        if (Character.isDigit(tmp)) {
            state4();
        } else {
            Constant temp;
            if (constantsTable.containsItem(builder.toString())) {
                temp = new Constant(builder.toString(), constantsTable.getItem(builder.toString()).getIndex());
            } else {
                temp = new Constant(builder.toString(), ++indexConst);
                constantsTable.add(temp);
            }
            addToken(temp);
        }
    }

    private void state5()   // OP
    {
        nextChar();
        addToken();
    }

    private void state6()   // =
    {
        nextChar();
        if (tmp == '=') {
            state5();  // ==
        } else {
            addToken();
        }
    }

    private void state7()   // <
    {
        nextChar();
        if (tmp == '=' || tmp == '<') {
            state5();  // <= or <<
        } else {
            addToken();
        }
    }

    private void state8()   // >
    {
        nextChar();
        if (tmp == '=' || tmp == '>') {
            state5();  // >= or >>
        } else {
            addToken();
        }
    }

    private void state9()   // !
    {
        nextChar();
        if (tmp == '=') {
            state5();  // !=
        } else {
            scanErrorsTable.add(new UnknownSymbolError('!', line));
        }
    }

    private void state10() {
        lexemeTable.add(new Lexeme(currentId++, line, "¶", Keywords.getInstance().getIndex("\n"), null));
    }

    private void state11()    // label
    {
        nextChar();
        Label temp;
        if (labelsTable.containsItem(builder.toString())) {
            temp = new Label(builder.toString(), labelsTable.getItem(builder.toString()).getIndex());
        } else {
            temp = new Label(builder.toString(), ++indexLabel);
            labelsTable.add(temp);
        }
        addLabelLine(builder.toString());
        addToken(temp);
    }

    private void nextChar() {
        builder.append(tmp);
        currentCharIndex++;
        tmp = code.charAt(currentCharIndex);
    }

    private boolean hasNextChar() {
        if (code.length() > currentCharIndex + 1) {
            return true;
        }
        return false;
    }

    public void clearBuilder() {
        if (builder.length() > 0) {
            builder.delete(0, builder.length());
        }
    }

    public void addToken() {
        lexemeTable.add(new Lexeme(currentId++, line, builder.toString(), Keywords.getInstance().getIndex(builder.toString()), null));
    }

    public void addToken(Identifier ident) {
        lexemeTable.add(new Lexeme(currentId++, line, builder.toString(), LexemeType.IDENT.getValue(), ident.getIndex()));
    }

    public void addToken(Constant constant) {
        lexemeTable.add(new Lexeme(currentId++, line, builder.toString(), LexemeType.CONST.getValue(), constant.getIndex()));
    }

    public void addToken(Label label) {
        lexemeTable.add(new Lexeme(currentId++, line, builder.toString(), LexemeType.LABEL.getValue(), label.getIndex()));
    }

    private void addLabelLine(String label) {
        for (Label tmp : labelsTable) {
            if (tmp.getName().equals(label)) {
                if (lexemeTable.get(lexemeTable.size() - 1).getName().equals("goto")) {
                    if (tmp.getLineFrom() == -1) {
                        tmp.setLineFrom(line);
                    } else {
                        scanErrorsTable.add(new LabelRepeatedCallError(line, label));
                    }
                } else {
                    if (tmp.getLineTo() == -1) {
                        tmp.setLineTo(line);
                    } else {
                        scanErrorsTable.add(new LabelReDeclarationError(line, label));
                    }
                }

            }
        }
    }

    private boolean isDeclaration() {
        for (Lexeme tmp : lexemeTable) {
            if (tmp.getLine() == line) {
                if (tmp.getCode() == IdentifierType.INT.getValue()) {
                    lastType = tmp.getName();
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void checkLabelErrors() {
        for (Label tmp : labelsTable) {
            if (tmp.getLineTo() == -1) {
                scanErrorsTable.add(new LabelNotDeclaratedError(-1, tmp.getName()));
            }
        }
    }
}