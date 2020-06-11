package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils;

public class ComandoExecutarSom extends BukkitCommand {

	public ComandoExecutarSom(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.executarsom")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length != 2) {
			MensagensAPI.formatoIncorreto("executarsom <som> <jogador/all>", sender);
			return false;
		}
		if (args[1].equalsIgnoreCase("all")) {
			if (!WalkersUtils.isValidEnum(Sound.class, args[0].toUpperCase())) {
				MensagensAPI.mensagemErro("Este SOM não é um som valido. Veja a lista completa de sons aqui: https://pastebin.com/raw/Etyrm0Pk", sender);
				return false;
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.valueOf(args[0].toUpperCase()), 1, 1);
			}
			MensagensAPI.mensagemSucesso("O som '"+args[0]+"' foi executado para todos os jogadores do servidor", sender);
			return true;
		}
		Player p = Bukkit.getPlayer(args[1]);
		if (p == null) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		if (!WalkersUtils.isValidEnum(Sound.class, args[0].toUpperCase())) {
			MensagensAPI.mensagemErro("Este SOM não é um som valido. Veja a lista completa de sons aqui: https://pastebin.com/raw/Etyrm0Pk", sender);
			return false;
		}
		p.playSound(p.getLocation(), Sound.valueOf(args[0].toUpperCase()), 1, 1);
		MensagensAPI.mensagemSucesso("O som '"+args[0]+"' foi executado para o player "+p.getName(), sender);
		return true;
	}

}
