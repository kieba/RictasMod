package rictas.energy;

import net.minecraftforge.common.ForgeDirection;

public interface IGuiOutputControl {

	public int getOutput(ForgeDirection side);
	public void increaseOutput(ForgeDirection side, int amount);
	public void decreaseOutput(ForgeDirection side, int amount);
	public boolean hasOutputControl();
	
}
