package rictas.energy;


import java.io.IOException;

import rictas.core.PacketTileEntity;
import rictas.helper.ModIDs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class TileEntityEnergyCable extends TileEntityEnergyBase implements IWalls {
	
    /*
    Bit[0] = DOWN(0, -1, 0), 
    [1] = UP(0, 1, 0),
    [2] = NORTH(0, 0, -1),
    [3] = SOUTH(0, 0, 1),
    [4] = WEST(-1, 0, 0),
    [5] = EAST(1, 0, 0),
    */
	private int connectedSides = 0x00;
	protected int[] maxCapacity = new int[] {50, 100, 200, 500};
	protected WallLogic wallLogic = new WallLogic(this);
	
	public TileEntityEnergyCable() {
		outputSides = 0xFF;
		inputSides = 0xFF;
	}
	
	@Override
	protected void metadataInit() {
		maxStorage = maxCapacity[this.getBlockMetadata()];
		this.maxInput = maxStorage;
		this.maxOutput = maxStorage;
		for(int i = 0; i< 6; i++) maxOutputPerSide[i] = maxOutput;
		onNeighborBlockChange();
	}

	@Override
	protected void update() {
		updateEnergy();
	}

	public int getConnectedSides() {
		return connectedSides;
	}
	
	
	
	@Override
	public void onNeighborBlockChange() {
		super.onNeighborBlockChange();
		updateSides();
	}

	protected void updateSides() {
		connectedSides = 0x00;
		for(int i = 0; i<6; i++) {
			int con = connectTo(ForgeDirection.getOrientation(i));
			if(con==0) {
				connectedSides |= 0x01 << i;
			} else if(con==1) {
				connectedSides |= 0x01 << i;
				connectedSides |= 0x01 << (i + 7);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		connectedSides = par1nbtTagCompound.getInteger("connectedSides");
		wallLogic.readFromNBT(par1nbtTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("connectedSides", connectedSides);
		wallLogic.writeToNBT(par1nbtTagCompound);
	}

	@Override
	protected PacketTileEntity createClientUpdateData(PacketTileEntity packet) throws IOException {
		packet.dos.writeInt(connectedSides);
		packet = wallLogic.createClientUpdateData(packet);
		return super.createClientUpdateData(packet);
	}

	@Override
	protected void receiveClientUpdateData(ByteArrayDataInput data) {
		this.connectedSides = data.readInt();
		wallLogic.receiveClientUpdateData(data);
		super.receiveClientUpdateData(data);
	}

	@Override
	protected boolean handleClientData(int cmdId, ByteArrayDataInput data) {
		return false; //Not used
	}

	@Override
	protected boolean handleServerData(int cmdId, ByteArrayDataInput data, Player playerEntity) {
		if(cmdId == PacketTileEntity.WALL_CONTROL) {
			wallLogic.handleGuiWallInput(this, data);
			updateSides();
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, ModIDs.blockEnergyCableSided);
			sendClientUpdatePacket();
			return true;
		}
		return false;
	}
	
	/**
	 * @return -1 for false, 0 for true and 1 for connection with adapter
	 */
	public int connectTo(ForgeDirection side) {
		int x = xCoord + side.offsetX;
		int y = yCoord + side.offsetY;
		int z = zCoord + side.offsetZ;
		TileEntity tileSide = worldObj.getBlockTileEntity(x,y,z);
		if(tileSide != null && tileSide instanceof ISideConnections) {
			ConType conType = ((ISideConnections)tileSide).getConType(side.getOpposite());
			if(conType == ConType.NONE || this.getConType(side) == ConType.NONE)
				return -1;
			if(this.getConType(side) == conType)  {
				return 0;
			} else {
				return 1;
			}
		}
		return -1;
	}

	@Override
	public ConType getConType(ForgeDirection side) {
		if(((outputSides >> side.ordinal()) & 0x01) == 0x00)
			return ConType.NONE;
		return ConType.values()[blockMetadata+2];
	}

	@Override
	public SideType getSideType(ForgeDirection side) {
		if(((outputSides >> side.ordinal()) & 0x01) == 0x00)
			return SideType.NORMAL_SIDE;
		return SideType.IN_OUTPUT_SIDE;
	}	
	
	@Override
	public boolean isMachine() {
		return false;
	}
	
	@Override
	public void enableWall(int side, boolean cutConnection) {
		int mask = ~(0x01 << side);
		if(cutConnection) {
			inputSides &= mask;
			outputSides &= mask;
		}
	}
	
	@Override
	public void disableWall(int side) {
		int mask = (0x01 << side);
		inputSides |= mask;
		outputSides |= mask;
	}

	@Override
	public WallLogic getWallLogic() {
		return wallLogic;
	}
}
