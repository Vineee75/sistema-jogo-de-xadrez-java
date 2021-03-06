package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import xadrez.Cores;
import xadrez.PartidaDeXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class UI {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void limparTela() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static PosicaoXadrez lerPosicaoXadrez(Scanner sc) {
		try {
			String s = sc.nextLine();
			char coluna = s.charAt(0);
			Integer linha = Integer.parseInt(s.substring(1));
			return new PosicaoXadrez(coluna, linha);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Erro na leitura da posicao do xadrez. Valores validos sao de a1 ate h8");
		}
	}

	public static void imprimirPartida(PartidaDeXadrez partidaDeXadrez, List<PecaXadrez> capturadas) {
		imprimirTabuleiro(partidaDeXadrez.getPecas());
		System.out.println();
		imprimirPecasCapturadas(capturadas);
		System.out.println();
		System.out.println("Turno: " + partidaDeXadrez.getTurno());
		if (!partidaDeXadrez.getXequeMate()) {
			System.out.println("Aguardando jogador: " + partidaDeXadrez.getJogadorAtual());
			if (partidaDeXadrez.getXeque()) {
				System.out.println("XEQUE!");
			}
		} else {
			System.out.println("XEQUE MATE!");
			System.out.println("Vencedor: " + partidaDeXadrez.getJogadorAtual());
		}
	}

	public static void imprimirTabuleiro(PecaXadrez[][] pecas) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.print((ANSI_RED + (8 - i) + ANSI_RESET) + " ");
			for (int j = 0; j < pecas.length; j++) {
				imprimirPeca(pecas[i][j], false);
			}
			System.out.println();

		}
		System.out.println(ANSI_PURPLE + "  a b c d e f g h" + ANSI_RESET);
	}

	public static void imprimirTabuleiro(PecaXadrez[][] pecas, boolean[][] movimentosPossiveis) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.print((ANSI_RED + (8 - i) + ANSI_RESET) + " ");
			for (int j = 0; j < pecas.length; j++) {
				imprimirPeca(pecas[i][j], movimentosPossiveis[i][j]);
			}
			System.out.println();

		}
		System.out.println(ANSI_PURPLE + "  a b c d e f g h" + ANSI_RESET);
	}

	private static void imprimirPeca(PecaXadrez peca, boolean fundo) {
		if (fundo) {
			System.out.print(ANSI_BLUE_BACKGROUND);
		}
		if (peca == null) {
			System.out.print("-" + ANSI_RESET);
		} else if (peca.getCor() == Cores.BRANCO) {
			System.out.print(ANSI_WHITE + peca + ANSI_RESET);
		} else {
			System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
		}

		System.out.print(" ");
	}

	private static void imprimirPecasCapturadas(List<PecaXadrez> capturadas) {
		List<PecaXadrez> branco = capturadas.stream().filter(x -> x.getCor() == Cores.BRANCO)
				.collect(Collectors.toList());
		List<PecaXadrez> preto = capturadas.stream().filter(x -> x.getCor() == Cores.PRETO)
				.collect(Collectors.toList());
		System.out.println("Pecas capturadas: ");
		System.out.print("Brancas: ");
		System.out.print(ANSI_WHITE);
		System.out.print(Arrays.toString(branco.toArray()));
		System.out.println(ANSI_RESET);

		System.out.print("Pretas: ");
		System.out.print(ANSI_YELLOW);
		System.out.print(Arrays.toString(preto.toArray()));
		System.out.println(ANSI_RESET);

	}

}
