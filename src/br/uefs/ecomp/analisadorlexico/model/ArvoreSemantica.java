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
    private Metodos auxMetodo;

    private Const consts;
    private ArrayList<Classes> classes;
//    private ArrayList<Metodos> metodos;
//    private ArrayList<Variaveis> variaveis;
    private Variaveis auxVariaveis;
    
    public ArvoreSemantica(){
        consts = new Const();
        classes = new ArrayList<>();
//        metodos = new ArrayList<>();
//        variaveis = new ArrayList<>();
    }

    public String analisa() {
        String result = "";
        
        result = escArvoreConst();
        //result = escArvoreClasse();
        
        return result;
    }
    
    public void analisador(){
        
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
//            Iterator<String> iteraS = op.getIterador();
//            while (iteraS.hasNext()) {
//                String test = iteraS.next();
//                result = result + test + " ";
//            }
            result = result + "\n";
        }
        return result;
    }
    
    void startConst() {
        if(consts == null){
            consts = new Const();
        }
    }
    
    public void addConst(Token t){
        consts.addVariavel(t,auxToken.getLexema());
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
    
    public Variaveis addVariaveis(Token a, String b){
        auxVariaveis = new Variaveis(a, b);
        auxClass.addVariaveis(auxVariaveis);
        return auxVariaveis;
    }
    
    public Metodos addMetodos(String tipo){
        auxMetodo = new Metodos(tipo);
        auxClass.addMetodos(auxMetodo);
        return auxMetodo;
    }
    
//    public Metodos addMetodo(String tipo){
//        auxMetodo = new Metodos(tipo);
//        metodos.add(auxMetodo);
//        return auxMetodo;
//    }

    public Classes getAuxClass() {
        return auxClass;
    }

    public Metodos getAuxMetodo() {
        return auxMetodo;
    }
    
}
