package br.com.redewalker.kaduzin.engine.conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.Punicao;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.PunicaoMotivoType;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.PunicoesType;

public class ConexaoPunicoesDAO extends Conexao {
	
	public ConexaoPunicoesDAO(String nome, String pk) {
		super(nome,pk);
	}
	
	public void checkTable() {
		String sql = "create table if not exists "+getNome()+"(id int auto_increment, jogador varchar(16) not null, data long, ate long, autor varchar(16) not null, prova text not null, motivo varchar(100) not null, tipo varchar(16) not null, ativo tinyint, primary key(id));";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.execute();
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("select * from "+getNome()+"");
			ResultSet rs =st.executeQuery();
			if (!WalkerEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "avaliado")) {
				st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `avaliado` tinyint AFTER `ativo`");
				st.executeUpdate();
			}
			if (!WalkerEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "comentario")) {
				st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `comentario` text AFTER `ativo`");
				st.executeUpdate();
			}
			if (!WalkerEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "avaliador")) {
				st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `avaliador` text AFTER `comentario`");
				st.executeUpdate();
			}
			if (!WalkerEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "despunidor")) {
				st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `despunidor` text AFTER `avaliador`");
				st.executeUpdate();
			}
			if (!WalkerEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "avaliacao")) {
				st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `avaliacao` tinyint AFTER `despunidor`");
				st.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Punicao> getPunicoesParaAvaliar(String autor) {
		ArrayList<Punicao> punicoes = new ArrayList<Punicao>();
		String sql = "select * from "+getNome()+" where autor = ? and avaliado = ? and ativo = ? and (ate = ? or ate > ?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, autor);
			st.setBoolean(2, false);
			st.setBoolean(3, true);
			st.setLong(4, 0L);
			st.setLong(5, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Punicao(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public List<Punicao> getPunicoesParaAvaliar(PunicoesType tipo) {
		ArrayList<Punicao> punicoes = new ArrayList<Punicao>();
		String sql = "select * from "+getNome()+" where tipo = ? and avaliado = ? and ativo = ? and (ate = ? or ate > ?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, tipo.toString());
			st.setBoolean(2, false);
			st.setBoolean(3, true);
			st.setLong(4, 0L);
			st.setLong(5, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Punicao(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public List<Punicao> getPunicoesParaAvaliar() {
		ArrayList<Punicao> punicoes = new ArrayList<Punicao>();
		String sql = "select * from "+getNome()+" where avaliado = ? and ativo = ? and (ate = ? or ate > ?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setBoolean(1, false);
			st.setBoolean(2, true);
			st.setLong(3, 0L);
			st.setLong(4, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Punicao(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public List<Punicao> getPunicoesAtivas(PunicoesType tipo) {
		ArrayList<Punicao> punicoes = new ArrayList<Punicao>();
		String sql = "select * from "+getNome()+" where (tipo = ? and ativo = ?) and (ate = ? or ate > ?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, tipo.toString());
			st.setBoolean(2, true);
			st.setLong(3, 0L);
			st.setLong(4, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Punicao(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public List<Punicao> getPunicoesAtivas(String jogador) {
		ArrayList<Punicao> punicoes = new ArrayList<Punicao>();
		if (!exists("jogador", jogador)) return punicoes;
		String sql = "select * from "+getNome()+" where (jogador = ? and ativo = ?) and (ate = ? or ate > ?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, jogador);
			st.setBoolean(2, true);
			st.setLong(3, 0L);
			st.setLong(4, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Punicao(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public boolean isJogadorPunido(String jogador) {
		if (!exists("jogador", jogador)) return false;
		String sql = "select * from "+getNome()+" where (jogador = ? and ativo = ?) and (ate = ? or ate > ?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, jogador);
			st.setBoolean(2, true);
			st.setLong(3, 0L);
			st.setLong(4, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void setAvaliado(long id, boolean aceito, String autor) {
		if (!exists(id)) return;
		String sql = "update "+getNome()+" set avaliado = ?, avaliador = ?, avaliacao = ? where id = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setBoolean(1, true);
			st.setString(2, autor);
			st.setBoolean(3, aceito);
			st.setLong(4, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Punicao getPunicao(long id) {
		String sql = "select * from "+getNome()+" where id = ? order by id";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				return new Punicao(rs.getLong("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Punicao> getAllPunicoesOnlyAtivas() {
		List<Punicao> punicoes = new ArrayList<>();
		String sql = "select * from "+getNome()+" where ativo = ? order by id";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setBoolean(1, true);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Punicao(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public List<Punicao> getAllPunicoes() {
		List<Punicao> punicoes = new ArrayList<>();
		String sql = "select * from "+getNome()+" order by id";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				punicoes.add(new Punicao(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punicoes;
	}
	
	public long createPunicao(boolean ativo, boolean avaliado, boolean avaliacao, String jogador, String avaliador, String despunidor, long data, long ate, String autor, String prova, PunicaoMotivoType motivo, String comentario, PunicoesType tipo) {
		String sql = "insert into "+getNome()+"(jogador,data,ate,autor,prova,motivo,tipo,ativo,avaliado,comentario,avaliador,despunidor,avaliacao) values(?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			String[] returnId = { "id" };
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql, returnId);
			st.setString(1, jogador);
			st.setLong(2, data);
			st.setLong(3, ate);
			st.setString(4, autor);
			st.setString(5, prova);
			st.setString(6, motivo.toString());
			st.setString(7, tipo.toString());
			st.setBoolean(8, ativo);
			st.setBoolean(9, avaliado);
			st.setString(10, comentario);
			st.setString(11, avaliador);
			st.setString(12, despunidor);
			st.setBoolean(13, avaliacao);
			int affectedRows = st.executeUpdate();
			if (affectedRows == 0) throw new SQLException("Creating user failed, no rows affected.");
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	/*public void saveAllPunicoes(List<Punicao> punicoes) {
		try {
			PreparedStatement st;
			for (Punicao punicao : punicoes) {
				String sql;
				if (exists(punicao.getID())) {
					sql = "update "+getNome()+" set jogador = ?, data = ?, ate = ?, autor = ?, prova = ?, motivo = ?, tipo = ?, ativo = ?, avaliado = ?, comentario = ?, avaliador =?, despunidor = ?, avaliacao = ? where id = ?;";
					st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
					st.setString(1, punicao.getJogador());
					st.setLong(2, punicao.getData());
					st.setLong(3, punicao.getAte());
					st.setString(4, punicao.getAutor());
					st.setString(5, punicao.getProva());
					st.setString(6, punicao.getMotivo().toString());
					st.setString(7, punicao.getTipo().toString());
					st.setBoolean(8, punicao.isAtivo());
					st.setBoolean(9, punicao.isAvaliado());
					st.setString(10, punicao.getComentario());
					st.setString(11, punicao.getAvaliador());
					st.setString(12, punicao.getDespunidor());
					st.setBoolean(13, punicao.isAvaliacao());
					st.setLong(14, punicao.getID());
				}else {
					sql = "insert into "+getNome()+"(id,jogador,data,ate,autor,prova,motivo,tipo,ativo,avaliado,comentario,avaliador,despunidor,avaliacao) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
					st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
					st.setLong(1, punicao.getID());
					st.setString(2, punicao.getJogador());
					st.setLong(3, punicao.getData());
					st.setLong(4, punicao.getAte());
					st.setString(5, punicao.getAutor());
					st.setString(6, punicao.getProva());
					st.setString(7, punicao.getMotivo().toString());
					st.setString(8, punicao.getTipo().toString());
					st.setBoolean(9, punicao.isAtivo());
					st.setBoolean(10, punicao.isAvaliado());
					st.setString(11, punicao.getComentario());
					st.setString(12, punicao.getAvaliador());
					st.setString(13, punicao.getDespunidor());
					st.setBoolean(14, punicao.isAvaliacao());
				}
				st.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	
	public boolean exists(long l) {
		String sql = "select * from "+getNome()+" where id = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, l);
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
