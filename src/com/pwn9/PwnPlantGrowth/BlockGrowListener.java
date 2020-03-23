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
	
	// called to run the calculations and determine what happens to the plant on the event
	public String runCalcs(BlockGrowEvent e, String thisBlock, String curBiome, Boolean isDark) 
	{
		String toLog = "";
		String frontLog = ", Biome: " + curBiome + ", Dark: " + isDark.toString() + ", ";
		String darkLog = "Dark Settings: {";
		String groupLog = "Settings: {";
		// bool to catch if the biome is never declared in any config, therefor a bad biome and should not grow
		boolean noBiome = true;
		
		int curGrowth = plugin.getConfig().getInt(thisBlock+".Growth");
		frontLog += "Default Growth: " + curGrowth + ", ";
		int curDeath = plugin.getConfig().getInt(thisBlock+".Death");
		frontLog += "Default Death: " + curDeath + ", ";
		
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
				
				groupLog += "BiomeGroup: " + groupList.toString() + ", ";
				
				// iterate through list and see if any of that list matches curBiome
				boolean matches = false;
				for (int i = 0; i < groupList.size(); i++) 
				{
					
					// check the biomegroup for this named group
					if ((plugin.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (plugin.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
					{
						matches = true;
						noBiome = false;
						groupLog += "Matches: " + groupList.get(i) + ", ";
						
						// reference the configs now to see if the config settings are set!
						if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Growth")) 
						{
							curGrowth = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Growth");
							groupLog += "New Growth: " + curGrowth + ", ";
						}
						
						if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Death")) 
						{
							curDeath = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Death");
							groupLog += "New Death: " + curDeath + ", ";
						}						
					}	
				}
				if (!matches) {
					groupLog += "Matches: NULL, ";
				}
			}	
			else {
				groupLog += "BiomeGroup: NULL,  ";
			}
			
			groupLog += "Specific Settings: {";
			
			if (plugin.getConfig().getList(thisBlock+".Biome").contains(curBiome)) {
				noBiome = false;
				// override with individual settings
				if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".Growth")) 
				{
					curGrowth = plugin.getConfig().getInt(thisBlock+"."+curBiome+".Growth");
					groupLog += "Growth for " + curBiome + ": " + curGrowth + ", ";
				}
				
				if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".Death")) 
				{
					curDeath = plugin.getConfig().getInt(thisBlock+"."+curBiome+".Death");
					groupLog += "Death for " + curBiome + ": " + curDeath + ", ";
				}
			}
			
			// if there is fertilizer, grow this plant at the fertilizer rate - default 100%
			// TODO: should fertilizer override dark settings or not - i think not for now
			if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
			{
				groupLog += PwnPlantGrowth.fertFound;
				// set the current growth to the fertilizer rate
				curGrowth = PwnPlantGrowth.frate;
			}
			groupLog += "}}, ";
			
			// See if there are special settings for dark growth
			if (isDark) 
			{
				// If uv is enabled and found, isDark remains false.
				if (uvBlocksFound.contains(PwnPlantGrowth.uv))
				{
					darkLog += PwnPlantGrowth.uvFound;
				}
				else 
				{							
					// default isDark config rates (if exist)
					if (plugin.getConfig().isSet(thisBlock+".GrowthDark")) 
					{
						curGrowth = plugin.getConfig().getInt(thisBlock+".GrowthDark");
						darkLog += "Growth: " + curGrowth + ", ";
					}
					
					if (plugin.getConfig().isSet(thisBlock+".DeathDark")) 
					{
						curDeath = plugin.getConfig().getInt(thisBlock+".DeathDark");
						darkLog += "Death: " + curDeath + ", ";
					}
					
					// override default values with biome group values
					if (plugin.getConfig().isSet(thisBlock+".BiomeGroup")) 
					{
						
						// create list from the config setting
						List<?> groupList = plugin.getConfig().getList(thisBlock+".BiomeGroup");
						
						darkLog += "BiomeGroup: " + groupList.toString() + ", ";
						
						// iterate through list and see if any of that list matches curBiome
						boolean matches = false;
						for (int i = 0; i < groupList.size(); i++) {
							
							// check the biomegroup for this named group
							if  ((plugin.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (plugin.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
							{
								
								matches = true;
								noBiome = false;
								darkLog += "Matching: " + groupList.get(i) + ", ";
								
								// reference the configs now to see if the config settings are set!
								if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".GrowthDark")) 
								{
									curGrowth = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".GrowthDark");
									darkLog += "New Growth: " + curGrowth + ", ";
								}
								
								if (plugin.getConfig().isSet(thisBlock+"."+groupList.get(i)+".DeathDark")) 
								{
									curDeath = plugin.getConfig().getInt(thisBlock+"."+groupList.get(i)+".DeathDark");
									darkLog += "New Death: " + curDeath + ", ";
								}						
							}
						}
						if (!matches) {
							darkLog += "Matches: NULL, ";
						}
					}
					else {
						darkLog += "BiomeGroup: NULL, ";
					}
					
					darkLog += "Specific Settings: {";
					
					// per biome isDark rates (if exist)
					if (plugin.getConfig().getList(thisBlock+".Biome").contains(curBiome)) {
						noBiome = false;
						if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".GrowthDark")) 
						{
							curGrowth = plugin.getConfig().getInt(thisBlock+"."+curBiome+".GrowthDark");
							darkLog += "Growth for " + curBiome + ": " + curGrowth + ", ";
						}
						
						if (plugin.getConfig().isSet(thisBlock+"."+curBiome+".DeathDark")) 
						{
							curDeath = plugin.getConfig().getInt(thisBlock+"."+curBiome+".DeathDark");
							darkLog += "Death for " + curBiome + ": " + curDeath + ", ";
						}
					}
					
					darkLog += "}}, ";
				}
			}	
			
			// cancel bad biomes here
			if (noBiome) 
			{
				e.setCancelled(true);
				toLog += "RESULT: {Failed Growth: Bad Biome}";	
				// chance of death
				if (PwnPlantGrowth.random(curDeath)) 
				{
					// TODO: make these configurable
					if (thisBlock == "COCOA") {
						e.getBlock().setType(Material.VINE);
					}
					else {
						e.getBlock().setType(Material.DEAD_BUSH);
					}
					toLog += " {Plant Died, Rate: " + curDeath + "}";
				}				
			}
			// Run the chance for growth here... 
			else if (!(PwnPlantGrowth.random(curGrowth))) 
			{
				e.setCancelled(true);
				toLog += "RESULT: {Failed Growth, Rate: " + curGrowth + "} ";
				
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
							e.getBlock().setType(Material.DEAD_BUSH);
						}
						toLog += " {Plant Died, Rate: " + curDeath + "}";
					}
				}
			}
			else 
			{
				toLog += "RESULT: {Plant Grew, Rate: " + curGrowth + "}";
				
			}
		}
		else 
		{
			e.setCancelled(true);
			toLog += "RESULT: {Failed Growth: Bad Biome}";	
			// chance of death
			if (PwnPlantGrowth.random(curDeath)) 
			{
				// TODO: make these configurable
				if (thisBlock == "COCOA") {
					Material replace = Material.VINE;
					e.getBlock().setType(replace);
				}
				else {
					e.getBlock().setType(Material.DEAD_BUSH);
				}
				toLog += " {Plant Died, Rate: " + curDeath + "}";
			}		
		}	
		
		String midLog = "";
		if (isDark) {
			midLog += darkLog;
		} 
		else {
			midLog += groupLog;
		}
		
		return frontLog + midLog + ", "+ toLog;
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
			//toLog += runCalcs(e, curBlock, curBiome, isDark);
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
			
			// Handle Cactus, Sugar Cane; the plants that grow vertically only.
			if (downBlock == "CACTUS" || downBlock == "SUGAR_CANE") 
			{
	
				toLog += downBlock;
				
				// run calcs
				//toLog += runCalcs(e, downBlock, curBiome, isDark);
				Calculate cal = getCalcs(specialBlockList(e), downBlock, curBiome, isDark);
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
				//toLog += runCalcs(e, thisBlock, curBiome, isDark);
				Calculate cal = getCalcs(specialBlockList(e), thisBlock, curBiome, isDark);
				toLog += cal.doLog;
				e.setCancelled(cal.isCancelled);
				if (cal.replacement != null) {
					e.getBlock().setType(cal.replacement);
				}
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