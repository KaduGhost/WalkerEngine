package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoSudo extends BukkitCommand {

	public ComandoSudo(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.sudo")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length < 2) {
			MensagensAPI.formatoIncorreto("sudo <player> /<comando>", sender);
			return false;
		}
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
		if (j2 == null) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		if (j instanceof Usuario && j.getGrupoIn().getPrioridade() <= j2.getGrupoIn().getPrioridade()) {
			MensagensAPI.mensagemErro("Você não pode usar o sudo em superiores", sender);
			return false;
		}
		Player p = j2.getPlayer();
		if (p == null) {
			MensagensAPI.jogadorOffline(sender);
			return false;
		}
		String comando = "";
		for (int i = 1; i < args.length; i++) {comando += args[i] + " ";}
		p.chat(comando);
		MensagensAPI.mensagemSucesso(" Sudo enviado com sucesso para "+j2.getNickOriginal()+".\n §aComando executado: §7"+comando, sender);
		return true;
	}

}
