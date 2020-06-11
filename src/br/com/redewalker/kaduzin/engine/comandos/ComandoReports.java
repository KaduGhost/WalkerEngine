package br.com.redewalker.kaduzin.engine.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import com.walkersmc.api.WalkersAPI;
import com.walkersmc.api.apis.JogadoresAPI;
import com.walkersmc.api.apis.MensagensAPI;
import com.walkersmc.api.sistemas.grupos.GrupoType;
import com.walkersmc.api.sistemas.jogador.Jogador;
import com.walkersmc.api.sistemas.reports.ReportManager;
import com.walkersmc.api.sistemas.reports.inventarios.InventarioReportType;

public class ComandoReports extends BukkitCommand {

	public ComandoReports(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		ReportManager manager = WalkersAPI.get().getReportManager();
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Jogador j = JogadoresAPI.getJogador(sender.getName());
		if (!j.hasPermission("walker.reports")) {
			MensagensAPI.semPermissaoComando(GrupoType.Moderador, sender);
			return false;
		}
		if (args.length == 0) {
			if (manager.isAnalisando(j)) manager.abrirInventario(j.getPlayer(), InventarioReportType.AVALIACAO, 1, null);
			else manager.abrirInventario(j.getPlayer(), InventarioReportType.MENU, 1, j);
			return true;
		}
		MensagensAPI.formatoIncorreto("reports", sender);
		return false;
	}

}
