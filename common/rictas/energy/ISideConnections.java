package rictas.energy;

import net.minecraftforge.common.ForgeDirection;

public interface ISideConnections {
	
	public enum ConType {
		NONE,
		MACHINE,
		CABLE_LOW,
		CABLE_MEDIUM,
		CABLE_HIGH,
		CABLE_VERY_HIGH,
		CABLE_SUPRA
	}
	
	public enum SideType {
		OUTPUT_SIDE,
		INPUT_SIDE,
		IN_OUTPUT_SIDE,
		NORMAL_SIDE;
	}
	
	public ConType getConType(ForgeDirection side);
	public SideType getSideType(ForgeDirection side);
	
}
