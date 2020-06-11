package br.com.redewalker.kaduzin.engine.conexao;

import br.com.redewalker.kaduzin.engine.configuracao.Config;

public class ConexaoManager {
	
	private ConexaoAPI api;
	private ConexaoServerAPI serverapi;
	private ConexaoUsuarioDAO usuario;
	private ConexaoGrupoDAO grupo;
	private ConexaoStaffDAO staff;
	
	public ConexaoManager(Config config) {
		this.api = new ConexaoAPI(config);
		this.serverapi = new ConexaoServerAPI(config);
		this.usuario = new ConexaoUsuarioDAO("engine_usuario", "nick");
		this.grupo = new ConexaoGrupoDAO("engine_grupo", "nick");
		this.staff = new ConexaoStaffDAO("engine_staff", "id");
	}
	
	public ConexaoStaffDAO getStaffConnection() {
		return staff;
	}
	
	public ConexaoGrupoDAO getGrupoConnection() {
		return grupo;
	}
	
	public ConexaoAPI getConexaoAPI() {
		return api;
	}
	
	public ConexaoServerAPI getConexaoServerAPI() {
		return serverapi;
	}
	
	public ConexaoUsuarioDAO getUsuarioConnection() {
		return usuario;
	}

}
