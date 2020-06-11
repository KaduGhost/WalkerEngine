package br.com.redewalker.kaduzin.engine.sistema.staff;

import java.util.HashMap;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType.CargoType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class StaffManager {
	
	public StaffManager() {
		WalkerEngine.get().getConexaoManager().getStaffConnection().checkTable();
	}
	
	public void removeStaff(Usuario usuario, GrupoType cargo, String server, Usuario autor) {
		if (!cargo.getTipo().equals(CargoType.Staff) || cargo.equals(GrupoType.Staff)) return;
		if (!autor.hasPermission("walker.staff.setar") || !WalkerEngine.get().getGruposManager().isSuperior(usuario, autor)) return;
		if (isStaffInServer(usuario.getNickOriginal(), server)) {
			Staff staff = WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
			staff.setGrupo(cargo);
		}else WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), cargo, server);
		usuario.atualizarGrupo();
		return;
	}
	
	public boolean setStaff(Usuario usuario, GrupoType cargo, String server, Usuario autor) {
		if (!cargo.getTipo().equals(CargoType.Staff) || cargo.equals(GrupoType.Staff)) return false;
		if (!autor.hasPermission("walker.staff.setar") || !WalkerEngine.get().getGruposManager().isSuperior(usuario, autor)) return false;
		if (isStaffInServer(usuario.getNickOriginal(), server)) {
			Staff staff = WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
			staff.setGrupo(cargo);
		}else WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), cargo, server);
		usuario.atualizarGrupo();
		return true;
	}
	
	public HashMap<Integer, Staff> getAllStaffs() {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getAllStaffs();
	}
	
	public HashMap<Integer, Staff> getAllStaffsServer(String server) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getAllStaffsServer(server);
	}
	
	public boolean isStaffInServer(String nick, String server) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().isStaffServer(nick, server);
	}
	
	public boolean isStaff(String nick) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().isStaff(nick);
	}
	
	public GrupoType getGrupoStaffInServer(Usuario usuario) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), WalkerEngine.get().getServerType().toString()).getGrupo();
	}
	

}
