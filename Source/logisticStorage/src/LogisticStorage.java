package logisticStorage.src;

import logisticStorage.src.common.core.LogisticCore;
import static logisticStorage.src.common.core.LSLib.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = modID, name = name, version = version)
public class LogisticStorage
{
	
	//Mod Access. FML AutoInserts that
	@Instance(modID)
	public static LogisticStorage instance;

	//Mod Core. (Also AutoInserted) and also AutoDetects Server and client
	@SidedProxy(serverSide = serverCore, clientSide = clientCore)
	public static LogisticCore core;
	
	
	//First Call
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{	
		//Config init
		//Block/Item/Fluid/TileEntity(Class) Init
		//GuiHandler Load
	}
	
	//Middle call
	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		//Recipe Init
		//HandlerInit (Player Events and co)
		//Plugin Load
		//Core can handle that by the way
	}
	
	//Almost last call but futher is only if there is no Other way
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		//Mod Support
		//Late PluginLoad
	}
	
	//Always called when server is Stopping but only Server Side!
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		//DataSaveing
	}
	
	//Always called when the Server/Game Starts but only Server Side!
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		//DataLoading
	}
}
