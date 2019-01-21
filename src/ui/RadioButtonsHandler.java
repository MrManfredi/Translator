package ui;

import exceptions.lexical.LexicalException;
import lexer.*;
import parser.SyntaxAnalyzerAutomate;
import parser.transitions.DataTableField;
import parser.transitions.MultiLineTableCellRenderer;
import parser.transitions.State;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RadioButtonsHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (MainWindow.getLexicalAnalyzer().getLexicalExceptions().isEmpty()) {
            MainWindow.table.setVisible(true);
            LexicalAnalyzer lexer = MainWindow.getLexicalAnalyzer();
            SyntaxAnalyzerAutomate parser = MainWindow.getSyntaxAnalyzerAutomate();
            if (MainWindow.allTokens.isSelected()) {
                String[] columnNames = {"#",
                        "# рядка",
                        "Лексема",
                        "LEX code",
                        "IDN code",
                        "CON code",
                        "LBL code"};

                Object[][] data = getLexemesData(lexer);

                TableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                MainWindow.table.setModel(model);
                MainWindow.table.getTableHeader().setUpdateTableInRealTime(false);

                setColumnWidth();
            } else if (MainWindow.identifiers.isSelected()) {
                String[] columnNames = {"#",
                        "Name",
                        "Type",};
                Object[][] data = getIdentifiersData(lexer);

                TableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                MainWindow.table.setModel(model);
                MainWindow.table.getTableHeader().setUpdateTableInRealTime(false);
            } else if (MainWindow.constants.isSelected()) {
                String[] columnNames = {"#",
                        "Index",
                        "Value",};
                Object[][] data = getConstantsData(lexer);

                TableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                MainWindow.table.setModel(model);
                MainWindow.table.getTableHeader().setUpdateTableInRealTime(false);
            } else if (MainWindow.labels.isSelected()) {
                String[] columnNames = {"#",
                        "Name",
                        "Index",
                        "From Line",
                        "To Line"};
                Object[][] data = getLabelsData(lexer);

                TableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                MainWindow.table.setModel(model);
                MainWindow.table.getTableHeader().setUpdateTableInRealTime(false);
            }  else if (MainWindow.transitions.isSelected()) {
                String[] columnNames = {"#",
                        "State",
                        "Label",
                        "Stack"};
                Object[][] data = getOutputTableData(parser);

                TableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                MainWindow.table.setModel(model);
                MainWindow.table.getTableHeader().setUpdateTableInRealTime(false);

            } else if (MainWindow.allTransitions.isSelected()) {
                String[] columnNames = {"#",
                        "State",
                        "Label",
                        "Transitions",
                        "Incomparability"};
                Object[][] data = getInputingTableData(parser);

                TableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                MainWindow.table.setModel(model);

                MainWindow.table.setDefaultRenderer(Object.class, new MultiLineTableCellRenderer());
                MainWindow.table.getTableHeader().setUpdateTableInRealTime(false);
                MainWindow.table.getColumnModel().getColumn(0).setMaxWidth(30);
                MainWindow.table.getColumnModel().getColumn(1).setMaxWidth(50);
                MainWindow.table.getColumnModel().getColumn(2).setMaxWidth(100);
            }
        }
    }

    private Object[][] getLexemesData(LexicalAnalyzer lexer) {
        Stream<Element> lexemeStream = lexer.getTokenTable().stream();

        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> lexemes = lexemeStream.map(l -> new Object[]{i.getAndIncrement(),
                l.getLine(),
                l.getText(),
                l.getCode(),
                (l.getCode() == LexemeType.IDENT.getValue()) ? l.getIndexInTable() : "",
                (l.getCode() == LexemeType.CONST.getValue()) ? l.getIndexInTable() : "",
                (l.getCode() == LexemeType.LABEL.getValue()) ? l.getIndexInTable() : ""}).collect(Collectors.toList());

        Object[][] data = new Object[lexemes.size()][];
        lexemes.toArray(data);
        return data;
    }

    private Object[][] getIdentifiersData(LexicalAnalyzer lexer) {
        Stream<Identifier> idnStream = lexer.getIdentTable().stream();

        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> identifiers = idnStream.map(l -> new Object[]{i.getAndIncrement(),
                l.getIdentifier(),
                l.getType()}).collect(Collectors.toList());

        Object[][] data = new Object[identifiers.size()][];
        identifiers.toArray(data);
        return data;
    }

    private Object[][] getConstantsData(LexicalAnalyzer lexer) {
        Stream<Constant> conStream = lexer.getConstTable().stream();

        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> identifiers = conStream.map(l -> new Object[]{i.getAndIncrement(),
                l.getIndex(),
                l.getConstant()}).collect(Collectors.toList());

        Object[][] data = new Object[identifiers.size()][];
        identifiers.toArray(data);
        return data;
    }

    private Object[][] getLabelsData(LexicalAnalyzer lexer) {
        Stream<Label> lblStream = lexer.getLabelTable().stream();

        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> identifiers = lblStream.map(l -> new Object[]{i.getAndIncrement(),
                l.getLabel(),
                l.getIndex(),
                l.getLineFrom(),
                l.getLineTo()}).collect(Collectors.toList());

        Object[][] data = new Object[identifiers.size()][];
        identifiers.toArray(data);
        return data;
    }

    private Object[][] getTransitionsData(LexicalAnalyzer lexer) {
        Stream<Label> transitionsStream = lexer.getLabelTable().stream();

        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> identifiers = transitionsStream.map(l -> new Object[]{i.getAndIncrement(),
                l.getLabel(),
                l.getIndex(),
                l.getLineFrom(),
                l.getLineTo()}).collect(Collectors.toList());

        Object[][] data = new Object[identifiers.size()][];
        identifiers.toArray(data);
        return data;
    }

    private Object[][] getInputingTableData(SyntaxAnalyzerAutomate synzer) {
        Stream<Map.Entry<Integer, State>> dataStream = synzer.getTransitions().entrySet().stream();
        Map<Integer, State> trans = synzer.getTransitions();
        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> errors = dataStream.map(d -> new Object[]{i.getAndIncrement(),
                d.getKey(),
                d.getValue().getTransitions(),
                d.getValue().getTV(),
                (d.getValue().getIncomparability() == null) ?
                        d.getValue().getIncomparabilityMsg() : d.getValue().getIncomparability()
        }).collect(Collectors.toList());

        Object[][] data = new Object[errors.size()][];
        errors.toArray(data);
        return data;
    }

    private Object[][] getOutputTableData(SyntaxAnalyzerAutomate synzer) {
        Stream<DataTableField> exceptionStream = synzer.getDataTable().stream();
        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> errors = exceptionStream.map(d -> new Object[]{i.getAndIncrement(),
                d.getState(), d.getLabel(), d.getStack()}).collect(Collectors.toList());

        Object[][] data = new Object[errors.size()][];
        errors.toArray(data);
        return data;
    }

    private void setColumnWidth() {
        MainWindow.table.getColumnModel().getColumn(0).setPreferredWidth(20);
        MainWindow.table.getColumnModel().getColumn(1).setPreferredWidth(25);
        MainWindow.table.getColumnModel().getColumn(4).setPreferredWidth(35);
        MainWindow.table.getColumnModel().getColumn(5).setPreferredWidth(35);
        MainWindow.table.getColumnModel().getColumn(6).setPreferredWidth(35);
    }
}
