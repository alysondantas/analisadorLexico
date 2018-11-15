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
            tokenAtual = tokens.get(posicao);
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
        match("const");
        System.out.println(tokenAtual.getLexema());
        match("{");
        System.out.println(tokenAtual.getLexema());
        System.out.println("dclaração constante");
        declaracaoConstante();
        while (tokenAtual.getNome().equals("PalavraReservada")) {
            declaracaoConstante();
        }
        match("}");
        System.out.println("foi");
    }

    private void declaracaoConstante() {
        System.out.println(tokenAtual.getLexema());
        match("PalavraReservada");
        System.out.println(tokenAtual.getLexema() + "b");
        inicializacaoConstante();
        System.out.println(tokenAtual.getLexema() + "c");
        while (tokenAtual.getLexema().equals(",")) {
            passaToken();
            System.out.println(tokenAtual.getLexema() + "d");
            inicializacaoConstante();
        }
        match(";");
    }

    private void inicializacaoConstante() {
        if (match("Identificador") || match("Letra")) {
            System.out.println(tokenAtual.getLexema() + "e");
            match("=");
            System.out.println(tokenAtual.getLexema());
            match("Numero"); //colocar mais coisas
        }
    }

    private void classe() {
        match("class");
        match("Identificador");
        if (tokenAtual.getLexema().equals("extends")) {
            match("Identificador");
        }
        match("{");
        codigoClasse();
        match("}");
        
    }

    private void codigoClasse() {
        variaveis();
    }

    private void variaveis() {
        
    }

}
