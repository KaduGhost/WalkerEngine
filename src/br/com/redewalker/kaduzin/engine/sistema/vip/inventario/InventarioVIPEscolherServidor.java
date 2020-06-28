package br.com.redewalker.kaduzin.engine.sistema.vip.inventario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers.ServerType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioVIPEscolherServidor {
	
	public InventarioVIPEscolherServidor(int pagina, Player p, String tipo) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.vips.admin")) return;
		ArrayList<String> vips = new ArrayList<String>();
		for (Servers gp : EnumSet.allOf(Servers.class)) {
			if (!gp.getTipo().equals(ServerType.Lobby)) vips.add(gp.toString());
		}
		Collections.sort(vips, Collections.reverseOrder());
		int paginas = 0;
		if (vips.size()%7 == 0) paginas = vips.size()/7;
		else paginas = vips.size()/7+1;
		Inventory inv = Bukkit.createInventory(p, 9*4, "§a§8Escolher Servidor§a");
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int quantItens = vips.size()-i*7;
			for (int f = 10; f < 16; f++) {
				if (quantItens == f-10) break;
				inv.setItem(f, WalkersItens.getItemVIP("servidor", null, vips.get(i*7+(f-10)), null, null));
			}
		}
		inv.setItem(35, WalkersItens.getItem("voltar",null));
		inv.setItem(31, WalkersItens.getPaginaAtualEscondeID(pagina, tipo));
		if (pagina < paginas) inv.setItem(32, WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(30, WalkersItens.getItem("paginaanterior",null));
		p.openInventory(inv);
	}

}
