package br.com.redewalker.kaduzin.engine.apis;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.grupos.GrupoType;

public class MensagensAPI {
	
	public static String erro = WalkerEngine.get().getConfigManager().getConfigPrincipal().getMensagensErro();
	public static String sucesso = WalkerEngine.get().getConfigManager().getConfigPrincipal().getMensagensSucesso();
	
	public static String getErro() {
		return erro;
	}
	
	public static String getSucesso() {
		return sucesso;
	}
	
	public static FancyMessage fixColorFancyMensage(String msg) {
		FancyMessage fancy = new FancyMessage(msg);
		String last = ChatColor.getLastColors(msg);
		String le = last.replace("§", "");
		if (le.equals("")) le = "f";
		fancy.color(ChatColor.getByChar(le));
		return fancy;
	}
	
	private static boolean isValido(CommandSender sender) {
		return sender instanceof CommandSender;
	}
	
	public static void semPermissao(CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Você não tem permissão para fazer isto.");
	}
	
	public static void semPermissaoComando(GrupoType tipo, CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Você precisa ser "+GruposAPI.getGrupo(tipo).getNome()+" ou superior para utilizar este comando.");
	}
	
	public static void semPermissaoOutro(GrupoType tipo, CommandSender sender, String outro) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Você precisa ser "+GruposAPI.getGrupo(tipo).getNome()+" ou superior para utilizar "+outro+".");
	}
	
	public static void apenasJogadores(CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Apenas jogadores podem utilizar isto.");
	}
	
	public static void formatoIncorreto(String formato, CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Formato incorreto, utilize /"+formato+".");
	}
	
	public static void jogadorNaoEncontrado(CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Jogador não encontrado.");
	}
	
	public static void jogadorOffline(CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Jogador está offline.");
	}
	
	public static void grupoNaoEncontrado(CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+"Este grupo não foi encontrado.");
	}
	
	public static void mensagemErro(String msg, CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getErro()+msg.replace("&", "§")+".");
	}
	
	public static void mensagemSucesso(String msg, CommandSender sender) {
		if (!isValido(sender)) return;
		sender.sendMessage(getSucesso()+msg.replace("&", "§")+".");
	}
	
	public static void emailEnviado(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage("§eEmail enviado com sucesso, verifique sua caixa de mensagens.");
		sender.sendMessage("§ePode levar até 15 minutos para a mensagem chegar.");
		sender.sendMessage("§eCaso não encontre, verifique a caixa de spam.");
		sender.sendMessage("");
	}

}
