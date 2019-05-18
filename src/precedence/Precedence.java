package precedence;

import grammar.Grammar;
import grammar.GrammarParser;
import grammar.RightSide;
import grammar.Rule;
import parser.Sharp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Precedence {
    private Grammar grammar;
    private PrecedenceStorage precedenceStorage;

    public Precedence(Grammar grammar) {
        this.grammar = grammar;
        this.precedenceStorage = new PrecedenceStorage();
    }

    public void computePrecedences() {
        precedenceStorage = new PrecedenceStorage();
        List<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            List<RightSide> rightSides = rule.getRightSides();
            for (RightSide rightSide : rightSides) {
                List<String> words = rightSide.getWords();

                for (int i = 0; i < words.size() - 1; i++) {
                    String leftWord = words.get(i);
                    String rightWord = words.get(i + 1);
                    List<String> firstPlusRight = new ArrayList<>();
                    List<String> lastPlusLeft = new ArrayList<>();
                    // 1) equal (first rule)
                    precedenceStorage.addСorrelation(leftWord, rightWord, RatioType.EQUAL);
                    // 2) less (second rule)
                    if (grammar.getNonterminals().contains(rightWord))
                    {
                        firstPlusRight = calculateFirstPlus(rightWord);
                        for (String firstPlusRightWord : firstPlusRight) {
                            precedenceStorage.addСorrelation(leftWord, firstPlusRightWord, RatioType.LESS);
                        }
                    }
                    // 3.1) more (third rule first part)
                    if (grammar.getNonterminals().contains(leftWord)) {
                        lastPlusLeft = calculateLastPlus(leftWord);
                        if (!grammar.getNonterminals().contains(rightWord)) {
                            for (String word : lastPlusLeft) {
                                precedenceStorage.addСorrelation(word, rightWord, RatioType.MORE);
                            }
                        }
                    }
                    // 3.2) more (third rule second part)
                    if (!firstPlusRight.isEmpty() && !lastPlusLeft.isEmpty()) {
                        for (String lastLeft : lastPlusLeft) {
                            for (String firstRight : firstPlusRight) {
                                precedenceStorage.addСorrelation(lastLeft, firstRight, RatioType.MORE);
                            }
                        }
                    }
                }
            }
        }
        Set<String> units = precedenceStorage.getPrecedenceStorage().keySet();
        PrecedenceStorage tempPrecedenceStorage = new PrecedenceStorage();
        for (String unit : units) {
            tempPrecedenceStorage.addСorrelation(Sharp.name(), unit, RatioType.LESS);
            tempPrecedenceStorage.addСorrelation(unit, Sharp.name(), RatioType.MORE);
        }
        precedenceStorage.getPrecedenceStorage().putAll(tempPrecedenceStorage.getPrecedenceStorage());
    }

    private List<String> calculateFirstPlus(String word) {
        List<String> firstPlus = calculateFirst(word);
        for (int i = 0; i < firstPlus.size(); i++) {
            if (grammar.getNonterminals().contains(firstPlus.get(i))) {
                for (String tempWord : calculateFirst(firstPlus.get(i))) {
                    if (!firstPlus.contains(tempWord)) {
                        firstPlus.add(tempWord);
                    }
                }
            }
        }
        return firstPlus;
    }

    private List<String> calculateFirst(String word) {
        List<String> first = new ArrayList<>();
        for (RightSide rightSide : grammar.getRule(word).getRightSides()) {
            String temp = rightSide.getWords().get(0);      // the first word of each right side
            if (!first.contains(temp)) {
                first.add(temp);
            }
        }
        return first;
    }

    private List<String> calculateLastPlus(String word) {
        List<String> lastPlus = calculateLast(word);
        for (int i = 0; i < lastPlus.size(); i++) {
            if (grammar.getNonterminals().contains(lastPlus.get(i))) {
                for (String tempWord : calculateLast(lastPlus.get(i))) {
                    if (!lastPlus.contains(tempWord)) {
                        lastPlus.add(tempWord);
                    }
                }
            }
        }
        return lastPlus;
    }

    private List<String> calculateLast(String word) {
        List<String> first = new ArrayList<>();
        for (RightSide rightSide : grammar.getRule(word).getRightSides()) {
            String temp = rightSide.getWords().get(rightSide.getWords().size() - 1);      // the last word of each right side
            if (!first.contains(temp)) {
                first.add(temp);
            }
        }
        return first;
    }

    public PrecedenceStorage getPrecedenceStorage() {
        return precedenceStorage;
    }

    public static void main(String[] args) {
        Grammar grammar = GrammarParser.parse("res/grammar.json");
        Precedence precedence = new Precedence(grammar);
        PrecedenceTable.createHtmlFile(precedence.getPrecedenceStorage(), "res/precedence_table.html");
        System.out.println("From class Precedence");
    }
}
