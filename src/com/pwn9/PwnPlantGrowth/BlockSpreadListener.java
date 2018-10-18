package com.pwn9.PwnPlantGrowth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
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
	
	// called to run the calculations and determine what happens to the plant on the event
	public String runCalcs(BlockSpreadEvent e, String thisBlock, String curBiome, Boolean isDark) 
	{
		
		String toLog = "";
		
		if ((plugin.getConfig().getList(thisBlock+".Biome").contains(curBiome)) || 
				(plugin.getConfig().getList(thisBlock+".Biome").isEmpty()) || 
				(plugin.getConfig().isSet(thisBlock+".BiomeGroup"))) 
		{	
			// check the area to find if any of the special blocks are found
			List<List<String>> specialBlocks = specialBlockList(e);
			List<String> fBlocksFound = specialBlocks.get(0);
			List<String> wkBlocksFound = specialBlocks.get(1);
			List<String> uvBlocksFound = specialBlocks.get(2);
			
			int curGrowth = plugin.getConfig().getInt(thisBlock+".Growth");
			int curDeath = plugin.getConfig().getInt(thisBlock+".Death");
			
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
					if (plugin.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome)) 
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
							if (plugin.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome)) 
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
					if (PwnPlantGrowth.random(curDeath)) 
					{
						// TODO: make these configurable
						if (thisBlock == "KELP") {
							e.getBlock().setType(Material.SEAGRASS);
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
		}	
		
		return toLog;
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
	
	// Listen for plant growth and then do stuff
	@EventHandler(ignoreCancelled = false)
	public void blockSpread(BlockSpreadEvent e) 
	{
		
		// Enabled in world?
		World world = e.getBlock().getWorld();
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;

		// Get source block type and make a string for comparison later
		String sourceBlock = String.valueOf(e.getSource().getType());
		
		// we only care about these 2 for now
		if (sourceBlock != "CHORUS_FLOWER" && sourceBlock != "KELP") 
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

		toLog += runCalcs(e, sourceBlock, curBiome, isDark);

		// Log it
		if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logPlantEnabled))  
    	{	
    		PwnPlantGrowth.logToFile(toLog, "BlockSpread");
    	}	
	}
	
}