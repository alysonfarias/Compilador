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

	private boolean LetterOrDigit(char c) {
		return this.isLetter(c) || this.isDigit(c);
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
		char c;
		int state = 0;
		String lexeme = "";

		while (this.hasNextChar()) {
			c = this.nextChar();
			switch (state) {
			case 0:
				if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
					state = 0;
				} else if ((this.isLetter(c) || c == '_')) {
					lexeme += c;
					state = 1;
				} else if (this.isDigit(c)) {
					lexeme += c;
					state = 2;
				} else if (c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';' || c == '[' || c == ']'
						|| c == '|') {
					lexeme += c;
					state = 5;
				} else if (c == '+' || c == '-' || c == '*' || c == '%') {
					lexeme += c;
					state = 6;
				} else if (c == '=') {
					lexeme += c;
					state = 7;
				} else if (c == '<' || c == '>') {
					lexeme += c;
					state = 15;
				}

				else if (c == '/') {
					lexeme += c;
					state = 8;
				} else if (c == '\'') {
					lexeme += c;
					state = 10;
				} else if (c == '+' || c == '-') {
					lexeme += c;
					state = 13;
				} else if (c == '$') {
					lexeme += c;
					state = 99;
					this.back();
				} else {
					lexeme += c;
					throw new RuntimeException("ERROR: INVALID TOKEN MAIN\"" + lexeme.toString() + "\"");
				}
				break;
			case 1:
				if (this.isLetter(c) || this.isDigit(c) || c == '_') {
					lexeme += c;
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
				if (this.isDigit(c)) {
					lexeme += c;
					state = 2;
				} else if (c == '.') {
					lexeme += c;
					state = 3;
				} else {
					this.back();
					return new Token(lexeme.toString(), Token.INTEGER_TYPE);
				}
				break;
			case 3:
				if (this.isDigit(c)) {
					lexeme += c;
					state = 4;
				} else {
					throw new RuntimeException("ERROR: REAL NUMBER BADLY FORMATTED \"" + lexeme.toString() + "\"");
				}
				break;
			case 4:
				if (this.isDigit(c)) {
					lexeme += c;
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
					lexeme += c;
					state = 14;
				} else {
					this.back();
					return new Token(lexeme.toString(), Token.ASSIGNMENT_TYPE);
				}
				break;
			case 8:
				if (c == '/') {
					lexeme += c;
					state = 9;
				} else if (c == '*') {
					lexeme += c;
					state = 16;
				} else {
					this.back();
					return new Token(lexeme.toString(), Token.ARITHMETIC_OPERATOR);
				}
				break;
			case 9:

				if (c == '\n' || c == '\t' || c == '\r') {
					state = 13;
					break;

				} else {
					lexeme += c;
					state = 9;
					break;
				}

			case 10:
				if (this.isDigit(c) || this.isLetter(c)) {
					lexeme += c;
					state = 11;
				} else {
					throw new RuntimeException("ERROR: CHAR BADLY FORMATTED \"" + lexeme.toString() + "\"");
				}
				break;
			case 11:
				if (c == '\'') {
					lexeme += c;
					return new Token(lexeme.toString(), Token.CHAR_TYPE);
				} else {
					throw new RuntimeException("ERROR: CHAR BADLY FORMATTED\"" + lexeme.toString() + "\"");
				}
			case 12:
				// TODO: Fazer o ++ e o --
				if ((c == '+' || c == '-')) {
					lexeme += c;
					return new Token(lexeme.toString(), Token.DE_IN_CREMENT);
				} else {
					throw new RuntimeException("ERROR: CREMENT  BADLY FORMATTED\"" + lexeme.toString() + "\"");
				}
			case 13:
				this.back();
				return new Token(lexeme.toString(), Token.INLINE_COMMENT);
			case 14:
				this.back();
				return new Token(lexeme.toString(), Token.RELACIONAL_OPERATOR);
			case 15:
				if (c == '=') {
					lexeme += c;
					state = 14;
					break;
				}
				this.back();
				return new Token(lexeme.toString(), Token.RELACIONAL_OPERATOR);
			case 99:
				return new Token(lexeme.toString(), Token.ENDCODE_TYPE);
			}
		}
		return token;
	}
}
