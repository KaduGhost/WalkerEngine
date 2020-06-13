package br.com.redewalker.kaduzin.engine;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.ProtocolLibrary;

import br.com.redewalker.kaduzin.engine.bungee.WalkerBungee;
import br.com.redewalker.kaduzin.engine.comandos.ComandoBroadcast;
import br.com.redewalker.kaduzin.engine.comandos.ComandoCash;
import br.com.redewalker.kaduzin.engine.comandos.ComandoCmdBlock;
import br.com.redewalker.kaduzin.engine.comandos.ComandoCrashar;
import br.com.redewalker.kaduzin.engine.comandos.ComandoDivulgar;
import br.com.redewalker.kaduzin.engine.comandos.ComandoExecutarSom;
import br.com.redewalker.kaduzin.engine.comandos.ComandoGrupo;
import br.com.redewalker.kaduzin.engine.comandos.ComandoHat;
import br.com.redewalker.kaduzin.engine.comandos.ComandoLimparChat;
import br.com.redewalker.kaduzin.engine.comandos.ComandoMatar;
import br.com.redewalker.kaduzin.engine.comandos.ComandoPunir;
import br.com.redewalker.kaduzin.engine.comandos.ComandoR;
import br.com.redewalker.kaduzin.engine.comandos.ComandoSkull;
import br.com.redewalker.kaduzin.engine.comandos.ComandoSpeed;
import br.com.redewalker.kaduzin.engine.comandos.ComandoStaff;
import br.com.redewalker.kaduzin.engine.comandos.ComandoSudo;
import br.com.redewalker.kaduzin.engine.comandos.ComandoTell;
import br.com.redewalker.kaduzin.engine.comandos.ComandoTimeSet;
import br.com.redewalker.kaduzin.engine.comandos.ComandoTitle;
import br.com.redewalker.kaduzin.engine.comandos.ComandoVanish;
import br.com.redewalker.kaduzin.engine.conexao.ConexaoManager;
import br.com.redewalker.kaduzin.engine.configuracao.ConfigManager;
import br.com.redewalker.kaduzin.engine.listeners.ComandosListeners;
import br.com.redewalker.kaduzin.engine.listeners.PunicoesListeners;
import br.com.redewalker.kaduzin.engine.listeners.UsuarioListeners;
import br.com.redewalker.kaduzin.engine.sistema.chat.ChatManager;
import br.com.redewalker.kaduzin.engine.sistema.comando.ComandosManager;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoManager;
import br.com.redewalker.kaduzin.engine.sistema.placeholder.PlaceHookWalkers;
import br.com.redewalker.kaduzin.engine.sistema.placeholder.PlaceholderAPI;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.PunicoesManager;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.staff.StaffManager;
import br.com.redewalker.kaduzin.engine.sistema.usuario.UsuarioManager;
import br.com.redewalker.kaduzin.engine.sistema.vanish.VanishManager;
//import br.com.redewalker.kaduzin.engine.sistema.voar.VoarManager;

public class WalkerEngine extends JavaPlugin {
	
	private static WalkerEngine instance;
	private UsuarioManager usuarioManager;
	private ConexaoManager conexaoAPI;
	private ConfigManager config;
	private GrupoManager grupoManager;
	private StaffManager staffManager;
	private ChatManager chatManager;
	private PlaceholderAPI placeholder;
	private ComandosManager comandosManager;
	private VanishManager vanishManager;
	private PunicoesManager punicoesManager;
	//private VoarManager voarManager;
	private boolean isUseProtocolLib;
	private String tag;
	private Servers server;
	private static CommandSender console = Bukkit.getConsoleSender();
	
	public static WalkerEngine get() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		tag = "("+getName()+") ";
		config = new ConfigManager(this);
		server = Servers.getTipo(config.getConfigPrincipal().getServerNome());
		console.sendMessage(server.toString());
		conexaoAPI = new ConexaoManager(getConfigManager().getConfigPrincipal());
		usuarioManager = new UsuarioManager();
		grupoManager = new GrupoManager(config.getConfigPrincipal());
		staffManager = new StaffManager();
		chatManager = new ChatManager(getConfigManager().getConfigChat());
		comandosManager = new ComandosManager(getConfigManager().getConfigComando());
		vanishManager = new VanishManager();
		punicoesManager = new PunicoesManager(getConfigManager().getConfigPrincipal());
//		voarManager = new VoarManager(getConfigManager().getConfigPrincipal());
		placeholder = new PlaceholderAPI();
		if (!checkHooks()) {
			console.sendMessage("§c"+tag+" Server desligando por não encontrar plugins necessários para seu funcionamento.");
			Bukkit.shutdown();
			return;
		}
		try {placeholder.registerPlaceHolder(new PlaceHookWalkers("walkers"));} catch (Exception e) {e.printStackTrace();}
		
