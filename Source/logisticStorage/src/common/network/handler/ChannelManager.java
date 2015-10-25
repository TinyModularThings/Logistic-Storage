package logisticStorage.src.common.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import logisticStorage.src.common.network.packets.IPacket;
import logisticStorage.src.common.network.packets.client.PacketClientTileEvent;
import logisticStorage.src.common.network.packets.client.PacketFieldRequest;
import logisticStorage.src.common.network.packets.server.PacketFieldUpdate;
import logisticStorage.src.common.network.packets.server.PacketServerTileEvent;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

@Sharable
public class ChannelManager extends FMLIndexedMessageToMessageCodec<IPacket>{
	
	private static int packetCounter = 0;
	
	public ChannelManager() {
		//Register your packets here
		
		//client side
		registerPacket(PacketClientTileEvent.class);
		registerPacket(PacketFieldRequest.class);
		//server Side
		registerPacket(PacketFieldUpdate.class);
		registerPacket(PacketServerTileEvent.class);
		
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
		msg.write(target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg) {
		msg.read(source);
	}
	
	private void registerPacket(Class<? extends IPacket> par1)
	{
		this.addDiscriminator(packetCounter++, par1);
	}
	
}
