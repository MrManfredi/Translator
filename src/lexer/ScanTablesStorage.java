package lexer;

import errors.lexical.LexicalError;
import lexer.values.Constant;
import lexer.values.Identifier;
import lexer.values.Label;
import lexer.values.Lexeme;

import java.util.List;

public class ScanTablesStorage {
    private List<Lexeme> lexemeTable;
    private Table<Identifier> identifiersTable;
    private Table<Constant> constantsTable;
    private Table<Label> labelsTable;
    private List<LexicalError> scanErrorsTable;

    public ScanTablesStorage(List<Lexeme> lexemeTable, Table<Identifier> identifiersTable, Table<Constant> constantsTable,
                             Table<Label> labelsTable, List<LexicalError> scanErrorsTable) {
        this.lexemeTable = lexemeTable;
        this.identifiersTable = identifiersTable;
        this.constantsTable = constantsTable;
        this.labelsTable = labelsTable;
        this.scanErrorsTable = scanErrorsTable;
    }

    public List<Lexeme>  getLexemeTable() {
        return lexemeTable;
    }

    public Table<Identifier> getIdentifiersTable() {
        return identifiersTable;
    }

    public Table<Constant> getConstantsTable() {
        return constantsTable;
    }

    public Table<Label> getLabelsTable() {
        return labelsTable;
    }

    public List<LexicalError> getScanErrorsTable() {
        return scanErrorsTable;
    }
}
