package xadrez;

import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Tabuleiro tabuleiro;

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		configInicial();
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];

		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {

				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}

		return mat;

	}

	private void colocarNovaPeca(char coluna, Integer linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}

	private void configInicial() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cores.WHITE));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cores.BLACK));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cores.WHITE));
	}
}
