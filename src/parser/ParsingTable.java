package parser;

import java.util.ArrayList;
import java.util.List;

public class ParsingTable {
    private List<ParsingTableRow> parsingTableRows;

    public ParsingTable() {
        parsingTableRows = new ArrayList<>();
    }

    public void addRow(ParsingTableRow row) {
        parsingTableRows.add(row);
    }

    public List<ParsingTableRow> getParsingTableRows() {
        return parsingTableRows;
    }
}
