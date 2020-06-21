package br.com.redewalker.kaduzin.engine.sistema.manutencao;

public enum ManutencaoType {
	
	MANUTENCAO("Manutenção"),
	STOP("Desligamento");
	
	private String nome;
	
	private ManutencaoType(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
