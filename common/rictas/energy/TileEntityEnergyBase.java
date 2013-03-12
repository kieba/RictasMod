package rictas.energy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import rictas.core.PacketTileEntity;
import rictas.core.TileEntityBase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

public abstract class TileEntityEnergyBase extends TileEntityBase implements IEnergyTransmitter, IEnergyReceiver {
	/*
	 * Bit [0] = DOWN(0, -1, 0), Bit [1] = UP(0, 1, 0), Bit [2] = NORTH(0,
	 * 0, -1), Bit [3] = SOUTH(0, 0, 1), Bit [4] = WEST(-1, 0, 0), Bit [5] =
	 * EAST(1, 0, 0),
	 */
	protected int maxStorage = 0;
	protected int storage = 0;

	protected int blockedSides = 0x00;
	protected int inputSides = 0x00;
	protected int outputSides = 0x00;

	protected int maxOutput = 0;
	protected int maxInput = 0;
	
	protected int currentMaxInput;
	
	protected int[] prioPerSide = new int[6];
	protected int[] maxOutputPerSide  = new int[6];

	private int meassureTicks;
	private int meassureTraffic;
	public boolean meassure;
	
	private boolean deadEnd = false;
	private boolean isEnergyFlowing = true;
	
	protected void updateEnergy() {
		if(meassure)
			meassureTicks++;
		TileEntity[] tileSide = new TileEntity[6];
		int[] inputPerSide = new int[6];
		int[] outputPerSide = new int[6];
		int inputAmount = 0;
		int outputAmount = 0;
		for (int i = 0; i < tileSide.length; i++) {
			int x = xCoord + ForgeDirection.getOrientation(i).offsetX;
			int y = yCoord + ForgeDirection.getOrientation(i).offsetY;
			int z = zCoord + ForgeDirection.getOrientation(i).offsetZ;
			tileSide[i] = worldObj.getBlockTileEntity(x, y, z);
		}
		for (int i = 0; i < tileSide.length; i++) {
			if (((blockedSides >> i) & 0x01) == 0x01 || ((outputSides >> i) & 0x01) == 0x00)
				continue;
			if (tileSide[i] instanceof IEnergyReceiver) {
				int o1 = ((IEnergyReceiver) tileSide[i]).maxInput(invertSide(i));
				int o2 = this.maxOutput(ForgeDirection.getOrientation(i));
				outputPerSide[i] = o1 > o2 ? o2 : o1;
				outputAmount += outputPerSide[i];
			}
		}
		if (!deadEnd) {
			// NEW PRIORITY CODE START!!!!!!!!!
			ArrayList<LinkedList<Integer>> tiles = new ArrayList<LinkedList<Integer>>();
			int[] totalOutputPerPrio = new int[6];
			for(int i = 0; i < 6; i++) {
				tiles.add(new LinkedList<Integer>());
			}
			for(int i = 0; i < 6; i++) {
				if(outputPerSide[i] > 0) {
					tiles.get(prioPerSide[i]).add(i);
					totalOutputPerPrio[prioPerSide[i]] += outputPerSide[i];
				}
			}
//			// DEBUGGING Code!!!!!
//			if(meassure) {
//				for(int i = 0; i < 6; i++) {
//					System.out.println("Priority: "+i);
//					for(int a = 0; a < tiles.get(i).size(); a++) {
//						System.out.println("Side: "+tiles.get(i).get(a));
//					}
//				}
//			}
//			// DEBUGGING Code END
			int maxCurrentOutput = maxOutput;
			for(int prio = 5; prio >= 0; prio--) {
				double percent = 0;
				isEnergyFlowing = false;
				if (maxCurrentOutput < storage) {
					if (maxCurrentOutput < totalOutputPerPrio[prio]) {
						percent = (double) maxCurrentOutput / (double) totalOutputPerPrio[prio];
						maxCurrentOutput = 0;
					} else {
						percent = 1;
						maxCurrentOutput -= totalOutputPerPrio[prio];
					}
				} else if (storage < totalOutputPerPrio[prio]) {
					percent = (double) storage / (double) totalOutputPerPrio[prio];
					maxCurrentOutput = 0;
				} else {
					percent = 1;
					maxCurrentOutput -= totalOutputPerPrio[prio];
				}
				for (Integer i : tiles.get(prio)) {
					if (tileSide[i] != null) {
						outputPerSide[i] = (int) (outputPerSide[i] * percent);
						((IEnergyReceiver) tileSide[i]).receiveEnergy(outputPerSide[i], invertSide(i));
						transmittEnergy(outputPerSide[i],ForgeDirection.getOrientation(i));
					}
				}
			}
			// NEW PRIORITY CODE END!!!!!!!!!
			
			/* old energy transfer code without priorities
			double percent = 0;
			isEnergyFlowing = false;
			if (maxOutput < storage)
				if (maxOutput < outputAmount)
					percent = (double) maxOutput / (double) outputAmount;
				else
					percent = 1;
			else if (storage < outputAmount)
				percent = (double) storage / (double) outputAmount;
			else
				percent = 1;
			for (int i = 0; i < 6; i++) {
				if (tileSide[i] != null) {
					outputPerSide[i] = (int) (outputPerSide[i] * percent);
					((IEnergyReceiver) tileSide[i]).receiveEnergy(outputPerSide[i], invertSide(i));
					transmittEnergy(outputPerSide[i],ForgeDirection.getOrientation(i));
				}
			}
			*/
			
		}
		deadEnd = checkDeadEnd(tileSide);
		if (storage < 0) {
			storage = 0;
		}
		if(storage<maxStorage) {
			currentMaxInput = maxInput;
		} else {
			currentMaxInput = 0;
		}
		blockedSides = 0x00;
	}
	
