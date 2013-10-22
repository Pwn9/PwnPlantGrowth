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
			
		// Get current biome and make a string for comparison later
		String curBiome = e.getBlock().getBiome().toString();
		
		// Get current block type and make a string for comparison later
		String curBlock = e.getBlock().getType().toString();
		
		// Regular growth blocks
		
		
		
		// Carrot
		if (curBlock == "CARROT")
		{
			if ((plugin.getConfig().getList("Carrot.Biome").contains(curBiome)) || (plugin.getConfig().getList("Carrot.Biome").isEmpty())) 
			{
				
				int curGrowth = PwnPlantGrowth.CarrotChance;
				int curDeath = PwnPlantGrowth.CarrotDeath;
				
				if (plugin.getConfig().isSet("Carrot."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Carrot."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Carrot."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Carrot."+curBiome+".Death");
				}
									
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}		
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		
		if (curBlock == "COCOA")
		{
			if ((plugin.getConfig().getList("Cocoa.Biome").contains(curBiome)) || (plugin.getConfig().getList("Cocoa.Biome").isEmpty())) 
			{		
				int curGrowth = PwnPlantGrowth.CocoaChance;
				int curDeath = PwnPlantGrowth.CocoaDeath;
				
				if (plugin.getConfig().isSet("Cocoa."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Cocoa."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Cocoa."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Cocoa."+curBiome+".Death");
				}
							
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}	
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}	
		
		
		
		if (curBlock == "CROPS")
		{
			if ((plugin.getConfig().getList("Wheat.Biome").contains(curBiome)) || (plugin.getConfig().getList("Wheat.Biome").isEmpty())) 
			{		
				int curGrowth = PwnPlantGrowth.WheatChance;
				int curDeath = PwnPlantGrowth.WheatDeath;
				
				if (plugin.getConfig().isSet("Wheat."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Wheat."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Wheat."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Wheat."+curBiome+".Death");
				}
							
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}	
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}	
		
		if (curBlock == "MELON_STEM")
		{
			if ((plugin.getConfig().getList("Melon_Stem.Biome").contains(curBiome)) || (plugin.getConfig().getList("Melon_Stem.Biome").isEmpty())) 
			{		
				int curGrowth = PwnPlantGrowth.MelonStemChance;
				int curDeath = PwnPlantGrowth.MelonStemDeath;
				
				if (plugin.getConfig().isSet("Melon_Stem."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Melon_Stem."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Melon_Stem."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Melon_Stem."+curBiome+".Death");
				}
							
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}	
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}	
		
		if (curBlock == "NETHER_WARTS")
		{
			if ((plugin.getConfig().getList("Nether_Wart.Biome").contains(curBiome)) || (plugin.getConfig().getList("Nether_Wart.Biome").isEmpty())) 
			{		
				int curGrowth = PwnPlantGrowth.NetherWartChance;
				int curDeath = PwnPlantGrowth.NetherWartDeath;
				
				if (plugin.getConfig().isSet("Nether_Wart."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Nether_Wart."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Nether_Wart."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Nether_Wart."+curBiome+".Death");
				}
							
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}	
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}
		}	
		
		if (curBlock == "POTATO")
		{
			if ((plugin.getConfig().getList("Potato.Biome").contains(curBiome)) || (plugin.getConfig().getList("Potato.Biome").isEmpty())) 
			{		
				int curGrowth = PwnPlantGrowth.PotatoChance;
				int curDeath = PwnPlantGrowth.PotatoDeath;
				
				if (plugin.getConfig().isSet("Potato."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Potato."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Potato."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Potato."+curBiome+".Death");
				}
							
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}	
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}	
		
		if (curBlock == "PUMPKIN_STEM")
		{
			if ((plugin.getConfig().getList("Pumpkin_Stem.Biome").contains(curBiome)) || (plugin.getConfig().getList("Pumpkin_Stem.Biome").isEmpty())) 
			{		
				int curGrowth = PwnPlantGrowth.PumpkinStemChance;
				int curDeath = PwnPlantGrowth.PumpkinStemDeath;
				
				if (plugin.getConfig().isSet("Pumpkin_Stem."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Pumpkin_Stem."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Pumpkin_Stem."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Pumpkin_Stem."+curBiome+".Death");
				}
							
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
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
			
			if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.CACTUS) 
			{
				toLog += "CACTUS";
				if ((plugin.getConfig().getList("Cactus.Biome").contains(curBiome)) || (plugin.getConfig().getList("Cactus.Biome").isEmpty()))
				{
					
					int curGrowth = PwnPlantGrowth.CactusChance;
					int curDeath = PwnPlantGrowth.CactusDeath;
					
					if (plugin.getConfig().isSet("Cactus."+curBiome+".Growth")) {
						curGrowth = plugin.getConfig().getInt("Cactus."+curBiome+".Growth");
					}
					
					if (plugin.getConfig().isSet("Cactus."+curBiome+".Death")) {
						curDeath = plugin.getConfig().getInt("Cactus."+curBiome+".Death");
					}
					
					if (!(PwnPlantGrowth.random(curGrowth))) 
					{
						e.setCancelled(true);
						toLog += " Failed (Rate: "+curGrowth+") ";
						if (PwnPlantGrowth.random(curDeath)) 
						{
							e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
							toLog += " and Died (Rate: "+curDeath+")";
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
				if ((plugin.getConfig().getList("Sugar_Cane.Biome").contains(curBiome)) || (plugin.getConfig().getList("Sugar_Cane.Biome").isEmpty())) 
				{
					
					int curGrowth = PwnPlantGrowth.CaneChance;
					int curDeath = PwnPlantGrowth.CaneDeath;
					
					if (plugin.getConfig().isSet("Sugar_Cane."+curBiome+".Growth")) {
						curGrowth = plugin.getConfig().getInt("Sugar_Cane."+curBiome+".Growth");
					}
					
					if (plugin.getConfig().isSet("Sugar_Cane."+curBiome+".Death")) {
						curDeath = plugin.getConfig().getInt("Sugar_Cane."+curBiome+".Death");
					}
					
					if (!(PwnPlantGrowth.random(curGrowth))) 
					{
						e.setCancelled(true);
						toLog += " Failed (Rate: "+curGrowth+") ";
						if (PwnPlantGrowth.random(curDeath)) 
						{
							e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
							toLog += " and Died (Rate: "+curDeath+")";
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
					
					int curGrowth = PwnPlantGrowth.MelonChance;
					int curDeath = PwnPlantGrowth.MelonDeath;
					
					if (plugin.getConfig().isSet("Melon_Block."+curBiome+".Growth")) {
						curGrowth = plugin.getConfig().getInt("Melon_Block."+curBiome+".Growth");
					}
					
					if (plugin.getConfig().isSet("Melon_Block."+curBiome+".Death")) {
						curDeath = plugin.getConfig().getInt("Melon_Block."+curBiome+".Death");
					}
					
					if (!(PwnPlantGrowth.random(curGrowth))) 
					{
						e.setCancelled(true);
						toLog += " Failed (Rate: "+curGrowth+") ";
						if (PwnPlantGrowth.random(curDeath)) 
						{
							e.getBlock().setType(Material.LONG_GRASS);
							toLog += " and Died (Rate: "+curDeath+")";
						}
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
				if ((plugin.getConfig().getList("Pumpkin_Block.Biome").contains(curBiome)) || (plugin.getConfig().getList("Pumpkin_Block.Biome").isEmpty())) 
				{				
					
					int curGrowth = PwnPlantGrowth.PumpkinChance;
					int curDeath = PwnPlantGrowth.PumpkinDeath;
					
					if (plugin.getConfig().isSet("Pumpkin_Block."+curBiome+".Growth")) {
						curGrowth = plugin.getConfig().getInt("Pumpkin_Block."+curBiome+".Growth");
					}
					
					if (plugin.getConfig().isSet("Pumpkin_Block."+curBiome+".Death")) {
						curDeath = plugin.getConfig().getInt("Pumpkin_Block."+curBiome+".Death");
					}
					
					if (!(PwnPlantGrowth.random(curGrowth))) 
					{
						e.setCancelled(true);
						toLog += " Failed (Rate: "+curGrowth+") ";
						if (PwnPlantGrowth.random(curDeath)) 
						{
							e.getBlock().setType(Material.LONG_GRASS);
							toLog += " and Died (Rate: "+curDeath+")";
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

		// Get current biome and make a string for comparison later
		String curBiome = e.getLocation().getBlock().getBiome().toString();
		
		// Get current block type and make a string for comparison later
		//String curBlock = e.getLocation().getBlock().getType().toString();
		
		if (e.getSpecies() == TreeType.BIG_TREE)
		{
			if ((plugin.getConfig().getList("Big_Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Big_Tree.Biome").isEmpty())) 
			{	
				
				int curGrowth = PwnPlantGrowth.BigTreeChance;
				int curDeath = PwnPlantGrowth.BigTreeDeath;
				
				if (plugin.getConfig().isSet("Big_Tree."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Big_Tree."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Big_Tree."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Big_Tree."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
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
				int curGrowth = PwnPlantGrowth.BirchChance;
				int curDeath = PwnPlantGrowth.BirchDeath;
				
				if (plugin.getConfig().isSet("Birch."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Birch."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Birch."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Birch."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
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
				int curGrowth = PwnPlantGrowth.BrownShroomChance;
				int curDeath = PwnPlantGrowth.BrownShroomDeath;
				
				if (plugin.getConfig().isSet("Brown_Mushroom."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Brown_Mushroom."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Brown_Mushroom."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Brown_Mushroom."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}
		}
		
		
		if ((e.getSpecies() == TreeType.JUNGLE) || (e.getSpecies() == TreeType.SMALL_JUNGLE) || (e.getSpecies() == TreeType.JUNGLE_BUSH))
		{
			if ((plugin.getConfig().getList("Jungle.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Jungle.Biome").isEmpty())) 
			{			
				int curGrowth = PwnPlantGrowth.JungleChance;
				int curDeath = PwnPlantGrowth.JungleDeath;
				
				if (plugin.getConfig().isSet("Jungle."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Jungle."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Jungle."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Jungle."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
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
				int curGrowth = PwnPlantGrowth.RedShroomChance;
				int curDeath = PwnPlantGrowth.RedShroomDeath;
				
				if (plugin.getConfig().isSet("Red_Mushroom."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Red_Mushroom."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Red_Mushroom."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Red_Mushroom."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		
		
		if ((e.getSpecies() == TreeType.REDWOOD) || (e.getSpecies() == TreeType.TALL_REDWOOD))
		{
			if ((plugin.getConfig().getList("Spruce.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Spruce.Biome").isEmpty())) 
			{			
				int curGrowth = PwnPlantGrowth.SpruceChance;
				int curDeath = PwnPlantGrowth.SpruceDeath;
				
				if (plugin.getConfig().isSet("Spruce."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Spruce."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Spruce."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Spruce."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
					}
				}
			}
			else 
			{
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		
		if ((e.getSpecies() == TreeType.SWAMP) || (e.getSpecies() == TreeType.TREE))
		{
			if ((plugin.getConfig().getList("Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (plugin.getConfig().getList("Tree.Biome").isEmpty())) 
			{			
				int curGrowth = PwnPlantGrowth.TreeChance;
				int curDeath = PwnPlantGrowth.TreeDeath;
				
				if (plugin.getConfig().isSet("Tree."+curBiome+".Growth")) {
					curGrowth = plugin.getConfig().getInt("Tree."+curBiome+".Growth");
				}
				
				if (plugin.getConfig().isSet("Tree."+curBiome+".Death")) {
					curDeath = plugin.getConfig().getInt("Tree."+curBiome+".Death");
				}
				
				if (!(PwnPlantGrowth.random(curGrowth))) 
				{
					e.setCancelled(true);
					toLog += " Failed (Rate: "+curGrowth+") ";
					if (PwnPlantGrowth.random(curDeath)) 
					{
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died (Rate: "+curDeath+")";
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