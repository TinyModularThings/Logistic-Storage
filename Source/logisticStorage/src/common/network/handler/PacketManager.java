package logisticStorage.src.common.network.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.EnumMap;

import logisticStorage.src.LogisticStorage;
import logisticStorage.src.common.network.packets.IPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Sharable
public class PacketManager extends SimpleChannelInboundHandler<IPacket> {
	
	private EnumMap<Side, FMLEmbeddedChannel> channel;
	public static PacketManager instance = new PacketManager();
	
	
	public void init()
	{
		channel = NetworkRegistry.INSTANCE.newChannel("Logistic Storage", new ChannelManager(), this);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception {
		try
		{
			INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
			EntityPlayer player = getPlayer(netHandler);
			if(player == null)
			{
				throw new RuntimeException("A case which never should happen did happen please Send a message to the ModAuthor and tell him how that Happend!");
			}
			msg.execute(player);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public EntityPlayer getPlayer(INetHandler handler)
	{
		if(handler instanceof NetHandlerPlayServer)
		{
			return ((NetHandlerPlayServer)handler).playerEntity;
		}
		return LogisticStorage.core.get().getPlayer();
	}
	
	public void sendToPlayer(IPacket par1, EntityPlayer par2)
	{
		channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(par2);
		channel.get(Side.SERVER).writeOutbound(par1);
	}
	
	public void sendToServer(IPacket par1)
	{
		channel.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.TOSERVER);
		channel.get(Side.CLIENT).writeOutbound(par1);
	}
}
