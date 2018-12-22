package net.theuniverscraft.Freebuild.Utils;

import java.util.List;
import java.util.Random;

import net.theuniverscraft.Freebuild.Managers.SaveManager;
import net.theuniverscraft.Freebuild.Managers.SaveManager.Zone;

public class Utils {
	private Utils() {}
	
	public static Zone getRandomZone() {
		List<Zone> zones = SaveManager.getInstance().getFreeZones();
		
		Random rand = new Random();
		
		Zone zone = null;
		if(zones.size() > 0) {
			zone = zones.get(rand.nextInt(zones.size()));
		}
		else {
			final int p = SaveManager.ZONE_WIDTH + SaveManager.CORRIDOR_WIDTH;
			int xPas = rand.nextInt(2) == 0 ? -1 : 1;
			int zPas = rand.nextInt(2) == 0 ? -1 : 1;
			
			
			int z = 0;
			for(int x = 0; zone == null || zone.hasOwner(); x += xPas) {
				for(int k = 0; k < 2; k += zPas) {
					z += k;
					zone = SaveManager.getInstance().getZone(new Location2D(x*p + 1, z*p + 1));
				}
			}
		}
		
		return zone;
	}
	
	public static Zone[] zoneDesc(Zone[] zones) {
		int i = 0;
		int j = 0;
		
		while(j < zones.length) {
			if(zones[i].getTime() > zones[j].getTime()) {
				// Permutte
				Zone tmpi = zones[i];
				zones[i] = zones[j];
				zones[j] = tmpi;
				
				i = j;
			}
			else {
				i += 1;
				if(i >= zones.length) {
					j += 1;
					i = j;
				}
			}
		}
		return zones;
	}
}
