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
class Classes {
    private String nome;
    private String linha;
    private String extend;
    private ArrayList<Variaveis> variebles;
    private ArrayList<Operacao> operacoes;
    private ArrayList<Metodos> metodos;
    
    public Classes(String nome){
        this.nome = nome;
        this.variebles = new ArrayList<>();
        this.metodos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public ArrayList<Variaveis> getVariebles() {
        return variebles;
    }


    public void setVariebles(ArrayList<Variaveis> variebles) {
        this.variebles = variebles;
    }

    public ArrayList<Metodos> getMetodos() {
        return metodos;
    }

    public void setMetodos(ArrayList<Metodos> metodos) {
        this.metodos = metodos;
    }

    void addVariaveis(Variaveis auxVariaveis) {
        auxVariaveis.setNomeClasse(this.nome);
        variebles.add(auxVariaveis);
    }
    
    Metodos addMetodos(Metodos auxMetodos){
        auxMetodos.setNomeClasse(this.nome);
        metodos.add(auxMetodos);
        return auxMetodos;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }
    
    void addOperacao(Operacao op) {
        operacoes.add(op);
    }
    
    public Iterator<Operacao> getIteradorOp(){
        return operacoes.iterator();
    }
    
    
    
}
