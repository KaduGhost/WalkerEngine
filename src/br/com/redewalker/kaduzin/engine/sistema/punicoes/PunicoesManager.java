package br.com.redewalker.kaduzin.engine.sistema.punicoes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.bungee.WalkerBungee;
import br.com.redewalker.kaduzin.engine.configuracao.Config;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.eventos.JogadorDespunidoEvent;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.eventos.JogadorPunidoEvent;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioDespunir;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunicoesType;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunir;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirAvaliados;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirHBanidos;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirHKicks;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirHMutes;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirLBanidos;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirLMutes;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirMBanidos;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirMMutes;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios.InventarioPunirMenu;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersInventarios;
import br.com.redewalker.kaduzin.engine.listeners.PunicoesListeners;

public class PunicoesManager implements Listener {
	
	private HashMap<PunicaoMotivoType, Integer> tempo;
	private Config config;
	
	public PunicoesManager(Config config) {
		this.config = config;
		checkData();
		load();
		Bukkit.getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	public void setAvaliado(long id, boolean aceito, String autor) {
		WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().setAvaliado(id, aceito, autor);
		if (!aceito) despunir(id, "n");
		atualizarInventarios();
	}
	
	public void abrirInventario(Player p, InventarioPunicoesType tipo, int pagina, String j2) {
		switch (tipo) {
		case MENU:
			new InventarioPunirMenu(p);
			return;
		case PUNIR:
			new InventarioPunir(p,j2);
			return;
		case DESPUNIR:
			new InventarioDespunir(p,j2);
			return;
		case AVALIADOS:
			new InventarioPunirAvaliados(pagina, p);
			return;
		case LISTABANIDOS:
			new InventarioPunirLBanidos(pagina, p);
			return;
		case LISTAMUTES:
			new InventarioPunirLMutes(pagina, p);
			return;
		case HISTORICOBANIDOS:
			new InventarioPunirHBanidos(pagina, p);
			return;
		case HISTORICOMUTES:
			new InventarioPunirHMutes(pagina, p);
			return;
		case HISTORICOKICKS:
			new InventarioPunirHKicks(pagina, p);
			return;
		case MEUSBANS:
			new InventarioPunirMBanidos(pagina, p);
			return;
		case MEUSMUTES:
			new InventarioPunirMMutes(pagina, p);
			return;
		default:
			break;
		}
	}
	
	public boolean jogadorPodePunir(Usuario j, PunicoesType tipo, int tempo) {
		/*switch (tipo) {
		case BAN:
			if (j.getGroup().getPriority() >= 6) return true;
			break;
		case IPBAN:
			if (j.getGroup().getPriority() >= 7) return true;
			break;
		case KICK:
			if (j.getGroup().getPriority() >= 6) return true;
			break;
		case MUTE:
			if ((j.getGroup().getPriority() >= 6 && tempo == 0) || (j.getGroup().getPriority() >= 5 && tempo != 0)) return true;
			break;
		default:
			break;
		}*/
		if (j.hasPermission("walker.punicao."+tipo.toString().toLowerCase())) return true;
		else if (j.hasPermission("walker.punicao.temp"+tipo.toString().toLowerCase()) && tempo != 0) return true;
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private void atualizarInventarios() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory().getType().equals(InventoryType.CHEST)) {
				Inventory inv = p.getOpenInventory().getTopInventory();
				InventarioPunicoesType tipo = PunicoesListeners.getTipoInventario(inv.getName());
				if (tipo == null) continue;
				if (tipo.equals(InventarioPunicoesType.MENU) || tipo.equals(InventarioPunicoesType.PUNIR) || tipo.equals(InventarioPunicoesType.DESPUNIR)) continue;
				int pagina = 0;
				if (inv.contains(49) && inv.getItem(49).getItemMeta().getDisplayName().contains("Pagina")) pagina = WalkersInventarios.getPagina(inv.getItem(49));
				else if (inv.contains(40) && inv.getItem(49).getItemMeta().getDisplayName().contains("Pagina")) pagina = WalkersInventarios.getPagina(inv.getItem(40));
				abrirInventario(p, tipo, pagina, null);
			}
		}
	}	
	
	public boolean exists(long id) {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().exists("id", id);
	}
	
	public Punicao getPunicao(long id) {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getPunicao(id);
	}
	
	public boolean isJogadorPunido(String jogador) {
		checkAtivos(jogador);
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().isJogadorPunido(jogador);
	}
	
	public List<Punicao> getPunicoes(String jogador) {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getPunicoesAtivas(jogador);
	}
	
	public Punicao getPunicaoAtiva(String jogador, PunicoesType tipo) {
		if (isJogadorPunido(jogador)) {
			for (Punicao puni : getPunicoes(jogador)) {
				if (puni.getTipo().equals(tipo)) return puni;
			}
		}
		return null;
	}
	
	public int getTempo(PunicaoMotivoType tipo) {
		return tempo.get(tipo);
	}
	
	public void punirJogador(String j, PunicaoMotivoType motivo, String comentario, int ate, String autor, String prova, PunicoesType tipo) {
		Punicao base = getPunicaoAtiva(j , tipo);
		Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(autor);
		long tempo = System.currentTimeMillis()+ate*60*1000L;
		if (!tipo.equals(PunicoesType.KICK) && j2 instanceof Usuario) {
			if (isJogadorPunido(j) && base != null) {
				if (base.isPermanente()) {
					MensagensAPI.mensagemErro("Este jogador já tem uma punição permanente do tipo que está tentando punir-lo", Bukkit.getPlayer(j2.getNickOriginal()));
					return;
				}else if (ate != 0 && tempo < base.getAte()) {
					MensagensAPI.mensagemErro("Este jogador já tem uma punição com tempo maior do tipo que está tentando punir-lo", Bukkit.getPlayer(j2.getNickOriginal()));
					return;
				}
			}
		}
		if (ate == 0) tempo = 0L;
		if (!motivo.equals(PunicaoMotivoType.OUTRO)) comentario = motivo.getDescricao();
		long punicao = 0L;
		if (tipo.equals(PunicoesType.KICK)) punicao = WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().createPunicao(true, false, false, j, "n", "n", System.currentTimeMillis(), System.currentTimeMillis(), autor, prova, PunicaoMotivoType.OUTRO, comentario, tipo);
		else punicao = WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().createPunicao(true, false, false, j, "n", "n", System.currentTimeMillis(), tempo, autor, prova, motivo, comentario, tipo);
		if (j2 instanceof Usuario && j2.hasPermission("walker.punir.avaliador")) setAvaliado(punicao, true, autor);
		Punicao punir = WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getPunicao(punicao);
		addAtiva(punir);
		atualizarInventarios();
		WalkerEngine.get().getServer().getPluginManager().callEvent(new JogadorPunidoEvent(j, punir));
	}
	
	public List<Punicao> getPunicoesParaAvaliar() {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getPunicoesParaAvaliar();
	}
	
	public List<Punicao> getPunicoesAtivas(PunicoesType tipo) {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getPunicoesAtivas(tipo);
	}
	
	public List<Punicao> getMeuHistoricoPunicoes(PunicoesType tipo, Usuario j) {
		List<Punicao> lista = new ArrayList<Punicao>();
		for (Punicao punicao : WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getAllPunicoes()) {
			if (tipo.equals(punicao.getTipo()) && j.getNickOriginal().equalsIgnoreCase(punicao.getAutor())) lista.add(punicao);
		}
		return lista;
	}
	
	public List<Punicao> getHistoricoPunicoes(PunicoesType tipo) {
		List<Punicao> lista = new ArrayList<Punicao>();
		for (Punicao punicao : WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getAllPunicoes()) {
			if (tipo.equals(punicao.getTipo())) lista.add(punicao);
		}
		return lista;
	}
	
	public List<Punicao> getPunicoesEmAvaliar(Usuario j) {
		return WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getPunicoesParaAvaliar(j.getNickOriginal());
	}
	
	public void despunir(long id, String autor) {
		Punicao punicao = getPunicao(id);
		punicao.setAtivo(false);
		punicao.setDespunidor(autor);
		checkAtivos(punicao.getJogador());
		atualizarInventarios();
		WalkerEngine.get().getServer().getPluginManager().callEvent(new JogadorDespunidoEvent(punicao.getJogador(), punicao));
	}
	
	public void despunir(String jogador, PunicoesType tipo, String autor) {
		if (isJogadorPunido(jogador)) {
			Punicao punicao = getPunicaoAtiva(jogador, tipo);
			if (punicao != null) despunir(punicao.getID(), autor);
			atualizarInventarios();
		}
	}
	
	private void load() {
		tempo = new HashMap<>();
		for (Punicao punicao : WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getAllPunicoes()) {
			if (punicao.isAtivo()) addAtiva(punicao);
		}
		for (PunicaoMotivoType grupo : EnumSet.allOf(PunicaoMotivoType.class)) {
			tempo.put(grupo, config.getPunicaoTempo(grupo.toString().toLowerCase()));
		}
	}
	
	private void checkData() {
		WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().checkTable();
	}
	
	private void addAtiva(Punicao punicao) {
		if (isJogadorPunido(punicao.getJogador())) {
			Punicao puni = getPunicaoAtiva(punicao.getJogador(), punicao.getTipo());
			if (puni != null) {
				if (puni.getID()==punicao.getID()) return;
				if (!puni.isPermanente() && (punicao.isPermanente() || puni.getAte() < punicao.getAte())) puni.setAtivo(false);
				else punicao.setAtivo(false);
			}
		}
	}
	
	private boolean isAtiva(Punicao punicao) {
		if (punicao.isAtivo() && (punicao.isPermanente() || punicao.getAte() > System.currentTimeMillis())) return true;
		else return false;
	}
	
	private void checkAtivos(String jogador) {
		boolean att = false;
		for (Punicao punicao : WalkerEngine.get().getConexaoManager().getConexaoPunicoesDAO().getAllPunicoesOnlyAtivas()) {
			if (!isAtiva(punicao)) {
				punicao.setAtivo(false);
				att = true;
			}
		}
		if (att) atualizarInventarios();
	}
	
	public String getMsgPunido(Punicao punicao) {
		String ate = WalkersUtils.formatedData(punicao.getAte()).replace(" ", " às ");
		String data = WalkersUtils.formatedData(punicao.getData()).replace(" ", " às ");
		if (punicao.getTipo().equals(PunicoesType.BAN)) {
			String msgKick = "§c§lWALKER NETWORK§c\n\n";
			if(punicao.isPermanente()) msgKick+="Você está banido permanentemente do servidor\n\n";
			else msgKick+="Você está banido temporariamente do servidor\nAté "+ate+"\n\n";
			msgKick+="Data da punição: "+data+"\n";
			msgKick+="Motivo: "+punicao.getComentario()+"\n";
			msgKick+="Autor: "+punicao.getAutor()+"\n";
			msgKick+="ID: §e#"+punicao.getID()+"§c\n";
			msgKick+="Utilize o ID acima caso queira pedir uma revisão\n";
			if (!punicao.isAvaliado()) msgKick+="§eEstá punição ainda não foi validada pela gerencia";
			return msgKick;
		}else if (punicao.getTipo().equals(PunicoesType.MUTE)) {
			String msg = "\n§c";
			if (punicao.isPermanente()) msg+="Você está mutado permanentemente do servidor\n\n";
			else msg+="Você esta mutado até "+ate+"!\n\n";
			msg+="§cMotivo: "+punicao.getComentario()+"\n";
			msg+="§cAutor: "+punicao.getAutor()+"\n";
			msg+="§cID: §e#"+punicao.getID()+"\n";
			msg+="§cUtilize o ID acima caso queira pedir uma revisão\n";
			if (!punicao.isAvaliado()) msg+="§eEstá punição ainda não foi validada pela gerencia\n";
			return msg+"\n§c ";
		}else if (punicao.getTipo().equals(PunicoesType.KICK)) {
			String msgKick = "§§c§lWALKER NETWORK§c\n\n";
			msgKick+="Você foi kickado\n\n";
			msgKick+="Data da punição: "+data+"\n";
			msgKick+="Motivo: "+punicao.getComentario()+"\n";
			msgKick+="Autor: "+punicao.getAutor()+"\n";
			msgKick+="ID: §e#"+punicao.getID()+"§c\n";
			return msgKick;
		}
		return "";
	}
	
	public String getMsg(Punicao punicao) {
		String ate = WalkersUtils.formatedData(punicao.getAte()).replace(" ", " às ");
		String msg = "\n§c";
		String tipo = "banido";
		if (punicao.getTipo().equals(PunicoesType.MUTE)) tipo = "mutado";
		else if (punicao.getTipo().equals(PunicoesType.KICK)) {
			msg+="O jogador "+punicao.getJogador()+" foi kickado!\n\n";
			msg+="§cMotivo: "+punicao.getComentario()+"\n";
			msg+="§cAutor: "+punicao.getAutor()+"\n";
			msg+="§cID: §e#"+punicao.getID()+"\n";
			return msg+"\n§c ";
		}
		if (punicao.isPermanente()) msg+="O jogador "+punicao.getJogador()+" está "+tipo+" permanentemente do servidor\n\n";
		else msg+="O jogador "+punicao.getJogador()+" está "+tipo+" até "+ate+"!\n\n";
		msg+="§cMotivo: "+punicao.getComentario()+"\n";
		msg+="§cAutor: "+punicao.getAutor()+"\n";
		msg+="§cID: §e#"+punicao.getID()+"\n";
		if (!punicao.isAvaliado()) msg+="§eEstá punição ainda não foi validada pela gerencia\n";
		return msg+"\n§c ";
	}
	
	@EventHandler
	void aoPunir(JogadorPunidoEvent e) {
		Bukkit.getScheduler().runTask(WalkerEngine.get(), new Runnable() {public void run() {
			Punicao punicao = e.getPunicao();
			Player p = Bukkit.getPlayer(e.getJogador());
			if (p instanceof Player) {
				if (!punicao.getTipo().equals(PunicoesType.MUTE)) p.kickPlayer(getMsgPunido(punicao));
				else p.sendMessage(getMsgPunido(punicao));
			}
			String msg = getMsg(punicao);
			WalkerBungee.sendBungeePunicao(punicao.getID()+"", punicao.getJogador(), punicao.getTipo().toString());
			for (Player p2 : Bukkit.getOnlinePlayers()) {
				Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(p2.getName());
				if (j2 instanceof Usuario && j2.hasPermission("walker.punir") && j2.isVerMsgPunicao()) p2.sendMessage(msg);
			}
		}});
	}
	
	@EventHandler
	void aoPunir(JogadorDespunidoEvent e) {
		Punicao punicao = e.getPunicao();
		Player p = Bukkit.getPlayer(e.getJogador());
		if (p instanceof Player) {
			if (punicao.getTipo().equals(PunicoesType.MUTE)) MensagensAPI.mensagemSucesso("Você foi desmutado.", p);
		}
	}

}
