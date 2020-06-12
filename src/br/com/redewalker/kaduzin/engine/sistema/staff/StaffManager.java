package br.com.redewalker.kaduzin.engine.sistema.staff;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.eventos.UsuarioAlteradoStaffEvent;
import br.com.redewalker.kaduzin.engine.eventos.UsuarioEntrouStaffEvent;
import br.com.redewalker.kaduzin.engine.eventos.UsuarioRemovidoStaffEvent;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType.CargoServerType;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers.ServerType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class StaffManager implements Listener {
	
	public StaffManager() {
		WalkerEngine.get().getConexaoManager().getStaffConnection().checkTable();
		Bukkit.getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	public boolean removerStaff(Usuario usuario, Servers server, Usuario autor) {
		if (autor instanceof Usuario && !autor.hasPermission("walker.staff.setar")) return false;
		Staff s = getStaffNoServer(usuario, Servers.Rede);
		if (s != null) {
			if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar.gerente"))) return false;
			if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) return false;
			UsuarioRemovidoStaffEvent event = new UsuarioRemovidoStaffEvent(usuario, s.getGrupo(), autor);
			WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(usuario.getNickOriginal());
			WalkerEngine.get().getServer().getPluginManager().callEvent(event);
		}else {
			if (server.getTipo().equals(ServerType.Lobby)) return false;
			s = getStaffNoServer(usuario, server);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) return false;
				UsuarioRemovidoStaffEvent event = new UsuarioRemovidoStaffEvent(usuario, s.getGrupo(), autor);
				WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(s.getId());
				WalkerEngine.get().getServer().getPluginManager().callEvent(event);
			}
		}
		usuario.atualizarGrupo();
		return true;
	}
	
	public boolean setStaff(Usuario usuario, Servers server, GrupoType grupo, Usuario autor) {
		if (grupo.equals(GrupoType.Staff)) return false;
		if (autor instanceof Usuario && !autor.hasPermission("walker.staff.setar")) return false;
		if (autor instanceof Usuario && autor.getGrupoIn().getPrioridade() <= grupo.getPrioridade()) return false;
		if (grupo.getServer().equals(CargoServerType.Rede)) {
			if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar.gerente"))) return false;
			Staff s = getStaffNoServer(usuario, Servers.Rede);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) return false;
				UsuarioAlteradoStaffEvent event = new UsuarioAlteradoStaffEvent(usuario, s.getGrupo(),grupo, autor);
				WalkerEngine.get().getConexaoManager().getStaffConnection().modifyStaff(s.getId(), grupo, Servers.Rede);
				WalkerEngine.get().getServer().getPluginManager().callEvent(event);
			}else {
				WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(usuario.getNickOriginal());
				WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), grupo, Servers.Rede);
				WalkerEngine.get().getServer().getPluginManager().callEvent(new UsuarioEntrouStaffEvent(usuario, grupo, autor));
			}
		}else {
			if (server.getTipo().equals(ServerType.Lobby)) return false;
			Staff s = getStaffNoServer(usuario, Servers.Rede);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) return false;
				WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(s.getId());
			}
			s = getStaffNoServer(usuario, server);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) return false;
				UsuarioAlteradoStaffEvent event = new UsuarioAlteradoStaffEvent(usuario, s.getGrupo(),grupo, autor);
				WalkerEngine.get().getConexaoManager().getStaffConnection().modifyStaff(s.getId(), grupo, server);
				WalkerEngine.get().getServer().getPluginManager().callEvent(event);
			}else {
				WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), grupo, server);
				WalkerEngine.get().getServer().getPluginManager().callEvent(new UsuarioEntrouStaffEvent(usuario, grupo, autor));
			}
		}
		usuario.atualizarGrupo();
		return true;
	}
	
	public boolean isStaffNoServer(Usuario usuario, Servers server) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().isStaffServer(usuario.getNickOriginal(), server);
	}
	
	public Staff getStaffNoServer(Usuario usuario, Servers server) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
	}
	
	public Staff getStaffAtivoNoServer(Usuario usuario, Servers server) {
		Staff s = getStaffNoServer(usuario, Servers.Rede);
		if (s != null) return s;
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
	}
	
	
	@EventHandler
	void aoEntrarStaff(UsuarioEntrouStaffEvent e) {
		MensagensAPI.mensagemSucesso("Você definiu o cargo "+WalkerEngine.get().getGruposManager().getNomeGrupo(e.getGrupo())+" ao jogador "+e.getJogador().getNickOriginal(), e.getAutor().getPlayer());
		MensagensAPI.mensagemSucesso("Você agora faz parte da equipe do servidor, bem vindo :D", e.getJogador().getPlayer());
	}
	
	@EventHandler
	void aoSairStaff(UsuarioRemovidoStaffEvent e) {
		MensagensAPI.mensagemSucesso("Você removeu o jogador "+e.getJogador().getNickOriginal()+" da equipe do servidor", e.getAutor().getPlayer());
		MensagensAPI.mensagemSucesso("Você foi removido da equipe do servidor", e.getJogador().getPlayer());
	}
	
	@EventHandler
	void aoAlterarStaff(UsuarioAlteradoStaffEvent e) {
		MensagensAPI.mensagemSucesso("Você definiu o cargo "+WalkerEngine.get().getGruposManager().getNomeGrupo(e.getGrupoNovo())+" ao jogador "+e.getJogador().getNickOriginal(), e.getAutor().getPlayer());
		MensagensAPI.mensagemSucesso("Seu cargo na equipe do servidor foi alterado. :D", e.getJogador().getPlayer());
	}
	
	
	
	
	
	
	/*public void removeStaff(Usuario usuario, Servers server, Usuario autor) {
		if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar") || !WalkerEngine.get().getGruposManager().isSuperior(autor, usuario))) return;
		if (server.getTipo().equals(ServerType.Lobby)) {
			if (autor instanceof Usuario && !autor.hasPermission("walker.staff.setar.gerente")) return;
			WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(usuario.getNickOriginal());
		}else if (isStaffInServer(usuario.getNickOriginal(), server)) {
			WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(server, usuario.getNickOriginal());
		}
		usuario.atualizarGrupo();
	}
	
	public boolean setStaff(Usuario usuario, GrupoType cargo, Servers server, Usuario autor) {
		if (!cargo.getTipo().equals(CargoType.Staff) || cargo.equals(GrupoType.Staff)) return false;
		if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar") || !WalkerEngine.get().getGruposManager().isSuperior(autor, usuario))) return false;
		if (server.getTipo().equals(ServerType.Lobby)) {
			if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar.gerente") || cargo.getServer().equals(CargoServerType.Server))) return false;
			Staff staff = WalkerEngine.get().getStaffManager().getStaffInServer(usuario,Servers.Rede);
			if (staff instanceof Staff) {
				WalkerEngine.get().getConexaoManager().getStaffConnection().modifyStaff(staff.getId(), , cargo);
			}
			else WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), cargo, Servers.Rede);
		}else {
			Staff staff = WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
			if (staff instanceof Staff) staff.setGrupo(cargo);
			else WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), cargo, server);
		}
		usuario.atualizarGrupo();
		return true;
	}*/
	
	public HashMap<Integer, Staff> getAllStaffs() {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getAllStaffs();
	}
	
	public HashMap<Integer, Staff> getAllStaffsServer(String server) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getAllStaffsServer(server);
	}
	
	/*public boolean isStaffInServer(String nick, Servers server) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().isStaffServer(nick, Servers.Rede) || WalkerEngine.get().getConexaoManager().getStaffConnection().isStaffServer(nick, server);
	}
	
	public Staff getStaffInServer(Usuario usuario, Servers server) {
		Staff s = WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), Servers.Rede);
		if (s instanceof Staff) return s;
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
		
	}*/
	
	public boolean isStaff(String nick) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().isStaff(nick);
	}
	
}
