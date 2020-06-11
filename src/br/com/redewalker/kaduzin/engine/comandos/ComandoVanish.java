package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoVanish extends BukkitCommand {

	public ComandoVanish(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.vanish")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 0) {
			WalkerEngine.get().getVanishManager().changeVanish(j);
			if (j.isVanish()) MensagensAPI.mensagemSucesso("Vanish ativado", sender);
			else MensagensAPI.mensagemSucesso("Vanish desativado", sender);
			return true;
		}else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("ativar")) {
				WalkerEngine.get().getVanishManager().ativarVanish(j);
				MensagensAPI.mensagemSucesso("Vanish ativado", sender);
				return true;
			}else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("desativar")) {
				WalkerEngine.get().getVanishManager().desativarVanish(j);
				MensagensAPI.mensagemSucesso("Vanish desativado", sender);
				return true;
			}
		}
		MensagensAPI.formatoIncorreto("vanish (ativar/desativar)", sender);
		return false;
	}

}
