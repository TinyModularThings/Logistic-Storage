package logisticStorage.src.api.network.client;

import net.minecraft.entity.player.EntityPlayer;

public interface IClientTileEventListener {
	
	/**
	 * Event that gets send from the Client.
	 * @param id that you defined
	 * @param player client who send that event
	 * @param argument extra Argument what could be needed
	 */
	public void onEvent(int id, EntityPlayer player, int argument);
}
