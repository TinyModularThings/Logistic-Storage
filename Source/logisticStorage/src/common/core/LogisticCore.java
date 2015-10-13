package logisticStorage.src.common.core;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class LogisticCore {

	public boolean isServer()
	{
		return !isClient();
	}
	
	public boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	public boolean isClientWorld()
	{
		return MinecraftServer.getServer().isSinglePlayer();
	}
	
	public boolean isServerWorld()
	{
		return !isClientWorld();
	}
}
