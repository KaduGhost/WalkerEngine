package br.com.redewalker.kaduzin.engine.eventos;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class UsuarioRemovidoStaffEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    private Usuario player, player2;
    private GrupoType novo;

    public UsuarioRemovidoStaffEvent(Usuario p, GrupoType novo, Usuario p2) {
        this.player = p;
        this.player2 = p2;
        this.novo = novo;
    }
    
    public Usuario getAutor() {
    	return player2;
    }
    
    public GrupoType getGrupo() {
    	return novo;
    }

    public Usuario getJogador() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}