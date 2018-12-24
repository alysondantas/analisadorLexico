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
class Const {
    private ArrayList<Variaveis> variaveis;
    private ArrayList<Operacao> operacoes;
    
    public Const(){
        variaveis = new ArrayList();
        operacoes = new ArrayList();
    }
    
    
    public void addVariavel(Token t, String tipo){
        Variaveis var = new Variaveis(t,tipo);
        variaveis.add(var);
    }

    void addOperacao(Operacao op) {
        operacoes.add(op);
    }
    
    public Iterator<Variaveis> getIteradorVars(){
        return variaveis.iterator();
    }
    
    public Iterator<Operacao> getIteradorOp(){
        return operacoes.iterator();
    }
    
}
