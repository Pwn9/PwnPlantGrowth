package com.pwn9.PwnPlantGrowth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrowListener implements Listener 
{

	private final PwnPlantGrowth plugin;
	
	public BlockGrowListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	    this.plugin = plugin;
	}

	static Calculate getCalcs(List<List<String>> specialBlocks, String thisBlock, String curBiome, Boolean isDark)
	{
		return new Calculate(specialBlocks, thisBlock, curBiome, isDark);
	}
	
	// retrieve list of special blocks
	public List<List<String>> specialBlockList(BlockGrowEvent e)
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
	
	// Listen for plant growth and then do stuff
	@EventHandler(ignoreCancelled = false)
	public void plantGrow(BlockGrowEvent e) 
	{
		
		// Enabled in world?
		World world = e.getBlock().getWorld();
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;

		// Get current block type and make a string for comparison later
		String curBlock = String.valueOf(e.getBlock().getType());	
		String downBlock = String.valueOf(e.getBlock().getRelative(BlockFace.DOWN).getType());
		
		// Get current biome and make a string for comparison later
		String curBiome = PwnPlantGrowth.getBiome(e);
		
		if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logPlantEnabled) && (PwnPlantGrowth.logVerbose))
		{
			PwnPlantGrowth.logToFile("Block Event for: " + curBlock + " - In biome: " + curBiome, "BlockGrow");
		}	
		
		// Is anything set for this block in the config, or is it AIR? If not, abort.
		if (!(plugin.getConfig().isSet(curBlock)) && (curBlock != "AIR") && (curBlock != "WATER")) {
			PwnPlantGrowth.logToFile("No plant configuration set in config for: " + curBlock);
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
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(curBlock)))
		{
			isDark = true;
		}
		
		String toLog = "";
		
		// Start log string with world name and coordinates
		if (PwnPlantGrowth.logCoords) 
		{
			toLog += coords + ": ";
		}

		// Log, if AIR or NORMAL growth event
		if (e.getBlock().getType() == Material.AIR) 
		{
			// AIR block, get block type of the actual plant later
			toLog += "Growing: ";
		}
		else 
		{
			toLog += "Growing: " + curBlock;
		}
			
		// Regular growth blocks that do not report initially as AIR - this is most of the normal crops
		if (curBlock != "AIR")
		{
			// run calcs
			Calculate cal = getCalcs(specialBlockList(e), curBlock, curBiome, isDark);
			toLog += cal.doLog;
			e.setCancelled(cal.isCancelled);
			if (cal.replacement != null) {
				e.getBlock().setType(cal.replacement);
			}
		}		
					
		// AIR BLOCKS - when event returns AIR as the block type, it must be one of the following
		else if ((curBlock == "AIR"))
		{
			
			// Get facing blocks, to check for melon or pumpkin stem.
			String faceBlock;

			// Melon Block
			if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.MELON_STEM)) 
			{
				faceBlock = "MELON";
			}
			// Pumpkin Block
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) 
			{
				 faceBlock = "PUMPKIN";	
			}
			else {
				faceBlock = "AIR";
			}
			
			// Handle Cactus, Sugar Cane, Bamboo; the plants that grow vertically only.
			if (downBlock == "CACTUS" || downBlock == "SUGAR_CANE") 
			{
	
				toLog += downBlock;
				
				// run calcs
				Calculate cal = getCalcs(specialBlockList(e), downBlock, curBiome, isDark);
				toLog += cal.doLog;
				e.setCancelled(cal.isCancelled);
				if (cal.replacement != null) {
					e.getBlock().setType(cal.replacement);
				}
			}
			// not a vertical growing plant so fallback to a melon
			else if (faceBlock == "MELON" || faceBlock == "PUMPKIN") 
			{
	
				toLog += faceBlock;
				
				// run calcs
				Calculate cal = getCalcs(specialBlockList(e), faceBlock, curBiome, isDark);
				toLog += cal.doLog;
				e.setCancelled(cal.isCancelled);
				if (cal.replacement != null) {
					e.getBlock().setType(cal.replacement);
				}
			}			
			
			// This is regular growing grass or bonemeal on grass
			else if (downBlock == "GRASS" || downBlock == "GRASS_BLOCK" || downBlock == "TALL_GRASS") {
				
				// log it, generally only occurs with bonemeal use but can be spammy in the logs
				toLog += downBlock;

				// run calcs - we're using GRASS as the downblock for all of these so that we only need one config
				//toLog += runCalcs(e, "GRASS", curBiome, isDark);
				Calculate cal = getCalcs(specialBlockList(e), "GRASS", curBiome, isDark);
				toLog += cal.doLog;
				e.setCancelled(cal.isCancelled);
				if (cal.replacement != null) {
					e.getBlock().setType(cal.replacement);
				}
			}
			
			// An unknown block growth event from AIR has occurred - log to main log for debugging
			else 
			{
				// were not sure what has happened so log it and let it grow, maybe a new plant we haven't caught yet.
				toLog += "on " + downBlock + " as UNKNOWN EVENT at location: " + coords;
				// Log it
				if (PwnPlantGrowth.logEnabled) 
				{	
					PwnPlantGrowth.logToFile(toLog);
				}	
				//e.setCancelled(true);
				return;
			}
		}
		else 
		{
			toLog += " Uncaptured block grow event, derp?";
		}

		// Log it
		if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logPlantEnabled))  
    	{	
    		PwnPlantGrowth.logToFile(toLog, "BlockGrow");
    	}	
		
		return;
	}
	
}