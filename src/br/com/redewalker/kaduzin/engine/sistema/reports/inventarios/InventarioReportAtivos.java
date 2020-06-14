package br.com.redewalker.kaduzin.engine.sistema.reports.inventarios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportMotivoType;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportPerfil;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioReportAtivos {
	
	public InventarioReportAtivos(Player p, int pagina) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.reports")) return;
		List<ReportPerfil> perfis = WalkerEngine.get().getReportManager().getReportados();
		Collections.sort(perfis, Collections.reverseOrder());
		int paginas = 0;
		if (perfis.size()%45 == 0) paginas = perfis.size()/45;
		else paginas = perfis.size()/45+1;
		Inventory inv = Bukkit.createInventory(p, 9*6, "§e§8Reports - Walkers§e");
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int quantItens = perfis.size()-i*45;
			for (int f = 0; f < 45; f++) {
				if (quantItens == f) break;
				inv.setItem(f, getItem(perfis.get(i*45+f), j));
			}
		}
		inv.setItem(53, WalkersItens.getItem("voltar",null));
		inv.setItem(49, WalkersItens.getPaginaAtual(pagina));
		if (pagina < paginas) inv.setItem(50, WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(48, WalkersItens.getItem("paginaanterior",null));
		p.openInventory(inv);
	}
	
	private ItemStack getItem(ReportPerfil punicao, Usuario j) {
		ArrayList<String> lore = new ArrayList<String>();
		Usuario analisando = WalkerEngine.get().getUsuarioManager().getUsuario(punicao.getAnalisando());
		lore.add("");
		for (ReportMotivoType tipo : EnumSet.allOf(ReportMotivoType.class)) {
			int quant = punicao.getQuantidadeReportsByType(tipo);
			if (quant != 0) lore.add(" §e• §f"+WalkerEngine.get().getReportManager().getReportMotivo(tipo).getNome()+"§7: "+punicao.getQuantidadeReportsByType(tipo));
		}
		lore.add("");
		lore.add(" §fTotal§7: "+punicao.getQuantidadeReports());
		if (punicao.isAnalisando()) {
			lore.add("");
			lore.add(" §fSendo analisado por§7:");
			if (analisando instanceof Usuario) lore.add("  §e• §f"+WalkerEngine.get().getGruposManager().getGrupo(analisando.getGrupoIn()).getTag().toString()+punicao.getAnalisando());
			else lore.add("  §e• §f"+punicao.getAnalisando());
		}
		lore.add("");
		lore.add(" §aClique para ver detalhers");
		return WalkersItens.getCabeca(punicao.getJogador(), lore, punicao.getJogador());
	}

}
