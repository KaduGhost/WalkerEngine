package br.com.redewalker.kaduzin.engine.sistema.chat;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class ChatTag implements CanalTag {
	
	private String nome, url, suggest, execute, tag, cor;
	private HashMap<String, ArrayList<String>> hovers;
	private HashMap<String, ArrayList<String>> hoverRecebedor;
	private ItemStack showItem;
	private ArrayList<String> ordemHovers;
	
	public ChatTag(String tag, String cor, String nome) {
		this.tag = tag.replace("&", "§");
		this.cor = cor.replace("&", "§");
		this.nome = nome;
		this.url = this.suggest = this.execute = null;
		this.hovers = null;
		this.showItem = null;
	}
	
	public boolean isPlayerCanUse(String jogador) {
		return true;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getSuggest() {
		return suggest;
	}
	
	public String getExecute() {
		return execute;
	}
	
	public ItemStack getItem() {
		return showItem;
	}
	
	public String getNome() {
		return "{" + nome.replace("{", "").replace("}", "") + "}";
	}
	
	public String getTag() {
		return tag;
	}
	
	public String getCor() {
		return cor;
	}
	
	public ArrayList<String> getOrdemHovers() {
		return ordemHovers;
	}

	public void setUrl(String url) {
		this.url = url;
		this.suggest = this.execute = null;
	}
	
	public void setSugestao(String sugestao) {
		this.suggest = sugestao;
		this.url = this.execute = null;
	}
	
	public void setExecuteComando(String comando) {
		this.execute = comando;
		this.url = this.suggest = null;
	}
	
	public boolean isUrl() {
		return url != null;
	}
	
	public boolean isSugestao() {
		return suggest != null;
	}
	
	public boolean isExecuteComando() {
		return execute != null;
	}
	
	public boolean isHover() {
		return (hovers != null && !hovers.isEmpty()) || (hoverRecebedor != null && !hoverRecebedor.isEmpty());
	}
	
	public boolean isItem() {
		return showItem != null;
	}
	
	public boolean containsHover(String permission) {
		return hovers.containsKey(permission);
	}
	
	public boolean containsHoverRecebedor(String permission) {
		return hoverRecebedor.containsKey(permission);
	}

	public ArrayList<String> getHover(String permission) {
		return hovers.get(permission.toLowerCase());
	}
	
	public ArrayList<String> getHoverRecebedor(String permission) {
		return hoverRecebedor.get(permission.toLowerCase());
	}
	
	public HashMap<String, ArrayList<String>> getHovers() {
		return hovers;
	}
	
	public HashMap<String, ArrayList<String>> getHoversRecebedor() {
		return hoverRecebedor;
	}

	public void setHover(String permission, ArrayList<String> hover) {
		if (hovers == null) hovers = new HashMap<String, ArrayList<String>>();
		if (ordemHovers == null) ordemHovers = new ArrayList<String>();
		this.hovers.put(permission.toLowerCase(), hover);
		this.ordemHovers.add(permission);
		this.showItem = null;
	}
	
	public void setHoverRecebedor(String permission, ArrayList<String> hover) {
		if (hoverRecebedor == null) hoverRecebedor = new HashMap<String, ArrayList<String>>();
		if (ordemHovers == null) ordemHovers = new ArrayList<String>();
		this.hoverRecebedor.put(permission.toLowerCase(), hover);
		this.ordemHovers.add(permission);
		this.showItem = null;
	}
	
	public void removeHover(String permission) {
		if (hovers != null) {
			hovers.remove(permission);
			if (hovers.size() == 0) hovers = null;
			ordemHovers.remove(permission);
		}
	}
	
	public void removeHoverRecebedor(String permission) {
		if (hoverRecebedor != null) {
			hoverRecebedor.remove(permission);
			if (hoverRecebedor.size() == 0) hoverRecebedor = null;
			ordemHovers.remove(permission);
		}
	}
	
	public void removeAllHovers() {
		hovers = null;
		hoverRecebedor = null;
		ordemHovers = new ArrayList<String>();
	}
	
	public void setItem(ItemStack item) {
		this.showItem = item;
		this.hovers = null;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTag(String nome) {
		this.tag = nome.replace("&", "§");
	}

	public void setCor(String nome) {
		this.cor = nome.replace("&", "§");
	}
	
}
