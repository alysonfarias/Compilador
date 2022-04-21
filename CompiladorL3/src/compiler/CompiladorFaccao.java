package compiler;

public class CompiladorFaccao {

    public static void main(String[] args) {
        Lexico lexico = new Lexico("CompiladorL3\\src\\compiler\\codigo.txt");
        Token t = null;
        while ((t = lexico.nextToken()) != null) {
            System.out.println(t.toString());
        }
    }
}
