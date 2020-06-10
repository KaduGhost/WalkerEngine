package br.com.redewalker.kaduzin.engine.sistema.usuario;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType.CargoType;

public class Usuario {
	
	private String nickOriginal;
	
	public Usuario(String nick) {
		this.nickOriginal = nick;
	}
	
	public boolean hasPermission(String permission) {
		return /*permissions.contains(permission.toLowerCase()) || */hasPermissionOP() || WalkerEngine.get().getGruposManager().hasPermission(getGrupoIn(), permission) || WalkerEngine.get().getGruposManager().hasPermission(getGrupoOut(), permission) || (getPlayer() instanceof Player && (getPlayer().hasPermission(permission)));
	}
	
	private boolean hasPermissionOP() {
		return /*permissions.contains("*") ||*/ WalkerEngine.get().getGruposManager().hasPermission(getGrupoIn(), "*") || WalkerEngine.get().getGruposManager().hasPermission(getGrupoOut(), "*") || (getPlayer() instanceof Player && (getPlayer().hasPermission("*") || getPlayer().isOp()));
	}
	
	public void atualizarGrupo() {
		boolean istaff = WalkerEngine.get().getStaffManager().isStaff(getNickOriginal());
		if (!istaff && getGrupoIn().isStaff()) {
			setGrupoIn(GrupoType.Membro); 
			return;
		}
		if (WalkerEngine.get().getStaffManager().isStaffInServer(getNickOriginal(), WalkerEngine.get().getServerType().toString())) {
			setGrupoIn(WalkerEngine.get().getStaffManager().getGrupoStaffInServer(this));
		}else if (istaff) setGrupoIn(GrupoType.Staff);
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(nickOriginal);
	}
	
	public void setGrupoIn(GrupoType grupo) {
		if (grupo.getTipo().equals(CargoType.Tag)) return;
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "grupo", grupo.toString());
	}
	
	public void setGrupoOut(GrupoType grupo) {
		if (!grupo.getTipo().equals(CargoType.Tag)) return;
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "grupoout", grupo.toString());
	}
	
	public GrupoType getGrupoIn() {
		return GrupoType.getTipo(WalkerEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "grupo"));
	}
	
	public GrupoType getGrupoOut() {
		return GrupoType.getTipo(WalkerEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "grupoout"));
	}
	
	public String getNome() {
		return nickOriginal;
	}
	
	public void setLogado(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "logado", set);
	}
	
	public boolean isLogado() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "logado");
	}
	
	public void setOnline(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "online", set);
	}
	
	public boolean isOnline() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "online");
	}
	
	public void getServer() {
		
	}
	
	public void setServer() {
		
	}
	
	public double getCash() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getDouble(nickOriginal, "cash");
	}

	public void setCash(double cash) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setDouble(nickOriginal, "cash", cash);
	}
	
	public String getNickOriginal() {
		return nickOriginal;
	}
	
	public boolean isVanish() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "vanish");
	}

	public void setVanish(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "vanish", set);
	}
	
}
