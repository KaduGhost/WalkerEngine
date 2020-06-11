package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.comando.inventario.InventarioComandosType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoCmdBlock extends BukkitCommand {

	public ComandoCmdBlock(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if(!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.cmdblock")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length != 0) {
			MensagensAPI.formatoIncorreto("cmdblock", sender);
			return false;
		}
		WalkerEngine.get().getComandosManager().abrirInventario(j.getPlayer(), InventarioComandosType.MENU, 1);
		return false;
	}

}
