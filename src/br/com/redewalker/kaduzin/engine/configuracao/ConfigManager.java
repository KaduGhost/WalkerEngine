package br.com.redewalker.kaduzin.engine.configuracao;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
	
	private Config config;
	
	public ConfigManager(JavaPlugin plugin) {
		config = new Config(plugin);
	}
	
	public Config getConfigPrincipal() {
		return config;
	}
	
}
