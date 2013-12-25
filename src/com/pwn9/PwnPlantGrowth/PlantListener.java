package com.pwn9.PwnPlantGrowth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;

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
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) 
		{
			return;
		}
		
		List<String> fBlocksFound = new ArrayList<String>();
		List<String> wkBlocksFound = new ArrayList<String>();
		
		if (PwnPlantGrowth.fenabled) 
		{
			for (int x = -(PwnPlantGrowth.fradius); x <= PwnPlantGrowth.fradius; x++) 
			{
	            for (int y = -(PwnPlantGrowth.fradius); y <= PwnPlantGrowth.fradius; y++) 
	            {
	               for (int z = -(PwnPlantGrowth.fradius); z <= PwnPlantGrowth.fradius; z++) 
	               {
	            	   fBlocksFound.add(e.getBlock().getRelative(x, y, z).getType().toString());
	               }
	            }
	        }
		}		
		
		if (PwnPlantGrowth.wkenabled)
		{
			for (int xx = -(PwnPlantGrowth.wkradius); xx <= PwnPlantGrowth.wkradius; xx++) 
			{
	            for (int yy = -(PwnPlantGrowth.wkradius); yy <= PwnPlantGrowth.wkradius; yy++) 
	            {
	               for (int zz = -(PwnPlantGrowth.wkradius); zz <= PwnPlantGrowth.wkradius; zz++) 
	               {
	            	   wkBlocksFound.add(e.getBlock().getRelative(xx, yy, zz).getType().toString());
	               }
	            }
	        }
		}		
		
		String coords = e.getBlock().getLocation().toString();
		
		// Light level acceptable?
		int lightLevel = e.getBlock().getLightFromSky();
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(e.getBlock().getType().toString())))
		{
	    	if (PwnPlantGrowth.logEnabled) 
	    	{	
	    		PwnPlantGrowth.logToFile(coords +": Too dark to grow " + e.getBlock().getType());
	    	}			
			e.setCancelled(true);
			return;
		}
		
		String toLog = coords +": ";
			
		// Start log, if AIR or NORMAL growth event
		if (e.getBlock().getType() == Material.AIR) 
		{
			toLog += "Growing: ";
		}
		else 
		{
			toLog += "Growing: " +e.getBlock().getType();
		}
			
		// Get current biome and make a string for comparison later
		String curBiome = e.getBlock().getBiome().toString();
		
		// Get current block type and make a string for comparison later
		String curBlock = e.getBlock().getType().toString();
		
		// Regular growth blocks that do no report initially as AIR - this is most of the normal crops
		if (curBlock != "AIR")
		{
			if ((plugin.getConfig().getList(curBlock+".Biome").contains(curBiome)) || (plugin.getConfig().getList(curBlock+".Biome").isEmpty())) 
			{
				if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
				{
					toLog += " has Fertilizer.";
				}
				else 
				{
					int curGrowth = plugin.getConfig().getInt(curBlock+".Growth");
					int curDeath = plugin.getConfig().getInt(curBlock+".Death");
					
					if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Growth")) 
					{
						curGrowth = plugin.getConfig().getInt(curBlock+"."+curBiome+".Growth");
					}
					
					if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Death")) 
					{
						curDeath = plugin.getConfig().getInt(curBlock+"."+curBiome+".Death");
					}
										
					if (!(PwnPlantGrowth.random(curGrowth))) 
					{
						e.setCancelled(true);
						toLog += " Failed (Rate: "+curGrowth+") ";
						
						if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller))
						{
							toLog += " Has Weed Killer. ";
						}
						else 
						{
							toLog += " No Weed Killer. ";
							if (PwnPlantGrowth.random(curDeath)) 
							{
								e.getBlock().setType(Material.LONG_GRASS);
								toLog += " Died (Rate: "+curDeath+")";
							}
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
		if (curBlock == "AIR") 
		{
			
			String downBlock = e.getBlock().getRelative(BlockFace.DOWN).getType().toString();
			
			// Handle Cactus and Sugar Cane, the 2 that grow vertically only.
			if (downBlock == "CACTUS" || downBlock == "SUGAR_CANE_BLOCK") 
			{
				
				toLog += downBlock;
				
				if ((plugin.getConfig().getList(downBlock+".Biome").contains(curBiome)) || (plugin.getConfig().getList(downBlock+".Biome").isEmpty())) 
				{
					
					if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
					{
						toLog += " has Fertilizer.";
					}
					else 
					{
						int curGrowth = plugin.getConfig().getInt(downBlock+".Growth");
						int curDeath = plugin.getConfig().getInt(downBlock+".Death");
						
						if (plugin.getConfig().isSet(downBlock+"."+curBiome+".Growth")) 
						{
							curGrowth = plugin.getConfig().getInt(downBlock+"."+curBiome+".Growth");
						}
						
						if (plugin.getConfig().isSet(downBlock+"."+curBiome+".Death")) 
						{
							curDeath = plugin.getConfig().getInt(downBlock+"."+curBiome+".Death");
						}
						
						if (!(PwnPlantGrowth.random(curGrowth))) 
						{
							e.setCancelled(true);
							toLog += " Failed (Rate: "+curGrowth+") ";
							if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller))
							{
								toLog += " Has Weed Killer. ";
							}
							else 
							{
								toLog += " No Weed Killer. ";
								if (PwnPlantGrowth.random(curDeath)) 
								{
									e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
									toLog += " Died (Rate: "+curDeath+")";
								}
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
			
			// Specially Handle Melon Block
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.MELON_STEM)) 
			{
				toLog += "MELON_BLOCK";
				if ((plugin.getConfig().getList("MELON_BLOCK.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("MELON_BLOCK.Biome").isEmpty())) 
				{	
					
					if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
					{
						toLog += " has Fertilizer.";
					}
					else 
					{
						int curGrowth = plugin.getConfig().getInt("MELON_BLOCK.Growth");
						int curDeath = plugin.getConfig().getInt("MELON_BLOCK.Death");
						
						if (plugin.getConfig().isSet("MELON_BLOCK."+curBiome+".Growth")) 
						{
							curGrowth = plugin.getConfig().getInt("MELON_BLOCK."+curBiome+".Growth");
						}
						
						if (plugin.getConfig().isSet("MELON_BLOCK."+curBiome+".Death")) 
						{
							curDeath = plugin.getConfig().getInt("MELON_BLOCK."+curBiome+".Death");
						}
						
						if (!(PwnPlantGrowth.random(curGrowth))) 
						{
							e.setCancelled(true);
							toLog += " Failed (Rate: "+curGrowth+") ";
							
							if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller)) 
							{
								toLog += " Has Weed Killer. ";
							}
							else 
							{
								toLog += " No Weed Killer. ";
								if (PwnPlantGrowth.random(curDeath)) 
								{
									e.getBlock().setType(Material.LONG_GRASS);
									toLog += " Died (Rate: "+curDeath+")";
								}
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
			
			// Specially handle Pumpkin Block
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) 
			{
				toLog += "PUMPKIN_BLOCK";
				if ((plugin.getConfig().getList("PUMPKIN_BLOCK.Biome").contains(curBiome)) || (plugin.getConfig().getList("PUMPKIN_BLOCK.Biome").isEmpty())) 
				{				
	
					if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
					{
						toLog += " has Fertilizer.";
					}
					else 
					{
						int curGrowth = plugin.getConfig().getInt("PUMPKIN_BLOCK.Growth");
						int curDeath = plugin.getConfig().getInt("PUMPKIN_BLOCK.Death");
						
						if (plugin.getConfig().isSet("PUMPKIN_BLOCK."+curBiome+".Growth")) 
						{
							curGrowth = plugin.getConfig().getInt("PUMPKIN_BLOCK."+curBiome+".Growth");
						}
						
						if (plugin.getConfig().isSet("PUMPKIN_BLOCK."+curBiome+".Death")) 
						{
							curDeath = plugin.getConfig().getInt("PUMPKIN_BLOCK."+curBiome+".Death");
						}
						
						if (!(PwnPlantGrowth.random(curGrowth))) 
						{
							e.setCancelled(true);
							toLog += " Failed (Rate: "+curGrowth+") ";
							
							if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller))
							{
								toLog += " Has Weed Killer. ";
							}
							else 
							{
								toLog += " No Weed Killer. ";
								if (PwnPlantGrowth.random(curDeath)) 
								{
									e.getBlock().setType(Material.LONG_GRASS);
									toLog += " Died (Rate: "+curDeath+")";
								}
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

		// Log it
    	if (PwnPlantGrowth.logEnabled) 
    	{	
    		PwnPlantGrowth.logToFile(toLog);
    	}	
	}
	
	// Structure Growth eg: trees
	@EventHandler(ignoreCancelled = true)
	public void structureGrow(StructureGrowEvent e) 
	{
	
		// Enabled in world?
		World world = e.getLocation().getWorld();
		if (!PwnPlantGrowth.isEnabledIn(world.getName())) 
		{
			return;
		}

		List<String> fBlocksFound = new ArrayList<String>();
		List<String> wkBlocksFound = new ArrayList<String>();
		
		if (PwnPlantGrowth.fenabled) 
		{
			for (int x = -(PwnPlantGrowth.fradius); x <= PwnPlantGrowth.fradius; x++) 
			{
	            for (int y = -(PwnPlantGrowth.fradius); y <= PwnPlantGrowth.fradius; y++) 
	            {
	               for (int z = -(PwnPlantGrowth.fradius); z <= PwnPlantGrowth.fradius; z++) 
	               {
	            	   fBlocksFound.add(e.getLocation().getBlock().getRelative(x, y, z).getType().toString());
	               }
	            }
	        }
		}		
		
		if (PwnPlantGrowth.wkenabled)
		{
			for (int xx = -(PwnPlantGrowth.wkradius); xx <= PwnPlantGrowth.wkradius; xx++) 
			{
	            for (int yy = -(PwnPlantGrowth.wkradius); yy <= PwnPlantGrowth.wkradius; yy++) 
	            {
	               for (int zz = -(PwnPlantGrowth.wkradius); zz <= PwnPlantGrowth.wkradius; zz++) 
	               {
	            	   wkBlocksFound.add(e.getLocation().getBlock().getRelative(xx, yy, zz).getType().toString());
	               }
	            }
	        }
		}		
		
		String coords = e.getLocation().toString();
				
		// Light level acceptable?
		int lightLevel = e.getLocation().getBlock().getLightFromSky();
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(e.getSpecies().toString())))
		{
	    	if (PwnPlantGrowth.logEnabled) 
	    	{	
	    		PwnPlantGrowth.logToFile(coords + ": Too dark to grow " + e.getSpecies());
	    	}			
			e.setCancelled(true);
			return;
		}
		
		String toLog = coords + ": Growing: " +e.getSpecies();

		// Get current biome and make a string for comparison later
		String curBiome = e.getLocation().getBlock().getBiome().toString();
		
		// Get current block type and make a string for comparison later
		String curBlock = e.getSpecies().toString();

		if ((plugin.getConfig().getList(curBlock+".Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList(curBlock+".Biome").isEmpty())) 
		{	
			
			if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
			{
				toLog += " has Fertilizer.";
			}
			else 
			{
				int curGrowth = plugin.getConfig().getInt(curBlock+".Growth");
				int curDeath = plugin.getConfig().getInt(curBlock+".Death");
				
				if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Growth")) 
				{
					curGrowth = plugin.getConfig().getInt(curBlock+"."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet(curBlock+"."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt(curBlock+"."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth)))
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					
					if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller))
					{
						toLog += " Has Weed Killer. ";
					}
					else 
					{
						toLog += " No Weed Killer. ";
						if (PwnPlantGrowth.random(curDeath)) 
						{
							e.getLocation().getBlock().setType(Material.LONG_GRASS);
							toLog += " Died (Rate: "+curDeath+")";
						}
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