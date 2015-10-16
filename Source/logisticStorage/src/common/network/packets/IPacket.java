package logisticStorage.src.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;

public interface IPacket {
	
	/**
	 * Reading function when a packet gets received
	 * @param stream the stream that provides the data
	 */
	public void read(ByteBuf stream);
	
	/**
	 * Writing function for the packet that gets Send
	 * @param stream that sends the data over to the other side
	 */
	public void write(ByteBuf stream);
	
	/**
	 * Function where the packet executes his work. Do not execute it before!
	 * @param player Player who send/received the Packet
	 */
	public void execute(EntityPlayer player);
}
