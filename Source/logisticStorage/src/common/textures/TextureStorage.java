package logisticStorage.src.common.textures;

import java.util.ArrayList;
import java.util.List;

import logisticStorage.src.common.textures.icons.ITextureRequester;
import logisticStorage.src.common.textures.icons.SpriteIcon;
import logisticStorage.src.common.textures.util.TextureInfo;
import logisticStorage.src.common.textures.util.TexturePosition;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TextureStorage
{
	private static TextureStorage storage;
	
	public TextureStorage()
	{
		MinecraftForge.EVENT_BUS.register(this);
		currentSide = FMLCommonHandler.instance().getEffectiveSide();
	}
	
	public static TextureStorage getStorage()
	{
		if(storage == null)
		{
			storage = new TextureStorage();
		}
		return storage;
	}	
	List<RequestData> delayedThings = new ArrayList<RequestData>();
	
	public final Side currentSide;
	
	String currentMod = "";
	String currentPath = "";
	
	//Loading Engine
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void preIconLoad(TextureStitchEvent.Pre evt)
	{
		loadDelayedTextures();
		for(StoragePart part : StoragePart.values())
		{
			if(part.textureType == evt.map.getTextureType())
			{
				part.getPart().loadIcons(evt.map);
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void postIconLoad(TextureStitchEvent.Post evt)
	{
		SpriteIcon.images.clear();
		for(StoragePart part : StoragePart.values())
		{
			if(part.textureType == evt.map.getTextureType())
			{
				part.getPart().onIconPostLoad(evt.map);
			}
		}
	}
	
	//Helper functions
	private void loadDelayedTextures()
	{
		for(int i = 0;i<delayedThings.size();i++)
		{
			RequestData data = delayedThings.get(i);
			if(data.data.onTextureLoading(this))
			{
				delayedThings.remove(i--);
			}
		}
	}
		
	//Mod and Path Registry
	public void setCurrentMod(String modID)
	{
		if(currentMod != "")
		{
			finishMod();
		}
		currentMod = modID;
	}
	
	public void finishMod()
	{
		currentMod = "";
	}
	
	public void setTexturePath(String path)
	{
		if(currentPath != "")
		{
			finishPath();
		}
		currentPath = path+"/";
	}
	
	public void finishPath()
	{
		currentPath = "";
	}
	
	String getCurrentMod()
	{
		return currentMod;
	}
	
	String getCurrentPath()
	{
		return currentPath;
	}
	
	public ResourceLocation createPath(String name)
	{
		return new ResourceLocation(currentMod, currentPath+name+".png");
	}
	
	//IIcon accessing
	
	public IIcon[] getTexture(Object par1, String key)
	{
		StoragePart part = StoragePart.getStoragePart(par1);
		if(part != null)
		{
			return part.getPart().getTexture(key);
		}
		return new IIcon[]{getMissingTexture()};
	}
	
	public IIcon getTexture(Object par1, String par2, int state)
	{
		IIcon[] textures = getTexture(par1, par2);
		if(textures.length > state)
		{
			return textures[state];
		}
		return getMissingTexture();
	}
	
	public IIcon getIcon(Object par1, String key)
	{
		return getTexture(par1, key, 0);
	}
	
	public IIcon getMissingTexture(Object par1)
	{
		StoragePart part = StoragePart.getStoragePart(par1);
		if(part != null)
		{
			return part.getPart().getMissingTexture();
		}
		return getMissingTexture();
	}
	
	private IIcon getMissingTexture()
	{
		return StoragePart.Blocks.getPart().getMissingTexture();
	}
	
	//Texture Registry
	public void registerTexture(Object par1, String key, String...textures)
	{
		if(currentSide == Side.SERVER)
		{
			return;
		}
		StoragePart part = StoragePart.getStoragePart(par1);
		if(part == null)
		{
			FMLLog.getLogger().info("Invalid Object wanted to register an Texture: "+par1);
			return;
		}
		part.getPart().registerTexture(key, textures);
	}
	
	public void registerInfo(Object par1, String key, TextureInfo info)
	{
		if(currentSide == Side.SERVER)
		{
			return;
		}
		StoragePart part = StoragePart.getStoragePart(par1);
		if(part == null)
		{
			FMLLog.getLogger().info("Invalid Object wanted to register an Texture Info: "+par1);
			return;
		}
		part.getPart().registerInfo(key, info);
	}
	
	public void registerPosInfo(Object par1, String key, TexturePosition pos)
	{
		if(currentSide == Side.SERVER)
		{
			return;
		}
		StoragePart part = StoragePart.getStoragePart(par1);
		if(part == null)
		{
			FMLLog.getLogger().info("Invalid Object wanted to register an Texture Position: "+par1);
			return;
		}
		part.getPart().registerPosition(key, pos);
	}
	
	//Delayed Registration
	public void registerDelayedTextureLoader(ITextureRequester data)
	{
		RequestData req = new RequestData();
		req.data = data;
		delayedThings.add(req);
	}
	
	public static class RequestData
	{
		ITextureRequester data;
	}
	
	public enum StoragePart
	{
		Blocks(0, Block.class, new TextureStoragePart<Block>()),
		Items(1, Item.class, new TextureStoragePart<Item>()),
		ItemBlocks(0, Item.class, new TextureStoragePart<Item>());
		
		final int textureType;
		final Class<?> castClz;
		final TextureStoragePart part;
		private StoragePart(int par1, Class<?> par2, TextureStoragePart par3)
		{
			textureType = par1;
			castClz = par2;
			part = par3;
			part.textureType = textureType;
		}
		
		public TextureStoragePart getPart()
		{
			return part;
		}
		
		private boolean matches(Object par1)
		{
			if(!castClz.isAssignableFrom(par1.getClass()))
			{
				return false;
			}
			return textureType == getTypeFromObj(par1);
		}
		
		private int getTypeFromObj(Object par1)
		{
			if(par1 instanceof Block) return 0;
			if(par1 instanceof Item) return ((Item)par1).getSpriteNumber();
			return -1;
		}
		
		public static StoragePart getStoragePart(Object par1)
		{
			for(StoragePart part : StoragePart.values())
			{
				if(part.matches(par1))
				{
					return part;
				}
			}
			return null;
		}
	}
}