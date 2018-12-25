package parser;

import exceptions.syntactic.*;
import lexer.Element;
import lexer.IdentifierType;
import lexer.LexemeType;
import lexer.LexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SyntaxAnalyzer {
    private List<SyntaxException> syntaxExceptions;
    private HashMap<String, Integer> comparisonMarks;
    private LexicalAnalyzer lex;
    private final String nothing = "nothing";
    private int index;
    private Element currentLex;
    private int currentLine;

    public SyntaxAnalyzer() {
        syntaxExceptions = new ArrayList<>();
        this.lex = null;
        comparisonMarks = new HashMap<>();
        comparisonMarks.put("==", 2);
        comparisonMarks.put("!=", 3);
        comparisonMarks.put(">=", 4);
        comparisonMarks.put("<=", 5);
        comparisonMarks.put("<", 27);
        comparisonMarks.put(">", 28);
    }

    public void setLex(LexicalAnalyzer lex) {
        this.lex = lex;
    }

    public List<SyntaxException> getSyntaxExceptions() {
        return syntaxExceptions;
    }

    private void init() {
        index = -1;
        currentLine = -1;
        syntaxExceptions.clear();
    }

    public void run() {
        init();
        if (!lex.getLexicalExceptions().isEmpty()) {
            syntaxExceptions.add(new RejectException());
        } else if (lex == null || lex.getTokenTable().isEmpty()) {
            syntaxExceptions.add(new TokenTableIsEmptyException());
        } else {
            nextLex();  // select first token
            if (declarationsList()) {
                if (isExistNextLex()) {
                    nextLex();
                    statementsList();
                }
                else
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine + 1, "Statement", nothing));
                }
            }
        }
    }

    private boolean declarationsList() {
        if (declaration()) {
            if (isExistNextLex()) {
                nextLex();
                if (currentLex.getText().equals("{")) {
                    return true;
                } else {
                    return declarationsList();
                }
            } else {
                syntaxExceptions.add(new TokenExpectedException(currentLine + 1, "Statement unit", nothing));
            }
        }
        return false;
    }

    private boolean declaration() {
        if (type()) {
            if (isExistNextLex() && hasNextLexInLine()) {
                nextLex();
                return identifiersList();
            } else {
                syntaxExceptions.add(new IdentifierExpectedException(currentLex.getLine(), nothing));
            }
        } else {
            syntaxExceptions.add(new TypeExpectedException(currentLine, currentLex.getText()));
        }
        return false;
    }

    private boolean type() {
        for (IdentifierType tmp : IdentifierType.values()) {
            if (currentLex.getCode() == tmp.getValue()) {
                return true;
            }
        }
        return false;
    }

    private boolean identifier() {
        return currentLex.getCode() == LexemeType.IDENT.getValue();
    }

    private boolean coma() {
        if (currentLex.getText().equals(",")) {
            if (isExistNextLex() && hasNextLexInLine()) {
                nextLex();
                return identifiersList();
            } else {
                syntaxExceptions.add(new IdentifierExpectedException(currentLine, "nothing"));
            }
        } else {
            syntaxExceptions.add(new TokenExpectedException(currentLine, "','", currentLex.getText()));
        }
        return false;
    }

    private boolean identifiersList() {
        if (identifier()) {
            if (isExistNextLex() && hasNextLexInLine()) {
                nextLex();
                return coma();
            }
            return true;
        } else {
            syntaxExceptions.add(new IdentifierExpectedException(currentLine, currentLex.getText()));
        }
        return false;
    }

    private boolean statementsListWrapper() {

        if (isExistNextLex()) {
            nextLex();
            if (currentLex.getText().equals("{")) {
                if (isExistNextLex()) {
                    nextLex();
                    return statementsList();
                } else {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "Statement list", nothing));
                }
            } else {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "'{'", currentLex.getText()));
            }
        } else {
            syntaxExceptions.add(new TokenExpectedException(currentLine, "'{'", nothing));
        }
        return false;
    }

    private boolean statementsList() {
        if (currentLex.getText().equals("{")) {
            if (isExistNextLex()) {
                nextLex();
                if (statement()) {
                    if (isExistNextLex()) {
                        nextLex();
                        if (currentLex.getText().equals("}")) {
                            return true;
                        }
                        return statementsList();
                    } else {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "'}'", nothing));
                    }
                }
            }
            else {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "Statement list", nothing));
            }
        }
        return false;
    }

    private boolean statement() {
        if (condition())    // ConditionalTransition or ConditionalStatement
        {
            if (isExistNextLex()) {
                nextLex();
                if (conditionalTransition()) {
                    return true;
                }
                return conditionalStatement();
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "'{' or 'goto'", nothing));
            }
        } else if (cycle() || appropriation() || input() || output() || label())
        {
            return true;
        } else {
            syntaxExceptions.add(new TokenExpectedException(currentLine, "Statement", currentLex.getText()));
        }
        return false;
    }

    private boolean conditionalTransition() {
        if (currentLex.getText().equals("goto")) {
            if (isExistNextLex()) {
                nextLex();
                if (label()) {
                    return true;
                } else {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "Label", currentLex.getText()));
                }
            } else {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "Label", nothing));
            }
        }
        return false;
    }

    private boolean conditionalStatement()  // L | R else L | R else L | R else L | R else R
    {
        if (statementsList()) {
            if (isExistNextLex()) {
                nextLex();
                if (currentLex.getText().equals("else")) {
                    if (isExistNextLex()) {
                        nextLex();
                        if (conditionWrapper()) {
                            return conditionalStatement();
                        }
                        return statementsListWrapper();
                    }
                }
                else
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "else", currentLex.getText()));
                }
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "else", nothing));
            }
        }
        return false;
    }

    private boolean conditionWrapper()     // if (LogicalExpression)
    {
        if (currentLex.getText().equals("if")) {
            if (logicalExpressionWrapper()) {
                return true;
            } else {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "Logical Expression", currentLex.getText()));
            }
        } else {
            syntaxExceptions.add(new TokenExpectedException(currentLine, "'if'", nothing));
        }
        return false;
    }

    private boolean condition()     // if (LogicalExpression)
    {
        if (currentLex.getText().equals("if")) {
            if (logicalExpressionWrapper()) {
                return true;
            } else {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "Logical Expression", currentLex.getText()));
            }
        }
        return false;
    }

    private boolean logicalExpressionWrapper() {
        if (isExistNextLex()) {
            nextLex();
            if (logicalExpression())
            {
                return true;
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "Identifier or constant", currentLex.getText()));
            }
        } else {
            syntaxExceptions.add(new TokenExpectedException(currentLine, "'('", nothing));
        }
        return false;
    }

    private boolean logicalExpression() {
            if (currentLex.getText().equals("(")) {
                if (isExistNextLex() && hasNextLexInLine()) {
                    nextLex();
                    if (currentLex.getCode() == LexemeType.IDENT.getValue() || currentLex.getCode() == LexemeType.CONST.getValue()) {
                        if (isExistNextLex() && hasNextLexInLine()) {
                            nextLex();
                            if (comparisonMarks.containsKey(currentLex.getText())) {
                                if (isExistNextLex() && hasNextLexInLine()) {
                                    nextLex();
                                    if (currentLex.getCode() == LexemeType.IDENT.getValue() || currentLex.getCode() == LexemeType.CONST.getValue()) {
                                        if (isExistNextLex() && hasNextLexInLine()) {
                                            nextLex();
                                            if (currentLex.getText().equals(")")) {
                                                return true;
                                            } else {
                                                syntaxExceptions.add(new TokenExpectedException(currentLine, ")", currentLex.getText()));
                                            }
                                        } else {
                                            syntaxExceptions.add(new TokenExpectedException(currentLine, "')'", nothing));
                                        }
                                    } else {
                                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Identifier or constant", currentLex.getText()));
                                    }
                                } else {
                                    syntaxExceptions.add(new TokenExpectedException(currentLine, "Identifier or constant", nothing));
                                }
                            } else {
                                syntaxExceptions.add(new TokenExpectedException(currentLine, "Comparison mark", currentLex.getText()));
                            }
                        } else {
                            syntaxExceptions.add(new TokenExpectedException(currentLine, "Comparison mark", nothing));
                        }
                    } else {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Identifier or constant", currentLex.getText()));
                    }
                } else {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "Identifier or constant", nothing));
                }
            }
        return false;
    }

    private boolean cycle() {
        if (currentLex.getText().equals("repeat"))
        {
            if (statementsListWrapper())
            {
                if (isExistNextLex())
                {
                    nextLex();
                    if (currentLex.getText().equals("until"))
                    {
                        return logicalExpressionWrapper();
                    }
                    else
                    {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "'until'",currentLex.getText()));
                    }
                }
                else
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "'until'", nothing));
                }
            }
        }
        return false;
    }

    private boolean appropriation() {
        if (currentLex.getCode() == LexemeType.IDENT.getValue())
        {
            if (isExistNextLex() && hasNextLexInLine())
            {
                nextLex();
                if (currentLex.getText().equals("="))
                {
                    if (isExistNextLex() && hasNextLexInLine())
                    {
                        nextLex();
                        return expression();
                    }
                }
            }
        }
        return false;
    }

    private boolean expression() {
        if (term())
        {
            if (isExistNextLex() && hasNextLexInLine())
            {
                nextLex();
                if (currentLex.getText().equals("+") || currentLex.getText().equals("-"))
                {
                    if (isExistNextLex() && hasNextLexInLine())
                    {
                        nextLex();
                        return term();
                    }
                    else
                    {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Term", nothing));
                    }
                }
                else // todo need debugging
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "'+' or '-'", currentLex.getText()));
                }
            }
            return true;
        }
        return false;
    }

    private boolean term() {
        if (multiplier())
        {
            if (isExistNextLex() && hasNextLexInLine())
            {
                nextLex();
                if (currentLex.getText().equals("*") || currentLex.getText().equals("/"))
                {
                    if (isExistNextLex() && hasNextLexInLine())
                    {
                        nextLex();
                        return multiplier();
                    }
                    else
                    {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Multiplier", nothing));
                    }
                }
                else // todo need debugging
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "'*' or '/'", currentLex.getText()));
                }
            }
            return true;
        }
        return false;
    }

    private boolean multiplier() {
        if (currentLex.getCode() == LexemeType.CONST.getValue() || currentLex.getCode() == LexemeType.IDENT.getValue())
        {
            return true;
        }
        else if (currentLex.getText().equals("("))
        {
            if (isExistNextLex() && hasNextLexInLine())
            {
                nextLex();
                if (expression())
                {
                    if (isExistNextLex() && hasNextLexInLine())
                    {
                        nextLex();
                        if (currentLex.getText().equals(")"))
                        {
                            return true;
                        }
                        else
                        {
                            syntaxExceptions.add(new TokenExpectedException(currentLine, "')'", currentLex.getText()));
                        }
                    }
                    else
                    {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "')'", nothing));
                    }
                }
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "Expression", nothing));
            }
        }
        else
        {
            syntaxExceptions.add(new TokenExpectedException(currentLine, "Multiplier", currentLex.getText()));
        }
        return false;
    }

    private boolean input() {
        if (currentLex.getText().equals("in"))
        {
            if (isExistNextLex() && hasNextLexInLine())
            {
                nextLex();
                if (currentLex.getText().equals(">>"))
                {
                    if (isExistNextLex() && hasNextLexInLine())
                    {
                        nextLex();
                        if (currentLex.getCode() == LexemeType.IDENT.getValue())
                        {
                            return true;
                        }
                        else
                        {
                            syntaxExceptions.add(new TokenExpectedException(currentLine, "Identifier", currentLex.getText()));
                        }
                    }
                    else
                    {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Identifier", nothing));
                    }
                }
                else
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, ">>", currentLex.getText()));
                }
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine, ">>", nothing));
            }
        }
        return false;
    }
    // todo fix conflict between expression and ternary statement ( '(' in two ways)
    private boolean output() {
        if (currentLex.getText().equals("out"))
        {
            if (isExistNextLex() && hasNextLexInLine())
            {
                nextLex();
                if (currentLex.getText().equals("<<"))
                {
                    if (isExistNextLex() && hasNextLexInLine())
                    {
                        nextLex();
                        if (expression() || ternaryStatement())
                        {
                            return true;
                        }
                        else
                        {
                            syntaxExceptions.add(new TokenExpectedException(currentLine, "Expression", currentLex.getText()));
                        }
                    }
                    else
                    {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Expression", nothing));
                    }
                }
                else
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "<<", currentLex.getText()));
                }
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "<<", nothing));
            }
        }
        return false;
    }


    private boolean ternaryStatement() {
        if (logicalExpressionWrapper())
        {
            if (isExistNextLex() && hasNextLexInLine())
            {
                nextLex();
                if (currentLex.getText().equals("?"))
                {
                    if (isExistNextLex() && hasNextLexInLine())
                    {
                        nextLex();
                        if (expression())
                        {
                            if (isExistNextLex() && hasNextLexInLine())
                            {
                                nextLex();
                                if (currentLex.getText().equals(":"))
                                {
                                    if (isExistNextLex() && hasNextLexInLine())
                                    {
                                        nextLex();
                                        return expression();
                                    }
                                    else
                                    {
                                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Expression", nothing));
                                    }
                                }
                                else
                                {
                                    syntaxExceptions.add(new TokenExpectedException(currentLine, "':'", currentLex.getText()));
                                }
                            }
                            else
                            {
                                syntaxExceptions.add(new TokenExpectedException(currentLine, "':'", nothing));
                            }
                        }
                    }
                    else
                    {
                        syntaxExceptions.add(new TokenExpectedException(currentLine, "Expression", nothing));
                    }
                }
                else
                {
                    syntaxExceptions.add(new TokenExpectedException(currentLine, "'?'", currentLex.getText()));
                }
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine, "'?'", nothing));
            }
        }
        return false;
    }

    private boolean label() {
        return currentLex.getCode() == LexemeType.LABEL.getValue();
    }

    private boolean isExistNextLex()    // has next token in token table
    {
        return index + 1 < lex.getTokenTable().size();
    }

    private boolean hasNextLexInLine() // has next token in the current line
    {
        return lex.getTokenTable().get(index + 1).getLine() == currentLine;
    }

    private void nextLex() {
        index++;
        currentLex = lex.getTokenTable().get(index);
        currentLine = currentLex.getLine();
    }
}
