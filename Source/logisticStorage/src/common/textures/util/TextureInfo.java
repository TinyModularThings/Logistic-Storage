package logisticStorage.src.common.textures.util;

import net.minecraft.util.ResourceLocation;

public class TextureInfo
{
	int maxX;
	int maxY;
	ResourceLocation texture;
	
	public TextureInfo(int maxX, int maxY, ResourceLocation texture)
	{
		this.maxX = maxX;
		this.maxY = maxY;
		this.texture = texture;
	}
	
	public int getMaxX()
	{
		return maxX;
	}
	
	public int getMaxY()
	{
		return maxY;
	}
	
	public ResourceLocation getTexture()
	{
		return texture;
	}
}
