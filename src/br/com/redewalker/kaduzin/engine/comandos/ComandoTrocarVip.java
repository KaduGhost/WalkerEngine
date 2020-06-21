package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.inventario.InvTempoVip;

public class ComandoTrocarVip extends BukkitCommand {

	public ComandoTrocarVip(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.trocarvip")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		Usuario j2 = null;
		if (args.length == 0) j2 = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		else {
			j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
			if (!(j2 instanceof Usuario)) {
				MensagensAPI.jogadorNaoEncontrado(sender);
				return false;
			}
		}
		InvTempoVip.inventarioMudar((Player) sender, j2, 1);
		return false;
	}

}
