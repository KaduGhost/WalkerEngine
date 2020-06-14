package br.com.redewalker.kaduzin.engine.conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.reports.Report;
import br.com.redewalker.kaduzin.engine.sistema.reports.ReportMotivoType;

public class ConexaoReportDAO extends Conexao {
	
	public ConexaoReportDAO(String nome, String pk) {
		super(nome,pk);
	}
	
	
	public void checkTable() {
		String sql = "create table if not exists "+getNome()+"(id int auto_increment, jogador varchar(16) not null, reportado varchar(16) not null, data long, motivo varchar(50) not null, comentario varchar(100) not null, ativo tinyint, primary key(id));";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoServerAPI().getConexao().prepareStatement(sql);
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Report> getAllReports() {
		List<Report> punicoes = new ArrayList<>();
		String sql = "select * from "+getNome()+" order by id";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoServerAPI().getConexao().prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Report((rs.getInt("id")), rs.getString("reportado"), rs.getString("jogador"), ReportMotivoType.getByNome(rs.getString("motivo")), rs.getString("comentario"), rs.getLong("data"), rs.getBoolean("ativo")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public void saveAllReports(List<Report> punicoes) {
		try {
			PreparedStatement st;
			for (Report punicao : punicoes) {
				String sql;
				if (exists(punicao.getID())) {
					sql = "update "+getNome()+" set jogador = ?, reportado = ?, data = ?, motivo = ?, comentario = ?, ativo = ? where id = ?;";
					st = WalkerEngine.get().getConexaoManager().getConexaoServerAPI().getConexao().prepareStatement(sql);
					st.setString(1, punicao.getAutor());
					st.setString(2, punicao.getReportado());
					st.setLong(3, punicao.getData());
					st.setString(4, punicao.getMotivo().toString().toLowerCase());
					st.setString(5, punicao.getComentario());
					st.setBoolean(6, punicao.isAtivo());
					st.setInt(7, punicao.getID());
				}else {
					sql = "insert into "+getNome()+"(id,jogador,reportado,data,motivo,comentario,ativo) values(?,?,?,?,?,?,?);";
					st = WalkerEngine.get().getConexaoManager().getConexaoServerAPI().getConexao().prepareStatement(sql);
					st.setInt(1, punicao.getID());
					st.setString(2, punicao.getAutor());
					st.setString(3, punicao.getReportado());
					st.setLong(4, punicao.getData());
					st.setString(5, punicao.getMotivo().toString().toLowerCase());
					st.setString(6, punicao.getComentario());
					st.setBoolean(7, punicao.isAtivo());
				}
				st.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void apagarReports(ArrayList<Integer> apagados) {
		PreparedStatement st;
		for (int id : apagados) {
			String sql = "DELETE FROM "+getNome()+" WHERE id = ?";
			try {
				st = WalkerEngine.get().getConexaoManager().getConexaoServerAPI().getConexao().prepareStatement(sql);
				st.setInt(1, id);
				st.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean exists(int l) {
		String sql = "select * from "+getNome()+" where id = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoServerAPI().getConexao().prepareStatement(sql);
			st.setInt(1, l);
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
