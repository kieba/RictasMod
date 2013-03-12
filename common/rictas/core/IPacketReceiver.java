package rictas.core;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public interface IPacketReceiver {

	public void receiveData(ByteArrayDataInput data, Player playerEntity, boolean isClient);	
}