		registrarEventos();
		registrarComandos();
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "bungee:walkerengine");
        getServer().getMessenger().registerIncomingPluginChannel(this, "bungee:walkerengine", new WalkerBungee());
        
        //getServer().getMessenger().registerOutgoingPluginChannel(this, "bungee:walkerpunicoes");
        //getServer().getMessenger().registerIncomingPluginChannel(this, "bungee:walkerpunicoes", new WalkerPunicaoBungee());
        
		console.sendMessage("§a"+tag+" Plugin iniciado com sucesso!");
		new BukkitRunnable() {
			@Override
			public void run() {
				for (String user : WalkerEngine.get().getUsuarioManager().getAllUsuarios().keySet()) {
					WalkerEngine.get().getUsuarioManager().getUsuario(user).atualizarGrupo();
				}
			}
		}.runTaskTimerAsynchronously(WalkerEngine.get(), 0, 20*300);
	}
	
	@Override
	public void onDisable() {
		grupoManager.desligar();
		comandosManager.save();
		ProtocolLibrary.getProtocolManager().removePacketListeners(this);
		console.sendMessage("§a"+tag+" Plugin desligado com sucesso!");
		instance = null;
	}
	
	public boolean checkHooks() {
		try {
			if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
	        	isUseProtocolLib = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
	        	return isUseProtocolLib;
	        }
	    }catch (Exception exception){exception.printStackTrace();}
		return false;
	}
	
	public Servers getServerType() {
		return server;
	}
	
//	public VoarManager getVoarManager() {
//		return voarManager;
//	}
	
	public PunicoesManager getPunicoesManager() {
		return punicoesManager;
	}
	
	public FileConfiguration getConfig() {
		return config.getConfigPrincipal().get();
	}
	
	public VanishManager getVanishManager() {
		return vanishManager;
	}
	
	public ComandosManager getComandosManager() {
		return comandosManager;
	}
	
	public PlaceholderAPI getPlaceholderAPI() {
		return placeholder;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
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
		Bukkit.getServer().getPluginManager().registerEvents(new ComandosListeners(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PunicoesListeners(), this);
	}
	
	public void registrarComandos() {
		comandosManager.registrarComando(new ComandoCash("cash"), "walker.membro", 0);
		comandosManager.registrarComando(new ComandoGrupo("grupo"), "walker.grupos.admin", 0);
		comandosManager.registrarComando(new ComandoStaff("staff"), "walker.staff", 0);
		comandosManager.registrarComando(new ComandoBroadcast("broadcast"), "walker.broadcast.admin", 0);
		comandosManager.registrarComando(new ComandoHat("hat"), "walker.hat", 0);
		comandosManager.registrarComando(new ComandoMatar("matar"), "walker.matar", 0);
		comandosManager.registrarComando(new ComandoTimeSet("timeset"), "walker.timeset", 0);
		comandosManager.registrarComando(new ComandoLimparChat("limparchat"), "walker.limparchat", 0);
		comandosManager.registrarComando(new ComandoSkull("skull"), "walker.skull", 0);
		comandosManager.registrarComando(new ComandoTell("tell"), "walker.membro", 0);
		comandosManager.registrarComando(new ComandoR("r"), "walker.membro", 0);
		comandosManager.registrarComando(new ComandoCmdBlock("cmdblock"), "walker.cmdblock", 0);
		comandosManager.registrarComando(new ComandoPunir("punir"), "walker.punir", 0);
		//comandosManager.registrarComando(new ComandoPunir("punir"), "punir", 0);
		//comandosManager.registrarComando(new ComandoMotd("motd"), "motd", 0);
		//comandosManager.registrarComando(new ComandoManutencao("manutencao"), "manutencao.admin", 0);
		//comandosManager.registrarComando(new ComandoReports("reports"), "reports", 0);
		//comandosManager.registrarComando(new ComandoReport("reportar"), "membro", 30);
		comandosManager.registrarComando(new ComandoVanish("vanish"), "walker.vanish", 30);
		//comandosManager.registrarComando(new ComandoEditItem("edititem"), "edititem", 0);
		//comandosManager.registrarComando(new ComandoVoar("voar"), "voar", 0);
		comandosManager.registrarComando(new ComandoCrashar("crashar"), "walker.crashar", 0);
		comandosManager.registrarComando(new ComandoDivulgar("divulgar"), "walker.divulgar", 60*30);
		//comandosManager.registrarComando(new ComandoEditarItem("editaritem"), "editaritem", 0);
		//comandosManager.registrarComando(new ComandoEditarPlaca("editarplaca"), "editarplaca", 0);
		comandosManager.registrarComando(new ComandoExecutarSom("executarsom"), "walker.executarsom", 0);
		comandosManager.registrarComando(new ComandoSpeed("speed"), "walker.speed", 0);
		comandosManager.registrarComando(new ComandoSudo("sudo"), "walker.sudo", 0);
		comandosManager.registrarComando(new ComandoTitle("title"), "walker.title", 0);
	}

}
