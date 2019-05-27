package gui;

import errors.lexical.LexicalError;
import errors.syntactic.SyntaxError;
import grammar.Grammar;
import grammar.GrammarParser;
import grammar.RightSide;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lexer.Constant;
import lexer.Identifier;
import lexer.Lexeme;
import lexer.LexicalAnalyzer;
import parser.Parser;
import parser.ParsingTableRow;
import parser.Unit;
import precedence.Precedence;
import precedence.RatioType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Controller {
    private Grammar grammar;
    private LexicalAnalyzer lexicalAnalyzer;
    private Precedence precedence;
    private Parser parser;

    // Menu panel

    //file
    @FXML
    private MenuItem menu_file_open;

    @FXML
    private MenuItem menu_file_save;

    // run
    @FXML
    private MenuItem menu_run_lexical_analyser;

    @FXML
    private MenuItem menu_run_parser;

    // code area
    @FXML
    private TextArea codeArea;

    /* Tabs */
    @FXML
    private Tab LA_errors_tab;

    @FXML
    private Tab parser_errors_tab;

    /* Lexical Analyser */

    // Tokens
    @FXML
    private TableView<Lexeme> LA_tokens_table;
    @FXML
    private TableColumn<Lexeme, Integer> LAT_id;
    @FXML
    private TableColumn<Lexeme, Integer> LAT_line;
    @FXML
    private TableColumn<Lexeme, String> LAT_lexeme;
    @FXML
    private TableColumn<Lexeme, Integer> LAT_lexeme_code;
    @FXML
    private TableColumn<Lexeme, Integer> LAT_identifier_code;
    @FXML
    private TableColumn<Lexeme, Integer> LAT_constant_code;
    @FXML
    private TableColumn<Lexeme, Integer> LAT_label_code;

    // Identifiers
    @FXML
    private TableView<Identifier> LA_identifiers_table;
    @FXML
    private TableColumn<Identifier, Integer> LAI_id;
    @FXML
    private TableColumn<Identifier, String> LAI_name;
    @FXML
    private TableColumn<Identifier, String> LAI_type;

    // Constants
    @FXML
    private TableView<Constant> LA_constants_table;
    @FXML
    private TableColumn<Constant, Integer> LAC_id;
    @FXML
    private TableColumn<Constant, Integer> LAC_value;

    // Labels
    @FXML
    private TableView<lexer.Label> LA_labels_table;
    @FXML
    private TableColumn<lexer.Label, Integer> LAL_id;
    @FXML
    private TableColumn<lexer.Label, String> LAL_name;
    @FXML
    private TableColumn<lexer.Label, Integer> LAL_line_from;
    @FXML
    private TableColumn<lexer.Label, Integer> LAL_line_to;

    // Errors
    @FXML
    private TableView<LexicalError> LA_errors_table;
    @FXML
    private TableColumn<LexicalError, String> LAE_message;

    // Grammar
    @FXML
    private TextArea grammarArea;

    /* Parser */
    @FXML
    private TableView<ParsingTableRow> parsing_table;
    @FXML
    private TableColumn<ParsingTableRow, Integer> PT_id;
    @FXML
    private TableColumn<ParsingTableRow, List<Unit>> PT_stack;
    @FXML
    private TableColumn<ParsingTableRow, RatioType> PT_ratio;
    @FXML
    private TableColumn<ParsingTableRow, List<Lexeme>> PT_source_sequence;
    @FXML
    private TableColumn<ParsingTableRow, RightSide> PT_basis;
    // errors table
    @FXML
    private TableView<SyntaxError> parsing_errors_table;
    @FXML
    private TableColumn<SyntaxError, String> PE_message;


    @FXML
    void initialize() {
        initGrammar();
        lexicalAnalyzer = new LexicalAnalyzer();
        precedence = new Precedence(grammar);
        initLexerTables();
        initParserTables();
        event_menu_file_open();
        event_menu_file_save();
        event_menu_run_lexical_analyser();
        event_menu_run_parser();
    }

    private void initGrammar() {
        grammar = GrammarParser.parse("res/grammar.json");
        if (grammar == null) {
            grammarArea.setText("Grammar reading error.");
        }
        else {
            grammarArea.setText(grammar.toString());
        }
    }

    private void initLexerTables() {
        LAT_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        LAT_line.setCellValueFactory(new PropertyValueFactory<>("line"));
        LAT_lexeme.setCellValueFactory(new PropertyValueFactory<>("text"));
        LAT_lexeme_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        LAT_identifier_code.setCellValueFactory(new PropertyValueFactory<>("spCodeIdn"));
        LAT_constant_code.setCellValueFactory(new PropertyValueFactory<>("spCodeCon"));
        LAT_label_code.setCellValueFactory(new PropertyValueFactory<>("spCodeLbl"));

        LAI_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        LAI_name.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        LAI_type.setCellValueFactory(new PropertyValueFactory<>("type"));

        LAC_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        LAC_value.setCellValueFactory(new PropertyValueFactory<>("constant"));

        LAL_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        LAL_name.setCellValueFactory(new PropertyValueFactory<>("label"));
        LAL_line_from.setCellValueFactory(new PropertyValueFactory<>("lineFrom"));
        LAL_line_to.setCellValueFactory(new PropertyValueFactory<>("lineTo"));

        LAE_message.setCellValueFactory(new PropertyValueFactory<>("message"));
    }

    private void initParserTables() {
        PT_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        PT_stack.setCellValueFactory(new PropertyValueFactory<>("stack"));
        PT_ratio.setCellValueFactory(new PropertyValueFactory<>("ratio"));
        PT_source_sequence.setCellValueFactory(new PropertyValueFactory<>("sourceSequence"));
        PT_basis.setCellValueFactory(new PropertyValueFactory<>("basis"));

        PE_message.setCellValueFactory(new PropertyValueFactory<>("message"));
    }

    private void event_menu_run_lexical_analyser() {
        this.menu_run_lexical_analyser.setOnAction(event -> {
            run_lexical_analysis();
        });
    }

    private void run_lexical_analysis() {
        lexicalAnalyzer.run(codeArea.getText());
        setLA_tokens_table();
        setLA_identifiers_table();
        setLA_constants_table();
        setLA_labels_table();
        setLA_errors_table();
        clear_parsing_tables();
    }

    private void event_menu_run_parser() {
        this.menu_run_parser.setOnAction(event -> {
            run_lexical_analysis();
            run_parsing();
        });
    }

    private void run_parsing() {
        parser = new Parser(lexicalAnalyzer, precedence);
        parser.run();
        setParsing_table();
        setParsing_errors_table();
    }

    private void clear_parsing_tables() {
        parsing_table.setItems(null);
        parsing_errors_table.setItems(null);
    }

    private void event_menu_file_open() {
        menu_file_open.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            fileChooser.setInitialDirectory(new File("./res"));
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                try {
                    String text = new String(Files.readAllBytes(Paths.get(file.getPath())), Charset.defaultCharset());
                    codeArea.setText(text);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void event_menu_file_save() {
        menu_file_save.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.setInitialDirectory(new File("./res"));
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                try {
                    Files.write(Paths.get(file.getPath()), codeArea.getText().getBytes());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setLA_tokens_table() {
        ObservableList<Lexeme> lexemes = FXCollections.observableArrayList(lexicalAnalyzer.getTokenTable());
        LA_tokens_table.setItems(lexemes);
    }

    private void setLA_identifiers_table() {
        ObservableList<Identifier> identifiers = FXCollections.observableArrayList(lexicalAnalyzer.getIdentTable());
        LA_identifiers_table.setItems(identifiers);
    }

    private void setLA_constants_table() {
        ObservableList<Constant> constants = FXCollections.observableArrayList(lexicalAnalyzer.getConstTable());
        LA_constants_table.setItems(constants);
    }

    private void setLA_labels_table() {
        ObservableList<lexer.Label> labels = FXCollections.observableArrayList(lexicalAnalyzer.getLabelTable());
        LA_labels_table.setItems(labels);
    }

    private void setLA_errors_table() {
        ObservableList<LexicalError> errors = FXCollections.observableArrayList(lexicalAnalyzer.getLexicalErrors());
        LA_errors_table.setItems(errors);
        if (!errors.isEmpty()) {
            LA_errors_tab.setStyle("-fx-color: red");
        }
        else {
            LA_errors_tab.setStyle("-fx-color: #dddddd");
        }
    }

    private void setParsing_table() {
        ObservableList<ParsingTableRow> tableRows = FXCollections.observableArrayList(parser.getParsingTable().getParsingTableRows());
        parsing_table.setItems(tableRows);
    }

    private void setParsing_errors_table() {
        ObservableList<SyntaxError> errors = FXCollections.observableArrayList(parser.getSyntaxError());
        parsing_errors_table.setItems(errors);
        if (parser.getSyntaxError() != null) {
            parser_errors_tab.setStyle("-fx-color: red");
        }
        else {
            parser_errors_tab.setStyle("-fx-color: #dddddd");
        }
    }
}
