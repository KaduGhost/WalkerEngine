package br.com.redewalker.kaduzin.engine.sistema.server;

import java.util.ArrayList;
import java.util.EnumSet;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers.ServerType;

public class ServerManager {
	
	private Servers server;
	
	public ServerManager(Servers tipo) {
		this.server = tipo;
		WalkerEngine.get().getConexaoManager().getConexaoServersDAO().checkTable();
		WalkerEngine.get().getConexaoManager().getConexaoServersDAO().saveAllServers(getAllServer());
	}
	
	public ArrayList<Server> getAllServersLobby() {
		ArrayList<Server> servers = new ArrayList<Server>();
		for (Server s : getAllServer()) {
			if (s.getServer().getTipo().equals(ServerType.Lobby)) servers.add(s);
		}
		return servers;
	}
	
	public Servers getServerType() {
		return server;
	}
	
	public Server getServer() {
		return new Server(server);
	}
	
	public Server getServer(Servers server) {
		return new Server(server); 
	}
	
	public ArrayList<Server> getAllServer() {
		ArrayList<Server> servers = new ArrayList<Server>();
		for (Servers gp : EnumSet.allOf(Servers.class)) {
			servers.add(new Server(gp));
		}
		return servers;
	}
	
	public static boolean isExists(String nome) {
		for (GrupoType gp : EnumSet.allOf(GrupoType.class)) {
			if (nome.equalsIgnoreCase(gp.toString())) return true;
		}
		return false;
	}
	
	

}
