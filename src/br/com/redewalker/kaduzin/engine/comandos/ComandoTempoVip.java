package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.inventario.InvTempoVip;

public class ComandoTempoVip extends BukkitCommand{

	public ComandoTempoVip(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Player p = (Player)sender;
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if(args.length != 0) {
			if (!j.hasPermission("walker.tempovip")) {
				MensagensAPI.formatoIncorreto("tempovip", sender);
				return false;
			}
		}
		if (args.length == 0) {
			InvTempoVip.inventario(p, j, 1);
			return false;
		}
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
		if (!(j2 instanceof Usuario)) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		InvTempoVip.inventario(p, j2, 1);
		return false;
	}

}
