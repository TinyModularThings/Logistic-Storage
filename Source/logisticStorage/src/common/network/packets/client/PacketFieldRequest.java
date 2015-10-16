package logisticStorage.src.common.network.packets.client;

import io.netty.buffer.ByteBuf;
import logisticStorage.src.LogisticStorage;
import logisticStorage.src.api.network.server.INetworkDataTile;
import logisticStorage.src.common.network.NetworkCore;
import logisticStorage.src.common.network.packets.IPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketFieldRequest implements IPacket {
	
	int dimID;
	int x;
	int y;
	int z;
	
	public PacketFieldRequest() {
	}
	
	public PacketFieldRequest(TileEntity par1) {
		dimID = par1.getWorldObj().provider.dimensionId;
		x = par1.xCoord;
		y = par1.yCoord;
		z = par1.zCoord;
	}
	
	@Override
	public void read(ByteBuf stream) {
		dimID = stream.readInt();
		x = stream.readInt();
		y = stream.readInt();
		z = stream.readInt();
	}

	@Override
	public void write(ByteBuf stream) {
		stream.writeInt(dimID);
		stream.writeInt(x);
		stream.writeInt(y);
		stream.writeInt(z);
	}

	@Override
	public void execute(EntityPlayer player) {
		World world = LogisticStorage.core.get().getWorld(dimID);
		if(world == null)
		{
			throw new RuntimeException("Field Update Request Packet got Incorrect data");
		}
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof INetworkDataTile))
		{
			throw new RuntimeException("Field Update Request Packet got Incorrect data");
		}
		NetworkCore core = LogisticStorage.network.get();
		for(String field : ((INetworkDataTile)tile).getUpdateFields())
		{
			core.updateTileEntityField(tile, field);
		}
	}

}
