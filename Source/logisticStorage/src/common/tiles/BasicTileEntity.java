package logisticStorage.src.common.tiles;

import java.util.ArrayList;
import java.util.List;

import logisticStorage.src.LogisticStorage;
import logisticStorage.src.api.network.server.INetworkUpdateReceiver;
import logisticStorage.src.api.utils.ITickNotfiy;
import logisticStorage.src.common.handler.TickHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public class BasicTileEntity extends TileEntity implements INetworkUpdateReceiver {

	public int facing;
	public int rotation;
	public boolean loaded;
	
	@Override
	public List<String> getUpdateFields() {
		List<String> list = new ArrayList<String>();
		list.add("direction");
		list.add("rotation");
		return list;
	}

	@Override
	public void onFieldUpdate(String fieldName) {
		if(fieldName.equals("facing") || fieldName.equals("rotation"))
		{
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	/**
	 * NEVER USE THIS AGAIN!
	 */
	@Override
	public void validate() {
		super.validate();
		if(!loaded)
		{
			if(!isInvalid() && worldObj != null)
			{
				if(worldObj.isRemote)
				{
					onLoad();
				}
				else
				{
					TickHandler.registerTick(getWorldObj(), new ITickNotfiy(){

						@Override
						public void onTick(World world)
						{
							BasicTileEntity.this.onLoad();
						}
						
					});
				}
			}
			else
			{
				FMLLog.getLogger().info("TileEntity loaded not correctly:"+this.toString());
			}
		}
	}
	
	/**
	 * NEVER USE THIS AGAIN!
	 */
	@Override
	public void invalidate() {
		if(loaded)
		{
			onUnload();
		}
		super.invalidate();
	}
	
	/**
	 * NEVER USE THIS AGAIN!
	 */
	@Override
	public void onChunkUnload() {
		if(loaded)
		{
			onUnload();
		}
		super.onChunkUnload();
	}
	
	public void onLoad()
	{
		if(worldObj.isRemote)
		{
			LogisticStorage.network.get().loadTileInitData(this);
		}
		loaded = true;
	}
	
	public void onUnload()
	{
		loaded = false;
	}
	
}
