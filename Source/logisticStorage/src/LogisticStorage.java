package logisticStorage.src;

import static logisticStorage.src.common.core.LSLib.clientCore;
import static logisticStorage.src.common.core.LSLib.modID;
import static logisticStorage.src.common.core.LSLib.name;
import static logisticStorage.src.common.core.LSLib.serverCore;
import static logisticStorage.src.common.core.LSLib.version;
import logisticStorage.src.common.core.LogisticCore;
import logisticStorage.src.common.network.NetworkCore;
import logisticStorage.src.common.network.handler.PacketManager;
import logisticStorage.src.common.util.helper.SidedAccess;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
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

	//Mod Core. More Effective way. Automaticly gives you the Access which you need
	public static SidedAccess<LogisticCore> core = new SidedAccess(clientCore, serverCore);
	
	public static SidedAccess<NetworkCore> network = new SidedAccess("logisticStorage.src.common.network.NetworkClient", "logisticStorage.src.common.network.NetworkCore");
	
	//First Call
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{	
		//Config init
		//Block/Item/Fluid/TileEntity(Class) Init
		//GuiHandler Load
		PacketManager.instance.init();
	}
	
	//Middle call
	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		//Recipe Init
		core.get().init();
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
