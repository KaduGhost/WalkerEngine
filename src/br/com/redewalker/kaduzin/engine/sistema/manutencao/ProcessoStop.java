package br.com.redewalker.kaduzin.engine.sistema.manutencao;

import org.bukkit.Bukkit;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.utils.Title;

public class ProcessoStop implements Manutencao {
	
	private ManutencaoType tipo;
	
	public ProcessoStop(ManutencaoType tipo) {
		this.tipo = tipo;
	}
	
	public void run() {
		new Title("§4"+tipo.getNome(), "§aServidor desligando").broadcast();
		Bukkit.getConsoleSender().sendMessage("§4"+getTipo().getNome()+"\n§cServidor desligando!");
		WalkerEngine.get().getManutencaoManager().setAberto(false);
		WalkerEngine.get().getManutencaoManager().setProcesso(false);
		Bukkit.shutdown();
	}
	
	public void reverse() {
	}
	
	public String getMensagemKick() {
		return "§cO servidor está reiniciando!";
	}
	
	public ManutencaoType getTipo() {
		return tipo;
	}
	
	public boolean isIgnoreBypassKick() {
		return true;
	}

}
