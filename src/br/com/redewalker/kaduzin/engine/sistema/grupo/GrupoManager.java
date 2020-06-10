package br.com.redewalker.kaduzin.engine.sistema.grupo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.configuracao.Config;
import br.com.redewalker.kaduzin.engine.sistema.tag.Tag;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class GrupoManager {
	
	private File file;
	private YamlConfiguration arquivo;
	private HashMap<GrupoType, Grupo> grupos;
	private Config config;
	
	public GrupoManager(Config config) {
		this.config = config;
		file = new File(WalkerEngine.get().getDataFolder()+File.separator+"grupos"+File.separator, "data.yml");
		arquivo = YamlConfiguration.loadConfiguration(file);
		checkData();
		load();
	}
	
	public void setGrupo(Usuario usuario, GrupoType tipo) {
	}
	
	public void addPermission(GrupoType tipo, String permission) {
		WalkerEngine.get().getGruposManager().getGrupo(tipo).addPermission(permission);
	}
	
	public void removePermission(GrupoType tipo, String permission) {
		WalkerEngine.get().getGruposManager().getGrupo(tipo).removePermission(permission);
	}
	
	public Tag getTagGrupo(GrupoType tipo) {
		return WalkerEngine.get().getGruposManager().getGrupo(tipo).getTag();
	}
	
	public String getNomeGrupo(GrupoType tipo) {
		return WalkerEngine.get().getGruposManager().getGrupo(tipo).getNome();
	}
	
	public boolean hasPermission(GrupoType tipo, String permission) {
		return getGrupo(tipo).hasPermission(permission.toLowerCase());
	}
	
	public boolean isSuperior(Usuario j, Usuario j2) {
		return j.getGrupoIn().getPrioridade() > j2.getGrupoIn().getPrioridade();
	}
	
	public Grupo getGrupo(GrupoType tipo) {
		return grupos.get(tipo);
	}
	
	public ArrayList<Grupo> getAllGrupos() {
		ArrayList<Grupo> arr = new ArrayList<Grupo>();
		for (GrupoType grupo : grupos.keySet()) {
			arr.add(grupos.get(grupo));
		}
		return arr;
	}
	
	private void checkData() {
		WalkerEngine.get().getConexaoManager().getGrupoConnection().checkTable();
		if (!file.exists()) saveConfig();
		for (GrupoType grupo : EnumSet.allOf(GrupoType.class)) {
			if (!arquivo.contains("Grupo."+grupo.toString())) {
				arquivo.set("Grupo."+grupo.toString()+".Permissions", "nenhum");
			}
			WalkerEngine.get().getConexaoManager().getGrupoConnection().createGrupo(grupo.toString(), grupo.toString(), "semtag", "semcor", "nenhuma");
		}
		saveConfig();
	}
	
	private void load() {
		grupos = new HashMap<GrupoType, Grupo>();
		for (GrupoType grupo : EnumSet.allOf(GrupoType.class)) {
			ArrayList<String> perms = new ArrayList<String>();
			if (!arquivo.get("Grupo."+grupo.toString()+".Permissions").equals("nenhum")) {
				for (String perm : arquivo.getStringList("Grupo."+grupo.toString()+".Permissions")) {
					perms.add(perm.toLowerCase());
				}
			}
			grupos.put(grupo, new Grupo(perms, grupo));
		}
		if (config.getLoadDefaultPermissions()) loadDefaultPermissions();
	}
	
	private void loadDefaultPermissions() {
		for (GrupoType gp : grupos.keySet()) {
			switch (gp) {
			case Membro:
				grupos.get(gp).setTag(new Tag("", "§7"));
				grupos.get(gp).addPermission("walker.chat.chatglobal");
				grupos.get(gp).addPermission("walker.chat.chatlocal");
				break;
			case VIPLobby:
				grupos.get(gp).setNome("VIP");
				grupos.get(gp).setTag(new Tag("[VIP]", "§e"));
				grupos.get(gp).addHeranca(GrupoType.Membro);
				grupos.get(gp).addPermission("walker.chat.cor");
				grupos.get(gp).addPermission("walker.chat.formato");
				break;
			case VIP1:
				grupos.get(gp).setNome("VIP");
				grupos.get(gp).setTag(new Tag("[VIP]", "§a"));
				grupos.get(gp).addHeranca(GrupoType.VIPLobby);
				break;
			case VIP2:
				grupos.get(gp).setNome("Walker");
				grupos.get(gp).setTag(new Tag("[Walker]", "§5"));
				grupos.get(gp).addHeranca(GrupoType.VIPLobby);
				break;
			case VIP3:
				grupos.get(gp).setNome("Walker+");
				grupos.get(gp).setTag(new Tag("[Walker+]", "§b"));
				grupos.get(gp).addHeranca(GrupoType.VIPLobby);
				break;
			case YoutuberJr:
				grupos.get(gp).setNome("YoutuberJr");
				grupos.get(gp).setTag(new Tag("[YoutuberJr]", "§c"));
				break;
			case Youtuber:
				grupos.get(gp).setNome("Youtuber");
				grupos.get(gp).setTag(new Tag("[Youtuber]", "§c"));
				break;
			case ModeradorYT:
				grupos.get(gp).setNome("Moderador Youtube");
				grupos.get(gp).setTag(new Tag("[ModYT]", "§2"));
				break;
			case ModeradorMidia:
				grupos.get(gp).setNome("Moderador Midias");
				grupos.get(gp).setTag(new Tag("[ModMidia]", "§2"));
				break;
			case ModeradorDiscord:
				grupos.get(gp).setNome("Moderador Discord");
				grupos.get(gp).setTag(new Tag("[ModDC]", "§2"));
				break;
			case Staff:
				grupos.get(gp).setNome("Staff");
				grupos.get(gp).setTag(new Tag("[Staff]", "§e"));
				grupos.get(gp).addHeranca(GrupoType.VIP3);
				grupos.get(gp).addPermission("walker.chat.nodelay");
				break;
			case Ajudante:
				grupos.get(gp).setNome("Ajudante");
				grupos.get(gp).setTag(new Tag("[Ajudante]", "§e"));
				grupos.get(gp).addHeranca(GrupoType.Staff);
				break;
			case Moderador:
				grupos.get(gp).setNome("Moderador");
				grupos.get(gp).setTag(new Tag("[Moderador]", "§2"));
				grupos.get(gp).addHeranca(GrupoType.VIP3);
				grupos.get(gp).addHeranca(GrupoType.Ajudante);
				break;
			case Administrador:
				grupos.get(gp).setNome("Administrador");
				grupos.get(gp).setTag(new Tag("[Admin]", "§c"));
				grupos.get(gp).addHeranca(GrupoType.Moderador);
				break;
			case Gerente:
				grupos.get(gp).setNome("Gerente");
				grupos.get(gp).setTag(new Tag("[Gerente]", "§4"));
				grupos.get(gp).addHeranca(GrupoType.Administrador);
				grupos.get(gp).addPermission("walker.staff.setar");
				break;
			case Master:
				grupos.get(gp).setNome("Master");
				grupos.get(gp).setTag(new Tag("[Master]", "§d"));
				grupos.get(gp).addHeranca(GrupoType.Gerente);
				break;
			default:
				break;
			}
		}
		config.setLoadDefaultPermissions(false);
	}
	
	private void saveAllGrupos() {
		for (GrupoType tipo : grupos.keySet()) {
			ArrayList<String> perms = grupos.get(tipo).getPermissions();
			if (perms.size() > 0) {
				arquivo.set("Grupo."+tipo.toString()+".Permissions", perms);
			} else {
				arquivo.set("Grupo."+tipo.toString()+".Permissions", "nenhum");
			}
		}
		saveConfig();
	}
	
	public void reload() {
		saveAllGrupos();
		load();
	}
	
	public void desligar() {
		saveAllGrupos();
	}
	
	private void saveConfig() {
		try {
			arquivo.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
