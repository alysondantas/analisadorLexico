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
public abstract class TipoErroSintatico {
    
    public abstract class Erro{
            public static final String AusenciaSimb = "ausencia de simbolo";
            public static final String SimbMalEscrito = "simbolo mal escrito";
            public static final String ExcessoSimb = "excesso de simbolo";
            
        }
    
}
