package com.pwn9.PwnPlantGrowth;

import org.bukkit.Material;
import org.bukkit.TreeType;
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
		
		// Light level acceptable?
		int lightLevel = e.getBlock().getLightFromSky();
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(e.getBlock().getType().toString())))
		{
	    	if (PwnPlantGrowth.logEnabled) 
	    	{	
	    		PwnPlantGrowth.logToFile("Too dark to grow " + e.getBlock().getType());
	    	}			
			e.setCancelled(true);
			return;
		}
		
		String toLog = "";
				
		if (e.getBlock().getType() == Material.AIR)
		{
			toLog += "Growing: ";
		}
		else 
		{
			toLog += "Growing: " +e.getBlock().getType();
		}
			

		// Regular growth blocks
		if (e.getBlock().getType() == Material.CARROT)
		{
			if ((plugin.getConfig().getList("Carrot.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Carrot.Biome").isEmpty())) 
			{		
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.CarrotChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.CarrotDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getBlock().getType() == Material.COCOA)
		{
			if ((plugin.getConfig().getList("Cocoa.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Cocoa.Biome").isEmpty())) 
			{		
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.CocoaChance))) 
				{	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.CocoaDeath)) 
					{
						e.getBlock().setType(Material.VINE);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}	
		if (e.getBlock().getType() == Material.CROPS)
		{
			if ((plugin.getConfig().getList("Wheat.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Wheat.Biome").isEmpty())) 
			{		
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.WheatChance))) 
				{	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.WheatDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}			
		if (e.getBlock().getType() == Material.MELON_STEM)
		{
			if ((plugin.getConfig().getList("Melon_Stem.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Melon_Stem.Biome").isEmpty())) 
			{		
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.MelonStemChance))) 
				{	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.MelonStemDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}			
		if (e.getBlock().getType() == Material.NETHER_WARTS)
		{
			if ((plugin.getConfig().getList("Nether_Wart.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Nether_Wart.Biome").isEmpty())) 
			{		
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.NetherWartChance))) 
				{	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.NetherWartDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}	
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}
		}	
		if (e.getBlock().getType() == Material.POTATO)
		{
			if ((plugin.getConfig().getList("Potato.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Potato.Biome").isEmpty())) 
			{		
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.PotatoChance))) 
				{	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.PotatoDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}			
		if (e.getBlock().getType() == Material.PUMPKIN_STEM)
		{
			if ((plugin.getConfig().getList("Pumpkin_Stem.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Pumpkin_Stem.Biome").isEmpty())) 
			{		
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.PumpkinStemChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.PumpkinStemDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
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
		if (e.getBlock().getType() == Material.AIR)
		{
			if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.CACTUS) 
			{
				toLog += "CACTUS";
				if ((plugin.getConfig().getList("Cactus.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Cactus.Biome").isEmpty()))
				{		
					if (!(PwnPlantGrowth.random(PwnPlantGrowth.CactusChance))) 
					{
						e.setCancelled(true);
						toLog += " Failed";
						if (PwnPlantGrowth.random(PwnPlantGrowth.CactusDeath)) 
						{
							e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
							toLog += " and Died";
						}
					}
				}
				else 
				{
					e.setCancelled(true);
					toLog += " Failed: Bad Biome";				
				}					
			}
			else if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) 
			{
				toLog += "CANE";
				if ((plugin.getConfig().getList("Sugar_Cane.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Sugar_Cane.Biome").isEmpty())) 
				{
					if (!(PwnPlantGrowth.random(PwnPlantGrowth.CaneChance))) 
					{
						e.setCancelled(true);
						toLog += " Failed";
						if (PwnPlantGrowth.random(PwnPlantGrowth.CaneDeath)) 
						{
							e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
							toLog += " and Died";
						}
					}
				}
				else 
				{
					e.setCancelled(true);
					toLog += " Failed: Bad Biome";				
				}					
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.MELON_STEM)) 
			{
				toLog += "MELON_BLOCK";
				if ((plugin.getConfig().getList("Melon_Block.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Melon_Block.Biome").isEmpty())) 
				{				
					if (!(PwnPlantGrowth.random(PwnPlantGrowth.MelonChance))) 
					{
						e.setCancelled(true);
						toLog += " Failed";
					}
				}
				else 
				{
					e.setCancelled(true);
					toLog += " Failed: Bad Biome";				
				}					
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) 
			{
				toLog += "PUMPKIN_BLOCK";
				if ((plugin.getConfig().getList("Pumpkin_Block.Biome").contains(e.getBlock().getBiome().toString())) || (plugin.getConfig().getList("Pumpkin_Block.Biome").isEmpty())) 
				{				
					if (!(PwnPlantGrowth.random(PwnPlantGrowth.PumpkinChance))) 
					{
						e.setCancelled(true);
						toLog += " Failed";
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
		
		// Light level acceptable?
		int lightLevel = e.getLocation().getBlock().getLightFromSky();
		if ((PwnPlantGrowth.naturalLight > lightLevel) && (!PwnPlantGrowth.canDarkGrow(e.getSpecies().toString())))
		{
	    	if (PwnPlantGrowth.logEnabled) 
	    	{	
	    		PwnPlantGrowth.logToFile("Too dark to grow " + e.getSpecies());
	    	}			
			e.setCancelled(true);
			return;
		}
		
		String toLog = "Growing: " +e.getSpecies();

		if (e.getSpecies() == TreeType.BIG_TREE)
		{
			if ((plugin.getConfig().getList("Big_Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Big_Tree.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.BigTreeChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.BigTreeDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.BIRCH)
		{
			if ((plugin.getConfig().getList("Birch.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Birch.Biome").isEmpty())) 
			{				
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.BirchChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.BirchDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.BROWN_MUSHROOM)
		{
			if ((plugin.getConfig().getList("Brown_Mushroom.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Brown_Mushroom.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.BrownShroomChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.BrownShroomDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}
		}
		if (e.getSpecies() == TreeType.JUNGLE)
		{
			if ((plugin.getConfig().getList("Jungle.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Jungle.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.JungleChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.JungleDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.JUNGLE_BUSH)
		{
			if ((plugin.getConfig().getList("Jungle.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Jungle.Biome").isEmpty())) 
			{				
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.JungleChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.JungleDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.RED_MUSHROOM)
		{
			if ((plugin.getConfig().getList("Red_Mushroom.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Red_Mushroom.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.RedShroomChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.RedShroomDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.REDWOOD)
		{
			if ((plugin.getConfig().getList("Spruce.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Spruce.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.SpruceChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.SpruceDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.SMALL_JUNGLE)
		{
			if ((plugin.getConfig().getList("Jungle.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Jungle.Biome").isEmpty())) 
			{				
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.JungleChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.JungleDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.SWAMP)
		{
			if ((plugin.getConfig().getList("Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Tree.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.TreeChance)))
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.TreeDeath))
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.TALL_REDWOOD)
		{
			if ((plugin.getConfig().getList("Spruce.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Spruce.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.SpruceChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.SpruceDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.TREE)
		{
			if ((plugin.getConfig().getList("Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Tree.Biome").isEmpty())) 
			{			
				if (!(PwnPlantGrowth.random(PwnPlantGrowth.TreeChance))) 
				{
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PwnPlantGrowth.TreeDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		
		// log it
		if (PwnPlantGrowth.logEnabled) 
		{	
			PwnPlantGrowth.logToFile(toLog);
		}
	}
	

}