package br.com.redewalker.kaduzin.engine.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
	
	private ItemStack stack;
	
	public ItemBuilder(Material id) {
		this.stack = new ItemStack(id);
	}
	
	@SuppressWarnings("deprecation")
	public ItemBuilder(Material id, int data) {
		this.stack = new ItemStack(id, 1, (short)1, (byte)data);
	}
	
	@SuppressWarnings("deprecation")
	public ItemBuilder(int item) {
		this.stack = new ItemStack(item);
	}
	
	@SuppressWarnings("deprecation")
	public ItemBuilder(int item, int data) {
		this.stack = new ItemStack(item, 1, (short)1, (byte)data);
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public ItemBuilder setOwner(String owner){
		SkullMeta a = (SkullMeta) stack.getItemMeta();
		a.setOwner(owner);
		stack.setItemMeta(a);
		return this;
	}
	
	
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
	
	public ItemBuilder setQuantidade(int quant) {
		stack.setAmount(quant);
		return this;
	}
	
	public ItemBuilder setName(String nome) {
		ItemMeta a = stack.getItemMeta();
		a.setDisplayName(nome.replace("&", "§"));
		stack.setItemMeta(a);
		return this;
	}
	
	public ItemBuilder setLore(List<String> nome) {
		ItemMeta a = stack.getItemMeta();
		a.setLore(nome);
		stack.setItemMeta(a);
		return this;
	}
	
	public ItemBuilder addEnchant(Enchantment enchant, int nivel) {
		ItemMeta a = stack.getItemMeta();
		a.addEnchant(enchant, nivel, true);
		stack.setItemMeta(a);
		return this;
	}
	
	public ItemBuilder addEnchantGlow(Boolean boole) {
		if (!boole) return this;
		ItemMeta a = stack.getItemMeta();
		a.addEnchant(Enchantment.DURABILITY, 1, true);
		a.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		stack.setItemMeta(a);
		return this;
	}
	
	public ItemStack fazer() {
		return stack;
	}

}
