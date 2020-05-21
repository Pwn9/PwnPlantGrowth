package com.pwn9.PwnPlantGrowth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener 
{

	private final PwnPlantGrowth plugin;
	
	public BlockSpreadListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	    this.plugin = plugin;
	}

	static Calculate getCalcs(List<List<String>> specialBlocks, String thisBlock, String curBiome, Boolean isDark)
	{
		return new Calculate(specialBlocks, thisBlock, curBiome, isDark);
	}

	// retrieve list of special blocks
	public List<List<String>> specialBlockList(BlockSpreadEvent e)
	{
		List<String> fBlocksFound = new ArrayList<String>();
		List<String> wkBlocksFound = new ArrayList<String>();
		List<String> uvBlocksFound = new ArrayList<String>();;

		List<List<String>> result = new ArrayList<List<String>>();
		
		// Check for fertilizer blocks
		if (PwnPlantGrowth.fenabled) 
		{
			for (int fx = -(PwnPlantGrowth.fradius); fx <= PwnPlantGrowth.fradius; fx++) 
			{
	            for (int fy = -(PwnPlantGrowth.fradius); fy <= PwnPlantGrowth.fradius; fy++) 
	            {
	               for (int fz = -(PwnPlantGrowth.fradius); fz <= PwnPlantGrowth.fradius; fz++) 
	               {
	            	   fBlocksFound.add(String.valueOf(e.getBlock().getRelative(fx, fy, fz).getType()));
	               }
	            }
	        }
		}		
		
		// Check for weed killer blocks
		if (PwnPlantGrowth.wkenabled)
		{
			for (int wx = -(PwnPlantGrowth.wkradius); wx <= PwnPlantGrowth.wkradius; wx++) 
			{
	            for (int wy = -(PwnPlantGrowth.wkradius); wy <= PwnPlantGrowth.wkradius; wy++) 
	            {
	               for (int wz = -(PwnPlantGrowth.wkradius); wz <= PwnPlantGrowth.wkradius; wz++) 
	               {
	            	   wkBlocksFound.add(String.valueOf(e.getBlock().getRelative(wx, wy, wz).getType()));
	               }
	            }
	        }
		}
		
		// Check for uv blocks
		if (PwnPlantGrowth.uvenabled)
		{
			for (int ux = -(PwnPlantGrowth.uvradius); ux <= PwnPlantGrowth.uvradius; ux++) 
			{
	            for (int uy = -(PwnPlantGrowth.uvradius); uy <= PwnPlantGrowth.uvradius; uy++) 
	            {
	               for (int uz = -(PwnPlantGrowth.uvradius); uz <= PwnPlantGrowth.uvradius; uz++) 
	               {
	            	   uvBlocksFound.add(String.valueOf(e.getBlock().getRelative(ux, uy, uz).getType()));
	               }
	            }
	        }
		}		
		
		result.add(fBlocksFound);
		result.add(wkBlocksFound);
		result.add(uvBlocksFound);

		return result;
	}	
	
	// Listen for plant growth from block spread, this is like chorus plant, and then do stuff
	@EventHandler(ignoreCancelled = false)
	public void blockSpread(BlockSpreadEvent e) 
	{
		
		// Enabled in world?
		World world = e.getBlock().getWorld();
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;

		// Get source block type and make a string for comparison later
		String sourceBlock = String.valueOf(e.getSource().getType());
		
		// we only care about these for now
		if (sourceBlock != "CHORUS_FLOWER" && sourceBlock != "KELP" && sourceBlock != "BAMBOO" && sourceBlock != "BAMBOO_SAPLING") 
		{
			return;
		}
		
		// Get current biome and make a string for comparison later
		String curBiome = PwnPlantGrowth.getBiome(e);
		
		if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logPlantEnabled) && (PwnPlantGrowth.logVerbose))
		{
			PwnPlantGrowth.logToFile("Block Event for: " + sourceBlock + " - In biome: " + curBiome, "BlockSpread");
		}	
		
		// Is anything set for this block in the config, or is it AIR? If not, abort.
		if (!(plugin.getConfig().isSet(sourceBlock))) {
			PwnPlantGrowth.logToFile("No plant configuration set in config for: " + sourceBlock);
			return;
		}
		
		// Get coords of the event for logging
		String coords = String.valueOf(e.getBlock().getLocation());
				
		// Setup boolean to see if event is in defined natural light or not
		Boolean isDark = false;
		
		// Get the current natural light level
		int lightLevel = e.getBlock().getLightFromSky();
		
		// Do we want to check on actual lighting?  Maybe in a future release
		//int actualLight = e.getBlock().getLightFromBlocks();
		
		// If the light level is lower than configured threshold and the plant is NOT exempt from dark grow, set this transaction to isDark = true
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(sourceBlock)))
		{
			isDark = true;
		}
		
		String toLog = "";
		
		// Start log string with world name and coordinates
		if (PwnPlantGrowth.logCoords) 
		{
			toLog += coords + ": ";
		}

		toLog += "Growing: " + sourceBlock;

		Calculate cal = getCalcs(specialBlockList(e), sourceBlock, curBiome, isDark);
		toLog += cal.doLog;
		e.setCancelled(cal.isCancelled);
		if (cal.replacement != null) {
			e.getBlock().setType(cal.replacement);
		}
		
		// Log it
		if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logPlantEnabled))  
    	{	
    		PwnPlantGrowth.logToFile(toLog, "BlockSpread");
    	}	
	}
	
}