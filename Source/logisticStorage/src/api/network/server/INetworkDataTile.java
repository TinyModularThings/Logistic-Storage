package logisticStorage.src.api.network.server;

import java.util.List;

public interface INetworkDataTile {
	
	/**
	 * return all fields which should be synced
	 */
	public List<String> getUpdateFields();
}
