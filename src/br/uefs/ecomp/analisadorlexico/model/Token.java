/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

/**
 *
 * @author alyso
 */
public class Token {
    private int id;
    private String nome;
    private String lexema;
    private String linha;
    
    public Token(int id, String nome, String lexema, String linha){
        this.id = id;
        this.nome = nome;
        this.lexema = lexema;
        this.linha = linha;
    }
    
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
     this.nome = nome;   
    }
    
    public String getLexema(){
        return lexema;
    }
    public void setLexema(String lexema){
        this.lexema = lexema;
    }
    
    public String getLinha(){
        return linha;
    }
    public void setLinha(String linha){
        this.linha = linha;
    }
    
}
