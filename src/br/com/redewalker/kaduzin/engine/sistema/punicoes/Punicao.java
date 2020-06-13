package br.com.redewalker.kaduzin.engine.sistema.punicoes;

import br.com.redewalker.kaduzin.engine.WalkerEngine;

public class Punicao implements Comparable<Punicao> {
	
	private long id;
	
	public Punicao(long id) {
		this.id = id;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public boolean isAvaliacao() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getBoolean(id, "avaliacao");
	}
	
	public void setAvaliacao(boolean set) {
		WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().setBoolean(id, "despunidor", set);
	}
	
	public void setAvaliador(String set) {
		WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().setString(id, "avaliador", set);
	}
	
	public void setDespunidor(String set) {
		WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().setString(id, "despunidor", set);
	}
	
	public String getAvaliador() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "avaliador");
	}
	
	public String getDespunidor() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "despunidor");
	}
	
	public boolean hasAvaliador() {
		return !getAvaliador().equalsIgnoreCase("n");
	}
	
	public boolean hasDespunidor() {
		return !getDespunidor().equalsIgnoreCase("n");
	}
	
	public String getComentario() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "comentario");
	}
	
	public boolean isAvaliado() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getBoolean(id, "avaliado");
	}
	
	public void setAvaliado(boolean set) {
		WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().setBoolean(id, "avaliado", set);
	}
	
	public boolean isPermanente() {
		return getAte() == 0;
	}
	
	public boolean isAtivo() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getBoolean(id, "ativo");
	}
	
	public void setAtivo(boolean set) {
		WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().setBoolean(id, "ativo", set);
	}
	
	public long getID() {
		return id;
	}

	public long getData() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getLong(id, "data");
	}

	public long getAte() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getLong(id, "ate");
	}

	public String getAutor() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "autor");
	}

	public String getProva() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "prova");
	}

	public PunicaoMotivoType getMotivo() {
		return PunicaoMotivoType.getByNome(WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "motivo"));
	}

	public PunicoesType getTipo() {
		return PunicoesType.getByNome(WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "tipo"));
	}

	public String getJogador() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "jogador");
	}

	@Override
	public int compareTo(Punicao o) {
		if (id < o.getID()) return -1;
		else if (id > o.getID()) return 1;
		else return 0;
	}

}
