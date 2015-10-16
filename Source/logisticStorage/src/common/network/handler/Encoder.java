package logisticStorage.src.common.network.handler;

import io.netty.buffer.ByteBuf;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import logisticStorage.src.LogisticStorage;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.mojang.authlib.GameProfile;

public class Encoder {
	
	public static void encode(ByteBuf buffer, Object obj)
	{
		if(obj == null)
		{
			buffer.writeByte(-1);
		}
		else if(obj instanceof Integer)
		{
			buffer.writeByte(0);
			buffer.writeByte(0);
			buffer.writeInt((Integer)obj);
		}
		else if(obj instanceof Short)
		{
			buffer.writeByte(0);
			buffer.writeByte(1);
			buffer.writeShort((Short)obj);
		}
		else if(obj instanceof Long)
		{
			buffer.writeByte(0);
			buffer.writeByte(2);
			buffer.writeLong((Long)obj);
		}
		else if(obj instanceof Float)
		{
			buffer.writeByte(0);
			buffer.writeByte(3);
			buffer.writeFloat((Float)obj);
		}
		else if(obj instanceof Double)
		{
			buffer.writeByte(0);
			buffer.writeByte(4);
			buffer.writeDouble((Double)obj);
		}
		else if(obj instanceof Byte)
		{
			buffer.writeByte(0);
			buffer.writeByte(5);
			buffer.writeByte((Byte)obj);
		}
		else if(obj instanceof Boolean)
		{
			buffer.writeByte(0);
			buffer.writeByte(6);
			buffer.writeBoolean((Boolean)obj);
		}
		else if(obj instanceof String)
		{
			buffer.writeByte(0);
			buffer.writeByte(7);
			byte[] text = ((String)obj).getBytes();
			buffer.writeInt(text.length);
			buffer.writeBytes(text);
		}
		else if(obj instanceof int[])
		{
			buffer.writeByte(1);
			buffer.writeByte(0);
			int[] array = (int[])obj;
			buffer.writeInt(array.length);
			for(int i = 0;i<array.length;i++)
			{
				buffer.writeInt(array[i]);
			}
		}
		else if(obj instanceof short[])
		{
			buffer.writeByte(1);
			buffer.writeByte(1);
			short[] array = (short[])obj;
			buffer.writeInt(array.length);
			for(int i = 0;i<array.length;i++)
			{
				buffer.writeShort(array[i]);
			}
		}
		else if(obj instanceof long[])
		{
			buffer.writeByte(1);
			buffer.writeByte(2);
			long[] array = (long[])obj;
			buffer.writeInt(array.length);
			for(int i = 0;i<array.length;i++)
			{
				buffer.writeLong(array[i]);
			}
		}
		else if(obj instanceof float[])
		{
			buffer.writeByte(1);
			buffer.writeByte(3);
			float[] array = (float[])obj;
			buffer.writeInt(array.length);
			for(int i = 0;i<array.length;i++)
			{
				buffer.writeFloat(array[i]);
			}
		}
		else if(obj instanceof double[])
		{
			buffer.writeByte(1);
			buffer.writeByte(4);
			double[] array = (double[])obj;
			buffer.writeInt(array.length);
			for(int i = 0;i<array.length;i++)
			{
				buffer.writeDouble(array[i]);
			}
		}
		else if(obj instanceof byte[])
		{
			buffer.writeByte(1);
			buffer.writeByte(5);
			byte[] data = (byte[])obj;
			buffer.writeInt(data.length);
			buffer.writeBytes(data);
		}
		else if(obj instanceof boolean[])
		{
			buffer.writeByte(1);
			buffer.writeByte(6);
			boolean[] array = (boolean[])obj;
			buffer.writeInt(array.length);
			for(int i = 0;i<array.length;i++)
			{
				buffer.writeBoolean(array[i]);
			}
		}
		else if(obj instanceof String[])
		{
			buffer.writeByte(1);
			buffer.writeByte(7);
			String[] array = (String[])obj;
			buffer.writeInt(array.length);
			for(int i = 0;i<array.length;i++)
			{
				byte[] text = array[i].getBytes();
				buffer.writeInt(text.length);
				buffer.writeBytes(text);
			}
		}
		else if(obj instanceof ItemStack)
		{
			buffer.writeByte(2);
			buffer.writeByte(0);
			ItemStack item = (ItemStack)obj;
			int id = Item.getIdFromItem(item.getItem());
			buffer.writeInt(id);
			if(id <= 0)
			{
				return;
			}
			buffer.writeInt(item.stackSize);
			buffer.writeInt(item.getItemDamage());
			boolean nbt = item.hasTagCompound();
			buffer.writeBoolean(nbt);
			if(nbt)
			{
				NBTTagCompound nbtData = item.getTagCompound();
				if(nbtData != null)
				{
					byte[] streamBytes = getBytesFromNBT(nbtData);
					buffer.writeInt(streamBytes.length);
					buffer.writeBytes(streamBytes);
				}
			}
		}
		else if(obj instanceof Item)
		{
			buffer.writeByte(2);
			buffer.writeByte(1);
			buffer.writeInt(Item.getIdFromItem((Item)obj));
		}
		else if(obj instanceof Block)
		{
			buffer.writeByte(2);
			buffer.writeByte(2);
			buffer.writeInt(Block.getIdFromBlock((Block)obj));
		}
		else if(obj instanceof Enchantment)
		{
			buffer.writeByte(2);
			buffer.writeByte(3);
			buffer.writeInt(((Enchantment)obj).effectId);
		}
		else if(obj instanceof Potion)
		{
			buffer.writeByte(2);
			buffer.writeByte(4);
			buffer.writeInt(((Potion)obj).getId());
		}
		else if(obj instanceof Achievement)
		{
			buffer.writeByte(2);
			buffer.writeByte(5);
			byte[] data = ((StatBase)obj).statId.getBytes();
			buffer.writeInt(data.length);
			buffer.writeBytes(data);
		}
		else if(obj instanceof NBTTagCompound)
		{
			buffer.writeByte(3);
			byte[] array = getBytesFromNBT((NBTTagCompound)obj);
			buffer.writeInt(array.length);
			buffer.writeBytes(array);
		}
		else if(obj instanceof ChunkCoordinates)
		{
			buffer.writeByte(4);
			buffer.writeByte(0);
			ChunkCoordinates coords = (ChunkCoordinates)obj;
			buffer.writeInt(coords.posX);
			buffer.writeInt(coords.posY);
			buffer.writeInt(coords.posZ);
		}
		else if(obj instanceof ChunkCoordIntPair)
		{
			buffer.writeByte(4);
			buffer.writeByte(1);
			ChunkCoordIntPair coords = (ChunkCoordIntPair)obj;
			buffer.writeInt(coords.chunkXPos);
			buffer.writeInt(coords.chunkZPos);
		}
		else if(obj instanceof ChunkPosition)
		{
			buffer.writeByte(4);
			buffer.writeByte(2);
			ChunkPosition pos = (ChunkPosition)obj;
			buffer.writeInt(pos.chunkPosX);
			buffer.writeInt(pos.chunkPosY);
			buffer.writeInt(pos.chunkPosZ);
		}
		else if(obj instanceof TileEntity)
		{
			buffer.writeByte(4);
			buffer.writeByte(3);
			TileEntity tile = (TileEntity)obj;
			buffer.writeInt(tile.getWorldObj().provider.dimensionId);
			buffer.writeInt(tile.xCoord);
			buffer.writeInt(tile.yCoord);
			buffer.writeInt(tile.zCoord);
		}
		else if(obj instanceof World)
		{
			buffer.writeByte(4);
			buffer.writeByte(4);
			buffer.writeInt(((World)obj).provider.dimensionId);
		}
		else if(obj instanceof FluidStack)
		{
			buffer.writeByte(5);
			buffer.writeByte(0);
			FluidStack fluid = (FluidStack)obj;
			buffer.writeInt(fluid.getFluidID());
			buffer.writeInt(fluid.amount);
			NBTTagCompound nbt = fluid.tag;
			buffer.writeBoolean(nbt != null);
			if(nbt != null)
			{
				byte[] data = getBytesFromNBT(nbt);
				buffer.writeInt(data.length);
				buffer.writeBytes(data);
			}
		}
		else if(obj instanceof FluidTank)
		{
			buffer.writeByte(5);
			buffer.writeByte(1);
			FluidTank tank = (FluidTank)obj;
			boolean empty = tank.getFluid() == null;
			buffer.writeBoolean(empty);
			if(empty)
			{
				buffer.writeInt(tank.getCapacity());
			}
			else
			{
				encode(buffer, tank.getFluid());
				buffer.writeInt(tank.getCapacity());
			}
		}
		else if(obj instanceof UUID)
		{
			buffer.writeByte(6);
			buffer.writeByte(0);
			UUID id = (UUID)obj;
			buffer.writeLong(id.getMostSignificantBits());
			buffer.writeLong(id.getLeastSignificantBits());
		}
		else if(obj instanceof GameProfile)
		{
			buffer.writeByte(6);
			buffer.writeByte(1);
			GameProfile profile = (GameProfile)obj;
			encode(buffer, profile.getId());
			byte[] array = profile.getName().getBytes();
			buffer.writeInt(array.length);
			buffer.writeBytes(array);
		}
		else if(obj instanceof Enum)
		{
			buffer.writeByte(7);
			buffer.writeInt(((Enum)obj).ordinal());
		}
		else if(obj instanceof ResourceLocation)
		{
			buffer.writeByte(8);
			byte[] bytes = ((ResourceLocation)obj).toString().getBytes();
			buffer.writeInt(bytes.length);
			buffer.writeBytes(bytes);
		}
	}
	
