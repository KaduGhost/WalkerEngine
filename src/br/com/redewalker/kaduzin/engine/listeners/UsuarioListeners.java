package br.com.redewalker.kaduzin.engine.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import br.com.redewalker.kaduzin.engine.WalkerEngine;

public class UsuarioListeners implements Listener {
	
	@EventHandler
	void usuarioEntrou(PlayerLoginEvent e) {
		WalkerEngine.get().getUsuarioManager().createUsuario(e.getPlayer());
	}

}
