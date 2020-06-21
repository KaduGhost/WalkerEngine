package br.com.redewalker.kaduzin.engine.sistema.server;

import java.util.EnumSet;

public enum Servers {
	
	Rede(ServerType.Lobby),
	Lobby(ServerType.Lobby),
	Lobby2(ServerType.Lobby),
	Factions(ServerType.Server),
	Factions1(ServerType.Server);
	
	private ServerType tipo;
	
	private Servers(ServerType tipo) {
		this.tipo = tipo;
	}
	
	public ServerType getTipo() {
		return tipo;
	}
	
	public static Servers getTipo(String nome) {
		for (Servers gp : EnumSet.allOf(Servers.class)) {
			if (nome.equalsIgnoreCase(gp.toString())) return gp;
		}
		return null;
	}
	
	public enum ServerType {
		
		Lobby, Server;
		
	}

}
