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
    private int qtdMain;

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
        qtdMain = 0;
//        metodos = new ArrayList<>();
//        variaveis = new ArrayList<>();
    }

    public String analisa() {
        String result = "";

        result = escArvoreConst();
        //result = escArvoreClasse();

        return result;
    }
    
    public String getVarMetodoClasse(String nomeClasse, String nomeVar){
        String l ="";
        ArrayList k = new ArrayList();
        for (Classes next : classes) {
            if (next.getNome().equals(nomeClasse)) {
                k = next.getMetodos();
            }
        }

        for (Iterator iterator = k.iterator(); iterator.hasNext();) {
            Metodos next = (Metodos)iterator.next();
            for (Iterator iterator1 = next.getIteratorVariaveis(); iterator1.hasNext();) {
                Variaveis next1 = (Variaveis) iterator1.next();
                if (next1.getToken().getLexema().equals(nomeVar)) {
                    l = next1.getToken().getIdTipo()+"";
                }
            }
            
        }
        return l;
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

        //verificar sobrescrita de metodos
        verificarSobrescritaMetodosClasse();

        //verificar retornos
        verificarRetornoMetodos();

        //verificar operações de Variaveis em classes
        verificarOperacoesVariaveisClasses();

        //verificar operações em metodos
        verificarOperacoesMetodos();
        
        if(qtdMain>2){
            erros = erros + "\nEERO! - Nao deve existir mais que uma main! \n";
        }
        
        if (erros.equals("")) {
            erros = "SUCESSO!!! - Nao ha erros\n";
        }

        erros = erros + "\nQtd Classes: " + classes.size() + "\nQtd Metodo Main: " + qtdMain + "\n";

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
            }
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

    public Const startConst() {
        if (consts == null) {
            consts = new Const();
            return consts;
        }
        return consts;
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
                            if(classAuxAtual.getNome().equals(classAuxAnterior.getNome())){
                                erros = erros + "ERRO: " + " Linha: " + classAuxAnterior.getLinha() + " | tipo: Classe extend ela mesma clase: " + classAuxAnterior.getNome() + "\n";
                            }
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
                if (!auxVariavel.getTipo().equals("int") && !auxVariavel.getTipo().equals("bool") && !auxVariavel.getTipo().equals("float") && !auxVariavel.getTipo().equals("string")) {
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
                    if (verificaInteiro(receb) && !receb.contains(".")) {
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
    
    private boolean verificaVoid(String s) {
        if(s.equals("void") || s.equals("")){
            return true;
        }
        return false;
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
                    if (!auxVariavel.getTipo().equals("int") && !auxVariavel.getTipo().equals("bool") && !auxVariavel.getTipo().equals("float") && !auxVariavel.getTipo().equals("string")) {
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
            while (iteraMetodos.hasNext()) {
                auxaMetodo = iteraMetodos.next();
                iteraVarsMetodos = auxaMetodo.getIteratorVariaveis();
                while (iteraVarsMetodos.hasNext()) {
                    auxVarM = iteraVarsMetodos.next();
                    iteraConstantes = consts.getIteradorVars();
                    while (iteraConstantes.hasNext()) {
                        auxVar = iteraConstantes.next();
                        if (auxVar.getToken().getLexema().equals(auxVarM.getToken().getLexema())) {
                            erros = erros + "ERRO: " + " Linha: " + auxVarM.getToken().getLinha() + " | tipo: Variavel já foi declarada em constantes: " + auxVarM.getToken().getLexema() + "\n";
                        }
                    }
                    iteraVarsClass = classAuxAtual.getVariebles().iterator();
                    while (iteraVarsClass.hasNext()) {
                        auxVar = iteraVarsClass.next();
                        if (auxVar.getToken().getLexema().equals(auxVarM.getToken().getLexema())) {
                            erros = erros + "ERRO: " + " Linha: " + auxVarM.getToken().getLinha() + " | tipo: Variavel já foi declarada na classe: " + auxVarM.getToken().getLexema() + "\n";
                        }
                    }

                }
            }
        }
    }

    private void verificarSobrescritaMetodosClasse() {
        Iterator<Classes> iteraClasses = classes.iterator();
        Classes auxClass;
        Iterator<Metodos> iteraMetodos;
        Iterator<Metodos> iteraMAux;
        Metodos metodo;
        Metodos auxMetodo;
        int contM = 0;
        while (iteraClasses.hasNext()) {
            auxClass = iteraClasses.next();
            iteraMetodos = auxClass.getMetodos().iterator();
            while (iteraMetodos.hasNext()) {
                metodo = iteraMetodos.next();
                iteraMAux = auxClass.getMetodos().iterator();
                while (iteraMAux.hasNext()) {
                    auxMetodo = iteraMAux.next();
                    if (auxMetodo.getNome().equals(metodo.getNome()) && auxMetodo.getParametros().equals(metodo.getParametros())) {
                        contM++;
                        if (contM > 1) {
                            erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " |e Linha: " + auxMetodo.getLinha() + " | tipo: Metodos com sobrescrita: " + metodo.getNome() + "\n";
                        }
                    }
                }
                contM = 0;

                if (auxClass.getExtend() != null && !auxClass.getExtend().equals("")) {
                    Iterator<Classes> iteraClasseExtend = classes.iterator();
                    Classes auxClassExtend = null;
                    boolean v = false;
                    while (iteraClasseExtend.hasNext()) {
                        auxClassExtend = iteraClasseExtend.next();
                        if (auxClassExtend.getNome().equals(auxClass.getExtend())) {
                            v = true;
                            break;
                        }
                    }
                    if (auxClassExtend != null && v) {
                        iteraMAux = auxClassExtend.getMetodos().iterator();
                        while (iteraMAux.hasNext()) {
                            auxMetodo = iteraMAux.next();
                            if (auxMetodo.getNome().equals(metodo.getNome()) && auxMetodo.getParametros().equals(metodo.getParametros())) {
                                erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " |e Linha: " + auxMetodo.getLinha() + " | tipo: Metodos com sobrescrita em herança: " + metodo.getNome() + "\n";
                            }
                        }
                    }
                }
            }
        }
    }

    private void verificarRetornoMetodos() {
        Iterator<Classes> iteraClasses = classes.iterator();
        Classes auxClasse;
        Iterator<Metodos> iteraMetodo;
        Metodos metodo;
        Iterator<String> iteraReturn;
        String retorn;
        String tipoReturn = "";
        Iterator<Variaveis> iteraVars;
        boolean b = false;
        Variaveis varAux = null;
        while (iteraClasses.hasNext()) {
            auxClasse = iteraClasses.next();
            iteraMetodo = auxClasse.getMetodos().iterator();
            while (iteraMetodo.hasNext()) {
                metodo = iteraMetodo.next();
                iteraReturn = metodo.getRetorno().iterator();
                while (iteraReturn.hasNext()) {
                    retorn = iteraReturn.next();
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println(retorn);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
                    tipoReturn = "";
                    if (verificaInteiro(retorn) && !retorn.contains(".")) {
                        if (!metodo.getTipo().equals("int")) {
                            erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                        }
                    } else if (verificaFloat(retorn)) {
                        if (!metodo.getTipo().equals("float")) {
                            erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                        }
                    } else if (verificaBool(retorn)) {
                        if (!metodo.getTipo().equals("bool")) {
                            erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                        }
                    } else if (verificaString(retorn)) {
                        if (!metodo.getTipo().equals("string")) {
                            erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                        }
                    }else if (verificaVoid(retorn)) {
                        if (!metodo.getTipo().equals("void")) {
                            erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                        }
                    } else {
                        //a | a.clu | a.clu() | clu() a.clu
                        if (retorn.contains("(") && retorn.contains(".")) {
                            String[] t = retorn.split("(");
                            varAux = procuraVarivelClasseHeranca(auxClasse.getNome(), t[0]);
                            if (varAux != null) {
                                int h = t[1].length();
                                t[1] = t[1].substring(0, h - 1);
                                String[] varivaeis = t[1].split(",");
                                String[] nomeMetodo = t[0].split(".");
                                ArrayList<String> tiposVariveis = descobriTipagemParametrosMetodo(varAux.getTipo(), nomeMetodo[1], varivaeis);
                                Metodos m = procuraMetodo(varAux.getTipo(), t[0], tiposVariveis);
                                if (m == null || !m.getTipo().equals(metodo.getTipo())) {
                                    erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                                }
                                //usar this.auxClass;
                            } else {
                                erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno nao foi declarado: " + metodo.getNome() + "\n";
                            }
                        } else if (retorn.contains(".")) {
                            String[] t = retorn.split(".");// divide a.calc em a e calc
                            Iterator<Variaveis> iteraVaras = metodo.getIteratorVariaveis();
                            Variaveis auxVara = null;
                            boolean ba = false;
                            while (iteraVaras.hasNext()) {
                                auxVara = iteraVaras.next();
                                if (auxVara.getToken().getLexema().equals(t[0])) {
                                    ba = true;
                                    break;
                                }
                            }
                            if (!ba) {
                                auxVara = procuraVarivelClasseHeranca(auxClasse.getNome(), t[0]);
                            }
                            if (auxVara != null) {
                                Classes auxCl;
                                ba = false;
                                Iterator<Classes> iteraCl = classes.iterator();
                                while (iteraCl.hasNext()) {
                                    auxCl = iteraCl.next();
                                    if (auxCl.getNome().equals(auxVara.getTipo())) {
                                        iteraVaras = auxCl.getVariebles().iterator();
                                        while (iteraVaras.hasNext()) {
                                            auxVara = iteraVaras.next();
                                            if (auxVara.getToken().getLexema().equals(t[1])) {
                                                ba = true;
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (ba) {
                                    if (!auxVara.getTipo().equals(metodo.getTipo())) {
                                        erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                                    }
                                }else{
                                    erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno nao foi declarado: " + metodo.getNome() + "\n";
                                }
                            } else {
                                erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno nao foi declarado: " + metodo.getNome() + "\n";
                            }
                        } else if (retorn.contains("(")) {
                            String[] t = retorn.split("(");
                            int h = t[1].length();
                            t[1] = t[1].substring(0, h - 1);
                            String[] varivaeis = t[1].split(",");

                            ArrayList<String> tiposVariveis = descobriTipagemParametrosMetodo(auxClasse.getNome(), t[0], varivaeis);
                            Metodos m = procuraMetodo(auxClasse.getNome(), t[0], tiposVariveis);
                            if (m == null || !m.getTipo().equals(metodo.getTipo())) {
                                erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                            }
                        } else {
                            iteraVars = metodo.getIteratorVariaveis();
                            boolean v = false;
                            while (iteraVars.hasNext()) {
                                varAux = iteraVars.next();
                                if (varAux.getToken().getLexema().equals(retorn)) {
                                    v = true;
                                    break;
                                }
                            }
                            if (!v) {
                                varAux = procuraVarivelClasseHeranca(auxClasse.getNome(), retorn);
                                if (varAux != null) {
                                    if (!varAux.getTipo().equals(metodo.getTipo())) {
                                        erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                                    }
                                } else {
                                    erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno nao foi declarado: " + metodo.getNome() + "\n";
                                }
                            } else {
                                if (!varAux.getTipo().equals(metodo.getTipo())) {
                                    erros = erros + "ERRO: " + " Linha: " + metodo.getLinha() + " | tipo: Retorno de tipo diferente de metodo: " + metodo.getNome() + "\n";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Variaveis procuraVarivelClasseHeranca(String nomeClasse, String nomeVariavel) {
        Iterator<Classes> iteraClass = classes.iterator();
        Classes auxClasse = null;
        Variaveis varAux;
        while (iteraClass.hasNext()) {
            auxClasse = iteraClass.next();
            if (auxClasse.getNome().equals(nomeClasse)) {
                break;
            } else {
                auxClasse = null;
            }
        }
        if (auxClasse == null) {
            return null;
        }
        Iterator<Variaveis> iteraVars = auxClasse.getVariebles().iterator();
        while (iteraVars.hasNext()) {
            varAux = iteraVars.next();
            if (varAux.getToken().getLexema().equals(nomeVariavel)) {
                this.auxClass = auxClasse;
                return varAux;
            }
        }

        if (auxClasse.getExtend() != null && !auxClasse.getExtend().equals("")) {
            return procuraVarivelClasseHeranca(auxClasse.getExtend(), nomeVariavel);
        }

        return null;
    }

    private Metodos procuraMetodo(String nomeclasse, String metodo, ArrayList<String> parametros) {
        Iterator<Classes> iteraClasses;
        Iterator<Metodos> iteraMetodos;
        Classes auxClass;
        Metodos auxMetodo;
        boolean b = false;
        String parametrosM;
        String parametrosA;

        iteraClasses = classes.iterator();
        while (iteraClasses.hasNext()) {
            auxClass = iteraClasses.next();
            if (auxClass.getNome().equals(nomeclasse)) {
                iteraMetodos = auxClass.getMetodos().iterator();
                while (iteraMetodos.hasNext()) {
                    auxMetodo = iteraMetodos.next();
                    if (auxMetodo.getNome().equals(metodo)) {
                        b = true;
                        ArrayList<String> param = auxMetodo.getParametros();
                        Iterator<String> iteraParam = param.iterator();
                        Iterator<String> iteraParametros = parametros.iterator();
                        while (iteraParam.hasNext() && iteraParametros.hasNext()) {
                            parametrosM = iteraParam.next();
                            parametrosA = iteraParametros.next();
                            if (!parametrosM.equals(parametrosA)) {
                                b = false;
                            }
                        }
                        if (b) {
                            return auxMetodo;
                        }
                    }
                }
                if (auxClass.getExtend() != null && !auxClass.getExtend().equals("")) {
                    return procuraMetodo(auxClass.getExtend(), metodo, parametros);
                }
            }
        }
        return null;
    }

    private ArrayList descobriTipagemParametrosMetodo(String nomeClasse, String nomeMetodo, String[] variaveis) {
        Iterator<Classes> iteraClasses;
        Iterator<Metodos> iteraMetodos;
        Iterator<Variaveis> iteraVars;
        Variaveis auxVar = null;
        Classes auxClass;
        Metodos auxMetodo;
        String nomeVar = "";
        ArrayList tipos = new ArrayList<String>();
        int i;
        boolean b = false;
        for (i = 0; i < variaveis.length; i++) {
            nomeVar = variaveis[i];
            iteraClasses = classes.iterator();
            while (iteraClasses.hasNext()) {
                auxClass = iteraClasses.next();
                if (auxClass.getNome().equals(nomeClasse)) {
                    iteraMetodos = auxClass.getMetodos().iterator();
                    while (iteraMetodos.hasNext()) {
                        auxMetodo = iteraMetodos.next();
                        if (auxMetodo.getNome().equals(nomeMetodo)) {
                            iteraVars = auxMetodo.getIteratorVariaveis();
                            while (iteraVars.hasNext()) {
                                auxVar = iteraVars.next();
                                if (auxVar.getToken().getLexema().equals(nomeVar)) {
                                    b = true;
                                    tipos.add(auxVar.getTipo());
                                    break;
                                }
                            }
                            if (b) {
                                break;
                            }
                        }
                    }
                    if (b) {
                        b = false;
                        break;
                    } else {
                        iteraVars = auxClass.getVariebles().iterator();
                        while (iteraVars.hasNext()) {
                            auxVar = iteraVars.next();
                            if (auxVar.getToken().getLexema().equals(nomeVar)) {
                                b = true;
                                tipos.add(auxVar.getTipo());
                                break;
                            }
                        }
                        if (b) {
                            b = false;
                            break;
                        } else {
                            if (auxClass.getExtend() != null && !auxClass.getExtend().equals("")) {
                                auxVar = procuraVarivelClasseHeranca(auxClass.getExtend(), nomeVar);
                                if (auxVar != null) {
                                    tipos.add(auxVar.getTipo());
                                    break;
                                } else {
                                    tipos.add("null");
                                    break;
                                }
                            } else {
                                tipos.add("null");
                                break;
                            }
                        }
                    }
                }

            }
        }
        return tipos;
    }

    private void verificarOperacoesVariaveisClasses() {
        Iterator<Classes> iteraClass = classes.iterator();
        Classes auxClasse;
        Iterator<Operacao> iteraOp;
        String tipo;
        Iterator<Variaveis> iteraVarsConsts;
        Iterator<Variaveis> iteraVarsClass;
        Variaveis auxVar;
        Iterator<String> iteraString;
        String receb;
        Operacao op;
        boolean b;

        while (iteraClass.hasNext()) {
            auxClasse = iteraClass.next();
            iteraOp = auxClasse.getIteradorOp();
            while (iteraOp.hasNext()) {
                op = iteraOp.next();
                tipo = "";
                iteraVarsClass = auxClasse.getVariebles().iterator();
                while (iteraVarsClass.hasNext()) {
                    auxVar = iteraVarsClass.next();
                    if (op.getVar().equals(auxVar.getToken().getLexema())) {
                        tipo = auxVar.getTipo();
                        break;
                    }
                }
                if (tipo.equals("")) {
                    iteraVarsConsts = consts.getIteradorVars();
                    while (iteraVarsConsts.hasNext()) {
                        auxVar = iteraVarsConsts.next();
                        if (op.getVar().equals(auxVar.getToken().getLexema())) {
                            tipo = auxVar.getTipo();
                            break;
                        }
                    }
                }
                if (tipo.equals("")) {
                    erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Variavel não declarado: " + op.getVar() + "\n";
                } else {
                    iteraString = op.getRecebe().iterator();
                    while (iteraString.hasNext()) {
                        receb = iteraString.next();
                        b = false;
                        if (verificaInteiro(receb) && !receb.contains(".")) {
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
                            Iterator<Classes> iteraAuxClass = classes.iterator();
                            Classes classAuxx;
                            while (iteraAuxClass.hasNext()) {
                                classAuxx = iteraAuxClass.next();
                                if (classAuxx.getNome().equals(tipo)) {
                                    b = true;
                                    break;
                                }
                            }
                            if (!b) {
                                iteraVarsConsts = consts.getIteradorVars();
                                while (iteraVarsConsts.hasNext()) {
                                    auxVar = iteraVarsConsts.next();
                                    if (auxVar.getToken().getLexema().equals(receb)) {
                                        if (auxVar.getTipo().equals(tipo)) {
                                            b = true;
                                        }
                                        break;
                                    }
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
    }

    private void verificarOperacoesMetodos() {
        Iterator<Classes> iteraClasses = classes.iterator();
        Classes auxClasse;
        Iterator<Metodos> iteraMetodo;
        Metodos metodo;
        Iterator<Operacao> iteraOp;
        Operacao op;
        String tipoReturn = "";
        String retorn;
        Iterator<String> iteraRecebOp;
        Iterator<Variaveis> iteraVars;
        boolean b = false;
        Variaveis varAux = null;
        while (iteraClasses.hasNext()) {
            auxClasse = iteraClasses.next();
            iteraMetodo = auxClasse.getMetodos().iterator();
            while (iteraMetodo.hasNext()) {
                metodo = iteraMetodo.next();
                iteraOp = metodo.getIteratorOperacoes();
                while (iteraOp.hasNext()) {
                    op = iteraOp.next();
                    if( op.getVar()!=null || !op.getVar().equals("")){
                        op.addRecebe(op.getVar());
                    }
                    if (op.getTipo() == null || op.getTipo().equals("") || op.getTipo().equals("null")) {
                        Iterator<Variaveis> iteraVarsAux = metodo.getIteratorVariaveis();
                        Variaveis auxVars;
                        boolean v = false;
                        while (iteraVarsAux.hasNext()) {
                            auxVars = iteraVarsAux.next();
                            if (auxVars.getToken().getLexema().equals(op.getVar())) {
                                op.setTipo(auxVars.getTipo());
                                v = true;
                                break;
                            }
                        }
                        if (!v) {
                            auxVars = procuraVarivelClasseHeranca(auxClasse.getNome(), op.getVar());
                            if (auxVars != null) {
                                op.setTipo(auxVars.getTipo());
                                v = true;
                            }
                        }
                    }
                    iteraRecebOp = op.getRecebe().iterator();
                    while (iteraRecebOp.hasNext()) {
                        retorn = iteraRecebOp.next();
                        tipoReturn = "";
                        if (verificaInteiro(retorn) && !retorn.contains(".")) {
                            if (!op.getTipo().equals("int")) {
                                erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                            }
                        } else if (verificaFloat(retorn)) {
                            if (!op.getTipo().equals("float")) {
                                erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                            }
                        } else if (verificaBool(retorn)) {
                            if (!op.getTipo().equals("bool")) {
                                erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                            }
                        } else if (verificaString(retorn)) {
                            if (!op.getTipo().equals("string")) {
                                erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                            }
                        } else {
                            //a | a.clu | a.clu() | clu()
                            if (retorn.contains("(") && retorn.contains(".")) {
                                String[] t = retorn.split("(");
                                varAux = procuraVarivelClasseHeranca(auxClasse.getNome(), t[0]);
                                if (varAux != null) {
                                    int h = t[1].length();
                                    t[1] = t[1].substring(0, h - 1);
                                    String[] varivaeis = t[1].split(",");
                                    String[] nomeMetodo = t[0].split(".");
                                    ArrayList<String> tiposVariveis = descobriTipagemParametrosMetodo(varAux.getTipo(), nomeMetodo[1], varivaeis);
                                    Metodos m = procuraMetodo(varAux.getTipo(), t[0], tiposVariveis);
                                    if (m == null || !m.getTipo().equals(op.getTipo())) {
                                        erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                    }
                                    //usar this.auxClass;
                                } else {
                                    erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                }
                            } else if (retorn.contains(".")) {
                                String[] t = retorn.split(".");// divide a.calc em a e calc
                                Iterator<Variaveis> iteraVaras = metodo.getIteratorVariaveis();
                                Variaveis auxVara = null;
                                boolean ba = false;
                                while (iteraVaras.hasNext()) {
                                    auxVara = iteraVaras.next();
                                    if (auxVara.getToken().getLexema().equals(t[0])) {
                                        ba = true;
                                        break;
                                    }
                                }
                                if (!ba) {
                                    auxVara = procuraVarivelClasseHeranca(auxClasse.getNome(), t[0]);
                                }
                                if (auxVara != null) {
                                    Classes auxCl;
                                    ba = false;
                                    Iterator<Classes> iteraCl = classes.iterator();
                                    while (iteraCl.hasNext()) {
                                        auxCl = iteraCl.next();
                                        if (auxCl.getNome().equals(auxVara.getTipo())) {
                                            iteraVaras = auxCl.getVariebles().iterator();
                                            while (iteraVaras.hasNext()) {
                                                auxVara = iteraVaras.next();
                                                if (auxVara.getToken().getLexema().equals(t[1])) {
                                                    ba = true;
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (ba) {
                                        if (!auxVara.getTipo().equals(op.getTipo())) {
                                            erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                        }
                                    } else {
                                        erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Não encontrou o tipo da variavel em operação: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                    }
                                } else {
                                    erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Não encontrou o tipo da variavel em operação: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                }
                            } else if (retorn.contains("(")) {
                                String[] t = retorn.split("(");
                                int h = t[1].length();
                                t[1] = t[1].substring(0, h - 1);
                                String[] varivaeis = t[1].split(",");
                                ArrayList<String> tiposVariveis = descobriTipagemParametrosMetodo(auxClasse.getNome(), t[0], varivaeis);
                                Metodos m = procuraMetodo(auxClasse.getNome(), t[0], tiposVariveis);
                                if (m == null || !m.getTipo().equals(op.getTipo())) {
                                    erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                }
                            } else {
                                iteraVars = metodo.getIteratorVariaveis();
                                boolean v = false;
                                while (iteraVars.hasNext()) {
                                    varAux = iteraVars.next();
                                    if (varAux.getToken().getLexema().equals(retorn)) {
                                        v = true;
                                        break;
                                    }
                                }
                                if (!v) {
                                    varAux = procuraVarivelClasseHeranca(auxClasse.getNome(), retorn);
                                    if (varAux != null) {
                                        if (!varAux.getTipo().equals(op.getTipo())) {
                                            erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                        }
                                    } else {
                                        //não sei se aqui é um erro precisa verificar
                                        erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Não encontrou o tipo da variavel em operação: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                    }
                                } else {
                                    if (!varAux.getTipo().equals(op.getTipo())) {
                                        erros = erros + "ERRO: " + " Linha: " + op.getLinha() + " | tipo: Operaçao de tipo diferente: " + op.getVar() + " | " + op.getTipo() + " com " + retorn + "\n";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void setQtdMain(int qtd){
        this.qtdMain = qtd;
    }
}
