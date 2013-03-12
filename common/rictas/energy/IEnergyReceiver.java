package rictas.energy;

import net.minecraftforge.common.ForgeDirection;

public interface IEnergyReceiver extends ISideConnections {

	public boolean receiveEnergy(int amount, ForgeDirection side);
	public int maxInput(ForgeDirection side);
	
}