	private boolean checkDeadEnd(TileEntity[] tileSide) {
		// checks if the side is a validConnection, if it is then its NOT a deadEnd, and its NOT a InputSide with maxInput = 0
		// and its NOT a OUTPUT_Side with maxOutput 0
		if(this.isMachine()) return false;
		int validConnections = 0;
		int validSideIndex = 0;
		for (int i = 0; i < tileSide.length; i++) {
			if (tileSide[i] == null)
				continue;
			SideType type = SideType.NORMAL_SIDE;
			if (tileSide[i] instanceof ISideConnections) {
				ISideConnections sidedTile = (ISideConnections) tileSide[i];
				type = sidedTile.getSideType(invertSide(i));
			}
			if (type == SideType.INPUT_SIDE) {
				if (((IEnergyReceiver) tileSide[i]).maxInput(invertSide(i)) > 0) {
					//ClientServerLogger.addLog("INPUT");
					return false;
				} else {
					//validConnections++;
				}
			} else if (type == SideType.OUTPUT_SIDE) {
				if (((IEnergyTransmitter) tileSide[i]).maxOutput(invertSide(i)) > 0) {
					//ClientServerLogger.addLog("OUTPUT");
					return false;
				} else {
					//validConnections++;
				}
			} else if (tileSide[i] instanceof TileEntityEnergyBase && type == SideType.IN_OUTPUT_SIDE) {
				TileEntityEnergyBase tileEBase = ((TileEntityEnergyBase) tileSide[i]);
				if (!tileEBase.isDeadEnd() /*|| tileEBase.getConType(ForgeDirection.values()[side]) == ConType.MACHINE*/) {
					validConnections++;
					validSideIndex = i;
				}
			}
		}
		if(validConnections==1 && tileSide[validSideIndex] instanceof TileEntityEnergyBase) {
			TileEntityEnergyBase tileEBase = ((TileEntityEnergyBase) tileSide[validSideIndex]);
			return tileEBase.isEnergyFlowing;
		} else if(validConnections == 0) {
//				ClientServerLogger.addLog("TRUE");
			return true;
		}
		return false;

	}
	
	public boolean isDeadEnd() {
		return deadEnd;
	}

	public boolean transmittEnergy(int amount, ForgeDirection side) {
		if(amount!=0) {
			blockSide(side);
			isEnergyFlowing = true;
		}
		if(meassure) meassureTraffic += amount;
		storage-=amount;
		return true;
	}

