package br.com.redewalker.kaduzin.engine.sistema.punicoes.inventarios;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.Punicao;
import br.com.redewalker.kaduzin.engine.sistema.punicoes.PunicoesType;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils.WalkersItens;

public class InventarioDespunir {
	
	public InventarioDespunir(Player p, String j2) {
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.punir")) return;
		if (!WalkerEngine.get().getPunicoesManager().isJogadorPunido(j2)) {
			MensagensAPI.mensagemErro("Este jogador não está punido", p);
			return;
		}
		Punicao ban = WalkerEngine.get().getPunicoesManager().getPunicaoAtiva(j2, PunicoesType.BAN), mute = WalkerEngine.get().getPunicoesManager().getPunicaoAtiva(j2, PunicoesType.MUTE);
		if (!j.hasPermission("walker.punir.admin")) {
			if (ban == null || !ban.getAutor().equalsIgnoreCase(p.getName())) ban = null;
			if (mute == null || !mute.getAutor().equalsIgnoreCase(p.getName())) mute = null;
		}
		Inventory inv = Bukkit.createInventory(p, 9*4, "§c§8Despunir - Tipos§e");
		if (ban != null) inv.setItem(21, WalkersItens.getItemPunicao("despunirban", null, null));
		if (mute != null) inv.setItem(23, WalkersItens.getItemPunicao("despunirmute", null, null));
		inv.setItem(4, WalkersItens.getItemPunicao("cabecadespunir", j2, null));
		p.openInventory(inv);
	}

}
