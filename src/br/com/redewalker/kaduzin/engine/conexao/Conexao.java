package br.com.redewalker.kaduzin.engine.conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.com.redewalker.kaduzin.engine.WalkerEngine;

public class Conexao {
	
private String nome, pk;
	
	public Conexao(String nome, String chave) {
		this.nome = nome;
		this.pk = chave;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getChave() {
		return pk;
	}
	
	public boolean exists(String coluna, String valor) {
		String sql = "select * from "+nome+" where "+coluna+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, valor);
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean exists(String coluna, long valor) {
		String sql = "select * from "+nome+" where "+coluna+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, valor);
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean exists(String coluna, int valor) {
		String sql = "select * from "+nome+" where "+coluna+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setInt(1, valor);
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getString(int chave, String coluna) {
		String sql = "select "+coluna+ " from "+nome+" where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setInt(1, chave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getString(coluna);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getString(long chave, String coluna) {
		String sql = "select "+coluna+ " from "+nome+" where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, chave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getString(coluna);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getString(String chave, String coluna) {
		String sql = "select "+coluna+ " from "+nome+" where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, chave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getString(coluna);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public double getDouble(String chave, String coluna) {
		String sql = "select "+coluna+ " from "+nome+" where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, chave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getDouble(coluna);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public long getLong(long chave, String coluna) {
		String sql = "select "+coluna+ " from "+nome+" where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, chave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getLong(coluna);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public boolean getBoolean(long chave, String coluna) {
		String sql = "select "+coluna+ " from "+nome+" where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, chave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getBoolean(coluna);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean getBoolean(String chave, String coluna) {
		String sql = "select "+coluna+ " from "+nome+" where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, chave);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getBoolean(coluna);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean setString(long chave, String coluna, String valor) {
		String sql = "update "+nome+" set "+coluna+" = ? where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, valor);
			st.setLong(2, chave);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setString(String chave, String coluna, String valor) {
		String sql = "update "+nome+" set "+coluna+" = ? where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, valor);
			st.setString(2, chave);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setDouble(String chave, String coluna, double valor) {
		String sql = "update "+nome+" set "+coluna+" = ? where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setDouble(1, valor);
			st.setString(2, chave);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setBoolean(long chave, String coluna, boolean valor) {
		String sql = "update "+nome+" set "+coluna+" = ? where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setBoolean(1, valor);
			st.setLong(2, chave);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setBoolean(String chave, String coluna, boolean valor) {
		String sql = "update "+nome+" set "+coluna+" = ? where "+pk+" = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setBoolean(1, valor);
			st.setString(2, chave);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
