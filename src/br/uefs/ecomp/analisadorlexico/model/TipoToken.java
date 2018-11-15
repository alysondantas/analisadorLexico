/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

/**
 *
 * @author Alyson Dantas e Lucas Cardoso
 */
public abstract class TipoToken {
    public abstract class Id{
            public static final int TokenPalavraReservada = 1;
            public static final int TokenIdentificador = 2;
            public static final int TokenLetra = 3;
            public static final int TokenCadeiaCaracteres = 4;
            public static final int TokenNumero = 5;
            public static final int TokenDigito = 6;
            public static final int TokenComentarioBloco = 7;
            public static final int TokenComentarioLinha = 8;
            public static final int TokenOpAritmetico = 9;
            public static final int TokenOpRelacional = 10;
            public static final int TokenOpLogico = 11;
            public static final int TokenDelimitador = 12;
            public static final int TokenSpace = 13;
            public static final int TokenSimbolo = 14;
        }
        
        public abstract class Nome{
            public static final String TokenPalavraReservada = "PalavraReservada";
            public static final String TokenIdentificador = "Identificador";
            public static final String TokenLetra = "Letra";
            public static final String TokenCadeiaCaracteres = "CadeiaDeCaracteres";
            public static final String TokenNumero = "Numero";
            public static final String TokenDigito = "Digito";
            public static final String TokenComentarioBloco = "ComentarioDeBloco";
            public static final String TokenComentarioLinha = "ComentarioDeLinha";
            public static final String TokenOpAritmetico = "OperadorAritmetico";
            public static final String TokenOpRelacional = "OperadorRelacional";
            public static final String TokenOpLogico = "OperadorLogico";
            public static final String TokenDelimitador = "Delimitador";
            public static final String TokenSpace = "Espaço";
            public static final String TokenSimbolo = "Simbolo";
            
        }
    
}
