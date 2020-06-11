package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoR extends BukkitCommand {

	public ComandoR(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		if (args.length == 0) {
			MensagensAPI.formatoIncorreto("r <msg>", sender);
			return false;
		}
		String msg = "";
		for (int i = 0; i < args.length; i++) {
			msg+=args[i]+" ";
		}
		ComandoTell.enviarMsg(j, msg);
		return false;
	}

}
