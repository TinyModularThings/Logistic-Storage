package logisticStorage.src.common.textures.icons;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import logisticStorage.src.common.textures.util.TextureInfo;
import logisticStorage.src.common.textures.util.TexturePosition;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class SpriteIcon extends TextureAtlasSprite
{
	TextureInfo info;
	TexturePosition.Entry pos;
	
	public SpriteIcon(String name, TextureInfo info, TexturePosition.Entry pos)
	{
		super(name);
		this.info = info;
		this.pos = pos;
	}
	
	
	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
	{
		return true;
	}
	
	@Override
	public boolean load(IResourceManager manager, ResourceLocation location)
	{
		try
		{
			BufferedImage image = getOrCreateImage(manager, info.getTexture());
			width = image.getWidth() / info.getMaxX();
			height = image.getHeight() / info.getMaxY();
			int pos = this.pos.getPosition();
			int x = (pos % info.getMaxX()) * width;
			int y = (pos / info.getMaxX()) * height;
			int[] data = new int[width * height];
			image.getRGB(x, y, width, height, data, 0, width);
	        int[][] mipmaps = new int[FMLClientHandler.instance().getClient().gameSettings.mipmapLevels + 1][];
	        mipmaps[0] = data;
	        this.framesTextureData.add(mipmaps);
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(0, false);
			return true;
		}
	}
	
	public static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	static BufferedImage getOrCreateImage(IResourceManager par1, ResourceLocation path)
	{
		if(images.containsKey(path.toString()))
		{
			return images.get(path.toString());
		}
		try
		{
			InputStream stream = par1.getResource(path).getInputStream();
			if(stream == null)
			{
				throw new RuntimeException("Could not find Texture!");
			}
			BufferedImage image = ImageIO.read(stream);
			images.put(path.toString(), image);
			return image;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static IIcon[] getTextures(TextureMap map, String name, TexturePosition pos, TextureInfo info)
	{
		List<TexturePosition.Entry> data = pos.getEntry(info);
		IIcon[] textures = new IIcon[data.size()];
		for(int i = 0;i<data.size();i++)
		{
			TexturePosition.Entry entry = data.get(i);
			String path = info.getTexture().getResourcePath();
			path = path.substring(0, path.lastIndexOf(".")-1);
			String realName = path+name+entry.getPosition();
			SpriteIcon texture = new SpriteIcon(realName, info, entry);
			map.setTextureEntry(realName, texture);
			textures[i] = texture;
		}
		return textures;
	}
}
