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
public class ArvoreSemantica {
    private Token auxToken;

    private Const consts;
    private ArrayList<Classes> classes;
    
    public ArvoreSemantica(){
        consts = new Const();
    }

    public String analisa() {
        String result = "";
        /*Iterator<Variaveis> itera = consts.getIteradorVars();
        Variaveis var;
        Token t;
        result = result + "Constantes\n";
        while(itera.hasNext()){
            var = itera.next();
            t = var.getToken();
            result = result + var.getTipo() + " " + t.getLexema()+ "\n";
        }
        
        result = result + "Operacoes das constantes\n";
        Iterator<Operacao> iteraOp = consts.getIteradorOp();
        Operacao op;
        while(iteraOp.hasNext()){
            op = iteraOp.next();
            result = result + op.getTipo1() + " " + op.getOp() + " " + op.getTipo2() + "\n";
        }*/
        
        
        return result;
    }

    void startConst() {
        if(consts == null){
            consts = new Const();
        }
    }
    
    public void addConst(Token t, String tipo){
        consts.addVariavel(t,tipo);
    }
    
    
    public void setAuxToken(Token auxToken) {
        this.auxToken = auxToken;
    }

    void inserirOpConst(Operacao op) {
        consts.addOperacao(op);
    }
    
}
