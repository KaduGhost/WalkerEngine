package br.com.redewalker.kaduzin.engine.sistema.comando.inventario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.comando.ComandoInfos;
import br.com.redewalker.kaduzin.engine.utils.ItemBuilder;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioListaComandos {
	
	public InventarioListaComandos(int pagina, Player p) {
		List<ComandoInfos> comandos = WalkerEngine.get().getComandosManager().getAllComandos();
		int paginas = 0;
		if (comandos.size()%28 == 0) paginas = comandos.size()/28;
		else paginas = comandos.size()/28+1;
		Inventory inv  = Bukkit.createInventory(p, 9*6, "§8Comandos - Lista§7");
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int itens = comandos.size()-i*28;
			List<Integer> slots = getSlotsMenu();
			for (int f = 0; f < 28; f++) {
				if (itens == f) break;
				inv.setItem(slots.get(f), getItem(comandos.get(i*28+f)));
			}
		}
		inv.setItem(4, WalkersItens.getItemComandos("listacomandosicon", null));
		inv.setItem(49, WalkersItens.getPaginaAtual(pagina));
		if (pagina < paginas) inv.setItem(50, WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(48, WalkersItens.getItem("paginaanterior",null));
		inv.setItem(53, WalkersItens.getItem("voltar",null));
		p.openInventory(inv);
	}
	
	private ItemStack getItem(ComandoInfos info) {
		ArrayList<String> lore = new ArrayList<String>();
		if (info.isRegistrado()) lore.add(" §aEste comando é registrado na api.");
		else lore.add(" §cEste comando não é registrado na api.");
		lore.add(" §fPermissão§7: "+info.getPermission());
		if (info.getDelay() == 0) lore.add(" §aEste comando não tem delay.");
		else lore.add(" §fDelay§7: "+info.getDelay()+" segundos.");
		List<String> aliases = info.getAliases();
		if (aliases.isEmpty()) lore.add(" §aEste comando não tem aliases.");
		else {
			String add = " §fAliases§7: ";
			for (int i = 1; i <=aliases.size(); i++) {
				if (i == aliases.size()) add+=aliases.get(i-1)+".";
				else add+=aliases.get(i-1)+",";
				if (i%5 == 0) {
					lore.add(add);
					add = " §7";
				}
			}
			if (!add.equals(" §7")) lore.add(add);
		}
		lore.add("");
		if (WalkerEngine.get().getComandosManager().containsListaBloqueio(info.getNome())) lore.add(" §cClique para remover da lista de bloqueio");
		else lore.add(" §aClique para adicionar na lista de bloqueio");
		if (WalkerEngine.get().getComandosManager().containsListaPermitir(info.getNome())) lore.add(" §cClique direito para remover da lista de permitir");
		else lore.add(" §aClique direito para adicionar na lista de permitir");
		return new ItemBuilder(Material.PAPER).setName("§c§e"+info.getNome()).setLore(lore).fazer();
	}
	
	private List<Integer> getSlotsMenu() {
		return Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43);
	}

}
