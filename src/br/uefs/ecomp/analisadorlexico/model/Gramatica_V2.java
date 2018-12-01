/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.ArrayList;

/**
 *
 * @author Aleatorio
 */
public class Gramatica_V2 {

    private Token tokenAtual, tokenAnterior;
    private int posicao = -1;
    private ArrayList<Token> tokens;
    private ArrayList naoTerminais;

    public Gramatica_V2(ArrayList tokens) {
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
            System.out.println("Acertou - (Nome) " + tokenAnterior.getNome() + " | (Lexema)  " + tokenAnterior.getLexema() + " | Passado  " + tipo);
            return true;
        }
        System.out.println("Deu Erro - (Nome)  " + tokenAtual.getNome() + " | (Lexema)  " + tokenAtual.getLexema() + " | Passado  " + tipo);
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
        if (match("Identificador")) {
            if (match("=")) {
                if (acessoAtributo()) {
                    match(";");
                    System.out.println("Terminou Atribuição");
                    return true;
                } else if (valorInicializacao()) {
                    match(";");
                    System.out.println("Terminou Atribuição");
                    return true;
                }
            } else if (matriz()) {
                match(";");
                System.out.println("Terminou Atribuição");
                return true;
            } else if (operadorAtitmetico()) {
                match(";");
                System.out.println("Terminou Atribuição");
                return true;
            } else if (acessoAtributo()) {
                match("=");
                valorInicializacao();
                match(";");
                System.out.println("Terminou Atribuição");
                return true;
            }
        }
        System.out.println("Terminou Atribuição");
        return false;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void start() {
        System.out.println("startou");
        passaToken();
        constante();
        classe();
        System.out.println("Deu certo");
    }

    private void constante() {
        System.out.println("Começou bloco Constante");
        match("const");
        match("{");
        codigoConstante();
        match("}");
        System.out.println("Terminou Constante");
    }

    private void codigoConstante() {
        System.out.println("Comecou Codigo Constante");
        tiposPrimarios();
        declaracaoConstante();
        match(";");
        if (tokenAtual.getLexema().equals("int") || tokenAtual.getLexema().equals("float") || tokenAtual.getLexema().equals("bool") || tokenAtual.getLexema().equals("string")) {
            codigoConstante();
        }
        System.out.println("Terminou Codigo Constante");
    }

    private void declaracaoConstante() {
        System.out.println("Comecou Declaração Constante");
        match("Identificador");
        if (match("=")) {
            valorInicializacao();
            if (match(",")) {
                declaracaoConstante();
            }
        } else if (match(",")) {
            declaracaoConstante();
        }
        System.out.println("Terminou Declaração Constante");
    }

    private boolean variaveis() {
        System.out.println("Começou bloco Variaveis");
        if (match("variables")) {
            match("{");
            codigoVariaveis();
            match("}");
            System.out.println("Terminou Variaveis");
            return true;
        }
        System.out.println("Terminou Variaveis");
        return false;
    }

    private void codigoVariaveis() {
        System.out.println("Comecou Codigo Variaveis");
        if (tiposPrimarios()) {
            declaracaoVariaveis();
            match(";");
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
        }
    }

