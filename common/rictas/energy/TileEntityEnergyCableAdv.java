package rictas.energy;

import rictas.core.GuiHandler;
import rictas.core.PacketTileEntity;
import rictas.helper.ClientServerLogger;
import rictas.helper.ModIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class TileEntityEnergyCableAdv extends TileEntityEnergyCable implements IInventory, IGuiFullControl {
	
    /*
    Bit[0] = DOWN(0, -1, 0), 
    [1] = UP(0, 1, 0),
    [2] = NORTH(0, 0, -1),
    [3] = SOUTH(0, 0, 1),
    [4] = WEST(-1, 0, 0),
    [5] = EAST(1, 0, 0),
    */
	private ItemStack[] inv = new ItemStack[4];
	private boolean hasOutputControl;
	private boolean hasSideControl;
	private boolean hasPrioControl;
	
	public TileEntityEnergyCableAdv() {
		outputSides = 0xFF;
		inputSides = 0xFF;
	}

	@Override
	protected void metadataInit() {
		super.metadataInit();
		maxStorage = maxCapacity[this.getBlockMetadata()];
		this.maxInput = maxStorage;
		this.maxOutput = maxStorage;
		if(!this.isNbtLoaded()) {
			for(int i = 0; i< 6; i++) maxOutputPerSide[i] = maxOutput;
			onNeighborBlockChange();
		}
		this.onInventoryChanged();
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		NBTTagList tagList = par1nbtTagCompound.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                byte slot = tag.getByte("Slot");
                if (slot >= 0 && slot < inv.length) {
                        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
                }
        }
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
        	ItemStack stack = inv[i];
        	if (stack != null) {
        		NBTTagCompound tag = new NBTTagCompound();
        		tag.setByte("Slot", (byte) i);
        		stack.writeToNBT(tag);
        		itemList.appendTag(tag);
        	}
        }
        par1nbtTagCompound.setTag("Inventory", itemList);
	}

	@Override
	protected boolean handleServerData(int cmdId, ByteArrayDataInput data, Player playerEntity) {
		if(cmdId == PacketTileEntity.SIDE_CONTROL) {
			GuiHandler.handleGuiControllerInput(this, data);
			updateSides();
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, ModIDs.blockEnergyCableSided);
			sendClientUpdatePacket();
			return true;
		} else if(cmdId == PacketTileEntity.WALL_CONTROL) {
			wallLogic.handleGuiWallInput(this, data);
			updateSides();
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, ModIDs.blockEnergyCableSided);
			sendClientUpdatePacket();
			return true;
		}
		return false;
	}

	@Override
	public ConType getConType(ForgeDirection side) {
		boolean isInput = ((inputSides >> side.ordinal()) & 0x01) == 0x01;
		boolean isOutput = ((outputSides >> side.ordinal()) & 0x01) == 0x01;
		if(!isInput && !isOutput)
			return ConType.NONE;
		return ConType.values()[blockMetadata+2];
	}	
	
	@Override
	public SideType getSideType(ForgeDirection side) {
		boolean isInput = ((inputSides >> side.ordinal()) & 0x01) == 0x01;
		boolean isOutput = ((outputSides >> side.ordinal()) & 0x01) == 0x01;
		if(isInput && isOutput) {
			return SideType.IN_OUTPUT_SIDE;
		} else if(isInput) {
			return SideType.INPUT_SIDE;
		} else if(isOutput) {
			return SideType.OUTPUT_SIDE;
		} else {
			return SideType.NORMAL_SIDE;
		}
	}	

	@Override
	public void changeSide(ForgeDirection side) {
		boolean isInput = ((inputSides >> side.ordinal()) & 0x01) == 0x01;
		boolean isOutput = ((outputSides >> side.ordinal()) & 0x01) == 0x01;
		if(isInput && isOutput) {
			inputSides &= ~(0x01<<side.ordinal());
			outputSides &= ~(0x01<<side.ordinal());
		} else if(isInput) {
			inputSides |= 0x01<<side.ordinal();
			outputSides |= 0x01<<side.ordinal();
		} else if(isOutput) {
			outputSides &= ~(0x01<<side.ordinal());
			inputSides |= 0x01<<side.ordinal();
		} else {
			inputSides &= ~(0x01<<side.ordinal());
			outputSides |= 0x01<<side.ordinal();
		}
	}
	
	public int getInputSides() {
		return inputSides;
	}
	
	public int getOutputSides() {
		return outputSides;
	}

	@Override
	public int getOutput(ForgeDirection side) {
		return maxOutputPerSide[side.ordinal()];
	}

	@Override
	public void increaseOutput(ForgeDirection side, int amount) {
		maxOutputPerSide[side.ordinal()] += amount;
		if(maxOutputPerSide[side.ordinal()]>maxOutput) maxOutputPerSide[side.ordinal()] = maxOutput;
	}

	@Override
	public void decreaseOutput(ForgeDirection side, int amount) {
		maxOutputPerSide[side.ordinal()] -= amount;
		if(maxOutputPerSide[side.ordinal()] < 0) maxOutputPerSide[side.ordinal()]=0;
	}

	@Override
	public boolean hasOutputControl() {
		return hasOutputControl;
	}

	@Override
	public boolean hasSideControl() {
		return hasSideControl;
	}

	@Override
	public int getPriority(ForgeDirection side) {
		return prioPerSide[side.ordinal()];
	}

	@Override
	public void increasePrio(ForgeDirection side) {
		prioPerSide[side.ordinal()] = (prioPerSide[side.ordinal()] + 1) % 6;
	}

	@Override
	public void decreasePrio(ForgeDirection side) {
		prioPerSide[side.ordinal()]--;
		if(prioPerSide[side.ordinal()] < 0) prioPerSide[side.ordinal()] = 5;
	}

	@Override
	public boolean hasPrioControl() {
		return hasPrioControl;
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inv[var1];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
                if (stack.stackSize <= amount) {
                        setInventorySlotContents(slot, null);
                } else {
                        stack = stack.splitStack(amount);
                        if (stack.stackSize == 0) {
                                setInventorySlotContents(slot, null);
                        }
                }
        }
        return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
                setInventorySlotContents(slot, null);
        }
        return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
        } 
	}

	@Override
	public String getInvName() {
		return "Ka was das bringt";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}
	
	@Override
	public void onInventoryChanged() {
		hasOutputControl = false;
		hasPrioControl = false;
		hasSideControl = false;
		int increaseUpgrades = 0;
		for(int i = 0; i < 4; i++)  {
			if(inv[i] != null && inv[i].getItem() instanceof ItemUpgrade) {
				if(inv[i].getItemDamage() == 0) {
					hasOutputControl = true;
				} else if(inv[i].getItemDamage() == 1) {
					hasPrioControl = true;
				} else if(inv[i].getItemDamage() == 2) {
					hasSideControl = true;
				} else if(inv[i].getItemDamage() == 3) {
					increaseUpgrades += inv[i].stackSize;
				}
			}
		}
		maxOutput = (int)(maxCapacity[this.blockMetadata] * (1+(increaseUpgrades * 0.1)));
		if(!hasOutputControl){
			for(int i = 0; i< 6; i++) {
				maxOutputPerSide[i] = maxOutput; 
			}
		}
		if(!hasPrioControl) {
			for(int i = 0; i< 6; i++) {
				prioPerSide[i] = 0;
			}
		}
		if(!hasSideControl) {
			outputSides = 0xFF;
			inputSides = 0xFF;
		}
		super.onInventoryChanged();
		if(!worldObj.isRemote) {
			worldObj.notifyBlockChange(xCoord, yCoord, zCoord, ModIDs.blockEnergyStorage);
			sendClientUpdatePacket();
		}
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}
}