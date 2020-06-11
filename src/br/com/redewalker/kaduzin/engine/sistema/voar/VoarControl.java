package br.com.redewalker.kaduzin.engine.sistema.voar;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class VoarControl {
	
	public static void ativarVoar(Player p) {
		if (!(p instanceof Player)) return;
		p.setAllowFlight(true);
	}
	
	public static void desativarVoar(Player p) {
		if (!(p instanceof Player)) return;
		if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) return;
		p.setAllowFlight(false);
	}

}
