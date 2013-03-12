package rictas.energy;



import rictas.core.GuiHandler;
import rictas.helper.ModIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class TileEntityEnergyStorage extends TileEntityEnergyBase implements IInventory, IGuiFullControl {

	private final static int textureTop = 0;
	private final static int textureBottom = 1;
	private final static int textureInput = 2;
	private final static int textureOutput = 3;
	private final static int textureSide = 4;
	private final static int[] maxCapacities = new int[] {100000,200000,500000,1000000};
	private final static int[] maxInputs = new int[] {50, 100, 200, 500};
	private final static int[] maxOutputs = new int[] {50, 100, 200, 500};

    /*
    [0] = DOWN(0, -1, 0), 
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
	
	public TileEntityEnergyStorage() {
		//default-Implementation for in/outputSides have to be init. BEFORE nbt-tags are loaded
		outputSides = 0x04;
		inputSides = 0x08;
	}
	
	public int getTextureFromSide(int side) {
		if(((outputSides >> side) & 0x01) == 0x01) {
			return textureOutput + 16 * this.blockMetadata;
		} else if(((inputSides >> side) & 0x01) == 0x01) {
			return textureInput + 16 * this.blockMetadata;
		} else if(side == 0) {
			return textureBottom + 16 * this.blockMetadata;
		} else if(side == 1) {
			return textureTop + 16 * this.blockMetadata;
		}
		return textureSide + 16 * this.blockMetadata; //+ (int)(10 * getProgress());//TextureOffset
	}
	
	@Override
	protected void metadataInit() {
		this.maxStorage = maxCapacities[this.getBlockMetadata()];
		this.maxInput = maxInputs[this.getBlockMetadata()];
		this.maxOutput = maxOutputs[this.getBlockMetadata()];
		if(!this.isNbtLoaded()) {
			//default-Implementation need metadata form the tileentity, if nbt-tags are NOT loaded
			for(int i = 0; i< 6; i++) maxOutputPerSide[i] = maxOutput;
		}
	}
	
	/*
	 * Needed for rendering the block in the inventory properly
	 */
	public static int getDefaultTextureFromSide(int side, int metadata) {
		int[] defaultSideTextures = new int[] {textureBottom, textureTop, textureSide, textureSide, textureOutput, textureInput};
		return defaultSideTextures[side] + 16 * metadata;
	}
	
	public int getMaxPossibleOutput() {
		return maxOutput;
	}
	
	public int getMaxPossibleInput() {
		return maxInput;
	}
	
	public int getMaxStorage() {
		return maxStorage;
	}
	
	public int getCurrentStorage() {
		return storage > maxStorage? maxStorage : storage;
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
        	if(stack != null) {
        		NBTTagCompound tag = new NBTTagCompound();
        		tag.setByte("Slot", (byte) i);
            	stack.writeToNBT(tag);
              	itemList.appendTag(tag);
          	}
        }
        par1nbtTagCompound.setTag("Inventory", itemList);
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
		maxOutput = (int)(maxOutputs[this.blockMetadata] * (1+(increaseUpgrades * 0.1)));
		maxInput = (int)(maxInputs[this.blockMetadata] * (1+(increaseUpgrades * 0.1)));
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
			outputSides = 0x04;
			inputSides = 0x08;
		}
		super.onInventoryChanged();
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public ConType getConType(ForgeDirection side) {
		if((((outputSides | inputSides) >> side.ordinal()) & 0x01) == 0x01)
			return ConType.MACHINE;
		else
			return ConType.NONE;
	}

	@Override
	protected boolean handleClientData(int cmdId, ByteArrayDataInput data) {
		return false;
	}

	@Override
	protected boolean handleServerData(int cmdId, ByteArrayDataInput data, Player playerEntity) {
		if(cmdId==1) {
			GuiHandler.handleGuiControllerInput(this, data);
			sendClientUpdatePacket();
			worldObj.notifyBlockChange(xCoord, yCoord, zCoord, ModIDs.blockEnergyStorage);
			return true;
		}
		return false;
	}

	@Override
	protected void update() {
		updateEnergy();
	}
	
	public double getProgress() {
		return storage / (double)maxStorage;
	}
	
	@Override
	public void changeSide(ForgeDirection side)  {
		if (((outputSides >> side.ordinal()) & 0x01) == 0x01) {
			inputSides |= 0x01 << side.ordinal();
			outputSides &= ~(0x01 << side.ordinal());
		} else if (((inputSides >> side.ordinal()) & 0x01) == 0x01) {
			inputSides &= ~(0x01 << side.ordinal());
		} else {
			outputSides |= 0x01 << side.ordinal();
		}
	}
	
	@Override
	public SideType getSideType(ForgeDirection side) {
		if(((outputSides >> side.ordinal()) & 0x01) == 0x01) {
			return SideType.OUTPUT_SIDE;
		} else if(((inputSides >> side.ordinal()) & 0x01) == 0x01) {
			return SideType.INPUT_SIDE;
		} else {
			return SideType.NORMAL_SIDE;
		}
	}
	
	@Override
	public int maxInput(ForgeDirection side) {
		if((((inputSides|blockedSides)>>side.ordinal()) & 0x01) == 0x01)
			return currentMaxInput;
		return 0;
	}

	@Override
	public int maxOutput(ForgeDirection side) {
		if((((outputSides|blockedSides)>>side.ordinal()) & 0x01) == 0x01)
			if(storage>maxOutputPerSide[side.ordinal()])
				return maxOutputPerSide[side.ordinal()];
			else
				return storage;
		else
			return 0;
	}
	
	@Override
	public boolean isMachine() {
		return true;
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
}
