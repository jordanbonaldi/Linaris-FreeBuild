package net.theuniverscraft.Freebuild.Managers;

import java.util.HashMap;

import org.bukkit.ChatColor;

public class Translation {
	private HashMap<String, String> lang = new HashMap<String, String>();
	
	private static Translation instance = null;
	public static String getString(String key) {
		if(instance == null) instance = new Translation();
		return instance.getStringLocale(key);
	}
	
	private Translation() {
		lang.put("ZONE_SEARCH", "&2Vous avez été téléporté à une zone libre : faite /freebuild claim pour la gardez");
		lang.put("ZONE_CLAIM", "&2La zone (<id>) vous appartient !");
		lang.put("YOU_ARENT_IN_ZONE", "&4Vous n'êtes pas dans une zone");
		lang.put("ZONE_NOT_FREE", "&4Cette zone n'est pas libre");
		
		lang.put("YOU_HASNT_HOME", "&4Vous n'avez pas de home");
		lang.put("YOU_HAS_ONE_HOME", "&4Vous n'avez qu'un home");
		lang.put("YOU_HAS_TOO_ZONE", "&4Vous avez trop de zones (max : <max>)");
		lang.put("ZONE_LOCK", "&4Cette zone est lock, vous devez être vip pour l'utiliser à nouveau");
		
		lang.put("TP_HOME", "&2Vous avez été téléporté à votre home <id>");
		
		lang.put("JOIN_MESSAGE", "&6<player> &eà rejoint le jeu !");
		lang.put("QUIT_MESSAGE", "&6<player> &eà quitté le jeu !");
	}
	
	public String getStringLocale(String key) {
		return ChatColor.translateAlternateColorCodes('&', lang.get(key));
	}
}
