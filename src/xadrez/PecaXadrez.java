package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca {

	private Cores cor;
	private int contarMovimento;

	public PecaXadrez(Tabuleiro tabuleiro, Cores cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cores getCor() {
		return cor;
	}

	public void aumentarContarMovimento() {
		contarMovimento++;
	}

	public void diminuirContarMovimento() {
		contarMovimento--;
	}

	public Integer getContarMovimento() {
		return contarMovimento;
	}

	public PosicaoXadrez getPosicaoXadrez() {
		return PosicaoXadrez.fromPosicaoXadrez(posicao);
	}

	protected boolean temUmaPecaOponente(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}

}
