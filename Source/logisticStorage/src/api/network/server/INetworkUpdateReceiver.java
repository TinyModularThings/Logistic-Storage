package logisticStorage.src.api.network.server;

public interface INetworkUpdateReceiver extends INetworkDataTile {
	
	/**
	 * get called when a field was updated
	 */
	public void onFieldUpdate(String fieldName);
}
