package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportManager;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoReports extends BukkitCommand {

	public ComandoReports(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		ReportManager manager = WalkerEngine.get().getReportManager();
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.reports")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 0) {
			if (manager.isAnalisando(j.getNickOriginal())) manager.abrirInventario(j.getPlayer(), InventarioReportType.AVALIACAO, 1, null);
			else manager.abrirInventario(j.getPlayer(), InventarioReportType.MENU, 1, j.getNickOriginal());
			return true;
		}
		MensagensAPI.formatoIncorreto("reports", sender);
		return false;
	}

}
