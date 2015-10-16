package logisticStorage.src.common.textures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import logisticStorage.src.common.textures.icons.SpriteIcon;
import logisticStorage.src.common.textures.util.TextureInfo;
import logisticStorage.src.common.textures.util.TexturePosition;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Speiger
 *
 * @param <T> Type of the Textures you want to store..
 */
public class TextureStoragePart<T>
{
	int textureType;
	@SideOnly(Side.CLIENT)
	private TextureMap map;
	
	private IIcon missingTexture;
	
	Map<String, IIcon[]> textures = new HashMap<String, IIcon[]>();
	Map<String, List<String>> textureNames = new HashMap<String, List<String>>();
	Map<String, TexturePosition> textureEntries = new HashMap<String, TexturePosition>();
	Map<String, TextureInfo> textureInfo = new HashMap<String, TextureInfo>();
	
	private TextureStorage engine = TextureStorage.getStorage();
	
	@SideOnly(Side.CLIENT)
	public void loadIcons(TextureMap par1)
	{
		map = par1;
		missingTexture = par1.registerIcon("spmodapi:missingTexture");
		for(Entry<String, List<String>> entry : textureNames.entrySet())
		{
			if(isPresentAsFile(entry.getKey()))
			{
				continue;
			}
			List<String> listedTextureNames = entry.getValue();
			IIcon[] textures = new IIcon[listedTextureNames.size()];
			for(int i = 0;i<listedTextureNames.size();i++)
			{
				textures[i] = map.registerIcon(listedTextureNames.get(i));
			}
			registerIcon(entry.getKey(), textures);
		}
		for(Entry<String, TexturePosition> pos : textureEntries.entrySet())
		{
			TextureInfo info = getInfo(pos.getKey());
			if(info == null)
			{
				continue;
			}
			registerIcon(pos.getKey(), SpriteIcon.getTextures(map, pos.getKey(), pos.getValue(), info));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void onIconPostLoad(TextureMap map)
	{
		for(Entry<String, IIcon[]> entry : textures.entrySet())
		{
			IIcon[] icons = entry.getValue();
			for(int i = 0;i<icons.length;i++)
			{
				if(!isTextureRegistered(icons[i]))
				{
					icons[i] = missingTexture;
				}
			}
			entry.setValue(icons);
		}
	}
	
	public boolean isPresentAsFile(String par1)
	{
		return textureInfo.containsKey(par1);
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isTextureRegistered(IIcon par1)
	{
		IIcon result = map.getAtlasSprite(par1.getIconName());
		if(result.getIconName().equalsIgnoreCase("missingno"))
		{
			return false;
		}
		return true;
	}
	
	private void registerIcon(String par1, IIcon[] par2)
	{
		textures.put(par1, par2);
	}
	
	private TextureInfo getInfo(String par1)
	{
		if(textureInfo.containsKey(par1))
		{
			return textureInfo.get(par1);
		}
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public TextureMap getTextureMap()
	{
		return map;
	}
	
	public int getTextureType()
	{
		return textureType;
	}
	
	public IIcon[] getTexture(String par1)
	{
		if(textures.containsKey(par1))
		{
			return textures.get(par1);
		}
		return new IIcon[]{missingTexture};
	}
	
	public void registerTexture(String par1, String...par2)
	{
		if(!textureNames.containsKey(par1))
		{
			textureNames.put(par1, new ArrayList<String>());
		}
		List<String> list = textureNames.get(par1);
		for(String key : par2)
		{
			list.add(engine.getCurrentMod()+":"+engine.getCurrentPath()+key);
		}
	}
	
	public void registerInfo(String par1, TextureInfo par2)
	{
		textureInfo.put(par1, par2);
	}
	
	public void registerPosition(String par1, TexturePosition par2)
	{
		textureEntries.put(par1, par2);
	}

	public IIcon getMissingTexture()
	{
		return missingTexture;
	}
}