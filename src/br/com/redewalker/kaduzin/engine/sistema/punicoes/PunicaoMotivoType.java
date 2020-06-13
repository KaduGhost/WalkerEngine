package br.com.redewalker.kaduzin.engine.sistema.punicoes;

import java.util.EnumSet;

import br.com.redewalker.kaduzin.engine.WalkerEngine;

public enum PunicaoMotivoType {
	
	OFENSAJOGADOR("Ofensa a jogador", PunicoesType.MUTE),	
	FLOODSPAM("Flood/Spam", PunicoesType.MUTE),
	INIFLOOD("Iniciativa de Flood", PunicoesType.MUTE),	
	DIVULGACAO("Divulgação [SIMPLES]", PunicoesType.MUTE),	
	DISCRIMINACAO("Discriminação", PunicoesType.MUTE),
	ANTIJOGOCHAT("Antijogo [CHAT]", PunicoesType.MUTE),
	AMEACA("Ameaça", PunicoesType.MUTE),
	BUG("Abuso de bugs", PunicoesType.BAN),
	ANTIJOGOJOGO("Antijogo [JOGO]", PunicoesType.BAN),
	CONTAFAKE("Conta Fake", PunicoesType.BAN),
	DIVULGACAOSERVER("Divulgação [GRAVE]", PunicoesType.BAN),
	ESTORNO("Estorno de Pagamento", PunicoesType.BAN),
	HACK("Uso de Hack", PunicoesType.BAN),
	MODNAOPERMITIDO("Uso de Mod não permitido", PunicoesType.BAN),
	OFENSASTAFF("Ofensa a staff/servidor", PunicoesType.BAN),
	DESINFORMACAO("Desinformação", PunicoesType.BAN),
	OUTRO("Outro", null);
	
	private String desc;
	private int tempo;
	private PunicoesType punicao;
	
	PunicaoMotivoType(String descricao, PunicoesType punicao) {
		this.desc = descricao;
		this.punicao = punicao;
		this.tempo = WalkerEngine.get().getConfigManager().getConfigPrincipal().getPunicaoTempo(this.toString().toLowerCase());
	}
	
	public PunicoesType getPunicao() {
		return punicao;
	}
	
	public int getTempo() {
		return tempo;
	}
	
	public String getDescricao() {
		return desc;
	}
	
	public static PunicaoMotivoType getByDesc(String nome) {
		for (PunicaoMotivoType mo : EnumSet.allOf(PunicaoMotivoType.class)) {
			if (nome.equals(mo.getDescricao())) return mo;
		}
		return OUTRO;
	}
	
	public static PunicaoMotivoType getByNome(String nome) {
		for (PunicaoMotivoType tipo : EnumSet.allOf(PunicaoMotivoType.class)) {
			if (nome.equalsIgnoreCase(tipo.toString())) return tipo;
		}
		return OUTRO;
	}

}
