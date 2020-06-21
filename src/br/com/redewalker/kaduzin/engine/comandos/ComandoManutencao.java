package br.com.redewalker.kaduzin.engine.comandos;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.manutencao.ManutencaoType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoManutencao extends BukkitCommand {

	public ComandoManutencao(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.manutencao.admin")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (WalkerEngine.get().getManutencaoManager().isProcesso() && !args[0].equalsIgnoreCase("cancelar")) {
			MensagensAPI.mensagemErro("Servidor em contagem regressiva, único alias disponivel é o cancelar", sender);
			return false;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("ajuda")) {
				sender.sendMessage("");
				sender.sendMessage(" §6Comandos de Manutenção");
				sender.sendMessage("§e/manutencao ajuda §7- para ver os comandos do plugin.");
				sender.sendMessage("§e/manutencao stop (delay inicial)§7- para desligar o servidor de forma segura.");
				sender.sendMessage("§e/manutencao manutencao (delay inicial) §7- para por o servidor em modo de manutenção.");
				sender.sendMessage("§e/manutencao stopforced §7- para desligar o servidor sem esperar o tempo da config.");
				sender.sendMessage("§e/manutencao manuforced §7- para por o servidor em modo de manutenção sem esperar o tempo da config.");
				sender.sendMessage("§e/manutencao cancelar §7- para cancelar a contagem regressiva.");
				sender.sendMessage("§e/manutencao voltar §7- para tirar o servidor de manutenção.");
				sender.sendMessage("");
				return true;
			}else if (args[0].equalsIgnoreCase("cancelar")) {
				if (!WalkerEngine.get().getManutencaoManager().isProcesso()) {
					MensagensAPI.mensagemErro("O servidor não está em contagem regressiva", sender);
					return false;
				}
				WalkerEngine.get().getManutencaoManager().cancelar();
				MensagensAPI.mensagemSucesso("Contagem regressiva cancelada e processo desativado", sender);
				return true;
			}else if (args[0].equalsIgnoreCase("stop")) {
				WalkerEngine.get().getManutencaoManager().iniciarManutencao(ManutencaoType.STOP, 0, false);
				MensagensAPI.mensagemSucesso("Servidor entrou em modo de contagem regressiva", sender);
				return true;
			}else if (args[0].equalsIgnoreCase("stopforced")) {
				WalkerEngine.get().getManutencaoManager().iniciarManutencao(ManutencaoType.STOP, 30, true);
				MensagensAPI.mensagemSucesso("Servidor irá ser desligado", sender);
				return true;
			}else if (args[0].equalsIgnoreCase("voltar")) {
				if (WalkerEngine.get().getManutencaoManager().isLiberado()) {
					MensagensAPI.mensagemErro("Servidor não está em manutenção", sender);
					return false;
				}
				WalkerEngine.get().getManutencaoManager().getManutencao().reverse();
				MensagensAPI.mensagemSucesso("Servidor saiu do modo de manutenção", sender);
				return true;
			}else if (args[0].equalsIgnoreCase("manutencao")) {
				WalkerEngine.get().getManutencaoManager().iniciarManutencao(ManutencaoType.MANUTENCAO, 0, false);
				MensagensAPI.mensagemSucesso("Servidor entrou em modo de contagem regressiva", sender);
				return true;
			}else if (args[0].equalsIgnoreCase("manuforced")) {
				WalkerEngine.get().getManutencaoManager().iniciarManutencao(ManutencaoType.MANUTENCAO, 30, true);
				MensagensAPI.mensagemSucesso("Servidor está em modo de manutenção", sender);
				return true;
			}
		}else if (args.length == 2) {
			if ((args[0].equalsIgnoreCase("manutencao") || args[0].equalsIgnoreCase("stop")) && !WalkerEngine.get().getManutencaoManager().isLiberado()) {
				MensagensAPI.mensagemErro("Servidor já está em modo de manutenção", sender);
				return false;
			}
			if (args[0].equalsIgnoreCase("manutencao")) {
				if (!NumberUtils.isDigits(args[1])) {
					MensagensAPI.mensagemErro("Digite um valor numerico", sender);
					return false;
				}
				WalkerEngine.get().getManutencaoManager().iniciarManutencao(ManutencaoType.MANUTENCAO, Integer.parseInt(args[1]), false);
				MensagensAPI.mensagemSucesso("Servidor entrou em modo de contagem regressiva", sender);
				return true;
			}else if (args[0].equalsIgnoreCase("stop")) {
				if (!NumberUtils.isDigits(args[1])) {
					MensagensAPI.mensagemErro("Digite um valor numerico", sender);
					return false;
				}
				WalkerEngine.get().getManutencaoManager().iniciarManutencao(ManutencaoType.STOP, Integer.parseInt(args[1]), false);
				MensagensAPI.mensagemSucesso("Servidor entrou em modo de contagem regressiva", sender);
				return true;
			}
		}
		MensagensAPI.formatoIncorreto("manutencao ajuda", sender);
		return false;
	}

}
