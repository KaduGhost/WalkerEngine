package br.com.redewalker.kaduzin.engine.sistema.punicoes;

public enum PunicoesType {
	
	BAN("Ban"),
	MUTE("Mute"),
	KICK("Kick"),
	IPBAN("IP");
	
	private String desc;
	
	private PunicoesType(String descricao) {
		this.desc = descricao;
	}
	
	public String getDescricao() {
		return desc;
	}
	
	public static PunicoesType getByNome(String nome) {
		if (nome.equalsIgnoreCase("ban")) return BAN;
		else if (nome.equalsIgnoreCase("mute")) return MUTE;
		else if (nome.equalsIgnoreCase("kick")) return KICK;
		else if (nome.equalsIgnoreCase("ip")) return IPBAN;
		return null;
	}

}
