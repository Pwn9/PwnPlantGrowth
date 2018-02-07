package com.pwn9.PwnPlantGrowth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class TreeListener implements Listener 
{

	private final PwnPlantGrowth plugin;
	
	public TreeListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	    this.plugin = plugin;
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
	@EventHandler(ignoreCancelled = true)
	public void structureGrow(StructureGrowEvent e) 
	{
	
		// Enabled in world?
		World world = e.getLocation().getWorld();
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;

		// Get current block type and make a string for comparison later
		String curBlock = String.valueOf(e.getSpecies());
		
		//TODO: check for bonemeal usage on structure growth and handle it
		if (e.isFromBonemeal()) {
			// bonemeal triggered this event, what should we do with it?
			if (!(PwnPlantGrowth.limitBonemeal)) {
				PwnPlantGrowth.logToFile("Bonemeal was used on " + curBlock);
				return;
			}
		}
		
		// Is anything set for this block in the config, if not, abort
		if (!(plugin.getConfig().isSet(curBlock))) {
			PwnPlantGrowth.logToFile("No configuration set in config for: " + curBlock);
			return;
		}
		
		// Get current biome and make a string for comparison later
		String curBiome = PwnPlantGrowth.getBiome(e);
		
		// Get event coords
		String coords = String.valueOf(e.getLocation());	

		// check the area to find if any of the special blocks are found
		List<List<String>> specialBlocks = specialBlockList(e);
		List<String> fBlocksFound = specialBlocks.get(0);
		List<String> wkBlocksFound = specialBlocks.get(1);
		List<String> uvBlocksFound = specialBlocks.get(2);
		
		// Setup boolean to see if event is in defined natural light or not
		Boolean isDark = false;
		
		// Get the current natural light level
		int lightLevel = e.getLocation().getBlock().getLightFromSky();
		
		// If the light level is lower than configured threshold and the plant is NOT exempt from dark grow, set this transaction to isDark = true
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(e.getSpecies().toString())))
		{
			isDark = true;
		}
		
		String toLog = coords + ": Growing: " +e.getSpecies();

		if ((plugin.getConfig().getList(curBlock+".Biome").contains(curBiome)) || (plugin.getConfig().getList(curBlock+".Biome").isEmpty())) 
		{	
			
			// get this block's default values
			int curGrowth = plugin.getConfig().getInt(curBlock+".Growth");
			int curDeath = plugin.getConfig().getInt(curBlock+".Death");
			
			
			//TODO: this will be useful if I can figure out a nice way to do it
			
			// override default values with biome group values
			if (plugin.getConfig().isSet(curBlock+".BiomeGroup")) {
				
				// see if this BiomeGroup has a biomegroups list attached
				if (plugin.getConfig().isSet("BiomeGroup.")) {
					
				}
				
				// check the list reference to config.biomegroups.thisname to see if it contains curBiome
				
				
				// get the values.
			}
			
			
			
			
			// override default and group values with per biome settings
			if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Growth")) 
			{
				curGrowth = plugin.getConfig().getInt(curBlock+"."+curBiome+".Growth");
			}
			
			if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Death")) {
				curDeath = plugin.getConfig().getInt(curBlock+"."+curBiome+".Death");
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
					if (plugin.getConfig().isSet(curBlock+".GrowthDark")) 
					{
						curGrowth = plugin.getConfig().getInt(curBlock+".GrowthDark");
					}
					
					if (plugin.getConfig().isSet(curBlock+".DeathDark")) 
					{
						curDeath = plugin.getConfig().getInt(curBlock+".DeathDark");
					}
					
					// per biome isDark rates (if exist)
					if (plugin.getConfig().isSet(curBlock+"."+curBiome+".GrowthDark")) 
					{
						curGrowth = plugin.getConfig().getInt(curBlock+"."+curBiome+".GrowthDark");
					}
					
					if (plugin.getConfig().isSet(curBlock+"."+curBiome+".DeathDark")) 
					{
						curDeath = plugin.getConfig().getInt(curBlock+"."+curBiome+".DeathDark");
					}
				}
			}				
			
			if (!(PwnPlantGrowth.random(curGrowth)))
			{
				e.setCancelled(true);
				toLog += " Failed (Rate: "+curGrowth+") ";
				
				if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller))
				{
					toLog += PwnPlantGrowth.wkFound;
				}
				else 
				{
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " Died (Rate: "+curDeath+")";
					}
				}
			}											
		}
		else 
		{
			e.setCancelled(true);
			toLog += " Failed: Bad Biome";				
		}				

		// log it
		if (PwnPlantGrowth.logEnabled) 
		{	
			PwnPlantGrowth.logToFile(toLog);
		}
	}
	
}