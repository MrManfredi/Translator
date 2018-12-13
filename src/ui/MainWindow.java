package ui;

import lexer.LexicalAnalyzer;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow {
    // variables
    private static LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

    // left part
    private static JButton open;
    private static JButton save;
    private static JLabel codeLabel;
    private static JTextArea code;
    static JScrollPane codeScrollPane;
    private static JLabel infoLabel;
    private static JTextArea info;
    static JScrollPane infoScrollPane;

    // right part
    static JRadioButton allTokens;
    static JRadioButton identifiers;
    static JRadioButton constants;
    static JRadioButton labels;
    static JTable table;
    static JScrollPane scrollPane;
    static JButton lexAnalyze;
    static JButton synAnalyze;

    public static void setCode(String text) {
        code.setText(text);
    }

    public static JTextArea getCode() {
        return code;
    }

    public static void setInfo(String info) {
        MainWindow.info.setText(info);
    }

    public static LexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }

    public MainWindow()
    {
        // left part
        open = new JButton("Open");
        save = new JButton("Save");
        codeLabel = new JLabel("Enter your code here:");
        code = new JTextArea("");
        codeScrollPane = new JScrollPane(code);
        infoLabel = new JLabel("Information:");
        info = new JTextArea(5, 50);
        infoScrollPane = new JScrollPane(info);

        // right part
        allTokens = new JRadioButton("Tokens");
        identifiers = new JRadioButton("Identifiers");
        constants = new JRadioButton("Constants");
        labels = new JRadioButton("Labels");
        table = new JTable();
        scrollPane = new JScrollPane(table);
        lexAnalyze = new JButton("Lexical Analyze");
        synAnalyze = new JButton("Syntax Analyze");
    }

    public static void launchFrame()
    {
        JFrame frame = new JFrame("Dmytro Nazarchuk");
        frame.setLayout(new GridLayout(1, 2));

        AbstractBorder border = new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new EmptyBorder(0, 0, 0, 0));
        // left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(border);
        JPanel topPanel = new JPanel();
        // top
        topPanel.setLayout(new GridLayout(2, 1));
        JPanel fileButtonsPanel = new JPanel();
        fileButtonsPanel.setLayout(new GridLayout(1, 2));
        fileButtonsPanel.add(open);
        fileButtonsPanel.add(save);
        // button listeners
        open.addActionListener(new OpenButtonHandler());
        save.addActionListener(new SaveButtonHandler());
        topPanel.add(fileButtonsPanel);
        topPanel.add(codeLabel);
        leftPanel.add(topPanel, BorderLayout.NORTH);
        // center
        leftPanel.add(codeScrollPane, BorderLayout.CENTER);
        // bottom
        JPanel bottomPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        bottomPanel.setLayout(gbl);
        GridBagConstraints con =  new GridBagConstraints();
        con.anchor = GridBagConstraints.NORTH;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridheight = 1;
        con.gridwidth  = GridBagConstraints.REMAINDER;
        con.gridx = GridBagConstraints.RELATIVE;
        con.gridy = GridBagConstraints.RELATIVE;
        con.insets = new Insets(2, 0, 2, 0);
        con.ipadx = 0;
        con.ipady = 0;
        con.weightx = 1.0;
        con.weighty = 0.0;
        gbl.setConstraints(infoLabel, con);
        con.anchor = GridBagConstraints.CENTER;
        con.gridheight = 5;
        con.insets = new Insets(2, 0, 0, 0);
        bottomPanel.add(infoLabel);
        gbl.setConstraints(infoScrollPane, con);
        bottomPanel.add(infoScrollPane);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(leftPanel);
        // right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(border);
        // top
        ButtonGroup radioButtonsGroup = new ButtonGroup();
        radioButtonsGroup.add(allTokens);
        radioButtonsGroup.add(identifiers);
        radioButtonsGroup.add(constants);
        radioButtonsGroup.add(labels);
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new GridLayout(1, 4));
        radioButtonsPanel.add(allTokens);
        radioButtonsPanel.add(identifiers);
        radioButtonsPanel.add(constants);
        radioButtonsPanel.add(labels);
        rightPanel.add(radioButtonsPanel, BorderLayout.NORTH);
        allTokens.addActionListener(new RadioButtonsHandler());
        identifiers.addActionListener(new RadioButtonsHandler());
        constants.addActionListener(new RadioButtonsHandler());
        labels.addActionListener(new RadioButtonsHandler());
        // center
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        // bottom
        JPanel analyzButtonsPanel = new JPanel();
        analyzButtonsPanel.setLayout(new GridLayout(1, 2));
        analyzButtonsPanel.add(lexAnalyze);
        analyzButtonsPanel.add(synAnalyze);
        rightPanel.add(analyzButtonsPanel, BorderLayout.SOUTH);
        lexAnalyze.addActionListener(new lexAnalyzeButtonHandler());
        frame.add(rightPanel);
        // characteristics
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1200, 500));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}