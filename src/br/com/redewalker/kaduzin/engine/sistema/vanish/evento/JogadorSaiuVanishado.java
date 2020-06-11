package br.com.redewalker.kaduzin.engine.sistema.vanish.evento;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class JogadorSaiuVanishado extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Usuario jogador;
	
	public JogadorSaiuVanishado(Usuario jogador) {
		this.jogador = jogador;
	}
	
	public Usuario getJogador() {
		return jogador;
	}
	
	public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}