	public boolean receiveEnergy(int amount, ForgeDirection side) {
		if(amount!=0) blockSide(side);
		storage += amount;
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		storage = par1nbtTagCompound.getInteger("storage");
		outputSides = par1nbtTagCompound.getInteger("outputSides");
		inputSides = par1nbtTagCompound.getInteger("inputSides");
		for (int i = 0; i < 6; i++) {
			prioPerSide[i] = par1nbtTagCompound.getInteger("prioPerSide"+i);
			maxOutputPerSide[i] = par1nbtTagCompound.getInteger("outputPerSide"+i);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("storage", storage);
		par1nbtTagCompound.setInteger("outputSides", outputSides);
		par1nbtTagCompound.setInteger("inputSides", inputSides);
		for (int i = 0; i < 6; i++) {
			par1nbtTagCompound.setInteger("prioPerSide"+i, prioPerSide[i]);
			par1nbtTagCompound.setInteger("outputPerSide"+i, maxOutputPerSide[i]);
		}
	}

	@Override
	protected PacketTileEntity createClientUpdateData(PacketTileEntity packet) throws IOException {
		packet.dos.writeInt(storage);
		packet.dos.writeInt(outputSides);
		packet.dos.writeInt(inputSides);
		for (int i = 0; i < 6; i++) {
			packet.dos.writeInt(prioPerSide[i]);
		}
		for (int i = 0; i < 6; i++) {
			packet.dos.writeInt(maxOutputPerSide[i]);
		}
		return packet;
	}
	
	@Override
	protected void receiveClientUpdateData(ByteArrayDataInput data) {
		storage = data.readInt();
		outputSides = data.readInt();
		inputSides = data.readInt();
		for (int i = 0; i < 6; i++) {
			prioPerSide[i] = data.readInt();
		}
		for (int i = 0; i < 6; i++) {
			maxOutputPerSide[i] = data.readInt();
		}
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
	
	
	
	public int maxInput(ForgeDirection side) {
		if(((blockedSides>>side.ordinal()) & 0x01) == 0x00)
			return currentMaxInput;
		return 0;
	} 

	public int maxOutput(ForgeDirection side) {
		if(((blockedSides>>side.ordinal()) & 0x01) == 0x00)
			if(storage>maxOutput)
				return maxOutput;
			else
				return storage;
		else
			return 0;
	}

	/*
	protected void transmittEnergyEvenly(int maxOutput, int outputSides) {
		TileEntity[] tileSides = new TileEntity[6];
		int[] sideEnergy = new int[6];
		int amountNeeded = 0;
		for (int i = 0; i < tileSides.length; i++) {
			int x = xCoord + ForgeDirection.getOrientation(i).offsetX;
			int y = yCoord + ForgeDirection.getOrientation(i).offsetY;
			int z = zCoord + ForgeDirection.getOrientation(i).offsetZ;
			tileSides[i] = worldObj.getBlockTileEntity(x, y, z);
		}
		for (int i = 0; i < 6; i++) {
				if (tileSides[i] instanceof IEnergyReceiver) {
					sideEnergy[i] = ((IEnergyReceiver) tileSides[i]).maxInput(invertSide(i));
					amountNeeded += sideEnergy[i];
				} else {
					tileSides[i] = null;
				}
		}
		if (amountNeeded == 0) {
			blockedSides = 0x00;
			return;
		}
		if (storage < maxOutput)
			maxOutput = storage;
		if (amountNeeded <= maxOutput) {
			for (int i = 0; i < 6; i++) {
				if (tileSides[i] != null) {
					((IEnergyReceiver) tileSides[i]).receiveEnergy(sideEnergy[i], invertSide(i));
				}
			}
			storage -= amountNeeded;
		} else {
			double percent = (double) maxOutput / (double) amountNeeded;
			for (int i = 0; i < 6; i++) {
				if (tileSides[i] != null) {
					int amount = (int) (sideEnergy[i] * percent);
					((IEnergyReceiver) tileSides[i]).receiveEnergy(amount, invertSide(i));
					storage -= amount;
				}
				if (storage < 0)
					storage = 0;
			}
		}
		blockedSides = 0x00;
	}
	*/

	private ForgeDirection invertSide(int side) {
		return ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[side]);
	}

	private void blockSide(ForgeDirection side) {
		blockedSides |= (0x01 << side.ordinal());
	}
	
	public void startMeassure() {
		meassure = true;
	}
	
	public String stopMeassure() {
		meassure = false;
		String str = "\nIsEnergyFlowing: "+isEnergyFlowing+"\nDeadEnd: "+deadEnd+"\nStorage: "+storage+"\nTicks: "+meassureTicks +"\nTraffic: "+meassureTraffic+"\nAvarage: "+(meassureTraffic/(double)meassureTicks);
		meassureTicks = 0;
		meassureTraffic  = 0;
		return str;
	}
	
	public abstract boolean isMachine();

}
