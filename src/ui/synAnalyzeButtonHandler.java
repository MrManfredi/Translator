package ui;

import exceptions.syntactic.SyntaxException;
import parser.SyntaxAnalyzer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class synAnalyzeButtonHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
//        // Recursive Syntax Analyze
//        MainWindow.getLexicalAnalyzer().run(MainWindow.getCode().getText());
//        MainWindow.getSyntaxAnalyzer().setLex(MainWindow.getLexicalAnalyzer());
//        MainWindow.getSyntaxAnalyzer().run();
//        if (MainWindow.getSyntaxAnalyzer().getSyntaxExceptions().isEmpty())
//        {
//            MainWindow.setInfo("Syntactic analysis successful!");
//        }
//        else
//        {
//            MainWindow.setInfo(getExceptionsData(MainWindow.getSyntaxAnalyzer()));
//        }
//        MainWindow.table.setVisible(false);

        MainWindow.getLexicalAnalyzer().run(MainWindow.getCode().getText());
        MainWindow.getSyntaxAnalyzerAutomate().run(MainWindow.getCode().getText());
        if (MainWindow.getSyntaxAnalyzerAutomate().getError().equals("exit"))
        {
            MainWindow.setInfo("Syntactic analysis successful!");
        }
        else
        {
            MainWindow.setInfo(MainWindow.getSyntaxAnalyzerAutomate().getError());
        }
        MainWindow.table.setVisible(false);
    }

    private String getExceptionsData(SyntaxAnalyzer syntaxAnalyzer) {
        StringBuilder builder = new StringBuilder();
        for (SyntaxException tmp : syntaxAnalyzer.getSyntaxExceptions())
        {
            builder.append(tmp.getMessage()).append("\n");
        }
        return builder.toString();
    }
}
