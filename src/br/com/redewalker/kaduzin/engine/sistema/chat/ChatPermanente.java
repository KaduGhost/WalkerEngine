package br.com.redewalker.kaduzin.engine.sistema.chat;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.tag.Tag;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ChatPermanente implements Canal {
	
	private String nome, nick, formato;
	private boolean ativo;
	private Tag tag;
	private int custo, delay, distancia;
	private boolean focus, entreMundos, mostrarMsgCusto;
	private ArrayList<String> mundos;
	private ArrayList<String> ignorando;
	private HashMap<String, Long> delayList;
	
	public ChatPermanente(String nome, String nick, String formato, boolean ativo, boolean focus, boolean entreMundos, boolean mostrarMsgCusto, Tag tag, int custo, int delay, int distancia, ArrayList<String> mundos) {
		this.nome = nome;
		this.nick = nick;
		this.formato = formato;
		this.ativo = ativo;
		this.tag = tag;
		this.custo = custo;
		this.delay = delay;
		this.distancia = distancia;
		this.focus = focus;
		this.entreMundos = entreMundos;
		this.mostrarMsgCusto = mostrarMsgCusto;
		this.mundos = mundos;
		this.ignorando = new ArrayList<String>();
		this.delayList = new HashMap<String, Long>();
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public int getCusto() {
		return custo;
	}

	public void setCusto(int custo) {
		this.custo = custo;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}
	
	public boolean isEntreMundos() {
		return entreMundos;
	}

	public void setEntreMundos(boolean entreMundos) {
		this.entreMundos = entreMundos;
	}

	public boolean isMostrarMsgCusto() {
		return mostrarMsgCusto;
	}

	public void setMostrarMsgCusto(boolean mostrarMsgCusto) {
		this.mostrarMsgCusto = mostrarMsgCusto;
	}

	public ArrayList<String> getMundos() {
		return mundos;
	}

	public void setMundos(ArrayList<String> mundos) {
		this.mundos = mundos;
	}

	public boolean podeNoMundo(String mundo) {
		return mundos.contains(mundo);
	}

	public void sendMessage(Usuario j, String message) {
		Player sender = j.getPlayer();
		if (!(sender instanceof Player)) return;
		boolean prox = false;
		boolean prox1 = false;
		if (isIgnorando(j)) removeIgnorar(j);
		for (Player p : Bukkit.getOnlinePlayers()) {
			Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
			if (j.getNickOriginal().equalsIgnoreCase(j2.getNickOriginal())) {
				ChatUtils.formatarMensagem(this, j, message, j2).send(p);
				continue;
			}
			if (getDistancia() > 0) {
				if (sender.getLocation().distance(p.getLocation()) <= getDistancia()) {
					if (j2.hasPermission("walker.chat.chat"+nome) && (!j2.isVanish() /*|| VanishAPI.jogadorCanSeeVanish(j)*/)) prox = true;
					if (j2.hasPermission("walker.chat.chat"+nome)) prox1 =true;
				}
				if (!isEntreMundos()) {
					if (mundos.size() == 0 || !mundos.contains(sender.getLocation().getWorld().getName().toLowerCase())) {
						MensagensAPI.mensagemErro("Este canal não pode ser utilizado neste mundo",sender);
						return;
					}
				}
			}else {
				prox1 =true;
				prox = true;
			}
			if (j2.hasPermission("walker.chat.chat"+nome)) {
				if (prox1 && !isIgnorando(j2)) ChatUtils.formatarMensagem(this, j, message, j2).send(p);
			}
		}
		if (!prox && getDistancia() > 0) {
			new BukkitRunnable() {
				@Override
				public void run() {
					MensagensAPI.mensagemErro("Nenhum jogador que pode ver este canal está proximo",sender);
				}
			}.runTaskLater(WalkerEngine.get(), 15);
		}
	}

	public boolean isIgnorando(Usuario j) {
		return ignorando.contains(j.getNickOriginal());
	}

	public void addIgnorar(Usuario j) {
		if (!isIgnorando(j)) ignorando.add(j.getNickOriginal());
	}

	public void removeIgnorar(Usuario j) {
		if (isIgnorando(j)) ignorando.remove(j.getNickOriginal());
	}

	public void addNoDelay(Usuario nick) {
		if (!isNoDelay(nick)) delayList.put(nick.getNickOriginal(), System.currentTimeMillis()+(delay*1000L));
		else {
			if (delayList.get(nick.getNickOriginal()) < System.currentTimeMillis()) delayList.put(nick.getNickOriginal(), System.currentTimeMillis()+(delay*1000L));
		}
	}
	
	public long getDelayRestante(Usuario j) {
		if (!isNoDelay(j)) return 0L;
		return (delayList.get(j.getNickOriginal()) - System.currentTimeMillis())/ 1000; 
	}

	public void removeDelay(Usuario nick) {
		if (delayList.containsKey(nick.getNickOriginal())) delayList.remove(nick.getNickOriginal());
	}

	public boolean isNoDelay(Usuario nick) {
		if (delayList.containsKey(nick.getNickOriginal())) {
			if (delayList.get(nick.getNickOriginal()) < System.currentTimeMillis()) removeDelay(nick);
			else return true;
		}
		return false;
	}
	
}