	public static Object decode(ByteBuf buffer)
	{
		switch(buffer.readByte())
		{
			case -1: return null;
			case 0:
			{
				switch(buffer.readByte())
				{
					case 0: return buffer.readInt();
					case 1: return buffer.readShort();
					case 2: return buffer.readLong();
					case 3: return buffer.readFloat();
					case 4: return buffer.readDouble();
					case 5: return buffer.readByte();
					case 6: return buffer.readBoolean();
					case 7:
					{
						byte[] text = new byte[buffer.readInt()];
						buffer.readBytes(text);
						return new String(text);
					}
					default: return null;
				}
			}
			case 1:
			{
				switch(buffer.readByte())
				{
					case 0:
					{
						int lenght = buffer.readInt();
						int[] array = new int[lenght];
						for(int i = 0;i<lenght;i++)
						{
							array[i] = buffer.readInt();
						}
						return array;
					}
					case 1:
					{
						int lenght = buffer.readInt();
						short[] array = new short[lenght];
						for(int i = 0;i<lenght;i++)
						{
							array[i] = buffer.readShort();
						}
						return array;
					}
					case 2:
					{
						int lenght = buffer.readInt();
						long[] array = new long[lenght];
						for(int i = 0;i<lenght;i++)
						{
							array[i] = buffer.readLong();
						}
						return array;
					}
					case 3:
					{
						int lenght = buffer.readInt();
						float[] array = new float[lenght];
						for(int i = 0;i<lenght;i++)
						{
							array[i] = buffer.readFloat();
						}
						return array;
					}
					case 4:
					{
						int lenght = buffer.readInt();
						double[] array = new double[lenght];
						for(int i = 0;i<lenght;i++)
						{
							array[i] = buffer.readDouble();
						}
						return array;
					}
					case 5:
					{
						int lenght = buffer.readInt();
						byte[] array = new byte[lenght];
						buffer.readBytes(array);
						return array;
					}
					case 6:
					{
						int lenght = buffer.readInt();
						boolean[] array = new boolean[lenght];
						for(int i = 0;i<lenght;i++)
						{
							array[i] = buffer.readBoolean();
						}
						return array;
					}
					case 7:
					{
						int lenght = buffer.readInt();
						String[] array = new String[lenght];
						for(int i = 0;i<lenght;i++)
						{
							byte[] text = new byte[buffer.readInt()];
							buffer.readBytes(text);
							array[i] = new String(text);
						}
						return array;
					}
					default: return null;
				}
			}
			case 2:
			{
				switch(buffer.readByte())
				{
					case 0:
					{
						int id = buffer.readInt();
						if(id <= 0)
						{
							return null;
						}
						int stacksize = buffer.readInt();
						int meta = buffer.readInt();
						ItemStack stack = new ItemStack(Item.getItemById(id), stacksize, meta);
						boolean nbt = buffer.readBoolean();
						if(nbt)
						{
							byte[] bytes = new byte[buffer.readInt()];
							buffer.readBytes(bytes);
							stack.setTagCompound(getNBTFromBytes(bytes));
						}
						return stack;
					}
					case 1: return Item.getItemById(buffer.readInt());
					case 2: return Block.getBlockById(buffer.readInt());
					case 3: return Enchantment.enchantmentsList[buffer.readInt()];
					case 4: return Potion.potionTypes[buffer.readInt()];
					case 5:
					{
						byte[] text = new byte[buffer.readInt()];
						buffer.readBytes(text);
						return StatList.func_151177_a(new String(text));
					}
					default: return null;
				}
			}
			case 3:
			{
				byte[] data = new byte[buffer.readInt()];
				buffer.readBytes(data);
				return getNBTFromBytes(data);
			}
			case 4:
			{
				switch(buffer.readByte())
				{
					case 0: return new ChunkCoordinates(buffer.readInt(), buffer.readInt(), buffer.readInt());
					case 1: return new ChunkCoordIntPair(buffer.readInt(), buffer.readInt());
					case 2: return new ChunkPosition(buffer.readInt(), buffer.readInt(), buffer.readInt());
					case 3:
					{
						World world = LogisticStorage.core.get().getWorld(buffer.readInt());
						if(world == null)
						{
							return null;
						}
						return world.getTileEntity(buffer.readInt(), buffer.readInt(), buffer.readInt());
					}
					case 4: return LogisticStorage.core.get().getWorld(buffer.readInt());
					default: return null;
				}
			}
			case 5:
			{
				switch(buffer.readByte())
				{
					case 0:
					{
						FluidStack fluid = new FluidStack(FluidRegistry.getFluid(buffer.readInt()), buffer.readInt());
						if(buffer.readBoolean())
						{
							byte[] data = new byte[buffer.readInt()];
							buffer.readBytes(data);
							fluid.tag = getNBTFromBytes(data);
						}
						return fluid;
					}
					case 1:
					{
						boolean empty = buffer.readBoolean();
						if(empty)
						{
							return new FluidTank(buffer.readInt());
						}
						else
						{
							return new FluidTank((FluidStack)decode(buffer), buffer.readInt());
						}
					}
					default: return null;
				}
			}
			case 6:
			{
				switch(buffer.readByte())
				{
					case 0: return new UUID(buffer.readLong(), buffer.readLong());
					case 1:
					{
						UUID id = (UUID)decode(buffer);
						byte[] array = new byte[buffer.readInt()];
						buffer.readBytes(array);
						return new GameProfile(id, new String(array));
					}
					default: return null;
				}
			}
			case 7: return buffer.readInt();
			case 8:
			{
				byte[] data = new byte[buffer.readInt()];
				buffer.readBytes(data);
				return new ResourceLocation(new String(data));
			}
		}
		return null;
	}
	
	private static byte[] getBytesFromNBT(NBTTagCompound nbt)
	{
		try
		{
			ByteArrayOutputStream array = new ByteArrayOutputStream();
			DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(array));
			CompressedStreamTools.write(nbt, stream);
			stream.close();
			return array.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	private static NBTTagCompound getNBTFromBytes(byte[] array)
	{
		try
		{
			DataInputStream stream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(array))));
			NBTTagCompound nbt = CompressedStreamTools.read(stream);
			stream.close();
			return nbt;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new NBTTagCompound();
	}
	
}
