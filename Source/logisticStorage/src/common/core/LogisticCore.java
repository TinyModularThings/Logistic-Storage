package logisticStorage.src.common.core;

import logisticStorage.src.common.handler.TickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class LogisticCore {

	public boolean isServer()
	{
		return true;
	}
	
	public boolean isClient()
	{
		return false;
	}
	
	public boolean isClientWorld()
	{
		return MinecraftServer.getServer().isSinglePlayer();
	}
	
	public boolean isServerWorld()
	{
		return !isClientWorld();
	}
	
	public EntityPlayer getPlayer()
	{
		return null;
	}
	
	public World getWorld(int dimID)
	{
		return DimensionManager.getWorld(dimID);
	}
	
	public void init()
	{
		TickHandler.instance.init();
	}
}
