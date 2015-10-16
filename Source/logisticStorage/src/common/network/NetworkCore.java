package logisticStorage.src.common.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logisticStorage.src.api.network.server.INetworkDataTile;
import logisticStorage.src.common.network.handler.PacketManager;
import logisticStorage.src.common.network.handler.TileField;
import logisticStorage.src.common.network.handler.TileField.FieldData;
import logisticStorage.src.common.network.packets.IPacket;
import logisticStorage.src.common.network.packets.server.PacketFieldUpdate;
import logisticStorage.src.common.network.packets.server.PacketServerTileEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class NetworkCore {
	/*
	 * Special Packet Handling Class
	 * Here will be functions that get called to send packets.
	 * Like Update Field, or Send updates for something and stuff like that
	 * Maybe a Login Packet.
	 * NetworkManager = Side From where it starts
	 * NetworkCore = Server
	 * NetworkClient = Client
	 * 
	 */
	
	public static Map<World, HashSet<TileField>> fieldMap = new HashMap<World, HashSet<TileField>>();
	
	
	public void sendTileEvent(TileEntity tile, int id, int argument, boolean limited)
	{
		int maxDistance = limited ? 400 : (MinecraftServer.getServer().getConfigurationManager().getEntityViewDistance() + 16);
		World world = tile.getWorldObj();
		IPacket packet = null;
		for(Object obj : world.playerEntities)
		{
			EntityPlayerMP mp = (EntityPlayerMP)obj;
			int distanceX = tile.xCoord - (int)(mp).posX;
			int distanceZ = tile.zCoord - (int)(mp).posZ;
			int distance;
			if(limited)
			{
				distance = distanceX * distanceX + distanceZ * distanceZ;
			}
			else
			{
				distance = Math.max(Math.abs(distanceX), Math.abs(distanceZ));
			}
			if(distance <= maxDistance)
			{
				if(packet == null)
				{
					packet = new PacketServerTileEvent(tile, id, argument);
				}
				PacketManager.instance.sendToPlayer(packet, mp);
			}
		}
	}
	
	public void sendClientEventUpdate(TileEntity par1, int eventID, int argument) {
		
	}
	
	public void loadTileInitData(INetworkDataTile par1) {
		
	}

	public void updateTileEntityField(TileEntity tile, String field) {
		if(!fieldMap.containsKey(tile.getWorldObj()))
		{
			fieldMap.put(tile.getWorldObj(), new HashSet<TileField>());
		}
		Set<TileField> fields = fieldMap.get(tile.getWorldObj());
		fields.add(new TileField(tile, field));
		if(fields.size() > 10000)
		{
			updateFields(tile.getWorldObj());
		}
	}
	
	//Called Every Tick!
	public void updateFields(World world)
	{
		if(!fieldMap.containsKey(world))
		{
			return;
		}
		Set<TileField> fields = fieldMap.get(world);
		if(fields.isEmpty())
		{
			return;
		}
		for(Object obj : world.playerEntities)
		{
			EntityPlayerMP entityPlayer = (EntityPlayerMP)obj;
			PacketManager.instance.sendToPlayer(new PacketFieldUpdate(createPacketData(world, entityPlayer, fields), world), entityPlayer);
		}
		fields.clear();
	}
	
	public List<FieldData> createPacketData(World world, EntityPlayer par1, Set<TileField> fields)
	{
		List<FieldData> data = new ArrayList<FieldData>();
		for(TileField field : fields)
		{
			if(field.isValid(world, par1))
			{
				data.add(field.toFieldData());
			}
		}
		return data;
	}
}
