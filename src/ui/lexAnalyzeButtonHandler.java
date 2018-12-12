package ui;

import exceptions.lexical.LexicalException;
import lexer.LexicalAnalyzer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class lexAnalyzeButtonHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        MainWindow.getLexicalAnalyzer().run(MainWindow.getCode().getText());
        MainWindow.getLexicalAnalyzer().getLexicalExceptions();
        MainWindow.setInfo("Hello!");
    }


    private Object[][] getExceptionsData(LexicalAnalyzer lexer) {
        Stream<LexicalException> exceptionStream = lexer.getLexicalExceptions().stream();
        AtomicInteger i = new AtomicInteger(1);
        List<Object[]> exceptions = exceptionStream.map(e -> new Object[]{i.getAndIncrement(),
                e.getMessage()}).collect(Collectors.toList());

        Object[][] data = new Object[exceptions.size()][];
        exceptions.toArray(data);
        return data;
    }
}
