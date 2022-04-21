package compiler;

public class Token {
    /*
    /*
    */
    public static int IDENTIFIER_TYPE = 1;
    public static int INTEGER_TYPE = 2;
    public static int REAL_TYPE = 3;
    public static int CHAR_TYPE = 4;
    public static int RELATIONAL_OPERATOR = 5;
    public static int ARITHMETIC_OPERATOR = 6;
    public static int SPECIAL_CHARACTER = 7;
    public static int ASSIGNMENT_TYPE = 8;
    public static int INLINE_COMMENT = 9;
    public static int RESERVED_WORD = 10;
    public static int INCREMENT = 11; 
    public static int DECREMENT = 22;
    //TODO: ALTERAR A ORDEM APOS MERGER p deixar sequencial, tanto aqui quanto no metodo toString()

    public static int ENDCODE_TYPE = 99;

    private int type;
    private String lexeme;

    public Token(String lexeme, int tipo) {
        this.lexeme = lexeme;
        this.type = tipo;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        switch (type) {
            case 1:
                return this.lexeme + " - IDENTIFIER";
            case 2:
                return this.lexeme + " - INTEGER";
            case 3:
                return this.lexeme + " - REAL";
            case 4:
                return this.lexeme + " - CHAR";
            case 5:
                return this.lexeme + " - RELATIONAL_OPERATOR";
            case 6:
                return this.lexeme + " - ARITHMETIC_OPERATOR";
            case 7:
                return this.lexeme + " - SPECIAL_CHARACTER";
            case 8:
                return this.lexeme + " - ASSIGNMENT";
            case 9:
                return this.lexeme + " - INLINE_COMMENT";
            case 10:
                return this.lexeme + " - RESERVED_WORD";
            case 11:
                return this.lexeme + " - INCREMENTAL_OPERATOR";
            case 22:
                return this.lexeme + " - DECREMENTAL_OPERATOR";
            case 99:
                return this.lexeme + " - END_CODE";
        }
        return "";
    }

}
