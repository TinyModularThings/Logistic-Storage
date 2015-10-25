package logisticStorage.src.common.core;

import logisticStorage.src.api.gui.ILSTileGui;
import logisticStorage.src.common.handler.TickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.IGuiHandler;

public class LogisticCore implements IGuiHandler {

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

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0)
		{
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile != null && tile instanceof ILSTileGui)
			{
				return ((ILSTileGui)tile).getContainer(player.inventory);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0)
		{
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile != null && tile instanceof ILSTileGui)
			{
				return ((ILSTileGui)tile).getGui(player.inventory);
			}
		}
		return null;
	}
}
