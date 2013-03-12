package rictas.energy;

import net.minecraftforge.common.ForgeDirection;

public interface IEnergyTransmitter extends ISideConnections {

	public int maxOutput(ForgeDirection side);
	public boolean transmittEnergy(int amount, ForgeDirection side);
}
