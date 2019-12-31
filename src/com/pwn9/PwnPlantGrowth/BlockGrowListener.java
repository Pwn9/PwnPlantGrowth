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
	
	// called to run the calculations and determine what happens to the plant on the event
	public String runCalcs(BlockGrowEvent e, String thisBlock, String curBiome, Boolean isDark) 
	{
		
		String toLog = "";
		
		int curGrowth = plugin.getConfig().getInt(thisBlock+".Growth");
		int curDeath = plugin.getConfig().getInt(thisBlock+".Death");
		
		if ((plugin.getConfig().isSet(thisBlock+".BiomeGroup")) || (plugin.getConfig().getList(thisBlock+".Biome").isEmpty()) || (plugin.getConfig().getList(thisBlock+".Biome").contains(curBiome))) 
		{	
			// check the area to find if any of the special blocks are found
			List<List<String>> specialBlocks = specialBlockList(e);
			List<String> fBlocksFound = specialBlocks.get(0);
			List<String> wkBlocksFound = specialBlocks.get(1);
			List<String> uvBlocksFound = specialBlocks.get(2);
			
			// check the biome group settings
			if (plugin.getConfig().isSet(thisBlock+".BiomeGroup")) 
			{
				
				// create list from the config setting
				List<?> groupList = plugin.getConfig().getList(thisBlock+".BiomeGroup");
				
				toLog += " BiomeGroup is set: " + groupList.toString() + " - ";
				
				// iterate through list and see if any of that list matches curBiome
				for (int i = 0; i < groupList.size(); i++) 
				{
					
					// check the biomegroup for this named group
					if ((plugin.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (plugin.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
					{
						
						toLog += "Matching BiomeGroup." + groupList.get(i) + " ";

						// reference the configs now to see if the config settings are set!
						if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Growth")) 
						{
							curGrowth = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Growth");
						}
						
						if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Death")) 
						{
							curDeath = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Death");
						}						
					}
					else {
						toLog += "Missing BiomeGroup." + groupList.get(i) + " ";
					}
				}
			}	
			else {
				toLog += " No BiomeGroup Found - ";
			}
			
			// override with individual settings
			if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".Growth")) 
			{
				curGrowth = plugin.getConfig().getInt(thisBlock+"."+curBiome+".Growth");
			}
			
			if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".Death")) 
			{
				curDeath = plugin.getConfig().getInt(thisBlock+"."+curBiome+".Death");
			}
			
			// if there is fertilizer, grow this plant at the fertilizer rate - default 100%
			// TODO: should fertilizer override dark settings or not - i think not for now
			if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
			{
				toLog += PwnPlantGrowth.fertFound;
				// set the current growth to the fertilizer rate
				curGrowth = PwnPlantGrowth.frate;
			}
			
			// See if there are special settings for dark growth
			if (isDark) 
			{
				// If uv is enabled and found, isDark remains false.
				if (uvBlocksFound.contains(PwnPlantGrowth.uv))
				{
					toLog += PwnPlantGrowth.uvFound;
				}
				else 
				{							
					toLog += " In dark. ";
					
					// default isDark config rates (if exist)
					if (plugin.getConfig().isSet(thisBlock+".GrowthDark")) 
					{
						curGrowth = plugin.getConfig().getInt(thisBlock+".GrowthDark");
					}
					
					if (plugin.getConfig().isSet(thisBlock+".DeathDark")) 
					{
						curDeath = plugin.getConfig().getInt(thisBlock+".DeathDark");
					}
					
					// override default values with biome group values
					if (plugin.getConfig().isSet(thisBlock+".BiomeGroup")) 
					{
						
						// create list from the config setting
						List<?> groupList = plugin.getConfig().getList(thisBlock+".BiomeGroup");
						
						toLog += " BiomeGroup is set: " + groupList.toString() + " - ";
						
						// iterate through list and see if any of that list matches curBiome
						for (int i = 0; i < groupList.size(); i++) {
							
							// check the biomegroup for this named group
							if  ((plugin.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (plugin.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
							{
								
								toLog += "Matching BiomeGroup." + groupList.get(i) + " ";
								
								// reference the configs now to see if the config settings are set!
								if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".GrowthDark")) 
								{
									curGrowth = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".GrowthDark");
								}
								
								if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".DeathDark")) 
								{
									curDeath = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".DeathDark");
								}						
							}
							else 
							{
								toLog += "Missing BiomeGroup." + groupList.get(i) + " ";	
							}
						}
					}	
					
					// per biome isDark rates (if exist)
					if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".GrowthDark")) 
					{
						curGrowth = plugin.getConfig().getInt(thisBlock+"."+curBiome+".GrowthDark");
					}
					
					if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".DeathDark")) 
					{
						curDeath = plugin.getConfig().getInt(thisBlock+"."+curBiome+".DeathDark");
					}
				}
			}	
			// Run the chance for growth here... 
			if (!(PwnPlantGrowth.random(curGrowth))) 
			{
				e.setCancelled(true);
				toLog += " Failed (Rate: " + curGrowth + ") ";
				
				if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller)) 
				{
					toLog += PwnPlantGrowth.wkFound;
				}
				else 
				{
					// chance of death
					if (PwnPlantGrowth.random(curDeath)) 
					{
						// TODO: make these configurable
						if (thisBlock == "COCOA") {
							e.getBlock().setType(Material.VINE);
						}
						else {
							e.getBlock().setType(Material.GRASS);
						}
						toLog += " Died (Rate: " + curDeath + ")";
					}
				}
			}									
		}
		else 
		{
			e.setCancelled(true);
			toLog += " Failed: Bad Biome";	
			// chance of death
			if (PwnPlantGrowth.random(curDeath)) 
			{
				// TODO: make these configurable
				if (thisBlock == "COCOA") {
					e.getBlock().setType(Material.VINE);
				}
				else {
					e.getBlock().setType(Material.GRASS);
				}
				toLog += " Died (Rate: " + curDeath + ")";
			}			
		}	
		
		return toLog;
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
			toLog += runCalcs(e, curBlock, curBiome, isDark);
		}		
					
		// AIR BLOCKS - when event returns AIR as the block type, it must be one of the following
		else if ((curBlock == "AIR"))
		{
			
			// Handle Cactus, Sugar Cane; Kelp; the plants that grow vertically only.
			if (downBlock == "CACTUS" || downBlock == "SUGAR_CANE") 
			{
	
				toLog += downBlock;
				
				// run calcs
				toLog += runCalcs(e, downBlock, curBiome, isDark);
				
			}
			
			// This is probably the regular growing grass, let's just leave this alone for now
			else if (downBlock == "GRASS" || downBlock == "GRASS_BLOCK" || downBlock == "TALL_GRASS") {
				
				// log it, generally only occurs with bonemeal use but can be spammy in the logs
				toLog += downBlock;
				
				// run calcs
				toLog += runCalcs(e, downBlock, curBiome, isDark);
				
				//TODO: this is an odd case, we need to handle this in some other way because it's not canceling in runcalcs.
			}
			
			// Specially Handle Melon/Pumpkin Blocks
			else 
			{
				String thisBlock;
				
				// Melon Block
				if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.MELON_STEM) ||
						(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.MELON_STEM) ||
						(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.MELON_STEM) ||
						(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.MELON_STEM)) 
				{
					thisBlock = "MELON";
				}
				// Pumpkin Block
				else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
						(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
						(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
						(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) 
				{
					 thisBlock = "PUMPKIN";	
				}
				else 
				{
					// were not sure what has happened so log it and cancel
					toLog += downBlock + " as UNKNOWN EVENT";	
					// Log it
					if (PwnPlantGrowth.logEnabled) 
					{	
						PwnPlantGrowth.logToFile(toLog);
					}	
					e.setCancelled(true);
					return;
				}
				
				toLog += thisBlock;	
				
				// run calcs
				toLog += runCalcs(e, thisBlock, curBiome, isDark);
		
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
	}
	
}