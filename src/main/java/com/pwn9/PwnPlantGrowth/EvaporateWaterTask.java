package com.pwn9.PwnPlantGrowth;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class EvaporateWaterTask implements Runnable 
{
	private Block block;
	
	public EvaporateWaterTask(Block block) 
	{
		this.block = block;
	}
	
	@Override
	public void run()
	{
		if (isWater(this.block))
		{
			this.block.setType(Material.AIR);			
		}
		if (isWater(this.block.getRelative(BlockFace.NORTH)))
		{
			this.block.getRelative(BlockFace.NORTH).setType(Material.AIR);
		}
		if (isWater(this.block.getRelative(BlockFace.SOUTH)))
		{
			this.block.getRelative(BlockFace.SOUTH).setType(Material.AIR);
		}
		if (isWater(this.block.getRelative(BlockFace.WEST)))
		{
			this.block.getRelative(BlockFace.WEST).setType(Material.AIR);
		}
		if (isWater(this.block.getRelative(BlockFace.EAST)))
		{
			this.block.getRelative(BlockFace.EAST).setType(Material.AIR);
		}
	}	
	
	public boolean isWater(Block block)
	{
		Block b = block;
		
		if (b.getType() == Material.STATIONARY_WATER || b.getType() == Material.WATER) 
		{
			return true;
		}
		else 
		{
			return false;
		}		
	}	
}
