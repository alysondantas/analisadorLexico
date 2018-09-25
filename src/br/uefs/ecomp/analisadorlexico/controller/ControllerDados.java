/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 *
 * @author Alyson Dantas
 */
public class ControllerDados {

    private static ControllerDados unicaInstancia;
    private String conteudoArq = ""; //String para salvar o conteúdo do arquivo original antes da compactação
    private String caminhoArq = "";
    private int op = 0;
    private int contLinha = 0;
    private int auxI = 0;
    private ArrayList contedudoArqLista;
    private char[] caracteres;
    private String auxLinha = null;

    File diretorio;

    private ControllerDados() {
        contedudoArqLista = new ArrayList<String>();
    }

    /**
     * controla o instanciamento de objetos Controller
     *
     * @return unicaInstancia
     */
    public static synchronized ControllerDados getInstance() {
        if (unicaInstancia == null) {
            unicaInstancia = new ControllerDados();
        }
        return unicaInstancia;
    }

    /**
     * reseta o objeto Controller ja instanciado
     */
    public static void zerarSingleton() {
        unicaInstancia = null;
    }

    public String lerArquivo(String local) throws IOException { //método que lê um arquivo de texto e converte todo o seu conteúdo para uma String
        Reader arq = new InputStreamReader(new FileInputStream(local), "ISO-8859-1"); //inicializo arq como a leitura de arquivos no local especificado no padrão ISO-8859-1
        BufferedReader buffRead = new BufferedReader(arq); //crio um novo objeto BuffReader e passo para ele a leitura do local em arq
        String linha = buffRead.readLine(); //crio uma string auxiliar e já armazeno a primeira linha do arquivo
        String c = ""; //crio uma string auxiliar para armazenar o conteúdo da string

        while (linha != null) { //enquanto não chegar no fim do arquivo
            contedudoArqLista.add(linha);
            c = c + linha; //salvo o a letra da string na posição atual
            linha = buffRead.readLine(); //salvo a linha atual do arquivo na string
            if (linha != null) {
                //System.out.println("add linha: " + linha);
                contedudoArqLista.add(linha);
                c = c + "\n"; //acrescento uma quebra de linha em "c" a cada fim de linha em "linha"
            }
        }
        buffRead.close(); //fecho a leitura do arquivo

        conteudoArq = c;//Uma varaivel local recebe a string com todo o conteudo do arquivo
        return c; //retorna a String com todo o conteúdo do arquivo de texto
    }

    public void escreverArquivo(String texto, String local, String nome) throws IOException { //método deixado aqui só para inspiração. Não vai ser usado do jeito que está descrito no momento
        local = local + nome; //anexo o nome do arquivo ao local que ele será escrito
        BufferedWriter buffWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(local), "ISO-8859-1")); //crio um novo objeto para escrita de arquivos e passo como parâmetro um novo objeto de escrita do arquivo no local especificado no padrão ISO-8859-1
        buffWrite.append(texto); //anexo essa string no arquivo de texto
        buffWrite.close(); //fecho o arquivo aberto
    }

    public void listaArquivos() throws IOException {
        diretorio = new File(caminhoArq);
	File afile[] = diretorio.listFiles();
	int i = 0;
	for (int j = afile.length; i < j; i++) {
		File arquivos = afile[i];
		System.out.println("Acessando um novo arquivo " + arquivos.getName());
                lerArquivo(caminhoArq + arquivos.getName());
                analisadordelinhas();
	}
    }

    public void analisadordelinhas() {
        Iterator<String> itera = contedudoArqLista.iterator();
        auxI = 0;
        while (itera.hasNext()) {
            contLinha++;
            auxLinha = itera.next();
            if (auxLinha != null) {
                caracteres = auxLinha.toCharArray();
                for (auxI = 0; auxI < caracteres.length; auxI++) {
                    //System.out.println("caractre: " + auxI + " | linha: " + contLinha + " | conteudo: " + caracteres[auxI]);
                    if (caracteres[auxI] == '/') {
                        //System.out.println("pode ser comentario");
                        if (auxI + 1 < caracteres.length) {
                            if (caracteres[auxI + 1] == '/') {
                                System.out.println("Token comentario de linha");
                                break;
                            } else if (caracteres[auxI + 1] == '*') {
                                System.out.println("Token inicio comentario de bloco");
                                //System.out.println("testi: " + auxI);
                                itera = analisetokenComentario(itera);
                                //System.out.println("testf: " + auxI);
                            }
                        } else {
                            System.out.println("Token operador Aritmetico");
                        }
                    }else{
                        System.out.println("A: " + caracteres[auxI]);
                    }

                    //System.out.println("letra atual " + caracteres[auxI] + "\n");
                }
            }
        }
    }

    public void setConteudoArquivo(String arqmod) {
        conteudoArq = arqmod;
    }

    public String getConteudoArquivo() {
        return conteudoArq;
    }

    private Iterator<String> analisetokenComentario(Iterator<String> itera) {
        boolean b = false;
        if (auxI < caracteres.length) {
            System.out.println("ainda possui caracteres a serem lidos na linha");
            b = true;
            int i = auxI + 1;
            for (auxI = i; auxI < caracteres.length; auxI++) {
                if (auxI + 1 < caracteres.length) {
                    if (caracteres[auxI] == '*' && caracteres[auxI + 1] == '/') {
                        System.out.println("Token final comentario de bloco ML");
                        auxI++;
                        return itera;
                    }
                }
            }
        }
            
            while (itera.hasNext()) {
                auxLinha = itera.next();
                if(b){
                    auxLinha = itera.next();
                }
                contLinha++;
                caracteres = auxLinha.toCharArray();
                for (auxI = 0; auxI < caracteres.length; auxI++) {
                    //System.out.println("caractre: " + auxI + " | linha: " + contLinha + " | conteudo: " + caracteres[auxI]);
                    if (auxI + 1 < caracteres.length) {
                        if (caracteres[auxI] == '*' && caracteres[auxI + 1] == '/') {
                            System.out.println("Token final comentario de bloco OL");
                            auxI++;
                            return itera;
                        }
                    }
                }
            }
        
        System.out.println("ERRO Token comentario de bloco");
        return itera;
    }
    
    public void setDiretorio(String caminho){
        caminhoArq = caminho;
    }
}
