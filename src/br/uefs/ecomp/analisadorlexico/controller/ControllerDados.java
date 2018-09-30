/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.controller;

import br.uefs.ecomp.analisadorlexico.model.Analisador;
import br.uefs.ecomp.analisadorlexico.model.Erros;
import br.uefs.ecomp.analisadorlexico.model.TipoToken;
import br.uefs.ecomp.analisadorlexico.model.Token;
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
    private Analisador fbi = new Analisador();
    private Iterator<String> itera;
    private int contSpaces = 0;
    private int contErros = 0;
    private ArrayList tokens;

    File diretorio;

    private ControllerDados() {
        contedudoArqLista = new ArrayList<String>();
        tokens = new ArrayList<Token>();
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
        contedudoArqLista = new ArrayList<String>();
        try {
            Reader arq = new InputStreamReader(new FileInputStream(local), "ISO-8859-1"); //inicializo arq como a leitura de arquivos no local especificado no padrão ISO-8859-1
            BufferedReader buffRead = new BufferedReader(arq); //crio um novo objeto BuffReader e passo para ele a leitura do local em arq
            String linha = buffRead.readLine(); //crio uma string auxiliar e já armazeno a primeira linha do arquivo
            String c = ""; //crio uma string auxiliar para armazenar o conteúdo da string
            if (linha != null) {
                contedudoArqLista.add(linha);
            }
            while (linha != null) { //enquanto não chegar no fim do arquivo

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
        } catch (FileNotFoundException e) {
            return null;
        }
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
            String c = lerArquivo(caminhoArq + arquivos.getName());
            if (c != null) {
                exibirConteudo();
                System.out.println("");
                analisadordelinhas(arquivos.getName());
            }
        }
    }

    public void analisadordelinhas(String nomeArq) throws IOException {
        itera = contedudoArqLista.iterator();
        auxI = 0;
        while (itera.hasNext()) {
            contLinha++;
            auxLinha = itera.next();
            if (auxLinha != null) {

                //System.err.println("linha: " + contLinha + " conteudo " + auxLinha);
                caracteres = auxLinha.toCharArray();
                for (auxI = 0; auxI < caracteres.length; auxI++) {
                    //System.err.println("Caracter lido: " + caracteres[auxI]);
                    if (caracteres[auxI] == '/') {
                        if (auxI + 1 < caracteres.length) {
                            if (caracteres[auxI + 1] == '/') {
                                System.out.println("Token comentario de linha");
                                String v = "";
                                int i = auxI;
                                for (i = auxI; i < caracteres.length; i++) {
                                    v = v + caracteres[i];
                                }
                                Token t = new Token(TipoToken.Id.TokenComentarioLinha, TipoToken.Nome.TokenComentarioLinha, v, contLinha);
                                tokens.add(t);
                                break;
                            } else if (caracteres[auxI + 1] == '*') {
                                //System.out.println("Token inicio comentario de bloco");
                                String v = analisetokenComentario();
                                if (v == null) {
                                    System.out.println("ERRO Token comentario de bloco");
                                    contErros++;
                                    Token t = new Token(Erros.Id.ComentarioAberto, Erros.Nome.ComentarioAberto, v, contLinha);
                                    tokens.add(t);
                                } else {
                                    System.out.println("TOKEN comentario de bloco");
                                    Token t = new Token(TipoToken.Id.TokenComentarioBloco, TipoToken.Nome.TokenComentarioBloco, v, contLinha);
                                    tokens.add(t);
                                }
                            } else {
                                System.out.println("TOKEN operador Aritmetico");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {
                            System.out.println("TOKEN operador Aritmetico");
                            Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (caracteres[auxI] == '-') {
                        if (auxI + 1 < caracteres.length) {
                            if (caracteres[auxI + 1] == '-') {
                                System.out.println("TOKEN Operador Aritmético");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                                auxI++;
                            } else {
                                int auxII;
                                String verifica = "";
                                for (auxII = auxI; auxII < caracteres.length; auxII++) {
                                    verifica = verifica + caracteres[auxII];
                                }
                                verifica = verifica.replace(" ", "");
                                //System.out.println("SP " + verifica);
                                char[] caracteresAux = verifica.toCharArray();

                                if (Analisador.validarDigito(caracteresAux[1] + "")) {
                                    String v = analisetokenNumero();
                                    if (Analisador.validarNumero(v)) {

                                        Token t = new Token(TipoToken.Id.TokenNumero, TipoToken.Nome.TokenNumero, v, contLinha);
                                        tokens.add(t);
                                        System.out.println("TOKEN Numero");
                                    } else {
                                        //System.out.println("ERR0 numero " + v);
                                        Token t = new Token(Erros.Id.ErroNumero, Erros.Nome.ErroNumero, v, contLinha);
                                        contErros++;
                                        tokens.add(t);
                                        System.out.println("ERRO Token numero mal formado");
                                    }
                                } else {
                                    System.out.println("TOKEN Operador Aritmético");
                                    Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                                    tokens.add(t);
                                }
                            }
                        } else {
                            System.out.println("TOKEN Operador Aritmético");
                            Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (caracteres[auxI] == '+') {
                        if (auxI + 1 < caracteres.length) {
                            if (caracteres[auxI + 1] == '+') {
                                System.out.println("TOKEN Operador Aritmético");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                                auxI++;
                            }else{
                                System.out.println("TOKEN Operador Aritmético");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {
                            System.out.println("TOKEN Operador Aritmético");
                            Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (Analisador.validarDigito(caracteres[auxI] + "")) {
                        if (auxI + 1 < caracteres.length) {
                            if (Analisador.validarDigito(caracteres[auxI + 1] + "") || caracteres[auxI + 1] == '.') {
                                String v = analisetokenNumero();
                                if (Analisador.validarNumero(v)) {
                                    System.out.println("TOKEN Numero");
                                    Token t = new Token(TipoToken.Id.TokenNumero, TipoToken.Nome.TokenNumero, v, contLinha);
                                    tokens.add(t);
                                } else {
                                    System.out.println("ERRO Token numero mal formado");
                                    contErros++;
                                    Token t = new Token(Erros.Id.ErroNumero, Erros.Nome.ErroNumero, v, contLinha);
                                    tokens.add(t);
                                }
                            } else {
                                System.out.println("TOKEN Dígito");
                                Token t = new Token(TipoToken.Id.TokenDigito, TipoToken.Nome.TokenDigito, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {
                            System.out.println("TOKEN Dígito");
                            Token t = new Token(TipoToken.Id.TokenDigito, TipoToken.Nome.TokenDigito, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }

                    } else if (caracteres[auxI] == '"') {
                        String v = analisetokenCadeiaCaracter();
                        if (v != null) {
                            if (Analisador.validarCadeiaCaracteres(v)) {
                                System.out.println("TOKEN cadeia de caracteres");
                                Token t = new Token(TipoToken.Id.TokenCadeiaCaracteres, TipoToken.Nome.TokenCadeiaCaracteres, v, contLinha);
                                tokens.add(t);
                            } else {
                                System.out.println("ERRO Token cadeia de caracteres");
                                contErros++;
                                Token t = new Token(Erros.Id.CadeiaCharsMalformada, Erros.Nome.CadeiaCharsMalformada, v, contLinha);
                                tokens.add(t);
                            }
                            //System.out.println("Conteudo da cadeia de caracteres: " + v);
                        } else {
                            System.out.println("ERRO Token cadeia de caracteres");
                            contErros++;
                            Token t = new Token(Erros.Id.CadeiaCharsMalformada, Erros.Nome.CadeiaCharsMalformada, v, contLinha);
                            tokens.add(t);
                        }
                    } else if (Analisador.validarLetra(caracteres[auxI] + "")) {
                        if (auxI + 1 < caracteres.length) {
                            if (Analisador.validarLetra(caracteres[auxI + 1] + "") || Analisador.validarDigito(caracteres[auxI + 1] + "") || caracteres[auxI + 1] == '_') {
                                String v = analisetokenIdentificador();
                                if (v != null) {
                                    if (Analisador.validarPalavrasReservadas(v)) {
                                        System.out.println("TOKEN de palavra reservada");
                                        Token t = new Token(TipoToken.Id.TokenPalavraReservada, TipoToken.Nome.TokenPalavraReservada, v, contLinha);
                                        tokens.add(t);
                                    } else if (Analisador.validarIdentificador(v)) {
                                        System.out.println("TOKEN de identificador");
                                        Token t = new Token(TipoToken.Id.TokenIdentificador, TipoToken.Nome.TokenIdentificador, v, contLinha);
                                        tokens.add(t);
                                    } else {
                                        System.out.println("ERRO Token de identificador mal formado");
                                        contErros++;
                                        Token t = new Token(Erros.Id.IdentificadorInvalido, Erros.Nome.IdentificadorInvalido, v, contLinha);
                                        tokens.add(t);
                                    }
                                    //System.out.println("Conteudo do identificador: " + v);
                                } else {
                                    System.out.println("ERRO Token identificador");
                                    contErros++;
                                    Token t = new Token(Erros.Id.IdentificadorInvalido, Erros.Nome.IdentificadorInvalido, v, contLinha);
                                    tokens.add(t);
                                }
                            } else {
                                System.out.println("TOKEN Letra");
                                Token t = new Token(TipoToken.Id.TokenLetra, TipoToken.Nome.TokenLetra, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {
                            System.out.println("TOKEN Letra");
                            Token t = new Token(TipoToken.Id.TokenLetra, TipoToken.Nome.TokenLetra, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "")) {
                        System.out.println("TOKEN Operador Aritimetico");
                        Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarOperadoresRelacionais(caracteres[auxI] + "")) {
                        System.out.println("TOKEN Operador Relacional");
                        Token t = new Token(TipoToken.Id.TokenOpRelacional, TipoToken.Nome.TokenOpRelacional, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarOperadoresLogicos(caracteres[auxI] + "")) {
                        System.out.println("TOKEN Operador Logico");
                        Token t = new Token(TipoToken.Id.TokenOpLogico, TipoToken.Nome.TokenOpLogico, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarDelimitadores(caracteres[auxI] + "")) {
                        System.out.println("TOKEN Delimitadores");
                        Token t = new Token(TipoToken.Id.TokenDelimitador, TipoToken.Nome.TokenDelimitador, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarEspaco((caracteres[auxI] + ""))) {
                        System.out.println("TOKEN espaço");
                        contSpaces++;
                    } else if (Analisador.validarSimbolos((caracteres[auxI] + ""))) {
                        System.out.println("TOKEN Simbolo");
                        Token t = new Token(TipoToken.Id.TokenSimbolo, TipoToken.Nome.TokenSimbolo, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else {
                        System.out.println("ERRO Simbolo invalido");
                        contErros++;
                        Token t = new Token(Erros.Id.ErroSimboloMalFormado, Erros.Nome.ErroSimboloMalFormado, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    }
                    //System.out.println("letra atual " + caracteres[auxI] + "\n");
                }
            }
        }

        Iterator<Token> it = tokens.iterator();
        Token t = null;
        String s = "";
        while (it.hasNext()) {
            t = it.next();
            s = s + "*" + t.getId() + " | " + t.getIdTipo() + " | " + t.getNome() + " | " + t.getLexema() + " | " + t.getLinha() + "\n";
        }
        s = s + "Quantidade de Espacos: " + contSpaces + "\n";
        s = s + "Quantidade de Erros: " + contErros;
        criaCaminho();
        escreverArquivo(s, caminhoArq + "saida/", nomeArq);
        tokens = new ArrayList<Token>();
        contErros = 0;
        contLinha = 0;
        contSpaces = 0;
    }

    public void setConteudoArquivo(String arqmod) {
        conteudoArq = arqmod;
    }

    public String getConteudoArquivo() {
        return conteudoArq;
    }

    /*private void analiseTokenNumero(char[] caracteres) {
        System.out.println("Analise Token Numero Begins");
        boolean b = false;
        for (int i = 0; i < caracteres.length; i++) {
        }
    }*/
    private String analisetokenIdentificador() {
        String result = "";
        if (auxI < caracteres.length) {
            //System.out.println("1: " + caracteres[auxI]);
            result = result + caracteres[auxI];
            int i = auxI + 1;
            //result = result + caracteres[i];
            //System.out.println("2: " + caracteres[auxI+1]);
            for (auxI = i; auxI < caracteres.length; auxI++) {
                //System.out.println("id: " + caracteres[auxI]);
                if (Analisador.validarEspaco(caracteres[auxI] + "") || Analisador.validarDelimitadores(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresLogicos(caracteres[auxI] + "") || Analisador.validarOperadoresRelacionais(caracteres[auxI] + "")) {
                    auxI--;
                    //System.out.println("acabou em " + caracteres[auxI]);
                    return result;
                } else {
                    result = result + caracteres[auxI];

                }
            }
        }
        auxI--;
        return result;
    }

    private String analisetokenNumero() {
        String result = "";
        boolean b = true;
        if (auxI < caracteres.length) {
//            System.out.println("1: " + caracteres[auxI]);
            result = result + caracteres[auxI];
            int i = auxI + 1;
            //result = result + caracteres[i];
//            System.out.println("2: " + caracteres[auxI+1]);
            for (auxI = i; auxI < caracteres.length; auxI++) {
//                System.out.println("id: " + caracteres[auxI]);
                if (Analisador.validarDelimitadores(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresLogicos(caracteres[auxI] + "") || Analisador.validarOperadoresRelacionais(caracteres[auxI] + "")) {
                    if (caracteres[auxI] == '.' && b) {
                        b = false;
                        result = result + caracteres[auxI];
//                        System.out.println("Detecçao do ponto @ "+ result);
                    } else {
                        auxI--;
//                        System.out.println("acabou em " + caracteres[auxI]);
//                        System.out.println("Resultado @ "+result);
                        return result;
                    }
                } else {
                    result = result + caracteres[auxI];
                }
            }
        }
        auxI--;
        return result;
    }

    private String analisetokenCadeiaCaracter() {
        String result = "";
        if (auxI < caracteres.length) {
            result = result + caracteres[auxI];
            int i = auxI + 1;
            for (auxI = i; auxI < caracteres.length; auxI++) {
                result = result + caracteres[auxI];
                if (caracteres[auxI] == '"' && caracteres[auxI - 1] != '/') {
                    auxI++;
                    return result;
                }
            }
        }
        return null;
    }

    private String analisetokenComentario() {
        boolean b = false;
        String result = "";
        if (auxI < caracteres.length) {
            b = true;
            result = result + caracteres[auxI];
            int i = auxI + 1;
            for (auxI = i; auxI < caracteres.length; auxI++) {
                result = result + caracteres[auxI];
                if (auxI + 1 < caracteres.length) {
                    if (caracteres[auxI] == '*' && caracteres[auxI + 1] == '/') {
                        result = result + caracteres[auxI + 1];
                        auxI++;
                        return result;
                    }
                }
            }
        }

        while (itera.hasNext()) {
            auxLinha = itera.next();
            contLinha++;
            //System.out.println("Linha: " + contLinha + " conteudo de comentario " + auxLinha);
            caracteres = auxLinha.toCharArray();
            for (auxI = 0; auxI < caracteres.length; auxI++) {
                result = result + caracteres[auxI];
                //System.out.println("caractre: " + auxI + " | linha: " + contLinha + " | conteudo: " + caracteres[auxI]);
                if (auxI + 1 < caracteres.length) {
                    if (caracteres[auxI] == '*' && caracteres[auxI + 1] == '/') {
                        result = result + caracteres[auxI + 1];
                        auxI++;
                        return result;
                    }
                }
            }
        }

        return null;

    }

    public void exibirConteudo() {
        Iterator<String> i = contedudoArqLista.iterator();
        String s = null;
        while (i.hasNext()) {
            s = i.next();
            System.out.println("" + s);
        }
    }

    public void setDiretorio(String caminho) {
        caminhoArq = caminho;
    }

    private void criaCaminho() {
        File caminhoArqs = new File(caminhoArq); // verifica se a pasta existe
        if (!caminhoArqs.exists()) {
            caminhoArqs.mkdirs(); //caso não exista cria a pasta
        }
        File caminhoSaida = new File(caminhoArq + "\\saida"); // verifica se a pasta existe
        if (!caminhoSaida.exists()) {
            caminhoSaida.mkdirs(); //caso não exista cria a pasta
        }

    }
}
