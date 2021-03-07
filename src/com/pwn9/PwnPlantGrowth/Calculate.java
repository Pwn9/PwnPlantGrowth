package com.pwn9.PwnPlantGrowth;

import java.util.List;

import org.bukkit.Material;

public class Calculate {
	Boolean isCancelled;
	Material replacement;
	String doLog;
	
	Calculate(List<List<String>> specialBlocks, String thisBlock, String curBiome, Boolean isDark)
	{
		isCancelled = false;
		String toLog = "";
		String frontLog = ", Biome: " + curBiome + ", Dark: " + isDark.toString() + ", ";
		String darkLog = "Dark Settings: {";
		String groupLog = "Settings: {";
		
		// bool to catch if the biome is never declared in any config, therefor a bad biome and should not grow
		// not true - i'm an idiot, the null biome is ok to have actually and means default growth
		boolean noBiome = true;
		
		int curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".Growth");
		frontLog += "Default Growth: " + curGrowth + ", ";
		int curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".Death");
		frontLog += "Default Death: " + curDeath + ", ";
		
		
		if ((PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".BiomeGroup")) || (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").isEmpty()) || (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").contains(curBiome))) 
		{	
			// check the area to find if any of the special blocks are found
			List<String> fBlocksFound = specialBlocks.get(0);
			List<String> wkBlocksFound = specialBlocks.get(1);
			List<String> uvBlocksFound = specialBlocks.get(2);
			
			

			// check the biome group settings if they are set.
			if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".BiomeGroup")) 
			{
				
				// create list from the config setting
				List<?> groupList = PwnPlantGrowth.instance.getConfig().getList(thisBlock+".BiomeGroup");
				
				groupLog += "BiomeGroup: " + groupList.toString() + ", ";
				
				// iterate through list and see if any of that list matches curBiome
				boolean matches = false;
				for (int i = 0; i < groupList.size(); i++) 
				{
					
					// check the biomegroup for this named group
					if ((PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
					{
						matches = true;
						noBiome = false;
						groupLog += "Matches: " + groupList.get(i) + ", ";
						
						// reference the configs now to see if the config settings are set!
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Growth")) 
						{
							curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Growth");
							groupLog += "New Growth: " + curGrowth + ", ";
						}
						
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Death")) 
						{
							curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Death");
							groupLog += "New Death: " + curDeath + ", ";
						}						
					}	
				}
				if (!matches) {
					groupLog += "Matches: NULL, ";
				}
			}	
			else {
				groupLog += "BiomeGroup: NULL,  ";
			}
			
			groupLog += "Specific Settings: {";
			
			
			
			// BIOME SETTINGS - if per biome is set, it overrides a biome group
			if (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").contains(curBiome)) {
				noBiome = false;
				// override with individual settings
				if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".Growth")) 
				{
					curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".Growth");
					groupLog += "Growth for " + curBiome + ": " + curGrowth + ", ";
				}
				
				if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".Death")) 
				{
					curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".Death");
					groupLog += "Death for " + curBiome + ": " + curDeath + ", ";
				}
			}
			
			
			
			
			// if there is fertilizer, grow this plant at the fertilizer rate - default 100%
			// TODO: should fertilizer override dark settings or not - i think not for now
			if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
			{
				groupLog += PwnPlantGrowth.fertFound;
				// set the current growth to the fertilizer rate
				curGrowth = PwnPlantGrowth.frate;
			}
			groupLog += "}}, ";
			
			
			
			
			// See if there are special settings for dark growth
			if (isDark) 
			{
				// If uv is enabled and found, isDark remains false.
				if (uvBlocksFound.contains(PwnPlantGrowth.uv))
				{
					darkLog += PwnPlantGrowth.uvFound;
				}
				else 
				{							
					// ISDARK: default isDark config rates (if exist)
					if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".GrowthDark")) 
					{
						curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".GrowthDark");
						darkLog += "Growth: " + curGrowth + ", ";
					}
					
					if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".DeathDark")) 
					{
						curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".DeathDark");
						darkLog += "Death: " + curDeath + ", ";
					}
					
					
					
					// ISDARK: override default values with biome group values
					if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".BiomeGroup")) 
					{
						
						// create list from the config setting
						List<?> groupList = PwnPlantGrowth.instance.getConfig().getList(thisBlock+".BiomeGroup");
						
						darkLog += "BiomeGroup: " + groupList.toString() + ", ";
						
						// iterate through list and see if any of that list matches curBiome
						boolean matches = false;
						for (int i = 0; i < groupList.size(); i++) {
							
							// check the biomegroup for this named group
							if  ((PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
							{
								
								matches = true;
								noBiome = false;
								darkLog += "Matching: " + groupList.get(i) + ", ";
								
								// reference the configs now to see if the config settings are set!
								if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".GrowthDark")) 
								{
									curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".GrowthDark");
									darkLog += "New Growth: " + curGrowth + ", ";
								}
								
								if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".DeathDark")) 
								{
									curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".DeathDark");
									darkLog += "New Death: " + curDeath + ", ";
								}						
							}
						}
						if (!matches) {
							darkLog += "Matches: NULL, ";
						}
					}
					else {
						darkLog += "BiomeGroup: NULL, ";
					}
					
					darkLog += "Specific Settings: {";
					
					// ISDARK: per biome isDark rates (if exist) override biomegroup rates
					if (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").contains(curBiome)) {
						noBiome = false;
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".GrowthDark")) 
						{
							curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".GrowthDark");
							darkLog += "Growth for " + curBiome + ": " + curGrowth + ", ";
						}
						
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".DeathDark")) 
						{
							curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".DeathDark");
							darkLog += "Death for " + curBiome + ": " + curDeath + ", ";
						}
					}
					
					darkLog += "}}, ";
				}
			}	
			
			// if BIOME was left empty, BIOME: [] it was intentional.. and this means use default growth rate.
			if ((PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").isEmpty())) {
				noBiome = false;
			}
			
			
			// check config again - nobiome means the plant had no config available anywhere not even empty/default
			if (noBiome) 
			{
				isCancelled = true;
				toLog += "RESULT: {Failed Growth: Bad Biome}";	
				// chance of death
				if (PwnPlantGrowth.random(curDeath)) 
				{
					// TODO: make these configurable
					if (thisBlock == "COCOA") {
						replacement = Material.VINE;
					}
					else if (thisBlock == "KELP") {
						replacement = Material.SEAGRASS;
					}
					else {
						replacement = Material.DEAD_BUSH;
					}
					toLog += " {Plant Died, Rate: " + curDeath + "}";
				}				
			}
			// Run the chance for growth here... 
			else if (!(PwnPlantGrowth.random(curGrowth))) 
			{
				isCancelled = true;
				toLog += "RESULT: {Failed Growth, Rate: " + curGrowth + "} ";
				
				if (wkBlocksFound.contains(PwnPlantGrowth.weedKiller)) 
				{
					toLog += PwnPlantGrowth.wkFound;
				}
				else 
				{
					// chance of death
					if (PwnPlantGrowth.random(curDeath)) 
					{
						// TODO: make these configurable
						if (thisBlock == "COCOA") {
							replacement = Material.VINE;
						}
						else if (thisBlock == "KELP") {
							replacement = Material.SEAGRASS;
						}
						else {
							replacement = Material.DEAD_BUSH;
						}
						toLog += " {Plant Died, Rate: " + curDeath + "}";
					}
				}
			}
			else 
			{
				toLog += "RESULT: {Plant Grew, Rate: " + curGrowth + "}";
				
			}
		}
		else 
		{
			isCancelled = true;
			toLog += "RESULT: {Failed Growth: Bad Biome}";	
			// chance of death
			if (PwnPlantGrowth.random(curDeath)) 
			{
				// TODO: make these configurable
				if (thisBlock == "COCOA") {
					replacement = Material.VINE;
				}
				else if (thisBlock == "KELP") {
					replacement = Material.SEAGRASS;
				}
				else {
					replacement = Material.DEAD_BUSH;
				}
				toLog += " {Plant Died, Rate: " + curDeath + "}";
			}		
		}	
		
		String midLog = "";
		if (isDark) {
			midLog += darkLog;
		} 
		else {
			midLog += groupLog;
		}
		
		doLog = frontLog + midLog + toLog;
	}

	// just report the rate for player listener feature for growth rates
	Calculate(Boolean report, List<List<String>> specialBlocks, String thisBlock, String curBiome, Boolean isDark)
	{
		isCancelled = false;
		String toLog = "";
		// bool to catch if the biome is never declared in any config, therefor a bad biome and should not grow
		boolean noBiome = true;
		boolean fert = false;
		boolean uv = false;
		
		// defaults
		int curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".Growth");
		int curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".Death");
		
		if ((PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".BiomeGroup")) || (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").isEmpty()) || (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").contains(curBiome))) 
		{	
			// check the area to find if any of the special blocks are found
			List<String> fBlocksFound = specialBlocks.get(0);
			//List<String> wkBlocksFound = specialBlocks.get(1);
			List<String> uvBlocksFound = specialBlocks.get(2);
			
			// check the biome group settings
			if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".BiomeGroup")) 
			{
				
				// create list from the config setting
				List<?> groupList = PwnPlantGrowth.instance.getConfig().getList(thisBlock+".BiomeGroup");
				
				// iterate through list and see if any of that list matches curBiome
				for (int i = 0; i < groupList.size(); i++) 
				{
					
					// check the biomegroup for this named group
					if ((PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
					{
						noBiome = false;
						
						// reference the configs now to see if the config settings are set!
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Growth")) 
						{
							curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Growth");
						}
						
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".Death")) 
						{
							curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".Death");
						}						
					}	
				}
			}	
			
			if (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").contains(curBiome)) {
				noBiome = false;
				// override with individual settings
				if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".Growth")) 
				{
					curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".Growth");
				}
				
				if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".Death")) 
				{
					curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".Death");
				}
			}
			
			// if there is fertilizer, grow this plant at the fertilizer rate - default 100%
			if (fBlocksFound.contains(PwnPlantGrowth.fertilizer))
			{
				// set the current growth to the fertilizer rate
				curGrowth = PwnPlantGrowth.frate;
				fert = true;
			}
			
			// See if there are special settings for dark growth
			if (isDark) 
			{
				// If no UV isDark remains false.
				if (uvBlocksFound.contains(PwnPlantGrowth.uv))
				{
					uv = true;
				}
				else
				{
					// default isDark config rates (if exist)
					if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".GrowthDark")) 
					{
						curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".GrowthDark");
					}
					
					if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".DeathDark")) 
					{
						curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+".DeathDark");
					}
					
					// override default values with biome group values
					if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+".BiomeGroup")) 
					{
						
						// create list from the config setting
						List<?> groupList = PwnPlantGrowth.instance.getConfig().getList(thisBlock+".BiomeGroup");
						
						// iterate through list and see if any of that list matches curBiome
						for (int i = 0; i < groupList.size(); i++) {
							
							// check the biomegroup for this named group
							if  ((PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)) != null) && (PwnPlantGrowth.instance.getConfig().getList("BiomeGroup."+groupList.get(i)).contains(curBiome))) 
							{
								
								noBiome = false;
								
								// reference the configs now to see if the config settings are set!
								if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".GrowthDark")) 
								{
									curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".GrowthDark");
								}
								
								if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+groupList.get(i)+".DeathDark")) 
								{
									curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+groupList.get(i)+".DeathDark");
								}						
							}
						}
					}
					
					// per biome isDark rates (if exist)
					if (PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").contains(curBiome)) {
						noBiome = false;
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".GrowthDark")) 
						{
							curGrowth = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".GrowthDark");
						}
						
						if (PwnPlantGrowth.instance.getConfig().isSet(thisBlock+"."+curBiome+".DeathDark")) 
						{
							curDeath = PwnPlantGrowth.instance.getConfig().getInt(thisBlock+"."+curBiome+".DeathDark");
						}
					}
				}
			}	
		}
		
		// if BIOME was left empty, BIOME: [] it was intentional.. and this means use default growth rate.
		if ((PwnPlantGrowth.instance.getConfig().getList(thisBlock+".Biome").isEmpty())) {
			noBiome = false;
		}		

		if (noBiome) 
		{
			toLog += thisBlock + " will not grow in biome: " + curBiome;				
		}
		else 
		{
			toLog += thisBlock + " grows at " + curGrowth + "%, dies at " + curDeath +"% in biome " + curBiome;
			if (isDark) 
			{
				toLog += " in the dark";
				if (uv) 
				{
					toLog += " with UV block nearby";
				}
			}
			if (fert)
			{
				toLog += " with fertilizer block nearby";
			}
		}

		doLog = toLog + ". ";
		return;
	}	
}
