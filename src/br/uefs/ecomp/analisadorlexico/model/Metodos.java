/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.ArrayList;

/**
 *
 * @author alyso
 */
class Metodos {
    
    private String nomeClasse;
    private String tipo;
    private String nome;
    private String parametros;
    private ArrayList<Variaveis> variaveis;
    private ArrayList<Operacao> operacoes;
    private ArrayList<Condicionais> condicionais;

    public Metodos(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }
    
    public void addVariaveis(Variaveis x){
        x.setNomeMetodo(this.nome);
        this.variaveis.add(x);
    }
    
    public void addOperacoes(Operacao x){
        x.setMetodo(this.nome);
        this.operacoes.add(x);
    }
}
