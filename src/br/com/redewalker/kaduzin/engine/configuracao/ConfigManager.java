package br.com.redewalker.kaduzin.engine.configuracao;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
	
	private Config config;
	private ConfigChat chat;
	private ConfigComando comando;
	
	public ConfigManager(JavaPlugin plugin) {
		config = new Config(plugin);
		chat = new ConfigChat(plugin);
		comando = new ConfigComando(plugin);
	}
	
	public ConfigComando getConfigComando() {
		return comando;
	}
	
	public ConfigChat getConfigChat() {
		return chat;
	}
	
	public Config getConfigPrincipal() {
		return config;
	}
	
}
