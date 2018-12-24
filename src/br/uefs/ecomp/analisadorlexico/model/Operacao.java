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
class Operacao {
    private ArrayList<String> tipos;
    private String op;
    private String linha;
    
    public Operacao(){
        tipos = new ArrayList<>();
        op = "";
        linha = "";
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }
    
    public void addTipo(String tipo){
        tipos.add(tipo);
    }
    
    public Iterator getIterador(){
        return tipos.iterator();
    }
    
}
