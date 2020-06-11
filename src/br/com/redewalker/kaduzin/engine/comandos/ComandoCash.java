package br.com.redewalker.kaduzin.engine.comandos;

import java.text.NumberFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.economia.EconomiaManager;
import br.com.redewalker.kaduzin.engine.sistema.economia.EconomiaType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoCash extends BukkitCommand {

	public ComandoCash(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (args.length == 0) {
			if (sender instanceof Player) {
				sender.sendMessage("§eCash: §f"+NumberFormat.getInstance().format(j.getCash()));
				return true;
			}else {
				MensagensAPI.formatoIncorreto("cash ajuda", sender);
				return true;
			}
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("ajuda")) {
				sender.sendMessage("");
				sender.sendMessage("  §6Comandos de cash");
				if (j instanceof Usuario) sender.sendMessage(" §e/cash §7 - para ver seu cash.");
				sender.sendMessage(" §e/cash ajuda§7 - para ver os comandos de ajuda.");
				if (!(sender instanceof Player) || j.hasPermission("walker.cash.admin")) {
					sender.sendMessage(" §e/cash add <cash> <jogador>§7 - para adicionar cash ao jogador.");
					sender.sendMessage(" §e/cash remove <cash> <jogador>§7 - para remover cash do jogador.");
					sender.sendMessage(" §e/cash set <cash> <jogador>§7 - para definir cash para um jogador.");
				}
				sender.sendMessage("");
				return true;
			}
			Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
			if (!(j2 instanceof Usuario)) {
				MensagensAPI.jogadorNaoEncontrado(sender);
				return false;
			}
			if (sender instanceof Player && !j.hasPermission("walker.cash.admin")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
			sender.sendMessage("§eCash de "+j2.getNickOriginal()+": §f"+NumberFormat.getInstance().format(j2.getCash()));
			return true;
		}  else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("add")) {
				if (sender instanceof Player && !j.hasPermission("walker.cash.admin")) {
					MensagensAPI.semPermissaoComando(sender);
					return true;
				}
				Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[2]);
				if (!(j2 instanceof Usuario)) {
					MensagensAPI.jogadorNaoEncontrado(sender);
					return true;
				}
				double value = Double.parseDouble(args[1]);
				if (value <= 0) {
					MensagensAPI.mensagemErro("Coloque um valor maior que 0",sender);
					return true;
				}else if (value > 0) {
					EconomiaManager.addMoney(j2.getNickOriginal(), value, EconomiaType.CASH);
					MensagensAPI.mensagemSucesso("Você deu "+NumberFormat.getInstance().format(value)+" cash para o jogador "+j2.getNickOriginal(),sender);
					if (j2.getPlayer() instanceof Player) MensagensAPI.mensagemSucesso("Você recebeu "+NumberFormat.getInstance().format(value)+" cash.",j2.getPlayer());
					return true;
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (sender instanceof Player && !j.hasPermission("walker.cash.admin")) {
					MensagensAPI.semPermissaoComando(sender);
					return true;
				}
				Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[2]);
				if (!(j2 instanceof Usuario)) {
					MensagensAPI.jogadorNaoEncontrado(sender);
					return true;
				}
				double value = Double.parseDouble(args[1]);
				if (value <= 0) {
					MensagensAPI.mensagemErro("Coloque um valor maior que 0",sender);
					return true;
				}else if (value > 0) {
					EconomiaManager.removeMoney(j2.getNickOriginal(), value, EconomiaType.CASH);
					MensagensAPI.mensagemSucesso("Você removeu "+NumberFormat.getInstance().format(value)+" cash do jogador "+j2.getNickOriginal(),sender);
					if (j2.getPlayer() instanceof Player) MensagensAPI.mensagemSucesso("Agora você tem "+NumberFormat.getInstance().format(j2.getCash())+" cash.",j2.getPlayer());
					return true;
				}
			} else if (args[0].equalsIgnoreCase("set")) {
				if (sender instanceof Player && !j.hasPermission("walker.cash.admin")) {
					MensagensAPI.semPermissaoComando(sender);
					return true;
				}
				Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[2]);
				if (!(j2 instanceof Usuario)) {
					MensagensAPI.jogadorNaoEncontrado(sender);
					return true;
				}
				double value = Double.parseDouble(args[1]);
				if (value < 0) {
					MensagensAPI.mensagemErro("Coloque um valor maior ou igual a 0",sender);
					return true;
				}else {
					EconomiaManager.setMoney(j2.getNickOriginal(), value, EconomiaType.CASH);
					MensagensAPI.mensagemSucesso("Você definiu os cash do jogador "+j2.getNickOriginal()+" para "+value,sender);
					if (j2.getPlayer() instanceof Player) MensagensAPI.mensagemSucesso("Agora você tem "+NumberFormat.getInstance().format(j2.getCash())+" cash",j2.getPlayer());
					return true;
				}
			}
		}
		MensagensAPI.formatoIncorreto("cash ajuda", sender);
		return true;
	}

}
