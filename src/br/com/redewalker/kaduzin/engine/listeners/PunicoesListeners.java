package br.com.redewalker.kaduzin.engine.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.Punicao;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.PunicaoMotivoType;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.PunicoesType;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunicoesType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersInventarios;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

enum PunicoesOperacaoType {
	PUNIR,DESPUNIR,MOTIVO,TEMPO,TIPO,PROVA,FINAL;
}

public class PunicoesListeners implements Listener {
	
	private HashMap<String, PunicoesOperacaoType> operacao = new HashMap<String, PunicoesOperacaoType>();
	private HashMap<String, String> motivo = new HashMap<String, String>();
	private HashMap<String, PunicaoMotivoType> principal = new HashMap<String, PunicaoMotivoType>();
	private HashMap<String, Boolean> kick = new HashMap<String, Boolean>();
	
	@EventHandler
	void antesDeEntrar(AsyncPlayerPreLoginEvent e) {
		if (WalkerEngine.get().getPunicoesManager().isJogadorPunido(e.getName())) {
			Punicao punicao = WalkerEngine.get().getPunicoesManager().getPunicaoAtiva(e.getName(), PunicoesType.BAN);
			if (punicao != null) {
				e.disallow(Result.KICK_OTHER, WalkerEngine.get().getPunicoesManager().getMsgPunido(punicao));
				return;
			}
		}
	}
	
