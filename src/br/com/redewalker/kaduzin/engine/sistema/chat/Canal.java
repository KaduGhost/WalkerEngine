package br.com.redewalker.kaduzin.engine.sistema.chat;

import java.util.ArrayList;

import br.com.redewalker.kaduzin.engine.sistema.tag.Tag;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public interface Canal {
	
	public boolean isBungee();
	
	public void setBungee(boolean set);
	
	public long getDelayRestante(Usuario nick);
	
	public void addNoDelay(Usuario nick);
	
	public void removeDelay(Usuario nick);
	
	public boolean isNoDelay(Usuario nick);

	public String getNome();

	public void setNome(String nome);

	public String getNick();

	public void setNick(String nick);

	public String getFormato();

	public void setFormato(String formato);

	public boolean isAtivo();

	public void setAtivo(boolean ativo);

	public Tag getTag();

	public void setTag(Tag tag);

	public int getCusto();

	public void setCusto(int custo);

	public int getDelay();

	public void setDelay(int delay);

	public int getDistancia();

	public void setDistancia(int distancia);

	public boolean isFocus();

	public void setFocus(boolean focus);

	public boolean isEntreMundos();

	public void setEntreMundos(boolean entreMundos);

	public boolean isMostrarMsgCusto();

	public void setMostrarMsgCusto(boolean mostrarMsgCusto);

	public ArrayList<String> getMundos();

	public void setMundos(ArrayList<String> mundos);
	
	public boolean podeNoMundo(String mundo);
	
	public void sendMessage(Usuario sender, String message);
	
	public boolean isIgnorando(Usuario j);
	
	public void addIgnorar(Usuario j);
	
	public void removeIgnorar(Usuario j);

}