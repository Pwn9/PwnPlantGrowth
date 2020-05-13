package com.pwn9.PwnPlantGrowth;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		if (e.isBlockInHand()) return;
	
		if (e.getHand() != null) {
			
			Player p = e.getPlayer();
			
			//Block b = e.getClickedBlock();
			
			// is this the material in their hand?
			Material m = e.getMaterial();
			
			if(PwnPlantGrowth.seedTypes.contains(m.toString()));
			
			p.sendMessage("You just click with: " + m.toString());
			
		}
		
	    
	    
	    


	    
	}
	
}