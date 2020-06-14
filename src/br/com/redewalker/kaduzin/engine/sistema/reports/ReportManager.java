package br.com.redewalker.kaduzin.engine.sistema.reports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.reflections.ActionBarAPI;
import br.com.redewalker.kaduzin.engine.sistema.reports.eventos.ReportCreateEvent;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportAtivos;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportAvaliacao;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportAvaliacoes;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportMenu;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportPerfil;
import br.com.redewalker.kaduzin.engine.sistema.reports.inventarios.InventarioReportType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ReportManager implements Listener {
	
	private File file;
	private YamlConfiguration arquivo;
	private HashMap<Integer, Report> reports;
	private HashMap<Integer, Report> ativos;
	private HashMap<String, ReportPerfil> reportados;
	private HashMap<ReportMotivoType, ReportMotivo> reportsTypes;
	private HashMap<String, ReportPerfil> analisando;
	private int id;
	
	public ReportManager() {
		file = new File(WalkerEngine.get().getDataFolder()+File.separator+"reports"+File.separator, "data.yml");
		arquivo = YamlConfiguration.loadConfiguration(file);
		checkData();
		load();
		WalkerEngine.get().getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	public void createReport(String autor, String reportado, ReportMotivoType tipo, String comentario) {
		checkID();
		addReport(new Report(id, reportado, autor, tipo, comentario, System.currentTimeMillis(), true), reportado);
		WalkerEngine.get().getServer().getPluginManager().callEvent(new ReportCreateEvent(reports.get(id)));
	}
	
	public ReportMotivo getReportMotivo(ReportMotivoType tipo) {
		return reportsTypes.get(tipo);
	}
	
	public List<ReportPerfil> getReportadosEmAvaliacao() {
		List<ReportPerfil> lista = new ArrayList<ReportPerfil>();
		for (String j : analisando.keySet()) {
			ReportPerfil perfil = analisando.get(j);
			lista.add(perfil);
		}
		return lista;
	}
	
	public List<ReportPerfil> getReportados() {
		List<ReportPerfil> lista = new ArrayList<ReportPerfil>();
		for (String j : reportados.keySet()) {
			ReportPerfil perfil = reportados.get(j);
			if (perfil.getQuantidadeReports() > 0) lista.add(reportados.get(j));
		}
		return lista;
	}
	
	public ReportPerfil getReportPerfil(String j) {
		if (reportados.containsKey(j)) return reportados.get(j);
		else return null;
	}
	
	public boolean exists(int id) {
		return reports.containsKey(id);
	}
	
	public ReportPerfil getAnalisando(String j) {
		if (analisando.containsKey(j)) return analisando.get(j);
		else return null;
	}
	
	public boolean isAnalisando(String j) {
		return analisando.containsKey(j);
	}
	
	public void finalizarAnalise(String j) {
		if (!analisando.containsKey(j)) return;
		ReportPerfil perfil = analisando.get(j);
		removeAnalise(j);
		List<Report> desativar = new ArrayList<Report>();
		for (Report report : perfil.getReports()) {
			desativar.add(report);
		}
		for (Report d : desativar) {
			setDesativado(d);
		}
	}
	
	public void cancelarAnalise(String j) {
		if (!analisando.containsKey(j)) return;
		removeAnalise(j);
	}
	
	public void analisar(String j, String j2) {
		ReportPerfil perfil = reportados.get(j2);
		if (perfil == null || perfil.getQuantidadeReports() < 1) return;
		reportados.get(j2).setAnalisando(j);
		analisando.put(j, reportados.get(j2));
		abrirInventario(Bukkit.getPlayer(j), InventarioReportType.AVALIACAO, 1, j2);
	}
	
	public void abrirInventario(Player p, InventarioReportType tipo, int pagina, String j2) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		switch (tipo) {
		case MENU:
			new InventarioReportMenu(p);
			return;
		case REPORTS:
			new InventarioReportAtivos(p, pagina);
			return;
		case REPORTPERFIL:
			new InventarioReportPerfil(p, pagina, reportados.get(j2));
			return;
		case AVALIACAO:
			new InventarioReportAvaliacao(p, analisando.get(j.getNickOriginal()));
			return;
		case AVALIACOES:
			new InventarioReportAvaliacoes(p, pagina);
			return;
		default:
			break;
		}
	}
	
	private void removeAnalise(String j) {
		ReportPerfil perfil = analisando.get(j);
		perfil.setAnalisando(null);
		analisando.remove(j);
	}
	
	private void setDesativado(Report report) {
		ativos.remove(report.getID());
		report.setAtivo(false);
		reportados.get(report.getReportado()).removeReport(report);
	}
	
	private void addReport(Report report, String reportado) {
		reports.put(report.getID(), report);
		if (report.isAtivo()) ativos.put(report.getID(), report);
		if (!reportados.containsKey(reportado)) reportados.put(report.getReportado(), new ReportPerfil(reportado));
		reportados.get(reportado).addReport(report);
	}
	
	private void checkData() {
		WalkerEngine.get().getConexaoManager().getConexaoReportDAO().checkTable();
		if (!file.exists()) saveConfig();
		for (ReportMotivoType tipo : EnumSet.allOf(ReportMotivoType.class)) {
			if (!arquivo.contains("motivos."+tipo.toString().toLowerCase())) {
				arquivo.set("motivos."+tipo.toString().toLowerCase()+".nome", "nenhum");
				arquivo.set("motivos."+tipo.toString().toLowerCase()+".descricao", "nenhum");
			}
		}
		saveConfig();
	}
	
	private void load() {
		reports = new HashMap<Integer, Report>();
		ativos = new HashMap<Integer, Report>();
		reportados = new HashMap<>();
		reportsTypes = new HashMap<ReportMotivoType, ReportMotivo>();
		analisando = new HashMap<String, ReportPerfil>();
		for (ReportMotivoType tipo : EnumSet.allOf(ReportMotivoType.class)) {
			reportsTypes.put(tipo, new ReportMotivo(tipo.toString().toLowerCase(), arquivo.getString("motivos."+tipo.toString().toLowerCase()+".nome"), arquivo.getString("motivos."+tipo.toString().toLowerCase()+".descricao"), tipo));
		}
		ArrayList<Integer> apagados = new ArrayList<Integer>();
		for (Report report : WalkerEngine.get().getConexaoManager().getConexaoReportDAO().getAllReports()) {
			if (report.getData()+604800*1000L < System.currentTimeMillis()) apagados.add(report.getID());
			else addReport(report, report.getReportado());
		}
		WalkerEngine.get().getConexaoManager().getConexaoReportDAO().apagarReports(apagados);
		id = getLastID();
	}
	
	public void save() {
		List<Report> save = new ArrayList<Report>();
		for (int id : reports.keySet()) {
			save.add(reports.get(id));
		}
		WalkerEngine.get().getConexaoManager().getConexaoReportDAO().saveAllReports(save);
	}
	
	private int getLastID() {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i : reports.keySet()) ids.add(i);
		if (!ids.isEmpty()) {
			Collections.sort(ids, Collections.reverseOrder());
			return ids.get(0);
		}
		return 1;
	}
	
	private void checkID() {
		while(exists(id)) id++;
	}
	
	private void saveConfig() {
		try {
			arquivo.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	void aoCriarReport(ReportCreateEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
			if (j.hasPermission("walker.reports")) ActionBarAPI.sendActionBarMessage(p, "§aUm novo reporte foi adicionado.");
		}
	}

}
