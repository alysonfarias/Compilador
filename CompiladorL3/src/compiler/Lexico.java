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

	private boolean isDigitOrLetter(char c) {
		return isLetter(c) || isDigit(c);
	}

	public Token nextToken() {
		Token token = null;
		char c;
		int state = 0;

		StringBuffer lexeme = new StringBuffer();
		while (this.hasNextChar()) {
			c = this.nextChar();
			switch (state) {
			case 0:
				if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
					state = 0;
				} else if ((this.isLetter(c) || c == '_')) {
					lexeme.append(c);
					state = 1;
				} else if (this.isDigit(c)) {
					lexeme.append(c);
					state = 2;
				} else if (c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';') {
					lexeme.append(c);
					state = 5;
				} else if (c == '+' || c == '-' || c == '*' || c == '%') {
					lexeme.append(c);
					state = 6;
				} else if (c == '=') {
					lexeme.append(c);
					state = 7;
				} else if (c == '/') {
					lexeme.append(c);
					state = 8;
				} else if (c == '\'') {
					lexeme.append(c);
					state = 10;
				} else if (c == '+' || c == '-') {
					lexeme.append(c);
					state = 13;
				} else if (c == '$') {
					lexeme.append(c);
					state = 99;
					this.back();
				} else {
					lexeme.append(c);
					throw new RuntimeException("ERROR: INVALID TOKEN MAIN\"" + lexeme.toString() + "\"");
				}
				break;
			case 1:
				if (this.isLetter(c) || this.isDigit(c) || c == '_') {
					lexeme.append(c);
					state = 1;
				} else {
					this.back();
					return new Token(lexeme.toString(), Token.IDENTIFIER_TYPE);
				}
				break;
			case 2:
				if (this.isDigit(c)) {
					lexeme.append(c);
					state = 2;
				} else if (c == '.') {
					lexeme.append(c);
					state = 3;
				} else {
					this.back();
					return new Token(lexeme.toString(), Token.INTEGER_TYPE);
				}
				break;
			case 3:
				if (this.isDigit(c)) {
					lexeme.append(c);
					state = 4;
				} else {
					throw new RuntimeException("ERROR: REAL NUMBER BADLY FORMATTED \"" + lexeme.toString() + "\"");
				}
				break;
			case 4:
				if (this.isDigit(c)) {
					lexeme.append(c);
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
				this.back();
				return new Token(lexeme.toString(), Token.ARITHMETIC_OPERATOR);
			case 7:
				if (c == '=') {
					// TODO OPERADOR RELACIONAL
				} else {
					this.back();
					return new Token(lexeme.toString(), Token.ASSIGNMENT_TYPE);
				}
				break;
			case 8:
				if (c == '/') {
					lexeme.append(c);
					state = 9;
				} else {
					this.back();
					return new Token(lexeme.toString(), Token.ARITHMETIC_OPERATOR);
				}
				break;
			case 9:
				// INLINE COMMENT EM PROGRESSO
				if (this.isDigitOrLetter(c) || c == '=') {
					lexeme.append(c);
					state = 9;
					break;
				}

				state = 13;
				break;

			case 10:
				if (this.isDigit(c) || this.isLetter(c)) {
					lexeme.append(c);
					state = 11;
				} else {
					throw new RuntimeException("ERROR: CHAR BADLY FORMATTED \"" + lexeme.toString() + "\"");
				}
				break;
			case 11:
				if (c == '\'') {
					lexeme.append(c);
					return new Token(lexeme.toString(), Token.CHAR_TYPE);
				} else {
					throw new RuntimeException("ERROR: CHAR BADLY FORMATTED\"" + lexeme.toString() + "\"");
				}
			case 12:
				// TODO: Fazer o ++ e o --
				if ((c == '+' || c == '-')) {
					lexeme.append(c);
					return new Token(lexeme.toString(), Token.DE_IN_CREMENT);
				} else {
					throw new RuntimeException("ERROR: CREMENT  BADLY FORMATTED\"" + lexeme.toString() + "\"");
				}
			case 13:
				this.back();
				return new Token(lexeme.toString(), Token.INLINE_COMMENT);

			case 99:
				return new Token(lexeme.toString(), Token.ENDCODE_TYPE);
			}
		}
		return token;
	}
}
