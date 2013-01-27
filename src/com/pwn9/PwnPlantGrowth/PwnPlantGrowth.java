package com.pwn9.PwnPlantGrowth;

import java.util.Random;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;

import org.bukkit.plugin.java.JavaPlugin;

public class PwnPlantGrowth extends JavaPlugin implements Listener {
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	
	public void onEnable() {
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		
	}
	
	@EventHandler
	public void plantGrow(BlockGrowEvent e) {
		//System.out.println("Growing "+ e.getBlock().getType().toString());
		if (e.getBlock().getType() == Material.CARROT){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("CARROT");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}			
		}
		// not yet supported by bukkit but maybe soon
		if (e.getBlock().getType() == Material.COCOA){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("COCOA");
			if (randomInt > Integer.parseInt(Chance)) {	
					e.setCancelled(true);
			}			
		}	
		if (e.getBlock().getType() == Material.CROPS){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("WHEAT");
			if (randomInt > Integer.parseInt(Chance)) {	
				e.setCancelled(true);
			}			
		}			
		if (e.getBlock().getType() == Material.MELON_STEM){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("MELON_STEM");
			if (randomInt > Integer.parseInt(Chance)) {	
				e.setCancelled(true);
			}			
		}			
		if (e.getBlock().getType() == Material.NETHER_WARTS){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("NETHERWART");
			if (randomInt > Integer.parseInt(Chance)) {	
				e.setCancelled(true);
			}			
		}	
		if (e.getBlock().getType() == Material.POTATO){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("POTATO");
			if (randomInt > Integer.parseInt(Chance)) {	
				e.setCancelled(true);
			}			
		}			
		if (e.getBlock().getType() == Material.PUMPKIN_STEM){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("PUMPKIN_STEM");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}			
		}	
		if (e.getBlock().getType() == Material.AIR){
			if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.CACTUS) {
				Random random = new Random();
				int randomInt = random.nextInt(100);
		    	String Chance = getConfig().getString("CACTUS");
				if (randomInt > Integer.parseInt(Chance)) {
					e.setCancelled(true);
				}				
			}
			else if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) {
				Random random = new Random();
				int randomInt = random.nextInt(100);
		    	String Chance = getConfig().getString("SUGAR_CANE");
				if (randomInt > Integer.parseInt(Chance)) {
					e.setCancelled(true);
				}				
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.MELON_STEM)) {
				Random random = new Random();
				int randomInt = random.nextInt(100);
		    	String Chance = getConfig().getString("MELON");
				if (randomInt > Integer.parseInt(Chance)) {
					e.setCancelled(true);
				}				
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) {
				Random random = new Random();
				int randomInt = random.nextInt(100);
		    	String Chance = getConfig().getString("PUMPKIN");
				if (randomInt > Integer.parseInt(Chance)) {
					e.setCancelled(true);
				}				
			}			
		}			
	}

	@EventHandler
	public void structureGrow(StructureGrowEvent e) {	
		//System.out.println("Structure " +e.getSpecies().toString());	
		if (e.getSpecies() == TreeType.BIG_TREE){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("BIG_TREE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.BIRCH){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("BIRCH");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.BROWN_MUSHROOM){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("BROWN_MUSHROOM");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.JUNGLE){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("JUNGLE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.JUNGLE_BUSH){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("JUNGLE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.RED_MUSHROOM){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("RED_MUSHROOM");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.REDWOOD){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("SPRUCE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.SMALL_JUNGLE){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("JUNGLE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.SWAMP){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("TREE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.TALL_REDWOOD){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("SPRUCE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}
		if (e.getSpecies() == TreeType.TREE){
			Random random = new Random();
			int randomInt = random.nextInt(100);
	    	String Chance = getConfig().getString("TREE");
			if (randomInt > Integer.parseInt(Chance)) {
				e.setCancelled(true);
			}
		}		
	}
}