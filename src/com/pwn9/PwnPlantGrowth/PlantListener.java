package com.pwn9.PwnPlantGrowth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class PlantListener implements Listener 
{

	private final PwnPlantGrowth plugin;
	
	public PlantListener(PwnPlantGrowth plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	    this.plugin = plugin;
	}
	
	// Listen for plant growth
	@EventHandler(ignoreCancelled = true)
	public void plantGrow(BlockGrowEvent e) 
	{
		
		// Enabled in world?
		World world = e.getBlock().getWorld();
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) return;

		// Get current block type and make a string for comparison later
		String curBlock = String.valueOf(e.getBlock().getType());	
		String downBlock = String.valueOf(e.getBlock().getRelative(BlockFace.DOWN).getType());
		
		// Is anything set for this block in the config, if not, abort - NO this breaks AIR
		if (!(plugin.getConfig().isSet(curBlock)) && (curBlock != "AIR")) {
			PwnPlantGrowth.logToFile("No configuration set in config for: " + curBlock);
			return;
		}
		
		// Get current biome and make a string for comparison later
		String curBiome = PwnPlantGrowth.getBiome(e);
				
		// Get coords of the event for logging
		String coords = String.valueOf(e.getBlock().getLocation());
			
		List<String> fBlocksFound = new ArrayList<String>();
		List<String> wkBlocksFound = new ArrayList<String>();
		List<String> uvBlocksFound = new ArrayList<String>();
		
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
				
		// Setup boolean to see if event is in defined natural light or not
		Boolean isDark = false;
		
		// Get the current natural light level
		int lightLevel = e.getBlock().getLightFromSky();
		
		// Do we want to check on actual lighting?  Maybe in a future release
		//int actualLight = e.getBlock().getLightFromBlocks();
		
		// If the light level is lower than configured threshold and the plant is NOT exempt from dark grow, set this transaction to isDark = true
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(e.getBlock().getType().toString())))
		{
			isDark = true;
		}
		
		// Start log string with world name and coordinates
		String toLog = coords +": ";
			
		// Log, if AIR or NORMAL growth event
		if (e.getBlock().getType() == Material.AIR) 
		{
			// AIR block, get block type of the actual plant later
			toLog += "Growing: ";
		}
		else 
		{
			toLog += "Growing: " +e.getBlock().getType();
		}
			
		// Regular growth blocks that do not report initially as AIR - this is most of the normal crops
		if (curBlock != "AIR")
		{
			if ((plugin.getConfig().getList(curBlock+".Biome").contains(curBiome)) || (plugin.getConfig().getList(curBlock+".Biome").isEmpty())) 
			{
				// get the configured growth and death rates, by hierarchy of importance
				
				// default config rates
				int curGrowth = plugin.getConfig().getInt(curBlock+".Growth");
				int curDeath = plugin.getConfig().getInt(curBlock+".Death");
				
				// per biome rates (if exist in config)
				if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Growth")) 
				{
					curGrowth = plugin.getConfig().getInt(curBlock+"."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Death")) 
				{
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
							
				// do some math based on curGrowth settings from above with our randomizer to determine if this plant should grow right now
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					
					// If weedkiller is enabled and found, don't let plant die.
					if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller))
					{
						toLog += PwnPlantGrowth.wkFound;
					}
					// Else get the death chance for plant.
					else 
					{
						if (PwnPlantGrowth.random(curDeath)) 
						{
							if (curBlock == "COCOA") {
								e.getBlock().setType(Material.VINE);
							}
							else {
								e.getBlock().setType(Material.LONG_GRASS);
							}
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
		}		
					
		// AIR BLOCKS - when event returns AIR as the block type, it must be one of the following
		else if (curBlock == "AIR") 
		{
			
			// Handle Cactus, Sugar Cane; the plants that grow vertically only.
			if (downBlock == "CACTUS" || downBlock == "SUGAR_CANE_BLOCK") 
			{
	
				toLog += downBlock;
				
				if ((plugin.getConfig().getList(downBlock+".Biome").contains(curBiome)) || (plugin.getConfig().getList(downBlock+".Biome").isEmpty())) 
				{
					
					// get the configured growth and death rates, by hierarchy of importance
						
					// default config rates
					int curGrowth = plugin.getConfig().getInt(downBlock+".Growth");
					int curDeath = plugin.getConfig().getInt(downBlock+".Death");
					
					// per biome rates (if exist)
					if (plugin.getConfig().isSet(downBlock+"."+curBiome+".Growth")) 
					{
						curGrowth = plugin.getConfig().getInt(downBlock+"."+curBiome+".Growth");
					}
					
					if (plugin.getConfig().isSet(downBlock+"."+curBiome+".Death")) 
					{
						curDeath = plugin.getConfig().getInt(downBlock+"."+curBiome+".Death");
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
							if (plugin.getConfig().isSet(downBlock+".GrowthDark")) 
							{
								curGrowth = plugin.getConfig().getInt(downBlock+".GrowthDark");
							}
							
							if (plugin.getConfig().isSet(downBlock+".DeathDark")) 
							{
								curDeath = plugin.getConfig().getInt(downBlock+".DeathDark");
							}
							
							// per biome isDark rates (if exist)
							if (plugin.getConfig().isSet(downBlock+"."+curBiome+".GrowthDark")) 
							{
								curGrowth = plugin.getConfig().getInt(downBlock+"."+curBiome+".GrowthDark");
							}
							
							if (plugin.getConfig().isSet(downBlock+"."+curBiome+".DeathDark")) 
							{
								curDeath = plugin.getConfig().getInt(downBlock+"."+curBiome+".DeathDark");
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
								e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
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
			}
			
			// This is probably the regular growing grass, let's just leave this alone for now
			else if (downBlock == "LONG_GRASS" || downBlock == "GRASS") {
				
				// log it, general only occurs with bonemeal use but can be spammy in the logs
				toLog += " Grass grew";	
				
				// in the future we could add this to the config, initial test show it causes strange things though.
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
					thisBlock = "MELON_BLOCK";
				}
				// Pumpkin Block
				else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
						(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
						(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
						(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) 
				{
					 thisBlock = "PUMPKIN_BLOCK";	
				}
				else 
				{
					 thisBlock = "UNKNOWN_BLOCK";
					 toLog += thisBlock;	
					 // Log it
			    	 if (PwnPlantGrowth.logEnabled) 
			    	 {	
			    		 PwnPlantGrowth.logToFile(toLog);
			    	 }	
					 e.setCancelled(true);
					 return;
				}
				
				toLog += thisBlock;	
		
				if ((plugin.getConfig().getList(thisBlock+".Biome").contains(String.valueOf(e.getBlock().getBiome()))) || (plugin.getConfig().getList(thisBlock+".Biome").isEmpty())) 
				{	
					
					int curGrowth = plugin.getConfig().getInt(thisBlock+".Growth");
					int curDeath = plugin.getConfig().getInt(thisBlock+".Death");
					
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
						toLog += " Failed (Rate: "+curGrowth+") ";
						
						if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller)) 
						{
							toLog += PwnPlantGrowth.wkFound;
						}
						else 
						{
							if (PwnPlantGrowth.random(curDeath)) 
							{
								e.getBlock().setType(Material.LONG_GRASS);
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
			}
		}
		else 
		{
			toLog += " Uncaptured block grow event, derp?";
		}

		// Log it
    	if (PwnPlantGrowth.logEnabled) 
    	{	
    		PwnPlantGrowth.logToFile(toLog);
    	}	
	}
	
}