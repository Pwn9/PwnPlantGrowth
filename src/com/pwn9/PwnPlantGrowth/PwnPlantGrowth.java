package com.pwn9.PwnPlantGrowth;

import java.util.Random;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;

public class PwnPlantGrowth extends JavaPlugin implements Listener {
	
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	
	static Random randomNumberGenerator = new Random();
	
	public void onEnable() {
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(this, this);

	}
	
	public void onDisable() {
		
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent breakEvent) {
		if(breakEvent.getBlock().getType() == Material.NETHER_WARTS) {
			if (PwnPlantGrowth.random(50)) {
				//breakEvent.getBlock().getDrops().clear();
				breakEvent.setCancelled(true);
				breakEvent.getBlock().setType(Material.AIR);
				
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void plantGrow(BlockGrowEvent e) {
		//System.out.println("Growing "+ e.getBlock().getType().toString());
		if (e.getBlock().getType() == Material.CARROT){
			
			
	    	int Chance = getConfig().getInt("CARROT");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}			
		}
		// not yet supported by bukkit but maybe soon
		if (e.getBlock().getType() == Material.COCOA){
	    	int Chance = getConfig().getInt("COCOA");
			if (PwnPlantGrowth.random(Chance)) {	
					e.setCancelled(true);
			}			
		}	
		if (e.getBlock().getType() == Material.CROPS){
	    	int Chance = getConfig().getInt("WHEAT");
			if (PwnPlantGrowth.random(Chance)) {	
				e.setCancelled(true);
			}			
		}			
		if (e.getBlock().getType() == Material.MELON_STEM){
	    	int Chance = getConfig().getInt("MELON_STEM");
			if (PwnPlantGrowth.random(Chance)) {	
				e.setCancelled(true);
			}			
		}			
		if (e.getBlock().getType() == Material.NETHER_WARTS){
	    	int Chance = getConfig().getInt("NETHERWART");
			if (PwnPlantGrowth.random(Chance)) {	
				e.setCancelled(true);
			}			
		}	
		if (e.getBlock().getType() == Material.POTATO){
	    	int Chance = getConfig().getInt("POTATO");
			if (PwnPlantGrowth.random(Chance)) {	
				e.setCancelled(true);
			}			
		}			
		if (e.getBlock().getType() == Material.PUMPKIN_STEM){
	    	int Chance = getConfig().getInt("PUMPKIN_STEM");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}			
		}	
		if (e.getBlock().getType() == Material.AIR){
			if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.CACTUS) {
		    	int Chance = getConfig().getInt("CACTUS");
				if (PwnPlantGrowth.random(Chance)) {
					e.setCancelled(true);
				}				
			}
			else if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) {
		    	int Chance = getConfig().getInt("SUGAR_CANE");
				if (PwnPlantGrowth.random(Chance)) {
					e.setCancelled(true);
				}				
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.MELON_STEM)) {
		    	int Chance = getConfig().getInt("MELON");
				if (PwnPlantGrowth.random(Chance)) {
					e.setCancelled(true);
				}				
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) {
		    	int Chance = getConfig().getInt("PUMPKIN");
				if (PwnPlantGrowth.random(Chance)) {
					e.setCancelled(true);
				}				
			}			
		}			
	}

	@EventHandler
	public void structureGrow(StructureGrowEvent e) {	
		//System.out.println("Structure " +e.getSpecies().toString());	
		if (e.getSpecies() == TreeType.BIG_TREE){
	    	int Chance = getConfig().getInt("BIG_TREE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.BIRCH){
	    	int Chance = getConfig().getInt("BIRCH");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.BROWN_MUSHROOM){
	    	int Chance = getConfig().getInt("BROWN_MUSHROOM");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.JUNGLE){
	    	int Chance = getConfig().getInt("JUNGLE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.JUNGLE_BUSH){
	    	int Chance = getConfig().getInt("JUNGLE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.RED_MUSHROOM){
	    	int Chance = getConfig().getInt("RED_MUSHROOM");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.REDWOOD){
	    	int Chance = getConfig().getInt("SPRUCE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.SMALL_JUNGLE){
	    	int Chance = getConfig().getInt("JUNGLE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.SWAMP){
	    	int Chance = getConfig().getInt("TREE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.TALL_REDWOOD){
	    	int Chance = getConfig().getInt("SPRUCE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.TREE){
	    	int Chance = getConfig().getInt("TREE");
			if (PwnPlantGrowth.random(Chance)) {
				e.setCancelled(true);
			}
		}		
	}
	
	static boolean random(int percentChance) {
			return randomNumberGenerator.nextInt(100) < percentChance;
	}
	
}