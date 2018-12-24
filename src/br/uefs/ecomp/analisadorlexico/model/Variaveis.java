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
    
}
