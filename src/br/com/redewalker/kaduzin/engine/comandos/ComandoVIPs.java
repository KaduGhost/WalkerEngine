package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.inventario.InventarioVipsType;

public class ComandoVIPs extends BukkitCommand {
	
	public ComandoVIPs(String name) {
		super(name);
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (args.length == 0) {
			if (!j.hasPermission("walker.vips.admin")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
			WalkerEngine.get().getVipManager().abrirInventario((Player) sender, InventarioVipsType.MENU, null, null, 1, null, null);
			return false;
		}else if (args.length == 1) {
			Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
			if (!(j2 instanceof Usuario)) {
				MensagensAPI.jogadorNaoEncontrado(sender);
				return false;
			}
			if (!j2.getNickOriginal().equals(j.getNickOriginal()) && !j.hasPermission("walker.vips.admin")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
//			Servers server = Servers.getTipo(args[1]);
//			if (server == null) {
//				MensagensAPI.mensagemErro("Este servidor não existe", sender);
//				return false;
//			}
//			if (server.getTipo().equals(ServerType.Lobby)) {
//				MensagensAPI.mensagemErro("Você não pode ver vips de servidor Lobby", sender);
//				return false;
//			}
			WalkerEngine.get().getVipManager().abrirInventario((Player) sender, InventarioVipsType.LISTAVIPS, null, Servers.Rede, 1, j2, null);
			return false;
		}
		MensagensAPI.formatoIncorreto("vips <jogador>", sender);
		return false;
	}
	
}
