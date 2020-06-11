package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.reflections.CrashAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoCrashar extends BukkitCommand {

	public ComandoCrashar(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.crashar")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 1) {
			Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
			if (!(j2 instanceof Usuario) || !(j2.getPlayer() instanceof Player)) {
				MensagensAPI.jogadorNaoEncontrado(sender);
				return false;
			}
			if (!WalkerEngine.get().getGruposManager().isSuperior(j, j2)) {
				MensagensAPI.mensagemErro("Você não pode crashar alguém superior a você", sender);
				return false;
			}
			CrashAPI.crashPlayer(j2.getPlayer());
			MensagensAPI.mensagemSucesso("Você crashou o jogador "+j2.getNickOriginal()+" com sucesso", sender);
			return true;
		}
		MensagensAPI.formatoIncorreto("crashar <jogador>", sender);
		return false;
	}

}
