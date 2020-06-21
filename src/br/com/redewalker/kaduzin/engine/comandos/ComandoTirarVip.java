package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.inventario.InvTempoVip;

public class ComandoTirarVip extends BukkitCommand {

	public ComandoTirarVip(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.tirarvip")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length != 1) {
			MensagensAPI.formatoIncorreto("tirarvip <jogador>", sender);
			return false;
		}
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
		if (!(j2 instanceof Usuario)) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		InvTempoVip.inventarioRemove((Player) sender, j2, 1);
		return false;
	}

}
