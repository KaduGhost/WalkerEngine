package br.com.redewalker.kaduzin.engine.sistema.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.configuracao.ConfigChat;
import br.com.redewalker.kaduzin.engine.sistema.tag.Tag;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils;

public class ChatManager implements Listener {
	
	private File file, filetags;
	private HashMap<String, Canal> canais;
	private HashMap<String, CanalTag> tags;
	private ConfigChat config;
	
	public ChatManager(ConfigChat config) {
		this.config = config;
		file = new File(WalkerEngine.get().getDataFolder(), "chats");
		filetags = new File(WalkerEngine.get().getDataFolder(), "chattags");
		canais = new HashMap<String, Canal>();
		tags = new HashMap<String, CanalTag>();
		loadCanais();
		loadChatTags();
		Bukkit.getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	public void criarCanal(Canal canal) {
		if (!existsCanal(canal.getNome())) canais.put(canal.getNome().toLowerCase(), canal);
	}
	
	public void criarCanalPermanente(Canal canal) {
		criarCanal(canal);
		salvarCanal(canal);
	}
	
	public void criarChatTag(CanalTag tag) {
		tags.put(tag.getNome(), tag);
		salvarTag(tag);
	}
	
	public void salvarCanais() {
		for (String c : canais.keySet()) {
			salvarCanal(canais.get(c));
		}
		for (String c : tags.keySet()) {
			salvarTag(tags.get(c));
		}
	}
	
	private void salvarCanal(Canal canal) {
		File channel = new File(WalkerEngine.get().getDataFolder(),"chats"+File.separator+canal.getNome().toLowerCase()+".yml");
		if(!channel.exists()) {
			try {channel.createNewFile();} catch(Exception e) {}
		}
		YamlConfiguration channel2 = YamlConfiguration.loadConfiguration(channel);
		channel2.set("nome", canal.getNome().toLowerCase());
		channel2.set("nick", canal.getNick().toLowerCase());
		channel2.set("formato", canal.getFormato().toLowerCase());
		channel2.set("cor", WalkersUtils.replaceColorByString(canal.getTag().getCor().toString()));
		channel2.set("ativo", canal.isAtivo());
		channel2.set("tag", canal.getTag().getTag());
		channel2.set("custo", canal.getCusto());
		channel2.set("delay", canal.getDelay());
		channel2.set("distancia", canal.getDistancia());
		channel2.set("focus", canal.isFocus());
		channel2.set("entreMundos", canal.isEntreMundos());
		channel2.set("mostrarMsgCusto", canal.isMostrarMsgCusto());
		channel2.set("mundos", canal.getMundos());
		try {channel2.save(channel);} catch (Exception e) {}
	}
	
	public void deleteCanal(Canal c) {
		if(existsCanal(c.getNome())) {
			canais.remove(c.getNome().toLowerCase());
			new File(WalkerEngine.get().getDataFolder(),"chats"+File.separator+c.getNome().toLowerCase()+".yml").delete();
		}
	}
	
	public Canal getCanal(String nome) {
		if (existsCanal(nome)) return canais.get(nome.toString());
		return null;
	}
	
	public Canal getCanalByNick(String nome) {
		for (String c : canais.keySet()) {
			Canal canal = getCanal(c);
			if (canal.getNick().equalsIgnoreCase(nome)) return canal;
		}
		return null;
	}
	
	public CanalTag getChatTag(String nome) {
		if (existsTag(nome)) return tags.get(nome.toString());
		return null;
	}
	
	public boolean existsCanal(String nome) {
		return canais.containsKey(nome.toLowerCase());
	}
	
	public boolean existsTag(String nome) {
		return tags.containsKey("{"+(nome.toLowerCase().replace("{", "").replace("}", ""))+"}");
	}
	
	public Canal getChatDefault() {
		String canalDefault = config.getCanalDefault();
		if (canais.containsKey(canalDefault)) return canais.get(canalDefault);
		return null;
	}
	
	public void loadCanais() {
		canais.clear();
		if (config.isLoadDefaultCanais()) {
			criarCanalPermanente(new ChatPermanente("local", "l", "{chat} {grupo} {jogador}{chat_cor}: {msg}", true, true, true, false, new Tag("(L)", "§e"), 0, 5, 20, new ArrayList<String>(),false));
			criarCanalPermanente(new ChatPermanente("global", "g", "{chat} {grupo} {jogador}{chat_cor}: {msg}", true, true, true, false, new Tag("(G)", "§7"), 0, 5, 0, new ArrayList<String>(),false));
			criarCanalPermanente(new ChatPermanente("staff", "s", "{chat} {grupo} {jogador}{chat_cor}: {msg}", true, true, true, false, new Tag("(S)", "§d"), 0, 0, 0, new ArrayList<String>(),false));
			criarCanalPermanente(new ChatPermanente("staffbungee", "sb", "{chat} {server}{grupo} {jogador}{chat_cor}: {msg}", true, true, true, false, new Tag("(S)", "§d"), 0, 0, 0, new ArrayList<String>(),false));
			config.setLoadDefaultCanais(false);
		}
		if (file.exists()) {
			for (File channel : file.listFiles()) {
				if(channel.getName().toLowerCase().endsWith(".yml")) {
			        if(!channel.getName().toLowerCase().equals(channel.getName())) channel.renameTo(new File(WalkerEngine.get().getDataFolder(),"chats"+File.separator+channel.getName().toLowerCase()));
			        if (!canais.containsKey(channel.getName().toLowerCase())) loadCanal(channel);
				}
		    }
		}
	}
	
	public void loadCanal(File channel) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(channel);
		criarCanalPermanente(new ChatPermanente(config.getString("nome"), config.getString("nick"), config.getString("formato"), config.getBoolean("ativo"), config.getBoolean("focus"), config.getBoolean("entreMundos"), config.getBoolean("mostrarMsgCusto"), new Tag(config.getString("tag"), config.getString("cor").replace("&", "§")), config.getInt("custo"), config.getInt("delay"), config.getInt("distancia"), (ArrayList<String>) config.getStringList("mundos"), config.getBoolean("entreMundos")));
	}
	
