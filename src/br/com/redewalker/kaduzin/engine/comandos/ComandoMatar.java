package br.com.redewalker.kaduzin.engine.comandos;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.apis.VanishAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoMatar extends BukkitCommand {

	public ComandoMatar(String name) {
		super(name);
		setAliases(Arrays.asList("kill"));
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (args.length == 1) {
			if (j instanceof Usuario && !j.hasPermission("walker.matar")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
			Player p2 = Bukkit.getServer().getPlayer(args[0]);
	 		if (!(p2 instanceof Player) || !VanishAPI.UsuarioIsOnline(WalkerEngine.get().getUsuarioManager().getUsuario(args[0]), j)) {
	 			MensagensAPI.jogadorNaoEncontrado(sender);
	 			return false;
	 		}
	 		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(p2.getName());
	 		if (j2.hasPermission("walker.matar.bypass") && !j.equals(j2)) {
	 			MensagensAPI.mensagemErro("Este jogador é imortal, acho melhor você chamar reforço", sender);
	 			return false;
	 		}
	 		if (!WalkerEngine.get().getGruposManager().isSuperior(j, j2)) {
				MensagensAPI.mensagemErro("Você não pode matar alguém superior a você", sender);
				return false;
			}
	 		p2.setHealth(0);
	 		p2.sendMessage("§eVocê explodiu!");
			if (sender.getName().equalsIgnoreCase(p2.getName())) {
				MensagensAPI.mensagemSucesso("§eVocê se matou, eu to bem mal com isso. :(", sender);
				return true;
			}
			MensagensAPI.mensagemSucesso("Você percebeu que matou alguém? Se sente bem com isso???", sender);
			return true;
		}
		MensagensAPI.formatoIncorreto("matar <jogador>", sender);
		return false;
	}

}
