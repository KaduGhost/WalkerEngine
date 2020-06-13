package br.com.redewalker.kaduzin.engine.sistema.punicoes.eventos;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.redewalker.kaduzin.engine.sistema.punicoes.Punicao;

public class JogadorPunidoEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    private String player;
    private Punicao punicao;

    public JogadorPunidoEvent(String p, Punicao punicao) {
        this.player = p;
        this.punicao = punicao;
    }
    
    public Punicao getPunicao() {
    	return punicao;
    }
    
    public String getJogador() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