	public void loadChatTags() {
		tags.clear();
		if (config.isLoadDefaultChatTags()) {
			CanalTag jogador = new ChatTag("%jogador_nick%", "%grupo_cor%", "jogador");
			jogador.setHover("walker.membro", new ArrayList<String>(Arrays.asList("§eGrupo: §f%grupo_tag%")));
			jogador.setHoverRecebedor("walker.chattag.cash", new ArrayList<String>(Arrays.asList("§eCash: §f%jogador_cash%")));
			criarChatTag(jogador);
			criarChatTag(new ChatTag("%grupo_tag%", "%grupo_cor%", "grupo"));
			criarChatTag(new ChatTag("[%server_nome%] ", "§5", "server"));
			criarChatTag(new ChatTag("%grupoin_tag%", "%grupoout_cor%", "grupoin"));
			criarChatTag(new ChatTag("%grupoout_tag%", "%grupoout_cor%", "grupoout"));
			criarChatTag(new ChatTag("%chat_tag%", "%chat_tag_cor%", "chat"));
			config.setLoadDefaultChatTags(false);
		}
		if (filetags.exists()) {
			for (File channel : filetags.listFiles()) {
				if(channel.getName().toLowerCase().endsWith(".yml")) {
			        if(!channel.getName().toLowerCase().equals(channel.getName())) channel.renameTo(new File(WalkerEngine.get().getDataFolder(),"chattags"+File.separator+channel.getName().toLowerCase()));
			        if (!tags.containsKey("{"+channel.getName().toLowerCase().replace(".yml", "")+"}")) loadChatTag(channel);
				}
		    }
		}
	}
	
	public void setChatTagCustom(String tag, CanalTag novo) {
		if (tags.containsKey(novo.getNome())) {
			if (filetags.exists()) {
				for (File channel : filetags.listFiles()) {
					if(channel.getName().toLowerCase().endsWith(".yml")) {
				        if(!channel.getName().toLowerCase().equals(channel.getName())) channel.renameTo(new File(WalkerEngine.get().getDataFolder(),"chattags"+File.separator+channel.getName().toLowerCase()));
				        if (channel.getName().toLowerCase().replace(".yml", "").replace("{", "").replace("}", "").equalsIgnoreCase(tag)) {
				        	loadChatTag(channel, novo);
				        }
					}
			    }
			}
		}else tags.put(novo.getNome(), novo);
	}
	
