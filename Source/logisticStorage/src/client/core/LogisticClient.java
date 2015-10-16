package logisticStorage.src.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import logisticStorage.src.common.core.LogisticCore;

public class LogisticClient extends LogisticCore {

	
	@Override
	public boolean isClient() {
		return true;
	}
	
	@Override
	public boolean isServer() {
		return false;
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	@Override
	public World getWorld(int dimID) {
		World world = Minecraft.getMinecraft().theWorld;
		if(world.provider.dimensionId != dimID)
		{
			return null;
		}
		return world;
	}
}
