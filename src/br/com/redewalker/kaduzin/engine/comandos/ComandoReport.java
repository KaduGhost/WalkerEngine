package br.com.redewalker.kaduzin.engine.comandos;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportMotivo;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportMotivoType;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportPerfil;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;

public class ComandoReport extends BukkitCommand {

	public ComandoReport(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (args.length == 1) {
			enviarMensagem(j, args[0]);
			return true;
		}else if (args.length >= 3) {
			ReportMotivoType tipo = ReportMotivoType.getByNome(args[1]);
			Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(args[0]);
			if (!(j2 instanceof Usuario)) {
				MensagensAPI.jogadorNaoEncontrado(sender);
				return false;
			}
			if (j.equals(j2)) {
				MensagensAPI.mensagemErro("Você não pode reportar a si mesmo", sender);
				return false;
			}
			if (j2.hasPermission("walker.reports")) {
				MensagensAPI.mensagemErro("Você não pode reportar alguém da equipe", sender);
				return false;
			}
			if (tipo == null) {
				MensagensAPI.mensagemErro("Escolha um motivo válido", sender);
				return false;
			}
			String comentario = "";
			for (int i = 2; i < args.length; i++) {
				comentario+=args[i]+" ";
			}
			ReportPerfil perfil = WalkerEngine.get().getReportManager().getReportPerfil(j2.getNickOriginal());
			if (perfil instanceof ReportPerfil && perfil.containsReport(tipo, j.getNickOriginal())) {
				MensagensAPI.mensagemErro("Você já reportou este jogador por este motivo", sender);
				return false;
			}
			WalkerEngine.get().getReportManager().createReport(j.getNickOriginal(), j2.getNickOriginal(), tipo, comentario);
			MensagensAPI.mensagemSucesso("Você reportou o jogador "+j2.getNickOriginal(), sender);
			return true;
		}
		MensagensAPI.formatoIncorreto("reportar <jogador> <motivo> <comentario>", sender);
		return false;
	}

	private void enviarMensagem(Usuario j, String alvo) {
		Player p = j.getPlayer();
		p.sendMessage("");
		p.sendMessage("  §6Tipos de infração disponíveis:");
		for (ReportMotivoType tipo : EnumSet.allOf(ReportMotivoType.class)) {
			ReportMotivo motivo = WalkerEngine.get().getReportManager().getReportMotivo(tipo);
			List<String> a = new ArrayList<String>();
			a.add(" §e" + motivo.getNome());
			a.add("");
			a.add(" §f" + motivo.getDescricao());
			a.add("");
			a.add(" §eClique para selecionar.");
			new FancyMessage(" §f" + motivo.getNome()).tooltip(a)
					.suggest("/reportar " + alvo + " " + tipo.toString().toLowerCase()).send(p);
		}
		p.sendMessage("");
	}

}
