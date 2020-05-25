package xadrez;

import tabuleiro.Peca;
import tabuleiro.Tabuleiro;

public class PecaXadrez extends Peca {
	
	private Cores cor;

	public PecaXadrez(Tabuleiro tabuleiro, Cores cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cores getCor() {
		return cor;
	}

	

}
