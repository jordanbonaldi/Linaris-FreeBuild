package net.theuniverscraft.Freebuild;

import java.util.Random;

import net.theuniverscraft.Freebuild.Managers.SaveManager;
import net.theuniverscraft.Freebuild.Utils.Location2D;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

public class PlotGen extends ChunkGenerator {
	private byte[] grass_dirt = new byte[4096];
	private byte[] full_dirt = new byte[4096];
	private byte[] dirt_bedrock = new byte[4096];
	
	@SuppressWarnings("deprecation")
	public PlotGen() {
		for(int i = 0; i < full_dirt.length; i++) {
			full_dirt[i] = (byte) Material.DIRT.getId();
		}
		
		grass_dirt = full_dirt.clone();
		for(int i = 0; i < 256; i++) {
			grass_dirt[grass_dirt.length - i - 1] = (byte) Material.GRASS.getId();
		}
		
		dirt_bedrock = full_dirt.clone();
		for(int i = 0; i < 256; i++) {
			dirt_bedrock[i] = (byte) Material.BEDROCK.getId();
		}
	}
	
	@SuppressWarnings("deprecation")
	public byte[][] generateBlockSections (World world, Random random, int cx, int cz, BiomeGrid biomes) {		
		byte[][] data = new byte[world.getMaxHeight() / 16][4096];
				
		data[0] = dirt_bedrock.clone();
		data[1] = full_dirt.clone();
		data[2] = full_dirt.clone();
		data[3] = grass_dirt.clone();
		
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				if(SaveManager.getInstance().isNearZone(new Location2D(cx*16 + x, cz*16 + z))) {
					setBlock(data, x, 63, z, (byte) Material.DOUBLE_STEP.getId());
				}
				else if(!SaveManager.getInstance().isInZone(new Location2D(cx*16 + x, cz*16 + z))) {
					setBlock(data, x, 63, z, (byte) Material.STONE.getId());
				}
				if(biomes.getBiome(x, z) != Biome.PLAINS) biomes.setBiome(x, z, Biome.PLAINS);
			}
		}
		return data;
	}
	
	private void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
		if (result[y >> 4] == null) {
			result[y >> 4] = new byte[4096];
		}
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
	}
}
