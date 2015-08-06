package com.pwn9.PwnPlantGrowth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.java.JavaPlugin;

//import com.khorn.terraincontrol.TerrainControl;

public class PwnPlantGrowth extends JavaPlugin 
{
	
	// declare some stuffs to be used later
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	public static File dataFolder;
	public static Boolean logEnabled;
	public static int naturalLight;
	public static List<String> darkGrow;
	public static List<String> enabledWorlds;
	public static List<String> plantTypes;
	static Random randomNumberGenerator = new Random();

	public void onEnable() 
	{
		this.saveDefaultConfig();
		
		// Setup listeners
		new PlantListener(this);
		
		// Get data folder
		PwnPlantGrowth.dataFolder = getDataFolder();
		
		// Get enabled worlds
		PwnPlantGrowth.enabledWorlds = getConfig().getStringList("enabled_worlds");
		
		// Get logfile setting
		PwnPlantGrowth.logEnabled = getConfig().getBoolean("debug_log", false);
		
		// Get Natural Light setting
		PwnPlantGrowth.naturalLight = getConfig().getInt("min_natural_light", 10);
		
		// Dark growth
		PwnPlantGrowth.darkGrow = getConfig().getStringList("grow_in_dark");

		// Load all possible plant types
		String sArray[] = new String[] { "CACTUS", "CARROT", "COCOA", "CROPS", "MELON_BLOCK", "MELON_STEM", "NETHER_WARTS", "POTATO", "PUMPKIN_BLOCK", "PUMPKIN_STEM", "SUGAR_CANE_BLOCK", "ACACIA", "BIG_TREE", "BIRCH", "BROWN_MUSHROOM", "COCOA_TREE", "DARK_OAK", "JUNGLE", "JUNGLE_BUSH", "MEGA_REDWOOD", "RED_MUSHROOM", "REDWOOD", "SMALL_JUNGLE", "SWAMP", "TALL_BIRCH", "TALL_REDWOOD", "TREE" };
		PwnPlantGrowth.plantTypes = Arrays.asList(sArray);
			
    	if (PwnPlantGrowth.logEnabled) 
    	{	
    		PwnPlantGrowth.logToFile("PwnPlantGrowth Enabled");
    	}	
    	
    	configCheck();
	}
		
	public void onDisable() 
	{
    	if (PwnPlantGrowth.logEnabled) 
    	{	
    		PwnPlantGrowth.logToFile("PwnPlantGrowth Disabled");
    	}	
	}
	
	public void configCheck()
	{
		for (int i = 0; i < PwnPlantGrowth.plantTypes.size(); i++) {
			//System.out.println(PwnPlantGrowth.plantTypes.get(i));
			
		    if (!(getConfig().contains(PwnPlantGrowth.plantTypes.get(i)))) {
		        // The config does not contain this plant value
		    	if (PwnPlantGrowth.logEnabled) 
		    	{	
		    		PwnPlantGrowth.logToFile("Configuration Error: " + PwnPlantGrowth.plantTypes.get(i) + " was not found in your config.yml and is required!");
		    	}			    	
		    }
		}
		
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

	public static String getBiome(BlockGrowEvent e) 
	{
		return String.valueOf(e.getBlock().getBiome());
	}

	public static String getBiome(StructureGrowEvent e) 
	{
			return String.valueOf(e.getLocation().getBlock().getBiome());
	}

}