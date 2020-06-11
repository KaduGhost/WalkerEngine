package br.com.redewalker.kaduzin.engine.configuracao;

import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;


public class Config extends Configuration {

	public Config(JavaPlugin plugin) {
		super("config", plugin);
		checkEstrutura();
	}
	
	public void checkEstrutura() {
		if (!get().contains("Conexao.MySQL")) {
			get().set("Conexao.MySQL.ativar", false);
			get().set("Conexao.host", "localhost");
			get().set("Conexao.port", "3306");
			get().set("Conexao.usuario", "root");
			get().set("Conexao.senha", "kaduzin");
			get().set("Conexao.banco", "hpteste");
		}
		if (!get().contains("ConexaoServer.MySQL")) {
			get().set("ConexaoServer.MySQL.ativar", false);
			get().set("ConexaoServer.host", "localhost");
			get().set("ConexaoServer.port", "3306");
			get().set("ConexaoServer.usuario", "root");
			get().set("ConexaoServer.senha", "kaduzin");
			get().set("ConexaoServer.banco", "hpteste");
		}
		if (!get().contains("Permissoes.load-default-permissions")) get().set("Permissoes.load-default-permissions", true);
		if (!get().contains("Mensagens.erro")) get().set("Mensagens.erro", "&c");
		if (!get().contains("Mensagens.sucesso")) get().set("Mensagens.sucesso", "&a");
		if (!get().contains("Warp.principal")) get().set("Warp.principal", "nenhuma");
		if (!get().contains("Punicoes.motivos.bug")) get().set("Punicoes.motivos.bug", 0);
		if (!get().contains("Punicoes.motivos.iniflood")) get().set("Punicoes.motivos.iniflood", 60);
		if (!get().contains("Punicoes.motivos.ameaca")) get().set("Punicoes.motivos.ameaca", 4320);
		if (!get().contains("Punicoes.motivos.desinformacao")) get().set("Punicoes.motivos.desinformacao", 180);
		if (!get().contains("Punicoes.motivos.antijogochat")) get().set("Punicoes.motivos.antijogochat", 180);
		if (!get().contains("Punicoes.motivos.antijogojogo")) get().set("Punicoes.motivos.antijogojogo", 2880);
		if (!get().contains("Punicoes.motivos.discriminacao")) get().set("Punicoes.motivos.discriminacao", 2880);
		if (!get().contains("Punicoes.motivos.contafake")) get().set("Punicoes.motivos.contafake", 0);
		if (!get().contains("Punicoes.motivos.divulgacao")) get().set("Punicoes.motivos.divulgacao", 720);
		if (!get().contains("Punicoes.motivos.divulgacaoserver")) get().set("Punicoes.motivos.divulgacaoserver", 0);
		if (!get().contains("Punicoes.motivos.estorno")) get().set("Punicoes.motivos.estorno", 0);
		if (!get().contains("Punicoes.motivos.floodspam")) get().set("Punicoes.motivos.floodspam", 60);
		if (!get().contains("Punicoes.motivos.hack")) get().set("Punicoes.motivos.hack", 0);
		if (!get().contains("Punicoes.motivos.ofensajogador")) get().set("Punicoes.motivos.ofensajogador", 720);
		if (!get().contains("Punicoes.motivos.ofensastaff")) get().set("Punicoes.motivos.ofensastaff", 0);
		if (!get().contains("Punicoes.motivos.modnaopermitido")) get().set("Punicoes.motivos.modnaopermitido", 0);
		if (!get().contains("Voar.mundos-permitidos")) get().set("Voar.mundos-permitidos", Arrays.asList("world", "world_the_end", "world_nether"));
		if (!get().contains("Motd.Motds")) get().set("Motd.Motds", Arrays.asList("&2&lWalkers MC &7(1.8.x) - &e&lFactions &a&lApocalipse\n&eServidor em §b§lDESENVOLVIMENTO", "&2&lWalkers MC &7(1.8.x) - &e&lFactions &a&lApocalipse\n&dVarias novidades no ar. Chega ai mermão!"));
		if (!get().contains("Manutencao.em-manutencao")) get().set("Manutencao.em-manutencao", false);
		if (!get().contains("Manutencao.tempo-inicio-manutencao")) get().set("Manutencao.tempo-inicio-manutencao", 300);
		if (!get().contains("Manutencao.tempo-inicio-stop")) get().set("Manutencao.tempo-inicio-stop", 150);
		if (!get().contains("Manutencao.manutencao-motd")) get().set("Manutencao.manutencao-motd", "&c&lWalkers MC &7(1.8.x) - &c&lServidor em manutenção\n§cVoltamos em breve.");
		if (!get().contains("Manutencao.stop-motd")) get().set("Manutencao.stop-motd", "&c&lWalkers MC &7(1.8.x) - &c&lServidor reiniciando\n§cVoltamos daqui a pouco.");
		if (!get().contains("Limite-Jogadores")) get().set("Limite-Jogadores", 20);
		if (!get().contains("Expulsar-sem-vip")) get().set("Expulsar-sem-vip", true);
		if (!get().contains("Tempo-para-expulsar")) get().set("Tempo-para-expulsar", 60);
		save();
	}
	
