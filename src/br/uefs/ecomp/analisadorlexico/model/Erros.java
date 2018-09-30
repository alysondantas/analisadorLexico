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
public abstract class Erros {
        public abstract class Id{
            public static final int ErroSimboloMalFormado = 1;
            public static final int ComentarioAberto = 2;
            public static final int CadeiaCharsMalformada = 3;
            public static final int IdentificadorInvalido = 4;
            public static final int ErroNumero = 4;
            
        }
        
        public abstract class Nome{
            public static final String ErroSimboloMalFormado = "ERRO: Simbolo invalido";
            public static final String ComentarioAberto = "ERRO: Delimitador de comentario não fechado.";
            public static final String CadeiaCharsMalformada = "ERRO: Cadeia de caracteres mal formada.";
            public static final String IdentificadorInvalido = "ERRO: Identificador mal formado";
            public static final String ErroNumero = "ERRO: Numero mal formado.";
        }
    
}
