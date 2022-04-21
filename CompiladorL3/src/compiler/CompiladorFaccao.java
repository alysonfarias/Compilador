package compiler;

public class CompiladorFaccao {

    public static void main(String[] args) {
        Lexico lexico = new Lexico("src\\compiler\\codigo.txt");
        Token t = null;
        while ((t = lexico.nextToken()) != null) {
            System.out.println(t.toString());
        }
    }
}
