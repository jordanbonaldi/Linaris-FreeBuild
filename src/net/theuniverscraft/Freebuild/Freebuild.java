package net.theuniverscraft.Freebuild;

import net.theuniverscraft.Freebuild.Commands.CommandFreebuild;
import net.theuniverscraft.Freebuild.Listeners.BasicListener;
import net.theuniverscraft.Freebuild.Listeners.ZoneProtectListener;
import net.theuniverscraft.Freebuild.Managers.SaveManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Freebuild extends JavaPlugin {
	public static Freebuild instance;
	public World fb;
	public PlotGen plotGen;
	
	@Override
	public void onEnable() {
		instance = this;
		
		SaveManager.getInstance();
		
		plotGen = new PlotGen();
		
		WorldCreator options = new WorldCreator("freebuild");
		fb = Bukkit.createWorld(options);
		
		PluginManager pm = getServer().getPluginManager();		
		pm.registerEvents(new BasicListener(), this);
		pm.registerEvents(new ZoneProtectListener(), this);
		
		this.getCommand("freebuild").setExecutor(new CommandFreebuild());
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldname, String id) {
		return plotGen;
	}
}
