package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Integer turno;
	private Cores jogadorAtual;
	private Tabuleiro tabuleiro;

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cores.WHITE;
		configInicial();
	}
	
	public Integer getTurno() {
	return turno;
	}
	
	public Cores getJogadorAtual(){
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
	
	public boolean[][] movimentosPossiveis(PosicaoXadrez posicaoOrigem){
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
		return pecaCapturada;
	}

	private void validarPosicaoDeOrigem(Posicao posicao) {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new XadrezException("Não tem nenhuma peça na posição de origem");
		}
		if(jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("A peça escolhida não é sua!");
		}
		if(!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()) {
			throw new XadrezException("Não existem movimentos possíveis para peça escolhida");
		}
	}
	
	private void validarPosicaoDeDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezException("A peça escolhida não pode ser mover para posição de destino");
		}
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cores.WHITE) ? Cores.BLACK : Cores.WHITE;
	}

	private void colocarNovaPeca(char coluna, Integer linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
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
