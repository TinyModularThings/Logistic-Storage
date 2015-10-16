package logisticStorage.src.common.network.packets.server;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.FMLLog;

import logisticStorage.src.LogisticStorage;
import logisticStorage.src.api.network.server.INetworkUpdateReceiver;
import logisticStorage.src.common.network.handler.Encoder;
import logisticStorage.src.common.network.handler.TileField.FieldData;
import logisticStorage.src.common.network.packets.IPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class PacketFieldUpdate implements IPacket {

	int dimID;
	Map<ChunkCoordinates, Map<String, Object>> data = new HashMap<ChunkCoordinates, Map<String, Object>>();
	
	public PacketFieldUpdate() {
	}
	
	public PacketFieldUpdate(List<FieldData> packetData, World world) {
		dimID = world.provider.dimensionId;
		for(int i = 0;i<packetData.size();i++)
		{
			FieldData field = packetData.get(i);
			ChunkCoordinates coords = field.getCoords();
			if(!data.containsKey(coords))
			{
				data.put(coords, new HashMap<String, Object>());
			}
			Map<String, Object> entries = data.get(coords);
			entries.put(field.getField(), field.getData());
		}
	}

	@Override
	public void read(ByteBuf stream) {
		dimID = stream.readInt();
		try {
			int amount = stream.readInt();
			for(int i = 0;i<amount;i++)
			{
				ChunkCoordinates coords = (ChunkCoordinates) Encoder.decode(stream);
				Map<String, Object> map = new HashMap<String, Object>();
				int entries = stream.readInt();
				for(int z = 0;z<entries;z++)
				{
					map.put((String)Encoder.decode(stream), Encoder.decode(stream));
				}
				data.put(coords, map);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(ByteBuf stream) {
		stream.writeInt(dimID);
		try {
			stream.writeInt(data.size());
			for(Entry<ChunkCoordinates, Map<String, Object>> entry : data.entrySet())
			{
				Encoder.encode(stream, entry.getKey());
				Map<String, Object> map = entry.getValue();
				stream.writeInt(map.size());
				for(Entry<String, Object> fields : map.entrySet())
				{
					Encoder.encode(stream, fields.getKey());
					Encoder.encode(stream, fields.getValue());
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void execute(EntityPlayer player) {
		World world = LogisticStorage.core.get().getWorld(dimID);
		if(world == null)
		{
			throw new RuntimeException("Field Update Packet got incorrect Data");
		}
		for(Entry<ChunkCoordinates, Map<String, Object>> map : data.entrySet())
		{
			ChunkCoordinates coords = map.getKey();
			TileEntity tile = world.getTileEntity(coords.posX, coords.posY, coords.posZ);
			if(tile != null)
			{
				for(Entry<String, Object> data : map.getValue().entrySet())
				{
					progressField(tile, data.getKey(), data.getValue());
				}
			}
		}
	}
	
	
	private void progressField(TileEntity tile, String field, Object data)
	{
		try
		{
			Field targetField = null;
			Class fieldDeclaringClass = tile.getClass();
			do
			{
				try
				{
					targetField = fieldDeclaringClass.getDeclaredField(field);
				}
				catch(NoSuchFieldException e4)
				{
					fieldDeclaringClass = fieldDeclaringClass.getSuperclass();
				}
			}
			while(targetField == null && fieldDeclaringClass != null);
			if(field == null)
			{
				FMLLog.getLogger().info("Can't find field " + field + " in te " + tile + " at " + tile.xCoord + "/" + tile.yCoord + "/" + tile.zCoord);
			}
			else
			{
				targetField.setAccessible(true);
			}
			if(targetField != null && tile != null)
			{
				if(targetField.getType().isEnum())
				{
					data = targetField.getType().getEnumConstants()[((Integer)data).intValue()];
				}
				targetField.set(tile, data);
			}
			if(tile != null && tile instanceof INetworkUpdateReceiver)
			{
				((INetworkUpdateReceiver)tile).onFieldUpdate(field);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
