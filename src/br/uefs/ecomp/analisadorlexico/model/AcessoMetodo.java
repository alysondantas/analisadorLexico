/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

/**
 *
 * @author Aleatorio
 */
public class AcessoMetodo {
    private String nomeObjeto;
    private String nomeMetodo;
    private String linha;

    public AcessoMetodo(String nomeObjeto, String nomeMetodo, String linha) {
        this.nomeObjeto = nomeObjeto;
        this.nomeMetodo = nomeMetodo;
        this.linha = linha;
    }

    public String getNomeObjeto() {
        return nomeObjeto;
    }

    public void setNomeObjeto(String nomeObjeto) {
        this.nomeObjeto = nomeObjeto;
    }

    public String getNomeMetodo() {
        return nomeMetodo;
    }

    public void setNomeMetodo(String nomeMetodo) {
        this.nomeMetodo = nomeMetodo;
    }
    
    
}
