package net.theuniverscraft.Freebuild.Utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Location2D {
	private int m_x;
	private int m_z;
	
	public Location2D(int x, int z) {
		m_x = x;
		m_z = z;
	}
	public Location2D(Location location3d) {
		m_x = location3d.getBlockX();
		m_z = location3d.getBlockZ();
	}
	
	public boolean is(Location2D location) {
		return location.getX() == m_x && location.getZ() == m_z;
	}
	public boolean is(int x, int z) {
		return x == m_x && z == m_z;
	}
	
	public void setX(int x) { m_x = x; }
	public void setZ(int z) { m_z = z; }
	
	public int getX() { return m_x; }
	public int getZ() { return m_z; }
	
	public Location to3D(World world) {
		return new Location(world, m_x, 66, m_z);
	}
	
	public String toString() {
		return m_x+","+m_z;
	}
}
