/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Aleatorio
 */
public class Gramatica {

    private ArrayList linhas;
    private ArrayList naoTerminais;
    private String grama = "a";

    private Token tokenAtual, tokenAnterior;
    private int posicao = -1;
    private ArrayList<Token> tokens;

    public Gramatica(ArrayList tokens) {
        naoTerminais = new ArrayList<NaoTerminal>();
        this.tokens = tokens;
    }

    public void lerLinha() {
        String linha[];
        String aux = "";
        char[] x = grama.toCharArray();
        ArrayList aaa = new ArrayList<String>();
        int i;
        //System.out.println("caralho" + x.length);
        for (i = 0; i < x.length; i++) {

            if (x.length > i + 1) {
                //System.out.println("aaaa");
                if (x[i] == '\n') {
                    //System.out.println(aux);
                    aaa.add(aux);
                    aux = "";
                } else {
                    aux = aux + x[i];
                }
            } else {
                //System.out.println("a " + x[i]);
                if (x[i] == '§') {
                    aaa.add(aux);
                } else {

                    aux = aux + x[i];
                }
            }
        }
        linhas = aaa;
        /*for (Iterator iterator = aaa.iterator(); iterator.hasNext();) {
            Object next = iterator.next();
            System.out.println(next);
        }*/
    }

    public void criaEstrutura() {
        Iterator<String> itera = linhas.iterator();
        String linha;
        while (itera.hasNext()) {
            linha = itera.next();
            String nome = "";
            String[] miney = linha.split("::=");
            nome = miney[0];
            String aux = "";
            for (int i = 0; i < nome.length(); i++) {
                if (nome.charAt(i) == '>') {
                    aux = aux + '>';
                    break;
                } else {
                    aux = aux + nome.charAt(i);
                }
            }
            nome = aux;
            //System.out.println(linhas.size());;
            System.out.println("Nome " + nome);
            String a[] = miney[1].split("|");
            ArrayList derivacoes = new ArrayList();
            boolean b = false;
            for (int i = 0; i < a.length; i++) {
                if (a[i].equals("")) {
                    derivacoes.add("@");
                    b = true;
                } else {
                    derivacoes.add(a[i]);
                }
            }

            NaoTerminal nt = new NaoTerminal(nome);
            nt.setDerivacoes(derivacoes);
            nt.setVazio(b);
            naoTerminais.add(nt);
        }
    }

    private boolean passaToken() {
        if (posicao + 1 < tokens.size()) {
            posicao++;
            tokenAnterior = tokenAtual;
//            System.out.println("Token Anterior: "+ tokenAnterior.getLexema());
            tokenAtual = tokens.get(posicao);
            System.out.println("Token Atual: " + tokenAtual.getLexema() + " Tipo Token: " + tokenAtual.getNome());
            return true;
        }
        return false;
    }

    private boolean match(String tipo) {
        if (tokenAtual.getNome().equals(tipo) || tokenAtual.getLexema().equals(tipo)) {
            passaToken();
            return true;
        }
        return false;
    }

    public void start() {
        System.out.println("startou");
        passaToken();
        constante();
        classe();
    }

    private void constante() {
        System.out.println("Chamou Constante");
        match("const");
        match("{");
        declaracaoConstante();
        while (tokenAtual.getNome().equals("PalavraReservada")) {
            declaracaoConstante();
        }
        match("}");
        System.out.println("Foi Constante");
    }

    private void declaracaoConstante() {
        System.out.println("Chamou Declaração Constante");
        match("PalavraReservada");
        inicializacaoConstante();
        while (tokenAtual.getLexema().equals(",")) {
            passaToken();
            inicializacaoConstante();
        }
        match(";");
        System.out.println("Foi Declaração Constante");
    }

    private void inicializacaoConstante() {
        System.out.println("Chamou Inicialização Constante");
        match("Identificador");
        match("=");
        if (match("Digito") || match("Numero")) {

        }
//        match("Digito"); //colocar mais coisas
        System.out.println("Foi Inicialização Constante");
    }

