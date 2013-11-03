package com.pwn9.PwnPlantGrowth;

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
	    			player.getItemInHand().setType(Material.BUCKET);
	    			Block block = event.getBlockClicked().getRelative(event.getBlockFace());
	    			
	    			// If the block is already water then don't do anything, otherwise set it water then set it air for water effect.
	    			if (!isWater(block)) 
	    			{
		    			block.setType(Material.WATER);
		    			EvaporateWaterTask task = new EvaporateWaterTask(block);
		    			//plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 15L);
		    			plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, task, 15L);
	    			}	 
	    			
		    		event.setCancelled(true);
		    		
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
					// Get direction dispenser is facing 
					MaterialData mat = dispenser.getState().getData(); 
					Dispenser disp_mat = (Dispenser) mat; 
					BlockFace face = disp_mat.getFacing(); 			
					Block block = dispenser.getRelative(face);			
					
	    			EvaporateWaterTask task = new EvaporateWaterTask(block);
	    			//plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 30L);
	    			plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, task, 15L);
	    			
	    	    	if (PwnPlantGrowth.logEnabled) 
	    	    	{	
	    	    		PwnPlantGrowth.logToFile("Blocked water source from dispenser");
	    	    	}
				}
	    	}
		}
	}
	
	public boolean isWater(Block block)
	{	
		if(block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) 
		{
			return true;
		}
		return false;	
	}		

}
	
	