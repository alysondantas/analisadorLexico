/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.model;

import java.util.regex.Pattern;
/**
 *
 * @author Alyson Dantas e Fada Cardoso
 */
public class Analisador {
    private static final Pattern number = Pattern.compile("(-)?\\s*[0-9]+[0-9](.+[0-9]([0-9])*)?");

    public static boolean validarNumero(String palavra) {
        return number.matcher(palavra).matches();
    }

}
