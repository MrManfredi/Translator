package parser;

import exceptions.syntactic.*;
import lexer.Element;
import lexer.IdentifierType;
import lexer.LexemeType;
import lexer.LexicalAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {
    private List<SyntaxException> syntaxExceptions;
    private LexicalAnalyzer lex;
    private int index;
    private Element currentLex;
    private int currentLine;

    public SyntaxAnalyzer()
    {
        syntaxExceptions = new ArrayList<>();
        this.lex = null;
    }

    public SyntaxAnalyzer(LexicalAnalyzer lex) {
        syntaxExceptions = new ArrayList<>();
        this.lex = lex;
    }

    public void setLex(LexicalAnalyzer lex) {
        this.lex = lex;
    }

    public List<SyntaxException> getSyntaxExceptions() {
        return syntaxExceptions;
    }

    private void init()
    {
        index = -1;
        currentLine = -1;
        syntaxExceptions.clear();
    }

    public void run()
    {
        init();
        if (!lex.getLexicalExceptions().isEmpty())
        {
            syntaxExceptions.add(new RejectException());
        }
        else if (lex == null || lex.getTokenTable().isEmpty())
        {
            syntaxExceptions.add(new TokenTableIsEmptyException());
        }
        else {
            nextLex();  // select first token
            if (declarationsList()) {
                // todo if (statementsList())
            }
        }
    }

    private boolean declarationsList() {
        if (declaration())
        {
            if (isExistNextLex()) {
                nextLex();
                if (currentLex.getText().equals("{"))
                {
                    return true;
                }
                else {
                    return declarationsList();
                }
            }
            else
            {
                syntaxExceptions.add(new TokenExpectedException(currentLine + 1, "Statement unit", "nothing"));
            }
        }
        return false;
    }

    private boolean declaration() {
        if (type()) {
            if (isExistNextLex() && hasNextLex()) {
                nextLex();
                return identifiersList();
            }
            else {
                syntaxExceptions.add(new IdentifierExpectedException(currentLex.getLine(), "nothing"));
            }
        }
        else {
            syntaxExceptions.add(new TypeExpectedException(currentLine, currentLex.getText()));
        }
        return false;
    }

    private boolean type() {
        for (IdentifierType tmp : IdentifierType.values())
        {
            if (currentLex.getCode() == tmp.getValue())
            {
                return true;
            }
        }
        return false;
    }

    private boolean identifier() {
        return currentLex.getCode() == LexemeType.IDENT.getValue();
    }

    private boolean coma()
    {
        if (currentLex.getText().equals(",")) {
            if (isExistNextLex() && hasNextLex()) {
                nextLex();
                return identifiersList();
            }
            else {
                syntaxExceptions.add(new IdentifierExpectedException(currentLine, "nothing"));
            }
        }
        else {
            syntaxExceptions.add(new TokenExpectedException(currentLine, "','", currentLex.getText()));
        }
        return false;
    }

    private boolean identifiersList() {
        if (identifier()) {
            if (isExistNextLex() && hasNextLex()) {
                nextLex();
                return coma();
            }
            return true;
        }
        else {
            syntaxExceptions.add(new IdentifierExpectedException(currentLine, currentLex.getText()));
        }
        return false;
    }

    private boolean isExistNextLex()    // has next token in token table
    {
        return index + 1 < lex.getTokenTable().size();
    }

    private boolean hasNextLex() // has next token in the current line
    {
        return lex.getTokenTable().get(index + 1).getLine() == currentLine;
    }

    private void nextLex()
    {
        index++;
        currentLex = lex.getTokenTable().get(index);
        currentLine = currentLex.getLine();
    }
}
