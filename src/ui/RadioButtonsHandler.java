package ui;

import exceptions.lexical.LexicalException;
import lexer.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RadioButtonsHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (MainWindow.getLexicalAnalyzer().getLexicalExceptions().isEmpty()) {
            MainWindow.table.setVisible(true);
            LexicalAnalyzer lexer = MainWindow.getLexicalAnalyzer();
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


    private void setColumnWidth() {
        MainWindow.table.getColumnModel().getColumn(0).setPreferredWidth(30);
        MainWindow.table.getColumnModel().getColumn(1).setPreferredWidth(35);
        MainWindow.table.getColumnModel().getColumn(4).setPreferredWidth(35);
        MainWindow.table.getColumnModel().getColumn(5).setPreferredWidth(35);
        MainWindow.table.getColumnModel().getColumn(6).setPreferredWidth(35);
    }
}
