package br.com.redewalker.kaduzin.engine.sistema.usuario;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType.CargoType;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers.ServerType;
import br.com.redewalker.kaduzin.engine.sistema.staff.Staff;
import br.com.redewalker.kaduzin.engine.sistema.vip.VIP;

public class Usuario {
	
	private String nickOriginal;
	
	public Usuario(String nick) {
		this.nickOriginal = nick;
	}
	
	public String getChatFocus() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "chatfocus");
	}

	public void setChatFocus(String chatFocus) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "chatfocus", chatFocus);
	}
	
	public boolean hasPermission(String permission) {
		return /*permissions.contains(permission.toLowerCase()) ||*/ hasPermissionOP() || WalkerEngine.get().getGruposManager().hasPermission(getGrupoIn(), permission) || WalkerEngine.get().getGruposManager().hasPermission(getGrupoOut(), permission) /*|| (getPlayer() instanceof Player && (getPlayer().hasPermission(permission)))*/;
	}
	
	private boolean hasPermissionOP() {
		return /*permissions.contains("*") ||*/ WalkerEngine.get().getGruposManager().hasPermission(getGrupoIn(), "*") || WalkerEngine.get().getGruposManager().hasPermission(getGrupoOut(), "*") /*|| (getPlayer() instanceof Player && (getPlayer().hasPermission("*") || getPlayer().isOp()))*/;
	}
	
	public void atualizarGrupo() {
		boolean istaff = WalkerEngine.get().getStaffManager().isStaff(getNickOriginal());
		boolean isvip = WalkerEngine.get().getVipManager().isVIP(this);
		if (!isvip && getGrupoIn().isVIP()) setGrupoIn(GrupoType.Membro);
		else if (isvip) {
			VIP vip = WalkerEngine.get().getVipManager().getVIPAtivo(getNickOriginal(), WalkerEngine.get().getServerType());
			if (vip != null) {
				setGrupoIn(vip.getTipo());
				return;
			}
			else if (vip == null && WalkerEngine.get().getServerType().getTipo().equals(ServerType.Lobby)) setGrupoIn(GrupoType.VIPLobby);
		}
		if (!istaff && getGrupoIn().isStaff()) setGrupoIn(GrupoType.Membro); 
		else if (istaff) {
			Staff s = WalkerEngine.get().getStaffManager().getStaffAtivoNoServer(this, WalkerEngine.get().getServerType());
			if (s != null) setGrupoIn(s.getGrupo());
			else setGrupoIn(GrupoType.Staff);
		}
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
	
	public void setVerMsgPunicao(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "msgban", set);
	}
	
	public boolean isVerMsgPunicao() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "msgban");
	}
	
	public void setLogado(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "logado", set);
	}
	
	public boolean isLogado() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "logado");
	}
	
	private void setOnlineInServer(String set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "onlineserver", set);
	}
	
	public boolean isOnlineInServer() {
		String s = WalkerEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "onlineserver");
		if (!s.equalsIgnoreCase("nenhum") && Servers.getTipo(s) != null) return Servers.getTipo(s).equals(WalkerEngine.get().getServerType());
		return false;
	}
	
	public void setOnline(boolean set) {
		if (!set) setOnlineInServer("nenhum");
		else setOnlineInServer(WalkerEngine.get().getServerType().toString());
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "online", set);
	}
	
	public boolean isOnline() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "online");
	}
	
	public void setTell(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "tell", set);
	}
	
	public boolean isTell() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "tell");
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