    private void declaracaoVariaveis() {
        System.out.println("Comecou declaração Variaveis");
        if (tokenAtual.getNome().equals("Identificador")) {
            match("Identificador");
            if (tokenAtual.getLexema().equals("=")) {
                match("=");
                valorInicializacao();
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
        if (expressaoAritimetica()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (eBoolean()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (cadeiaCaracter()) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        }
        System.out.println("Terminou Valor Inicialização");
        return false;
    }

    private void classe() {
        System.out.println("Começou bloco Classe");
        match("class");
        match("Identificador");
        if (tokenAtual.getLexema().equals("extends")) {
            match("extends");
            match("Identificador");
        }
        match("{");
        codigoClasse();
        match("}");
        if (tokenAtual.getLexema().equals("class")) {
            classe();
        }
        System.out.println("Terminou Classe");
    }

    private void codigoClasse() {
        System.out.println("Comecou codigo Classe");
        if (variaveis()) {
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        } else if (metodo()) {
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        } else if (chamadaMetodo()) {
            match(";");
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        } else if (acessoAtributo()) {
            match(";");
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse();
            }
        }
        System.out.println("Terminou Codigo Classe");
    }

    private boolean metodo() {
        if (tokenAtual.getLexema().equals("method")) {
            match("method");
            tipoRetorno();
            match("Identificador");
            match("(");
            parametrosMetodo();
            match(")");
            match("{");
            codigoMetodo();
            if (tokenAtual.getLexema().equals("reutrn")) {
                match("return");
                retorno();
                match(";");
            }
            match("}");
        }
        return false;
    }

    private boolean tipoRetorno() {
        if (tiposPrimarios()) {
            return true;
        } else if (match("void")) {
            return true;
        } else if (match("Identificador")) {
            return true;
        }
        return false;
    }

    private void parametrosMetodo() {
        tiposParametros();
        if (match("Identificador")) {
            if (match(",")) {
                parametrosMetodo();
            } else if (matriz()) {
                if (match(",")) {
                    parametrosMetodo();
                }
            }
        }
    }

    private boolean tiposParametros() {
        if (tiposPrimarios()) {
            return true;
        } else if (match("Identificador")) {
            return true;
        }
        return false;
    }

    private void codigoMetodo() {
        if (variaveis()) {
            codigoMetodo();
        } else if (If()) {
            codigoMetodo();
        } else if (While()) {
            codigoMetodo();
        } else if (Read()) {
            codigoMetodo();
        } else if (Write()) {
            codigoMetodo();
        } else if (atribuicao()) {
            codigoMetodo();
        } else if (chamadaMetodo()) {
            match(";");
            codigoMetodo();
        }
    }

    private boolean retorno() {
        if (match("(")) {
            if (expressaoAritimetica()) {
                return true;
            } else if (eBoolean()) {
                return true;
            }
            match(")");
        } else {
            if (expressaoAritimetica()) {
                return true;
            } else if (eBoolean()) {
                return true;
            }
        }
        return false;
    }

    private boolean matriz() {
        System.out.println("Comecou Matriz");
        if (match("[")) {
            indiceMatriz();
            match("]");
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
        match("{");
        linhasMatriz();
        match("}");
        System.out.println("Terminou Inicializa Matriz");
    }

    private void linhasMatriz() {
        System.out.println("Comecou Linhas Matriz");
        match("(");
        colunasMatriz();
        match(")");
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
        if (match("Identificador")) {
            if (match(".")) {
                chamadaMetodobase();
            }
            return true;
        }
        return false;
    }

    private void chamadaMetodobase() {
        match("Identificador");
        match("(");
        argumentosMetodo();
        match(")");
    }

    private void argumentosMetodo() {
        valoresArgumentos();
        if (match(",")) {
            argumentosMetodo();
        }
    }

    private boolean valoresArgumentos() {
        if (Numero()) {
            return true;
        } else if (eBoolean()) {
            return true;
        } else if (chamadaMetodo()) {
            return true;
        } else if (match("Identificador")) {
            if (matriz()) {
                return true;
            }
            return true;
        } else if (acessoAtributo()) {
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
        System.out.println("COmecou MultExpr");
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
        if (match("Identificador")) {
            if (match(".")) {
                match("Identificador");
                return true;
            } else if (matriz()) {
                match(".");
                match("Identificador");
                return true;
            }
        }
        return false;
    }

    private boolean If() {
        if (match("if")) {
            match("(");
            expressaoLogica();
            match(")");
            match("then");
            match("{");
            codigoIf();
            match("}");
            if (match("else")) {
                match("{");
                codigoIf();
                match("}");
                if (tokenAtual.getLexema().equals("if")) {
                    If();
                }
            }
            return true;
        }
        return false;
    }

    private void expressaoLogica() {
        escopoExpIf();
        if (operadorLogico()) {
            expressaoLogica();
        }
    }

    private void escopoExpIf() {
        if (match("(")) {
            opcoesExpLogica();
            operadorRelacional();
            opcoesExpLogica();
            match(")");
        } else {
            opcoesExpLogica();
            operadorRelacional();
            opcoesExpLogica();
        }
    }

    private boolean opcoesExpLogica() {
        if (match("Identificador")) {
            if (matriz()) {
                return true;
            }
            return true;
        } else if (Numero()) {
            return true;
        } else if (eBoolean()) {
            return true;
        }
        return false;
    }

    private boolean codigoIf() {
        if (Read()) {
            return true;
        } else if (Write()) {
            return true;
        } else if (If()) {
            return true;
        } else if (atribuicao()) {
            return true;
        } else if (While()) {
            return true;
        }
        return false;
    }

    private boolean While() {
        if (match("while")) {
            match("(");
            expressaoLogica();
            match(")");
            match("{");
            codigoIf();
            match("}");
            return true;
        }
        return false;
    }

    private boolean Read() {
        if (match("read")) {
            match("(");
            parametrosRead();
            match(")");
            match(";");
            return true;
        }
        return false;
    }

    private void parametrosRead() {
        opcoesRead();
        if (match(",")) {
            parametrosRead();
        }
    }

    private boolean opcoesRead() {
        if (match("Identificador")) {
            if (matriz()) {
                return true;
            }
            return true;
        } else if (acessoAtributo()) {
            return true;
        }
        return false;
    }

    private boolean Write() {
        if (match("write")) {
            match("(");
            conteudoWrite();
            match(")");
            match(";");
            return true;
        }
        return false;
    }

    private void conteudoWrite() {
        opcoesWrite();
        if (match(",")) {
            conteudoWrite();
        }
    }

    private boolean opcoesWrite() {
        if (match("Identificador")) {
            if (matriz()) {
                return true;
            }
            return true;
        } else if (acessoAtributo()) {
            return true;
        } else if (cadeiaCaracter()) {
            return true;
        }
        return false;
    }

    private boolean cadeiaCaracter() {
        System.out.println("Cadeia Caracter");
        return false;
    }

}
