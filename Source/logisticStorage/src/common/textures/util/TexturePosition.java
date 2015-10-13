package logisticStorage.src.common.textures.util;

import java.util.ArrayList;
import java.util.List;

public class TexturePosition
{
	int minX;
	int minY;
	int maxX;
	int maxY;
	
	public TexturePosition(int minX, int minY, int maxX, int maxY)
	{
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public List<Entry> getEntry(TextureInfo info)
	{
		List<Entry> list = new ArrayList<Entry>();
		for(int y = minY;y<maxY;y++)
		{
			for(int x = minX;x<maxX;x++)
			{
				list.add(new Entry(x, y, info));
			}
		}
		return list;
	}
	
	public static class Entry
	{
		int position;
		
		public Entry(int x, int y, TextureInfo info)
		{
			this(x + (y * info.getMaxX()));
		}
		
		public Entry(int position)
		{
			this.position = position;
		}
		
		public int getPosition()
		{
			return position;
		}
	}
}
