package br.com.redewalker.kaduzin.engine.conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.sistema.vip.VIP;

public class ConexaoVIPDAO extends Conexao {
	
	public ConexaoVIPDAO(String nome, String pk) {
		super(nome,pk);
	}
	
	public void checkTable() {
		String sql = "create table if not exists "+getNome()+" (id int not null auto_increment, nick varchar(30) not null, vip varchar(30) not null, server varchar(50) not null, tempo bigint default 1, ativo tinyint default 1, dias int default 0, dia bigint default 0, recebido varchar(30) default 'sem', removido varchar(30) default 'none', primary key (id));";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public VIP createVIP(String jogador, String vip, String server, long tempo, boolean ativo, long dia, String recebido, int dias) {
		PreparedStatement stmt = null;
		String sql = "insert into "+getNome()+"(nick,vip,tempo,ativo,dia,recebido,server,dias) values (?,?,?,?,?,?,?,?);";
		try {
			String[] returnId = { "id" };
			stmt = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql, returnId);
			stmt.setString(1, jogador);
			stmt.setString(2, vip);
			stmt.setLong(3, tempo);
			stmt.setBoolean(4, ativo);
			stmt.setLong(5, dia);
			stmt.setString(6, recebido);
			stmt.setString(7, server);
			stmt.setInt(8, dias);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) throw new SQLException("Creating user failed, no rows affected.");
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) return new VIP(rs.getLong(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isVIP(String nick) {
		String sql = "select * from "+getNome()+" where nick = ?";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if (rs.getLong("tempo") == 0L || rs.getLong("tempo") > System.currentTimeMillis()) {
					if (rs.getBoolean("ativo")) return true;
					else return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isAtivo(long id) {
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where id = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if (rs.getLong("tempo") == 0L || rs.getLong("tempo") > System.currentTimeMillis()) {
					if (rs.getBoolean("ativo")) return true;
					else return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public VIP getVip(long id) {
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where id = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				return new VIP(rs.getLong("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public VIP getVipOnlyAtivo(String p, String server) {
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where nick = ? and server = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, p);
			st.setString(2, server);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				long tempo = rs.getLong("tempo");
				if ((tempo < System.currentTimeMillis() && tempo != 0L) && rs.getBoolean("ativo")) return new VIP(rs.getLong("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public VIP getVipAtivo(String p, String server) {
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where nick = ? and server = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, p);
			st.setString(2, server);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				long tempo = rs.getLong("tempo");
				if ((tempo > System.currentTimeMillis() || tempo == 0L) && rs.getBoolean("ativo")) return new VIP(rs.getLong("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean containsPermanente(String jog, String vip, String server) {
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where nick = ? and vip = ? and server = ? and tempo = 0";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, jog);
			st.setString(2, vip);
			st.setString(3, server);
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isExpirado(long id) {
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where id = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				boolean ativo = rs.getBoolean("ativo");
				long tempo = rs.getLong("tempo");
				if ((ativo && tempo <= System.currentTimeMillis() && tempo != 0L) || (!ativo && tempo < 0L)) return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<VIP> getVipsValidosByUsuario(String p) {
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where nick = ? and tempo >= 0";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, p);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getVipsParaAtivar(String p, String server) {
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where nick = ? and server = ? and tempo = 0 and ativo = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, p);
			st.setString(2, server);
			st.setBoolean(3, false);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getVipsValidosByJogador(Usuario j, Servers server) {
		boolean rede = server.equals(Servers.Rede);
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where tempo >= 0 and nick = ? and ((ativo = true and  tempo > ?) or ativo = false)";
		if (!rede) sql += " and server = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, j.getNickOriginal());
			st.setLong(2, System.currentTimeMillis());
			if (!rede) st.setString(3, server.toString());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getVipsValidosByServer(Servers server) {
		boolean rede = server.equals(Servers.Rede);
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where tempo >= 0 and ((ativo = true and  tempo > ?) or ativo = false)";
		if (!rede) sql += " and server = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, System.currentTimeMillis());
			if (!rede) st.setString(2, server.toString());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public HashMap<String, ArrayList<VIP>> getVipsWithKey(Servers server) {
		boolean rede = server.equals(Servers.Rede);
		HashMap<String, ArrayList<VIP>> retorn = new HashMap<String, ArrayList<VIP>>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome();
		if (!rede) sql += " where server = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			if (!rede) st.setString(1, server.toString());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				String usuario = rs.getString("nick");
				if (!retorn.containsKey(usuario)) retorn.put(usuario, new ArrayList<VIP>());
				retorn.get(usuario).add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getVips(Servers server) {
		boolean rede = server.equals(Servers.Rede);
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome();
		if (!rede) sql += " where server = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			if (!rede) st.setString(1, server.toString());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getVips(String p, String server) {
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where nick = ? and server = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, p);
			st.setString(2, server);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getVipsAtivos(String p, String server) {
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where nick = ? and server = ? and ativo = ?";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, p);
			st.setString(2, server);
			st.setBoolean(3, true);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getAllVipsAtivos() {
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where ativo = ? and tempo <> 0 order by id desc;";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
	public ArrayList<VIP> getAllVipsTempo() {
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where tempo > ? order by id desc;";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, System.currentTimeMillis());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}

	public ArrayList<VIP> getAllVipsPerma() {
		ArrayList<VIP> retorn = new ArrayList<VIP>();
		PreparedStatement st = null;
		String sql = "select * from "+getNome()+" where tempo = ? order by id desc;";
		try {
			st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setLong(1, 0);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				retorn.add(new VIP(rs.getLong("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorn;
	}
	
}
