package com.pwn9.PwnPlantGrowth;

public class Config extends PwnPlantGrowth
{
	public static void LoadConfig()
	{
		// Get enabled worlds
		PwnPlantGrowth.enabledWorlds = instance.getConfig().getStringList("enabled_worlds");
		
		// Get water source setting
		PwnPlantGrowth.blockWaterBucket = instance.getConfig().getBoolean("block_water_bucket", false);
		
		// Get water source setting
		PwnPlantGrowth.blockWaterDispenser = instance.getConfig().getBoolean("block_water_dispenser", false);
		
		// Get logfile setting
		PwnPlantGrowth.logEnabled = instance.getConfig().getBoolean("debug_log", false);
		
		// Get Natural Light setting
		PwnPlantGrowth.naturalLight = instance.getConfig().getInt("min_natural_light", 10);
		
		// Dark growth
		PwnPlantGrowth.darkGrow = instance.getConfig().getStringList("grow_in_dark");

		// Weed Killer
		PwnPlantGrowth.wkradius = instance.getConfig().getInt("weed_killer_radius", 5);
		PwnPlantGrowth.weedKiller = instance.getConfig().getString("weed_killer", "GOLD_BLOCK");
		PwnPlantGrowth.wkenabled = instance.getConfig().getBoolean("weed_killer_enabled", false);

		// Fertilizer
		PwnPlantGrowth.fradius = instance.getConfig().getInt("fertilizer_radius", 5);
		PwnPlantGrowth.frate = instance.getConfig().getInt("fertilizer_rate", 100);
		PwnPlantGrowth.fertilizer = instance.getConfig().getString("fertilizer", "SOUL_SAND");
		PwnPlantGrowth.fenabled = instance.getConfig().getBoolean("fertilizer_enabled", false);
		
		// UV
		PwnPlantGrowth.uvradius = instance.getConfig().getInt("uv_radius", 5);
		PwnPlantGrowth.uv = instance.getConfig().getString("uv", "GLOWSTONE");
		PwnPlantGrowth.uvenabled = instance.getConfig().getBoolean("uv_enabled", false);
		
		// Bonemeal setting
		PwnPlantGrowth.uvenabled = instance.getConfig().getBoolean("limit_bonemeal", false);		
	}
}