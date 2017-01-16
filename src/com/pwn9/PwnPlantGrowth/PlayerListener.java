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
	private final PwnPlantGrowth plugin;
	
	public PlayerListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	    this.plugin = plugin;
	}	
	
	// Listen for plant growth
	@EventHandler(ignoreCancelled = true)
	public void bonemealUse(PlayerInteractEvent e) 
	{
		
		World world = e.getPlayer().getLocation().getWorld();
		
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;
		
	    ItemStack bonemeal = new ItemStack(Material.INK_SACK, 1, (short) 15);
	    	    
	    Block b = e.getClickedBlock();

	    if (e.getItem() == bonemeal) {
	    	
	    	PwnPlantGrowth.logToFile("Player used bonemeal on: " + b.getType().name());
	    	
	    }
	    
	}
	
}