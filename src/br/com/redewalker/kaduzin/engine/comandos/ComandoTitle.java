package br.com.redewalker.kaduzin.engine.comandos;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.Title;

public class ComandoTitle extends BukkitCommand {

	public ComandoTitle(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.title")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length < 2) {
			MensagensAPI.formatoIncorreto("title <all/jogador> [titulo] <nl> [subtitulo]", sender);
			return false;
		}
		ArrayList<Usuario> jogadores = new ArrayList<>();
		if (args[0].equalsIgnoreCase("all")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
				if (j2 == null || !j2.isLogado()) continue;
				jogadores.add(j2);
			}
		}else {
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
			jogadores.add(j2);
		}
		String mensagem = "";
		for (int i = 1; i < args.length; i++) {
			mensagem+=args[i].replace("&", "§")+" ";
		}
		String[] txt = mensagem.split("<nl>");
		String head = mensagem;
		String footer = "";
		if (mensagem.contains("<nl>")) {
			head = txt[0];
			footer = txt[1];
		}
		for (Usuario j2 : jogadores) {
			new Title(head, footer).send(j2.getPlayer());
		}
		MensagensAPI.mensagemSucesso("Title enviado", sender);
		return false;
	}

}
