package tabuleiro;

public class Tabuleiro {
	
	private Integer linhas;
	private Integer colunas;
	private Peca[][] pecas;
	
	public Tabuleiro(Integer linhas, Integer colunas) {
		if(linhas < 1 || colunas< 1) {
			throw new TabuleiroException("Erro criando o tabuleiro: É necessário que haja pelo menos uma linha e uma coluna");	
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[this.linhas][this.colunas];
	}

	public Integer getLinhas() {
		return linhas;
	}

	public Integer getColunas() {
		return colunas;
	}

	public Peca peca(Integer linha, Integer coluna) {
		if(!posicaoExistente(linha, coluna)) {
			throw new TabuleiroException("A posição não está no tabuleiro");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Posicao posicao) {
		if(!posicaoExistente(posicao)) {
			throw new TabuleiroException("A posição não está no tabuleiro");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void colocarPeca(Peca peca, Posicao posicao) {
		if(temUmaPeca(posicao)) {
			throw new TabuleiroException("Ja existe uma peça nessa posição");
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	private Boolean posicaoExistente(Integer linha, Integer coluna) {
		return linha >= 0 && linha < this.linhas && coluna >= 0 && coluna < this.colunas;
	}
	
	public Boolean posicaoExistente(Posicao posicao) {
		return posicaoExistente(posicao.getLinha() , posicao.getColuna());
	}
	
	public Boolean temUmaPeca(Posicao posicao) {
		if(!posicaoExistente(posicao)) {
			throw new TabuleiroException("A posição não está no tabuleiro");
		}
		return peca(posicao) != null;
		
	}
	
}
