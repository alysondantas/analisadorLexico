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
class Variaveis {
    private Token token;
    private String tipo;
    private String nomeClasse;
    private String nomeMetodo;

    Variaveis(Token t, String tipo) {
        this.token = t;
        this.tipo = tipo;
    }

    public Token getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public String getNomeMetodo() {
        return nomeMetodo;
    }

    public void setNomeMetodo(String nomeMetodo) {
        this.nomeMetodo = nomeMetodo;
    }
    
}
