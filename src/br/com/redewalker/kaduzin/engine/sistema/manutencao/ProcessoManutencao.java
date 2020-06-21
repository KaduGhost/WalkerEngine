package br.com.redewalker.kaduzin.engine.sistema.manutencao;

import org.bukkit.Bukkit;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.utils.Title;

public class ProcessoManutencao implements Manutencao{
	
	private ManutencaoType tipo;
	
	public ProcessoManutencao(ManutencaoType tipo) {
		this.tipo = tipo;
	}
	
	public void run() {
		new Title("§4"+tipo.getNome(), "§aAtivada").broadcast();
		Bukkit.getConsoleSender().sendMessage("§4"+getTipo().getNome()+"\n§cServidor em manutenção");
		WalkerEngine.get().getManutencaoManager().setLiberado(false);
		WalkerEngine.get().getManutencaoManager().setProcesso(false);
	}
	
	public void reverse() {
		new Title("§4"+tipo.getNome(), "§cDesativada").broadcast();;
		WalkerEngine.get().getManutencaoManager().setLiberado(true);
	}
	
	public String getMensagemKick() {
		return "§cServidor entrou em manutenção!";
	}
	
	public ManutencaoType getTipo() {
		return tipo;
	}
	
	public boolean isIgnoreBypassKick() {
		return false;
	}

}
