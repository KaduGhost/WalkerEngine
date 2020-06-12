package br.com.redewalker.kaduzin.engine.conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.staff.Staff;

public class ConexaoStaffDAO extends Conexao {
	
	public ConexaoStaffDAO(String nome, String pk) {
		super(nome,pk);
	}
	
	public void checkTable() {
		String sql = "create table if not exists "+getNome()+"(id int auto_increment, nick varchar(16) not null, grupo varchar(50) not null, server varchar(100), primary key(id));";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public GrupoType getGrupo(int id) {
		String sql = "select * from "+getNome()+" where id = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next()) return GrupoType.getTipo(rs.getString("grupo"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Staff getStaff(String nick, Servers server) {
		if (!isStaffServer(nick, server)) return null;
		String sql = "select * from "+getNome()+" where nick = ? and server = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.setString(2, server.toString());
			ResultSet rs = st.executeQuery();
			if (rs.next()) return new Staff(rs.getInt("id"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<Integer, Staff> getAllStaffsServer(String server) {
		HashMap<Integer, Staff> jogadores = new HashMap<>();
		String sql = "select * from "+getNome()+" where server = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, server);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Staff staff = new Staff(rs.getInt("id"));
				jogadores.put(rs.getInt("id"), staff);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jogadores;
	}
	
	public HashMap<Integer, Staff> getAllStaffs(String usuario) {
		HashMap<Integer, Staff> jogadores = new HashMap<>();
		String sql = "select * from "+getNome()+" where nick = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, usuario);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Staff staff = new Staff(rs.getInt("id"));
				jogadores.put(rs.getInt("id"), staff);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jogadores;
	}
	
	public HashMap<Integer, Staff> getAllStaffs() {
		HashMap<Integer, Staff> jogadores = new HashMap<>();
		String sql = "select * from "+getNome();
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Staff staff = new Staff(rs.getInt("id"));
				jogadores.put(rs.getInt("id"), staff);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jogadores;
	}
	
	public boolean isStaff(String nick) {
		String sql = "select * from "+getNome()+" where nick = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			return st.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isStaffServer(String nick, Servers server) {
		String sql = "select * from "+getNome()+" where nick = ? and server = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.setString(2, server.toString());
			return st.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean createStaff(String nick, GrupoType grupo, Servers server) {
		if (isStaffServer(nick, server)) return false;
		String sql = "insert into "+getNome()+"(nick,grupo,server) values(?,?,?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.setString(2, grupo.toString());
			st.setString(3, server.toString());
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void modifyStaff(int id, GrupoType grupo, Servers server) {
		if (!exists("id", id)) return; 
		String sql = "update "+getNome()+" set grupo = ?, server = ? where id = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, grupo.toString());
			st.setString(2, server.toString());
			st.setInt(3, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStaff(String nick) {
		String sql = "delete from "+getNome()+" where nick = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStaff(Servers tipo, String nick) {
		String sql = "delete from "+getNome()+" where nick = ? and server = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.setString(2, tipo.toString());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStaff(int id) {
		if (!exists("id", id)) return; 
		String sql = "delete from "+getNome()+" where id = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
