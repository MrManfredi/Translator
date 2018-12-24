package ui;

import exceptions.lexical.LexicalException;
import lexer.LexicalAnalyzer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lexAnalyzeButtonHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        MainWindow.getLexicalAnalyzer().run(MainWindow.getCode().getText());
        if (MainWindow.getLexicalAnalyzer().getLexicalExceptions().isEmpty())
        {
            MainWindow.setInfo("Lexical analysis successful!");
        }
        else
        {
            MainWindow.setInfo(getExceptionsData(MainWindow.getLexicalAnalyzer()));
        }
        MainWindow.table.setVisible(false);
    }

    private String getExceptionsData(LexicalAnalyzer lexer) {
        StringBuilder builder = new StringBuilder();
        for (LexicalException tmp : lexer.getLexicalExceptions())
        {
            builder.append(tmp.getMessage()).append("\n");
        }
        return builder.toString();
    }
}
