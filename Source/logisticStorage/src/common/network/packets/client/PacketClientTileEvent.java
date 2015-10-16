package logisticStorage.src.common.network.packets.client;

import io.netty.buffer.ByteBuf;
import logisticStorage.src.LogisticStorage;
import logisticStorage.src.api.network.client.IClientTileEventListener;
import logisticStorage.src.common.network.packets.IPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketClientTileEvent implements IPacket {
	
	int dimID;
	int x;
	int y;
	int z;
	int id;
	int argument;
	
	public PacketClientTileEvent(TileEntity par1, int par2, int par3) {
		dimID = par1.getWorldObj().provider.dimensionId;
		x = par1.xCoord;
		y = par1.yCoord;
		z = par1.zCoord;
		id = par2;
		argument = par3;
	}
	
	
	@Override
	public void read(ByteBuf stream) {
		dimID = stream.readInt();
		x = stream.readInt();
		y = stream.readInt();
		z = stream.readInt();
		id = stream.readInt();
		argument = stream.readInt();
	}

	@Override
	public void write(ByteBuf stream) {
		stream.writeInt(dimID);
		stream.writeInt(x);
		stream.writeInt(y);
		stream.writeInt(z);
		stream.writeInt(id);
		stream.writeInt(argument);
	}

	@Override
	public void execute(EntityPlayer player) {
		World world = LogisticStorage.core.get().getWorld(dimID);
		if(world == null)
		{
			throw new RuntimeException("Client Tile Event packet got incorrect data!");
		}
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof IClientTileEventListener))
		{
			throw new RuntimeException("Client Tile Event packet got incorrect data!");
		}
		((IClientTileEventListener)tile).onEvent(id, player, argument);
	}

}
