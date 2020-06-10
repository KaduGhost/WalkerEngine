package br.com.redewalker.kaduzin.engine.conexao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import br.com.redewalker.kaduzin.engine.WalkerEngine;

public class ConexaoGrupoDAO extends Conexao {
	
	public ConexaoGrupoDAO(String nome, String pk) {
		super(nome,pk);
	}
	
	public void checkTable() {
		String sql = "create table if not exists "+getNome()+"(nick varchar(50), nome varchar(100) not null default 'membro', tag varchar(50) not null default 'membro', cor varchar(10) not null default '&a', herancas longtext, primary key(nick));";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createGrupo(String nick, String nome, String tag, String cor, String herancas) {
		if (exists("nick", nick)) return false; 
		String sql = "insert into "+getNome()+"(nick,nome,tag,cor,herancas) values(?,?,?,?,?)";
		try {
			PreparedStatement st = WalkerEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.setString(2, nome);
			st.setString(3, tag);
			st.setString(4, cor);
			st.setString(5, herancas);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
