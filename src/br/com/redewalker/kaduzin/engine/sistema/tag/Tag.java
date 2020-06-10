package br.com.redewalker.kaduzin.engine.sistema.tag;

import org.bukkit.ChatColor;

public class Tag {
	
	private String tag;
	private ChatColor cor;
	
	public Tag(String tag, ChatColor cor) {
		this.tag = tag;
		if (cor.isColor()) this.cor = cor;
		else this.cor = ChatColor.WHITE;
	}
	
	public Tag(String tag, String cor) {
		this.tag = tag;
		if (cor.equalsIgnoreCase("nenhum")) this.cor = ChatColor.WHITE;
		else this.cor = ChatColor.getByChar(cor.replace("§", "").replace("&", ""));
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public ChatColor getCor() {
		return cor;
	}

	public void setCor(ChatColor cor) {
		this.cor = cor;
	}
	
	public String toString() {
		return cor+tag;
	}

}
