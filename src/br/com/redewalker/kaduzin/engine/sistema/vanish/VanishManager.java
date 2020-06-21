package br.com.redewalker.kaduzin.engine.sistema.vanish;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vanish.evento.JogadorAtivouVanish;
import br.com.redewalker.kaduzin.engine.sistema.vanish.evento.JogadorDesativouVanish;
import br.com.redewalker.kaduzin.engine.sistema.vanish.evento.JogadorEntrouVanishado;
import br.com.redewalker.kaduzin.engine.sistema.vanish.evento.JogadorSaiuVanishado;

public class VanishManager implements Listener {
	
	private List<Usuario> vanishados;
	
	public VanishManager() {
		load();
		Bukkit.getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	public List<Usuario> getJogadoresVanishados() {
		List<Usuario> jogadores = new ArrayList<Usuario>();
		for (Usuario j : vanishados) {
			if (j.isOnline()) jogadores.add(j);
		}
		return jogadores;
	}
	
	public void changeVanish(Usuario j) {
		if (j.isVanish()) desativarVanish(j);
		else ativarVanish(j);
	}
	
	public void desativarVanish(Usuario j) {
		j.setVanish(false);
		VanishControl.desativarVanish(j.getPlayer());
		removeVanishado(j);
		Bukkit.getServer().getPluginManager().callEvent(new JogadorDesativouVanish(j));
	}
	
	public void ativarVanish(Usuario j) {
		j.setVanish(true);
		VanishControl.ativarVanish(j.getPlayer());
		addVanishado(j);
		Bukkit.getServer().getPluginManager().callEvent(new JogadorAtivouVanish(j));
	}
	
	private void addVanishado(Usuario j) {
		if (j.isVanish()) vanishados.add(j);
	}
	
	private void removeVanishado(Usuario j) {
		vanishados.remove(j);
	}
	
	private void load() {
		vanishados = new ArrayList<Usuario>();
		for (Usuario j : WalkerEngine.get().getUsuarioManager().getAllUsuarios().values()) {
			if (j.isVanish()) addVanishado(j);
			VanishControl.preventHider(j.getPlayer());
		}
	}
	
	/*@EventHandler
	void aoGanharPermission() {
		
	}*/
	
	@EventHandler
	void aoSair(PlayerQuitEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j instanceof Usuario && j.isVanish()) Bukkit.getServer().getPluginManager().callEvent(new JogadorSaiuVanishado(j));
	}
	
	@EventHandler
	void aoEntrar(PlayerJoinEvent e) {
		VanishControl.preventHider(e.getPlayer());
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j instanceof Usuario) {
			if (j.isVanish()) {
				addVanishado(j);
				Bukkit.getServer().getPluginManager().callEvent(new JogadorEntrouVanishado(j));
			}
			else removeVanishado(j);
		}
		for (Usuario j2 : getJogadoresVanishados()) {
			if (j2.isOnline()) VanishControl.preventHider(j2.getPlayer(), e.getPlayer());
		}
	}
	
	@EventHandler
	void antesEntrar(AsyncPlayerPreLoginEvent e) {
	}
	
	@EventHandler
	void aoMudardeMundo(PlayerChangedWorldEvent e) {
		VanishControl.preventHider(e.getPlayer());
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j instanceof Usuario) {
			if (j.isVanish()) addVanishado(j);
			else removeVanishado(j);
		}
		for (Usuario j2 : getJogadoresVanishados()) {
			if (j2.isOnline() && j2.isOnlineInServer()) VanishControl.preventHider(j2.getPlayer(), e.getPlayer());
		}
	}

}
