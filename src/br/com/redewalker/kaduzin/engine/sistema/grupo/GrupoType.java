package br.com.redewalker.kaduzin.engine.sistema.grupo;

import java.util.EnumSet;

public enum GrupoType {
	
	Master(CargoType.Staff, 1000),
	Gerente(CargoType.Staff, 990),
	Administrador(CargoType.Staff, 980),
	Moderador(CargoType.Staff, 970),
	Ajudante(CargoType.Staff, 960),
	Staff(CargoType.Staff,950),
	
	Desenvolvedor(CargoType.Tag,900),
	Arquiteto(CargoType.Tag,890),
	ModeradorDiscord(CargoType.Tag,880),
	ModeradorMidia(CargoType.Tag,870),
	ModeradorYT(CargoType.Tag,860),
	Youtuber(CargoType.Tag,850),
	YoutuberJr(CargoType.Tag,840),
	
	VIP3(CargoType.VIP, 4),
	VIP2(CargoType.VIP, 3),
	VIP1(CargoType.VIP, 2),
	VIPLobby(CargoType.VIP,1),
	Membro(CargoType.Membro, 0);
	
	private int prioridade;
	private CargoType tipo;
	
	private GrupoType(CargoType tipo, int prioridade) {
		this.prioridade = prioridade;
		this.tipo = tipo;
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
	
	public enum CargoType {
		
		Staff,
		Tag,
		VIP,
		Membro;
		
	}
	
}
