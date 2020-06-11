package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

/*enum SpeedType {
	WALK, FLY, ALL;
}*/

public class ComandoSpeed extends BukkitCommand {

	public ComandoSpeed(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.speed")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				MensagensAPI.apenasJogadores(sender);
				return false;
			}
			Player p = j.getPlayer();
			if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("reset")) {
				p.setFlySpeed(0.1f);
				p.setWalkSpeed(0.2f);
				MensagensAPI.mensagemSucesso("Velocidade alterada com sucesso para PADRÃO", sender);
				return true;
			}
			float speed;
			try {
				speed = Float.parseFloat(args[0]);
			} catch (NumberFormatException e) {
				MensagensAPI.mensagemErro("Número inválido", sender);
				return false;
			}
			if (speed > 1.0f || speed < -1.0f) {
				MensagensAPI.mensagemErro("Velocidade inválida! Você deve usar um valor entre &70 &ce &71 &ccomo por exemplo &70.5&c", sender);
				return false;
			}
			p.setFlySpeed(speed);
			p.setWalkSpeed(speed);
			MensagensAPI.mensagemSucesso("Velocidade alterada com sucesso para "+args[0], sender);
			return true;
		}else if (args.length == 2) {
			if (j instanceof Usuario && !j.hasPermission("walkers.speed.admin")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
			Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
			if (!(j2 instanceof Usuario)) {
				MensagensAPI.jogadorNaoEncontrado(sender);
				return false;
			}
			Player p2 = j2.getPlayer();
			if (!(p2 instanceof Player)) {
				MensagensAPI.jogadorOffline(sender);
				return false;
			}
			if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("reset")) {
				p2.setFlySpeed(0.1f);
				p2.setWalkSpeed(0.2f);
				MensagensAPI.mensagemSucesso("Velocidade do jogador "+j2.getNickOriginal()+" alterada com sucesso para PADRÃO", sender);
				return true;
			}
			float speed;
			try {
				speed = Float.parseFloat(args[0]);
			} catch (NumberFormatException e) {
				MensagensAPI.mensagemErro("Número inválido", sender);
				return false;
			}
			if (speed > 1.0f || speed < -1.0f) {
				MensagensAPI.mensagemErro("Velocidade inválida! Você deve usar um valor entre &70 &ce &71 &ccomo por exemplo &70.5&c", sender);
				return false;
			}
			p2.setFlySpeed(speed);
			p2.setWalkSpeed(speed);
			MensagensAPI.mensagemSucesso("Velocidade do jogador "+j2.getNickOriginal()+" alterada com sucesso para "+args[0], sender);
			return true;
		}
		return false;
	}

}
