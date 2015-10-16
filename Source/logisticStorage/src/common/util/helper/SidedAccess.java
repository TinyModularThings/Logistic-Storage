package logisticStorage.src.common.util.helper;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * 
 * @author Speiger
 *
 * @param <T> Class you want to share
 */
public class SidedAccess<T> {
	
	/**
	 * This class simply auto splits the Server and client side
	 * It autoDetects which side is requesting stuff and provides the right side
	 * FML Stuff does that only for 1 Time. If the client starts the game or server
	 * but a client has a internal server so this comes right handy.
	 * It is based on IC2 Exp&Classics System.
	 */
	
	private T client;
	private T server;
	
	public SidedAccess(String clientClass, String serverClass) {
		try {
			if(FMLCommonHandler.instance().getSide().isClient()){
				client = (T)Class.forName(clientClass);
			}
			else {
				client = null;
			}
			server = (T)Class.forName(serverClass).newInstance();
			
		} catch (Exception e) {
		}
	}
	
	
	public T get()
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return client;
		}
		return server;
	}
}
