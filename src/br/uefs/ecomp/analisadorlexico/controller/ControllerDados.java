/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.analisadorlexico.controller;

/**
 *
 * @author alyso
 */
public class ControllerDados {
    
    private static ControllerDados unicaInstancia;
    private String arquivoOriginal=""; //String para salvar o conteúdo do arquivo original antes da compactação


	private ControllerDados() {
	}

	/**
	 * controla o instanciamento de objetos Controller
	 *
	 * @return unicaInstancia
	 */
	public static synchronized ControllerDados getInstance() {
		if (unicaInstancia == null) {
			unicaInstancia = new ControllerDados();
		}
		return unicaInstancia;
	}

	/**
	 * reseta o objeto Controller ja instanciado
	 */
	public static void zerarSingleton() {
		unicaInstancia = null;
	}
        
        public void lerArq(){
            
        }
    
}
