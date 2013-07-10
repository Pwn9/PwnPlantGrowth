package com.pwn9.PwnPlantGrowth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.util.Vector;

import org.bukkit.material.Dispenser;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

public class PwnPlantGrowth extends JavaPlugin implements Listener {
	
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	
	static Random randomNumberGenerator = new Random();
	
	// data store for chance ints
	public int CarrotChance;
	public int CarrotDeath;
	public int CocoaChance;
	public int CocoaDeath;
	public int WheatChance;
	public int WheatDeath;
	public int MelonStemChance;
	public int MelonStemDeath;
	public int NetherWartChance;
	public int NetherWartDeath;
	public int PotatoChance;
	public int PotatoDeath;
	public int PumpkinStemChance;
	public int PumpkinStemDeath;
	public int CactusChance;
	public int CactusDeath;
	public int CaneChance;
	public int CaneDeath;
	public int MelonChance;
	public int MelonDeath;
	public int PumpkinChance;
	public int PumpkinDeath;
	public int BigTreeChance;
	public int BigTreeDeath;
	public int BirchChance;
	public int BirchDeath;
	public int BrownShroomChance;
	public int BrownShroomDeath;
	public int RedShroomChance;
	public int RedShroomDeath;
	public int JungleChance;
	public int JungleDeath;
	public int SpruceChance;
	public int SpruceDeath;
	public int TreeChance;
	public int TreeDeath;
	
	public void onEnable() {
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
		
		// Config Growth imports
		this.CarrotChance = getConfig().getInt("Carrot.Growth");
		this.CocoaChance = getConfig().getInt("Cocoa.Growth");
		this.WheatChance = getConfig().getInt("Wheat.Growth");
		this.MelonStemChance = getConfig().getInt("Melon_Stem.Growth");
		this.NetherWartChance = getConfig().getInt("Nether_Wart.Growth");
		this.PotatoChance = getConfig().getInt("Potato.Growth");
		this.PumpkinStemChance = getConfig().getInt("Pumpkin_Stem.Growth");
		this.CactusChance = getConfig().getInt("Cactus.Growth");
		this.CaneChance = getConfig().getInt("Sugar_Cane.Growth");
		this.MelonChance = getConfig().getInt("Melon_Block.Growth");
		this.PumpkinChance = getConfig().getInt("Pumpkin_Block.Growth");
		this.BigTreeChance = getConfig().getInt("Big_Tree.Growth");
		this.BirchChance = getConfig().getInt("Birch.Growth");
		this.BrownShroomChance = getConfig().getInt("Brown_Mushroom.Growth");
		this.RedShroomChance = getConfig().getInt("Red_Mushroom.Growth");
		this.JungleChance = getConfig().getInt("Jungle.Growth");
		this.SpruceChance = getConfig().getInt("Spruce.Growth");
		this.TreeChance = getConfig().getInt("Tree.Growth");
		
		//Config Death imports
		this.CarrotDeath = getConfig().getInt("Carrot.Death");
		this.CocoaDeath = getConfig().getInt("Cocoa.Death");
		this.WheatDeath = getConfig().getInt("Wheat.Death");
		this.MelonStemDeath = getConfig().getInt("Melon_Stem.Death");
		this.NetherWartDeath = getConfig().getInt("Nether_Wart.Death");
		this.PotatoDeath = getConfig().getInt("Potato.Death");
		this.PumpkinStemDeath = getConfig().getInt("Pumpkin_Stem.Death");
		this.CactusDeath = getConfig().getInt("Cactus.Death");
		this.CaneDeath = getConfig().getInt("Sugar_Cane.Death");
		this.MelonDeath = getConfig().getInt("Melon_Block.Death");
		this.PumpkinDeath = getConfig().getInt("Pumpkin_Block.Death");
		this.BigTreeDeath = getConfig().getInt("Big_Tree.Death");
		this.BirchDeath = getConfig().getInt("Birch.Death");
		this.BrownShroomDeath = getConfig().getInt("Brown_Mushroom.Death");
		this.RedShroomDeath = getConfig().getInt("Red_Mushroom.Death");
		this.JungleDeath = getConfig().getInt("Jungle.Death");
		this.SpruceDeath = getConfig().getInt("Spruce.Death");
		this.TreeDeath = getConfig().getInt("Tree.Death");			
	}
		
	public void onDisable() {
		
	}
	
	/*@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent breakEvent) {
		if(breakEvent.getBlock().getType() == Material.NETHER_WARTS) {
			breakEvent.setCancelled(true);
			breakEvent.getBlock().setType(Material.AIR);
			breakEvent.getBlock().getWorld().dropItemNaturally(breakEvent.getBlock().getLocation(), new ItemStack(Material.NETHER_STALK, randomNumberGenerator.nextInt(2)+1));
		}
	}
	*/
	
