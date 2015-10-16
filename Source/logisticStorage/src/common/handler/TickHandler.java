package logisticStorage.src.common.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logisticStorage.src.LogisticStorage;
import logisticStorage.src.api.utils.ITickNotfiy;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TickHandler {
	
	public static TickHandler instance = new TickHandler();
	
	private static Map<World, List<ITickNotfiy>> ticks = new HashMap<World, List<ITickNotfiy>>();
	private static Map<World, List<ITickNotfiy>> delays = new HashMap<World, List<ITickNotfiy>>();
	private static boolean locked = false;
	
	public void init()
	{
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onTick(WorldTickEvent evt)
	{
		if(evt.phase == Phase.END)
		{
			LogisticStorage.network.get().updateFields(evt.world);
		}
		else
		{
			handleTicks(evt.world);
		}
	}
	
	public void handleTicks(World world)
	{
		if(ticks.containsKey(world))
		{
			locked = true;
			List<ITickNotfiy> notifies = ticks.get(world);
			for(int i = 0;i<notifies.size();i++)
			{
				notifies.get(i).onTick(world);
			}
			notifies.clear();
			locked = false;
		}
		ticks.putAll(delays);
		delays.clear();
	}
	
	public static void registerTick(World world, ITickNotfiy par1)
	{
		if(world == null)
		{
			return;
		}
		if(locked)
		{
			if(!delays.containsKey(world))
			{
				delays.put(world, new ArrayList<ITickNotfiy>());
			}
			delays.get(world).add(par1);
			return;
		}
		if(!ticks.containsKey(world))
		{
			ticks.put(world, new ArrayList<ITickNotfiy>());
		}
		ticks.get(world).add(par1);
	}
	
}
