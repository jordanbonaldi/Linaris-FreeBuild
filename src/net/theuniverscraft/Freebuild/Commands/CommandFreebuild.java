package net.theuniverscraft.Freebuild.Commands;

import net.theuniverscraft.Freebuild.Freebuild;
import net.theuniverscraft.Freebuild.Managers.DbManager;
import net.theuniverscraft.Freebuild.Managers.SaveManager;
import net.theuniverscraft.Freebuild.Managers.Translation;
import net.theuniverscraft.Freebuild.Managers.SaveManager.Zone;
import net.theuniverscraft.Freebuild.Utils.Location2D;
import net.theuniverscraft.Freebuild.Utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFreebuild implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(args.length == 0) {
				player.teleport(Freebuild.instance.fb.getSpawnLocation());
			}
			else if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("auto")) {
					Zone zone = Utils.getRandomZone();
					Location loc = zone.getLocation().to3D(Freebuild.instance.fb).add(SaveManager.ZONE_WIDTH / 2, 0, -1);
					player.teleport(loc);
					player.sendMessage(Translation.getString("ZONE_SEARCH"));
				}
				else if(args[0].equalsIgnoreCase("claim")) {
					Zone zone = SaveManager.getInstance().getZone(new Location2D(player.getLocation()));
					if(zone == null) {
						player.sendMessage(Translation.getString("YOU_ARENT_IN_ZONE"));
					}
					else {
						if(zone.hasOwner()) {
							player.sendMessage(Translation.getString("ZONE_NOT_FREE"));
						}
						else {
							if(SaveManager.getInstance().getZonesByPlayer(player.getName()).length >= DbManager.getInstance().getMaxZone(player.getName())) {
								int max = DbManager.getInstance().getMaxZone(player.getName());
								player.sendMessage(Translation.getString("YOU_HAS_TOO_ZONE").replaceAll("<max>", Integer.toString(max)));
							}
							else {
								zone.setOwner(player.getName());
								int id = SaveManager.getInstance().getZonesByPlayer(player.getName()).length;
								
								player.sendMessage(Translation.getString("ZONE_CLAIM").replaceAll("<id>", Integer.toString(id)));
							}
						}
					}
				}
				else if(args[0].equalsIgnoreCase("zone")) {
					Zone zone = SaveManager.getInstance().getZone(new Location2D(player.getLocation()));
					if(zone == null) {
						player.sendMessage("Couloir");
					}
					else {
						player.sendMessage("Zone : X = " + zone.getLocation().getX() + " ; Z = " + zone.getLocation().getZ());
					}
				}
				else if(args[0].equalsIgnoreCase("home")) {
					int no = 0;
					
					Zone[] zones = SaveManager.getInstance().getZonesByPlayer(player.getName());
					if(zones.length == 0) {
						player.sendMessage(Translation.getString("YOU_HASNT_HOME"));
						return true;
					}
					
					if(args.length >= 2) {
						try {
							no = Integer.parseInt(args[1]);
							no--;
							
							if(no >= zones.length) {
								player.sendMessage(Translation.getString("YOU_HAS_ONE_HOME_NO").replaceAll("<nb>", args[1]));
								return true;
							}
							else if(no < 0) {
								player.sendMessage(ChatColor.RED + "/freebuild home <numero>");
								return true;
							}
						} catch(NumberFormatException e) {
							player.sendMessage(ChatColor.RED + "/freebuild home <numero>");
						}
					}
					Zone zone = zones[no];
					Location loc = zone.getLocation().to3D(Freebuild.instance.fb).add(SaveManager.ZONE_WIDTH / 2, 0, -1);
					player.teleport(loc);
					player.sendMessage(Translation.getString("TP_HOME").replaceAll("<id>", Integer.toString(no + 1)));
				}
				else if(args[0].equalsIgnoreCase("setspawn")) {
					if(!player.isOp()) return false;
					Location l = player.getLocation();
					player.getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
					player.sendMessage("ok !");
				}
				else if(args[0].equalsIgnoreCase("loc")) {
					player.sendMessage("world : " + player.getWorld().getName());
				}
			}
		}
		return true;
	}
}
