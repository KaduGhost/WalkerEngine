package br.com.redewalker.kaduzin.engine.configuracao;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
	
	private Config config;
	private ConfigChat chat;
	
	public ConfigManager(JavaPlugin plugin) {
		config = new Config(plugin);
		chat = new ConfigChat(plugin);
	}
	
	public ConfigChat getConfigChat() {
		return chat;
	}
	
	public Config getConfigPrincipal() {
		return config;
	}
	
}
