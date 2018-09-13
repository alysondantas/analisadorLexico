/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

/**
 *
 * @author alyso
 */
public abstract class Erros {
        public abstract class Id{
            public static final int ComentarioFinalAberto = 1;
            public static final int ComentarioInicioAberto = 2;
            public static final int CadeiaCharsInicioAberto = 3;
            public static final int CadeiaCharsFinalAberto = 4;
            public static final int SimboloInvalido = 5;
            public static final int IdentificadorInvalido = 6;
            
        }
        
        public abstract class Valor{
            public static final String ComentarioFinalAberto = "ERRO: Final de delimitador de comentario não fechado.\n";
            public static final String ComentarioInicioAberto = "ERRO: Inicio de delimitador de comentario não fechado.\n";
            public static final String CadeiaCharsInicioAberto = "ERRO: Inicio de cadeia de caracteres sem aspas.\n";
            public static final String CadeiaCharsFinalAberto = "ERRO: Final de cadeia de caracteres sem aspas.\n";
            public static final String SimboloInvalido = "ERRO: Simbolo invalido encontrado.\n";
            public static final String IdentificadorInvalido = "ERRO: Digito seguido de letra encontrado.\n";
        }
    
}
