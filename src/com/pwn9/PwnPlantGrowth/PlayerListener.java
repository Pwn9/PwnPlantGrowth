package com.pwn9.PwnPlantGrowth;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener
{

	public PlayerListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	}	
	
	//TODO: replace this with BlockFertilizeEvent 
	
	// Listen for bonemeal usage, this doesn't seem to do anything at the moment.
	@EventHandler(ignoreCancelled = true)
	public void bonemealUse(PlayerInteractEvent e) 
	{
		
		World world = e.getPlayer().getLocation().getWorld();
		
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;
		
	    ItemStack bonemeal = new ItemStack(Material.BONE_MEAL);
	    	    
	    Block b = e.getClickedBlock();

	    if (e.getItem() == bonemeal) {
	    	
	    	PwnPlantGrowth.logToFile("Player used bonemeal on: " + b.getType().name());
	    	
	    }
	    
	}
	
}