package br.com.redewalker.kaduzin.engine.sistema.staff;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class Staff {
	
	private int id;
	
	public Staff(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setGrupo(GrupoType grupo) {
		WalkerEngine.get().getConexaoManager().getStaffConnection().modifyStaff(id, grupo, getServer());
	}
	
	public GrupoType getGrupo() {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getGrupo(id);
	}
	
	public Usuario getUsuario() {
		return WalkerEngine.get().getUsuarioManager().getUsuario(WalkerEngine.get().getConexaoManager().getStaffConnection().getString(id, "nick"));
	}
	
	public Servers getServer() {
		return Servers.getTipo(WalkerEngine.get().getConexaoManager().getStaffConnection().getString(id, "server"));
	}

}
