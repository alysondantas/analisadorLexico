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
            
        }
        
        public abstract class Valor{
            public static final String ComentarioFinalAberto = "Final de delimitador de comentario não fechado";
            public static final String ComentarioInicioAberto = "Inicio de delimitador de comentario não fechado";
            public static final String CadeiaCharsInicioAberto = "Inicio de cadeia de caracteres sem aspas";
            public static final String CadeiaCharsFinalAberto = "Final de cadeia de caracteres sem aspas";
        }
    
}