	public List<String> getVoarMundos() {
		return get().getStringList("Voar.mundos-permitidos");
	}
	
	public void setLoadDefaultPermissions(boolean b) {
		get().set("Permissoes.load-default-permissions", b);
		save();
	}
	
	public boolean getLoadDefaultPermissions() {
		return get().getBoolean("Permissoes.load-default-permissions");
	}
	
	public int getTempoKickNaoVip() {
		return get().getInt("Tempo-para-expulsar");
	}
	
	public boolean isExpulsarSemVip() {
		return get().getBoolean("Expulsar-sem-vip");
	}
	
	public int getLimiteJogadores() {
		return get().getInt("Limite-Jogadores");
	}
	
	public String getManutencaoManuMotd() {
		return get().getString("Manutencao.manutencao-motd");
	}
	
	public String getManutencaoStopMotd() {
		return get().getString("Manutencao.stop-motd");
	}
	
	public boolean isManutencao() {
		return get().getBoolean("Manutencao.em-manutencao");
	}
	
	public void setManutencao(boolean set) {
		 get().set("Manutencao.em-manutencao", set);
		 save();
	}
	
	public int getManutencaoTempoStop() {
		return get().getInt("Manutencao.tempo-inicio-stop");
	}
	
	public int getManutencaoTempoManu() {
		return get().getInt("Manutencao.tempo-inicio-manutencao");
	}
	
	public List<String> getMotds() {
		return get().getStringList("Motd.Motds");
	}
	
	public List<String> getGruposHierarquia() {
		return get().getStringList("Grupos.hierarquia");
	}
	
	public int getPunicaoTempo(String motivo) {
		return get().getInt("Punicoes.motivos."+motivo);
	}
	
	public String getWarpPrincipal() {
		return get().getString("Warp.principal");
	}
	
	public void setWarpPrincipal(String nome) {
		get().set("Warp.principal", nome);
		save();
	}
	
	public String getMensagensErro() {
		return get().getString("Mensagens.erro").replace("&", "§");
	}
	
	public String getMensagensSucesso() {
		return get().getString("Mensagens.sucesso").replace("&", "§");
	}
	
	public boolean getConexaoMySQL() {
		return get().getBoolean("Conexao.MySQL.ativar");
	}

	public String getConexaoHost() {
		return get().getString("Conexao.host");
	}

	public String getConexaoPort() {
		return get().getString("Conexao.port");
	}

	public String getConexaoUsuario() {
		return get().getString("Conexao.usuario");
	}

	public String getConexaoSenha() {
		return get().getString("Conexao.senha");
	}

	public String getConexaoBanco() {
		return get().getString("Conexao.banco");
	}
	
	public boolean getConexaoServerMySQL() {
		return get().getBoolean("ConexaoServer.MySQL.ativar");
	}

	public String getConexaoServerHost() {
		return get().getString("ConexaoServer.host");
	}

	public String getConexaoServerPort() {
		return get().getString("ConexaoServer.port");
	}

	public String getConexaoServerUsuario() {
		return get().getString("ConexaoServer.usuario");
	}

	public String getConexaoServerSenha() {
		return get().getString("ConexaoServer.senha");
	}

	public String getConexaoServerBanco() {
		return get().getString("ConexaoServer.banco");
	}

}