package net.theuniverscraft.Freebuild.Listeners;

import net.theuniverscraft.Freebuild.Managers.SaveManager;
import net.theuniverscraft.Freebuild.Utils.Location2D;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

public class ZoneProtectListener implements Listener {
	private SaveManager save;
	
	public ZoneProtectListener() {
		save = SaveManager.getInstance();
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(!save.canBuild(event.getPlayer(), new Location2D(event.getBlock().getLocation())));
		if(event.getPlayer().isOp()) event.setCancelled(false);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(!save.canBuild(event.getPlayer(), new Location2D(event.getBlock().getLocation())));
		if(event.getPlayer().isOp()) event.setCancelled(false);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) return;
		
		event.setCancelled(!save.canBuild(event.getPlayer(), new Location2D(event.getClickedBlock().getLocation())));
		if(event.getPlayer().isOp()) event.setCancelled(false);
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		event.setCancelled(!save.canBuild(event.getPlayer(), new Location2D(event.getBlockClicked().getLocation())));
		if(event.getPlayer().isOp()) event.setCancelled(false);
	}
	
	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		event.setCancelled(!save.canBuild(event.getPlayer(), new Location2D(event.getBlockClicked().getLocation())));
		if(event.getPlayer().isOp()) event.setCancelled(false);
	}
	
	@EventHandler
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		if(event.getRemover() instanceof Player) {
			Player player = (Player) event.getRemover();
			event.setCancelled(!save.canBuild(player, new Location2D(event.getEntity().getLocation())));
			if(player.isOp()) event.setCancelled(false);
		}
		else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHangingBreak(HangingBreakEvent event) {
		if(event.getCause() != RemoveCause.ENTITY) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		event.setCancelled(!save.isInZone(new Location2D(event.getBlock().getLocation())));
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		event.setCancelled(!save.canBuild(event.getPlayer(), new Location2D(event.getRightClicked().getLocation())));
		if(event.getPlayer().isOp()) event.setCancelled(false);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			event.setCancelled(!save.canBuild(player, new Location2D(event.getEntity().getLocation())));
			if(player.isOp()) event.setCancelled(false);
		}
		else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		if(event.getAttacker() instanceof Player) {
			Player player = (Player) event.getAttacker();
			event.setCancelled(!save.canBuild(player, new Location2D(event.getVehicle().getLocation())));
			if(player.isOp()) event.setCancelled(false);
		}
		else {
			event.setCancelled(true);
		}
	}
}
