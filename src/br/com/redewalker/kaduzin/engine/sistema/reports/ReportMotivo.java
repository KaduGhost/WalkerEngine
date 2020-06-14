package br.com.redewalker.kaduzin.engine.sistema.reports;

public class ReportMotivo {
	
	private String nome, nick, descricao;
	private ReportMotivoType tipo;
	
	public ReportMotivo(String nick, String nome, String descricao, ReportMotivoType tipo) {
		this.nome = nome;
		this.nick = nick;
		this.descricao = descricao;
		this.tipo = tipo;
	}

	public ReportMotivoType getTipo() {
		return tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getNome() {
		return nome;
	}

	public String getNick() {
		return nick;
	}

}
