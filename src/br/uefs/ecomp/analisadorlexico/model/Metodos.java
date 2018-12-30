/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author alyso
 */
class Metodos {
    
    private String nomeClasse;
    private String tipo;
    private String nome;
    private String linha;
    private ArrayList<String> parametros;// transforma em arraylist de tipos dos parametros
    private ArrayList<String> retorno;
    private ArrayList<Variaveis> variaveis;
    private ArrayList<Operacao> operacoes;
    private ArrayList<Condicionais> condicionais;

    public Metodos(String tipo) {
        this.tipo = tipo;
        variaveis = new ArrayList<>();
        retorno = new ArrayList<>();
        operacoes = new ArrayList<>();
        parametros = new ArrayList<>();
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
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
        this.operacoes.add(x);
    }

    public ArrayList getRetorno() {
        return retorno;
    }

    public void addRetorno(String retorno) {
        this.retorno.add(retorno);
    }
    
    public Iterator<Variaveis> getIteratorVariaveis(){
        return variaveis.iterator();
    }
    
    public Iterator<String> getIteratorParametro(){
        return parametros.iterator();
    }
    
    public ArrayList<String> getParametros(){
        return parametros;
    }
    
    public void setParametro(ArrayList<String> parametro){
        parametros = parametro;
    }
    
    public void addParametro(String param){
        parametros.add(param);
    }
    
}
