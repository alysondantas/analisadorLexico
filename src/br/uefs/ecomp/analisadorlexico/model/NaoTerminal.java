/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.ArrayList;

/**
 *
 * @author Aleatorio
 */
public class NaoTerminal {
    private String nome;
    private ArrayList<NaoTerminal> derivacoes;
    private boolean vazio;
    
    public NaoTerminal(String nome){
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<NaoTerminal> getDerivacoes() {
        return derivacoes;
    }

    public void setDerivacoes(ArrayList<NaoTerminal> derivacoes) {
        this.derivacoes = derivacoes;
    }

    public boolean isVazio() {
        return vazio;
    }

    public void setVazio(boolean vazio) {
        this.vazio = vazio;
    }
    
    
    
    
}
