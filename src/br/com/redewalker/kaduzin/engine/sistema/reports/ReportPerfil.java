package br.com.redewalker.kaduzin.engine.sistema.reports;

import java.util.ArrayList;
import java.util.List;

public class ReportPerfil implements Comparable<ReportPerfil>{
	
	private String jogador, analisando;
	private List<Report> reports;
	
	public ReportPerfil(String jogador) {
		this.jogador = jogador;
		this.analisando = null;
		this.reports = new ArrayList<Report>();
	}
	
	public boolean containsReport(ReportMotivoType tipo, String j) {
		for (Report report : reports) {
			if (report.getAutor().equals(j) && report.getMotivo().equals(tipo)) return true;
		}
		return false;
	}
	
	public int getQuantidadeReportsByType(ReportMotivoType tipo) {
		int retur = 0;
		for (Report report : reports) {
			if (report.getMotivo().equals(tipo)) retur++;
		}
		return retur;
	}
	
	public int getQuantidadeReports() {
		return reports.size();
	}
	
	public void removeReport(Report report) {
		reports.remove(report);
	}
	
	public boolean isAnalisando() {
		return analisando != null;
	}
	
	public void addReport(Report report) {
		if (report.isAtivo()) reports.add(report);
	}

	public String getAnalisando() {
		return analisando;
	}

	public void setAnalisando(String analisando) {
		this.analisando = analisando;
	}

	public String getJogador() {
		return jogador;
	}

	public List<Report> getReports() {
		return reports;
	}

	@Override
	public int compareTo(ReportPerfil o) {
		if (getQuantidadeReports() < o.getQuantidadeReports()) return -1;
		else if (getQuantidadeReports() > o.getQuantidadeReports()) return 1;
		else return 0;
	}

}
