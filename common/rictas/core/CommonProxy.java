package rictas.core;

import net.minecraft.world.World;

public class CommonProxy {
	
	public static int cableRenderId = -1;
	
	 
	 public void preInit()
	 {
	  registerGuiHandler();
	 }
	 
	 public void init()
	 {
		 
	 }


	 public void registerGuiHandler() {
		 
	 }
	 
	public World getClientWorld() {
		return null;
	}
	 
	
}