	// water buckets
	@EventHandler(ignoreCancelled = true)
	public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event)
	{
		Player player = event.getPlayer();
    	String noSourceWater = getConfig().getString("block_water_bucket");
    	if (noSourceWater == "true") {	
    		if(player.getItemInHand().getType() == Material.WATER_BUCKET) {   			
    			Block block = event.getBlockClicked().getRelative(event.getBlockFace());
    			EvaporateWaterTask task = new EvaporateWaterTask(block);
    			getServer().getScheduler().scheduleSyncDelayedTask(this, task, 30L);		
    		}
    	}
	}
	
	//when a dispenser dispenses...
	@EventHandler(ignoreCancelled = true)
	public void onBlockDispense(BlockDispenseEvent event)
	{
		//only care about water
		if(event.getItem().getType() == Material.WATER_BUCKET)
		{
			Block dispenser = event.getBlock();
			// Get location of dispenser 
			Location loc = dispenser.getLocation();
			// Get direction dispenser is facing 
			MaterialData mat = dispenser.getState().getData(); 
			Dispenser disp_mat = (Dispenser) mat; 
			BlockFace face = disp_mat.getFacing(); 			
			Block block;
			if(face.toString() == "WEST") 
			{
				block = loc.add(-1, 0, 0).getBlock();			
			}
			else if(face.toString() == "EAST") 
			{
				block = loc.add(1, 0, 0).getBlock();
			}
			else if(face.toString() == "NORTH") 
			{
				block = loc.add(0, 0, -1).getBlock();
			}
			else
			{
				block = loc.add(0, 0, 1).getBlock();
			}			
			EvaporateWaterTask task = new EvaporateWaterTask(block);
			getServer().getScheduler().scheduleSyncDelayedTask(this, task, 45L);
		}	
	}
	
	// Listen for plant growth
	@EventHandler(ignoreCancelled = true)
	public void plantGrow(BlockGrowEvent e) {
		String toLog = "";
		if (e.getBlock().getType() == Material.AIR){
			toLog = "Growing: ";
		}
		else {
			toLog = "Growing: " +e.getBlock().getType();
		}

		if (e.getBlock().getType() == Material.CARROT){
			if ((getConfig().getList("Carrot.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Carrot.Biome").isEmpty())) {		
				if (!(PwnPlantGrowth.random(CarrotChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(CarrotDeath)) {
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getBlock().getType() == Material.COCOA){
			if ((getConfig().getList("Cocoa.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Cocoa.Biome").isEmpty())) {		
				if (!(PwnPlantGrowth.random(CocoaChance))) {	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(CocoaDeath)) {
						e.getBlock().setType(Material.VINE);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}	
		if (e.getBlock().getType() == Material.CROPS){
			if ((getConfig().getList("Wheat.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Wheat.Biome").isEmpty())) {		
				if (!(PwnPlantGrowth.random(WheatChance))) {	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(WheatDeath)) {
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}			
		if (e.getBlock().getType() == Material.MELON_STEM){
			if ((getConfig().getList("Melon_Stem.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Melon_Stem.Biome").isEmpty())) {		
				if (!(PwnPlantGrowth.random(MelonStemChance))) {	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(MelonStemDeath)) {
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}			
		if (e.getBlock().getType() == Material.NETHER_WARTS){
			if ((getConfig().getList("Nether_Wart.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Nether_Wart.Biome").isEmpty())) {		
				if (!(PwnPlantGrowth.random(NetherWartChance))) {	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(NetherWartDeath)) {
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}	
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}
		}	
		if (e.getBlock().getType() == Material.POTATO){
			if ((getConfig().getList("Potato.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Potato.Biome").isEmpty())) {		
				if (!(PwnPlantGrowth.random(PotatoChance))) {	
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PotatoDeath)) {
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}			
		if (e.getBlock().getType() == Material.PUMPKIN_STEM){
			if ((getConfig().getList("Pumpkin_Stem.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Pumpkin_Stem.Biome").isEmpty())) {		
				if (!(PwnPlantGrowth.random(PumpkinStemChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(PumpkinStemDeath)) {
						e.getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}	
		// AIR BLOCKS
		if (e.getBlock().getType() == Material.AIR){
			if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.CACTUS) {
				toLog += "CACTUS";
				if ((getConfig().getList("Cactus.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Cactus.Biome").isEmpty())) {		
					if (!(PwnPlantGrowth.random(CactusChance))) {
						e.setCancelled(true);
						toLog += " Failed";
						if (PwnPlantGrowth.random(CactusDeath)) {
							e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
							toLog += " and Died";
						}
					}
				}
				else {
					e.setCancelled(true);
					toLog += " Failed: Bad Biome";				
				}					
			}
			else if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) {
				toLog += "CANE";
				if ((getConfig().getList("Sugar_Cane.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Sugar_Cane.Biome").isEmpty())) {
					if (!(PwnPlantGrowth.random(CaneChance))) {
						e.setCancelled(true);
						toLog += " Failed";
						if (PwnPlantGrowth.random(CaneDeath)) {
							e.getBlock().getRelative(BlockFace.DOWN).setType(Material.LONG_GRASS);
							toLog += " and Died";
						}
					}
				}
				else {
					e.setCancelled(true);
					toLog += " Failed: Bad Biome";				
				}					
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.MELON_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.MELON_STEM)) {
				toLog += "MELON_BLOCK";
				if ((getConfig().getList("Melon_Block.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Melon_Block.Biome").isEmpty())) {				
					if (!(PwnPlantGrowth.random(MelonChance))) {
						e.setCancelled(true);
						toLog += " Failed";
					}
				}
				else {
					e.setCancelled(true);
					toLog += " Failed: Bad Biome";				
				}					
			}
			else if ((e.getBlock().getRelative(BlockFace.EAST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.PUMPKIN_STEM) ||
					(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.PUMPKIN_STEM)) {
				toLog += "PUMPKIN_BLOCK";
				if ((getConfig().getList("Pumpkin_Block.Biome").contains(e.getBlock().getBiome().toString())) || (getConfig().getList("Pumpkin_Block.Biome").isEmpty())) {				
					if (!(PwnPlantGrowth.random(PumpkinChance))) {
						e.setCancelled(true);
						toLog += " Failed";
					}
				}
				else {
					e.setCancelled(true);
					toLog += " Failed: Bad Biome";				
				}					
			}
		}	
    	String logEnabled = getConfig().getString("debug_log");
    	if (logEnabled == "true") {	
    		logToFile(toLog);
    	}
	}

	@EventHandler(ignoreCancelled = true)
	public void structureGrow(StructureGrowEvent e) {	
		//System.out.println("Structure " +e.getSpecies().toString());	
		
		String toLog = "Growing: " +e.getSpecies();

		if (e.getSpecies() == TreeType.BIG_TREE){
			if ((getConfig().getList("Big_Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Big_Tree.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(BigTreeChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(BigTreeDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.BIRCH){
			if ((getConfig().getList("Birch.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Birch.Biome").isEmpty())) {				
				if (!(PwnPlantGrowth.random(BirchChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(BirchDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.BROWN_MUSHROOM){
			if ((getConfig().getList("Brown_Mushroom.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Brown_Mushroom.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(BrownShroomChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(BrownShroomDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}
		}
		if (e.getSpecies() == TreeType.JUNGLE){
			if ((getConfig().getList("Jungle.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Jungle.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(JungleChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(JungleDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.JUNGLE_BUSH){
			if ((getConfig().getList("Jungle.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Jungle.Biome").isEmpty())) {				
				if (!(PwnPlantGrowth.random(JungleChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(JungleDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.RED_MUSHROOM){
			if ((getConfig().getList("Red_Mushroom.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Red_Mushroom.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(RedShroomChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(RedShroomDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.REDWOOD){
			if ((getConfig().getList("Spruce.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Spruce.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(SpruceChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(SpruceDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.SMALL_JUNGLE){
			if ((getConfig().getList("Jungle.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Jungle.Biome").isEmpty())) {				
				if (!(PwnPlantGrowth.random(JungleChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(JungleDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.SWAMP){
			if ((getConfig().getList("Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Tree.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(TreeChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(TreeDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.TALL_REDWOOD){
			if ((getConfig().getList("Spruce.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Spruce.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(SpruceChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(SpruceDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
		if (e.getSpecies() == TreeType.TREE){
			if ((getConfig().getList("Tree.Biome").contains(e.getLocation().getBlock().getBiome().toString())) || (getConfig().getList("Tree.Biome").isEmpty())) {			
				if (!(PwnPlantGrowth.random(TreeChance))) {
					e.setCancelled(true);
					toLog += " Failed";
					if (PwnPlantGrowth.random(TreeDeath)) {
						e.getLocation().getBlock().setType(Material.LONG_GRASS);
						toLog += " and Died";
					}
				}
			}
			else {
				e.setCancelled(true);
				toLog += " Failed: Bad Biome";				
			}				
		}
			
    	String logEnabled = getConfig().getString("debug_log");
    	if (logEnabled == "true") {	
    		logToFile(toLog);
    	}
	}
	
	static boolean random(int percentChance) {
			return randomNumberGenerator.nextInt(100) < percentChance;
	}

    public void logToFile(String message) {   
	    	try {
			    File dataFolder = getDataFolder();
			    if(!dataFolder.exists()) {
			    	dataFolder.mkdir();
			    }
			     
			    File saveTo = new File(getDataFolder(), "pwnplantgrowth.log");
			    if (!saveTo.exists())  {
			    	saveTo.createNewFile();
			    }
			    
			    FileWriter fw = new FileWriter(saveTo, true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(getDate() +" "+ message);
			    pw.flush();
			    pw.close();
		    } 
		    catch (IOException e) {
		    	e.printStackTrace();
		    }
    }
    
    public String getDate() {
    	  String s;
    	  Format formatter;
    	  Date date = new Date(); 
    	  formatter = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
    	  s = formatter.format(date);
    	  return s;
    }	
}