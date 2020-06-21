package br.com.redewalker.kaduzin.engine.sistema.server;

import br.com.redewalker.kaduzin.engine.WalkerEngine;

public class Server {
	
	private Servers server;
	
	public Server(Servers server) {
		this.server = server;
	}
	
	public Servers getServer() {
		return server;
	}
	
	public void setManutencao(boolean set) {
		WalkerEngine.get().getConexaoManager().getConexaoServersDAO().setBoolean(server.toString(), "manutencao", set);
	}
	
	public boolean isManutencao() {
		return WalkerEngine.get().getConexaoManager().getConexaoServersDAO().getBoolean(server.toString(), "manutencao");
	}
	
}
