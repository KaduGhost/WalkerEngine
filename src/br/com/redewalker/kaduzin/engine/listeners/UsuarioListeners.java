package br.com.redewalker.kaduzin.engine.listeners;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.configuracao.Config;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class UsuarioListeners implements Listener {
	
	Config config = WalkerEngine.get().getConfigManager().getConfigPrincipal();
	
	@EventHandler
	void usuarioEntrou(PlayerLoginEvent e) {
		WalkerEngine.get().getUsuarioManager().createUsuario(e.getPlayer().getName());
		Usuario user = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		user.atualizarGrupo();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void aoMorrer2(PlayerDeathEvent e) {
		e.setDeathMessage(null);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void aoEntrar(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	void aoSair(PlayerQuitEvent e) {
		e.setQuitMessage("");
		Player p = e.getPlayer();
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
	}
	
	private HashSet<Player> protegidos = new HashSet<>();

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void aoTeleportar(PlayerTeleportEvent e) {
		if (e.getCause() == TeleportCause.COMMAND || e.getCause() == TeleportCause.PLUGIN) {
			protegidos.add(e.getPlayer());
			new BukkitRunnable() {
				@Override
				public void run() {
					protegidos.remove(e.getPlayer());
				}
			}.runTaskLater(WalkerEngine.get(), 20L * 2);
		}
	}
	
	@EventHandler
	public void aoMorrer(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.manterxp")) {
			e.setKeepLevel(true);
			e.setDroppedExp(0);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void aoTomarDano(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (protegidos.contains(e.getEntity())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void aoEntrarLotado(PlayerLoginEvent e) {
		int playersOnline = Bukkit.getOnlinePlayers().size();
		int limite = config.getLimiteJogadores();
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (playersOnline >= limite) {
			if (!(j instanceof Usuario) || !j.hasPermission("walker.lotado.entrar")) e.setKickMessage("&cDesculpe mas o servidor esta cheio!\\n&cApenas VIPs podem entrar com servidor lotado");
			else if (config.isExpulsarSemVip()) {
				Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[playersOnline]);
				int cont = 0;
				while(cont < 4) {
					int random = new Random().nextInt(playersOnline);
					Player p = players[random];
					Usuario j2 = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
					if (j2 == null) continue;
					if (j2.hasPermission("walker.lotado.entrar")) {
						cont++;
						continue;
					} else {
						kickPlayer(p);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null)
			 e.setCancelled(true);
	}
	  
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null)
			e.setCancelled(true);
	}
	  
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getDamager().getName());
			if (j == null)
				e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player) {
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getTarget().getName());
			if (j == null) {
				e.setTarget(null);
				e.setCancelled(true);
			}
		}
	}
	  
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player) {
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getEntity().getName());
			if (j == null)
				e.setCancelled(true);
		}
	}
	  
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void entityRegainHealthEvent(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getEntity().getName());
			if (j == null) {
				e.setAmount(0.0D);
				e.setCancelled(true);
			} 
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoExecutarComando(PlayerCommandPreprocessEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) 
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoMover(PlayerMoveEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) {
			Location volta = e.getFrom();
			e.getPlayer().teleport(volta);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoDropar(PlayerDropItemEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) 
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoInteragirEn(PlayerInteractEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null)
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoInteragir(PlayerInteractEntityEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) 
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void semConsumo(PlayerItemConsumeEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) 
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoGastar(PlayerItemDamageEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) 
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoPegar(PlayerPickupItemEvent e) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) 
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoFalar(AsyncPlayerChatEvent e ) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(e.getPlayer().getName());
		if (j == null) 
			e.setCancelled(true);
	}
	
	private void kickPlayer(Player p) {
		MensagensAPI.mensagemSucesso(String.format("&e &e\n&e&lAVISO: &eVocê sera kickado do servidor em %s segundos para dar lugar a um vip.\n&e &e", config.getTempoKickNaoVip()), p);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (p != null)
					p.kickPlayer("&cVocê foi kickado para dar lugar a um vip.");	
			}
		}.runTaskLater(WalkerEngine.get(), 20L * config.getTempoKickNaoVip());
	}

}
