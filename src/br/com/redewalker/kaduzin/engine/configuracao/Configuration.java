package br.com.redewalker.kaduzin.engine.configuracao;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {

    private JavaPlugin plugin;
    private String name, path;
	private File file;
    private YamlConfiguration config;

    public Configuration(String name, JavaPlugin plugin) {
    	this.plugin = plugin;
    	this.name = name;
    	this.path = File.separator+"configuracao"+File.separator;
        load();
    }
    
    public Configuration(JavaPlugin plugin, String name) {
    	this.plugin = plugin;
    	this.name = name;
    	this.path = File.separator;
        load();
    }
    
    public Configuration(String name, String path, JavaPlugin plugin) {
    	this.plugin = plugin;
    	this.name = name;
    	this.path = File.separator+path+File.separator;
        load();
    }

    public void load(){
    	file = new File(plugin.getDataFolder()+path, name+".yml");
    	if (plugin.getResource(name+".yml") != null) plugin.saveResource(name+".yml", false);
		config = YamlConfiguration.loadConfiguration(file);
    }

    public void save(){
    	try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public YamlConfiguration get(){
        return this.config;
    }

    public File getFile(){
        return file;
    }
}
