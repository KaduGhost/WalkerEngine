package br.com.redewalker.kaduzin.engine.sistema.vip.inventario;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.VIP;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InvTempoVip {
	
	public static void inventario(Player p, Usuario usuario, int pagina) {
		int[] slots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
		ArrayList<VIP> vips = WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsValidos(p.getName());
		int inicio = (pagina*28)-28;
		int fim = (pagina*28);
		int cont = 0;
		Inventory inv = Bukkit.createInventory(p, 54, "§3§8Vips - " + p.getName());
		if (vips.isEmpty()) inv.setItem(22, WalkersItens.getItemVIP("semvip", null));
		else {
			for (int i = inicio; i < fim ; i++) {
				if (i >= vips.size()) break;
				inv.setItem(slots[cont], WalkersItens.getItemVIP("vip", vips.get(i)));
				cont++;
			}
			if (fim < vips.size()) inv.setItem(50, WalkersItens.getItem("proximapagina",null));
			if (pagina != 1) inv.setItem(48, WalkersItens.getItem("paginaanterior",null));
			inv.setItem(49, WalkersItens.getPaginaAtual(pagina));
		}
		inv.setItem(53, WalkersItens.getItem("sair",null));
		p.openInventory(inv);
	}
	
	public static void inventarioRemove(Player p, Usuario wp, int pagina) {
		int[] slots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
		ArrayList<VIP> vips = WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsValidos(wp.getNickOriginal());
		int inicio = (pagina*28)-28;
		int fim = (pagina*28);
		int cont = 0;
		Inventory inv = Bukkit.createInventory(p, 54, "§3§8Remove - " + wp.getNickOriginal());
		if (vips.isEmpty()) inv.setItem(22, WalkersItens.getItemVIP("semvip", null));
		else {
			for (int i = inicio; i < fim ; i++) {
				if (i >= vips.size()) break;
				inv.setItem(slots[cont], WalkersItens.getItemVIP("vipr", vips.get(i)));
				cont++;
			}
			if (fim < vips.size()) inv.setItem(50, WalkersItens.getItem("proximapagina",null));
			if (pagina != 1) inv.setItem(48, WalkersItens.getItem("paginaanterior",null));
			inv.setItem(49, WalkersItens.getPaginaAtual(pagina));
		}
		inv.setItem(53, WalkersItens.getItem("sair",null));
		p.openInventory(inv);
	}
	
	public static void confirmarRemove(Player p, VIP id) {
		if (id == null) return;
		Inventory inv = Bukkit.createInventory(p, 36, "§7§8Remover VIP");
		inv.setItem(13, WalkersItens.getItemVIP("vip", id));
		inv.setItem(21, WalkersItens.getItem("confirmar", null));
		inv.setItem(23, WalkersItens.getItem("cancelar", null));
		p.openInventory(inv);
	}
	
	public static void inventarioMudar(Player p, Usuario wp, int pagina) {
		int[] slots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
		ArrayList<VIP> vips = WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsValidos(p.getName());
		int inicio = (pagina*28)-28;
		int fim = (pagina*28);
		int cont = 0;
		Inventory inv = Bukkit.createInventory(p, 54, "§4§8Vips - " + wp.getNickOriginal());
		if (vips.isEmpty()) inv.setItem(22, WalkersItens.getItemVIP("semvip", null));
		else {
			for (int i = inicio; i < fim ; i++) {
				if (i >= vips.size()) break;
				inv.setItem(slots[cont], WalkersItens.getItemVIP("vipm", vips.get(i)));
				cont++;
			}
			if (fim < vips.size()) inv.setItem(50, WalkersItens.getItem("proximapagina",null));
			if (pagina != 1) inv.setItem(48, WalkersItens.getItem("paginaanterior",null));
			inv.setItem(49, WalkersItens.getPaginaAtual(pagina));
		}
		inv.setItem(53, WalkersItens.getItem("sair",null));
		p.openInventory(inv);
	}
	
//	public static void inventarioVerKeys(Player p, int pagina) {
//		int[] slots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
//		ArrayList<String> vips = VipsMethods.getKeys();
//		int inicio = (pagina*28)-28;
//		int fim = (pagina*28);
//		int cont = 0;
//		Inventory inv = Bukkit.createInventory(p, 54, "§4§8Keys Vip");
//		if (vips.isEmpty()) {
//			inv.setItem(22, Items.semkey);
//		}
//		else {
//			for (int i = inicio; i < fim ; i++) {
//				if (i >= vips.size()) break;
//				inv.setItem(slots[cont], Items.key(vips.get(i)));
//				cont++;
//			}
//			if (fim < vips.size()) {
//				inv.setItem(50, Items.proxima);
//			}
//			if (pagina != 1) {
//				inv.setItem(48, Items.anterior);
//			}
//			inv.setItem(49, Items.pagina(pagina));
//		}
//		inv.setItem(53, Items.sair);
//		p.openInventory(inv);
//	}
//	
//	public static void confirmarUsarKey(Player p, String codigo) {
//		Inventory inv = Bukkit.createInventory(p, 36, "§7§8Usar Key");
//		inv.setItem(13, Items.keyr(codigo));
//		inv.setItem(21, Items.confirmar);
//		inv.setItem(23, Items.negar);
//		p.openInventory(inv);
//	}
//	
//	public static void confirmarApagarKey(Player p, String codigo) {
//		Inventory inv = Bukkit.createInventory(p, 36, "§7§8Apagar Key");
//		inv.setItem(13, Items.keyr(codigo));
//		inv.setItem(21, Items.confirmar);
//		inv.setItem(23, Items.negar);
//		p.openInventory(inv);
//	}
	
	public static void confirmarMudar(Player p, VIP id) {
		Inventory inv = Bukkit.createInventory(p, 36, "§7§8Trocar VIP");
		inv.setItem(13, WalkersItens.getItemVIP("vip", id));
		inv.setItem(21, WalkersItens.getItem("confirmar", null));
		inv.setItem(23, WalkersItens.getItem("cancelar", null));
		p.openInventory(inv);
	}

}
