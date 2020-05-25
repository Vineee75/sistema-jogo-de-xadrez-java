package application;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.PartidaDeXadrez;

public class Programa {

	public static void main(String[] args) {
		
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		UI.imprimirTabuleiro(partidaDeXadrez.getPecas());

	}

}
