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
 * @author Alyson Dantas e Lucas Cardoso
 */
public class ControllerDados {

    private static ControllerDados unicaInstancia;//intancia do controller
    private String conteudoArq = ""; //String para salvar o conteúdo do arquivo original antes da compactação
    private String caminhoArq = "";//String para o caminho do arquivo
    private int contLinha = 0;//contadot de linhas
    private int auxI = 0;//variavel auxiliar para a contagem de caracteres
    private ArrayList contedudoArqLista;//linhas do conteudo do arquivo em uma lista
    private char[] caracteres;//caracteres da linha
    private String auxLinha = null;//variavel auxiliar para tratamento das linhas
    private Iterator<String> itera;//iterador de linhas
    private int contSpaces = 0;//contador de espaços
    private int contErros = 0;//contador de erros
    private ArrayList tokens;//tokens do arquivo
    File diretorio;//diretorio dos arquivos

    /**
     * Construtor do singleton
     */
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
    
    /**
     * Faz a leitura dos arquivos da pasta
     * @param local
     * @return
     * @throws IOException 
     */
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

    /**
     * Metodo de escrita em arquivo para a saida do analisador
     * @param texto
     * @param local
     * @param nome
     * @throws IOException 
     */
    public void escreverArquivo(String texto, String local, String nome) throws IOException { //método deixado aqui só para inspiração. Não vai ser usado do jeito que está descrito no momento
        local = local + nome; //anexo o nome do arquivo ao local que ele será escrito
        BufferedWriter buffWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(local), "ISO-8859-1")); //crio um novo objeto para escrita de arquivos e passo como parâmetro um novo objeto de escrita do arquivo no local especificado no padrão ISO-8859-1
        buffWrite.append(texto); //anexo essa string no arquivo de texto
        buffWrite.close(); //fecho o arquivo aberto
    }

    /**
     * Metodo que lista os arquivos de uma determinada pasta e inicia a analise
     * @throws IOException 
     */
    public void listaArquivos() throws IOException {
        diretorio = new File(caminhoArq);
        File afile[] = diretorio.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {//enquanto houver conteudo na pasta ele vai continuar enviando para o metodo analisador
            File arquivos = afile[i];
            System.out.println("Acessando um novo arquivo " + arquivos.getName());
            String c = lerArquivo(caminhoArq + arquivos.getName());
            if (c != null) {//se for um arquivo e não uma pasta ele realiza analise
                exibirConteudo();
                System.out.println("");
                analisadordelinhas(arquivos.getName());
            }
        }
    }

    /**
     * Metodo que inicia a analise de linhas do arquivo
     * @param nomeArq
     * @throws IOException 
     */
    public void analisadordelinhas(String nomeArq) throws IOException {
        itera = contedudoArqLista.iterator();//iterador recebe o conteudo da do arquivo
        auxI = 0;
        while (itera.hasNext()) {
            contLinha++;//lendo mais uma linha
            auxLinha = itera.next();
            if (auxLinha != null) {//caso a linha não for nula
                //System.err.println("linha: " + contLinha + " conteudo " + auxLinha);
                caracteres = auxLinha.toCharArray();//converte a string em array de caracteres
                for (auxI = 0; auxI < caracteres.length; auxI++) {//percorrer o array
                    //System.err.println("Caracter lido: " + caracteres[auxI]);
                    if (caracteres[auxI] == '/') {//caso encontre um / deve verificar se é para comentario ou para operador aritmetico
                        if (auxI + 1 < caracteres.length) {
                            if (caracteres[auxI + 1] == '/') {//caso o proximo elemento seja outra / a linha a partir dai é um comentario
                                System.out.println("Token comentario de linha");
                                String v = "";
                                int i = auxI;
                                for (i = auxI; i < caracteres.length; i++) {//captura todo resto da linha
                                    v = v + caracteres[i];
                                }
                                Token t = new Token(TipoToken.Id.TokenComentarioLinha, TipoToken.Nome.TokenComentarioLinha, v, contLinha);
                                tokens.add(t);//cria um token e add na lista
                                break;
                            } else if (caracteres[auxI + 1] == '*') {//é um comentario de bloco
                                //System.out.println("Token inicio comentario de bloco");
                                String v = analisetokenComentario();
                                if (v == null) {//se a analise retornr diferente de null ele encontrou um fim
                                    System.out.println("ERRO Token comentario de bloco");
                                    contErros++;
                                    Token t = new Token(Erros.Id.ComentarioAberto, Erros.Nome.ComentarioAberto, v, contLinha);
                                    tokens.add(t);
                                } else {//se retornou null não foi encontrado um fim
                                    System.out.println("ERRO TOKEN comentario de bloco");
                                    Token t = new Token(TipoToken.Id.TokenComentarioBloco, TipoToken.Nome.TokenComentarioBloco, v, contLinha);
                                    tokens.add(t);
                                }
                            } else {//caso não seja nada disso é um operador aritmetico
                                System.out.println("TOKEN operador Aritmetico");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {//caso não exista proximo é um operador aritmetico
                            System.out.println("TOKEN operador Aritmetico");
                            Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (caracteres[auxI] == '-') {//caso encontre no caractere um - deve detectar a procedencia
                        if (auxI + 1 < caracteres.length) {
                            if (caracteres[auxI + 1] == '-') {//caso o proximo seja outro - é um token
                                System.out.println("TOKEN Operador Aritmético");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                                auxI++;
                            } else {//se não for deve verificar se é um numero
                                int auxII;
                                String verifica = "";
                                for (auxII = auxI; auxII < caracteres.length; auxII++) {//pega todo o resto da linha e transforma em string para verificar se não existem espaços entre os digitos
                                    verifica = verifica + caracteres[auxII];
                                }
                                verifica = verifica.replace(" ", "");//remove espaços
                                //System.out.println("SP " + verifica);
                                char[] caracteresAux = verifica.toCharArray();//transforma nvamente em vetor

                                if (Analisador.validarDigito(caracteresAux[1] + "")) {//analisa o proximo caractere para ver se é digito
                                    String v = analisetokenNumero();
                                    if (Analisador.validarNumero(v)) {//se for numero

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
                                } else {//caso o proximo não for digito é op aritmetico
                                    System.out.println("TOKEN Operador Aritmético");
                                    Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                                    tokens.add(t);
                                }
                            }
                        } else {//caso não exista proximo é um operador aritmetico
                            System.out.println("TOKEN Operador Aritmético");
                            Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (caracteres[auxI] == '+') {//caso ele detecte um + verifica a procedencia
                        if (auxI + 1 < caracteres.length) {
                            if (caracteres[auxI + 1] == '+') {//caso o proximo seja outro + é um token unico aritmetico
                                System.out.println("TOKEN Operador Aritmético");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                                auxI++;
                            }else{//caso não seja é isolado token aritmetico
                                System.out.println("TOKEN Operador Aritmético");
                                Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {//caso nao exista proximo é op aritmetico
                            System.out.println("TOKEN Operador Aritmético");
                            Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (Analisador.validarDigito(caracteres[auxI] + "")) {//caso o caractere atual seja um digito
                        if (auxI + 1 < caracteres.length) {
                            if (Analisador.validarDigito(caracteres[auxI + 1] + "") || caracteres[auxI + 1] == '.') {//se o proximo for um ponto pode ser um numero
                                String v = analisetokenNumero();
                                if (Analisador.validarNumero(v)) {
                                    System.out.println("TOKEN Numero");
                                    Token t = new Token(TipoToken.Id.TokenNumero, TipoToken.Nome.TokenNumero, v, contLinha);
                                    tokens.add(t);
                                } else {//caso o numero for um erro
                                    System.out.println("ERRO Token numero mal formado");
                                    contErros++;
                                    Token t = new Token(Erros.Id.ErroNumero, Erros.Nome.ErroNumero, v, contLinha);
                                    tokens.add(t);
                                }
                            } else {//se existe é um digito
                                System.out.println("TOKEN Dígito");
                                Token t = new Token(TipoToken.Id.TokenDigito, TipoToken.Nome.TokenDigito, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {//caso seja o ultimo da linha é um digito
                            System.out.println("TOKEN Dígito");
                            Token t = new Token(TipoToken.Id.TokenDigito, TipoToken.Nome.TokenDigito, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }

                    } else if (caracteres[auxI] == '"') {//inicia a busca pelo resto da cadeia de caracteres
                        String v = analisetokenCadeiaCaracter();
                        if (v != null) {//caso encontre um final
                            if (Analisador.validarCadeiaCaracteres(v)) {
                                System.out.println("TOKEN cadeia de caracteres");
                                Token t = new Token(TipoToken.Id.TokenCadeiaCaracteres, TipoToken.Nome.TokenCadeiaCaracteres, v, contLinha);
                                tokens.add(t);
                            } else {//se não for valida
                                System.out.println("ERRO Token cadeia de caracteres");
                                contErros++;
                                Token t = new Token(Erros.Id.CadeiaCharsMalformada, Erros.Nome.CadeiaCharsMalformada, v, contLinha);
                                tokens.add(t);
                            }
                            //System.out.println("Conteudo da cadeia de caracteres: " + v);
                        } else {//caso não encontre um final
                            System.out.println("ERRO Token cadeia de caracteres");
                            contErros++;
                            Token t = new Token(Erros.Id.CadeiaCharsMalformada, Erros.Nome.CadeiaCharsMalformada, v, contLinha);
                            tokens.add(t);
                        }
                    } else if (Analisador.validarLetra(caracteres[auxI] + "")) {//verifica se é apenas uma letra ou identificador
                        if (auxI + 1 < caracteres.length) {
                            if (Analisador.validarLetra(caracteres[auxI + 1] + "") || Analisador.validarDigito(caracteres[auxI + 1] + "") || caracteres[auxI + 1] == '_') {//caso o proximo seja uma letra, digito ou _ ele é um identificador
                                String v = analisetokenIdentificador();
                                if (v != null) {//caso o identificar for valido
                                    if (Analisador.validarPalavrasReservadas(v)) {//verifica se é uma palavra reservada
                                        System.out.println("TOKEN de palavra reservada");
                                        Token t = new Token(TipoToken.Id.TokenPalavraReservada, TipoToken.Nome.TokenPalavraReservada, v, contLinha);
                                        tokens.add(t);
                                    } else if (Analisador.validarIdentificador(v)) {//verifica se é um identificador
                                        System.out.println("TOKEN de identificador");
                                        Token t = new Token(TipoToken.Id.TokenIdentificador, TipoToken.Nome.TokenIdentificador, v, contLinha);
                                        tokens.add(t);
                                    } else {//caso não seja nenhum dos dois é um erro
                                        System.out.println("ERRO Token de identificador mal formado");
                                        contErros++;
                                        Token t = new Token(Erros.Id.IdentificadorInvalido, Erros.Nome.IdentificadorInvalido, v, contLinha);
                                        tokens.add(t);
                                    }
                                    //System.out.println("Conteudo do identificador: " + v);
                                } else {//caso receba nulo é um erro
                                    System.out.println("ERRO Token identificador");
                                    contErros++;
                                    Token t = new Token(Erros.Id.IdentificadorInvalido, Erros.Nome.IdentificadorInvalido, v, contLinha);
                                    tokens.add(t);
                                }
                            } else {//caso o proximo não for nenhuma das coisa esse é apenas uma letra
                                System.out.println("TOKEN Letra");
                                Token t = new Token(TipoToken.Id.TokenLetra, TipoToken.Nome.TokenLetra, caracteres[auxI] + "", contLinha);
                                tokens.add(t);
                            }
                        } else {//caso seja o final da linha esse é apenas uma letra
                            System.out.println("TOKEN Letra");
                            Token t = new Token(TipoToken.Id.TokenLetra, TipoToken.Nome.TokenLetra, caracteres[auxI] + "", contLinha);
                            tokens.add(t);
                        }
                    } else if (Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "")) {//caso seja um operador aritmetico
                        System.out.println("TOKEN Operador Aritimetico");
                        Token t = new Token(TipoToken.Id.TokenOpAritmetico, TipoToken.Nome.TokenOpAritmetico, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarOperadoresRelacionais(caracteres[auxI] + "")) {//caso sela um operador relacional
                        System.out.println("TOKEN Operador Relacional");
                        Token t = new Token(TipoToken.Id.TokenOpRelacional, TipoToken.Nome.TokenOpRelacional, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarOperadoresLogicos(caracteres[auxI] + "")) {//caso seja um operador logico
                        System.out.println("TOKEN Operador Logico");
                        Token t = new Token(TipoToken.Id.TokenOpLogico, TipoToken.Nome.TokenOpLogico, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarDelimitadores(caracteres[auxI] + "")) {//caso seja um delimitador
                        System.out.println("TOKEN Delimitadores");
                        Token t = new Token(TipoToken.Id.TokenDelimitador, TipoToken.Nome.TokenDelimitador, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else if (Analisador.validarEspaco((caracteres[auxI] + ""))) {//caso seja um espaço
                        System.out.println("TOKEN espaço");
                        contSpaces++;
                    } else if (Analisador.validarSimbolos((caracteres[auxI] + ""))) {//caso seja um simbolo
                        System.out.println("TOKEN Simbolo");
                        Token t = new Token(TipoToken.Id.TokenSimbolo, TipoToken.Nome.TokenSimbolo, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    } else {//se não for nada é um erro
                        System.out.println("ERRO Simbolo invalido");
                        contErros++;
                        Token t = new Token(Erros.Id.ErroSimboloMalFormado, Erros.Nome.ErroSimboloMalFormado, caracteres[auxI] + "", contLinha);
                        tokens.add(t);
                    }
                    //System.out.println("letra atual " + caracteres[auxI] + "\n");
                }
            }
        }
        //realiza a escrita no arquivo
        Iterator<Token> it = tokens.iterator();
        Token t = null;
        String s = "";
        while (it.hasNext()) {
            t = it.next();
            s = s + "*ID " + t.getId() + " | Tipo: " + t.getIdTipo() + " | Nome: " + t.getNome() + " | Lexema: " + t.getLexema() + " | Linha: " + t.getLinha() + "\n";
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
    
    /**
     * Metodo que seta o conteudo do arquivo
     * @param arqmod 
     */
    public void setConteudoArquivo(String arqmod) {
        conteudoArq = arqmod;
    }

    /**
     * Metodo que retorna o conteudo do arquivo
     * @return 
     */
    public String getConteudoArquivo() {
        return conteudoArq;
    }
    
    /**
     * Metodo que realiza a analise do token identificador
     * @return 
     */
    private String analisetokenIdentificador() {
        String result = "";
        if (auxI < caracteres.length) {
            result = result + caracteres[auxI];
            int i = auxI + 1;
            for (auxI = i; auxI < caracteres.length; auxI++) {
                if (Analisador.validarEspaco(caracteres[auxI] + "") || Analisador.validarDelimitadores(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresLogicos(caracteres[auxI] + "") || Analisador.validarOperadoresRelacionais(caracteres[auxI] + "")) {//caso encontre um desse ele deve parar de procurar o identificador
                    auxI--;
                    return result;
                } else {
                    result = result + caracteres[auxI];
                }
            }
        }
        auxI--;
        return result;
    }

    /**
     * Metodo que realiza a analise do numero
     * @return 
     */
    private String analisetokenNumero() {
        String result = "";
        boolean b = true;
        if (auxI < caracteres.length) {
            result = result + caracteres[auxI];
            int i = auxI + 1;
            for (auxI = i; auxI < caracteres.length; auxI++) {
                if (Analisador.validarDelimitadores(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresAritimeticos(caracteres[auxI] + "") || Analisador.validarOperadoresLogicos(caracteres[auxI] + "") || Analisador.validarOperadoresRelacionais(caracteres[auxI] + "")) {
                    if (caracteres[auxI] == '.' && b) {
                        b = false;
                        result = result + caracteres[auxI];
                    } else {
                        auxI--;
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

    /**
     * Metodo que realiza a analise de uma cadeia de caracteres
     * @return 
     */
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

    /**
     * Metodo que realiza a analise de comentarios
     * @return 
     */
    private String analisetokenComentario() {
        boolean b = false;
        String result = "";
        if (auxI < caracteres.length) {//analisa a linha atual
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

        while (itera.hasNext()) {//analisa o resto do arquivo em busca do final do comentrio 
            auxLinha = itera.next();
            contLinha++;
            caracteres = auxLinha.toCharArray();
            for (auxI = 0; auxI < caracteres.length; auxI++) {
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
        return null;
    }

    /**
     * Metodo que exibe o conteudo do arquivo
     */
    public void exibirConteudo() {
        Iterator<String> i = contedudoArqLista.iterator();
        String s = null;
        while (i.hasNext()) {
            s = i.next();
            System.out.println("" + s);
        }
    }

    /**
     * Metodo que seta o caminho para o arquivo
     * @param caminho 
     */
    public void setDiretorio(String caminho) {
        caminhoArq = caminho;
    }

    /**
     * Metodo que cria o caminho para o arquivo de saida
     */
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
