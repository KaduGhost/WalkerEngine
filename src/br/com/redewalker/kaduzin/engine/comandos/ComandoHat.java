package br.com.redewalker.kaduzin.engine.comandos;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class ComandoHat extends BukkitCommand {

	public ComandoHat(String name) {
		super(name);
		setAliases(Arrays.asList("chapeu"));
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WalkerEngine.get().getUsuarioManager().getUsuario(sender.getName());
		Player p = j.getPlayer();
		if (args.length == 0) {
			if (!j.hasPermission("walker.hat")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
			if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
				MensagensAPI.mensagemErro("Você precisa de um item na mão", sender);
				return false;
			}
			ItemStack cab = p.getInventory().getHelmet();
			ItemStack itemStack = p.getItemInHand();
			p.setItemInHand(cab);
			p.getInventory().setHelmet(itemStack);
			p.sendMessage("§aAproveite o seu novo chapéu!");
			return true;
		}
		MensagensAPI.formatoIncorreto("chapeu", sender);
		return false;
	}
	
}
