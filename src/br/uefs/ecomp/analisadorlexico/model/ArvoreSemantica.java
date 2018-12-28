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

    public ArvoreSemantica() {
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

    public String analisador() {

        //verificar operações de constantes
        verificarOperacoesConst();

        //verificar os nomes das classes, se não existe nenhum duplicado.
        verificarNomesClasses();

        //verificar se extends existe
        verificarExtendsClasses();

        //verificar variaveis com nomes de classes
        verificarNomeVariaveisClasses();

        //verificar tipagem de variaveis de classes
        verificarTipagemVariaveisClasses();

        //verificar tipagem de variaveis de metodos
        verificarTipagemVariaveisMetodos();
        
        //verificar nome de variaveis de metodos
        verificarNomeVariaveisMetodos();

        if (erros.equals("")) {
            erros = "SUCESSO!!!\n";
        }

        erros = erros + "\nQtd Classes: " + classes.size() + "\n";

        return erros;

    }

    public String escArvoreClasse() {
        String result = "";
        Iterator<Classes> itera = classes.iterator();
        Classes c;

        result = result + "Classes\n";
        while (itera.hasNext()) {
            c = itera.next();
            String extend;
            if (c.getExtend() != null && !c.getExtend().equals("null")) {
                extend = "extend" + c.getExtend();
            } else {
                extend = "";
            };
            result = result + c.getNome() + " " + extend + "\n";
        }
        return result;
    }

    public String escArvoreConst() {
        String result = "";
        Iterator<Variaveis> itera = consts.getIteradorVars();
        Variaveis var;
        Token t;
        result = result + "Constantes\n";
        while (itera.hasNext()) {
            var = itera.next();
            t = var.getToken();
            result = result + var.getTipo() + " " + t.getLexema() + "\n";
        }

        result = result + "Operacoes das constantes\n";
        Iterator<Operacao> iteraOp = consts.getIteradorOp();
        Operacao op;
        while (iteraOp.hasNext()) {
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
        if (consts == null) {
            consts = new Const();
        }
    }

    public void addConst(Token t) {
        consts.addVariavel(t, auxToken.getLexema());
    }

    public int addAcessoMetodoConst(AcessoMetodo e) {
        consts.addAcesso(e);
        return (consts.getAcessos().size() - 1);
    }

    public void setAuxToken(Token auxToken) {
        this.auxToken = auxToken;
    }

    void inserirOpConst(Operacao op) {
        consts.addOperacao(op);
    }

    public Classes addClasse(String nome) {
        auxClass = new Classes(nome);
        classes.add(auxClass);
        return auxClass;
    }

    void addExtendsClasse(String l) {
        auxClass.setExtend(l);
    }

    public Variaveis addVariaveis(Token a, String b) {
        auxVariaveis = new Variaveis(a, b);
        auxClass.addVariaveis(auxVariaveis);
        return auxVariaveis;
    }

    public Metodos addMetodos(String tipo) {
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

    private void verificarNomesClasses() {
        Iterator<Classes> iteraClasses = classes.iterator();
        Classes classAuxAtual = null;
        Classes classAuxAnterior = null;
        int contClass = 0;
        int i;
        for (i = 0; i < classes.size(); i++) {// PRESTENÇÃO pode ser que o index tenha que começar do 1
            classAuxAnterior = classes.get(i);
            iteraClasses = classes.iterator();
            while (iteraClasses.hasNext()) {
                classAuxAtual = iteraClasses.next();
                if (classAuxAtual.getNome().equals(classAuxAnterior.getNome())) {
                    contClass++;
                    if (contClass > 1) {
                        erros = erros + "ERRO: " + " Linha: " + classAuxAtual.getLinha() + " |e Linha: " + classAuxAnterior.getLinha() + " | tipo: Classes com nomes repetidos: " + classAuxAnterior.getNome() + "\n";
                    }
                }
            }
            contClass = 0;
        }
    }

    private void verificarExtendsClasses() {
        Iterator<Classes> iteraClasses;
        Classes classAuxAtual = null;
        Classes classAuxAnterior = null;
        int contClass = 0;
        int i;
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
    }

    private void verificarNomeVariaveisClasses() {
        Variaveis auxVariavel;
        Variaveis auxVariavelConst;
        Classes classAuxAtual = null;
        int i;
        iteraConstantes = consts.getIteradorVars();
        Iterator<Variaveis> iteraVarivaisClasse;
        for (i = 0; i < classes.size(); i++) {
            classAuxAtual = classes.get(i);
            iteraVarivaisClasse = classAuxAtual.getVariebles().iterator();
            while (iteraVarivaisClasse.hasNext()) {
                auxVariavel = iteraVarivaisClasse.next();
                iteraConstantes = consts.getIteradorVars();
                while (iteraConstantes.hasNext()) {
                    auxVariavelConst = iteraConstantes.next();
                    if (auxVariavelConst.getToken().getLexema().equals(auxVariavel.getToken().getLexema())) {
                        erros = erros + "ERRO: " + " Linha: " + auxVariavel.getToken().getLinha() + " | tipo: Variavel já foi declarada em constantes: " + auxVariavel.getToken().getLexema() + "\n";
                    }
                }
            }
        }
    }

    private void verificarTipagemVariaveisClasses() {
        Variaveis auxVariavel;
        Iterator<Classes> iteraClasses;
        Classes classAuxAtual = null;
        Classes classAuxAnterior = null;
        int contClass = 0;
        Iterator<Variaveis> iteraVarivaisClasse;
        int i;
        for (i = 0; i < classes.size(); i++) {
            classAuxAtual = classes.get(i);
            iteraVarivaisClasse = classAuxAtual.getVariebles().iterator();
            while (iteraVarivaisClasse.hasNext()) {
                auxVariavel = iteraVarivaisClasse.next();
                if (!auxVariavel.getTipo().equals("int") || !auxVariavel.getTipo().equals("bool") || !auxVariavel.getTipo().equals("float") || !auxVariavel.getTipo().equals("string")) {
                    iteraClasses = classes.iterator();
                    while (iteraClasses.hasNext()) {
                        classAuxAnterior = iteraClasses.next();
                        if (classAuxAnterior.getNome().equals(auxVariavel.getTipo())) {
                            contClass++;
                        }
                    }
                } else {
                    contClass = 1;
                }
                if (contClass < 1) {
                    erros = erros + "ERRO: " + " Linha: " + auxVariavel.getToken().getLinha() + " | tipo: Variavel de tipo não declarado: " + auxVariavel.getTipo() + "\n";
                }
                contClass = 0;
            }
        }
    }

    private void verificarOperacoesConst() {
        String tipo;
        Iterator<Operacao> operacoes = consts.getIteradorOp();
        Operacao op;
        Iterator<Variaveis> iteraConsts = consts.getIteradorVars();
        Variaveis auxVar;
        Iterator<String> iteraString;
        String receb;
        String tipoOP;
        boolean b = false;
        while (operacoes.hasNext()) {
            op = operacoes.next();
            tipo = "";
            iteraConsts = consts.getIteradorVars();
            while (iteraConsts.hasNext()) {
                auxVar = iteraConsts.next();
                if (op.getVar().equals(auxVar.getToken().getLexema())) {
                    tipo = auxVar.getTipo();
                    break;
                }
            }
            if (tipo.equals("")) {
                erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Variavel não declarado: " + op.getVar() + "\n";
            } else {
                iteraString = op.getRecebe().iterator();
                while (iteraString.hasNext()) {
                    receb = iteraString.next();
                    b = false;
                    if (verificaInteiro(receb)) {
                        if (tipo.equals("int")) {
                            b = true;
                        }
                    } else if (verificaBool(receb)) {
                        if (tipo.equals("bool")) {
                            b = true;
                        }
                    } else if (verificaFloat(receb)) {
                        if (tipo.equals("float")) {
                            b = true;
                        }
                    } else if (verificaString(receb)) {
                        if (tipo.equals("string")) {
                            b = true;
                        }
                    } else {
                        iteraConsts = consts.getIteradorVars();
                        while (iteraConsts.hasNext()) {
                            auxVar = iteraConsts.next();
                            if (auxVar.getToken().getLexema().equals(receb)) {
                                if (auxVar.getTipo().equals(tipo)) {
                                    b = true;
                                }
                                break;
                            }
                        }
                    }

                    if (!b) {
                        erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operação com tipagem diferente: " + op.getVar() + "\n";
                    }

                }
            }
        }
    }

    public boolean verificaInteiro(String s) {
        try {
            int i = Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean verificaBool(String s) {
        if (s.equals("false") || s.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean verificaFloat(String s) {
        try {
            float f = Float.parseFloat(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean verificaString(String s) {
        if (s.charAt(0) == '"') {
            return true;
        } else {
            return false;
        }
    }

    private void verificarTipagemVariaveisMetodos() {
        Variaveis auxVariavel;
        Iterator<Classes> iteraClasses;
        Classes classAuxAtual = null;
        Classes classAuxAnterior = null;
        Iterator<Metodos> iteraMetodos;
        int contClass = 0;
        Iterator<Variaveis> iteraVarivaisMetodo;
        int i;
        Metodos auxMetodo;
        for (i = 0; i < classes.size(); i++) {
            classAuxAtual = classes.get(i);
            iteraMetodos = classAuxAtual.getMetodos().iterator();

            while (iteraMetodos.hasNext()) {
                auxMetodo = iteraMetodos.next();
                iteraVarivaisMetodo = auxMetodo.getIteratorVariaveis();
                while (iteraVarivaisMetodo.hasNext()) {
                    auxVariavel = iteraVarivaisMetodo.next();
                    if (!auxVariavel.getTipo().equals("int") || !auxVariavel.getTipo().equals("bool") || !auxVariavel.getTipo().equals("float") || !auxVariavel.getTipo().equals("string")) {
                        iteraClasses = classes.iterator();
                        while (iteraClasses.hasNext()) {
                            classAuxAnterior = iteraClasses.next();
                            if (classAuxAnterior.getNome().equals(auxVariavel.getTipo())) {
                                contClass++;
                            }
                        }
                    } else {
                        contClass = 1;
                    }
                    if (contClass < 1) {
                        erros = erros + "ERRO: " + " Linha: " + auxVariavel.getToken().getLinha() + " | tipo: Variavel de tipo não declarado: " + auxVariavel.getTipo() + "\n";
                    }
                    contClass = 0;
                }
            }

        }
    }

    private void verificarNomeVariaveisMetodos() {
        Iterator<Classes> iteraClasse;
        Iterator<Metodos> iteraMetodos;
        Iterator<Variaveis> iteraVarsClass;
        Iterator<Variaveis> iteraVarsMetodos;
        iteraConstantes = consts.getIteradorVars();
        int i;
        Classes classAuxAtual;
        Metodos auxaMetodo;
        Variaveis auxVarM;
        Variaveis auxVar;
        for (i = 0; i < classes.size(); i++) {
            classAuxAtual = classes.get(i);
            
            iteraMetodos = classAuxAtual.getMetodos().iterator();
            while(iteraMetodos.hasNext()){
                auxaMetodo = iteraMetodos.next();
                iteraVarsMetodos = auxaMetodo.getIteratorVariaveis();
                while(iteraVarsMetodos.hasNext()){
                    auxVarM = iteraVarsMetodos.next();
                    iteraConstantes = consts.getIteradorVars();
                    while(iteraConstantes.hasNext()){
                        auxVar = iteraConstantes.next();
                        if(auxVar.getToken().getLexema().equals(auxVarM.getToken().getLexema())){
                            erros = erros + "ERRO: " + " Linha: " + auxVarM.getToken().getLinha() + " | tipo: Variavel já foi declarada em constantes: " + auxVarM.getToken().getLexema() + "\n";
                        }
                    }
                    iteraVarsClass = classAuxAtual.getVariebles().iterator();
                    while(iteraVarsClass.hasNext()){
                        auxVar = iteraVarsClass.next();
                        if(auxVar.getToken().getLexema().equals(auxVarM.getToken().getLexema())){
                            erros = erros + "ERRO: " + " Linha: " + auxVarM.getToken().getLinha() + " | tipo: Variavel já foi declarada na classe: " + auxVarM.getToken().getLexema() + "\n";
                        }
                    }
                    
                }
            }
        }
    }

}
