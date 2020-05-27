package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cores;
import xadrez.PartidaDeXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Peao extends PecaXadrez {

	private PartidaDeXadrez partida;

	public Peao(Tabuleiro tabuleiro, Cores cor, PartidaDeXadrez partida) {
		super(tabuleiro, cor);
		this.partida = partida;
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		if (getCor() == Cores.BRANCO) {
			p.setValores(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p))
				;
			{
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValores(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p)
					&& getTabuleiro().posicaoExistente(p2) && !getTabuleiro().temUmaPeca(p2)
					&& getContarMovimento() == 0) {

				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExistente(p) && temUmaPecaOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExistente(p) && temUmaPecaOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}

			// Movimento Especial en passant Branco
			if (posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExistente(esquerda) && temUmaPecaOponente(esquerda)
						&& getTabuleiro().peca(esquerda) == partida.getEnPassantVulneravel()) {
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExistente(direita) && temUmaPecaOponente(direita)
						&& getTabuleiro().peca(direita) == partida.getEnPassantVulneravel()) {
					mat[direita.getLinha() - 1][direita.getColuna()] = true;
				}
			}
		} else {
			p.setValores(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p))
				;
			{
				mat[p.getLinha()][p.getColuna()] = true;
			}

			p.setValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p)
					&& getTabuleiro().posicaoExistente(p2) && !getTabuleiro().temUmaPeca(p2)
					&& getContarMovimento() == 0) {

				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExistente(p) && temUmaPecaOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExistente(p) && temUmaPecaOponente(p))
				mat[p.getLinha()][p.getColuna()] = true;
		}
		// Movimento Especial en passant Preto
		if (posicao.getLinha() == 4) {
			Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExistente(esquerda) && temUmaPecaOponente(esquerda)
					&& getTabuleiro().peca(esquerda) == partida.getEnPassantVulneravel()) {
				mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
			}
			Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExistente(direita) && temUmaPecaOponente(direita)
					&& getTabuleiro().peca(direita) == partida.getEnPassantVulneravel()) {
				mat[direita.getLinha() + 1][direita.getColuna()] = true;
			}

		}
		return mat;

	}

	@Override
	public String toString() {
		return "P";
	}
}
