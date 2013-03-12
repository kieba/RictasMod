package rictas.energy;

import net.minecraftforge.common.ForgeDirection;

public interface IGuiPriorityControl {

	public int getPriority(ForgeDirection side);
	public void increasePrio(ForgeDirection side);
	public void decreasePrio(ForgeDirection side);
	public boolean hasPrioControl();

}
