package br.com.redewalker.kaduzin.engine.conexao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.server.Server;

public class ConexaoServersDAO extends Conexao {
	
	public ConexaoServersDAO(String nome, String pk) {
		super(nome,pk);
	}
	
	public void checkTable() {
		String sql = "create table if not exists "+getNome()+"(server varchar(50), manutencao tinyint default 1, primary key(server));";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createServer(String nick, String nome, String tag, String cor, String herancas) {
		if (exists("nick", nick)) return false; 
		String sql = "insert into "+getNome()+"(server,manutencao) values(?,?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.setBoolean(2, true);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void saveAllServers(ArrayList<Server> jogadores) {
		try {
			PreparedStatement st;
			for (Server j : jogadores) {
				String sql;
				if (exists("server", j.getServer().toString())) continue;
				sql = "insert into "+getNome()+"(server,manutencao) values(?,?);";
				st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
				st.setString(1, j.getServer().toString());
				st.setBoolean(2, false);
				st.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
