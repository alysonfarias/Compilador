package compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexico {

    private char[] content;
    private int index;

    public Lexico(String srcPath) {
        try {
            String stringContent;
            stringContent = new String(Files.readAllBytes(Paths.get(srcPath)));
            this.content = stringContent.toCharArray();
            this.index = 0;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private char nextChar() {
        return this.content[this.index++];
    }

    public char tokenBefore() {
        if (this.index == 0) {
            return this.content[this.index];
        } else {
            return this.content[this.index - 1];
        }
    }

    private boolean hasNextChar() {
        return index < this.content.length;
    }

    private void back() {
        this.index--;
    }

    private boolean isLetter(char c) {
        return (c >= 'a') && (c <= 'z') || (c >= 'A') && (c <= 'Z');
    }

    private boolean isDigit(char c) {
        return (c >= '0') && (c <= '9');
    }

    private boolean isReserved(String c) {
        if (c.compareTo("main") == 0 || c.compareTo("if") == 0 || c.compareTo("else") == 0 || c.compareTo("while") == 0
                || c.compareTo("do") == 0 || c.compareTo("for") == 0 || c.compareTo("int") == 0
                || c.compareTo("float") == 0 || c.compareTo("char") == 0) {
            return true;
        }
        return false;
    }

    public Token nextToken() {
        Token token = null;
        char currentChar;
        int state = 0;
        String lexeme = "";

        while (this.hasNextChar()) {
            currentChar = this.nextChar();
            switch (state) {
                case 0:
                    if (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r') {
                        state = 0;
                    } else if ((this.isLetter(currentChar) || currentChar == '_')) {
                        lexeme += currentChar;
                        state = 1;
                    } else if (this.isDigit(currentChar)) {
                        lexeme += currentChar;
                        state = 2;
                    } else if (currentChar == ')' || currentChar == '(' || currentChar == '{' || currentChar == '}'
                            || currentChar == ',' || currentChar == ';' || currentChar == '[' || currentChar == ']'
                            || currentChar == '|') {
                        lexeme += currentChar;
                        state = 5;
                    } else if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '%') {
                        lexeme += currentChar;
                        state = 6;
                    } else if (currentChar == '=') {
                        lexeme += currentChar;
                        state = 7;
                    } else if (currentChar == '<' || currentChar == '>') {
                        lexeme += currentChar;
                        state = 15;
                    } else if (currentChar == '/') {
                        lexeme += currentChar;
                        state = 8;
                    } else if (currentChar == '\'') {
                        lexeme += currentChar;
                        state = 10;
                    } else if (currentChar == '$') {
                        lexeme += currentChar;
                        state = 99;
                        this.back();
                    } else if (currentChar == '!') {
                        lexeme += currentChar;
                        state = 7;
                    } else {
                        lexeme += currentChar;
                        throw new RuntimeException("ERROR: INVALID TOKEN MAIN\"" + lexeme.toString() + "\"");
                    }
                    break;
                case 1:
                    if (this.isLetter(currentChar) || this.isDigit(currentChar) || currentChar == '_') {
                        lexeme += currentChar;
                        state = 1;

                    } else {
                        if (this.isReserved(lexeme)) {
                            this.back();
                            return new Token(lexeme.toString(), Token.RESERVED_WORD);
                        }
                        this.back();
                        return new Token(lexeme.toString(), Token.IDENTIFIER_TYPE);
                    }
                    break;
                case 2:
                    if (this.isDigit(currentChar)) {
                        lexeme += currentChar;
                        state = 2;
                    } else if (currentChar == '.') {
                        lexeme += currentChar;
                        state = 3;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.INTEGER_TYPE);
                    }
                    break;
                case 3:
                    if (this.isDigit(currentChar)) {
                        lexeme += currentChar;
                        state = 4;
                    } else {
                        throw new RuntimeException("ERROR: REAL NUMBER BADLY FORMATTED \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 4:
                    if (this.isDigit(currentChar)) {
                        lexeme += currentChar;
                        state = 4;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.REAL_TYPE);
                    }
                    break;
                case 5:
                    this.back();
                    return new Token(lexeme.toString(), Token.SPECIAL_CHARACTER);
                case 6:
                    if (currentChar == '+') {
                        lexeme += currentChar;
                        return new Token(lexeme.toString(), Token.INCREMENT_OPERATOR);
                    } else if (currentChar == '-') {
                        lexeme += currentChar;
                        return new Token(lexeme.toString(), Token.DECREMENT_OPERATOR);
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.ARITHMETIC_OPERATOR);
                    }
                case 7:
                    if (currentChar == '=') {
                        lexeme += currentChar;
                        state = 14;
                    } else {
                        return new Token(lexeme.toString(), Token.ASSIGNMENT_TYPE);
                    }
                    break;
                case 8:
                    if (currentChar == '/') {
                        lexeme += currentChar;
                        state = 9;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.ARITHMETIC_OPERATOR);
                    }
                    break;
                case 9:
                    if (currentChar == '\n' || currentChar == '\t' || currentChar == '\r') {
                        state = 13;
                        break;
                    } else {
                        lexeme += currentChar;
                        state = 9;
                        break;
                    }
                case 10:
                    if (currentChar != '\'') {
                        lexeme += currentChar;
                        state = 11;
                    } else {
                        throw new RuntimeException("ERROR: CHAR BADLY FORMATTED \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 11:
                    if (currentChar == '\'') {
                        lexeme += currentChar;
                        return new Token(lexeme.toString(), Token.CHAR_TYPE);
                    } else {
                        throw new RuntimeException("ERROR: CHAR BADLY FORMATTED\"" + lexeme.toString() + "\"");
                    }
                case 13:
                    this.back();
                    return new Token(lexeme.toString(), Token.INLINE_COMMENT);
                case 14:
                    this.back();
                    return new Token(lexeme.toString(), Token.RELATIONAL_OPERATOR);
                case 15:
                    if (currentChar == '=') {
                            lexeme += currentChar;
                            state = 14;
                            break;
                    }
                    this.back();
                    return new Token(lexeme.toString(), Token.RELATIONAL_OPERATOR);
                case 99:
                    return new Token(lexeme.toString(), Token.ENDCODE_TYPE);
            }
        }
        return token;
    }
}
