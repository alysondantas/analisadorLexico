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
    private Classes auxClass;

    private Const consts;
    private ArrayList<Classes> classes;
    
    public ArvoreSemantica(){
        consts = new Const();
        classes = new ArrayList<>();
    }

    public String analisa() {
        String result = "";
        
        //result = escArvoreConst();
        result = escArvoreClasse();
        
        return result;
    }
    
    public String escArvoreClasse(){
        String result = "";
        Iterator<Classes> itera = classes.iterator();
        Classes c;
        
        result = result + "Classes\n";
        while(itera.hasNext()){
            c = itera.next();
            String extend; 
            if(c.getExtend() !=null && !c.getExtend().equals("null")){
                extend = "extend" + c.getExtend();
            }else{extend = "";};
            result = result + c.getNome() + " " + extend + "\n";
        }
        return result;
    }

    public String escArvoreConst(){
        String result = "";
        Iterator<Variaveis> itera = consts.getIteradorVars();
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
        }
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
    
    public Classes addClasse(String nome){
        auxClass = new Classes(nome);
        classes.add(auxClass);
        return auxClass;
    }

    void addExtendsClasse(String l) {
        auxClass.setExtend(l);
    }
    
}
