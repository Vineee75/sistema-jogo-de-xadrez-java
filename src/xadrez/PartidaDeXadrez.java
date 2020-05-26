package xadrez;

import java.util.ArrayList;
import java.util.List;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Integer turno;
	private Cores jogadorAtual;
	private Tabuleiro tabuleiro;

	private List<Peca> pecasNoTabuleiro = new ArrayList<Peca>();
	private List<Peca> pecasCapturadas = new ArrayList<Peca>();

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cores.WHITE;
		configInicial();
	}

	public Integer getTurno() {
		return turno;
	}

	public Cores getJogadorAtual() {
		return jogadorAtual;
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

	public boolean[][] movimentosPossiveis(PosicaoXadrez posicaoOrigem) {
		Posicao posicao = posicaoOrigem.toPosicao();
		validarPosicaoDeOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}

	public PecaXadrez executarMovimentoDeXadrez(PosicaoXadrez posicaoDeOrigem, PosicaoXadrez posicaoDeDestino) {
		Posicao origem = posicaoDeOrigem.toPosicao();
		Posicao destino = posicaoDeDestino.toPosicao();
		validarPosicaoDeOrigem(origem);
		validarPosicaoDeDestino(origem, destino);
		Peca pecaCapturada = fazerMovimento(origem, destino);
		proximoTurno();
		return (PecaXadrez) pecaCapturada;
	}

	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.colocarPeca(p, destino);
		
		if(pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		
		return pecaCapturada;
	}

	private void validarPosicaoDeOrigem(Posicao posicao) {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new XadrezException("N�o tem nenhuma pe�a na posi��o de origem");
		}
		if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("A pe�a escolhida n�o � sua!");
		}
		if (!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()) {
			throw new XadrezException("N�o existem movimentos poss�veis para pe�a escolhida");
		}
	}

	private void validarPosicaoDeDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezException("A pe�a escolhida n�o pode ser mover para posi��o de destino");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cores.WHITE) ? Cores.BLACK : Cores.WHITE;
	}

	private void colocarNovaPeca(char coluna, Integer linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configInicial() {
		colocarNovaPeca('c', 1, new Torre(tabuleiro, Cores.WHITE));
		colocarNovaPeca('c', 2, new Torre(tabuleiro, Cores.WHITE));
		colocarNovaPeca('d', 2, new Torre(tabuleiro, Cores.WHITE));
		colocarNovaPeca('e', 2, new Torre(tabuleiro, Cores.WHITE));
		colocarNovaPeca('e', 1, new Torre(tabuleiro, Cores.WHITE));
		colocarNovaPeca('d', 1, new Rei(tabuleiro, Cores.WHITE));

		colocarNovaPeca('c', 7, new Torre(tabuleiro, Cores.BLACK));
		colocarNovaPeca('c', 8, new Torre(tabuleiro, Cores.BLACK));
		colocarNovaPeca('d', 7, new Torre(tabuleiro, Cores.BLACK));
		colocarNovaPeca('e', 7, new Torre(tabuleiro, Cores.BLACK));
		colocarNovaPeca('e', 8, new Torre(tabuleiro, Cores.BLACK));
		colocarNovaPeca('d', 8, new Rei(tabuleiro, Cores.BLACK));
	}
}
