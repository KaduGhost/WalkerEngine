package br.com.redewalker.kaduzin.engine.sistema.reports;

public class Report implements Comparable<Report>{
	
	private int id;
	private String autor, reportado;
	private String comentario;
	private ReportMotivoType motivo;
	private long data;
	private boolean ativo;
	
	public Report(int id, String reportado, String autor, ReportMotivoType motivo, String comentario, long data, boolean ativo) {
		this.id = id;
		this.autor = autor;
		this.motivo = motivo;
		this.ativo = ativo;
		this.comentario = comentario;
		this.reportado = reportado;
		this.data = data;
	}
	
	public long getData() {
		return data;
	}
	
	public int getID() {
		return id;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getAutor() {
		return autor;
	}

	public ReportMotivoType getMotivo() {
		return motivo;
	}

	public String getComentario() {
		return comentario;
	}

	public String getReportado() {
		return reportado;
	}

	@Override
	public int compareTo(Report o) {
		if (data < o.getData()) return -1;
		else if (data > o.getData()) return 1;
		else return 0;
	}

}
