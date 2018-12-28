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
    private Variaveis auxVariaveis;
    
    private Iterator<Variaveis> iteraConstantes;
    private String erros;

    private Const consts;
    private ArrayList<Classes> classes;
//    private ArrayList<Metodos> metodos;
//    private ArrayList<Variaveis> variaveis;
    
    public ArvoreSemantica(){
        consts = new Const();
        classes = new ArrayList<>();
        erros = "";
//        metodos = new ArrayList<>();
//        variaveis = new ArrayList<>();
    }

    public String analisa() {
        String result = "";
        
        result = escArvoreConst();
        //result = escArvoreClasse();
        
        return result;
    }
    
    public String analisador(){
        
        //verificar operações de constantes
        Variaveis auxVariavel;
        Variaveis auxVariavelConst;
        /*
        Iterator<Operacao> iteraOperacoes = consts.getIteradorOp();
        iteraConstantes = consts.getIteradorVars();
        Variaveis auxVariavel;
        Variaveis auxVariavelConst;
        Operacao op;
        String tipagem;
        while(iteraOperacoes.hasNext()){
            op = iteraOperacoes.next();
            tipagem = op.getRecebe();
            String v1[] = tipagem.split("+");
        }
        
        */
  
        //verificar os nomes das classes, se não existe nenhum duplicado.
        Iterator<Classes> iteraClasses = classes.iterator();
        Classes classAuxAtual = null;
        Classes classAuxAnterior = null;
        int contClass = 0;
        int i;
        for(i=0;i<classes.size();i++){// PRESTENÇÃO pode ser que o index tenha que começar do 1
            classAuxAnterior = classes.get(i);
            iteraClasses = classes.iterator();
            while(iteraClasses.hasNext()){
                classAuxAtual = iteraClasses.next();
                if(classAuxAtual.getNome().equals(classAuxAnterior.getNome())){
                    contClass++;
                    if(contClass>1){
                        erros = erros + "ERRO: " + " Linha: " + classAuxAtual.getLinha() + " |e Linha: " + classAuxAnterior.getLinha() + " | tipo: Classes com nomes repetidos: " + classAuxAnterior.getNome() + "\n";
                    }
                }
            }
            contClass = 0;
        }
        
        //verificar se extends existe
        classAuxAtual = null;
        classAuxAnterior = null;
        contClass = 0;
        for (i = 0; i < classes.size(); i++) {
            classAuxAnterior = classes.get(i);
            if (classAuxAnterior.getExtend() != null && classAuxAnterior.getExtend() != "") {
                if (classAuxAnterior.getExtend().equals(classAuxAnterior.getNome())) {
                    erros = erros + "ERRO: " + " Linha: " + classAuxAnterior.getLinha() + " | tipo: Classe extende da propria classe: " + classAuxAnterior.getNome() + "\n";
                } else {
                    iteraClasses = classes.iterator();
                    while (iteraClasses.hasNext()) {
                        classAuxAtual = iteraClasses.next();
                        if (classAuxAtual.getNome().equals(classAuxAnterior.getExtend())) {
                            contClass++;
                        }
                    }
                    if (contClass < 1) {
                        erros = erros + "ERRO: " + " Linha: " + classAuxAnterior.getLinha() + " | tipo: Classe extende de clase inexistente: " + classAuxAnterior.getNome() + "\n";
                    }
                }
            }
            contClass = 0;
        }
        
        //verificar variaveis com nomes de constantes
        classAuxAnterior = null;
        classAuxAtual = null;
        iteraConstantes = consts.getIteradorVars();
        Iterator<Variaveis> iteraVarivaisClasse;
        for (i = 0; i < classes.size(); i++) {
            classAuxAtual = classes.get(i);
            iteraVarivaisClasse = classAuxAtual.getVariebles().iterator();
            while(iteraVarivaisClasse.hasNext()){
                auxVariavel = iteraVarivaisClasse.next();
                iteraConstantes = consts.getIteradorVars();
                while(iteraConstantes.hasNext()){
                    auxVariavelConst = iteraConstantes.next();
                    if(auxVariavelConst.getToken().getLexema().equals(auxVariavel.getToken().getLexema())){
                        erros = erros + "ERRO: " + " Linha: " + auxVariavel.getToken().getLinha()+ " | tipo: Variavel já foi declarada em constantes: " + auxVariavel.getToken().getLexema() + "\n";
                    }
                }
            }
        }
        
        //verificar tipagem de variaveis
        classAuxAnterior = null;
        classAuxAtual = null;
        auxVariavel = null;
        iteraVarivaisClasse = null;
        contClass = 0;
        for (i = 0; i < classes.size(); i++) {
            classAuxAtual = classes.get(i);
            iteraVarivaisClasse = classAuxAtual.getVariebles().iterator();
            while(iteraVarivaisClasse.hasNext()){
                auxVariavel = iteraVarivaisClasse.next();
                if (!auxVariavel.getTipo().equals("int") || !auxVariavel.getTipo().equals("bool") || !auxVariavel.getTipo().equals("float") || !auxVariavel.getTipo().equals("string")) {
                    iteraClasses = classes.iterator();
                    while (iteraClasses.hasNext()) {
                        classAuxAnterior = iteraClasses.next();
                        if(classAuxAnterior.getNome().equals(auxVariavel.getTipo())){
                            contClass++;
                        }
                    }
                }
                if(contClass < 1){
                    erros = erros + "ERRO: " + " Linha: " + auxVariavel.getToken().getLinha()+ " | tipo: Variavel de tipo não declarado: " + auxVariavel.getTipo() + "\n";
                }
                contClass = 0;
            }
        }
        
        
        
        
        return erros;
        
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
    
    public int addAcessoMetodoConst(AcessoMetodo e){
        consts.addAcesso(e);
        return (consts.getAcessos().size() - 1);
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
