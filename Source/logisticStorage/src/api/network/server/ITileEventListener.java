package logisticStorage.src.api.network.server;

public interface ITileEventListener {
	
	/**
	 * This is a Tile Event that gets send from Server
	 * to the client.
	 * @param id you defined to call
	 * @param argument if you need one there you have it
	 */
	public void onEvent(int id, int argument);
}
