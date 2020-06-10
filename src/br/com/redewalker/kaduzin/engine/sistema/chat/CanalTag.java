package br.com.redewalker.kaduzin.engine.sistema.chat;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public interface CanalTag {
	
	public boolean isPlayerCanUse(String jogador);
	
	public String getUrl();
	
	public String getSuggest();
	
	public String getExecute();
	
	public ItemStack getItem();
	
	public String getNome();
	
	public String getTag();
	
	public String getCor();
	
	public ArrayList<String> getOrdemHovers();

	public void setUrl(String url);
	
	public void setSugestao(String sugestao);
	
	public void setExecuteComando(String comando);
	
	public boolean isUrl();
	
	public boolean isSugestao();
	
	public boolean isExecuteComando();
	
	public boolean isHover();
	
	public boolean isItem();
	
	public boolean containsHover(String permission);
	
	public boolean containsHoverRecebedor(String permission);

	public ArrayList<String> getHover(String permission);
	
	public ArrayList<String> getHoverRecebedor(String permission);
	
	public HashMap<String, ArrayList<String>> getHovers();
	
	public HashMap<String, ArrayList<String>> getHoversRecebedor();
	
	public void setHover(String permission, ArrayList<String> hover);
	
	public void setHoverRecebedor(String permission, ArrayList<String> hover);
	
	public void removeHover(String permission);
	
	public void removeHoverRecebedor(String permission);
	
	public void removeAllHovers();
	
	public void removeHovers();
	
	public void removeHoverRecebedor();
	
	public void setItem(ItemStack item);

}
