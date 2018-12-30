/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.ArrayList;
import javax.swing.JOptionPane;
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
    private Classes c;
    private Const consta;
    private String local;
    private ArvoreSemantica arvore;
    private int camada = 0; // 0 - antes de classes | 1 - dentro de classe | 2 - dentro de metodos | 3 - dentro de condicionais

    public Gramatica_V2(ArrayList tokens, ArvoreSemantica arvore) {
        contMain = 0;
        msgErro = "";
        linhasMain = "";
        contClass = 0;
        contErros = 0;
        naoTerminais = new ArrayList<NaoTerminal>();
        this.tokens = tokens;
        this.arvore = arvore;
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
        System.out.println("Terminou eBoolean");
        return match("true") || match("false");
    }

    private boolean eBoolean(Operacao op) {
        System.out.println("Comecou eBoolean");
        if (match("true") || match("false")) {
            op.addRecebe(tokenAnterior.getLexema());
            System.out.println("Terminou eBoolean");
            return true;
        }
        System.out.println("Terminou eBoolean");
        return false;
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
        if (match("++") || match("--") || match("+") || match("-") || match("*") || match("/")) {
            return true;
        }
        return false;
    }
    
    private boolean operadorAtitmetico(Operacao op) {
        System.out.println("Comecou OperadorAritmetico");
        if (match("++") || match("--") || match("+") || match("-") || match("*") || match("/")) {
            op.addRecebe(tokenAnterior.getLexema());
            return true;
        }
        return false;
    }

    private boolean operadorLogico() {
        System.out.println("Comecou OperadorLogico");
        return match("!") || match("&&") || match("||");
    }

    private boolean operadorRelacional() {
        System.out.println("Comecou OperadorRelacional");
        return match("!=") || match("==") || match("<") || match("<=") || match(">") || match(">=") || match("=");
    }

    private boolean atribuicao(Metodos d) {
        System.out.println("Comecou Atribuição");
        boolean b;
        Operacao op = new Operacao();
        if (tiposParametros()) {
            String tipoOP = tokenAnterior.getLexema();
            match("Identificador");
            String nomeVar = tokenAnterior.getLexema();
            d.addVariaveis(new Variaveis(tokenAnterior, tipoOP));
            op.setTipo(tipoOP);
            op.setVar(nomeVar);
            if (match("=")) {
                if (expressaoAritimetica(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    d.addOperacoes(op);
                    return true;
                } else if (valorInicializacao(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    d.addOperacoes(op);
                    return true;
                } else if (acessoAtributo(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    d.addOperacoes(op);
                    return true;
                } else {
                    modoPaniquete("ausencia de simbolo");
                }
            } else if (matriz(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                d.addOperacoes(op);
                return true;
            } else if (operadorAtitmetico(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                d.addOperacoes(op);
                return true;
            } else if (acessoAtributo(op)) {
                b = match("=");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                valorInicializacao(op);
                b = match(";");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                d.addOperacoes(op);
                return true;
            } else {
                modoPaniquete("simbolo mal escrito");
            }
        } else {
            match("Identificador");
            String nomeVar = tokenAnterior.getLexema();
            d.addVariaveis(new Variaveis(tokenAnterior, ""));
            op.setVar(nomeVar);
            if (match("=")) {
                if (acessoAtributo(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    d.addOperacoes(op);
                    return true;
                } else if (valorInicializacao(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    d.addOperacoes(op);
                    return true;
                } else if (expressaoAritimetica(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    d.addOperacoes(op);
                    return true;
                } else {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
            } else if (matriz(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                d.addOperacoes(op);
                return true;
            } else if (operadorAtitmetico(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                System.out.println("Terminou Atribuição");
                return true;
            } else if (acessoAtributo(op)) {
                b = match("=");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                b = valorInicializacao(op);
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                b = match(";");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                d.addOperacoes(op);
                return true;
            }
        }
        System.out.println("Terminou Atribuição");
        return false;
    }
   
     private boolean atribuicao(Operacao op) {
        System.out.println("Comecou Atribuição");
        boolean b;
        if (tiposParametros()) {
            String tipoOP = tokenAnterior.getLexema();
            match("Identificador");
            String nomeVar = tokenAnterior.getLexema();
            op.setTipo(tipoOP);
            op.setVar(nomeVar);
            if (match("=")) {
                if (expressaoAritimetica(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    return true;
                } else if (valorInicializacao(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    return true;
                } else if (acessoAtributo(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    return true;
                } else {
                    modoPaniquete("ausencia de simbolo");
                }
            } else if (matriz(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                return true;
            } else if (operadorAtitmetico(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                return true;
            } else if (acessoAtributo(op)) {
                b = match("=");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                valorInicializacao(op);
                b = match(";");
                if (!b) {
                    modoPaniquete("ausencia de simbolo");
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                return true;
            } else {
                modoPaniquete("simbolo mal escrito");
            }
        } else {
            match("Identificador");
            String nomeVar = tokenAnterior.getLexema();
            op.setVar(nomeVar);
            if (match("=")) {
                if (acessoAtributo(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete("ausencia de simbolo");
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    return true;
                } else if (valorInicializacao(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    return true;
                } else if (expressaoAritimetica(op)) {
                    b = match(";");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    System.out.println("Terminou Atribuição");
                    op.setLinha(tokenAnterior.getLinha() + "");
                    return true;
                } else {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
            } else if (matriz(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
                return true;
            } else if (operadorAtitmetico(op)) {
                b = match(";");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                System.out.println("Terminou Atribuição");
                return true;
            } else if (acessoAtributo(op)) {
                b = match("=");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                b = valorInicializacao(op);
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                b = match(";");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                System.out.println("Terminou Atribuição");
                op.setLinha(tokenAnterior.getLinha() + "");
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
        consta = null;
        boolean b;
        b = match("const");
        if (b) {
            consta = arvore.startConst();
            camada = 1;
            b = match("{");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            codigoConstante(consta);
            b = match("}");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            } else {
                camada = 0;
                System.out.println("Terminou Constante");
            }

        }
    }

    private void codigoConstante(Const consta) {
        System.out.println("Comecou Codigo Constante");
        boolean b;

        Operacao op = new Operacao();
        arvore.setAuxToken(tokenAtual);
        b = tiposPrimarios();
        String tipo = tokenAnterior.getLexema();
        camada = 2;
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
        } else {
            op.setTipo(tipo);
        }
        declaracaoConstante(op, consta);
        b = match(";");
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
        }
        if (tokenAtual.getLexema().equals("int") || tokenAtual.getLexema().equals("float") || tokenAtual.getLexema().equals("bool") || tokenAtual.getLexema().equals("string")) {
            codigoConstante(consta);
        }
        camada = 1;
        System.out.println("Terminou Codigo Constante");
    }

    private void declaracaoConstante(Operacao op) {

        System.out.println("Comecou Declaração Constante");
        boolean b;
        b = match("Identificador");
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
        }
        op.setLinha(tokenAnterior.getLinha() + "");
        op.setVar(tokenAnterior.getLexema());
        if (match("=")) {
            b = valorInicializacao(op);
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            } else {
                arvore.inserirOpConst(op);
            }
            if (match(",")) {
                Operacao op2 = new Operacao();
                //op2.setTipo1(op.getTipo1());
                declaracaoConstante(op2);
            }
        } else if (match(",")) {
            arvore.inserirOpConst(op);
            Operacao op2 = new Operacao();
            //op2.setTipo1(op.getTipo1());
            declaracaoConstante(op2);
        }

        System.out.println("Terminou Declaração Constante");
    }

    private boolean variaveis(Classes c) {
        System.out.println("Começou bloco Variaveis");
        if (match("variables")) {
            boolean b;
            camada = 2;
            b = match("{");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            codigoVariaveis(c);
            b = match("}");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            System.out.println("Terminou Variaveis");
            return true;

        }
        System.out.println("Terminou Variaveis");
        return false;
    }

    private void codigoVariaveis(Classes c) {
        System.out.println("Comecou Codigo Variaveis");
        boolean b;
        if (tiposPrimarios()) {
            String tipo = tokenAnterior.getLexema();
            declaracaoVariaveis(c, tipo);
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            if (!tokenAtual.getLexema().equals("}")) {
                codigoVariaveis(c);
            }
            System.out.println("Terminou Codigo Variaveis");
        } else if (match("Identificador")) {
            String tipo = tokenAnterior.getLexema();
            declaracaoVariaveis(c, tipo);
            if (!tokenAtual.getLexema().equals("}")) {
                codigoVariaveis(c);
            }
            System.out.println("Terminou Codigo Variaveis");
        } else {
            modoPaniquete(TipoErroSintatico.Erro.ExcessoSimb);
        }
    }

    private void declaracaoVariaveis(Classes c, String tipo) {
        System.out.println("Comecou declaração Variaveis");
        boolean b;
        if (tokenAtual.getNome().equals("Identificador")) {
            b = match("Identificador");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            Variaveis x = new Variaveis(tokenAnterior, tipo);
            c.addVariaveis(x);
            if (tokenAtual.getLexema().equals("=")) {
                b = match("=");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                b = valorInicializacao();
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                if (tokenAtual.getLexema().equals(",")) {
                    match(",");
                    declaracaoVariaveis(c, tipo);
                }
            } else if (tokenAtual.getLexema().equals(",")) {
                match(",");
                declaracaoVariaveis(c, tipo);
                System.out.println("Terminou Declaraçao Variaveis");
            } else if (matriz()) {
                if (tokenAtual.getLexema().equals("=")) {
                    match("=");
                    if (tokenAtual.getLexema().equals("{")) {
                        inicializaMatriz();
                        if (tokenAtual.getLexema().equals(",")) {
                            match(",");
                            declaracaoVariaveis(c, tipo);
                        }
                    } else {
                        chamadaMetodo();
                        if (tokenAtual.getLexema().equals(",")) {
                            match(",");
                            declaracaoVariaveis(c, tipo);
                        }
                    }
                }
                System.out.println("Terminou Declaração Variaveis");
            }
        }
    }

    private boolean valorInicializacao() {
        boolean b;
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
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            System.out.println("Terminou Valor Inicialização");
            return true;
        }
        System.out.println("Terminou Valor Inicialização");
        return false;
    }

    private boolean valorInicializacao(Operacao op) {
        boolean b;
        System.out.println("Comecou Valor Inicialização");
        if (acessoMetodo(op)) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (eBoolean(op)) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (cadeiaCaracter(op)) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (expressaoAritimetica(op)) {
            System.out.println("Terminou Valor Inicialização");
            return true;
        } else if (acessoAtributo(op)) {
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            System.out.println("Terminou Valor Inicialização");
            return true;
        }
        System.out.println("Terminou Valor Inicialização");
        return false;
    }

    private boolean variaveis(Metodos d) {
        System.out.println("Começou bloco Variaveis");
        if (match("variables")) {
            boolean b;
            camada = 2;
            b = match("{");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            codigoVariaveis(d);
            b = match("}");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            System.out.println("Terminou Variaveis");
            return true;

        }
        System.out.println("Terminou Variaveis");
        return false;
    }

    private void codigoVariaveis(Metodos d) {
        System.out.println("Comecou Codigo Variaveis");
        boolean b;
        if (tiposPrimarios()) {
            String tipo = tokenAnterior.getLexema();
            declaracaoVariaveis(d, tipo);
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            if (!tokenAtual.getLexema().equals("}")) {
                codigoVariaveis(d);
            }
            System.out.println("Terminou Codigo Variaveis");
        } else if (match("Identificador")) {
            String tipo = tokenAnterior.getLexema();
            declaracaoVariaveis(d, tipo);
            if (!tokenAtual.getLexema().equals("}")) {
                codigoVariaveis(d);
            }
            System.out.println("Terminou Codigo Variaveis");
        } else {
            modoPaniquete(TipoErroSintatico.Erro.ExcessoSimb);
        }
    }

    private void declaracaoVariaveis(Metodos d, String tipo) {
        System.out.println("Comecou declaração Variaveis");
        boolean b;
        if (tokenAtual.getNome().equals("Identificador")) {
            b = match("Identificador");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            Variaveis x = new Variaveis(tokenAnterior, tipo);
            d.addVariaveis(x);
            if (tokenAtual.getLexema().equals("=")) {
                b = match("=");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                b = valorInicializacao();
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                if (tokenAtual.getLexema().equals(",")) {
                    match(",");
                    declaracaoVariaveis(d, tipo);
                }
            } else if (tokenAtual.getLexema().equals(",")) {
                match(",");
                declaracaoVariaveis(d, tipo);
                System.out.println("Terminou Declaraçao Variaveis");
            } else if (matriz()) {
                if (tokenAtual.getLexema().equals("=")) {
                    match("=");
                    if (tokenAtual.getLexema().equals("{")) {
                        inicializaMatriz();
                        if (tokenAtual.getLexema().equals(",")) {
                            match(",");
                            declaracaoVariaveis(d, tipo);
                        }
                    } else {
                        chamadaMetodo();
                        if (tokenAtual.getLexema().equals(",")) {
                            match(",");
                            declaracaoVariaveis(d, tipo);
                        }
                    }
                }
                System.out.println("Terminou Declaração Variaveis");
            }
        }
    }

    private void classe() {
        System.out.println("Começou bloco Classe");
        boolean b;
        
        b = match("class");
        if (b) {
            contClass++;
            b = match("Identificador");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            } else {
                c = arvore.addClasse(tokenAnterior.getLexema());
                c.setLinha(tokenAnterior.getLinha() + "");
                local = "c";
            }
            if (tokenAtual.getLexema().equals("extends")) {
                b = match("extends");
                if (b) {
                    b = match("Identificador");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                    } else {
                        if (c != null) {
                            c.setExtend(tokenAnterior.getLexema());
                        }
                        //arvore.addExtendsClasse(tokenAnterior.getLexema());
                    }
                }
            }
            b = match("{");
            camada = 1;
            if (b) {
                codigoClasse(c);
                System.out.println(tokenAtual.getLexema());
                b = match("}");
                System.out.println(tokenAtual.getLexema());
                System.out.println(b);
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                } else {
                    camada = 0;
                }
            }
            if (tokenAtual.getLexema().equals("class")) {
                classe();

            } else if (posicao == tokens.size()) {
                modoPaniquete(TipoErroSintatico.Erro.ExcessoSimb);
                if (tokenAtual.getLexema().equals("class")) {
                    classe();
                }
            }
//            } else {
//                modoPaniquete();
//            }
        }
        camada = 0;
        System.out.println("Terminou Classe");

    }

    private void codigoClasse(Classes c) {
        System.out.println("Comecou codigo Classe");
        boolean b;
        if (variaveis(c)) {
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse(c);
            }
        } else if (metodo(c)) {
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse(c);
            }
        } else if (chamadaMetodo()) {
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse(c);
            }
        } else if (acessoAtributo()) {
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            if (!tokenAtual.getLexema().equals("}")) {
                codigoClasse(c);
            }
        } else if (!tokenAtual.getLexema().equals("}")) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
        }
        System.out.println("Terminou Codigo Classe");
    }

    private boolean metodo(Classes c) {
        boolean b;
        System.out.println("Começou Metodo");
        if (tokenAtual.getLexema().equals("method")) {
            camada = 2;
            b = match("method");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = tipoRetorno();
            String tipo = tokenAnterior.getLexema();
            Metodos d = new Metodos(tipo);
            d.setLinha(tokenAnterior.getLinha()+"");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            if (tokenAtual.getLexema().equals("main")) {
                match("main");
                d.setNome(tokenAnterior.getLexema());
                System.out.println("Achou uma main");
                contMain++;
            } else {
                b = match("Identificador");
                d.setNome(tokenAnterior.getLexema());
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
            }
            b = match("(");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            String x = "";
            d.addParametro(parametrosMetodo(x));
            b = match(")");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = match("{");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            codigoMetodo(d);
            if (tokenAtual.getLexema().equals("return")) {
                System.out.println("Entrou aqui");
                match("return");
                retorno();
                d.addRetorno(tokenAnterior.getLexema());
                b = match(";");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
            }
            b = match("}");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            c.addMetodos(d);
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

    private String parametrosMetodo(String x) {
        System.out.println("Começou Parametros Metodo");
        boolean b;

        b = tiposParametros();
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
        }
        x += tokenAnterior.getLexema();
        if (match("Identificador")) {
            if (match(",")) {
                x += tokenAnterior.getLexema();
                parametrosMetodo(x);
            } else if (matriz()) {
                if (match(",")) {
                    x += tokenAnterior.getLexema();
                    parametrosMetodo(x);
                }
            }
        }
        System.out.println("Terminou Parametros Metodo");
        return x;
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

    private void codigoMetodo(Metodos d) {
        boolean b;
        System.out.println("Começou Codigo Metodo");
        if (variaveis(d)) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
        } else if (atribuicao(d)) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
        } else if (While()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
        } else if (Read()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
        } else if (Write()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
        } else if (If()) {
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
        } else if (chamadaMetodo()) {

            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
        } else if (acessoAtributo()) {
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            System.out.println("Terminou Codigo Metodo");
            codigoMetodo(d);
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
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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

    private boolean matriz(Operacao op) {
        System.out.println("Comecou Matriz");
        boolean b;
        if (match("[")) {
            indiceMatriz(op);
            b = match("]");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            if (tokenAtual.getLexema().equals("[")) {
                matriz(op);
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

    private boolean indiceMatriz(Operacao op) {
        System.out.println("Comecou Indice Matriz");
        if (Numero()) {
            op.addIndiceMatriz(tokenAnterior.getLexema());
            System.out.println("Terminou Tndece Matriz");
            return true;
        } else if (match("Identificador")) {
            String k = tokenAnterior.getLexema();
            if (operadorAtitmetico(op)) {
                k += tokenAnterior.getLexema();
                op.addIndiceMatriz(k);
                System.out.println("Terminou Indice Matriz");
                return true;
            }
            op.addIndiceMatriz(tokenAnterior.getLexema());
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
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
        }
        linhasMatriz();
        b = match("}");
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
        }
        System.out.println("Terminou Inicializa Matriz");
    }

    private void linhasMatriz() {
        System.out.println("Comecou Linhas Matriz");
        boolean b;
        b = match("(");
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
        }
        b = colunasMatriz();
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
        }
        b = match(")");
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            System.out.println("Terminou Chamada Metodo");
            return true;
        }
        System.out.println("Terminou Chamada Metodo");
        return false;
    }

    private boolean chamadaMetodoConstante(Operacao op) {
        System.out.println("Começou Chamda Metosdo");
        String a = "";
        String l = "";
        if (match("Identificador")) {
            a = tokenAnterior.getLexema();
            if (match(".")) {
                l = chamadaMetodobase();
            } else {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            System.out.println("Terminou Chamada Metodo");
            AcessoMetodo e = new AcessoMetodo(a, l, tokenAnterior.getLinha() + "");
            int posi = arvore.addAcessoMetodoConst(e);
            op.setAcessoMetodoId(posi + "");
            return true;
        }
        System.out.println("Terminou Chamada Metodo");
        return false;
    }

    private String chamadaMetodobase() {
        System.out.println("Começou Chamada Metodo Base");
        boolean b;
        String x = "";
        b = match("Identificador");
        x = tokenAnterior.getLexema();
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
        }
        b = match("(");
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
        }
        argumentosMetodo();
        b = match(")");
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
        }
        System.out.println("Terminou Chamada Metodo Base");
        return x;
    }

    private void argumentosMetodo() {
        System.out.println("Começou Argumentos Metodos");
        boolean b;
        b = valoresArgumentos();
        if (!b) {
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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

    private boolean expressaoAritimetica(Operacao op) {
        System.out.println("Comecou Expressao Aritmetica");
        if (multExpr(op)) {
            if (match("+") || match("-")) {
                op.addRecebe(tokenAnterior.getLexema());
                expressaoAritimetica(op);
                System.out.println("Terminou Expresssao Aritmetica");
                return true;
            }
            System.out.println("Terminou Expresssao Aritmetica");
            return true;
        }
        System.out.println("Terminou Expresssao Aritmetica");
        return false;
    }

    private boolean expressaoAritimeticaConstante(Operacao op) {
        System.out.println("Comecou Expressao Aritmetica");
        if (multExpr(op)) {
            if (match("+") || match("-")) {
                op.addRecebe(tokenAnterior.getLexema());
                expressaoAritimetica(op);
                System.out.println("Terminou Expresssao Aritmetica");
                op.addRecebe(tokenAnterior.getLexema());
                return true;
            }
            System.out.println("Terminou Expresssao Aritmetica");
            op.addRecebe(tokenAnterior.getLexema());
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

    private boolean multExpr(Operacao op) {
        System.out.println("Comecou MultExpr");
        if (negExpr(op)) {
            if (match("*") || match("/")) {
                op.addRecebe(tokenAnterior.getLexema());
                multExpr(op);
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

    private boolean negExpr(Operacao op) {
        System.out.println("Comecou NegExpr");
        if (match("-")) {
            op.addRecebe(tokenAnterior.getLexema());
            valorExpr(op);
            System.out.println("Terminou NegExpt");
            return true;
        } else {
            valorExpr(op);
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

    //VER MATRIZ
    private boolean valorExpr(Operacao op) {
        System.out.println("Comecou ValorExpr");
        if (Numero()) {
            op.addRecebe(tokenAnterior.getLexema());
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (match("Identificador")) {
            if (matriz()) {
                System.out.println("Terminou ValorExpt");
                return true;
            }
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (chamadaMetodoConstante(op)) {
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (acessoAtributo(op)) {
            System.out.println("Terminou ValorExpt");
            return true;
        } else if (match("(")) {
            expressaoAritimetica(op);
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
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                System.out.println("Terminou acesso Atributo");
                return true;
            } else if (matriz()) {
                b = match(".");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                b = match("Identificador");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                System.out.println("Terminou acesso Atributo");
                return true;
            } else {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            System.out.println("Terminou acesso Atributo");
            return true;
        } else if (match(".")) {
            b = match("Identificador");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            System.out.println("Terminou acesso Atributo");
            return true;
        }
        System.out.println("Terminou acesso Atributo");
        return false;
    }

    //VER MATRIZ
    private boolean acessoAtributo(Operacao op) {
        System.out.println("Começou acesso Atributo");
        boolean b;
        String a = "";
        String l = "";
        if (match("Identificador")) {
            a = tokenAnterior.getLexema();
            if (match(".")) {
                b = match("Identificador");
                l = tokenAnterior.getLexema();
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                System.out.println("Terminou acesso Atributo");
                AcessoMetodo e = new AcessoMetodo(a, l, tokenAnterior.getLinha() + "");
                int posi = arvore.addAcessoMetodoConst(e);
                op.setAcessoMetodoId(posi + "");
                return true;
            } else if (matriz()) {
                b = match(".");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                b = match("Identificador");
                l = tokenAnterior.getLexema();
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                System.out.println("Terminou acesso Atributo");
                AcessoMetodo e = new AcessoMetodo(a, l, tokenAnterior.getLinha() + "");
                int posi = arvore.addAcessoMetodoConst(e);
                op.setAcessoMetodoId(posi + "");
                return true;
            } else {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            System.out.println("Terminou acesso Atributo");
            return true;
        } else if (match(".")) {
            b = match("Identificador");
            l = tokenAnterior.getLexema();
            AcessoMetodo e = new AcessoMetodo(a, l, tokenAnterior.getLinha() + "");
            int posi = arvore.addAcessoMetodoConst(e);
            op.setAcessoMetodoId(posi + "");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            System.out.println("Terminou acesso Atributo");
            return true;
        }
        System.out.println("Terminou acesso Atributo");
        return false;
    }

    private boolean acessoMetodoConstante(Operacao op) {
        System.out.println("Começou Acesso Metodo");
        boolean b;
        String a = "";
        String l = "";
        if (match("Identificador")) {
            a = tokenAnterior.getLexema();
            if (match(".")) {
                b = match("Identificador");
                l = tokenAnterior.getLexema();
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                b = match("(");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                if (parametrosRead()) {
                    b = match(")");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    System.out.println("Terminou Acesso Metodo");
                    return true;
                }
                b = match(")");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }

                System.out.println("Terminou Acesso Metodo");
                return true;
            }
        }
        AcessoMetodo e = new AcessoMetodo(a, l, tokenAnterior.getLinha() + "");
        int posi = arvore.addAcessoMetodoConst(e);
        op.setAcessoMetodoId(posi + "");
        System.out.println("Terminou Acesso Metodo");
        return false;
    }

    private boolean acessoMetodo() {
        System.out.println("Começou Acesso Metodo");
        boolean b;
        String a = "";
        String l = "";
        if (match("Identificador")) {
            if (match(".")) {
                b = match("Identificador");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                b = match("(");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                if (parametrosRead()) {
                    b = match(")");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    System.out.println("Terminou Acesso Metodo");
                    return true;
                }
                b = match(")");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                System.out.println("Terminou Acesso Metodo");
                return true;
            }
        }
        System.out.println("Terminou Acesso Metodo");
        return false;
    }
    
    private boolean acessoMetodo(Operacao op) {
        System.out.println("Começou Acesso Metodo");
        boolean b;
        String a = "";
        String l = "";
        if (match("Identificador")) {
            if (match(".")) {
                b = match("Identificador");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                }
                b = match("(");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                }
                if (parametrosRead()) {
                    b = match(")");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    System.out.println("Terminou Acesso Metodo");
                    return true;
                }
                b = match(")");
                if (!b) {
                    modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            expressaoLogica();
            b = match(")");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = match("then");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = match("{");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = codigoIf();
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            b = match("}");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            if (match("else")) {
                if (tokenAtual.getLexema().equals("if")) {
                    If();
                } else {
                    b = match("{");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                    b = codigoIf();
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
                    }
                    b = match("}");
                    if (!b) {
                        modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
                    }
                }
            }
            System.out.println("Terminou If");
            camada = 2;
            return true;
        }
        System.out.println("Terminou If");
        camada = 2;
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
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            b = operadorRelacional();
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            b = opcoesExpLogica();
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            b = match(")");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            System.out.println("Terminou Escopo Exp If");
        } else {
            b = opcoesExpLogica();
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            b = operadorRelacional();
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
            }
            b = opcoesExpLogica();
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.SimbMalEscrito);
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
//        System.out.println("Começou Codigo If");
//        if (Read()) {
//            System.out.println("Terminou Codigo If");
//            codigoIf();
//            return true;
//        } else if (Write()) {
//            System.out.println("Terminou Codigo If");
//            codigoIf();
//            return true;
//        } else if (If()) {
//            System.out.println("Terminou Codigo If");
//            codigoIf();
//            return true;
//        } else if (atribuicao()) {
//            System.out.println("Terminou Codigo If");
//            codigoIf();
//            return true;
//        } else if (While()) {
//            System.out.println("Terminou Codigo If");
//            codigoIf();
//            return true;
//        }
//        System.out.println("Terminou Codigo If");
        return false;
    }
    
    private boolean codigoIf(Operacao op) {
        System.out.println("Começou Codigo If");
        if (Read()) {
            System.out.println("Terminou Codigo If");
            codigoIf(op);
            return true;
        } else if (Write()) {
            System.out.println("Terminou Codigo If");
            codigoIf(op);
            return true;
        } else if (If()) {
            System.out.println("Terminou Codigo If");
            codigoIf(op);
            return true;
        } else if (atribuicao(op)) {
            System.out.println("Terminou Codigo If");
            codigoIf(op);
            return true;
        } else if (While()) {
            System.out.println("Terminou Codigo If");
            codigoIf(op);
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
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            expressaoLogica();
            b = match(")");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = match("{");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            codigoIf();
            b = match("}");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = parametrosRead();
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = match(")");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            conteudoWrite();
            b = match(")");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
            }
            b = match(";");
            if (!b) {
                modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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
            modoPaniquete(TipoErroSintatico.Erro.AusenciaSimb);
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
        } else if (acessoAtributo()) {
            System.out.println("Terminou Opçoes Write");
            return true;
        }
        return false;
    }

    private boolean cadeiaCaracter() {
        System.out.println("Começou CadeiaCaracter");
        System.out.println("Terminou CadeiaCaracter");
        return match("CadeiaDeCaracteres");
    }

    private boolean cadeiaCaracter(Operacao op) {
        System.out.println("Começou CadeiaCaracter");
        if (match("CadeiaDeCaracteres")) {
            op.addRecebe(tokenAnterior.getLexema());
            System.out.println("Terminou CadeiaCaracter");
            return true;
        }
        System.out.println("Terminou CadeiaCaracter");
        return false;
    }

    private Token seguinte() {
        if (posicao + 1 < tokens.size()) {
            return tokens.get(posicao + 1);
        }
        return null;
    }

    private void modoPaniquete(String tipo) {
        contErros++;
        msgErro = msgErro + "ERRO: " + " lexema Anterior: " + tokenAnterior.getLexema() + " | lexema Atual: " + tokenAtual.getLexema() + " | linha: " + tokenAtual.getLinha() + " | tipo: " + tipo + "\n";
        System.out.println("ERRO: " + " lexema Anterior: " + tokenAnterior.getLexema() + " | lexema Atual: " + tokenAtual.getLexema() + " | linha: " + tokenAtual.getLinha());
        //passaToken();
        buscaSync();
        //if (match(";")) {
        switch (camada) {
            case 0:
                break;
            case 1:
//                codigoClasse();
//                if (match("}")) {
//                    classe();
//                }
                break;
            case 2:
//                codigoMetodo();
//                if (match("}")) {
//                    metodo();
//                } else {
//                    passaToken();
//                    modoPaniquete();
//                }
                break;
            case 3:
//                //atribuicao();
//                //codigoIf();
//                if (match("}")) {
//                    //  codigoIf();
//                }
                break;
            default:
                modoPaniquete(TipoErroSintatico.Erro.ExcessoSimb);
                break;
        }
//        }else if(match("{")){
//            if(camada == 0){
//                
//            }else if (camada == 1) {
//                variavelConstanteObjeto();
//                metodo();
//                if (match("}")) {
//                    classe();
//                }
//            } else if (nivel == 2) {
//                program();
//                if (aceitarToken("}")) {
//                    metodo();
//                } else {
//                    panicMode();
//                }
//            } else if (nivel == 3) {
//                program();
//                if (aceitarToken("}")) {
//                    program();
//                }
//            } else {
//                proximoToken();
//                panicMode();
//            }
//        }
    }

    private void buscaSync() {
        if (tokenAtual.getLexema().equals(";") || tokenAtual.getLexema().equals("{") || tokenAtual.getLexema().equals("}") || tokenAtual.getLexema().equals("class") || tokenAtual.getLexema().equals("variables") || tokenAtual.getLexema().equals("if") || tokenAtual.getLexema().equals("method") || tokenAtual.getLexema().equals("else") || tokenAtual.getNome().equals("read") || tokenAtual.getLexema().equals("write") || tokenAtual.getLexema().equals("while")) {
        } else if (tokenAtual.getLexema().equals("int") || tokenAtual.getLexema().equals("float") || tokenAtual.getLexema().equals("bool") || tokenAtual.getLexema().equals("string") && seguinte() != null && seguinte().getNome().equals(TipoToken.Nome.TokenIdentificador)) {
        } else {
            if (passaToken()) {
                buscaSync();
            }
        }
    }
}
