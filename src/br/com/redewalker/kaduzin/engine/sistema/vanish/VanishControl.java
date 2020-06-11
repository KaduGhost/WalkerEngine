package br.com.redewalker.kaduzin.engine.sistema.vanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.VanishAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class VanishControl {
	
	public static void ativarVanish(Player p) {
		if (!(p instanceof Player)) return;
		esconderPlayer(p);
	}
	
	public static void desativarVanish(Player p) {
		if (!(p instanceof Player)) return;
		mostrarPlayer(p);
	}
	
	public static void preventHider(Player p) {
		if (!(p instanceof Player)) return;
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario)) return;
		if (j.isVanish()) esconderPlayer(p);
		else mostrarPlayer(p);
	}
	
	public static void preventHider(Player p, Player p2) {
		if (!(p instanceof Player) || !(p2 instanceof Player) || p.equals(p2)) return;
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario)) return;
		if (j.isVanish()) esconderPlayer(p,p2);
		else mostrarPlayer(p,p2);
	}
	
	private static void mostrarPlayer(Player p) {
		for (Player p2 : Bukkit.getOnlinePlayers()) {
			mostrarPlayer(p, p2);
		}
	}
	
	private static void mostrarPlayer(Player p, Player see) {
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(see.getName());
		if (!VanishAPI.usuarioCanSeeVanish(j2)) see.showPlayer(p);
		p.setSleepingIgnored(false);
	}
	
	private static void esconderPlayer(Player p) {
		for (Player p2 : Bukkit.getOnlinePlayers()) {
			esconderPlayer(p, p2);
		}
	}
	
	private static void esconderPlayer(Player p, Player see) {
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(see.getName());
		if (!VanishAPI.usuarioCanSeeVanish(j2)) see.hidePlayer(p);
		p.setSleepingIgnored(true);
	}

}
