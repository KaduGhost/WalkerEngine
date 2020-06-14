package br.com.redewalker.kaduzin.engine.sistema.reports.inventarios;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioReportMenu {
	
	public InventarioReportMenu(Player p) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.reports")) return;
		Inventory inv = Bukkit.createInventory(p, 9*3, "§c§8Reports - Menu§e");
		inv.setItem(10, WalkersItens.getCabeca(j.getNickOriginal(), Arrays.asList(), p.getName()));
		inv.setItem(13, WalkersItens.getItemReports("cabecareports",null,null));
		inv.setItem(15, WalkersItens.getItemReports("emanalise",null,null));
		p.openInventory(inv);
	}

}
