package br.com.redewalker.kaduzin.engine.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class WalkersUtils {
	
	public static boolean isStringEmpty(String st) {
		st=removeColorsOfString(st).replace(" ", "");
		System.out.println(st+"teste");
		return st.isEmpty();
	}
	
	public static boolean isEmailValido(String email) {
	      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	      return email.matches(regex);
	   }
	
	public static String traduzirBoolean(boolean traduz) {
		return traduz ? "§aSim" : "§cNão";
	}
	
	public static Sound tryLoadSound(String try__, String catch__) {
		try {
			return Sound.valueOf(try__.toUpperCase());
		} catch (Throwable e) {
			return Sound.valueOf(catch__.toUpperCase());
		}
	}
	
	public static boolean isSimilar(ItemStack first,ItemStack second){
		if(first == null && second == null) return true;
        if(first == null || second == null) return false;
        return first.getAmount() == second.getAmount() && first.isSimilar(second);
    }
	
	public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
		try {
			Enum.valueOf(enumClass, enumName);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}
	
	public static String formatedData(long tempo){
		Date now = new Date(tempo);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return format.format(now);
	}
	
	public static String gerarCodigoSimples(int tamanho){
	      Random r = new Random();
	      String[] caract ={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		  String b = "";
		  for (int i = 0; i < tamanho; i++){
		    int a = r.nextInt(caract.length);
			b += caract[a];
		  }
		  return b;
	   }
	
	public static WalkersItens getItens() {
		return new WalkersItens();
	}
	
	public static Map<Integer, Long> getSortDescByLong(Map<Integer, Long> unsortedMap) {
		List<Entry<Integer, Long>> list = new LinkedList<Map.Entry<Integer, Long>>(unsortedMap.entrySet());
		Collections.sort(list, new Comparator<Entry<Integer, Long>>() {
			public int compare(Map.Entry<Integer, Long> o2, Map.Entry<Integer, Long> o1) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		Map<Integer, Long> sortedMap = new LinkedHashMap<Integer, Long>();
		for (Entry<Integer, Long> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static void performCommand(Player to, CommandSender sender, String command) {
        Bukkit.getScheduler().runTask(WalkerEngine.get(), () -> {
            if (to == null || to.isOnline()) {
                WalkerEngine.get().getServer().dispatchCommand(sender, command);
            }
        });
    }
	
	public static ArrayList<String> replaceColorByString(ArrayList<String> rec) {
		ArrayList<String> arr = new ArrayList<String>();
		for (String i : rec) {
			arr.add(i.replace("§", "&"));
		}
		return arr;
	}
	
	public static ArrayList<String> replaceStringByColor(ArrayList<String> rec) {
		ArrayList<String> arr = new ArrayList<String>();
		for (String i : rec) {
			arr.add(i.replace("&", "§"));
		}
		return arr;
	}
	
	public static ArrayList<String> replaceStringByColor(List<String> list) {
		ArrayList<String> arr = new ArrayList<String>();
		for (String i : list) {
			arr.add(i.replace("&", "§"));
		}
		return arr;
	}
	
	public static String replaceColorByString(String rec) {
		return rec.replace("§", "&");
	}
	
	public static String replaceStringByColor(String rec) {
		return rec.replace("&", "§");
	}
	
	public static String removeColorsOfString(String rec) {
		for (ChatColor tipo : EnumSet.allOf(ChatColor.class)) {
			rec = rec.replace(tipo.toString(), "").replace(tipo.toString().replace("§", "&"), "");
		}
		return rec;
	}
	
	public static String getTempoRestante(Long tempoinicial, Long tempofinal) {
		return getTempoRestante((tempofinal - tempoinicial)/1000);
	}
	
	public static String getTempoRestante(long tempo) {
		int segundos = 0;
		int minutos = 0;
		int horas = 0;
		int dias = 0;
		String resul = "";
		if (tempo > 0) {
			for (boolean i = false; i == false; i = false) {
				if (tempo > 59) {
					tempo = tempo - 60;
					segundos+=60;
				}
				else {
					segundos+=tempo;
					tempo = 0;
					break;
				}
			}
			for (boolean i = false; i == false; i = false) {
				if (segundos > 59) {
					segundos-=60;
					minutos+=1;
				}
				else break;
			}
			for (boolean i = false; i == false; i = false) {
				if (minutos > 59) {
					minutos-=60;
					horas+=1;
				}
				else break;
			}
			for (boolean i = false; i == false; i = false) {
				if (horas > 23) {
					horas-=24;
					dias+=1;
				}
				else break;
			}
		}
		if (dias != 0) {
			if (dias == 1) resul = resul + dias + " dia, ";
			else resul = resul + dias + " dias, ";
		}
		if (horas != 0) resul = resul + horas + "h, ";
		if (minutos != 0) resul = resul + minutos + "m, ";
		resul = resul + segundos + "s ";
		return resul;
	}
	
	public static class WalkersItens {
		
		public static ItemStack getCabeca(String nome, List<String> lore, String skin) {
			ItemStack cabeIni = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
			SkullMeta meta = (SkullMeta)cabeIni.getItemMeta();
		    meta.setDisplayName(nome);
		    meta.setLore(lore);
		    meta.setOwner(skin);
		    cabeIni.setItemMeta(meta);
		    return cabeIni;
		}
		
		public static ItemStack getCabecaMercadoPessoal(String skin) {
			ItemStack cabeIni = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
			SkullMeta meta = (SkullMeta)cabeIni.getItemMeta();
		    meta.setDisplayName("§6Mercado pessoal");
		    meta.setLore(Arrays.asList("§7Clique para ver seu", "§7mercado pessoal."));
		    meta.setOwner(skin);
		    cabeIni.setItemMeta(meta);
		    return cabeIni;
		}
		
		public static boolean isItemPagina(ItemStack item) {
			return item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("§ePágina at§c§eual: §f");
		}
		
		public static ItemStack getPaginaAtual(Integer pagina) {
			ItemStack paginaatual = new ItemBuilder(Material.DIAMOND_SWORD).setName("§ePágina at§c§eual: §f" + pagina).addEnchantGlow(true).fazer();
			return paginaatual;
		}
		
		public static ItemStack getPaginaAtualEscondeID(Integer pagina, String str) {
			ItemStack paginaatual = new ItemBuilder(Material.DIAMOND_SWORD).setName("§ePágina at§c§eual: §f" + pagina).setLore(Arrays.asList(EsconderStringUtils.esconderString(str))).addEnchantGlow(true).fazer();
			return paginaatual;
		}
		
		public static ItemStack getItemComandos(String tipo, Usuario j) {
			switch (tipo) {
			case "cabecamenu":
				ItemStack cabeca = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
				SkullMeta mt = (SkullMeta)cabeca.getItemMeta();
				mt.setDisplayName("§e" + j.getNickOriginal());
				mt.setOwner(j.getNickOriginal());
				cabeca.setItemMeta(mt);
		    	return cabeca;
			case "registrados":
				ItemBuilder item = null;
				ArrayList<String> loree = new ArrayList<>();
				loree.addAll(Arrays.asList(" §7Quando ativo, apenas comandos", " §7registrados pela api podem ser usados."));
				if (WalkerEngine.get().getComandosManager().isSomenteRegistrados()) {
					item = new ItemBuilder(Material.STAINED_GLASS_PANE, 5);
					loree.addAll(Arrays.asList(" §fStatus§7: §aAtivado§7.",""," §7Clique para desativar."));
				}
				else {
					item = new ItemBuilder(Material.STAINED_GLASS_PANE, 14);
					loree.addAll(Arrays.asList(" §fStatus§7: §cDesativado§7.",""," §7Clique para ativar."));
				}
				return item.setName("§eSomente comandos registrados").setLore(loree).fazer();
			case "lista":
				item = null;
				loree = new ArrayList<>();
				loree.add(" §7Quando ativo, os comandos na lista");
				if (WalkerEngine.get().getComandosManager().isBloquear()) loree.add(" §7de bloqueados não poderão ser usados.");
				else loree.add(" §7de permitidos, apenas, poderão ser usados.");
				if (WalkerEngine.get().getComandosManager().isLista()) {
					item = new ItemBuilder(Material.STAINED_GLASS_PANE, 5);
					loree.addAll(Arrays.asList(" §fStatus§7: §aAtivado§7.",""," §7Clique para desativar."));
				}
				else {
					item = new ItemBuilder(Material.STAINED_GLASS_PANE, 14);
					loree.addAll(Arrays.asList(" §fStatus§7: §cDesativado§7.",""," §7Clique para ativar."));
				}
				loree.add(" §7Clique direito para mudar o tipo da lista.");
				return item.setName("§eLista de comandos").setLore(loree).fazer();
			case "info":
				return new ItemBuilder(Material.BOOK_AND_QUILL).setName("§eAjuda menu").setLore(Arrays.asList(" §7Neste menu você pode ver", " §7todas as informações de todos", " §7os comandos do servidor.")).fazer();
			case "configs":
				return new ItemBuilder(Material.ANVIL).setName("§eConfigurações").setLore(Arrays.asList(" §7Clique para ver e editar as", " §7configurações dos comandos", " §7disponiveis no servidor.")).fazer();
			case "configsicon":
				return new ItemBuilder(Material.ANVIL).setName("§eConfigurações").setLore(Arrays.asList(" §7Neste menu você pode alterar", " §7as configurações dos comandos", " §7disponiveis no servidor.")).fazer();
			case "listacomandos":
				return new ItemBuilder(Material.BOOK).setName("§eLista de comandos").setLore(Arrays.asList(" §7Clique para ver e editar", " §7todos os comandos", " §7disponiveis no servidor.")).fazer();
			case "listacomandosicon":
				return new ItemBuilder(Material.BOOK).setName("§eLista de comandos").setLore(Arrays.asList(" §7Neste menu você pode ver", " §7e editar todos os comandos", " §7disponiveis no servidor.")).fazer();
			case "listapermitidos":
				return new ItemBuilder(Material.PAPER).setName("§eLista de comandos permitidos").setLore(Arrays.asList(" §7Clique para ver e editar", " §7os comandos permitidos de", " §7serem usados no servidor.")).fazer();
			case "listapermitidosicon":
				return new ItemBuilder(Material.BOOK).setName("§eLista de comandos permitidos").setLore(Arrays.asList(" §7Neste menu você pode ver", " §7e editar todos os comandos permitidos", " §7disponiveis no servidor.")).fazer();
			case "listabloqueados":
				return new ItemBuilder(Material.PAPER).setName("§eLista de comandos bloqueados").setLore(Arrays.asList(" §7Clique para ver e editar", " §7os comandos bloqueados de", " §7serem usados no servidor.")).fazer();
			case "listabloqueadosicon":
				return new ItemBuilder(Material.BOOK).setName("§eLista de comandos bloqueados").setLore(Arrays.asList(" §7Neste menu você pode ver", " §7e editar todos os comandos", " §7 bloqueados no servidor.")).fazer();
			default:
				return null;
			}
		}
		
		public static ItemStack getItemEditItem(String tipo, int inte, String str) {
			switch (tipo) {
			case "lore":
				return new ItemBuilder(Material.PAPER).setName("§eLore "+inte).setLore(Arrays.asList(str, " ", " §aClique para editar lore", " §aClique direito para remover lore")).fazer();
			default:
				return null;
			}
		}
		
		public static ItemStack getItem(String tipo, String jogador) {
			switch (tipo) {
			case "cabecamenu":
				ItemStack cabeca = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
				SkullMeta mt = (SkullMeta)cabeca.getItemMeta();
				mt.setDisplayName("§e" + jogador);
				mt.setOwner(jogador);
				cabeca.setItemMeta(mt);
		    	return cabeca;
			case "sair":
				return new ItemBuilder(Material.ARROW).setName("§cSair").setLore(Arrays.asList("§cClique aqui para sair do menu.")).fazer();
			case "confirmar":
				return new ItemBuilder(Material.WOOL, 5).setName("§aConfirmar").fazer();
			case "cancelar":
				return new ItemBuilder(Material.WOOL, 14).setName("§cCancelar").fazer();
			case "voltar":
				return new ItemBuilder(Material.ARROW).setName("§cVoltar").setLore(Arrays.asList("§cClique aqui para voltar", "§cao menu anterior.")).fazer();
			case "voltaresconde":
				return new ItemBuilder(Material.ARROW).setName("§cVoltar").setLore(Arrays.asList("§cClique aqui para voltar", "§cao menu anterior.", EsconderStringUtils.esconderString(jogador))).fazer();
			case "proximapagina":
				return new ItemBuilder(Material.PAPER).setName("§ePróxima Página").fazer();
			case "paginaanterior":
				return new ItemBuilder(Material.PAPER).setName("§ePágina Anterior").fazer();
			case "echest":
				return new ItemBuilder(Material.ENDER_CHEST).setName("§eEnderChest").setLore(Arrays.asList("§7Clique aqui para abrir", "§7seu enderchest.")).fazer();
			case "echestoutro":
				return new ItemBuilder(Material.ENDER_CHEST).setName("§eEnderChest").setLore(Arrays.asList("§7Clique aqui para abrir", "§7o enderchest do jogador.")).fazer();
			default:
				return null;
			}
		}
		
	}
	
	public static class WalkersTeleporte {
		
		public static boolean teleporteJogadorParaJogador(Player p, Player loc, boolean silence, boolean safe, boolean instantaneo) {
			if (safe && !isSafeLocation(loc.getLocation())) return false;
			teleporte(p, loc.getLocation(), 5, silence, false, null, instantaneo);
			return true;
		}
		
		public static boolean teleporteJogadorParaLocal(Player p, Location loc, boolean silence, boolean safe, boolean title, String nome, boolean instantaneo) {
			if (safe && !isSafeLocation(loc)) return false;
			teleporte(p, loc, 5, silence, title, nome, instantaneo);
			return true;
		}
		
		public static boolean teleporteJogadorParaJogador(Player p, Player loc, int delay, boolean silence, boolean safe, boolean instantaneo) {
			if (safe && !isSafeLocation(loc.getLocation())) return false;
			teleporte(p, loc.getLocation(), delay, silence, false, null, instantaneo);
			return true;
		}
		
		public static boolean teleporteJogadorParaLocal(Player p, Location loc, int delay, boolean silence, boolean safe, boolean title, String nome, boolean instantaneo) {
			if (safe && !isSafeLocation(loc)) return false;
			teleporte(p, loc, delay, silence, title, nome, instantaneo);
			return true;
		}
		
		private static void sendMessageWait(Player p, boolean title, String nome, int tempo) {
			if (title) {
				TitleAPI.sendTitle(p, 5, 20*3, 5, "§a"+nome, "§eTeleportando em "+tempo+"s");
			}else MensagensAPI.mensagemSucesso("Teleportando em "+tempo+" segundos", p);
		}
		
		private static void sendMessageTeleporte(Player p, boolean title, String nome, boolean silence) {
			if (silence) return;
			if (title) {
				TitleAPI.sendTitle(p, 5, 20*3, 5, "§a"+nome, "§eTeleportado");
			}else MensagensAPI.mensagemSucesso("Teleportado", p);
		}
		
		private static void teleporte(Player p, Location loc, int tempo, boolean silence, boolean title, String nome, boolean instantaneo) {
			try{loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ());}catch (Exception e) {}
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
			long delay = 3;
			if (!instantaneo && !j.hasPermission("walker.teleporte.insta")) {
				sendMessageWait(p, false, nome, tempo);
				delay = 20*tempo;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					p.teleport(loc, TeleportCause.COMMAND);
					sendMessageTeleporte(p, title, nome, silence);
				}
			}.runTaskLater(WalkerEngine.get(), delay);
		}
		
		public static boolean isSafeLocation(Location location) {
	        Block feet = location.getBlock();
	        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
	            return false; // not transparent (will suffocate)
	        }
	        Block head = feet.getRelative(BlockFace.UP);
	        if (!head.getType().isTransparent()) {
	            return false; // not transparent (will suffocate)
	        }
	        Block ground = feet.getRelative(BlockFace.DOWN);
	        if (!ground.getType().isSolid()) {
	            return false; // not solid
	        }
	        return true;
	    }
	}
	
	public static class WalkersInventarios {
		
		public static int getPagina(ItemStack item) {
			return Integer.parseInt(removeColorsOfString(item.getItemMeta().getDisplayName().split(":")[1].replace(" ", "")));
		}
		
		public static boolean isConfirmar(ItemStack item) {
			if (item.hasItemMeta()) {
				ItemMeta mt = item.getItemMeta();
				if (mt.hasLore()) {
					List<String> list = mt.getLore();
					for (String s : list) {
						if (s.contains("§c§lClique para confirmar!")) return true;
					}
				}
			}
			return false;
		}
		
		public static ClickType getTipoClick(List<String> list) {
			for (String s : list) {
				if (s.contains("§c§lClique para confirmar!")) {
					String[] in2 = s.split("!");
					return ClickType.valueOf(EsconderStringUtils.extrairStringEscondida(in2[1]));
				}
			}
			return null;
		}
		
		public static void setConfirmar(ItemStack item, Inventory inv, ClickType tipoClick, int slot) {
			ItemMeta mt = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			if (mt.hasLore()) lore.addAll(mt.getLore());
			lore.add(" §c§lClique para confirmar!"+EsconderStringUtils.esconderString(tipoClick.toString()));
			mt.setLore(lore);
			item.setItemMeta(mt);
			inv.setItem(slot, item);
		}
		
		public static String getIDStringNaLore(List<String> list) {
			if (list == null || list.isEmpty()) return null;
			for (String s : list) {
				if (s.contains("§fID§7: ")) {
					String[] in2 = s.split(": ");
					return in2[1];
				}
			}
			return null;
		}
		
		public static int getIDNaLore(List<String> list) {
			if (list == null || list.isEmpty()) return -1;
			for (String s : list) {
				if (s.contains("§fID§7: ")) {
					String[] in2 = s.split(": ");
					return Integer.parseInt(in2[1]);
				}
			}
			return -1;
		}
	}
	
}
