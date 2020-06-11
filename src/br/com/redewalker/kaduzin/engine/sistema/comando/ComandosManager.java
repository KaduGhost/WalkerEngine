package br.com.redewalker.kaduzin.engine.sistema.comando;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.configuracao.ConfigComando;
import br.com.redewalker.kaduzin.engine.sistema.comando.inventario.InventarioComandosType;
import br.com.redewalker.kaduzin.engine.sistema.comando.inventario.InventarioConfiguracao;
import br.com.redewalker.kaduzin.engine.sistema.comando.inventario.InventarioListaBloqueados;
import br.com.redewalker.kaduzin.engine.sistema.comando.inventario.InventarioListaComandos;
import br.com.redewalker.kaduzin.engine.sistema.comando.inventario.InventarioListaPermitidos;
import br.com.redewalker.kaduzin.engine.sistema.comando.inventario.InventarioMenu;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandosManager implements Listener {
	
	private File file;
	private YamlConfiguration arquivo;
	private HashMap<String, ComandoInfos> delays;
	private HashMap<String, String> aliases;
	private boolean registrado, lista, bloquear;
	private List<String> listaBloqueio, listaPermitir;
	private ConfigComando config;
	
	public ComandosManager(ConfigComando config) {
		this.config = config;
		delays = new HashMap<String, ComandoInfos>();
		aliases = new HashMap<String, String>();
		listaBloqueio = listaPermitir = new ArrayList<String>();
		file = new File(WalkerEngine.get().getDataFolder()+File.separator+"comandos"+File.separator, "data.yml");
		arquivo = YamlConfiguration.loadConfiguration(file);
		checkData();
		load();
		Bukkit.getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	public void addListaBloqueio(String comando) {
		if (!listaBloqueio.contains(comando)) listaBloqueio.add(comando);
	}
	
	public void addListaPermitir(String comando) {
		if (!listaPermitir.contains(comando)) listaPermitir.add(comando);
	}
	
	public void removeListaBloqueio(String comando) {
		listaBloqueio.remove(comando);
	}
	
	public void removeListaPermitir(String comando) {
		listaPermitir.remove(comando);
	}
	
	public boolean containsListaBloqueio(String comando) {
		return listaBloqueio.contains(comando);
	}
	
	public boolean containsListaPermitir(String comando) {
		return listaPermitir.contains(comando);
	}
	
	public boolean isComandoAlias(String comando) {
		return aliases.containsKey(comando);
	}
	
	public boolean isComandoPrincipal(String comando) {
		return delays.containsKey(comando);
	}
	
	public boolean isBloquear() {
		return bloquear;
	}
	
	public void setBloquear(boolean set) {
		bloquear = set;
	}
	
	public boolean isLista() {
		return lista;
	}
	
	public void setLista(boolean set) {
		lista = set;
	}
	
	public void setSomenteRegistrados(boolean set) {
		registrado = set;
	}
	
	public boolean isSomenteRegistrados() {
		return registrado;
	}
	
	public void registrarComando(Command comando, String sistema, int delay) {
		adicionarComando(comando, delay, sistema, true, true);
	}
	
	public void adicionarComando(Command comando, int delay, String permission, boolean registrado, boolean ativo) {
		adicionarComando(comando.getName(), delay, comando.getAliases(), permission, registrado, ativo);
		((CraftServer) WalkerEngine.get().getServer()).getCommandMap().register("walker", comando);
	}
	
	public void adicionarComando(String comando, int delay, List<String> aliases, String permission, boolean registrado, boolean ativo) {
		comando=comando.toLowerCase();
		if (existsComando(comando)) {
			getComando(comando).setRegistrado(registrado);
			getComando(comando).setAtivo(ativo);
		}
		else delays.put(comando, new ComandoInfos(comando, delay, permission, aliases, registrado, ativo));
		if (aliases.size() != 0) {
			for (String alias : aliases) {
				this.aliases.put(alias, comando);
			}
		}
	}
	
	public boolean isComandoBloqueado(String comando) {
		ComandoInfos infos = getComando(comando);
		if (registrado && !infos.isRegistrado()) return true;
		if(lista) {
			if (bloquear && listaBloqueio.contains(comando)) return true;
			else if (!bloquear && !listaPermitir.contains(comando)) return true;
		}
		return false;
	}
	
	public boolean isComandoDelay(Usuario j, String comando) {
		ComandoInfos infos = getComando(comando);
		if (existsComando(comando) && infos.isPodeUsarDelay(j)) return true;
		return false;
	}
	
	public boolean isJogadorPodeUsarBlock(Usuario j, String comando) {
		ComandoInfos infos = getComando(comando);
		if (existsComando(comando)) {
			if (isComandoBloqueado(comando) && !checkPermission(j, infos.getPermission(), PermissionComandoType.BLOQUEAR)) return false;
			if (isJogadorPodeUsarPermission(j, infos.getPermission())) return true;
		}
		return false;
	}
	
	public void abrirInventario(Player p, InventarioComandosType tipo, int pagina) {
		switch (tipo) {
		case MENU:
			new InventarioMenu(p);
			return;
		case CONFIG:
			new InventarioConfiguracao(p);
			return;
		case LISTACOMANDOS:
			new InventarioListaComandos(pagina, p);
			return;
		case LISTABLOQUEADOS:
			new InventarioListaBloqueados(pagina, p);
			return;
		case LISTAPERMITIDOS:
			new InventarioListaPermitidos(pagina, p);
			return;
		default:
			break;
		}
	}
	
	public List<ComandoInfos> getAllComandos() {
		List<ComandoInfos> lista = new ArrayList<ComandoInfos>();
		List<String> list = getListaComandos(null, "");
		Collections.sort(list);
		for (String comando : list) {
			lista.add(getComando(comando));
		}
		return lista;
	}
	
	public List<ComandoInfos> getAllComandosBloqueados() {
		List<ComandoInfos> lista = new ArrayList<ComandoInfos>();
		List<String> list = getListaComandos(null, "");
		Collections.sort(list);
		for (String comando : list) {
			if (containsListaBloqueio(comando))lista.add(getComando(comando));
		}
		return lista;
	}
	
	public List<ComandoInfos> getAllComandosPermitidos() {
		List<ComandoInfos> lista = new ArrayList<ComandoInfos>();
		List<String> list = getListaComandos(null, "");
		Collections.sort(list);
		for (String comando : list) {
			if (containsListaPermitir(comando))lista.add(getComando(comando));
		}
		return lista;
	}
	
	private boolean isJogadorPodeUsarPermission(Usuario j, String permission) {
		if (checkPermission(j, permission, PermissionComandoType.PERMISSION)) return true;
		return false;
	}
	
	private String getComandoEmString(String comando) {
		return comando.replaceAll("/", "").split(" ")[0].toLowerCase();
	}
	
	private long getTempoRestante(Usuario j, String comando) {
		if (delays.containsKey(comando)) return (delays.get(comando).tempoTotal(j)-System.currentTimeMillis())/1000L;
		return 0;
	}
	
	private void addDelay(Usuario j, String comando) {
		if (delays.containsKey(comando)) delays.get(comando).addDelay(j);
	}
	
	private List<String> getListaComandos(Usuario j, String start) {
		List<String> lista = new ArrayList<String>();
		for (String comando : delays.keySet()) {
			if (j instanceof Usuario) {
				if (isJogadorPodeUsarBlock(j, comando)) {
					if (!start.equals("") && comando.startsWith(start) || (start.equals(""))) lista.add(comando);
				}
			}else lista.add(comando);
			
		}
		return lista;
	}
	
	private boolean checkPermission(Usuario j, String permission, PermissionComandoType tipo) {
		if (permission.equalsIgnoreCase("default")) permission = "walker.comandos";
		switch (tipo) {
		case BLOQUEAR:
			if (j.hasPermission("walker.comandos.bypass-block") || j.hasPermission(permission+".bypass-block")) return true;
			return false;
		case PERMISSION:
			if (j.hasPermission(permission) || j.hasPermission("walkers.comandos.bypass-permission") || j.hasPermission(permission+".bypass-permission")) return true;
			return false;
		default:
			if (j.hasPermission("walker.comandos.bypass-delay") || j.hasPermission(permission+".bypass-delay")) return true;
			return false;
		}
	}
	
	private String getComandoAlias(String comando) {
		if (aliases.containsKey(comando)) return aliases.get(comando);
		return comando;
	}
	
	private boolean existsComando(String comando) {
		return delays.containsKey(comando);
	}
	
	public ComandoInfos getComando(String comando) {
		return delays.get(comando);
	}
	
	private void removerInativos() {
		ArrayList<String> apagar = new ArrayList<String>();
		for (String comando : delays.keySet()) {
			ComandoInfos c = delays.get(comando);
			if (!c.isAtivo()) {
				apagar.add(comando);
				arquivo.set("comandos."+comando, null);
			}
		}
		for (String comando : apagar) {
			ComandoInfos c = delays.get(comando);
			List<String> alias = c.getAliases();
			removeListaBloqueio(comando);
			removeListaPermitir(comando);
			delays.remove(comando);
			for (String ali : alias) {
				aliases.remove(ali);
			}
		}
		saveConfig();
	}
	
	private void checkData() {
		if (!file.exists()) saveConfig();
	}
	
	private void load() {
		loadTabComplete();
		registrado = config.isSomenteRegistrados();
		lista = config.isLista();
		bloquear = config.isBloquear();
		listaBloqueio = config.getlistaBloquear();
		listaPermitir = config.getlistaPermitir();
		if (arquivo.contains("comandos") && !arquivo.getConfigurationSection("comandos").getKeys(false).isEmpty()) {
			for (String key : arquivo.getConfigurationSection("comandos").getKeys(false)) {
				String permission = arquivo.getString("comandos."+key+".permission");
				List<String> all = arquivo.getStringList("comandos."+key+".alias");
				adicionarComando(key, arquivo.getInt("comandos."+key+".delay"), all, permission, false, false);
			}
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				Iterator<Command> a = ((CraftServer) WalkerEngine.get().getServer()).getCommandMap().getCommands().iterator();
				while (a.hasNext()) {
					Command i = a.next();
					String permission = "default";
					if (i.getPermission() != null) permission = i.getPermission();
					if (!existsComando(i.getName().toLowerCase())) adicionarComando(i.getName(), 0, i.getAliases(), permission, false, true);
					else getComando(i.getName().toLowerCase()).setAtivo(true);
				}
				removerInativos();
			}
		}.runTaskLaterAsynchronously(WalkerEngine.get(), 0);
	}
	
	public void save() {
		for (String command : delays.keySet()) {
			ComandoInfos infos = getComando(command);
			arquivo.set("comandos."+command+".delay", infos.getDelay());
			arquivo.set("comandos."+command+".permission", infos.getPermission());
			arquivo.set("comandos."+command+".alias", infos.getAliases());
		}
		config.setBloquear(bloquear);
		config.setLista(lista);
		config.setListaBloquear(listaBloqueio);
		config.setListaPermitir(listaPermitir);
		config.setSomenteRegistrados(registrado);
		saveConfig();
	}
	
	private void saveConfig() {
		try {arquivo.save(file);} catch (IOException e) {e.printStackTrace();}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	void aoUsarComando(PlayerCommandPreprocessEvent e) {
		String comando = getComandoAlias(getComandoEmString(e.getMessage()));
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		ComandoInfos infos = getComando(comando);
		if (!existsComando(comando) || (isComandoBloqueado(comando) && !checkPermission(j, infos.getPermission(), PermissionComandoType.BLOQUEAR)) || (!checkPermission(j, infos.getPermission(), PermissionComandoType.PERMISSION))) {
			e.setCancelled(true);
			MensagensAPI.mensagemErro("Comando não encontrado", j.getPlayer());
			return;
		}
		if (!isComandoDelay(j, comando) && !checkPermission(j, infos.getPermission(), PermissionComandoType.DELAY)) {
			MensagensAPI.mensagemErro("Você precisa esperar "+getTempoRestante(j, comando)+" segundos para usar este comando novamente", j.getPlayer());
			e.setCancelled(true);
			return;
		}
		addDelay(j, comando);
	}
	
	private void loadTabComplete() {
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.addPacketListener(new PacketAdapter(WalkerEngine.get(), new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
			public void onPacketReceiving(PacketEvent event) {
				String comando = ((String)event.getPacket().getStrings().read(0));
				if (!event.getPacketType().equals(PacketType.Play.Client.TAB_COMPLETE) || !comando.startsWith("/") || comando.contains(" ")) return;
				Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(event.getPlayer().getName());
				event.setCancelled(true);
				String start = comando.replaceFirst("/", "").split(" ")[0];
				List<String> list = getListaComandos(j, start);
				Collections.sort(list);
				String[] tabList = new String[list.size()];
				for (int index = 0; index < list.size(); index++) {
					tabList[index] = "/"+((String)list.get(index));
				}	
				PacketContainer tabComplete = manager.createPacket(PacketType.Play.Server.TAB_COMPLETE);
				tabComplete.getStringArrays().write(0, tabList);
				try {
					manager.sendServerPacket(event.getPlayer(), tabComplete);
				}
				catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}