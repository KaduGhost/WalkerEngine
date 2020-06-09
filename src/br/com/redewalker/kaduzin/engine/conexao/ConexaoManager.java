package br.com.redewalker.kaduzin.engine.conexao;

import br.com.redewalker.kaduzin.engine.configuracao.Config;

public class ConexaoManager {
	
	private ConexaoAPI api;
	private ConexaoUsuarioDAO usuario;
	
	public ConexaoManager(Config config) {
		this.api = new ConexaoAPI(config);
		this.usuario = new ConexaoUsuarioDAO("engine_usuario", "uuid");
	}
	
	public ConexaoAPI getConexaoAPI() {
		return api;
	}
	
	public ConexaoUsuarioDAO getUsuarioConnection() {
		return usuario;
	}

}
