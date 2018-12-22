package net.theuniverscraft.Freebuild.Managers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.theuniverscraft.Freebuild.Freebuild;
import net.theuniverscraft.Freebuild.Utils.Location2D;
import net.theuniverscraft.Freebuild.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SaveManager {
	private static SaveManager instance = null;
	
	private File save_file;
	private FileConfiguration m_save;
	private List<Zone> m_zones = new LinkedList<Zone>();
	
	public static final int ZONE_WIDTH = 64;
	public static final int CORRIDOR_WIDTH = 12;
	
	public static SaveManager getInstance() {
		if(instance == null) instance = new SaveManager();
		return instance;
	}
	
	private SaveManager() {
		new File("plugins/" + Freebuild.instance.getName()).mkdirs();
		
		save_file = new File("plugins/" + Freebuild.instance.getName() + "/plots.yml");
		m_save = YamlConfiguration.loadConfiguration(save_file);
		
		Map<String, Object> values = m_save.getValues(false);
		for(String key : values.keySet()) {
			if(values.get(key) instanceof MemorySection) {
				MemorySection mem = (MemorySection) values.get(key);
				String owner = mem.getString("owner");
				
				long time = mem.getLong("time");
				
				int x = mem.getInt("x");
				int z = mem.getInt("z");
				
				m_zones.add(new Zone(new Location2D(x, z), owner, time, mem));
			}
		}
	}
	
	public Zone getZone(Location2D location) {
		for(Zone zone : m_zones) {
			if(zone.isInto(location)) return zone;
		}
		// On créé la zone si elle n'est pas dans un couloir
		// 1. Détermination si on est ou pas dans un couloir
		int xDis = location.getX();
		int zDis = location.getZ();
		
		while(xDis >= ZONE_WIDTH + CORRIDOR_WIDTH) xDis -= ZONE_WIDTH + CORRIDOR_WIDTH;
		while(xDis < - ZONE_WIDTH - CORRIDOR_WIDTH) xDis += ZONE_WIDTH + CORRIDOR_WIDTH;
		
		while(zDis >= ZONE_WIDTH + CORRIDOR_WIDTH) zDis -= ZONE_WIDTH + CORRIDOR_WIDTH;
		while(zDis < - ZONE_WIDTH - CORRIDOR_WIDTH) zDis += ZONE_WIDTH + CORRIDOR_WIDTH;
		
		// ici xDis et yDis sont compris entre -p et p
		if(xDis > ZONE_WIDTH || (xDis < 0 && xDis >= -CORRIDOR_WIDTH)) return null; // couloir
		else if(zDis > ZONE_WIDTH || (zDis < 0 && zDis >= -CORRIDOR_WIDTH)) return null; // couloir
		
		// 2. Arrivé ici on est bien dans une zone, il faut détérminer ces coordonnées
		
		
		int x;
		int z;
		
		if(xDis >= 0) x = location.getX() - xDis;
		else x = location.getX() - (ZONE_WIDTH + CORRIDOR_WIDTH + xDis);
		
		if(zDis >= 0) z = location.getZ() - zDis;
		else z = location.getZ() - (ZONE_WIDTH + CORRIDOR_WIDTH + zDis);
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Nouvelle zone en X = " + x + " ; Z = " + z);
		
		// 3. On créé la zone et on la retourn
		return createZone(new Location2D(x, z), null);
	}
	
	public Zone[] getZonesByPlayer(String player) {
		List<Zone> zones = new LinkedList<Zone>();
		for(Zone zone : m_zones) {
			if(player.equals(zone.getOwner())) zones.add(zone);
		}
		
		return Utils.zoneDesc(zones.toArray(new Zone[0]));
		// return zones.toArray(new Zone[0]);
	}
	
	public List<Zone> getFreeZones() {
		List<Zone> zones = new LinkedList<Zone>();
		
		for(Zone zone : m_zones) {
			if(!zone.hasOwner()) zones.add(zone);
		}
		
		return zones;
	}
	
	public List<Zone> getZones() {
		return m_zones;
	}
	
	
	public boolean isNearZone(Location2D loc) {
		if(getZone(loc) != null) return false;
		
		else if(getZone(new Location2D(loc.getX() + 1, loc.getZ())) != null) return true;
		else if(getZone(new Location2D(loc.getX() - 1, loc.getZ())) != null) return true;
		
		else if(getZone(new Location2D(loc.getX(), loc.getZ() + 1)) != null) return true;
		else if(getZone(new Location2D(loc.getX(), loc.getZ() - 1)) != null) return true;
		
		else if(getZone(new Location2D(loc.getX() + 1, loc.getZ() + 1)) != null) return true;
		else if(getZone(new Location2D(loc.getX() - 1, loc.getZ() - 1)) != null) return true;
		
		else if(getZone(new Location2D(loc.getX() + 1, loc.getZ() - 1)) != null) return true;
		else if(getZone(new Location2D(loc.getX() - 1, loc.getZ() + 1)) != null) return true;
		
		return false;
	}
	
	public boolean isInZone(Location2D loc) {
		return getZone(loc) != null;
	}
	
	public boolean canBuild(Player player, Location2D loc) {
		Zone zone = getZone(loc);
		return zone != null && zone.canBuild(player);
	}
	
	private Zone createZone(Location2D pos, String owner) {
		long time = System.currentTimeMillis();
		
		MemorySection mem = (MemorySection) m_save.createSection(UUID.randomUUID().toString());
		mem.set("owner", owner);
		mem.set("time", time);
		mem.set("x", pos.getX());
		mem.set("z", pos.getZ());
		save();
		
		Zone zone = new Zone(pos, owner, time, mem);
		m_zones.add(zone);
		return zone;
	}
	
	private void save() {
		try {
			m_save.save(save_file);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public class Zone {
		private Location2D m_pos;
		private String m_owner;
		private long m_time;
		private MemorySection m_mem;
		
		private Zone(Location2D pos, String owner, long time, MemorySection mem) {
			m_pos = pos;
			m_owner = owner;
			m_time = time;
			m_mem = mem;
		}
		
		public boolean isInto(Location2D location) { // Conflit avec pos négatif
			return location.getX() >= m_pos.getX() && location.getX() <= m_pos.getX() + ZONE_WIDTH &&
					location.getZ() >= m_pos.getZ() && location.getZ() <= m_pos.getZ() + ZONE_WIDTH;
		}
		
		public void setOwner(String owner) {
			m_owner = owner;
			m_mem.set("owner", owner);
			
			m_time = System.currentTimeMillis();
			m_mem.set("time", m_time);
			save();
		}
		public String getOwner() { return m_owner; }
		public boolean hasOwner() { return m_owner != null; }
		
		public boolean canBuild(Player player) {
			boolean isOwner = player.getName().equalsIgnoreCase(m_owner);
			if(!isOwner) return false;
			
			int no_zone = 0;
			Zone[] player_zones = SaveManager.getInstance().getZonesByPlayer(player.getName());
			for(int i = 0; i < player_zones.length; i++) {
				if(player_zones[i].getTime() == this.getTime()) {
					no_zone = i;
					break;
				}
			}
			
			if(no_zone >= DbManager.getInstance().getMaxZone(player.getName())) {
				player.sendMessage(Translation.getString("ZONE_LOCK"));
				return false;
			}
			
			return true;
		}
		
		public Location2D getLocation() { return m_pos; }
		public long getTime() { return m_time; }
	}
}
