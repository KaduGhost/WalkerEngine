package br.com.redewalker.kaduzin.engine.sistema.vip.inventario;

import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.VIP;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioVIPsLista {
	
	public InventarioVIPsLista(int pagina, Player p, Servers server, InventarioVipsType tipo) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.vips.admin")) return;
		ArrayList<VIP> vips = WalkerEngine.get().getVipManager().getVIPsValidos(server);
		Collections.sort(vips, Collections.reverseOrder());
		int paginas = 0;
		if (vips.size()%45 == 0) paginas = vips.size()/45;
		else paginas = vips.size()/45+1;
		Inventory inv = Bukkit.createInventory(p, 9*6, "§c§8VIPs - "+server.toString());
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int quantItens = vips.size()-i*45;
			for (int f = 0; f < 45; f++) {
				if (quantItens == f) break;
				inv.setItem(f, WalkersItens.getItemVIP("vip", vips.get(i*45+f), null, tipo, j));
			}
		}
		inv.setItem(53, WalkersItens.getItem("voltar",null));
		inv.setItem(49, WalkersItens.getPaginaAtualEscondeID(pagina, server.toString()));
		if (pagina < paginas) inv.setItem(50, WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(48, WalkersItens.getItem("paginaanterior",null));
		p.openInventory(inv);
	}
	
}