package com.pwn9.PwnPlantGrowth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class PwnPlantGrowth extends JavaPlugin 
{
	
	// declare some stuffs to be used later
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	public static File dataFolder;
	public static Boolean logEnabled;
	public static Boolean blockWater;
	public static int naturalLight;
	public static List<String> darkGrow;
	public static List<String> enabledWorlds;
	static Random randomNumberGenerator = new Random();
	
	// data store for chance ints - this is a bit kludgy imho, we'll have to clean this up
	public static int CarrotChance;
	public static int CarrotDeath;
	public static int CocoaChance;
	public static int CocoaDeath;
	public static int WheatChance;
	public static int WheatDeath;
	public static int MelonStemChance;
	public static int MelonStemDeath;
	public static int NetherWartChance;
	public static int NetherWartDeath;
	public static int PotatoChance;
	public static int PotatoDeath;
	public static int PumpkinStemChance;
	public static int PumpkinStemDeath;
	public static int CactusChance;
	public static int CactusDeath;
	public static int CaneChance;
	public static int CaneDeath;
	public static int MelonChance;
	public static int MelonDeath;
	public static int PumpkinChance;
	public static int PumpkinDeath;
	public static int BigTreeChance;
	public static int BigTreeDeath;
	public static int BirchChance;
	public static int BirchDeath;
	public static int BrownShroomChance;
	public static int BrownShroomDeath;
	public static int RedShroomChance;
	public static int RedShroomDeath;
	public static int JungleChance;
	public static int JungleDeath;
	public static int SpruceChance;
	public static int SpruceDeath;
	public static int TreeChance;
	public static int TreeDeath;
	
	public void onEnable() 
	{
		this.saveDefaultConfig();
		
		// Start Metrics
		try 
		{
		    MetricsLite metricslite = new MetricsLite(this);
		    metricslite.start();
		} 
		catch (IOException e) 
		{
		    // Failed to submit the stats :-(
		}
				
		// Setup listeners
		new PlantListener(this);
		new WaterListener(this);
		
		// Get data folder
		PwnPlantGrowth.dataFolder = getDataFolder();
		
		// Get enabled worlds
		PwnPlantGrowth.enabledWorlds = getConfig().getStringList("enabled_worlds");
		
		// Get water source setting
		PwnPlantGrowth.blockWater = getConfig().getBoolean("block_water_bucket");
		
		// Get logfile setting
		PwnPlantGrowth.logEnabled = getConfig().getBoolean("debug_log");
		
		// Get Natural Light setting
		PwnPlantGrowth.naturalLight = getConfig().getInt("min_natural_light");
		
		// Dark growth
		PwnPlantGrowth.darkGrow = getConfig().getStringList("grow_in_dark");
		
		// Config Growth imports
		PwnPlantGrowth.CarrotChance = getConfig().getInt("Carrot.Growth");
		PwnPlantGrowth.CocoaChance = getConfig().getInt("Cocoa.Growth");
		PwnPlantGrowth.WheatChance = getConfig().getInt("Wheat.Growth");
		PwnPlantGrowth.MelonStemChance = getConfig().getInt("Melon_Stem.Growth");
		PwnPlantGrowth.NetherWartChance = getConfig().getInt("Nether_Wart.Growth");
		PwnPlantGrowth.PotatoChance = getConfig().getInt("Potato.Growth");
		PwnPlantGrowth.PumpkinStemChance = getConfig().getInt("Pumpkin_Stem.Growth");
		PwnPlantGrowth.CactusChance = getConfig().getInt("Cactus.Growth");
		PwnPlantGrowth.CaneChance = getConfig().getInt("Sugar_Cane.Growth");
		PwnPlantGrowth.MelonChance = getConfig().getInt("Melon_Block.Growth");
		PwnPlantGrowth.PumpkinChance = getConfig().getInt("Pumpkin_Block.Growth");
		PwnPlantGrowth.BigTreeChance = getConfig().getInt("Big_Tree.Growth");
		PwnPlantGrowth.BirchChance = getConfig().getInt("Birch.Growth");
		PwnPlantGrowth.BrownShroomChance = getConfig().getInt("Brown_Mushroom.Growth");
		PwnPlantGrowth.RedShroomChance = getConfig().getInt("Red_Mushroom.Growth");
		PwnPlantGrowth.JungleChance = getConfig().getInt("Jungle.Growth");
		PwnPlantGrowth.SpruceChance = getConfig().getInt("Spruce.Growth");
		PwnPlantGrowth.TreeChance = getConfig().getInt("Tree.Growth");
		
		//Config Death imports
		PwnPlantGrowth.CarrotDeath = getConfig().getInt("Carrot.Death");
		PwnPlantGrowth.CocoaDeath = getConfig().getInt("Cocoa.Death");
		PwnPlantGrowth.WheatDeath = getConfig().getInt("Wheat.Death");
		PwnPlantGrowth.MelonStemDeath = getConfig().getInt("Melon_Stem.Death");
		PwnPlantGrowth.NetherWartDeath = getConfig().getInt("Nether_Wart.Death");
		PwnPlantGrowth.PotatoDeath = getConfig().getInt("Potato.Death");
		PwnPlantGrowth.PumpkinStemDeath = getConfig().getInt("Pumpkin_Stem.Death");
		PwnPlantGrowth.CactusDeath = getConfig().getInt("Cactus.Death");
		PwnPlantGrowth.CaneDeath = getConfig().getInt("Sugar_Cane.Death");
		PwnPlantGrowth.MelonDeath = getConfig().getInt("Melon_Block.Death");
		PwnPlantGrowth.PumpkinDeath = getConfig().getInt("Pumpkin_Block.Death");
		PwnPlantGrowth.BigTreeDeath = getConfig().getInt("Big_Tree.Death");
		PwnPlantGrowth.BirchDeath = getConfig().getInt("Birch.Death");
		PwnPlantGrowth.BrownShroomDeath = getConfig().getInt("Brown_Mushroom.Death");
		PwnPlantGrowth.RedShroomDeath = getConfig().getInt("Red_Mushroom.Death");
		PwnPlantGrowth.JungleDeath = getConfig().getInt("Jungle.Death");
		PwnPlantGrowth.SpruceDeath = getConfig().getInt("Spruce.Death");
		PwnPlantGrowth.TreeDeath = getConfig().getInt("Tree.Death");			
	}
		
	public void onDisable() 
	{
		
	}
	
	static boolean random(int percentChance) 
	{
			return randomNumberGenerator.nextInt(100) < percentChance;
	}
	
	public static boolean isEnabledIn(String world) 
	{
		return enabledWorlds.contains(world);
	}	

	public static boolean canDarkGrow(String plant) 
	{
		return darkGrow.contains(plant);
	}	
	
    public static void logToFile(String message) 
    {   
	    	try 
	    	{
			    
			    if(!dataFolder.exists()) 
			    {
			    	dataFolder.mkdir();
			    }
			     
			    File saveTo = new File(dataFolder, "pwnplantgrowth.log");
			    if (!saveTo.exists())  
			    {
			    	saveTo.createNewFile();
			    }
			    
			    FileWriter fw = new FileWriter(saveTo, true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(getDate() +" "+ message);
			    pw.flush();
			    pw.close();
		    } 
		    catch (IOException e) 
		    {
		    	e.printStackTrace();
		    }
    }
    
    public static String getDate() 
    {
    	  String s;
    	  Format formatter;
    	  Date date = new Date(); 
    	  formatter = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
    	  s = formatter.format(date);
    	  return s;
    }	
    
}