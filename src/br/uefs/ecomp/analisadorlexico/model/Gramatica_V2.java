/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.ArrayList;
import javax.xml.transform.Source;

/**
 *
 * @author Aleatorio
 */
public class Gramatica_V2 {

    private Token tokenAtual, tokenAnterior;
    private int posicao = -1;
    private ArrayList<Token> tokens;
    private ArrayList naoTerminais;
    private String msgErro;
    private String linhasMain;
    private int contMain;
    private int contErros;
    private int contClass;
    private int camada = 0; // 0 - antes de classes | 1 - dentro de classe | 2 - dentro de metodos | 3 - dentro de condicionais

    public Gramatica_V2(ArrayList tokens) {
        contMain = 0;
        msgErro = "";
        linhasMain = "";
        contClass = 0;
        contErros = 0;
        naoTerminais = new ArrayList<NaoTerminal>();
        this.tokens = tokens;
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
            System.out.println("Match - (Nome) " + tokenAnterior.getNome() + " | (Lexema)  " + tokenAnterior.getLexema() + " | Passado  " + tipo);
            return true;
        }
        System.out.println("No Match - (Nome)  " + tokenAtual.getNome() + " | (Lexema)  " + tokenAtual.getLexema() + " | Passado  " + tipo);
        return false;
    }

    private boolean tiposPrimarios() {
        System.out.println("Comecou Tipos Primarios");
        return match("int") || match("bool") || match("float") || match("string");
    }

    private boolean eBoolean() {
        System.out.println("Comecou eBoolean");
        return match("true") || match("false");
    }

    private boolean Numero() {
        System.out.println("Comecou Numero");
        if (match("-")) {
            if (match("Digito") || match("Numero")) {
                return true;
            }
        }
        if (match("Digito") || match("Numero")) {
            return true;
        }
        System.out.println("Terminou Numero");
        return false;
    }

    private boolean operadorAtitmetico() {
        System.out.println("Comecou OperadorAritmetico");
        return match("++") || match("--") || match("+") || match("-") || match("*") || match("/");
    }

    private boolean operadorLogico() {
        System.out.println("Comecou OperadorLogico");
        return match("!") || match("&&") || match("||");
    }

    private boolean operadorRelacional() {
        System.out.println("Comecou OperadorRelacional");
        return match("!=") || match("==") || match("<") || match("<=") || match(">") || match(">=") || match("=");
    }

    private boolean atribuicao() {
        System.out.println("Comecou Atribuição");
        boolean b;
        if (tiposParametros()) {
            match("Identificador");
            if (match("=")) {
                if (expressaoAritimetica()) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete();
                    }
                    System.out.println("Terminou Atribuição");
                    return true;
                } else if (valorInicializacao()) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete();
                    }
                    System.out.println("Terminou Atribuição");
                    return true;
                } else if (acessoAtributo()) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete();
                    }
                    System.out.println("Terminou Atribuição");
                    return true;
                } else {
                    modoPaniquete();
                }
            } else if (matriz()) {
                b = match(";");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Atribuição");
                return true;
            } else if (operadorAtitmetico()) {
                b = match(";");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Atribuição");
                return true;
            } else if (acessoAtributo()) {
                b = match("=");
                if (!b) {
                    modoPaniquete();
                }
                valorInicializacao();
                b = match(";");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Atribuição");
                return true;
            } else {
                modoPaniquete();
            }
        } else {
            match("Identificador");
            if (match("=")) {
                if (acessoAtributo()) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete();
                    }
                    System.out.println("Terminou Atribuição");
                    return true;
                } else if (valorInicializacao()) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete();
                    }
                    System.out.println("Terminou Atribuição");
                    return true;
                } else if (expressaoAritimetica()) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete();
                    }
                    System.out.println("Terminou Atribuição");
                    return true;
                } else {
                    modoPaniquete();
                }
            } else if (matriz()) {
                b = match(";");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Atribuição");
                return true;
            } else if (operadorAtitmetico()) {
                b = match(";");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Atribuição");
                return true;
            } else if (acessoAtributo()) {
                b = match("=");
                if (!b) {
                    modoPaniquete();
                }
                b = valorInicializacao();
                if (!b) {
                    modoPaniquete();
                }
                b = match(";");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Atribuição");
                return true;
            }
        }
        System.out.println("Terminou Atribuição");
        return false;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String start() {
        System.out.println("startou");
        passaToken();
        constante();
        classe();
        System.out.println("Deu certo");
        String s = "";

        s = s + "Quantidade de Mains: " + contMain + " \n";
        s = s + "Quantidade de Erros: " + contErros + " \n";

        if (contMain > 1) {
            s = s + "Erro! - deve existir ao menos uma classe! \n";
        } else {
            s = s + "Quantidade de Classes: " + contClass + " \n";;
        }

        if (contErros == 0 && contMain < 2) {
            s = s + "SUCESSO!!! - Nao ha erros";
        } else {
            s = s + msgErro + "\n";
        }

        if (contMain > 1) {
            s = s + "Erro!!! - nao deve existir mais que uma main! \n";
            s = s + linhasMain + "\n";
        }

        return s;
    }

    private void constante() {
        System.out.println("Começou bloco Constante");
        boolean b;
        b = match("const");
        if (b) {
            camada = 1;
            b = match("{");
            if (b) {
                codigoConstante();
                b = match("}");
                if (!b) {
                    modoPaniquete();
                } else {
                    camada = 0;
                    System.out.println("Terminou Constante");
                }
            } else {
                modoPaniquete();
            }
        }
    }

    private void codigoConstante() {
        System.out.println("Comecou Codigo Constante");
        boolean b;
        b = tiposPrimarios();
        camada = 2;
        if (!b) {
            modoPaniquete();
        }
        declaracaoConstante();
        b = match(";");
        if (!b) {
            modoPaniquete();
        }
        if (tokenAtual.getLexema().equals("int") || tokenAtual.getLexema().equals("float") || tokenAtual.getLexema().equals("bool") || tokenAtual.getLexema().equals("string")) {
            codigoConstante();
        }
        camada = 1;
        System.out.println("Terminou Codigo Constante");
    }

    private void declaracaoConstante() {
        System.out.println("Comecou Declaração Constante");
        boolean b;
        b = match("Identificador");
        if (b) {
            if (match("=")) {
                b = valorInicializacao();
                if (!b) {
                    modoPaniquete();
                }
                if (match(",")) {
                    declaracaoConstante();
                }
            } else if (match(",")) {
                declaracaoConstante();
            }
        } else {
            modoPaniquete();
        }
        System.out.println("Terminou Declaração Constante");
    }

    private boolean variaveis() {
        System.out.println("Começou bloco Variaveis");
        if (match("variables")) {
            boolean b;
            camada = 2;
            b = match("{");
            if (b) {
                codigoVariaveis();
                b = match("}");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Variaveis");
                return true;
            } else {
                modoPaniquete();
            }
        } 
        System.out.println("Terminou Variaveis");
        return false;
    }

    private void codigoVariaveis() {
        System.out.println("Comecou Codigo Variaveis");
        boolean b;
        if (tiposPrimarios()) {
            declaracaoVariaveis();
            b = match(";");
            if (!b) {
                modoPaniquete();
            }
            if (!tokenAtual.getLexema().equals("}")) {
                codigoVariaveis();
            }
            System.out.println("Terminou Codigo Variaveis");
        } else if (match("Identificador")) {
            declaracaoVariaveis();
            if (!tokenAtual.getLexema().equals("}")) {
                codigoVariaveis();
            }
            System.out.println("Terminou Codigo Variaveis");
        } else {
            modoPaniquete();
        }
    }

    private void declaracaoVariaveis() {
        System.out.println("Comecou declaração Variaveis");
        boolean b;
        if (tokenAtual.getNome().equals("Identificador")) {
            b = match("Identificador");
            if (!b) {
                modoPaniquete();
            }
            if (tokenAtual.getLexema().equals("=")) {
                b = match("=");
                if (!b) {
                    modoPaniquete();
                }
                b = valorInicializacao();
                if (!b) {
                    modoPaniquete();
                }
                if (tokenAtual.getLexema().equals(",")) {
                    match(",");
                    declaracaoVariaveis();
                }
            } else if (tokenAtual.getLexema().equals(",")) {
                match(",");
                declaracaoVariaveis();
                System.out.println("Terminou Declaraçao Variaveis");
            } else if (matriz()) {
                if (tokenAtual.getLexema().equals("=")) {
                    match("=");
                    if (tokenAtual.getLexema().equals("{")) {
                        inicializaMatriz();
                        if (tokenAtual.getLexema().equals(",")) {
                            match(",");
                            declaracaoVariaveis();
                        }
                    } else {
                        chamadaMetodo();
                        if (tokenAtual.getLexema().equals(",")) {
                            match(",");
                            declaracaoVariaveis();
                        }
                    }
                }
                System.out.println("Terminou Declaração Variaveis");
            }
        }
    }

    private boolean valorInicializacao() {
        System.out.println("Comecou Valor Inicialização");
        if (acessoMetodo()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (eBoolean()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (cadeiaCaracter()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (expressaoAritimetica()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (acessoAtributo()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        }
        System.out.println("Terminou Valor Inicialização");
        return false;
    }

    private void classe() {
        System.out.println("Começou bloco Classe");
        boolean b;
        b = match("class");
        if (b) {
            camada = 1;
            contClass++;
            b = match("Identificador");
            if (b) {
                if (tokenAtual.getLexema().equals("extends")) {
                    b = match("extends");
                    if (b) {
                        b = match("Identificador");
                        if (!b) {
                            modoPaniquete();
                        }
                    }
                }
                b = match("{");
                if (b) {
                    codigoClasse();
                    b = match("}");
                    if (!b) {
                        modoPaniquete();
                    } else {
                        camada = 0;
                    }
                }
                if (tokenAtual.getLexema().equals("class")) {
                    classe();
                }
            } else {
                modoPaniquete();
            }
        }
        camada = 0;
        System.out.println("Terminou Classe");

    }

    private void codigoClasse() {
        System.out.println("Comecou codigo Classe");
        boolean b;
        if (variaveis()) {
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        } else if (metodo()) {
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        } else if (chamadaMetodo()) {
            b = match(";");
            if (!b) {
                modoPaniquete();
            }
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        } else if (acessoAtributo()) {
            b = match(";");
            if (!b) {
                modoPaniquete();
            }
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        } else if (!tokenAtual.getLexema().equals("}")) {
            modoPaniquete();
        }
        System.out.println("Terminou Codigo Classe");
    }

    private boolean metodo() {
        boolean b;
        System.out.println("Começou Metodo");
        if (tokenAtual.getLexema().equals("method")) {
            camada = 2;
            b = match("method");
            if (!b) {
                modoPaniquete();
            }
            b = tipoRetorno();
            if (!b) {
                modoPaniquete();
            }
            if (tokenAtual.getLexema().equals("main")) {
                match("main");
                System.out.println("Achou uma main");
                contMain++;
            } else {
                b = match("Identificador");
                if (!b) {
                    modoPaniquete();
                }
            }
            b = match("(");
            if (!b) {
                modoPaniquete();
            }
            parametrosMetodo();
            b = match(")");
            if (!b) {
                modoPaniquete();
            }
            b = match("{");
            if (!b) {
                modoPaniquete();
            }
            codigoMetodo();
            if (tokenAtual.getLexema().equals("return")) {
                System.out.println("Entrou aqui");
                match("return");
                retorno();
                b = match(";");
                if (!b) {
                    modoPaniquete();
                }
            }
            b = match("}");
            if (!b) {
                modoPaniquete();
            }
            System.out.println("Terminou Metodo");
            camada = 1;
            return true;
        }
        return false;
    }

    private boolean tipoRetorno() {
        System.out.println("Começou Tipo Retorno");
        if (tiposPrimarios()) {
            System.out.println("Terminou Retorno");
            return true;
        } else if (match("void")) {
            System.out.println("Terminou Retorno");
            return true;
        } else if (match("Identificador")) {
            System.out.println("Terminou Retorno");
            return true;
        }
        return false;
    }

    private void parametrosMetodo() {
        System.out.println("Começou Parametros Metodo");
        boolean b;
        b = tiposParametros();
        if (!b) {
            modoPaniquete();
        }
        if (match("Identificador")) {
            if (match(",")) {
                parametrosMetodo();
            } else if (matriz()) {
                if (match(",")) {
                    parametrosMetodo();
                }
            }
        }
        System.out.println("Terminou Parametros Metodo");
    }

    private boolean tiposParametros() {
        System.out.println("Começou Tipos Parametros");
        if (tiposPrimarios()) {
            System.out.println("Terminou Tipos Parametros");
            return true;
        } else if (match("Identificador")) {
            System.out.println("Terminou Tipos Parametros");
            return true;
        } else if (tokenAtual.getLexema().equals(")")) {
            return true;
        }
        return false;
    }

    private void codigoMetodo() {
        System.out.println("Começou Codigo Metodo");
        if (variaveis()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo();
        } else if (atribuicao()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo();
        } else if (While()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo();
        } else if (Read()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo();
        } else if (Write()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo();
        } else if (If()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo();
        } else if (chamadaMetodo()) {
            boolean b;
            b = match(";");
            if (!b) {
                modoPaniquete();
            }
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo();
        } 
            camada = 2;
        System.out.println("Terminou Codigo Metodo");
    }

    private boolean retorno() {
        System.out.println("Começou Retorno");
        if (match("(")) {
            if (expressaoAritimetica()) {
                System.out.println("Terminou Retorno");
                match(")");
                return true;

            } else if (eBoolean()) {
                System.out.println("Terminou Retorno");
                match(")");
                return true;
            }
        } else {
            if (expressaoAritimetica()) {
                System.out.println("Terminou Retorno");
                return true;
            } else if (eBoolean()) {
                System.out.println("Terminou Retorno");
                return true;
            }
        }
        return false;
    }

    private boolean matriz() {
        System.out.println("Comecou Matriz");
        boolean b;
        if (match("[")) {
            b = indiceMatriz();
            
            b = match("]");
            if (!b) {
                modoPaniquete();
            }
            if (tokenAtual.getLexema().equals("[")) {
                matriz();
            }
            System.out.println("Terminou Matriz");
            return true;
        }
        System.out.println("Terminou Matriz");
        return false;
    }

    private boolean indiceMatriz() {
        System.out.println("Comecou Indice Matriz");
        if (Numero()) {
            System.out.println("Terminou Tndece Matriz");
            return true;
        } else if (match("Identificador")) {
            if (operadorAtitmetico()) {
                System.out.println("Terminou Indice Matriz");
                return true;
            }
            System.out.println("Terminou Indice Matriz");
            return true;
        }
        System.out.println("Terminou Indice Matriz");
        return false;
    }

    private void inicializaMatriz() {
        System.out.println("Comecou Inicializa Matriz");
        boolean b;
        b = match("{");
        if (!b) {
            modoPaniquete();
        }
        linhasMatriz();
        b = match("}");
        if (!b) {
            modoPaniquete();
        }
        System.out.println("Terminou Inicializa Matriz");
    }

    private void linhasMatriz() {
        System.out.println("Comecou Linhas Matriz");
        boolean b;
        b = match("(");
        if (!b) {
            modoPaniquete();
        }
        b = colunasMatriz();
        if (!b) {
            modoPaniquete();
        }
        b = match(")");
        if (!b) {
            modoPaniquete();
        }
        if (match(",")) {
            linhasMatriz();
        }
        System.out.println("Terminou Linhas Matriz");
    }

    private boolean colunasMatriz() {
        System.out.println("Comecou Colunas Matriz");
        if (Numero()) {
            if (match(",")) {
                colunasMatriz();
            }
            System.out.println("Terminou Colunas Matriz");
            return true;
        } else if (chamadaMetodo()) {
            System.out.println("Terminou Colunas Matriz");
            return true;
        }
        System.out.println("Terminou Colunas Matriz");
        return false;
    }

    private boolean chamadaMetodo() {
        System.out.println("Começou Chamda Metosdo");
        if (match("Identificador")) {
            if (match(".")) {
                chamadaMetodobase();
            } else {
                modoPaniquete();
            }
            System.out.println("Terminou Chamada Metodo");
            return true;
        }
        System.out.println("Terminou Chamada Metodo");
        return false;
    }

    private void chamadaMetodobase() {
        System.out.println("Começou Chamada Metodo Base");
        boolean b;
        b = match("Identificador");
        if (!b) {
            modoPaniquete();
        }
        b = match("(");
        if (!b) {
            modoPaniquete();
        }
        argumentosMetodo();
        b = match(")");
        if (!b) {
            modoPaniquete();
        }
        System.out.println("Terminou Chamada Metodo Base");
    }

    private void argumentosMetodo() {
        System.out.println("Começou Argumentos Metodos");
        boolean b;
        b = valoresArgumentos();
        if (!b) {
            modoPaniquete();
        }
        if (match(",")) {
            argumentosMetodo();
        }
        System.out.println("Terminou Argumentos Metodos");
    }

    private boolean valoresArgumentos() {
        System.out.println("Começou Valores Argumentos");
        if (Numero()) {
            System.out.println("Terminou Valores Argumentos");
            return true;
        } else if (eBoolean()) {
            System.out.println("Terminou Valores Argumentos");
            return true;
        } else if (chamadaMetodo()) {
            System.out.println("Terminou Valores Argumentos");
            return true;
        } else if (match("Identificador")) {
            if (matriz()) {
                System.out.println("Terminou Valores Argumentos");
                return true;
            }
            System.out.println("Terminou Valores Argumentos");
            return true;
        } else if (acessoAtributo()) {
            System.out.println("Terminou Valores Argumentos");
            return true;
        }
        return false;
    }

    private boolean expressaoAritimetica() {
        System.out.println("Comecou Expressao Aritmetica");
        if (multExpr()) {
            if (match("+") || match("-")) {
                expressaoAritimetica();
                System.out.println("Terminou Expresssao Aritmetica");
                return true;
            }
            System.out.println("Terminou Expresssao Aritmetica");
            return true;
        }
        System.out.println("Terminou Expresssao Aritmetica");
        return false;
    }

    private boolean multExpr() {
        System.out.println("Comecou MultExpr");
        if (negExpr()) {
            if (match("*") || match("/")) {
                multExpr();
                System.out.println("Terminou MultExpt");
                return true;
            }
            System.out.println("Terminou MultExpt");
            return true;
        }
        System.out.println("Terminou MultExpt");
        return false;
    }

    private boolean negExpr() {
        System.out.println("Comecou NegExpr");
        if (match("-")) {
            valorExpr();
            System.out.println("Terminou NegExpt");
            return true;
        } else {
            valorExpr();
            System.out.println("Terminou NegExpt");
            return true;
        }
    }

    private boolean valorExpr() {
        System.out.println("Comecou ValorExpr");
        if (Numero()) {
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (match("Identificador")) {
            if (matriz()) {
                System.out.println("Terminou ValorExpt");
                return true;
            }
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (chamadaMetodo()) {
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (acessoAtributo()) {
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (match("(")) {
            expressaoAritimetica();
            match(")");
            System.out.println("Terminou ValorExpt");
            return true;
        }
        System.out.println("Terminou ValorExpt");
        return false;
    }

    private boolean acessoAtributo() {
        System.out.println("Começou acesso Atributo");
        boolean b;
        if (match("Identificador")) {
            if (match(".")) {
                b = match("Identificador");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou acesso Atributo");
                return true;
            } else if (matriz()) {
                b = match(".");
                if (!b) {
                    modoPaniquete();
                }
                b = match("Identificador");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou acesso Atributo");
                return true;
            } else {
                modoPaniquete();
            }
            System.out.println("Terminou acesso Atributo");
            return true;
        }
        System.out.println("Terminou acesso Atributo");
        return false;
    }

    private boolean acessoMetodo() {
        System.out.println("Começou Acesso Metodo");
        boolean b;
        if (match("Identificador")) {
            if (match(".")) {
                b = match("Identificador");
                if (!b) {
                    modoPaniquete();
                }
                b = match("(");
                if (!b) {
                    modoPaniquete();
                }
                if (parametrosRead()) {
                    b = match(")");
                    if (!b) {
                        modoPaniquete();
                    }
                    System.out.println("Terminou Acesso Metodo");
                    return true;
                }
                b = match(")");
                if (!b) {
                    modoPaniquete();
                }
                System.out.println("Terminou Acesso Metodo");
                return true;
            }
        }
        System.out.println("Terminou Acesso Metodo");
        return false;
    }

    private boolean If() {
        System.out.println("Começou If");
        boolean b;
        if (match("if")) {
            camada = 3;
            b = match("(");
            if (!b) {
                modoPaniquete();
            }
            expressaoLogica();
            b = match(")");
            if (!b) {
                modoPaniquete();
            }
            b = match("then");
            if (!b) {
                modoPaniquete();
            }
            b = match("{");
            if (!b) {
                modoPaniquete();
            }
            b = codigoIf();
            if (!b) {
                modoPaniquete();
            }
            b = match("}");
            if (!b) {
                modoPaniquete();
            }
            if (match("else")) {
                if (tokenAtual.getLexema().equals("if")) {
                    If();
                } else {
                    b = match("{");
                    if (!b) {
                        modoPaniquete();
                    }
                    b = codigoIf();
                    if (!b) {
                        modoPaniquete();
                    }
                    b = match("}");
                    if (!b) {
                        modoPaniquete();
                    }
                }
            }
            System.out.println("Terminou If");
            camada = 2;
            return true;
        }
        System.out.println("Terminou If");
        return false;
    }

    private void expressaoLogica() {
        System.out.println("Começou Expressao Logica");
        escopoExpIf();
        if (operadorLogico()) {
            expressaoLogica();
        }
        System.out.println("Terminou Expressao Logica");
    }

    private void escopoExpIf() {
        System.out.println("Começou Escopo Exp If");
        boolean b;
        if (match("(")) {
            b = opcoesExpLogica();
            if (!b) {
                modoPaniquete();
            }
            b = operadorRelacional();
            if (!b) {
                modoPaniquete();
            }
            b = opcoesExpLogica();
            if (!b) {
                modoPaniquete();
            }
            b = match(")");
            if (!b) {
                modoPaniquete();
            }
            System.out.println("Terminou Escopo Exp If");
        } else {
            b = opcoesExpLogica();
            if (!b) {
                modoPaniquete();
            }
            b = operadorRelacional();
            if (!b) {
                modoPaniquete();
            }
            b = opcoesExpLogica();
            if (!b) {
                modoPaniquete();
            }
            System.out.println("Terminou Escopo Exp If");
        }
    }

    private boolean opcoesExpLogica() {
        System.out.println("Começou Opcoes Exp Logica");
        if (match("Identificador")) {
            if (matriz()) {
                System.out.println("Terminou Opções Exp Logica");
                return true;
            }
            System.out.println("Terminou Opções Exp Logica");
            return true;
        } else if (Numero()) {
            System.out.println("Terminou Opções Exp Logica");
            return true;
        } else if (eBoolean()) {
            System.out.println("Terminou Opções Exp Logica");
            return true;
        }
        System.out.println("Terminou Opções Exp Logica");
        return false;
    }

    private boolean codigoIf() {
        System.out.println("Começou Codigo If");
        if (Read()) {
            System.out.println("Terminou Codigo If");
            codigoIf();
            return true;
        } else if (Write()) {
            System.out.println("Terminou Codigo If");
            codigoIf();
            return true;
        } else if (If()) {
            System.out.println("Terminou Codigo If");
            codigoIf();
            return true;
        } else if (atribuicao()) {
            System.out.println("Terminou Codigo If");
            codigoIf();
            return true;
        } else if (While()) {
            System.out.println("Terminou Codigo If");
            codigoIf();
            return true;
        }
        System.out.println("Terminou Codigo If");
        return false;
    }

    private boolean While() {
        boolean b;
        System.out.println("Começou While");
        if (match("while")) {
            camada = 3;
            b = match("(");
            if (!b) {
                modoPaniquete();
            }
            expressaoLogica();
            b = match(")");
            if (!b) {
                modoPaniquete();
            }
            b = match("{");
            if (!b) {
                modoPaniquete();
            }
            codigoIf();
            b = match("}");
            if (!b) {
                modoPaniquete();
            }
            System.out.println("Terminou While");
            camada = 2;
            return true;
        }
        System.out.println("Terminou While");
        return false;
    }

    private boolean Read() {
        System.out.println("Começou Read");
        if (match("read")) {
            boolean b;
            b = match("(");
            if (!b) {
                modoPaniquete();
            }
            b = parametrosRead();
            if (!b) {
                modoPaniquete();
            }
            b = match(")");
            if (!b) {
                modoPaniquete();
            }
            b = match(";");
            if (!b) {
                modoPaniquete();
            }
            System.out.println("Terminou Read");
            return true;
        }
        System.out.println("Terminou Read");
        return false;
    }

    private boolean parametrosRead() {
        System.out.println("Começou Parametros Read");
        if (opcoesRead()) {
            if (match(",")) {
                parametrosRead();
                System.out.println("Terminou Parametros Read");
                return true;
            }
            System.out.println("Terminou Parametros Read");
            return true;
        }
        System.out.println("Terminou Parametros Read");
        return false;
    }

    private boolean opcoesRead() {
        System.out.println("Começou Opções Read");
        if (match("Identificador")) {
            if (matriz()) {
                System.out.println("Terminou Opçoes Read");
                return true;
            }
            System.out.println("Terminou Opçoes Read");
            return true;
        } else if (acessoAtributo()) {
            System.out.println("Terminou Opçoes Read");
            return true;
        }
        System.out.println("Terminou Opçoes Read");
        return false;
    }

    private boolean Write() {
        System.out.println("Começou Write");
        boolean b;
        if (match("write")) {
            b = match("(");
            if (!b) {
                modoPaniquete();
            }
            conteudoWrite();
            b = match(")");
            if (!b) {
                modoPaniquete();
            }
            b = match(";");
            if (!b) {
                modoPaniquete();
            }
            System.out.println("Terminou Write");
            return true;
        }
        System.out.println("Terminou Write");
        return false;
    }

    private void conteudoWrite() {
        System.out.println("Começou Conteudo Write");
        boolean b;
        b = opcoesWrite();
        if (!b) {
            modoPaniquete();
        }
        if (match(",")) {
            conteudoWrite();
        }
        System.out.println("Terminou Conteudo Write");
    }

    private boolean opcoesWrite() {
        System.out.println("Começou Opçoes Write");
        if (!seguinte().getLexema().equals("[") && match("Identificador")) {
            System.out.println("Terminou Opçoes Write");
            return true;
        } else if (cadeiaCaracter()) {
            System.out.println("Terminou Opçoes Write");
            return true;
        }else if (acessoAtributo()) {
            System.out.println("Terminou Opçoes Write");
            return true;
        }
        return false;
    }

    private boolean cadeiaCaracter() {
        return match("CadeiaDeCaracteres");
    }

    private Token seguinte() {
        if (posicao + 1 < tokens.size()) {
            return tokens.get(posicao + 1);
        }
        return null;
    }

    private void modoPaniquete() {
        contErros++;
        msgErro = msgErro + "ERRO: " + " lexema Anterior: " + tokenAnterior.getLexema() + " | lexema Atual: " + tokenAtual.getLexema() + " | linha: " + tokenAtual.getLinha() + "\n";
        System.out.println("ERRO: " + " lexema Anterior: " + tokenAnterior.getLexema() + " | lexema Atual: " + tokenAtual.getLexema() + " | linha: " + tokenAtual.getLinha());
    }

    private void buscaSync() {
        if (tokenAtual.getLexema().equals(";") || tokenAtual.getLexema().equals(",") || tokenAtual.getLexema().equals("{") || tokenAtual.getLexema().equals("}") || tokenAtual.getLexema().equals("(") || tokenAtual.getLexema().equals(")") || tokenAtual.getLexema().equals("[") || tokenAtual.getLexema().equals("]") || tokenAtual.getLexema().equals("class") || tokenAtual.getLexema().equals("const") || tokenAtual.getLexema().equals("variables") || tokenAtual.getLexema().equals("if") || tokenAtual.getLexema().equals("method") || tokenAtual.getLexema().equals("return") || tokenAtual.getLexema().equals("main") || tokenAtual.getLexema().equals("else") || tokenAtual.getNome().equals("read") || tokenAtual.getLexema().equals("write") || tokenAtual.getLexema().equals("while") || tokenAtual.getLexema().equals("void") || tokenAtual.getLexema().equals("extends")) {
            return;
        } else if (tokenAtual.getLexema().equals("int") || tokenAtual.getLexema().equals("float") || tokenAtual.getLexema().equals("bool") || tokenAtual.getLexema().equals("string") && seguinte() != null && seguinte().getNome().equals(TipoToken.Nome.TokenIdentificador)) {
            return;
        } else {
            if (passaToken()) {
                buscaSync();
            }
        }
    }
}
