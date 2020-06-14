package br.com.redewalker.kaduzin.engine.sistema.reports.eventos;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.reports.Report;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportPerfil;

public class ReportCreateEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Report report;
	
	public ReportCreateEvent(Report report) {
		this.report = report;
	}
	
	public Report getReport() {
		return report;
	}
	
	public ReportPerfil getReportPerfil() {
		return WalkerEngine.get().getReportManager().getReportPerfil(report.getReportado());
	}
	
	public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
