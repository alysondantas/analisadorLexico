/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author alyso
 */
public class Token {
    private static final AtomicInteger count = new AtomicInteger(0); 
    private int id;
    private int idTipo;
    private String nome;
    private String lexema;
    private int linha;
    
    public Token(int idTipo, String nome, String lexema, int linha){
        this.id = count.incrementAndGet();
        this.idTipo = idTipo;
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
    
    public int getIdTipo(){
        return idTipo;
    }
    public void setIdTipo(int i){
        this.idTipo = i;
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
    
    public int getLinha(){
        return linha;
    }
    public void setLinha(int linha){
        this.linha = linha;
    }
    
}
