package br.com.redewalker.kaduzin.engine.reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.utils.ReflectionUtils;

public class OfflinePlayerAPI {

	private static Object worldType;
	private static Method getServer;
	private static Method getWorldServer;
	private static Method getBukkitEntity;
	private static Constructor<?> gameProfileConstructor;
	private static Constructor<?> entityPlayerConstructor;
	private static Constructor<?> playerInteractManagerConstructor;
	
	@SuppressWarnings("deprecation")
	public static Player getPlayer(String playerName) {
        Player testPlayer = Bukkit.getPlayerExact(playerName);
        if (testPlayer != null) return testPlayer; // Caso ele j� estiver online...
        
        try {
	        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
	        if (!offlinePlayer.hasPlayedBefore()) return null; // Caso o player não existir...
	        
	        Object uuid = offlinePlayer.getUniqueId();
	        Object gameProfile = gameProfileConstructor.newInstance(uuid, playerName);
	        Object minecraftServer = getServer.invoke(null);
	        Object worldServer = getWorldServer.invoke(minecraftServer, worldType);
	        Object playerInteractManager = playerInteractManagerConstructor.newInstance(worldServer);
	        Object entityPlayer = entityPlayerConstructor.newInstance(minecraftServer, worldServer, gameProfile, playerInteractManager);
	 
	        Player player = (Player) getBukkitEntity.invoke(entityPlayer);
	        player.loadData();
	 
	        return player;
        } catch (Throwable e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	public static Collection<? extends Player> getPlayers() {
		Collection<Player> players = new ArrayList<>();
		
		for (Player p : Bukkit.getOnlinePlayers()) 
			players.add(p);
		
		try {
	        Object minecraftServer = getServer.invoke(null);
	        Object worldServer = getWorldServer.invoke(minecraftServer, worldType);
	        Object playerInteractManager = playerInteractManagerConstructor.newInstance(worldServer);
	        
			for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
				// Caso ele j� estiver online ou for nulo...
		        if (offlinePlayer == null || offlinePlayer.isOnline()) continue;
		        
		        Object uuid = offlinePlayer.getUniqueId();
		        Object gameProfile = gameProfileConstructor.newInstance(uuid, offlinePlayer.getName());
		        Object entityPlayer = entityPlayerConstructor.newInstance(minecraftServer, worldServer, gameProfile, playerInteractManager);
		        
		        Player player = (Player) getBukkitEntity.invoke(entityPlayer);
		        player.loadData();
		        
		        players.add(player);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return players;
	}

	static void load() {
		try 
		{
			Class<?> PlayerInteractManagerClass = ReflectionUtils.getNMSClass("PlayerInteractManager");
			Class<?> MinecraftServerClass = ReflectionUtils.getNMSClass("MinecraftServer");
			Class<?> EntityPlayerClass = ReflectionUtils.getNMSClass("EntityPlayer");
			Class<?> WorldServerClass = ReflectionUtils.getNMSClass("WorldServer");
			Class<?> WorldClass = ReflectionUtils.getNMSClass("World");
			Class<?> WorldTypeClass;
			Class<?> gameProfileClass;

			WorldTypeClass = int.class;
			worldType = 0;
			
			gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
			gameProfileConstructor = gameProfileClass.getConstructor(UUID.class, String.class);	
			
			getServer = MinecraftServerClass.getMethod("getServer");
			getWorldServer = MinecraftServerClass.getMethod("getWorldServer", WorldTypeClass);
			getBukkitEntity = EntityPlayerClass.getMethod("getBukkitEntity");
			entityPlayerConstructor = EntityPlayerClass.getConstructor(MinecraftServerClass, WorldServerClass, gameProfileClass, PlayerInteractManagerClass);
			playerInteractManagerConstructor = PlayerInteractManagerClass.getConstructor(WorldClass);
		}
		catch (Throwable e) {}
	}
	
}