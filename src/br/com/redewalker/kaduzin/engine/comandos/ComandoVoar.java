package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoVoar extends BukkitCommand {

	public ComandoVoar(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.voar")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 0) {
			if (!WalkerEngine.get().getVoarManager().isPodeVoar(j)) {
				MensagensAPI.mensagemErro("Você não pode voar nesta localização", sender);
				return false;
			}
			WalkerEngine.get().getVoarManager().mudarVoar(j,j);
			return true;
		}else if (args.length == 1) {
			if (!j.hasPermission("walker.voar.admin")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
			Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
			if (!(j2 instanceof Usuario) || !(j2.getPlayer() instanceof Player)) {
				MensagensAPI.jogadorOffline(sender);
				return false;
			}
			WalkerEngine.get().getVoarManager().mudarVoar(j2,j);
			return true;
		}
		MensagensAPI.formatoIncorreto("voar (jogador)", sender);
		return false;
	}

}
