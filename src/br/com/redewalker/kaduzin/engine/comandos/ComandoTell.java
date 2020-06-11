package br.com.redewalker.kaduzin.engine.comandos;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.apis.VanishAPI;
import br.com.redewalker.kaduzin.engine.sistema.chat.CanalTag;
import br.com.redewalker.kaduzin.engine.sistema.chat.ChatProtecao;
import br.com.redewalker.kaduzin.engine.sistema.chat.ChatUtils;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;

public class ComandoTell extends BukkitCommand {
	
	private static HashMap<Usuario, Usuario> cache = new HashMap<Usuario, Usuario>();

	public ComandoTell(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		if (args.length < 2) {
			MensagensAPI.formatoIncorreto("tell <jogador> <msg>", sender);
			return false;
		}
		String nick = args[0];
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(nick);
		if (!(j2 instanceof Usuario) || !VanishAPI.UsuarioIsOnline(j2, j)) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		String msg = "";
		for (int i = 1; i <= args.length-1; i++) {
			msg+=args[i]+" ";
		}
		enviarMsg(j, j2, msg);
		return false;
	}
	
	public static void enviarMsg(Usuario sender, String msg) {
		Player p1 = sender.getPlayer();
		if (!cache.containsKey(sender) || !VanishAPI.UsuarioIsOnline(WalkerEngine.get().getUsuarioManager().getUsuario(p1.getName()), sender)) {
			MensagensAPI.mensagemErro("Nenhum jogador para você responder", p1);
			return;
		}
		Usuario j2 = cache.get(sender);
		send(sender, j2, msg);
	}
	
	public static void enviarMsg(Usuario sender, Usuario j2, String msg) {
		send(sender, j2, msg);
	}
	
	private static void send(Usuario j1, Usuario j2, String msg) {
		cache.put(j1, j2);
		cache.put(j2, j1);
		Player p1 = j1.getPlayer();
		Player p2 = j2.getPlayer();
		/*if (WalkerEngine.get().getPunicoesManager().isJogadorPunido(j1.getNickOriginal())) {
			Punicao punicao = WalkerEngine.get().getPunicoesManager().getPunicaoAtiva(j1.getNickOriginal(), PunicoesType.MUTE);
			if (punicao != null) {
				p1.sendMessage(WalkerEngine.get().getPunicoesManager().getMsgPunido(punicao));
				return;
			}
		}*/
		if (!(p2 instanceof Player)) {
			MensagensAPI.jogadorOffline(p1);
			return;
		}
		if (j1.equals(j2)) {
			MensagensAPI.mensagemErro("Evite enviar mensagens a você mesmo, se tiver precisando de um amigo usa o /g", p1);
			return;
		}
		if (!j2.isTell() && !j1.hasPermission("walker.tell.bypass")) {
			MensagensAPI.mensagemErro("Este jogador desativou o recebimento de mensagens", p1);
			return;
		}
		msg = ChatProtecao.filtrarMensagem(j1, msg);
		String corCabeca = "";
		if (j1.hasPermission("walker.tell.staff") || j2.hasPermission("walker.tell.staff")) {
			msg = ChatUtils.colorirMsg(j1, msg, "§c");
			corCabeca = "§4";
		} else {
			msg = ChatUtils.colorirMsg(j1, msg, "§6");
			corCabeca = "§8";
		}
		String last = ChatColor.getLastColors(msg);
		String le = last.replace("§", "");
		if (le.equals("")) le = "f";
		CanalTag ct = WalkerEngine.get().getChatManager().getChatTag("{grupo}");
		new FancyMessage(corCabeca+"Mensagem de " + WalkerEngine.get().getPlaceholderAPI().checkPlaceholders(ct.getTag(), j1, null) + j1.getNickOriginal() + corCabeca+": ").tooltip("§6Clique para responder.").suggest("/tell " + j1.getNickOriginal() + " ").then(msg).color(ChatColor.getByChar(le)).send(p2);
		new FancyMessage(corCabeca+"Mensagem para " + WalkerEngine.get().getPlaceholderAPI().checkPlaceholders(ct.getTag(), j2, null) + j2.getNickOriginal() + corCabeca+": ").tooltip("§6Clique para responder.").suggest("/tell " + j2.getNickOriginal() + " ").then(msg).color(ChatColor.getByChar(le)).send(p1);
	
	}

}
