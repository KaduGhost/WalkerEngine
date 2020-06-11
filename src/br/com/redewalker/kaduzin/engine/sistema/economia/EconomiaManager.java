package br.com.redewalker.kaduzin.engine.sistema.economia;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class EconomiaManager {
	
	public static boolean hasMoney(String jogador, double value, EconomiaType tipo) {
		if (!WalkerEngine.get().getUsuarioManager().exists(jogador)) return false;
		if (value < 0) return false;
		switch (tipo) {
		case CASH:
			if (WalkerEngine.get().getUsuarioManager().getUsuario(jogador).getCash() >= value) return true;
			break;
		default:
			break;
		}
		return false;
	}
	
	public static void addMoney(String jogador, double value, EconomiaType tipo) {
		if (!WalkerEngine.get().getUsuarioManager().exists(jogador)) return;
		if (value < 0) return;
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(jogador);
		switch (tipo) {
		case CASH:
			j.setCash(j.getCash()+value);
			break;
		default:
			break;
		}
	}
	
	public static void removeMoney(String jogador, double value, EconomiaType tipo) {
		if (!WalkerEngine.get().getUsuarioManager().exists(jogador)) return;
		if (value < 0) return;
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(jogador);
		switch (tipo) {
		case CASH:
			if (j.getCash() <= value) j.setCash(0);
			else j.setCash(j.getCash()-value);
			break;
		default:
			break;
		}
	}
	
	public static void setMoney(String jogador, double value, EconomiaType tipo) {
		if (!WalkerEngine.get().getUsuarioManager().exists(jogador)) return;
		if (value < 0) return;
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(jogador);
		switch (tipo) {
		case CASH:
			if (value <= 0) j.setCash(0);
			else j.setCash(value);
			break;
		default:
			break;
		}
	}
	
}
