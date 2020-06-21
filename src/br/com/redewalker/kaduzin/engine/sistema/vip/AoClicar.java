package br.com.redewalker.kaduzin.engine.sistema.vip;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.inventario.InvTempoVip;

public class AoClicar implements Listener {
	
	@EventHandler
	public void aoClicar(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		if (e.getClickedInventory().getName().contains("§3§8Vips - ")) {
			if (e.getCurrentItem().getType().equals(Material.AIR)) return;
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			String[] inve = e.getClickedInventory().getName().split(" - ");
			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
			if (nam.equals("§cSair")) {
				p.closeInventory();
				return;
			}
			else if (nam.equals("§ePróxima Página")) {
				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				int pagina = Integer.parseInt(i[1]);
				Usuario wp = WalkerEngine.get().getUsuarioManager().getUsuario(inve[1]);
				InvTempoVip.inventario(p, wp, pagina+1);
				return;
			}
			else if (nam.equals("§ePágina Anterior")) {
				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				int pagina = Integer.parseInt(i[1]);
				Usuario wp = WalkerEngine.get().getUsuarioManager().getUsuario(inve[1]);
				InvTempoVip.inventario(p, wp, pagina-1);
				return;
			}
		}
		else if (e.getClickedInventory().getName().contains("§3§8Remove - ")) {
			if (e.getCurrentItem().getType().equals(Material.AIR)) return;
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			String[] inve = e.getClickedInventory().getName().split(" - ");
			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
			if (nam.equals("§cSair")) {
				p.closeInventory();
				return;
			}
			else if (nam.equals("§ePróxima Página")) {
				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				int pagina = Integer.parseInt(i[1]);
				Usuario wp = WalkerEngine.get().getUsuarioManager().getUsuario(inve[1]);
				InvTempoVip.inventarioRemove(p, wp, pagina+1);
				return;
			}
			else if (nam.equals("§ePágina Anterior")) {
				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				int pagina = Integer.parseInt(i[1]);
				Usuario wp = WalkerEngine.get().getUsuarioManager().getUsuario(inve[1]);
				InvTempoVip.inventarioRemove(p, wp, pagina-1);
				return;
			}
			if (e.getClick().equals(ClickType.RIGHT)) {
				if (nam.contains("§7ID: §f")) {
					String[] i = nam.split(": §f");
					VIP id = WalkerEngine.get().getVipManager().getVIP(Long.parseLong(i[1]));
					if (id == null) return;
					InvTempoVip.confirmarRemove(p, id);
				}
			}
		}
		else if (e.getClickedInventory().getName().equals("§7§8Remover VIP")) {
			if (e.getCurrentItem().getType().equals(Material.AIR)) return;
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
			if (nam.equals("§cCancelar")) {
				p.closeInventory();
				return;
			}
			else if (nam.equals("§aConfirmar")) {
				String nam1 = e.getClickedInventory().getItem(13).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				VIP id = WalkerEngine.get().getVipManager().getVIP(Long.parseLong(i[1]));
				if (id == null) return;
				p.sendMessage("§aVocê removeu o vip "+WalkerEngine.get().getGruposManager().getGrupo(id.getTipo()).getTag().toString()+" §ado jogador '"+id.getUsuario().getNickOriginal()+"'.");
				WalkerEngine.get().getVipManager().removerVip(id, p.getName());
				p.closeInventory();
				return;
			}
		}
		else if (e.getClickedInventory().getName().contains("§4§8Vips - ")) {
			if (e.getCurrentItem().getType().equals(Material.AIR)) return;
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			String[] inve = e.getClickedInventory().getName().split(" - ");
			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
			if (nam.equals("§cSair")) {
				p.closeInventory();
				return;
			}
			else if (nam.equals("§ePróxima Página")) {
				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				int pagina = Integer.parseInt(i[1]);
				Usuario wp = WalkerEngine.get().getUsuarioManager().getUsuario(inve[1]);
				InvTempoVip.inventarioMudar(p, wp, pagina+1);
				return;
			}
			else if (nam.equals("§ePágina Anterior")) {
				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				int pagina = Integer.parseInt(i[1]);
				Usuario wp = WalkerEngine.get().getUsuarioManager().getUsuario(inve[1]);
				InvTempoVip.inventarioMudar(p, wp, pagina-1);
				return;
			}
			if (e.getClick().equals(ClickType.RIGHT)) {
				if (nam.contains("§7ID: §f")) {
					String[] i = nam.split(": §f");
					VIP id = WalkerEngine.get().getVipManager().getVIP(Long.parseLong(i[1]));
					if (id == null) return;
					if (id.isAtivo()) return;
					VIP atual = WalkerEngine.get().getVipManager().getVIPAtivo(id.getUsuario().getNickOriginal(), id.getServer());
					if (atual instanceof VIP && !id.isPermanente()) {
						p.closeInventory();
						MensagensAPI.mensagemErro("Aguarde seu VIP temporario acabar para ativar outro", p);
						return;
					}
					InvTempoVip.confirmarMudar(p, id);
					return;
				}
			}
		}
		else if (e.getClickedInventory().getName().equals("§7§8Trocar VIP")) {
			if (e.getCurrentItem().getType().equals(Material.AIR)) return;
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
			if (nam.equals("§cCancelar")) {
				p.closeInventory();
				return;
			}
			else if (nam.equals("§aConfirmar")) {
				String nam1 = e.getClickedInventory().getItem(13).getItemMeta().getDisplayName();
				String[] i = nam1.split(": §f");
				VIP id = WalkerEngine.get().getVipManager().getVIP(Long.parseLong(i[1]));
				if (id == null) return;
				WalkerEngine.get().getVipManager().ativarVIP(id);
				p.closeInventory();
				return;
			}
		}
//		else if (e.getClickedInventory().getName().equals("§4§8Keys Vip")) {
//			if (e.getCurrentItem().getType().equals(Material.AIR)) {
//				return;
//			}
//			Player p = (Player) e.getWhoClicked();
//			e.setCancelled(true);
//			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
//			if (nam.equals("§cSair")) {
//				p.closeInventory();
//				return;
//			}
//			else if (nam.equals("§ePróxima Página")) {
//				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
//				String[] i = nam1.split(": §f");
//				int pagina = Integer.parseInt(i[1]);
//				InvTempoVip.inventarioVerKeys(p, pagina+1);
//				return;
//			}
//			else if (nam.equals("§ePágina Anterior")) {
//				String nam1 = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName();
//				String[] i = nam1.split(": §f");
//				int pagina = Integer.parseInt(i[1]);
//				InvTempoVip.inventarioVerKeys(p, pagina-1);
//				return;
//			}
//			else if (nam.contains("§7Key: §f")) {
//				String[] i = nam.split(": §f");
//				if (e.getClick().equals(ClickType.RIGHT)) {
//					InvTempoVip.confirmarApagarKey(p, i[1]);
//				}
//				else if (e.getClick().equals(ClickType.LEFT)) {
//					InvTempoVip.confirmarUsarKey(p, i[1]);
//				}
//				return;
//			}
//		}
//		else if (e.getClickedInventory().getName().equals("§7§8Usar Key")) {
//			if (e.getCurrentItem().getType().equals(Material.AIR)) {
//				return;
//			}
//			Player p = (Player) e.getWhoClicked();
//			e.setCancelled(true);
//			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
//			if (nam.equals("§cCancelar")) {
//				p.closeInventory();
//				return;
//			}
//			else if (nam.equals("§aConfirmar")) {
//				String nam1 = e.getClickedInventory().getItem(13).getItemMeta().getDisplayName();
//				String[] i = nam1.split(": §f");
//				p.performCommand("usarkey "+i[1]);
//				p.closeInventory();
//				return;
//			}
//		}
//		else if (e.getClickedInventory().getName().equals("§7§8Apagar Key")) {
//			if (e.getCurrentItem().getType().equals(Material.AIR)) {
//				return;
//			}
//			Player p = (Player) e.getWhoClicked();
//			e.setCancelled(true);
//			String nam = e.getCurrentItem().getItemMeta().getDisplayName();
//			if (nam.equals("§cCancelar")) {
//				p.closeInventory();
//				return;
//			}
//			else if (nam.equals("§aConfirmar")) {
//				String nam1 = e.getClickedInventory().getItem(13).getItemMeta().getDisplayName();
//				String[] i = nam1.split(": §f");
//				VipsMethods.setKeyUsada(i[1]);
//				p.sendMessage("§aKey apagada.");
//				p.closeInventory();
//				return;
//			}
//		}
	}

}
