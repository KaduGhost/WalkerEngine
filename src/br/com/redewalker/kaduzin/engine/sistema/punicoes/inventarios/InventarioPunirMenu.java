package br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioPunirMenu {
	
	public InventarioPunirMenu(Player p) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.punir")) return;
		Inventory inv = Bukkit.createInventory(p, 9*5, "§c§8Punições - Walkers§e");
		inv.setItem(3, WalkersItens.getItemPunicao("punirjogador", null, null));
		inv.setItem(4, WalkersItens.getCabeca(j.getNickOriginal(), Arrays.asList(), p.getName()));
		inv.setItem(5, WalkersItens.getItemPunicao("despunirjogador", null, null));
		inv.setItem(22, WalkersItens.getItemPunicao("listaavaliados", j.getNickOriginal(), null));
		inv.setItem(19, WalkersItens.getItemPunicao("listabanidos", null, null));
		inv.setItem(20, WalkersItens.getItemPunicao("listamutados", null, null));
		inv.setItem(24, WalkersItens.getItemPunicao("historicobanidos", null, null));
		inv.setItem(25, WalkersItens.getItemPunicao("historicomutados", null, null));
		inv.setItem(34, WalkersItens.getItemPunicao("historicokicks", null, null));
		inv.setItem(39, WalkersItens.getItemPunicao("listameusbanidos", null, null));
		inv.setItem(41, WalkersItens.getItemPunicao("listameusmutados", null, null));
		p.openInventory(inv);
	}

}
