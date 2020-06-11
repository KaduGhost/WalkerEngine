package br.com.redewalker.kaduzin.engine.sistema.voar;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.configuracao.Config;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class VoarManager implements Listener {
	
	private ArrayList<Usuario> voadores;
	private ArrayList<Usuario> permitidos;
	private ArrayList<String> permi;
	private Config config;
	
	public VoarManager(Config config) {
		this.config = config;
		load();
		Bukkit.getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	private void load() {
		voadores = new ArrayList<Usuario>();
		permi = new ArrayList<String>();
		permitidos = new ArrayList<Usuario>();
		for (String m : config.getVoarMundos()) {
			 permi.add(m);
		}
	}
	
	public void mudarVoar(Usuario j, Usuario autor) {
		if (voadores.contains(j)) desativarVoar(j);
		else ativarVoar(j,autor);
	}
	
	public void verificarVoar(Usuario j) {
		if (voadores.contains(j)) VoarControl.ativarVoar(j.getPlayer());
		else VoarControl.desativarVoar(j.getPlayer());
	}
	
	private void ativarVoar(Usuario j, Usuario autor) {
		if (!isPodeVoar(j)) return;
		addVoador(j,autor);
		VoarControl.ativarVoar(j.getPlayer());
		MensagensAPI.mensagemSucesso("Voar ativado", j.getPlayer());
	}
	
	private void desativarVoar(Usuario j) {
		removeVoador(j);
		VoarControl.desativarVoar(j.getPlayer());
		MensagensAPI.mensagemSucesso("Voar desativado", j.getPlayer());
	}
	
	public boolean isPodeVoar(Usuario j) {
		if (j.hasPermission("walker.voar.bypass") || permitidos.contains(j)) return true;
		Player p = j.getPlayer();
		if (p instanceof Player) {
			if (!permi.contains(p.getLocation().getWorld().getName())) return false;
		}
		return true;
	}
	
	private void addVoador(Usuario j, Usuario autor) {
		if (!voadores.contains(j)) voadores.add(j);
		if (!j.equals(autor)) permitidos.add(j);
	}
	
	private void removeVoador(Usuario j) {
		voadores.remove(j);
		permitidos.remove(j);
	}
	
	@EventHandler
	void aoEntrar(PlayerLoginEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (!(j instanceof Usuario)) return;
		verificarVoar(j);
	}
	
	@EventHandler
	void aoSair(PlayerQuitEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (!(j instanceof Usuario)) return;
		VoarControl.desativarVoar(e.getPlayer());
	}
	
	@EventHandler
	void aoSeMexer(PlayerMoveEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (!(j instanceof Usuario)) return;
		verificarVoar(j);
	}
	
	@EventHandler
	void aoMudarMundo(PlayerChangedWorldEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (!(j instanceof Usuario)) return;
		verificarVoar(j);
	}

}
