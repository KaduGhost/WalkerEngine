package br.com.redewalker.kaduzin.engine.sistema.usuario;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;

public class UsuarioManager {
	
	public UsuarioManager() {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().checkTable();
	}
	
	public int getQuantidadeUsuariosOnlineNoServerBypassVanish() {
		return Bukkit.getOnlinePlayers().size();
	}
	
	public long getQuantidadeUsuariosOnlineBypassVanish() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getQuantUsuariosOnline();
	}
	
	public long getQuantidadeUsuariosOnlineNoServer() {
		int cont = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			Usuario j = getUsuario(p.getName());
			if (!j.isVanish()) cont++;
		}
		return cont;
	}
	
	public HashMap<String, Usuario> getAllUsuarios() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getAllUsuarios();
	}
	
	public Usuario getUsuario(String nick) {
		if (!exists(nick)) return null;
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getUsuario(nick);
	}
	
	public Usuario createUsuario(Player p) {
		if (exists(p.getName())) return null;
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().createUsuario(p.getName(), p.getUniqueId().toString());
		return getUsuario(p.getName());
	}
	
	public boolean exists(String nick) {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().exists("nick", nick);
	}
	
}
