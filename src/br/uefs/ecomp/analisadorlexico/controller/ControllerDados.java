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
import java.util.regex.Pattern;

/**
 *
 * @author Alyson Dantas
 */
public class ControllerDados {
    
    private static ControllerDados unicaInstancia;
    private String conteudoArq = ""; //String para salvar o conteúdo do arquivo original antes da compactação
    private String caminhoArq = "";

	private ControllerDados() {
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
		
		while(linha != null) { //enquanto não chegar no fim do arquivo
			c = c + linha; //salvo o a letra da string na posição atual
			linha = buffRead.readLine(); //salvo a linha atual do arquivo na string
			if(linha!=null)
				c = c + "\n"; //acrescento uma quebra de linha em "c" a cada fim de linha em "linha"
		}
		buffRead.close(); //fecho a leitura do arquivo

		arquivoOriginal=c;//Uma varaivel local recebe a string com todo o conteudo do arquivo
		return c; //retorna a String com todo o conteúdo do arquivo de texto
	}

	public void escreverArquivo(String texto, String local, String nome) throws IOException { //método deixado aqui só para inspiração. Não vai ser usado do jeito que está descrito no momento
		local = local + nome; //anexo o nome do arquivo ao local que ele será escrito
		BufferedWriter buffWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(local), "ISO-8859-1")); //crio um novo objeto para escrita de arquivos e passo como parâmetro um novo objeto de escrita do arquivo no local especificado no padrão ISO-8859-1
		buffWrite.append(texto); //anexo essa string no arquivo de texto
		buffWrite.close(); //fecho o arquivo aberto
	}
        
        public void setConteudoArquivo(String arqmod){
		conteudoArq = arqmod;
	}
        
        public String getConteudoArquivo(){
		return conteudoArq;
	}
    
}
