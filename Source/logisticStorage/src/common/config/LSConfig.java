package logisticStorage.src.common.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class LSConfig {
	
	public static Configuration config;
	
	public static void loadConfig(File file)
	{
		config = new Configuration(file);
		try {
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			config.save();
		}
		
		//Utility/FluidLoad
		
		//BlockLoad
		
		//ItemLoad
	}
	
}
