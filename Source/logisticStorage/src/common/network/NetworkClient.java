package logisticStorage.src.common.network;

import logisticStorage.src.api.network.server.INetworkDataTile;
import logisticStorage.src.common.network.handler.PacketManager;
import logisticStorage.src.common.network.packets.client.PacketClientTileEvent;
import logisticStorage.src.common.network.packets.client.PacketFieldRequest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class NetworkClient extends NetworkCore {
	
	@Override
	public void sendTileEvent(TileEntity tile, int id, int argument, boolean limited) {
		
	}
	
	@Override
	public void sendClientEventUpdate(TileEntity par1, int id, int argument)
	{
		PacketManager.instance.sendToServer(new PacketClientTileEvent(par1, id, argument));
	}
	
	@Override
	public void loadTileInitData(INetworkDataTile par1) {
		PacketManager.instance.sendToServer(new PacketFieldRequest((TileEntity) par1));
	}
	
	@Override
	public void updateTileEntityField(TileEntity tile, String field) {
	
	}
	
	@Override
	public void updateFields(World world) {
	}
}
