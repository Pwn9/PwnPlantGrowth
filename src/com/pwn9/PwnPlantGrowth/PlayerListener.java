package com.pwn9.PwnPlantGrowth;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener
{

	public PlayerListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	}	
	
	//TODO: BlockFertilizeEvent for bonemeal 
	
	// https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/player/PlayerInteractEvent.html
	// This will check for block clicks with growable items and report if they can grow well here or not - feature...
	@EventHandler(ignoreCancelled = true)
	public void checkBlockClick(PlayerInteractEvent e) 
	{
		
		World world = e.getPlayer().getLocation().getWorld();
		
		// if the plugin is enabled and the setting enabled, otherwise bail
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;
		if (!PwnPlantGrowth.reportGrowth) return;
		
		// bail if no item in hand
		if (!e.hasItem()) return;
		
		// bail if the item is a block
		//if (e.isBlockInHand()) return;
	
		// get action
		Action action = e.getAction();
		
		if (action == Action.LEFT_CLICK_BLOCK) 
		{
			
			Player p = e.getPlayer();
			
			Block block = e.getClickedBlock();
			
			if (block.getType() == Material.FARMLAND || block.getType() == Material.DIRT || block.getType() == Material.GRASS) 
			{
				// is this the material in their hand?
				Material m = e.getMaterial();
				
				if(PwnPlantGrowth.plantTypes.contains(m.toString())) {
					int a = PwnPlantGrowth.instance.getConfig().getInt(m.toString() + ".Growth");
					p.sendMessage("Growth rate for " + m.toString() + ": " + a);
					
				}
				else if(PwnPlantGrowth.seedTypes.contains(m.toString())) {
					
					String msg = "";
					
					if (m == Material.BEETROOT_SEEDS) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("BEETROOTS.Growth");
						msg = "BEETROOT: " + a;
					}
					else if (m == Material.CARROT) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("CARROTS.Growth");
						msg = "CARROTS: " + a;
					}						
					else if (m == Material.COCOA_BEANS) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("COCOA.Growth");
						msg = "COCOA: " + a;
					}	
					else if (m == Material.MELON_SEEDS) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("MELON.Growth");
						msg = "MELON: " + a;
					}
					else if (m == Material.POTATO) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("POTATOES.Growth");	
						msg = "POTATOES: " + a;
					}					
					else if (m == Material.PUMPKIN_SEEDS) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("PUMPKIN.Growth");	
						msg = "PUMPKIN: " + a;
					}
					else if (m == Material.SWEET_BERRIES) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("SWEET_BERRY_BUSH.Growth");		
						msg = "SWEET_BERRY_BUSH: " + a;
					}
					else if (m == Material.WHEAT_SEEDS) {
						int a = PwnPlantGrowth.instance.getConfig().getInt("WHEAT.Growth");		
						msg = "WHEAT: " + a;
					}
					
					p.sendMessage("Growth rate for " + msg);
	
				}
			}
		}
		

	}
	
}