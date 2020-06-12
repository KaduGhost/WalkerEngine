package br.com.redewalker.kaduzin.engine.sistema.grupo;

import java.util.EnumSet;

public enum GrupoType {
	
	Master(CargoType.Staff, 1000, CargoServerType.Rede),
	Gerente(CargoType.Staff, 990, CargoServerType.Rede),
	Administrador(CargoType.Staff, 980, CargoServerType.Server),
	Moderador(CargoType.Staff, 970, CargoServerType.Server),
	Ajudante(CargoType.Staff, 960, CargoServerType.Server),
	Staff(CargoType.Staff,950, CargoServerType.Server),
	
	Desenvolvedor(CargoType.Tag,900, CargoServerType.Rede),
	Arquiteto(CargoType.Tag,890, CargoServerType.Rede),
	ModeradorDiscord(CargoType.Tag,880, CargoServerType.Rede),
	ModeradorMidia(CargoType.Tag,870, CargoServerType.Rede),
	ModeradorYT(CargoType.Tag,860, CargoServerType.Rede),
	Youtuber(CargoType.Tag,850, CargoServerType.Server),
	YoutuberJr(CargoType.Tag,840, CargoServerType.Server),
	
	VIP3(CargoType.VIP, 4, CargoServerType.Server),
	VIP2(CargoType.VIP, 3, CargoServerType.Server),
	VIP1(CargoType.VIP, 2, CargoServerType.Server),
	VIPLobby(CargoType.VIP,1, CargoServerType.Rede),
	Membro(CargoType.Membro, 0, CargoServerType.Server);
	
	private int prioridade;
	private CargoType tipo;
	private CargoServerType server;
	
	private GrupoType(CargoType tipo, int prioridade, CargoServerType server) {
		this.prioridade = prioridade;
		this.tipo = tipo;
		this.server = server;
	}
	
	public CargoServerType getServer() {
		return server;
	}
	
	public CargoType getTipo() {
		return tipo;
	}
	
	public int getPrioridade() {
		return prioridade;
	}
	
	public static boolean isExists(String nome) {
		for (GrupoType gp : EnumSet.allOf(GrupoType.class)) {
			if (nome.equalsIgnoreCase(gp.toString())) return true;
		}
		return false;
	}
	
	public boolean isVIP() {
		return this.tipo == CargoType.VIP;
	}
	
	public boolean isStaff() {
		return this.tipo == CargoType.Staff;
	}
	
	public boolean isMembro() {
		return this.tipo == CargoType.Membro;
	}
	
	public CargoType getCargoType() {
		return tipo;
	}
	
	public static GrupoType getTipo(String nome) {
		for (GrupoType gp : EnumSet.allOf(GrupoType.class)) {
			if (nome.equalsIgnoreCase(gp.toString())) return gp;
		}
		return Membro;
	}
	
	public enum CargoServerType {
		
		Rede, Server;
		
	}
	
	public enum CargoType {
		
		Staff,
		Tag,
		VIP,
		Membro;
		
	}
	
}
