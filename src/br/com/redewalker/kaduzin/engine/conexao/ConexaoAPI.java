package br.com.redewalker.kaduzin.engine.conexao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.configuracao.Config;

public class ConexaoAPI {
	
	private Connection con;
	private String host, usuario, senha, banco, porta;
	private File file;
	private ConexaoType tipo;
	
	public ConexaoAPI(Config config) {
		this.banco = config.getConexaoBanco();
		if (config.getConexaoMySQL()) {
			tipo = ConexaoType.MYSQL;
			this.usuario = config.getConexaoUsuario();
			this.senha = config.getConexaoSenha();
			this.porta = config.getConexaoPort();
			this.host = config.getConexaoHost();
		}
		else {
			file = new File(WalkerEngine.get().getDataFolder()+File.separator+banco+".db");
			tipo = ConexaoType.SQLITE;
		}
	}
	
	public ConexaoType getTipo() {
		return tipo;
	}
	
	public Connection getConexao() {
		try {
			if ((con != null) && (!con.isClosed())) return con;
			switch (tipo) {
			case MYSQL:
				this.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + porta + "/" + banco + "?autoReconnect=true", usuario, senha);
				Bukkit.getConsoleSender().sendMessage("§a"+WalkerEngine.get().getTag()+" Conexão com MySQL concluida com sucesso!");
				return con;
			default:
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						Bukkit.getConsoleSender().sendMessage("§c"+WalkerEngine.get().getTag()+" Erro ao criar o arquivo: " + banco + ".db");
					}
				}
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:" + file);
				Bukkit.getConsoleSender().sendMessage("§a"+WalkerEngine.get().getTag()+" Conexão com SQLite concluida com sucesso!");
				return con;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§c"+WalkerEngine.get().getTag()+" Erro ao tentar fazer conexão com o banco de dados");
			Bukkit.shutdown();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§c"+WalkerEngine.get().getTag()+" Erro ao tentar fazer conexão com o banco de dados");
		}
		return null;
	}
	
	public boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (columnName.equals(rsmd.getColumnName(x))) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void closeConnection() {
		try {
			if ((con == null) || (con.isClosed())) return;
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
