package br.com.redewalker.kaduzin.engine.sistema.comando.inventario;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioMenu {
	
	public InventarioMenu(Player p) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		Inventory inv  = Bukkit.createInventory(p, 9*6, "§8Comandos - Menu§7");
		inv.setItem(11, WalkersItens.getItem("cabecamenu", j.getNickOriginal()));
		inv.setItem(15, WalkersItens.getItemComandos("info", null));
		inv.setItem(38, WalkersItens.getItemComandos("configs", null));
		inv.setItem(39, WalkersItens.getItemComandos("listacomandos", null));
		inv.setItem(41, WalkersItens.getItemComandos("listapermitidos", null));
		inv.setItem(42, WalkersItens.getItemComandos("listabloqueados", null));
		inv.setItem(53, WalkersItens.getItem("sair",null));
		p.openInventory(inv);
	}

}
