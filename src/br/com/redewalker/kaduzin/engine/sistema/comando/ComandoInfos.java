package br.com.redewalker.kaduzin.engine.sistema.comando;

import java.util.HashMap;
import java.util.List;

import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoInfos {
	
	private int delay;
	private String permission, nome;
	private HashMap<Usuario, Long> delays;
	private boolean registrado, ativo;
	private List<String> aliases;
	
	public ComandoInfos(String nome, int delay, String permission, List<String> aliases, boolean registrado, boolean ativo) {
		this.nome = nome;
		this.delay = delay;
		this.delays = new HashMap<Usuario, Long>();
		this.permission = permission.toLowerCase();
		this.registrado = registrado;
		this.aliases = aliases;
		this.ativo = ativo;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
	
	public void setAtivo(boolean set) {
		this.ativo = set;
	}
	
	public String getNome() {
		return nome;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public boolean isRegistrado() {
		return registrado;
	}
	
	public void setRegistrado(boolean set) {
		this.registrado = set;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public long tempoTotal(Usuario j) {
		if (delays.containsKey(j)) return delays.get(j)+delay*1000L;
		return 0;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void addDelay(Usuario j) {
		delays.put(j, System.currentTimeMillis());
	}
	
	public boolean isPodeUsarDelay(Usuario j) {
		if (!delays.containsKey(j)) return true;
		long tempo = delays.get(j);
		return tempo+delay*1000L < System.currentTimeMillis();
	}
	
	public boolean isPodeUsarPermission(Usuario j) {
		return j.hasPermission(permission);
	}

}
