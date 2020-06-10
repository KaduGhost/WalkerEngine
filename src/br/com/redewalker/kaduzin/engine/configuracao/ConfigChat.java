package br.com.redewalker.kaduzin.engine.configuracao;

import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigChat extends Configuration {

	public ConfigChat(JavaPlugin plugin) {
		super("chat", plugin);
		checkEstrutura();
	}

	public void checkEstrutura() {
		if (!get().contains("canal-default")) get().set("canal-default", "local");
		if (!get().contains("load-defaults-chats")) get().set("load-defaults-chats", true);
		if (!get().contains("load-defaults-chattags")) get().set("load-defaults-chattags", true);
		if (!get().contains("mostrar-mensagem-nenhum-jogador-proximo")) get().set("mostrar-mensagem-nenhum-jogador-proximo", true);
		if (!get().contains("protection.antispam.enable")) get().set("protection.antispam.enable", true);
		if (!get().contains("protection.antispam.tempo-entre-mensagens")) get().set("protection.antispam.tempo-entre-mensagens", 10);
		if (!get().contains("protection.antispam.desativar-em-canais")) get().set("protection.antispam.desativar-em-canais", Arrays.asList("teste"));
		if (!get().contains("protection.antiip.enable")) get().set("protection.antiip.enable", true);
		if (!get().contains("protection.antiip.regex-ip")) get().set("protection.antiip.regex-ip", "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])");
		if (!get().contains("protection.antiip.regex-url")) get().set("protection.antiip.regex-url", "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)");
		if (!get().contains("protection.antiip.whitelist")) get().set("protection.antiip.whitelist", Arrays.asList("prntscr.com", "gyazo.com"));
		if (!get().contains("protection.antiip.desativar-em-canais")) get().set("protection.antiip.desativar-em-canais", Arrays.asList("teste"));
		if (!get().contains("protection.antiip.cancelar-ou-replace")) get().set("protection.antiip.cancelar-ou-replace", "replace");
		if (!get().contains("protection.antiip.replace")) get().set("protection.antiip.replace", "-removido-");
		if (!get().contains("protection.antiip.punicao.enable")) get().set("protection.antiip.punicao.enable", true);
		if (!get().contains("protection.antiip.punicao.max-chances")) get().set("protection.antiip.punicao.max-chances", 3);
		if (!get().contains("protection.antiip.punicao.tempo-segundos")) get().set("protection.antiip.punicao.tempo-segundos", 240);
		save();
	}
	
	public boolean isAntiIPPunicao() {
		return get().getBoolean("protection.antiip.punicao.enable");
	}
	
	public int getAntiIPPunicaoTempo() {
		return get().getInt("protection.antiip.punicao.tempo-segundos");
	}
	
	public int getAntiIPPunicaoMaxChances() {
		return get().getInt("protection.antiip.punicao.max-chances");
	}
	
	public String getAntiIPReplace() {
		return get().getString("protection.antiip.replace");
	}
	
	public String getAntiIPCancelarOuReplace() {
		return get().getString("protection.antiip.cancelar-ou-replace");
	}
	
	public boolean isAntiIP() {
		return get().getBoolean("protection.antiip.enable");
	}
	
	public List<String> getAntiIPDesativarNosCanais() {
		return get().getStringList("protection.antiip.desativar-em-canais");
	}
	
	public List<String> getAntiIPWhitelist() {
		return get().getStringList("protection.antiip.whitelist");
	}
	
	public String getAntiIPRegexIP() {
		return get().getString("protection.antiip.regex-ip");
	}
	
	public String getAntiIPRegexURL() {
		return get().getString("protection.antiip.regex-url");
	}
	
	public int getAntiSpamTempoEntreMensagens() {
		return get().getInt("protection.antispam.tempo-entre-mensagens");
	}
	
	public boolean isAntiSpam() {
		return get().getBoolean("protection.antispam.enable");
	}
	
	public List<String> getAntiSpamDesativarNosCanais() {
		return get().getStringList("protection.antispam.desativar-em-canais");
	}
	
	public String getCanalDefault() {
		return get().getString("canal-default");
	}
	
	public boolean isLoadDefaultCanais() {
		return get().getBoolean("load-defaults-chats");
	}
	
	public boolean isLoadDefaultChatTags() {
		return get().getBoolean("load-defaults-chattags");
	}
	
	public boolean isMostrarMsgJogadorProximo() {
		return get().getBoolean("mostrar-mensagem-nenhum-jogador-proximo");
	}
	
	public void setLoadDefaultCanais(boolean b) {
		get().set("load-defaults-chats", b);
		save();
	}
	
	public void setLoadDefaultChatTags(boolean b) {
		get().set("load-defaults-chattags", b);
		save();
	}
	
}
