package br.com.redewalker.kaduzin.engine.sistema.manutencao;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.configuracao.Config;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.Title;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils;

public class ManutencaoManager implements Listener {
	
	private boolean aberto, liberado, processo, travar;
	private Manutencao manutencao;
	private int tempoStop, tempoManutencao, countdown;
	private Title titleContagem;
	private Config config;
	
	public ManutencaoManager(Config config) {
		this.config = config;
		load();
		new BukkitRunnable(){@Override public void run() {setAberto(true);}}.runTaskLater(WalkerEngine.get(), 20*5);
		Bukkit.getServer().getPluginManager().registerEvents(this, WalkerEngine.get());
	}
	
	public Manutencao getManutencao() {
		return manutencao;
	}
	
	public void iniciarManutencao(ManutencaoType tipo, int tempo, boolean fast) {
		if (isProcesso()) return;
		setProcesso(true);
		if (tipo.equals(ManutencaoType.MANUTENCAO)) manutencao = new ProcessoManutencao(tipo);
		else manutencao = new ProcessoStop(tipo);
		if (tempo == 0 && tipo.equals(ManutencaoType.MANUTENCAO)) tempo=tempoManutencao;
		else if (tempo == 0 && tipo.equals(ManutencaoType.STOP)) tempo=tempoStop;
		iniciarCountDown(tempo);
	}
	
	public boolean isAberto() {
		return aberto;
	}
	
	public void setAberto(boolean set) {
		if (set) Bukkit.getConsoleSender().sendMessage(" §aServidor aberto");
		else {
			kickPlayers(manutencao.getMensagemKick(), manutencao.isIgnoreBypassKick());
			Bukkit.getConsoleSender().sendMessage(" §cServidor fechado");
		}
		aberto = set;
	}
	
	private void iniciarCountDown(int tempo) {
		countdown = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(WalkerEngine.get(), new Runnable() {
            long i = tempo;
            public void run() {
            	if ((i%30 == 0 || i == tempo) && i != 0) {
            		new Title("§4"+manutencao.getTipo().getNome(), "§c"+manutencao.getTipo().getNome()+" em "+WalkersUtils.getTempoRestante(i)).broadcast();
            		Bukkit.getConsoleSender().sendMessage("§4"+manutencao.getTipo().getNome()+"\n§c"+manutencao.getTipo().getNome()+" em "+WalkersUtils.getTempoRestante(i));
            	}else if (i == 10) {
            		titleContagem = new Title("§4"+manutencao.getTipo().getNome(), "§c"+manutencao.getTipo().getNome()+" em "+WalkersUtils.getTempoRestante(i), 1, 11*20, 1);
            		titleContagem.broadcast();
            		Bukkit.getConsoleSender().sendMessage("§4"+manutencao.getTipo().getNome()+"\n§c"+manutencao.getTipo().getNome()+" em "+WalkersUtils.getTempoRestante(i));
            	}else if ((i < 10 && i > 0)) {
            		titleContagem.setSubtitle("§c"+manutencao.getTipo().getNome()+" em "+WalkersUtils.getTempoRestante(i));
            		for (Player p : Bukkit.getOnlinePlayers()) {
            			titleContagem.updateSubtitle(p);
            		}
            		Bukkit.getConsoleSender().sendMessage("§4"+manutencao.getTipo().getNome()+"\n§c"+manutencao.getTipo().getNome()+" em "+WalkersUtils.getTempoRestante(i));
            	}else if (i == 0) {
            		titleContagem.setSubtitle("§c"+manutencao.getTipo().getNome()+" iniciado.");
            		for (Player p : Bukkit.getOnlinePlayers()) {
            			titleContagem.updateSubtitle(p);
            		}
            		manutencao.run();
            		Bukkit.getScheduler().cancelTask(countdown);
            		travar = false;
            	}
            	if (i < 2 && !travar) travar = true;
            	i--;
            }
        }
        , 0L, 20L);
	}
	
	public void setLiberado(boolean set) {
		this.liberado = set;
		WalkerEngine.get().getServerManager().getServer().setManutencao(!set);
		if (!liberado) kickPlayers(manutencao.getMensagemKick(), manutencao.isIgnoreBypassKick());
	}
	
	public void setProcesso(boolean set) {
		this.processo = set;
	}
	
	public boolean isProcesso() {
		return processo;
	}
	
	public boolean isLiberado() {
		return liberado;
	}
	
	private void kickPlayers(String msg, boolean ignore) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
			if (!ignore && j.hasPermission("walkers.manutencao.entrar")) continue;
			p.kickPlayer(msg);
		}
	}
	
	public void cancelar() {
		new Title("§4"+manutencao.getTipo().getNome(), "§cCancelado", 1, 11*20, 1).broadcast();
		setProcesso(false);
		Bukkit.getScheduler().cancelTask(countdown);
		travar = false;
	}
	
	private void load() {
		aberto = travar = false;
		liberado = !WalkerEngine.get().getServerManager().getServer().isManutencao();
		tempoStop = config.getManutencaoTempoStop();
		tempoManutencao = config.getManutencaoTempoManu();
		//WalkerEngine.get().getMotdManager().registrarMotdPlugin("manutencaoaberto", config.getManutencaoStopMotd());
		//WalkerEngine.get().getMotdManager().registrarMotdPlugin("manutencaoliberado", config.getManutencaoManuMotd());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void aoUsarChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) return;
		if (travar) {
			MensagensAPI.mensagemErro("Sistemas travados por causa da contagem regressiva", e.getPlayer());
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void aoUsarComando(PlayerCommandPreprocessEvent e) {
		if (e.isCancelled()) return;
		if (travar) {
			MensagensAPI.mensagemErro("Sistemas travados por causa da contagem regressiva", e.getPlayer());
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void reiniciar(PlayerDropItemEvent e) {
		if (e.isCancelled()) return;
		if (travar) {
			MensagensAPI.mensagemErro("Sistemas travados por causa da contagem regressiva", e.getPlayer());
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void reiniciar2(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
		if (e.getItem() == null) return;
			if (travar) {
				MensagensAPI.mensagemErro("Sistemas travados por causa da contagem regressiva", e.getPlayer());
				e.setCancelled(true);
			}
	    }
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void reiniciar3(PlayerPickupItemEvent e) {
		if (travar) {
			MensagensAPI.mensagemErro("Sistemas travados por causa da contagem regressiva", e.getPlayer());
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void reiniciar4(BlockBreakEvent e) {
		if (travar) {
			MensagensAPI.mensagemErro("Sistemas travados por causa da contagem regressiva", e.getPlayer());
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void reiniciar6(BlockPlaceEvent e) {
		if (travar) {
			MensagensAPI.mensagemErro("Sistemas travados por causa da contagem regressiva", e.getPlayer());
			e.setCancelled(true);
		}
	}
	
}
