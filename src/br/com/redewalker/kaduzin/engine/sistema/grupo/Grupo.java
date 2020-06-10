package br.com.redewalker.kaduzin.engine.sistema.grupo;

import java.util.ArrayList;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.tag.Tag;

public class Grupo {
	
	private ArrayList<String> permissions;
	private GrupoType tipo;
	
	public Grupo(ArrayList<String> permissions, GrupoType tipo) {
		this.permissions = permissions;
		this.tipo = tipo;
	}

	public String getNome() {
		return WalkerEngine.get().getConexaoManager().getGrupoConnection().getString(tipo.toString(), "nome").replace("&", "§");
	}

	public void setNome(String nome) {
		WalkerEngine.get().getConexaoManager().getGrupoConnection().setString(tipo.toString(), "nome", nome.replace("§", "&"));
	}

	public Tag getTag() {
		return new Tag(WalkerEngine.get().getConexaoManager().getGrupoConnection().getString(tipo.toString(), "tag").replace("&", "§"), WalkerEngine.get().getConexaoManager().getGrupoConnection().getString(tipo.toString(), "cor").replace("&", "§"));
	}

	public void setTag(Tag tag) {
		WalkerEngine.get().getConexaoManager().getGrupoConnection().setString(tipo.toString(), "tag", tag.getTag().replace("§", "&"));
		WalkerEngine.get().getConexaoManager().getGrupoConnection().setString(tipo.toString(), "cor", tag.getCor().toString().replace("§", "&"));
	}
	
	public void addHeranca(GrupoType permission) {
		ArrayList<GrupoType> herancas = getHerancas();
		if (!herancas.contains(permission)) herancas.add(permission);
		String st = "";
		for(GrupoType i : herancas) {
			st+=i.toString()+":::";
		}
		WalkerEngine.get().getConexaoManager().getGrupoConnection().setString(tipo.toString(), "herancas", st);
	}
	
	private ArrayList<GrupoType> getHerancas() {
		ArrayList<GrupoType> herancas = new ArrayList<GrupoType>();
		String stheranca = WalkerEngine.get().getConexaoManager().getGrupoConnection().getString(tipo.toString(), "herancas");
		if (!stheranca.equalsIgnoreCase("nenhuma")) {
			String[] herancs = stheranca.split(":::");
			for (String gp : herancs) {
				herancas.add(GrupoType.getTipo(gp));
			}
		}
		return herancas;
	}
	
	public void removeHeranca(GrupoType permission) {
		ArrayList<GrupoType> herancas = getHerancas();
		if (herancas.contains(permission)) herancas.remove(permission);
		String st = "";
		for(GrupoType i : herancas) {
			st+=i.toString()+":::";
		}
		WalkerEngine.get().getConexaoManager().getGrupoConnection().setString(tipo.toString(), "herancas", st);
	}
	
	public void removeAllHerancas() {
		WalkerEngine.get().getConexaoManager().getGrupoConnection().setString(tipo.toString(), "herancas", "nenhuma");
	}
	
	public boolean hasHeranca(GrupoType permission) {
		String stheranca = WalkerEngine.get().getConexaoManager().getGrupoConnection().getString(tipo.toString(), "herancas");
		if (!stheranca.equalsIgnoreCase("nenhuma")) {
			String[] herancs = stheranca.split(":::");
			for (String gp : herancs) {
				if (gp.equalsIgnoreCase(permission.toString())) return true;
			}
		}
		return false;
	}
	
	public void addPermission(String permission) {
		if (!permissions.contains(permission.toLowerCase())) permissions.add(permission.toLowerCase());
	}
	
	public void removePermission(String permission) {
		if (permissions.contains(permission.toLowerCase())) permissions.remove(permission.toLowerCase());
	}
	
	public void removeAllPermissions() {
		permissions = new ArrayList<String>();
	}
	
	public boolean hasPermission(String permission) {
		if (permissions.contains(permission.toLowerCase())) return true;
		for (GrupoType tipo : getHerancas()) {
			if (WalkerEngine.get().getGruposManager().hasPermission(tipo, permission)) return true;
		}
		return false;
	}
	
	public ArrayList<String> getPermissions() {
		return permissions;
	}

	public GrupoType getTipo() {
		return tipo;
	}

	public void setTipo(GrupoType tipo) {
		this.tipo = tipo;
	}

}
