package net.theuniverscraft.Freebuild.Listeners;

import net.theuniverscraft.Freebuild.Freebuild;
import net.theuniverscraft.Freebuild.Managers.Translation;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BasicListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().setGameMode(GameMode.CREATIVE);
		event.getPlayer().teleport(Freebuild.instance.fb.getSpawnLocation());
		event.setJoinMessage(Translation.getString("JOIN_MESSAGE").replaceAll("<player>", event.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(Translation.getString("QUIT_MESSAGE").replaceAll("<player>", event.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(Translation.getString("QUIT_MESSAGE").replaceAll("<player>", event.getPlayer().getName()));
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}
}
