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
class Classes {
    private String nome;
    private String extend;
    private ArrayList<Variebles> variebles;
    private ArrayList<Metodos> metodos;
    
    public Classes(String nome){
        this.nome = nome;
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

    public ArrayList<Variebles> getVariebles() {
        return variebles;
    }

    public void setVariebles(ArrayList<Variebles> variebles) {
        this.variebles = variebles;
    }

    public ArrayList<Metodos> getMetodos() {
        return metodos;
    }

    public void setMetodos(ArrayList<Metodos> metodos) {
        this.metodos = metodos;
    }
    
    
}
