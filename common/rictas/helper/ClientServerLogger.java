package rictas.helper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientServerLogger {

	public static void addLog(String msg) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			System.out.println("[SERVER] "+ msg);
		} else if (side == Side.CLIENT) {
			System.out.println("[CLIENT] "+ msg);
		}
	}
}
