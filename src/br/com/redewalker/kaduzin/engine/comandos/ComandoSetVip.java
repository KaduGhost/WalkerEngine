package br.com.redewalker.kaduzin.engine.comandos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.grupo.Grupo;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers.ServerType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoSetVip extends BukkitCommand {

	public ComandoSetVip(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (sender instanceof Player) {
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
			if (!j.hasPermission("walker.darvip")) MensagensAPI.mensagemErro("Somente o console pode usar este comando, pois é usado para venda dos VIPs", sender);
			return false;
		}
		if (args.length != 3) {
			MensagensAPI.formatoIncorreto("darvip <jogador> <vip> <dias> <server>", sender);
			return false;
		}
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
		if (!(j2 instanceof Usuario)) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		if (!GrupoType.isExists(args[1])) {
			MensagensAPI.grupoNaoEncontrado(sender);
			return true;
		}
		GrupoType gp = GrupoType.getTipo(args[1]);
		if (!gp.isVIP() || gp.equals(GrupoType.VIPLobby)) {
			sender.sendMessage(MensagensAPI.getErro()+"Esse grupo não é um grupo vip.");
			return false;
		}
		Grupo grupo = WalkerEngine.get().getGruposManager().getGrupo(gp);
		Servers server = Servers.getTipo(args[3]);
		if (server == null) {
			MensagensAPI.mensagemErro("Este servidor não existe", sender);
			return false;
		}
		if (server.getTipo().equals(ServerType.Lobby)) {
			MensagensAPI.mensagemErro("Você não pode dar vip em servidor Lobby", sender);
			return false;
		}
		Pattern pt = Pattern.compile("[^0-9]");
		Matcher m = pt.matcher(args[2]);
	    boolean b = m.find();
		if (b) {
	    	sender.sendMessage(MensagensAPI.getErro() + "Digite um tempo em númerico em dias.");
	    	return false;
	    }
		int dias = Integer.parseInt(args[2]);
		WalkerEngine.get().getVipManager().setVip(j2, gp, dias, "compra", server);
		if (dias == 0) sender.sendMessage("§aO jogador "+j2.getNickOriginal()+" comprou um vip "+grupo.getTag().toString()+"§a de duração permanente.");
		else sender.sendMessage("§aO jogador "+j2.getNickOriginal()+" comprou um vip "+grupo.getTag().toString()+"§a com duração de "+dias+" dias.");
		WalkerEngine.get().getVipManager().darItens(j2, gp);
		return false;
	}

}
