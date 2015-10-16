package logisticStorage.src.common.network.handler;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileField {
	TileEntity tile;
	EntityPlayerMP target;
	String field;
	
	public TileField(TileEntity tile, String field, EntityPlayerMP mp) {
		this(tile, field);
		target = mp;
	}
	
	public TileField(TileEntity tile, String field) {
		this.tile = tile;
		this.field = field;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof TileField)
		{
			TileField tef = (TileField)obj;
			return tef.tile == this.tile && tef.field.equals(this.field);
		}
		return false;
	}
	
	public boolean isValid(World world, EntityPlayer player)
	{
		if(tile.isInvalid())
		{
			return false;
		}
		if(tile.getWorldObj() == world)
		{
			if(target == null || target == player)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.tile.hashCode() * 31 ^ this.field.hashCode();
	}
	
	public Object getData()
	{
		try
		{
			Field targetfield = null;
			Class fieldDeclaringClass = tile.getClass();
			do
			{
				try
				{
					targetfield = fieldDeclaringClass.getDeclaredField(field);
				}
				catch(NoSuchFieldException e3)
				{
					fieldDeclaringClass = fieldDeclaringClass.getSuperclass();
				}
			}
			while(targetfield == null && fieldDeclaringClass != null);
			if(targetfield == null)
			{
				throw new NoSuchFieldException(field);
			}
			targetfield.setAccessible(true);
			return targetfield.get(tile);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public FieldData toFieldData()
	{
		FieldData data = new FieldData();
		data.x = tile.xCoord;
		data.y = tile.yCoord;
		data.z = tile.zCoord;
		data.field = field;
		data.data = getData();
		return data;
	}
	
	public static class FieldData
	{
		int x;
		int y;
		int z;
		String field;
		Object data;
		
		public Object getData() {
			return data;
		}
		
		public String getField() {
			return field;
		}
		
		public ChunkCoordinates getCoords()
		{
			return new ChunkCoordinates(x, y, z);
		}
	}
}