    private void classe() {
        System.out.println("Chamou Classe");
        match("class");
        match("Identificador");
        if (tokenAtual.getLexema().equals("extends")) {
            passaToken();
            match("Identificador");
        }
        match("{");
        codigoClasse();
        match("}");
        System.out.println("Foi Classe");
        if (tokenAtual.getLexema().equals("class")) {
            classe();
        }
    }

    private void codigoClasse() {
        if (tokenAtual.getLexema().equals("variables")) {
            variaveis();
            codigoClasse();
        }
        if (tokenAtual.getLexema().equals("method")) {
            metodo();
            codigoClasse();
        }
    }

    private void variaveis() {
        System.out.println("Chamou Variaveis");
        match("variables");
        match("{");
        declaracaoVariaveis();
        while (tokenAtual.getNome().equals("PalavraReservada")) {
            declaracaoVariaveis();
        }
        match("}");
        System.out.println("Foi Variaveis");
    }

    private void declaracaoVariaveis() {
        match("PalavraReservada");
        inicializacaoVariaveis();
        while (tokenAtual.getLexema().equals(",")) {
            passaToken();
            inicializacaoVariaveis();
        }
        match(";");
    }

    private void inicializacaoVariaveis() {
        match("Identificador");
        match("=");
        match("Numero"); //colocar mais coisas
    }

    private void metodo() {
        System.out.println("Chamou Metodo");
        match("method");
        if (match("PalavraReservada") || match("Identificador")) {
            match("Identificador");
            match("(");
            argumentosMetodos();
            match(")");
            match("{");
            codigoMetodo();
            match("}");
            if (tokenAtual.getLexema().equals("return")) {
                match("return");
                opcoesRetorno();
                match(";");
            }
        }
        System.out.println("Foi Metodo");
    }

    private void opcoesRetorno() {

    }

    private void argumentosMetodos() {
        System.out.println("Chamou Argumentos Metodos");
        if (match("Identificador") || match("PalavraReservada")) {
            if (tokenAtual.getNome().equals("Identificador")) {
                passaToken();
                if (tokenAtual.getLexema().equals("[")) {
                    passaToken();
                    if (tokenAtual.getNome().equals("Numero") || tokenAtual.getNome().equals("Digito") || tokenAtual.getNome().equals("Identificador")) {
                        passaToken();
                        match("]");
                    }
                    match("]");
                    if (tokenAtual.getLexema().equals("[")) {
                        match("[");
                        if (tokenAtual.getNome().equals("Numero") || tokenAtual.getNome().equals("Digito") || tokenAtual.getNome().equals("Identificador")) {
                            passaToken();
                            match("]");
                        }
                    }
                }
            }
            if (tokenAtual.getLexema().equals(",")) {
                argumentosMetodos();
            }
        }
        System.out.println("Foi Argumentos Metodos");
    }

    private void codigoMetodo() {
        if (tokenAtual.getLexema().equals("variables")) {
            variaveis();
        }
        if (tokenAtual.getLexema().equals("while")) {
            While();
        }
        if (tokenAtual.getLexema().equals("write")) {
            Write();
        }
    }

    private void While() {
        System.out.println("Chamou While");
        match("while");
        if (tokenAtual.getLexema().equals("(")) {
            passaToken();
            match("Identificador");
            match("OperadorRelacional");
            match("Digito");
            match("Numero");
            match(")");
            match("{");
            codigoWhile();
            match("}");
        }
        System.out.println("Foi While");
    }

    private void codigoWhile() {

    }

    public void Write() {
        System.out.println("Chamou Write");
        match("write");
        match("(");
        arguentosWrite();
        match(")");
        System.out.println("Foi Write");
    }

    private void arguentosWrite() {
        System.out.println("Chamou Argumentos Write");
        match("Identificador");
        if (tokenAtual.getLexema().equals("[")) {
            passaToken();
            if (tokenAtual.getNome().equals("Numero") || tokenAtual.getNome().equals("Digito") || tokenAtual.getNome().equals("Identificador")) {
                passaToken();
                match("]");
            }
            match("]");
            if (tokenAtual.getLexema().equals("[")) {
                match("[");
                if (tokenAtual.getNome().equals("Numero") || tokenAtual.getNome().equals("Digito") || tokenAtual.getNome().equals("Identificador")) {
                    passaToken();
                    match("]");
                }
            }
        }
    }

}
