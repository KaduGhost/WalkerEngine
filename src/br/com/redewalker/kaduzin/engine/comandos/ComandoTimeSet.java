package br.com.redewalker.kaduzin.engine.comandos;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoTimeSet extends BukkitCommand {

	public ComandoTimeSet(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.timeset")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (!NumberUtils.isDigits(args[0])) {
			MensagensAPI.mensagemErro("Digite apenas números",sender);
			return false;
		}
		long tempo = Long.parseLong(args[0]);
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				MensagensAPI.apenasJogadores(sender);
				return false;
			}
			j.getPlayer().getLocation().getWorld().setTime(tempo);
			MensagensAPI.mensagemSucesso("Você definiu o tempo do mundo "+j.getPlayer().getLocation().getWorld().getName()+" para "+tempo, sender);
			return true;
		} else if (args.length == 2) {
			String mundo = args[1];
			if (args[1].equalsIgnoreCase("all")) {
				for(World w : Bukkit.getWorlds()){
					w.setTime(0);
				}
				MensagensAPI.mensagemSucesso("Você definiu o tempo de todos os mundos para "+tempo, sender);
				return true;
			}
			else {
				World world = Bukkit.getWorld(mundo);
				if (world == null) {
					MensagensAPI.mensagemErro("Este mundo não existe", sender);
					return false;
				}
				world.setTime(0);
				MensagensAPI.mensagemSucesso("Você definiu o tempo do mundo "+world.getName()+" para "+tempo, sender);
				return true;
			}
		}
		MensagensAPI.formatoIncorreto("timeset <tempo> (mundo/all)", sender);
		return false;
	}

}
