package com.pwn9.PwnPlantGrowth;

public class Config
{
	public static void LoadConfig()
	{
		// Get enabled worlds
		PwnPlantGrowth.enabledWorlds = PwnPlantGrowth.instance.getConfig().getStringList("enabled_worlds");
		
		// Get water source setting
		PwnPlantGrowth.blockWaterBucket = PwnPlantGrowth.instance.getConfig().getBoolean("block_water_bucket", false);
		
		// Get water source setting
		PwnPlantGrowth.blockWaterDispenser = PwnPlantGrowth.instance.getConfig().getBoolean("block_water_dispenser", false);
		
		// Get logfile setting
		PwnPlantGrowth.logEnabled = PwnPlantGrowth.instance.getConfig().getBoolean("debug_log", false);
		PwnPlantGrowth.logCoords = PwnPlantGrowth.instance.getConfig().getBoolean("log_coords", false);
		
		// Get Natural Light setting
		PwnPlantGrowth.naturalLight = PwnPlantGrowth.instance.getConfig().getInt("min_natural_light", 10);
		
		// Dark growth
		PwnPlantGrowth.darkGrow = PwnPlantGrowth.instance.getConfig().getStringList("grow_in_dark");

		// Weed Killer
		PwnPlantGrowth.wkradius = PwnPlantGrowth.instance.getConfig().getInt("weed_killer_radius", 5);
		PwnPlantGrowth.weedKiller = PwnPlantGrowth.instance.getConfig().getString("weed_killer", "GOLD_BLOCK");
		PwnPlantGrowth.wkenabled = PwnPlantGrowth.instance.getConfig().getBoolean("weed_killer_enabled", false);

		// Fertilizer
		PwnPlantGrowth.fradius = PwnPlantGrowth.instance.getConfig().getInt("fertilizer_radius", 5);
		PwnPlantGrowth.frate = PwnPlantGrowth.instance.getConfig().getInt("fertilizer_rate", 100);
		PwnPlantGrowth.fertilizer = PwnPlantGrowth.instance.getConfig().getString("fertilizer", "SOUL_SAND");
		PwnPlantGrowth.fenabled = PwnPlantGrowth.instance.getConfig().getBoolean("fertilizer_enabled", false);
		
		// UV
		PwnPlantGrowth.uvradius = PwnPlantGrowth.instance.getConfig().getInt("uv_radius", 5);
		PwnPlantGrowth.uv = PwnPlantGrowth.instance.getConfig().getString("uv", "GLOWSTONE");
		PwnPlantGrowth.uvenabled = PwnPlantGrowth.instance.getConfig().getBoolean("uv_enabled", false);
		
		// Bonemeal setting
		PwnPlantGrowth.uvenabled = PwnPlantGrowth.instance.getConfig().getBoolean("limit_bonemeal", false);		
	}
}