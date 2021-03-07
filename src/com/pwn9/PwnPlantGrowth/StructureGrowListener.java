package com.pwn9.PwnPlantGrowth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class StructureGrowListener implements Listener 
{

	private final PwnPlantGrowth plugin;
	
	public StructureGrowListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	    this.plugin = plugin;
	}
	
	static Calculate getCalcs(List<List<String>> specialBlocks, String thisBlock, String curBiome, Boolean isDark)
	{
		return new Calculate(specialBlocks, thisBlock, curBiome, isDark);
	}

	// retrieve list of special blocks
	public List<List<String>> specialBlockList(StructureGrowEvent e)
	{
		List<String> fBlocksFound = new ArrayList<String>();
		List<String> wkBlocksFound = new ArrayList<String>();
		List<String> uvBlocksFound = new ArrayList<String>();;

		List<List<String>> result = new ArrayList<List<String>>();
		
		if (PwnPlantGrowth.fenabled) 
		{
			for (int x = -(PwnPlantGrowth.fradius); x <= PwnPlantGrowth.fradius; x++) 
			{
	            for (int y = -(PwnPlantGrowth.fradius); y <= PwnPlantGrowth.fradius; y++) 
	            {
	               for (int z = -(PwnPlantGrowth.fradius); z <= PwnPlantGrowth.fradius; z++) 
	               {
	            	   fBlocksFound.add(String.valueOf(e.getLocation().getBlock().getRelative(x, y, z).getType()));
	               }
	            }
	        }
		}		
		
		if (PwnPlantGrowth.wkenabled)
		{
			for (int x = -(PwnPlantGrowth.wkradius); x <= PwnPlantGrowth.wkradius; x++) 
			{
	            for (int y = -(PwnPlantGrowth.wkradius); y <= PwnPlantGrowth.wkradius; y++) 
	            {
	               for (int z = -(PwnPlantGrowth.wkradius); z <= PwnPlantGrowth.wkradius; z++) 
	               {
	            	   wkBlocksFound.add(String.valueOf(e.getLocation().getBlock().getRelative(x, y, z).getType()));
	               }
	            }
	        }
		}		
		
		// Check for uv blocks
		if (PwnPlantGrowth.uvenabled)
		{
			for (int x = -(PwnPlantGrowth.uvradius); x <= PwnPlantGrowth.uvradius; x++) 
			{
	            for (int y = -(PwnPlantGrowth.uvradius); y <= PwnPlantGrowth.uvradius; y++) 
	            {
	               for (int z = -(PwnPlantGrowth.uvradius); z <= PwnPlantGrowth.uvradius; z++) 
	               {
	            	   uvBlocksFound.add(String.valueOf(e.getLocation().getBlock().getRelative(x, y, z).getType()));
	               }
	            }
	        }
		}	
		
		result.add(fBlocksFound);
		result.add(wkBlocksFound);
		result.add(uvBlocksFound);

		return result;
	}	
	
	// Structure Growth eg: trees
	@EventHandler(ignoreCancelled = false)
	public void structureGrow(StructureGrowEvent e) 
	{
	
		// Enabled in world?
		World world = e.getLocation().getWorld();
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;

		// Get current block type and make a string for comparison later
		String eventBlock = String.valueOf(e.getSpecies());
		
		// Get event coords
		String coords = String.valueOf(e.getLocation());
		
		// Get the material type of the block at the event coords
		String curBlock = String.valueOf(e.getLocation().getBlock().getType());
		
		// Get current biome and make a string for comparison later
		String curBiome = PwnPlantGrowth.getBiome(e);
		
		if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logTreeEnabled) && (PwnPlantGrowth.logVerbose)) 
		{
			PwnPlantGrowth.logToFile("Structure Event for: " + curBlock + " - In biome: " + curBiome, "StructureGrow");
			PwnPlantGrowth.logToFile("Species: " + eventBlock, "StructureGrow");
		}		
		
		//TODO: check for bonemeal usage on structure growth and handle it
		if (e.isFromBonemeal()) {
			// bonemeal triggered this event, what should we do with it?
			if (!(PwnPlantGrowth.limitBonemeal)) 
			{
				if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logTreeEnabled)) 
				{
					PwnPlantGrowth.logToFile("Bonemeal was used on " + curBlock, "TreeGrow");
				}
				return;
			}
		}
		
		// Is anything set for this block in the config, if not, abort
		if (!(plugin.getConfig().isSet(curBlock))) 
		{
			PwnPlantGrowth.logToFile("No tree configuration set in config for: " + curBlock);
			return;
		}
				
		// Setup boolean to see if event is in defined natural light or not
		Boolean isDark = false;
		
		// Get the current natural light level
		int lightLevel = e.getLocation().getBlock().getLightFromSky();
		
		// If the light level is lower than configured threshold and the plant is NOT exempt from dark grow, set this transaction to isDark = true
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(e.getSpecies().toString())))
		{
			isDark = true;
		}
		
		String toLog = "";
		
		if (PwnPlantGrowth.logCoords) 
		{
			toLog += coords + ": Growing: " + curBlock;
		}
		else {
			toLog += "Growing: " + curBlock;
		}

		Calculate cal = getCalcs(specialBlockList(e), curBlock, curBiome, isDark);
		toLog += cal.doLog;
		e.setCancelled(cal.isCancelled);
		if (cal.replacement != null) {
			e.getLocation().getBlock().setType(cal.replacement);
		}
		

		// log it
		if ((PwnPlantGrowth.logEnabled) && (PwnPlantGrowth.logTreeEnabled)) 
		{	
			PwnPlantGrowth.logToFile(toLog, "StructureGrow");
		}
		
		return;
	}
	
}