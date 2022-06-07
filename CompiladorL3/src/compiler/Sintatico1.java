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

public class Sintatico1 {
    private Lexico lexico;
    private Token token;
    
    public Sintatico1(Lexico lexico){
        this.lexico = lexico;
    }
    
    public void S(){//S determina estado inicial
        this.token = this.lexico.nextToken();
        this.E();
        if(this.token.getType() == Token.ENDCODE_TYPE){
            System.out.println("O Código tá massa! Arretado! Tu botou pra torar!");        
        }else{
            System.out.println("Faiô no fim!");       
        }
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
