package rictas.energy;

import rictas.energy.ISideConnections.SideType;
import net.minecraftforge.common.ForgeDirection;

public interface IGuiSideControl {

	public SideType getSideType(ForgeDirection side);
	public void changeSide(ForgeDirection side);
	public boolean hasSideControl();
	
}
