package br.com.redewalker.kaduzin.engine.reflections;

public class ReflectionsAPI {
	
	public static void load() {
		try 
		{
			ItemAPI.load();
			CrashAPI.load();
			TablistAPI.load();
			OfflinePlayerAPI.load();
			SkullAPI.load();
		} 
		catch (Throwable e) {}
	}

}
