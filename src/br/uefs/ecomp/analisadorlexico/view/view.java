/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.view;

import br.uefs.ecomp.analisadorlexico.controller.ControllerDados;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alyso
 */
public class view {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ControllerDados controller = ControllerDados.getInstance();
        controller.setDiretorio("teste/");
        System.out.println("Iniciando analisador lexico");
        try {
            controller.listaArquivos();
            // TODO code application logic here
        } catch (IOException ex) {
            Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
