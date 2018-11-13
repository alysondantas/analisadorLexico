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
    
    private String grama = "<Program>                     ::= <Class> <Program> | <Constants> <Program> |\n" +
"<Expr Arit>                   ::= <Mult Exp> '+' <Expr Arit> | <Mult Exp> '-' <Expr Arit> | <Mult Exp>\n" +
"<Mult Exp>                    ::= <Negate Exp> '*' <Mult Exp> | <Negate Exp> '/' <Mult Exp> | <Negate Exp>\n" +
"<Expression>                  ::= '(' <Expr Arit> ')' | <Expr Arit>\n" +
"<Minus>                       ::= '-' |\n" +
"<Negate Exp>                  ::= <Minus> <Expression> | <Minus> <Initial Value>\n" +
"<Relational Operator>         ::= '!=' | '==' | '<' | '<=' | '>' | '>=' | '='\n" +
"<Logic Operator>              ::= '&&' | '||'\n" +
"<Negate>                      ::= '!' <Negate> |\n" +
"<Logic Expression>            ::= <Initial Value> <Logic Operator> <Initial Value> | <Initial Value>\n" +
"<Negate Logic>                ::= <Negate> <Logic Expression>\n" +
"<Relational Logic>            ::= <Relational Operator> <Expression> <Relational Logic> | <Logic Operator> <Condition Expression> |\n" +
"<Condition Expression>        ::= <Negate Logic> <Relational Logic> | '(' <Condition Expression> ')' <Relational Logic>\n" +
"<Value>                       ::= 'true' | 'false' | StringLiteral | NumberTerminal\n" +
"<Array Position>              ::= <Expression> |\n" +
"<Array>                       ::= '[' <Array Position> ']' <Array>  |\n" +
"<Init Array>                  ::= '{' <Init Array_2> '}'\n" +
"<Init Array_2>                ::= '(' <Init Array_3> ')' |  '(' <Init Array_3> ')' <Init Array_2>\n" +
"<Init Array_3>                ::= <Initial Value> | <Initial Value> ',' <Init Array_3>\n" +
"<Declaration>                 ::= <Type> <Valid Identifier>\n" +
"<Valid Identifier>            ::= Identifier <Array>\n" +
"<Type>                        ::= 'string' | 'int' | 'float' | 'bool' | 'void' | Identifier\n" +
"<Attribute Access>            ::= '.' <Valid Identifier> <Attribute Access> |\n" +
"<Valid Values>                ::= <Value> <Increment-Decrement> | <Valid Identifier> <Attribute Access>\n" +
"<Initial Value>               ::= <Init Array> | <Valid Values> <Increment-Decrement> | <Method Call>\n" +
"<Increment-Decrement>         ::= '++' | '--' |\n" +
"<Method Call>                 ::= <Valid Identifier> <Attribute Access> '(' <Arguments> ')'\n" +
"<Multiple Identifier>         ::= ',' <Valid Identifier> <Multiple Identifier> |\n" +
"<Initialize Constant>         ::= <Multiple Identifier> '=' <Expression> <Initialize Constant> |\n" +
"<Constant Assignment>         ::= <Declaration> '=' <Expression> <Initialize Constant> ';' <Constant Assignment> |\n" +
"<Initialize>                  ::= '=' <Expression> |\n" +
"<Initialize Variable>         ::= <Multiple Identifier> <Initialize>\n" +
"<Variable Assignment>         ::=  <Declaration> <Initialize> <Initialize Variable> ';' <Variable Assignment> |\n" +
"<Constants>                   ::= 'const' '{' <Constant Assignment> '}'\n" +
"<Variables>                   ::= 'variables' '{' <Variable Assignment> '}'\n" +
"<Extends>                     ::= 'extends' Identifier |\n" +
"<Class Code>                  ::= <Variables> <Class Code> | <Methods> <Class Code> |\n" +
"<Class>                       ::= 'class' Identifier <Extends> '{' <Class Code> '}'\n" +
"<Parameters>                  ::= <Declaration> <Parameter> |\n" +
"<Parameter>                   ::= ',' <Parameters> |\n" +
"<Methods>                     ::= 'method' <Declaration> '(' <Parameters> ')' <Code Block>\n" +
"<Code Block>                  ::= '{' <Code Statements> '}'\n" +
"<Code Statements>             ::= <If-Block> <Code Statements> | <Looping-Block> <Code Statements>\n" +
"<Condition>                   ::= <Condition Expression> | <Method Call>\n" +
"<Argument>                    ::=  ',' <Initial Value> <Argument> |\n" +
"<Arguments>                   ::= <Initial Value> <Argument> |\n" +
"<Return Statement>            ::= <Initial Value> | <Condition Expression> | '(' <Return Statement> ')' |\n" +
"<Attributition>               ::= '=' <Expression> | <Increment-Decrement>\n" +
"<Line Code>                   ::= 'return' <Return Statement> ';' | <Method Call> ';' | <Read> ';' | <Write> ';' | <Valid Identifier> <Attribute Access> <Attributition> ';'\n" +
"<If-Block>                    ::= 'if' '(' <Condition> ')' 'then' <Code Block> <Else-Block>\n" +
"<Else-Block>                  ::= 'else' <Post-Else-Block> |\n" +
"<Post-Else-Block>             ::= <If-Block> | <Code Block> |\n" +
"<Looping-Block>               ::= 'while' '(' <Condition> ')' <Code Block>\n" +
"<Read>                        ::= 'read' '(' <Valid Values> <Read Parameters> ')'\n" +
"<Read Parameters>             ::= ',' <Valid Values> <Read Parameters> |\n" +
"<Write>                       ::= 'write' '(' <Write Parameters> ')'\n" +
"<To-Write>                    ::= ',' <Valid Values> <To-Write> | ',' <Method Call> <To-Write> |\n" +
"<Write Parameters>            ::= <Valid Values> <To-Write> | <Method Call> <To-Write>§" ;
    
    public void Gramatica(){
        naoTerminais = new ArrayList<NaoTerminal>();
    }
    
    public void lerLinha(){
        String linha[];
        String aux = "";
        char[] x = grama.toCharArray();
        ArrayList aaa = new ArrayList<String>();
        int i;
        //System.out.println("caralho" + x.length);
        for(i = 0; i<x.length ; i++){
            
            if(x.length > i+1){
                //System.out.println("aaaa");
                if(x[i] == '\n'){
                 //System.out.println(aux);
                    aaa.add(aux);
                    aux = "";
                }else{
                    aux = aux + x[i];
                }
            }else{
                //System.out.println("a " + x[i]);
                if(x[i] == '§'){
                    aaa.add(aux);
                }else{
                    
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
    
    public void criaEstrutura(){
        Iterator<String> itera = linhas.iterator();
        String linha;
        while(itera.hasNext()){
            linha = itera.next();
            String nome = "";
            String[] miney = linha.split("::=");
            nome = miney[0];
            String aux = "";
            for (int i = 0; i < nome.length(); i++) {
                if(nome.charAt(i) == '>'){
                    aux = aux + '>';
                    break;
                }else{
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
                if(a[i].equals("")){
                    derivacoes.add("@");
                    b = true;
                }else{
                    derivacoes.add(a[i]);
                }
            }
            
            NaoTerminal nt = new NaoTerminal(nome);
            nt.setDerivacoes(derivacoes);
            nt.setVazio(b);
            naoTerminais.add(nt);
        }
    }
    
    
}
