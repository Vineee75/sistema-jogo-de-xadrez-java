package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Integer turno;
	private Cores jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean xeque;
	private boolean xequeMate;
	private PecaXadrez enPassantVulneravel;
	private PecaXadrez promovida;

	private List<Peca> pecasNoTabuleiro = new ArrayList<Peca>();
	private List<Peca> pecasCapturadas = new ArrayList<Peca>();

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cores.BRANCO;
		configInicial();
	}

	public Integer getTurno() {
		return turno;
	}

	public Cores getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getXeque() {
		return xeque;
	}

	public boolean getXequeMate() {
		return this.xequeMate;
	}

	public PecaXadrez getEnPassantVulneravel() {
		return enPassantVulneravel;
	}

	public PecaXadrez getPromovida() {
		return promovida;
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

		if (testarXeque(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadrezException("Voce nao pode se colocar em xeque!");
		}

		PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(destino);

		// #Movimento Especial: Promoção!
		promovida = null;
		if (pecaMovida instanceof Peao) {
			if ((pecaMovida.getCor() == Cores.BRANCO && destino.getLinha() == 0)
					|| (pecaMovida.getCor() == Cores.PRETO && destino.getLinha() == 7)) {
				promovida = (PecaXadrez) tabuleiro.peca(destino);
				promovida = substituirPecaPromovida("Q");
			}
		}

		xeque = (testarXeque(oponente(jogadorAtual))) ? true : false;

		if (testarXequeMate(oponente(jogadorAtual))) {
			xequeMate = true;
		} else {
			proximoTurno();
		}

		// Movimento Especial en passant
		if (pecaMovida instanceof Peao
				&& (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			enPassantVulneravel = pecaMovida;
		} else {
			enPassantVulneravel = null;
		}

		return (PecaXadrez) pecaCapturada;
	}

	public PecaXadrez substituirPecaPromovida(String tipo) {
		if (promovida == null) {
			throw new IllegalStateException("Nao ha peca para ser promovida");
		}
		if (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") && !tipo.equals("Q")) {
			throw new InvalidParameterException("Tipo invalido para promocao");
		}
		Posicao pos = promovida.getPosicaoXadrez().toPosicao();
		Peca p = tabuleiro.removerPeca(pos);
		pecasNoTabuleiro.remove(p);

		PecaXadrez novaPeca = novaPeca(tipo, promovida.getCor());
		tabuleiro.colocarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);

		return novaPeca;

	}

	private PecaXadrez novaPeca(String tipo, Cores cor) {
		if (tipo.equals("B"))
			return new Bispo(tabuleiro, cor);
		if (tipo.equals("C"))
			return new Cavalo(tabuleiro, cor);
		if (tipo.equals("Q"))
			return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
	}

	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(origem);
		p.aumentarContarMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.colocarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		// Movimento especial Roque lado do Rei
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.colocarPeca(torre, destinoT);
			torre.aumentarContarMovimento();
		}
		// Movimento especial Roque lado da Rainha
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.colocarPeca(torre, destinoT);
			torre.aumentarContarMovimento();
		}

		// #Movimento Especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posiPeao;
				if (p.getCor() == Cores.BRANCO) {
					posiPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());

				} else {
					posiPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removerPeca(posiPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}

		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(destino);
		p.diminuirContarMovimento();
		tabuleiro.colocarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}

		// Movimento especial Roque lado do Rei
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.diminuirContarMovimento();
		}
		// Movimento especial Roque lado da Rainha
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.diminuirContarMovimento();
		}

		// #Movimento Especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulneravel) {
				PecaXadrez peao = (PecaXadrez) tabuleiro.removerPeca(destino);
				Posicao posiPeao;
				if (p.getCor() == Cores.BRANCO) {
					posiPeao = new Posicao(3, destino.getColuna());

				} else {
					posiPeao = new Posicao(4, destino.getColuna());
				}

				tabuleiro.colocarPeca(peao, posiPeao);
			}
		}

	}

	private void validarPosicaoDeOrigem(Posicao posicao) {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new XadrezException("Nao tem nenhuma peca na posicao de origem");
		}
		if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("A peca escolhida nao e sua!");
		}
		if (!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()) {
			throw new XadrezException("Nao existem movimentos possiveis para peca escolhida");
		}
	}

	private void validarPosicaoDeDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezException("A peca escolhida nao pode ser mover para posicao de destino");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cores.BRANCO) ? Cores.PRETO : Cores.BRANCO;
	}

	private Cores oponente(Cores cor) {
		return (cor == Cores.BRANCO) ? Cores.PRETO : Cores.BRANCO;
	}

	private PecaXadrez rei(Cores cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : lista) {
			if (p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Nao existe o Rei da cor " + cor + " no tabuleiro.");
	}

	private boolean testarXeque(Cores cor) {
		Posicao posicaoDoRei = rei(cor).getPosicaoXadrez().toPosicao();
		List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecasOponentes) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[posicaoDoRei.getLinha()][posicaoDoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testarXequeMate(Cores cor) {
		if (!testarXeque(cor)) {
			return false;
		}
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : lista) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez) p).getPosicaoXadrez().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazerMovimento(origem, destino);
						boolean testarXeque = testarXeque(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testarXeque) {
							return false;
						}
					}
				}

			}
		}
		return true;
	}

	private void colocarNovaPeca(char coluna, Integer linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configInicial() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cores.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cores.BRANCO));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cores.BRANCO));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cores.BRANCO));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cores.BRANCO));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cores.BRANCO));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cores.BRANCO));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cores.BRANCO, this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cores.BRANCO, this));

		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cores.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cores.PRETO));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cores.PRETO));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cores.PRETO));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cores.PRETO));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cores.PRETO));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cores.PRETO));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cores.PRETO, this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cores.PRETO, this));
	}
}
