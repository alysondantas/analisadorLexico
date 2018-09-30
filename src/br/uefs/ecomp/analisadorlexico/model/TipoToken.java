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
            public static final String TokenPalavraReservada = "TokenPalavraReservada";
            public static final String TokenIdentificador = "TokenIdentificador";
            public static final String TokenLetra = "TokenLetra";
            public static final String TokenCadeiaCaracteres = "TokenCadeiaDeCaracteres";
            public static final String TokenNumero = "TokenNumero";
            public static final String TokenDigito = "TokenDigito";
            public static final String TokenComentarioBloco = "TokenComentarioDeBloco";
            public static final String TokenComentarioLinha = "TokenComentarioDeLinha";
            public static final String TokenOpAritmetico = "TokenOperadorAritmetico";
            public static final String TokenOpRelacional = "TokenOperadorRelacional";
            public static final String TokenOpLogico = "TokenOperadorLogico";
            public static final String TokenDelimitador = "TokenDelimitador";
            public static final String TokenSpace = "TokenEspaço";
            public static final String TokenSimbolo = "TokenSimbolo";
            
        }
    
}
