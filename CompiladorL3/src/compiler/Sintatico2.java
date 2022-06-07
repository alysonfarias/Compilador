/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

/**
 *
 * @author tarci
 */

public class Sintatico2 {
    private Lexico lexico;
    private Token token;
    
    public Sintatico2(Lexico lexico){
        this.lexico = lexico;
    }
    
    public void S(){//S determina estado inicial
        this.token = this.lexico.nextToken();
        if(!token.getLexeme().equals("main")){
            throw new RuntimeException("Oxe, cadê main?");
        }
        
        this.token = this.lexico.nextToken();
        if(!token.getLexeme().equals("(")){
            throw new RuntimeException("Abre o parêntese do main cabra!");
        }
        
        this.token = this.lexico.nextToken();
        if(!token.getLexeme().equals(")")){
            throw new RuntimeException("Fechar o parêntese do main cabra!");
        }        
        this.token = this.lexico.nextToken();
        
        this.B();
        if(this.token.getType() == Token.ENDCODE_TYPE){
            System.out.println("O Código tá massa! Arretado! Tu botou pra torar!");        
        }else{
            throw new RuntimeException("Oxe, eu deu bronca preto do fim do programa.");
        }
    }
    
    private void B(){
        if(!this.token.getLexeme().equals("{")){
            throw new RuntimeException("Oxe, tave esperando um \"{\" pertinho de " + this.token.getLexeme());
        }
        this.token = this.lexico.nextToken();
        this.CS();
        if(!this.token.getLexeme().equals("}")){
            throw new RuntimeException("Oxe, tava esperando um \"}\" pertinho de " + this.token.getLexeme());
        }        
        this.token = this.lexico.nextToken();        
    }
    
    private void CS(){
        if((this.token.getType() == Token.IDENTIFIER_TYPE) ||
            this.token.getLexeme().equals("int") ||
            this.token.getLexeme().equals("float")){
            
            this.C();
            this.CS();
        }else{
        
        }       
    }
    
    private void C(){
        if(this.token.getType() == Token.IDENTIFIER_TYPE){
            this.ATRIBUICAO();            
        }else if(this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float")){
            this.DECLARACAO();
        }else{
            throw new RuntimeException("Oxe, eu tava esperando tu "
                    + "declarar um comando pertinho de :" + this.token.getLexeme());
        }
    }
        
    private void DECLARACAO(){
        if(!(this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float"))){
            throw new RuntimeException("Tu vacilou na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexeme());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getType() != Token.IDENTIFIER_TYPE){
            throw new RuntimeException("Tu vacilou na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexeme());
        }
        this.token = this.lexico.nextToken();
        if(!this.token.getLexeme().equalsIgnoreCase(";")){
            throw new RuntimeException("Tu vacilou  na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexeme());
        }
        this.token = this.lexico.nextToken();        
    }
    
    private void ATRIBUICAO(){
        if(this.token.getType() != Token.IDENTIFIER_TYPE){
            throw new RuntimeException("Erro na atribuição. Pertinho de: " + this.token.getLexeme());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getType() != Token.ASSIGNMENT_TYPE){
            throw new RuntimeException("Erro na atribuição. Pertinho de: " + this.token.getLexeme());
        }
        this.token = this.lexico.nextToken();
        this.E();
        if(!this.token.getLexeme().equals(";")){
            throw new RuntimeException("Erro na atribuição. Pertinho de: " + this.token.getLexeme());
        }
        this.token = this.lexico.nextToken();                
    }
    
    private void E(){
        this.T();
        this.El();
    }
    
    private void El(){
        if(this.token.getType() == Token.ARITHMETIC_OPERATOR){
            this.OP();
            this.T();
            this.El();
        }else{        
        }
    }
    
    private void T(){
        if(this.token.getType() == Token.IDENTIFIER_TYPE || 
                this.token.getType() == Token.INTEGER_TYPE ||
                this.token.getType() == Token.REAL_TYPE){
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Oxe, era para ser um identificador "
                    + "ou número pertinho de " + this.token.getLexeme());
        }
    }
    
    private void OP(){
        if(this.token.getType() == Token.ARITHMETIC_OPERATOR){
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Oxe, era para ser um operador "
                    + "aritmético (+,-,/,*) pertinho de "  + 
                    this.token.getLexeme());
        }
    }
    
    
    
}
