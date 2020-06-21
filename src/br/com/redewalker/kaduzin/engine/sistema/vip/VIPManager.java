package br.com.redewalker.kaduzin.engine.sistema.vip;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.bungee.WalkerBungee;
import br.com.redewalker.kaduzin.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.server.Servers;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class VIPManager {
	
	public VIPManager() {
		WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().checkTable();
		new BukkitRunnable() {
			@Override
			public void run() {
				checkAllVipsAtivos();
			}
		}.runTaskTimer(WalkerEngine.get(), 0, 20*60*15);
		Bukkit.getServer().getPluginManager().registerEvents(new AoClicar(), WalkerEngine.get());
	}
	
	public void setVip(Usuario p, GrupoType vip, int dias, String recebido, Servers server) {
		if (!vip.isVIP() || vip.equals(GrupoType.VIPLobby)) return;
		long tempo = 86400*dias;
		VIP vip1 = WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().createVIP(p.getNickOriginal(), vip.toString(), server.toString(), tempo, false, System.currentTimeMillis(), recebido, dias);
		ativarVIP(vip1);
		WalkerBungee.sendBungeeVip(vip1.getID()+"", vip1.getServer().toString());
	}
	
	public void darItens(Usuario p, GrupoType vip) {
		if (!vip.isVIP() || vip.equals(GrupoType.VIPLobby)) return;
		  ArrayList<String> cmds = WalkerEngine.get().getConfigManager().getConfigPrincipal().getCmds(vip.toString().toLowerCase());
		  for (String cmd : cmds) {
			  if (cmd.contains(";")) {
				  String[] cc = cmd.split(";");
				  Random r = new Random();
				  if (Integer.parseInt(cc[0]) >= r.nextInt(100)) Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cc[1].replace("@jogador", p.getNickOriginal()));
			  }else Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("@jogador", p.getNickOriginal()));
		  }
	}
	
	public boolean containsVIPPermanente(Usuario user, GrupoType vip, Servers server) {
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().containsPermanente(user.getNickOriginal(), vip.toString(), server.toString());
	}

	public void ativarVIP(VIP id) {
		VIP atv = getVIPAtivo(id.getUsuario().getNickOriginal(), id.getServer());
		if (atv instanceof VIP && id.getID() == atv.getID()) return;
		if (atv instanceof VIP) atv.setAtivo(false);
		id.setAtivo(true);
	}
	
	public void desativarVIP(VIP id) {
		id.setAtivo(false);
		VIP vip = getVIPAtivo(id.getUsuario().getNickOriginal(), id.getServer());
		if (!(vip instanceof VIP)) setNextVIP(id.getUsuario(), id.getServer());
	}
	
	public void removerVip(VIP id, String remove) {
		id.setAtivo(false);
		id.setTempoRestante(-1);
		id.setRemovidoPor(remove);
	}
	
	public void checkAllVipsAtivos() {
		ArrayList<VIP> vips = WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getAllVipsAtivos();
		for (VIP vip : vips) {
			long tempo = vip.getTempoRestante();
			if (tempo < 0 || tempo < System.currentTimeMillis()) desativarVIP(vip);
		}
	}
	
	private void setNextVIP(Usuario user, Servers server) {
		ArrayList<VIP> vips = getVIPsParaAtivar(user, server);
		if (!vips.isEmpty()) ativarVIP(vips.get(0));
	}
	
	public boolean isVIP(Usuario user) {
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().isVIP(user.getNickOriginal());
	}
	
	public VIP getVIPAtivo(String usuario, Servers server) {
		VIP vip = WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVipOnlyAtivo(usuario, server.toString());
		if (vip instanceof VIP) desativarVIP(vip);
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVipAtivo(usuario, server.toString());
	}
	
	public VIP getVIP(long id) {
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVip(id);
	}
	
	public ArrayList<VIP> getAllVipsPermanentes() {
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getAllVipsPerma();
	}
	
	public ArrayList<VIP> getAllVipsTemporarios() {
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getAllVipsTempo();
	}
	
	public ArrayList<VIP> getVIPsParaAtivar(Usuario user, Servers server) {
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsParaAtivar(user.getNickOriginal(), server.toString());
	}
	
	public ArrayList<VIP> getVIPs(Usuario user, Servers server) {
		return WalkerEngine.get().getConexaoManager().getConexaoVIPDAO().getVips(user.getNickOriginal(), server.toString());
	}

}