	public void loadChatTag(File tag, CanalTag tagg) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(tag);
		tagg.setTag(config.getString("tag"));
		tagg.setCor(config.getString("corTag"));
		tagg.setNome(config.getString("nome"));
		tagg.setUrl(null);
		tagg.removeAllHovers();
		tagg.setItem(null);
		String onClick = config.getString("tipo.onClick");
		if (onClick.equalsIgnoreCase("execute")) tagg.setExecuteComando(WalkersUtils.replaceStringByColor(config.getString("onClick")));
		else if (onClick.equalsIgnoreCase("sugestao")) tagg.setSugestao(WalkersUtils.replaceStringByColor(config.getString("onClick")));
		else if (onClick.equalsIgnoreCase("url")) tagg.setUrl(WalkersUtils.replaceStringByColor(config.getString("onClick")));
		String onHover = config.getString("tipo.onHover");
		if (onHover.equalsIgnoreCase("hover")) {
			ConfigurationSection section = config.getConfigurationSection("onHover");
			if (section != null) {
				for (String s : config.getConfigurationSection("onHover").getKeys(false)) {
					if(config.contains("onHover."+s.replace(".", "-")+".modelo")) tagg.setHover(s.replace("-", "."), WalkersUtils.replaceStringByColor(config.getStringList("onHover."+s.replace(".", "-")+".modelo")));
					else if (config.contains("onHover."+s.replace(".", "-")+".modelo-recebedor")) tagg.setHoverRecebedor(s.replace("-", "."), WalkersUtils.replaceStringByColor(config.getStringList("onHover."+s.replace(".", "-")+".modelo-recebedor")));
				}
			}
		}
		tags.put(tagg.getNome(), tagg);
		
	}
	
	public void loadChatTag(File tag) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(tag);
		CanalTag tagg = new ChatTag(config.getString("tag"), config.getString("corTag"), config.getString("nome"));
		String onClick = config.getString("tipo.onClick");
		if (onClick.equalsIgnoreCase("execute")) tagg.setExecuteComando(WalkersUtils.replaceStringByColor(config.getString("onClick")));
		else if (onClick.equalsIgnoreCase("sugestao")) tagg.setSugestao(WalkersUtils.replaceStringByColor(config.getString("onClick")));
		else if (onClick.equalsIgnoreCase("url")) tagg.setUrl(WalkersUtils.replaceStringByColor(config.getString("onClick")));
		String onHover = config.getString("tipo.onHover");
		if (onHover.equalsIgnoreCase("hover")) {
			ConfigurationSection section = config.getConfigurationSection("onHover");
			if (section != null) {
				for (String s : config.getConfigurationSection("onHover").getKeys(false)) {
					if(config.contains("onHover."+s.replace(".", "-")+".modelo")) tagg.setHover(s.replace("-", "."), WalkersUtils.replaceStringByColor(config.getStringList("onHover."+s.replace(".", "-")+".modelo")));
					else if (config.contains("onHover."+s.replace(".", "-")+".modelo-recebedor")) tagg.setHoverRecebedor(s.replace("-", "."), WalkersUtils.replaceStringByColor(config.getStringList("onHover."+s.replace(".", "-")+".modelo-recebedor")));
				}
			}
		}
		tags.put(tagg.getNome(), tagg);
		
	}
	
	private void salvarTag(CanalTag tag) {
		File channel = new File(WalkerEngine.get().getDataFolder(),"chattags"+File.separator+tag.getNome().toLowerCase().replace("{", "").replace("}", "")+".yml");
		if(!channel.exists()) try {channel.createNewFile();} catch(Exception e) {}
		YamlConfiguration channel2 = YamlConfiguration.loadConfiguration(channel);
		channel2.set("nome", WalkersUtils.replaceColorByString(tag.getNome().toLowerCase()));
		channel2.set("tag", WalkersUtils.replaceColorByString(tag.getTag()));
		channel2.set("corTag", WalkersUtils.replaceColorByString(tag.getCor()));
		String tipoOnCLick = "nenhum";
		String onClick = "";
		if (tag.isExecuteComando()) {
			tipoOnCLick = "execute";
			onClick = WalkersUtils.replaceColorByString(tag.getExecute());
		}
		else if (tag.isSugestao()) {
			tipoOnCLick = "sugestao";
			onClick = WalkersUtils.replaceColorByString(tag.getSuggest());
		}
		else if (tag.isUrl()) {
			tipoOnCLick = "url";
			onClick = WalkersUtils.replaceColorByString(tag.getUrl());
		}
		channel2.set("tipo.onClick", tipoOnCLick);
		String tipoOnHover = "nenhum";
		if (tag.isHover()) {
			tipoOnHover = "hover";
			channel2.set("onHover", null);
			for (String permission : tag.getOrdemHovers()) {
				if (tag.containsHover(permission)) channel2.set("onHover."+permission.replace(".", "-")+".modelo", WalkersUtils.replaceColorByString(tag.getHover(permission)));
				else channel2.set("onHover."+permission.replace(".", "-")+".modelo-recebedor", WalkersUtils.replaceColorByString(tag.getHoverRecebedor(permission)));
			}
		}
		else if (tag.isItem()) tipoOnHover = "item";
		channel2.set("tipo.onHover", tipoOnHover);
		channel2.set("onClick", WalkersUtils.replaceColorByString(onClick));
		try {channel2.save(channel);} catch (Exception e) {}
	}
	
	public void enviarMensagem(Usuario j, String msg, Canal canal) {
		Player p = j.getPlayer();
		/*if (WalkerEngine.get().getPunicoesManager().isJogadorPunido(j.getNickOriginal())) {
			Punicao punicao = WalkerEngine.get().getPunicoesManager().getPunicaoAtiva(j.getNickOriginal(), PunicoesType.MUTE);
			if (punicao != null) {
				p.sendMessage(WalkerEngine.get().getPunicoesManager().getMsgPunido(punicao));
				return;
			}
		}*/
		if (!j.hasPermission("walker.chat.chat"+canal.getNome().toLowerCase())) {
			MensagensAPI.mensagemErro("Você não tem permissão para usar este canal", p);
			return;
		}
		/*int custo = canal.getCusto();
		if (custo > 0 && !EconomiaAPI.hasMoney(j.getNickOriginal(), custo, EconomiaType.COINS)) {
			MensagensAPI.mensagemErro("Você não tem money o suficiente para fazer isso", p);
			return;
		}*/
		if (canal != null && !canal.isAtivo()) {
			MensagensAPI.mensagemErro("Este canal está desativado",p);
			return;
		}
		if (canal.getDelay() != 0) {
			if (canal.isNoDelay(j) && !j.hasPermission("walker.chat.nodelay")) {
				MensagensAPI.mensagemErro("Aguarde "+canal.getDelayRestante(j)+" segundos para poder usar esse canal novamente",p);
				return;
			}
			else canal.addNoDelay(j);
		}
		msg = ChatProtecao.filtrarMensagem(j, msg, canal);
		if (msg == null) return; 
		/*if (custo > 0) {
			EconomiaAPI.removeMoney(j.getNickOriginal(), custo, EconomiaType.COINS);
			MensagensAPI.mensagemSucesso("Você pagou "+custo+" para enviar esta mensagem", p);
		}*/
		canal.sendMessage(j, msg);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	void aoFalar(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) return;
		String msg = e.getMessage();
		Player p = e.getPlayer();
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		e.setCancelled(true);
		Canal canal;
		if (!j.getChatFocus().equalsIgnoreCase("default")) canal = WalkerEngine.get().getChatManager().getCanal(j.getChatFocus());
		else canal = WalkerEngine.get().getChatManager().getChatDefault();
		if (!(canal instanceof Canal)) {
			j.setChatFocus("default");
			MensagensAPI.mensagemErro("Nenhum canal disponivel",p);
			return;
		}
		WalkerEngine.get().getChatManager().enviarMensagem(j, msg, canal);
	}
	
	@EventHandler
	void aoFalarPorComando(PlayerCommandPreprocessEvent e) {
		if (e.isCancelled()) return;
		String msg = e.getMessage();
		String comando = e.getMessage();
		if (msg.contains(" ")) comando = msg.split(" ")[0].replaceFirst("/", "");
		else comando = msg.replaceFirst("/", "");
		Canal canal = WalkerEngine.get().getChatManager().getCanalByNick(comando);
		if (canal != null) {
			e.setCancelled(true);
			msg = msg.replaceFirst("/"+comando, "");
			Player p = e.getPlayer();
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
			if (msg.replace(" ", "").equals("")) {
				MensagensAPI.mensagemErro("A mensagem não pode estar vazia",p);
				return;
			}
			if (msg.startsWith(" ")) msg = msg.substring(1);
			WalkerEngine.get().getChatManager().enviarMensagem(j, msg, canal);
		}
	}

}
