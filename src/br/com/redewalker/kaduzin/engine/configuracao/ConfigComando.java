package br.com.redewalker.kaduzin.engine.configuracao;

import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigComando extends Configuration {
	
	public ConfigComando(JavaPlugin plugin) {
		super("comandos", plugin);
		checkEstrutura();
	}
	
	public void checkEstrutura() {
		if (!get().contains("somente-comandos-registrados")) get().set("somente-comandos-registrados", false);
		if (!get().contains("lista.ativar")) get().set("lista.ativar", false);
		if (!get().contains("lista.bloquear-ou-permitir")) get().set("lista.bloquear-ou-permitir", "bloquear");
		if (!get().contains("lista.bloquear")) get().set("lista.bloquear", Arrays.asList("plugins"));
		if (!get().contains("lista.permitir")) get().set("lista.permitir", Arrays.asList("cmdblock"));
		save();
	}
	
	public boolean isSomenteRegistrados() {
		return get().getBoolean("somente-comandos-registrados");
	}
	
	public void setSomenteRegistrados(boolean set) {
		get().set("somente-comandos-registrados", set);
		save();
	}
	
	public boolean isLista() {
		return get().getBoolean("lista.ativar");
	}
	
	public void setLista(boolean set) {
		get().set("lista.ativar", set);
		save();
	}
	
	public boolean isBloquear() {
		if (get().getString("lista.bloquear-ou-permitir").equalsIgnoreCase("bloquear")) return true;
		return false;
	}
	
	public void setBloquear(boolean set) {
		if (set) get().set("lista.bloquear-ou-permitir", "bloquear");
		else get().set("lista.bloquear-ou-permitir", "permitir");
		save();
	}
	
	public List<String> getlistaBloquear() {
		return get().getStringList("lista.bloquear");
	}
	
	public void setListaBloquear(List<String> set) {
		get().set("lista.bloquear", set);
		save();
	}
	
	public List<String> getlistaPermitir() {
		return get().getStringList("lista.permitir");
	}
	
	public void setListaPermitir(List<String> set) {
		get().set("lista.permitir", set);
		save();
	}

}
