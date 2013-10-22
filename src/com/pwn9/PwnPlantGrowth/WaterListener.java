package com.pwn9.PwnPlantGrowth;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.material.Dispenser;
import org.bukkit.material.MaterialData;

public class WaterListener implements Listener 
{

	private final PwnPlantGrowth plugin;
	
	public WaterListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	    this.plugin = plugin;
	}
	
	// water buckets
	@EventHandler(ignoreCancelled = true)
	public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event)
	{
		World world = event.getPlayer().getWorld();
		if (PwnPlantGrowth.isEnabledIn(world.getName())) 
		{
			if(PwnPlantGrowth.blockWaterBucket)
	    	{				
	    		Player player = event.getPlayer();
	    		if(player.getItemInHand().getType() == Material.WATER_BUCKET) 
	    		{   			
	    			Block block = event.getBlockClicked().getRelative(event.getBlockFace());
	    			EvaporateWaterTask task = new EvaporateWaterTask(block);
	    			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 30L);
	    	    	if (PwnPlantGrowth.logEnabled) 
	    	    	{	
	    	    		PwnPlantGrowth.logToFile("Blocked water source from bucket");
	    	    	}
	    		}
	    	}
		}
	}
	
	//when a dispenser dispenses...
	@EventHandler(ignoreCancelled = true)
	public void onBlockDispense(BlockDispenseEvent event)
	{
		World world = event.getBlock().getWorld();
		if (PwnPlantGrowth.isEnabledIn(world.getName())) 
		{
			if(PwnPlantGrowth.blockWaterDispenser)
	    	{				
				//only care about water
				if(event.getItem().getType() == Material.WATER_BUCKET)
				{
					Block dispenser = event.getBlock();
					// Get location of dispenser 
					Location loc = dispenser.getLocation();
					// Get direction dispenser is facing 
					MaterialData mat = dispenser.getState().getData(); 
					Dispenser disp_mat = (Dispenser) mat; 
					BlockFace face = disp_mat.getFacing(); 			
					Block block;
					if(face.toString() == "WEST") 
					{
						block = loc.add(-1, 0, 0).getBlock();			
					}
					else if(face.toString() == "EAST") 
					{
						block = loc.add(1, 0, 0).getBlock();
					}
					else if(face.toString() == "NORTH") 
					{
						block = loc.add(0, 0, -1).getBlock();
					}
					else
					{
						block = loc.add(0, 0, 1).getBlock();
					}			
					EvaporateWaterTask task = new EvaporateWaterTask(block);
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 45L);
	    	    	if (PwnPlantGrowth.logEnabled) 
	    	    	{	
	    	    		PwnPlantGrowth.logToFile("Blocked water source from dispenser");
	    	    	}
				}
	    	}
		}
	}

}
	
	