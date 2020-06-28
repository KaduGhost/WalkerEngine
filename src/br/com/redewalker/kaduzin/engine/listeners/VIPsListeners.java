package br.com.redewalker.kaduzin.engine.listeners;

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
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.VIP;
import br.com.redewalker.kaduzin.engine.sistema.vip.inventario.InventarioVipsType;
import br.com.redewalker.kaduzin.engine.utils.EsconderStringUtils;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersInventarios;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class VIPsListeners implements Listener {
	
	@EventHandler
	void aoClicar(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		Inventory inv = e.getInventory();
		InventarioVipsType tipo = getTipoInventario(inv.getName());
		if (tipo == null) return;
		e.setCancelled(true);
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || !e.getClickedInventory().equals(e.getInventory())) return;
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		ItemMeta meta = item.getItemMeta();
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		int pagina = 1;
		if (meta.hasDisplayName()) {
			if (item.equals(WalkersItens.getItem("sair",null))) {
				p.closeInventory();
				return;
			}else if (item.equals(WalkersItens.getItem("voltar",null))) {
				WalkerEngine.get().getVipManager().abrirInventario(p, InventarioVipsType.MENU, null, null, pagina, null, null);
				return;
			}
		}
		ClickType click = e.getClick();
		if (WalkersInventarios.isConfirmar(item)) {
			checkOperacao(WalkerEngine.get().getVipManager().getVIP(getID(item.getItemMeta().getLore().get(0))), item, click, e.getSlot(), inv, p, tipo, pagina);
			return;
		}
		switch (tipo) {
		case MENU:
			if (item.equals(WalkersItens.getItemVIP("listavips", null, null, tipo, null))) WalkerEngine.get().getVipManager().abrirInventario(p, InventarioVipsType.ESCOLHERSERVIDOR, null, null, 1, null, "servers");
			else if (item.equals(WalkersItens.getItemVIP("listavipsall", null, null, tipo, null))) WalkerEngine.get().getVipManager().abrirInventario(p, InventarioVipsType.LISTAVIPSSERVER, null, Servers.Rede, pagina, null, null);
			else if (item.equals(WalkersItens.getItemVIP("historicovips", null, null, tipo, null))) WalkerEngine.get().getVipManager().abrirInventario(p, InventarioVipsType.ESCOLHERSERVIDOR, null, null, 1, null, "historico");
			else if (item.equals(WalkersItens.getItemVIP("historicovipsall", null, null, tipo, null))) WalkerEngine.get().getVipManager().abrirInventario(p, InventarioVipsType.HISTORICOVIPS, null, Servers.Rede, pagina, null, null);
			return;
		case ESCOLHERSERVIDOR:
			pagina = WalkersInventarios.getPagina(inv.getItem(31));
			if (checkTrocarPagina(p, item, pagina, tipo, null, EsconderStringUtils.extrairStringEscondida(inv.getItem(31).getItemMeta().getLore().get(inv.getItem(31).getItemMeta().getLore().size()-1)), null, null)) return;
			String name = item.getItemMeta().getDisplayName();
			if (!name.contains("§e")) return;
			Servers server = Servers.getTipo(name.replace("§e", ""));
			String tp = EsconderStringUtils.extrairStringEscondida(inv.getItem(31).getItemMeta().getLore().get(inv.getItem(31).getItemMeta().getLore().size()-1));
			if (tp.equals("servers")) WalkerEngine.get().getVipManager().abrirInventario(p, InventarioVipsType.LISTAVIPSSERVER, null, server, 1, null, null);
			else WalkerEngine.get().getVipManager().abrirInventario(p, InventarioVipsType.HISTORICOVIPS, null, server, 1, null, null);
			return;
		case HISTORICOVIPS:
			server = Servers.getTipo(EsconderStringUtils.extrairStringEscondida(inv.getItem(49).getItemMeta().getLore().get(inv.getItem(49).getItemMeta().getLore().size()-1)));
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			checkTrocarPagina(p, item, pagina, tipo, null, null, server, null);
			return;
		case LISTAVIPSSERVER:
			server = Servers.getTipo(EsconderStringUtils.extrairStringEscondida(inv.getItem(49).getItemMeta().getLore().get(inv.getItem(49).getItemMeta().getLore().size()-1)));
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null, null, server, null)) return;
			name = item.getItemMeta().getDisplayName();
			if (!name.contains(" §c§e")) return;
			VIP vip = WalkerEngine.get().getVipManager().getVIP(Long.parseLong(EsconderStringUtils.extrairStringEscondida(item.getItemMeta().getLore().get(0))));
			if (vip == null) return;
			if (click.equals(ClickType.RIGHT)) checkOperacao(vip, item, click, e.getSlot(), inv, p, tipo, pagina);
			else if (click.equals(ClickType.LEFT)) checkOperacao(vip, item, click, e.getSlot(), inv, p, tipo, pagina);
			return;
		case LISTAVIPS:
			server = Servers.getTipo(EsconderStringUtils.extrairStringEscondida(inv.getItem(49).getItemMeta().getLore().get(inv.getItem(49).getItemMeta().getLore().size()-1)));
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			Usuario j2 = getJogadorPeloNomeDoInventario(inv.getName());
			if (checkTrocarPagina(p, item, pagina, tipo, j2, null, server, null)) return;
			name = item.getItemMeta().getDisplayName();
			if (!name.contains(" §c§e")) return;
			vip = WalkerEngine.get().getVipManager().getVIP(Long.parseLong(EsconderStringUtils.extrairStringEscondida(item.getItemMeta().getLore().get(0))));
			if (vip == null) return;
			if (click.equals(ClickType.RIGHT)) {
				if (j.hasPermission("walker.vips.admin")) checkOperacao(vip, item, click, e.getSlot(), inv, p, tipo, pagina);
			}
			else if (click.equals(ClickType.LEFT)) checkOperacao(vip, item, click, e.getSlot(), inv, p, tipo, pagina);
			return;
		default:
			return;
		}
	}
	
	private long getID(String nome) {
		try {
			return Long.parseLong(EsconderStringUtils.extrairStringEscondida(nome));
		} catch (Exception e) {}
		return -1;
	}
	
	private void checkOperacao(VIP vip, ItemStack itemClicado, ClickType tipoClick, int slot, Inventory inv, Player j, InventarioVipsType tipo, int pagina) {
		boolean confirm = false;
		ClickType click = null;
		if (WalkersInventarios.isConfirmar(itemClicado)) {
			confirm = true;
			click = WalkersInventarios.getTipoClick(itemClicado.getItemMeta().getLore());
		}
		if (!confirm) {
			WalkersInventarios.setConfirmar(itemClicado, inv, tipoClick, slot);
			return;
		}
		switch (tipo) {
		case LISTAVIPSSERVER:
			if (click.equals(ClickType.LEFT)) {
				if (!vip.isAtivo()) {
					VIP atual = WalkerEngine.get().getVipManager().getVIPAtivo(vip.getUsuario().getNickOriginal(), vip.getServer());
					if (atual instanceof VIP && !vip.isPermanente()) {
						MensagensAPI.mensagemErro("Aguarde o VIP temporario acabar para ativar outro", j);
						return;
					}
					WalkerEngine.get().getVipManager().ativarVIP(vip);
				}
				else WalkerEngine.get().getVipManager().desativarVIP(vip);
			}
			else if (click.equals(ClickType.RIGHT)) {
				WalkerEngine.get().getVipManager().removerVip(vip, j.getName());
				j.sendMessage("§aVocê removeu o vip "+WalkerEngine.get().getGruposManager().getGrupo(vip.getTipo()).getTag().toString()+" §ado jogador "+vip.getUsuario().getNickOriginal()+".");
			}
			j.closeInventory();
			return;
		case LISTAVIPS:
			if (click.equals(ClickType.LEFT)) {
				if (!vip.isAtivo()) {
					VIP atual = WalkerEngine.get().getVipManager().getVIPAtivo(vip.getUsuario().getNickOriginal(), vip.getServer());
					if (atual instanceof VIP && !vip.isPermanente()) {
						j.closeInventory();
						MensagensAPI.mensagemErro("Aguarde o VIP temporario acabar para ativar outro", j);
						return;
					}
					WalkerEngine.get().getVipManager().ativarVIP(vip);
				}
				else WalkerEngine.get().getVipManager().desativarVIP(vip);
			}
			else if (click.equals(ClickType.RIGHT)) {
				WalkerEngine.get().getVipManager().removerVip(vip, j.getName());
				j.sendMessage("§aVocê removeu o vip "+WalkerEngine.get().getGruposManager().getGrupo(vip.getTipo()).getTag().toString()+" §ado jogador "+vip.getUsuario().getNickOriginal()+".");
			}
			j.closeInventory();
			return;
		default:
			break;
		}
	}
	
	private boolean checkTrocarPagina(Player p, ItemStack item, int paginaatual, InventarioVipsType tipo, Usuario j2, String st, Servers server, VIP vip) {
		if (item.equals(WalkersItens.getItem("proximapagina",null))) {
			WalkerEngine.get().getVipManager().abrirInventario(p, tipo, vip, server, paginaatual+1, j2, st);
			return true;
		}
		else if (item.equals(WalkersItens.getItem("paginaanterior",null))) {
			WalkerEngine.get().getVipManager().abrirInventario(p, tipo, vip, server, paginaatual-1, j2, st);
			return true;
		}
		return false;
	}
	
	public static Usuario getJogadorPeloNomeDoInventario(String name) {
		return WalkerEngine.get().getUsuarioManager().getUsuario(name.replace("§b§8VIPs - ", ""));
	}
	
	private InventarioVipsType getTipoInventario(String nome) {
		if (nome.contains("§a§8Escolher Servidor§a")) return InventarioVipsType.ESCOLHERSERVIDOR;
		else if (nome.contains("§c§8VIPS - Walker§e")) return InventarioVipsType.MENU;
		else if (nome.startsWith("§b§8VIPs - ")) return InventarioVipsType.LISTAVIPS;
		else if (nome.startsWith("§c§8VIPs - ")) return InventarioVipsType.LISTAVIPSSERVER;
		else if (nome.startsWith("§a§8VIPs - ")) return InventarioVipsType.HISTORICOVIPS;
		return null;
	}

}
