package br.com.redewalker.kaduzin.engine;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.redewalker.kaduzin.engine.conexao.ConexaoManager;
import br.com.redewalker.kaduzin.engine.configuracao.ConfigManager;
import br.com.redewalker.kaduzin.engine.listeners.UsuarioListeners;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoManager;
import br.com.redewalker.kaduzin.engine.sistema.server.ServerType;
import br.com.redewalker.kaduzin.engine.sistema.staff.StaffManager;
import br.com.redewalker.kaduzin.engine.sistema.usuario.UsuarioManager;

public class WalkerEngine extends JavaPlugin {
	
	private static WalkerEngine instance;
	private UsuarioManager usuarioManager;
	private ConexaoManager conexaoAPI;
	private ConfigManager config;
	private GrupoManager grupoManager;
	private StaffManager staffManager;
	private String tag;
	private ServerType server = ServerType.Lobby;
	
	public static WalkerEngine get() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		tag = "("+getName()+") ";
		config = new ConfigManager(this);
		conexaoAPI = new ConexaoManager(getConfigManager().getConfigPrincipal());
		usuarioManager = new UsuarioManager();
		grupoManager = new GrupoManager(config.getConfigPrincipal());
		staffManager = new StaffManager();
		registrarEventos();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public ServerType getServerType() {
		return server;
	}
	
	public FileConfiguration getConfig() {
		return config.getConfigPrincipal().get();
	}
	
	public StaffManager getStaffManager() {
		return staffManager;
	}
	
	public GrupoManager getGruposManager() {
		return grupoManager;
	}
	
	public ConfigManager getConfigManager() {
		return config;
	}
	
	public ConexaoManager getConexaoManager() {
		return conexaoAPI;
	}
	
	public String getTag() {
		return tag;
	}
	
	public UsuarioManager getUsuarioManager() {
		return usuarioManager;
	}
	
	public void registrarEventos() {
		Bukkit.getServer().getPluginManager().registerEvents(new UsuarioListeners(), this);
	}

}