	@EventHandler
	void aoFala2r(AsyncPlayerChatEvent e) {
		if (WalkerEngine.get().getPunicoesManager().isJogadorPunido(e.getPlayer().getName())) {
			Punicao punicao = WalkerEngine.get().getPunicoesManager().getPunicaoAtiva(e.getPlayer().getName(), PunicoesType.MUTE);
			if (punicao != null) {
				e.getPlayer().sendMessage(WalkerEngine.get().getPunicoesManager().getMsgPunido(punicao));
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	void aoUsarComando(PlayerCommandPreprocessEvent e) {
		String msg = e.getMessage();
		String[] msgs = msg.split(" ");
		if (msgs.length != 0 && (msgs[0].equalsIgnoreCase("/g") || msgs[0].equalsIgnoreCase("/tell") || msgs[0].equalsIgnoreCase("/r"))) {
			if (WalkerEngine.get().getPunicoesManager().isJogadorPunido(e.getPlayer().getName())) {
				Punicao punicao = WalkerEngine.get().getPunicoesManager().getPunicaoAtiva(e.getPlayer().getName(), PunicoesType.MUTE);
				if (punicao != null) {
					e.getPlayer().sendMessage(WalkerEngine.get().getPunicoesManager().getMsgPunido(punicao));
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	void aoClicar(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		Inventory inv = e.getInventory();
		InventarioPunicoesType tipo = getTipoInventario(inv.getName());
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
				WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.MENU, pagina, null);
				return;
			}
		}
		ClickType click = e.getClick();
		if (WalkersInventarios.isConfirmar(item)) {
			checkOperacao(WalkerEngine.get().getPunicoesManager().getPunicao(WalkersInventarios.getIDNaLore(meta.getLore())), item, click, e.getSlot(), inv, j.getNickOriginal(), tipo, pagina);
			return;
		}
		switch (tipo) {
		case MENU:
			if (item.equals(WalkersItens.getItemPunicao("punirjogador", null, null))) {
				operacao.put(j.getNickOriginal(), PunicoesOperacaoType.PUNIR);
				p.sendMessage("§aDigite o nome do jogador a qual deseja punir.");
				p.sendMessage("§aCaso deseje encerrar a operação digite 'exit'.");
				p.closeInventory();
				return;
			}else if (item.equals(WalkersItens.getItemPunicao("despunirjogador", null, null))) {
				operacao.put(j.getNickOriginal(), PunicoesOperacaoType.DESPUNIR);
				p.sendMessage("§aDigite o nome do jogador a qual deseja despunir.");
				p.sendMessage("§aCaso deseje encerrar a operação digite 'exit'.");
				p.closeInventory();
				return;
			}else if (item.equals(WalkersItens.getItemPunicao("listaavaliados", j.getNickOriginal(), null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.AVALIADOS, pagina, null);
			else if (item.equals(WalkersItens.getItemPunicao("listabanidos", null, null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.LISTABANIDOS, 1, null);
			else if (item.equals(WalkersItens.getItemPunicao("listamutados", null, null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.LISTAMUTES, 1, null);
			else if (item.equals(WalkersItens.getItemPunicao("historicobanidos", null, null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.HISTORICOBANIDOS, 1, null);
			else if (item.equals(WalkersItens.getItemPunicao("historicomutados", null, null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.HISTORICOMUTES, 1, null);
			else if (item.equals(WalkersItens.getItemPunicao("historicokicks", null, null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.HISTORICOKICKS, 1, null);
			else if (item.equals(WalkersItens.getItemPunicao("listameusbanidos", null, null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.MEUSBANS, 1, null);
			else if (item.equals(WalkersItens.getItemPunicao("listameusmutados", null, null))) WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.MEUSMUTES, 1, null);
			return;
		case PUNIR:
			String name = item.getItemMeta().getDisplayName();
			if (!name.contains("§e")) return;
			String j2 = inv.getItem(4).getItemMeta().getDisplayName().replace("§cPunir ", "");
			PunicaoMotivoType motivo = PunicaoMotivoType.OUTRO;
			boolean kick = false;
			int estagio = 1;
			PunicoesOperacaoType operacao = PunicoesOperacaoType.MOTIVO;
			if (item.getItemMeta().getDisplayName().equalsIgnoreCase(WalkersItens.getItemPunicao("kickarjogador", null, null).getItemMeta().getDisplayName())) {
				if (checkIsOnline(item, inv, j2, e.getSlot())) return;
				kick = true;
			}
			else {
				motivo = PunicaoMotivoType.getByDesc(item.getItemMeta().getDisplayName().replace("§e", ""));
				if (!motivo.equals(PunicaoMotivoType.OUTRO)) {
					estagio = 4;
					operacao = PunicoesOperacaoType.PROVA;
				}
			}
			continuarPunicao(j.getNickOriginal(),j2,estagio,operacao, motivo,false,kick);
			p.closeInventory();
			return;
		case DESPUNIR:
			j2 = inv.getItem(4).getItemMeta().getDisplayName().replace("§aDespunir ", "");
			if (e.getSlot() == 4) return;
			if (item.equals(WalkersItens.getItemPunicao("despunirban", null, null))) {
				WalkerEngine.get().getPunicoesManager().despunir(j2, PunicoesType.BAN, j.getNickOriginal());
				p.closeInventory();
				return;
			}else if (item.equals(WalkersItens.getItemPunicao("despunirmute", null, null))) {
				WalkerEngine.get().getPunicoesManager().despunir(j2, PunicoesType.MUTE, j.getNickOriginal());
				p.closeInventory();
				return;
			}
			return;
		case AVALIADOS:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null)) return;
			int id = WalkersInventarios.getIDNaLore(meta.getLore());
			Punicao punicao = WalkerEngine.get().getPunicoesManager().getPunicao(id);
			if (!(punicao instanceof Punicao)) return;
			if (click.equals(ClickType.LEFT)) {
				if (!j.hasPermission("walker.punir.avaliador")) return;
				checkOperacao(punicao, item, click, e.getSlot(), inv, j.getNickOriginal(), tipo, pagina);
			}else if (click.equals(ClickType.RIGHT)) checkOperacao(punicao, item, click, e.getSlot(), inv, j.getNickOriginal(), tipo, pagina);
			return;
		case LISTABANIDOS:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null)) return;
			id = WalkersInventarios.getIDNaLore(meta.getLore());
			punicao = WalkerEngine.get().getPunicoesManager().getPunicao(id);
			if (!(punicao instanceof Punicao)) return;
			if (click.equals(ClickType.RIGHT)) {
				if (j.hasPermission("walker.punir.avaliador") || j.getNickOriginal().equalsIgnoreCase(punicao.getAutor())) checkOperacao(punicao, item, click, e.getSlot(), inv, j.getNickOriginal(), tipo, pagina);
			}
			return;
		case LISTAMUTES:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null)) return;
			id = WalkersInventarios.getIDNaLore(meta.getLore());
			punicao = WalkerEngine.get().getPunicoesManager().getPunicao(id);
			if (!(punicao instanceof Punicao)) return;
			if (click.equals(ClickType.RIGHT)) {
				if (j.hasPermission("walker.punir.avaliador") || j.getNickOriginal().equalsIgnoreCase(punicao.getAutor())) checkOperacao(punicao, item, click, e.getSlot(), inv, j.getNickOriginal(), tipo, pagina);
			}
			return;
		case HISTORICOBANIDOS:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			checkTrocarPagina(p, item, pagina, tipo, null);
			return;
		case HISTORICOMUTES:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			checkTrocarPagina(p, item, pagina, tipo, null);
			return;
		case HISTORICOKICKS:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			checkTrocarPagina(p, item, pagina, tipo, null);
			return;
		case MEUSBANS:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null)) return;
			id = WalkersInventarios.getIDNaLore(meta.getLore());
			punicao = WalkerEngine.get().getPunicoesManager().getPunicao(id);
			if (!(punicao instanceof Punicao)) return;
			if (click.equals(ClickType.RIGHT)) checkOperacao(punicao, item, click, e.getSlot(), inv, j.getNickOriginal(), tipo, pagina);
			return;
		case MEUSMUTES:
			pagina = WalkersInventarios.getPagina(inv.getItem(49));
			if (checkTrocarPagina(p, item, pagina, tipo, null)) return;
			id = WalkersInventarios.getIDNaLore(meta.getLore());
			punicao = WalkerEngine.get().getPunicoesManager().getPunicao(id);
			if (!(punicao instanceof Punicao)) return;
			if (click.equals(ClickType.RIGHT)) checkOperacao(punicao, item, click, e.getSlot(), inv, j.getNickOriginal(), tipo, pagina);
			return;
		default:
			return;
		}
	}
	
	@EventHandler
	void aoComando(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if(operacao.containsKey(p.getName())) {
			operacao.remove(p.getName());
			motivo.remove(p.getName());
			principal.remove(p.getName());
			MensagensAPI.mensagemErro("Operação cancelada", p);
			return;
		}
	}
	
	@EventHandler
	void aoFalar(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		String msg = e.getMessage();
		if(operacao.containsKey(p.getName())) {
			e.setCancelled(true);
			PunicoesOperacaoType tipo = operacao.get(p.getName());
			operacao.remove(p.getName());
			switch (tipo) {
			case PUNIR:
				//User j2 = WalkerEngine.get().getUsuarioManager().getUsuario(msg);
				//if (!(j2 instanceof User)) {
					//cancelarOperacao(p);
					//return;
				//}
				WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.PUNIR, 1, msg);
				break;
			case DESPUNIR:
				//j2 = WalkerEngine.get().getUsuarioManager().getUsuario(msg);
				//if (!(j2 instanceof User)) {
					//cancelarOperacao(p);
					//return;
				//}
				WalkerEngine.get().getPunicoesManager().abrirInventario(p, InventarioPunicoesType.DESPUNIR, 1, msg);
				break;
			case MOTIVO:
				if (e.getMessage().equalsIgnoreCase("exit")) {
					cancelarOperacao(p);
					return;
				}
				if (kick.containsKey(p.getName())) continuarPunicao(j.getNickOriginal(), e.getMessage(), 4, PunicoesOperacaoType.PROVA, PunicaoMotivoType.OUTRO,false,true);
				else continuarPunicao(j.getNickOriginal(), e.getMessage(), 2, PunicoesOperacaoType.TEMPO, PunicaoMotivoType.OUTRO,false,false);
				break;
			case TEMPO:
				if (!NumberUtils.isNumber(msg) || msg.contains(".") || msg.contains(",")) {
					cancelarOperacao(p);
					return;
				}
				int numero = Integer.parseInt(msg);
				if (numero < 0) numero = 1;
				continuarPunicao(j.getNickOriginal(), e.getMessage(), 3, PunicoesOperacaoType.TIPO, PunicaoMotivoType.OUTRO,false,false);
				break;
			case TIPO:
				if (!e.getMessage().equalsIgnoreCase("ban") && !e.getMessage().equalsIgnoreCase("mute")) {
					cancelarOperacao(p);
					return;
				}
				continuarPunicao(j.getNickOriginal(), e.getMessage(), 4, PunicoesOperacaoType.PROVA, PunicaoMotivoType.OUTRO,false,false);
				break;
			case PROVA:
				String regexUrl = WalkerEngine.get().getConfigManager().getConfigPrincipal().getAntiIPRegexURL();
				if (!Pattern.compile(regexUrl).matcher(msg).find()) {
					cancelarOperacao(p);
					return;
				}
				if (kick.containsKey(p.getName())) continuarPunicao(j.getNickOriginal(), e.getMessage(), 5, PunicoesOperacaoType.FINAL, principal.get(j.getNickOriginal()),false,true);
				else continuarPunicao(j.getNickOriginal(), e.getMessage(), 5, PunicoesOperacaoType.FINAL, principal.get(j.getNickOriginal()),false,false);
				return;
			default:
				break;
			}
		}
	}
	
	private void cancelarOperacao(Player p) {
		MensagensAPI.mensagemErro("Operação cancelada", p);
		motivo.remove(p.getName());
		principal.remove(p.getName());
		kick.remove(p.getName());
	}
	
	private boolean checkIsOnline(ItemStack item, Inventory inv, String j2, int slot) {
		Player p2 = Bukkit.getPlayer(j2);
		if (!(p2 instanceof Player)) {
			ItemMeta mt = item.getItemMeta();
			List<String> lore = mt.getLore();
			if (lore == null) lore = new ArrayList<String>();
			if (!lore.contains(" §f§cUser não está online.")) {
				lore.add(" §f§cJogador não está online.");
				mt.setLore(lore);
				item.setItemMeta(mt);
				inv.setItem(slot, item);
			}
			return true;
		}
		return false;
	}
	
	private void checkOperacao(Punicao punicao, ItemStack itemClicado, ClickType tipoClick, int slot, Inventory inv, String j, InventarioPunicoesType tipo, int pagina) {
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
		case AVALIADOS:
			if (click.equals(ClickType.LEFT)) WalkerEngine.get().getPunicoesManager().setAvaliado(punicao.getID(), true, j);
			else if (click.equals(ClickType.RIGHT)) {
				if (!punicao.getAutor().equals(j)) WalkerEngine.get().getPunicoesManager().setAvaliado(punicao.getID(), false, j);
				else WalkerEngine.get().getPunicoesManager().despunir(punicao.getID(), j);
			}
			return;
		case LISTABANIDOS:
			if (click.equals(ClickType.RIGHT)) WalkerEngine.get().getPunicoesManager().despunir(punicao.getID(), j);
			return;
		case LISTAMUTES:
			if (click.equals(ClickType.RIGHT)) WalkerEngine.get().getPunicoesManager().despunir(punicao.getID(), j);
			return;
		case MEUSBANS:
			if (click.equals(ClickType.RIGHT)) WalkerEngine.get().getPunicoesManager().despunir(punicao.getID(), j);
			return;
		case MEUSMUTES:
			if (click.equals(ClickType.RIGHT)) WalkerEngine.get().getPunicoesManager().despunir(punicao.getID(), j);
			return;
		default:
			break;
		}
	}
	
	private boolean checkTrocarPagina(Player p, ItemStack item, int paginaatual, InventarioPunicoesType tipo, String j2) {
		if (item.equals(WalkersItens.getItem("proximapagina",null))) {
			WalkerEngine.get().getPunicoesManager().abrirInventario(p, tipo, paginaatual+1, j2);
			return true;
		}
		else if (item.equals(WalkersItens.getItem("paginaanterior",null))) {
			WalkerEngine.get().getPunicoesManager().abrirInventario(p, tipo, paginaatual-1, j2);
			return true;
		}
		return false;
	}
	
	private void continuarPunicao(String j, String msg, int estagio, PunicoesOperacaoType operacao, PunicaoMotivoType motivo, boolean silence, boolean isKick) {
		Player p = Bukkit.getPlayer(j);
		Usuario u = WalkerEngine.get().getUsuarioManager().getUsuario(j);
		principal.put(j, motivo);
		if (isKick) kick.put(j, true);
		if (!silence) {
			if (this.motivo.containsKey(j)) this.motivo.put(j, this.motivo.get(j)+msg+"&:&");
			else this.motivo.put(j, msg+"&:&");
			this.operacao.put(j, operacao);
		}
		String[] argumentos = this.motivo.get(p.getName()).split("&:&");
		if (estagio != 5) p.sendMessage(" ");
		if (estagio == 1) {
			p.sendMessage("§aDigite o motivo da punição.");
			p.sendMessage("§aCaso deseje encerrar a operação digite 'exit'.");
		}else if (estagio == 2) {
			p.sendMessage("§aDigite o tempo em minutos ou digite 0 para tempo eterno.");
			p.sendMessage("§aCaso deseje encerrar a operação digite qualquer outra coisa.");
		}else if (estagio == 3) {
			p.sendMessage("§aDigite o tipo de punição 'ban' para banir e 'mute' para mutar.");
			p.sendMessage("§aCaso deseje encerrar a operação digite qualquer outra coisa.");
		}else if (estagio == 4) {
			PunicoesType punitipo = PunicoesType.getByNome(msg);
			int tempo = 0;
			if (punitipo == null) {
				if (isKick) {
					tempo = 0;
					punitipo = PunicoesType.KICK;
				}else {
					punitipo = principal.get(j).getPunicao();
					tempo = principal.get(j).getTempo();
				}
			}else tempo = Integer.parseInt(argumentos[2]);
			if (!WalkerEngine.get().getPunicoesManager().jogadorPodePunir(u, punitipo, tempo)) {
				MensagensAPI.mensagemErro("Operação cancelada, pois você colocou um tipo de punição ou tempo diferente ao que é permitido a você", p);
				this.motivo.remove(p.getName());
				principal.remove(p.getName());
				return;
			}
			if (u.hasPermission("walker.punir.avaliador")) {
				continuarPunicao(j, "sem", 5, operacao, motivo,false,false);
				return;
			}
			p.sendMessage("§aDigite uma url com print ou video da prova.");
			p.sendMessage("§aCaso a punição não tenha provas, dificilmente ela será aprovada na avaliação.");
			p.sendMessage("§aCaso deseje encerrar a operação digite algo que não seja uma url.");
		}else {
			PunicaoMotivoType moti = principal.get(j);
			String comentario = "sem";
			int tempo = 0;
			String prova = "sem";
			PunicoesType tipo = moti.getPunicao();
			if (argumentos.length == 2) {
				tempo = WalkerEngine.get().getPunicoesManager().getTempo(moti);
				prova = argumentos[1];
			}else if (argumentos.length == 3) {
				comentario = argumentos[1];
				prova = argumentos[2];
				tipo = PunicoesType.KICK;
			}else {
				tempo = Integer.parseInt(argumentos[2]);
				prova = argumentos[4];
				tipo = PunicoesType.getByNome(argumentos[3]);
				comentario = argumentos[1];
			}
			WalkerEngine.get().getPunicoesManager().punirJogador(argumentos[0], moti, comentario, tempo, j, prova, tipo);
			this.motivo.remove(p.getName());
			principal.remove(p.getName());
			kick.remove(p.getName());
			this.operacao.remove(p.getName());
			return;
		}
		p.sendMessage(" ");
	}
	
	public static InventarioPunicoesType getTipoInventario(String nome) {
		if (nome.equals("§c§8Punir - Motivos§e")) return InventarioPunicoesType.PUNIR;
		else if (nome.equals("§c§8Punições - Walkers§e")) return InventarioPunicoesType.MENU;
		else if (nome.equals("§c§8Avaliação - Walkers§e")) return InventarioPunicoesType.AVALIADOS;
		else if (nome.equals("§c§8Punições - Banidos§e")) return InventarioPunicoesType.LISTABANIDOS;
		else if (nome.equals("§c§8Punições - Mutes§e")) return InventarioPunicoesType.LISTAMUTES;
		else if (nome.equals("§c§8Historico - Banidos§e")) return InventarioPunicoesType.HISTORICOBANIDOS;
		else if (nome.equals("§c§8Historico - Mutes§e")) return InventarioPunicoesType.HISTORICOMUTES;
		else if (nome.equals("§c§8Meus Bans§e")) return InventarioPunicoesType.MEUSBANS;
		else if (nome.equals("§c§8Meus Mutes§e")) return InventarioPunicoesType.MEUSMUTES;
		else if (nome.equals("§c§8Despunir - Tipos§e")) return InventarioPunicoesType.DESPUNIR;
		else if (nome.equals("§c§8Historico - Kicks§e")) return InventarioPunicoesType.HISTORICOKICKS;
		return null;
	}

}
