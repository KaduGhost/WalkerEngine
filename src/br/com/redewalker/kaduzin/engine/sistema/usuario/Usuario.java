package br.com.redewalker.kaduzin.engine.sistema.usuario;

import br.com.redewalker.kaduzin.engine.WalkerEngine;

public class Usuario {
	
	private String nickOriginal;
	
	public Usuario(String nick) {
		this.nickOriginal = nick;
	}
	
	public String getNome() {
		return nickOriginal;
	}
	
	public void setLogado(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "logado", set);
	}
	
	public boolean isLogado() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "logado");
	}
	
	public void setOnline(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "online", set);
	}
	
	public boolean isOnline() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "online");
	}
	
	public void getServer() {
		
	}
	
	public void setServer() {
		
	}
	
	public double getCash() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getDouble(nickOriginal, "cash");
	}

	public void setCash(double cash) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setDouble(nickOriginal, "cash", cash);
	}
	
	public String getNickOriginal() {
		return nickOriginal;
	}
	
	public boolean isVanish() {
		return WalkerEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "vanish");
	}

	public void setVanish(boolean set) {
		WalkerEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "vanish", set);
	}
	
}
