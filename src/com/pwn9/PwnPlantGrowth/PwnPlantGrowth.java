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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.khorn.terraincontrol.TerrainControl;

public class PwnPlantGrowth extends JavaPlugin 
{
	
	// declare some stuffs to be used later
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	public static File dataFolder;
	public static Boolean logEnabled;
	public static Boolean blockWaterBucket;
	public static Boolean blockWaterDispenser;
	public static int naturalLight;
	public static List<String> darkGrow;
	public static List<String> enabledWorlds;
	public static List<String> plantTypes;
	static Random randomNumberGenerator = new Random();
	
	// Weedkiller
	public static int wkradius;
	public static String weedKiller;
	public static Boolean wkenabled;

	// Fertilizer
	public static int fradius;
	public static int frate;
	public static String fertilizer;
	public static Boolean fenabled;
	
	// UV
	public static int uvradius;
	public static String uv;
	public static Boolean uvenabled;	
	
	// Common messages
	public static String fertFound;
	public static String wkFound;
	public static String uvFound;
	
	// Terrain Control Hook
	public static TerrainControl tc;	
	
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
		
		// Get data folder
		PwnPlantGrowth.dataFolder = getDataFolder();
		
		// Get enabled worlds
		PwnPlantGrowth.enabledWorlds = getConfig().getStringList("enabled_worlds");
		
		// Get water source setting
		PwnPlantGrowth.blockWaterBucket = getConfig().getBoolean("block_water_bucket", false);
		
		// Get water source setting
		PwnPlantGrowth.blockWaterDispenser = getConfig().getBoolean("block_water_dispenser", false);
		
		// Get logfile setting
		PwnPlantGrowth.logEnabled = getConfig().getBoolean("debug_log", false);
		
		// Get Natural Light setting
		PwnPlantGrowth.naturalLight = getConfig().getInt("min_natural_light", 10);
		
		// Dark growth
		PwnPlantGrowth.darkGrow = getConfig().getStringList("grow_in_dark");

		// Weed Killer
		PwnPlantGrowth.wkradius = getConfig().getInt("weed_killer_radius", 5);
		PwnPlantGrowth.weedKiller = getConfig().getString("weed_killer", "GOLD_BLOCK");
		PwnPlantGrowth.wkenabled = getConfig().getBoolean("weed_killer_enabled", false);

		// Fertilizer
		PwnPlantGrowth.fradius = getConfig().getInt("fertilizer_radius", 5);
		PwnPlantGrowth.frate = getConfig().getInt("fertilizer_rate", 100);
		PwnPlantGrowth.fertilizer = getConfig().getString("fertilizer", "SOUL_SAND");
		PwnPlantGrowth.fenabled = getConfig().getBoolean("fertilizer_enabled", false);
		
		// UV
		PwnPlantGrowth.uvradius = getConfig().getInt("uv_radius", 5);
		PwnPlantGrowth.uv = getConfig().getString("uv", "GLOWSTONE");
		PwnPlantGrowth.uvenabled = getConfig().getBoolean("uv_enabled", false);
		
		// Messages
		PwnPlantGrowth.fertFound = " Fertilizer found, growth rate 100%";
		PwnPlantGrowth.wkFound = " Weed Killer found, plants won't die.";
		PwnPlantGrowth.uvFound = " UV found, allowing false light growth.";
		
		// Load all possible plant types
		String sArray[] = new String[] { "BEETROOT_BLOCK", "CACTUS", "CARROT", "COCOA", "CROPS", "MELON_BLOCK", "MELON_STEM", "NETHER_WARTS", "POTATO", "PUMPKIN_BLOCK", "PUMPKIN_STEM", "SUGAR_CANE_BLOCK", "ACACIA", "BIG_TREE", "BIRCH", "BROWN_MUSHROOM", "COCOA_TREE", "DARK_OAK", "JUNGLE", "JUNGLE_BUSH", "MEGA_REDWOOD", "RED_MUSHROOM", "REDWOOD", "SMALL_JUNGLE", "SWAMP", "TALL_BIRCH", "TALL_REDWOOD", "TREE" };
		PwnPlantGrowth.plantTypes = Arrays.asList(sArray);
		
		// Check for TerrainControl
		Plugin plug = getServer().getPluginManager().getPlugin("TerrainControl");
		if (plug != null)
		{
	    	if (PwnPlantGrowth.logEnabled) 
	    	{				
	    		PwnPlantGrowth.logToFile("Terrain Control Found, Enabling Hooks");	
	    	}
		}
			
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
		if (tc != null) 
		{		
			String tControl = TerrainControl.getBiomeName(e.getBlock().getWorld().getName(), e.getBlock().getLocation().getBlockX(), e.getBlock().getLocation().getBlockZ());
			if (tControl != null)
			{
				return tControl;
			}
			else
			{
				return String.valueOf(e.getBlock().getBiome());
			}
		}
		else
		{
			return String.valueOf(e.getBlock().getBiome());
		}
	}

	public static String getBiome(StructureGrowEvent e) 
	{
		if (tc != null) 
		{
			String tControl = TerrainControl.getBiomeName(e.getWorld().getName(), e.getLocation().getBlockX(), e.getLocation().getBlockZ());
			if (tControl != null)
			{
				return tControl;
			}
			else 
			{
				return String.valueOf(e.getLocation().getBlock().getBiome());
			}
		}
		else
		{
			return String.valueOf(e.getLocation().getBlock().getBiome());
		}
	}

}