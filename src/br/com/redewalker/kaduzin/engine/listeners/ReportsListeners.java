package br.com.redewalker.kaduzin.engine.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportPerfil;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.EsconderStringUtils;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersInventarios;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersTeleporte;

public class ReportsListeners implements Listener {
	
	@EventHandler
	void aoClicar(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		Inventory inv = e.getInventory();
		InventarioReportType tipo = getTipoInventario(inv.getName());
		if (tipo == null) return;
		e.setCancelled(true);
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || !e.getClickedInventory().equals(e.getInventory())) return;
		Player p = (Player) e.getWhoClicked();
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		ItemStack item = e.getCurrentItem();
		ItemMeta meta = item.getItemMeta();
		int pagina = 1;
		if (meta.hasDisplayName()) {
			if (item.equals(WalkersItens.getItem("sair",null))) {
				p.closeInventory();
				return;
			}else if (item.equals(WalkersItens.getItem("voltar",null))) {
				WalkerEngine.get().getReportManager().abrirInventario(p, InventarioReportType.MENU, 1, j.getNickOriginal());
				return;
			}
		}
		ClickType click = e.getClick();
		if (WalkersInventarios.isConfirmar(item)) {
			
		}
		switch (tipo) {
		case MENU:
			if (meta.getDisplayName().equals(WalkersItens.getItemReports("cabecareports",null,null).getItemMeta().getDisplayName())) WalkerEngine.get().getReportManager().abrirInventario(p, InventarioReportType.REPORTS, pagina, null);
			else if (item.equals(WalkersItens.getItemReports("emanalise",null,null))) WalkerEngine.get().getReportManager().abrirInventario(p, InventarioReportType.AVALIACOES, pagina, null);
			return;
		case AVALIACOES:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null)) return;
			String[] split = item.getItemMeta().getDisplayName().split(" ");
			if (click.equals(ClickType.LEFT)) WalkerEngine.get().getReportManager().abrirInventario(p, InventarioReportType.REPORTPERFIL, pagina, split[split.length-1]);
			return;
		case REPORTS:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null)) return;
			split = item.getItemMeta().getDisplayName().split(" ");
			if (click.equals(ClickType.LEFT)) WalkerEngine.get().getReportManager().abrirInventario(p, InventarioReportType.REPORTPERFIL, pagina, split[split.length-1]);
			return;
		case REPORTPERFIL:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			String j2 = EsconderStringUtils.extrairStringEscondida(inv.getItem(46).getItemMeta().getLore().get(0));
			if (checkTrocarPagina(p, item, pagina, tipo, j2)) return;
			if (meta.getDisplayName().equals(WalkersItens.getItemReports("analisar", null, j2).getItemMeta().getDisplayName())) {
				if (checkHasAnalise(WalkerEngine.get().getReportManager().getReportPerfil(j2), item, inv, p, e.getSlot())) return;
				WalkerEngine.get().getReportManager().analisar(j.getNickOriginal(), j2);
			}
			return;
		case AVALIACAO:
			if (item.equals(WalkersItens.getItemReports("finalizaravaliacao", null, null))) WalkerEngine.get().getReportManager().finalizarAnalise(j.getNickOriginal());
			else if (item.equals(WalkersItens.getItemReports("teleportaraojogador",null,null))) {
				Player teleportar = Bukkit.getPlayer(WalkerEngine.get().getReportManager().getAnalisando(j.getNickOriginal()).getJogador());
				if (teleportar instanceof Player) WalkersTeleporte.teleporteJogadorParaJogador(p, teleportar, false, false, true);
				else p.sendMessage("§cJogador não está online!");
			}
			else if (item.equals(WalkersItens.getItemReports("cancelaravaliacao",null,null))) WalkerEngine.get().getReportManager().cancelarAnalise(j.getNickOriginal());
			p.closeInventory();
			return;
		default:
			return;
		}
	}
	
	private boolean checkHasAnalise(ReportPerfil report, ItemStack item, Inventory inv, Player p, int slot) {
		if (report.isAnalisando()) {
			ItemMeta mt = item.getItemMeta();
			List<String> lore = mt.getLore();
			if (lore == null) lore = new ArrayList<String>();
			if (!lore.contains(" §f§cUser já está sendo analisado.")) {
				lore.add(" §f§cUser já está sendo analisado.");
				mt.setLore(lore);
				item.setItemMeta(mt);
				inv.setItem(slot, item);
			}
			return true;
		}
		return false;
	}
	
	private boolean checkTrocarPagina(Player p, ItemStack item, int paginaatual, InventarioReportType tipo, String j2) {
		if (item.equals(WalkersItens.getItem("proximapagina",null))) {
			WalkerEngine.get().getReportManager().abrirInventario(p, tipo, paginaatual+1, j2);
			return true;
		}
		else if (item.equals(WalkersItens.getItem("paginaanterior",null))) {
			WalkerEngine.get().getReportManager().abrirInventario(p, tipo, paginaatual-1, j2);
			return true;
		}
		return false;
	}
	
	public static Usuario getJogadorPeloNomeDoInventario(String name) {
		return WalkerEngine.get().getUsuarioManager().getUsuario(name.replace("§t§8Expirados ", "").replace( "§p§8Items ", "").replace( "§g§8Mercado ", ""));
	}
	
	private InventarioReportType getTipoInventario(String nome) {
		if (nome.contains("§c§8Reports - Menu§e")) return InventarioReportType.MENU;
		else if (nome.startsWith("§c§8Avaliação - Report§e")) return InventarioReportType.AVALIACAO;
		else if (nome.startsWith("§c§8Avaliações - Reports§e")) return InventarioReportType.AVALIACOES;
		else if (nome.startsWith("§c§8Reports - ")) return InventarioReportType.REPORTPERFIL;
		else if (nome.startsWith("§e§8Reports - Walkers§e")) return InventarioReportType.REPORTS;
		return null;
	}

}
