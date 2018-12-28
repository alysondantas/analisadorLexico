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

    private String tipo;
    private String var;
    private String linha;
    private ArrayList<String> indiceMatriz;
    private ArrayList<String> recebe;
    private String acessoMetodoId;

    public Operacao() {
        recebe = new ArrayList();
        var = "";
        linha = "";
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public ArrayList getRecebe() {
        return recebe;
    }

    public void addRecebe(String recebe) {
        this.recebe.add(recebe);
    }
    
    public ArrayList getIndiceMatriz() {
        return recebe;
    }

    public void addIndiceMatriz(String indice) {
        this.indiceMatriz.add(indice);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAcessoMetodoId() {
        return acessoMetodoId;
    }

    public void setAcessoMetodoId(String acessoMetodoId) {
        this.acessoMetodoId = acessoMetodoId;
    }
    
}
