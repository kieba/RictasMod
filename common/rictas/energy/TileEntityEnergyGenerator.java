package rictas.energy;


import java.io.IOException;

import rictas.core.GuiHandler;
import rictas.core.PacketTileEntity;
import rictas.helper.ClientServerLogger;
import rictas.helper.ModIDs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TileEntityEnergyGenerator extends TileEntityEnergyBase implements IInventory, IGuiFullControl  {

	private static int textureTop = 0 + 16*4;
	private static int textureBottom = 1 + 16*4;
	private static int textureSideActive = 2 + 16*4;
	private static int textureSideInActive = 3 + 16*4;
	private static int textureOutput = 4 + 16*4;
	private static int defaultMaxOutput = 15;
	private static int defaultProdutcionSpeed = 10;
    /*
    [0] = DOWN(0, -1, 0), 
    [1] = UP(0, 1, 0),
    [2] = NORTH(0, 0, -1),
    [3] = SOUTH(0, 0, 1),
    [4] = WEST(-1, 0, 0),
    [5] = EAST(1, 0, 0),
    */
	private int remainingBurnTime;
	private int currentItemBurnTime;
	private ItemStack[] inv = new ItemStack[5];
	
	private int productionSpeedBonus = 0; // if 10 => double production
	private int count = 0;
	
	private boolean hasOutputControl;
	private boolean hasSideControl;
	private boolean hasPrioControl;
	
	public TileEntityEnergyGenerator() {
		outputSides = 0x04;
		this.maxStorage = 10000;
		this.maxOutput = defaultMaxOutput;
	}
	
	@Override
	protected void metadataInit() {
		if(!this.isNbtLoaded()) {
			for(int i = 0; i< 6; i++) maxOutputPerSide[i] = maxOutput;
		}
		this.onInventoryChanged();
	}
	
	/*
	 * Needed for rendering the block in the inventory properly
	 */
	public static int getDefaultTextureFromSide(int side) {
		int[] defaultSideTextures = new int[] {textureBottom, textureTop, textureSideActive, textureSideActive, textureOutput, textureSideActive};
		return defaultSideTextures[side];
	}
	
	public int getTextureFromSide(int side) {	
		if(((outputSides >> side) & 0x01) == 0x01) {
			return textureOutput;
		} else if(side == 0) {
			return textureBottom;
		} else if(side == 1) {
			return textureTop;
		}
		if(remainingBurnTime>0)
			return textureSideActive;
		else
			return textureSideInActive;
	}
	
	public double getProgress() {
		return storage / (double)maxStorage;
	}
	
	public double getBurnProgress() {
		if(currentItemBurnTime==0)
			return 0;
		return remainingBurnTime / (double)currentItemBurnTime;
	}
	
	@Override
	protected PacketTileEntity createClientUpdateData(PacketTileEntity packet)
			throws IOException {
		packet.dos.writeInt(remainingBurnTime);
		packet.dos.writeInt(currentItemBurnTime);
		return super.createClientUpdateData(packet);
	}

	@Override
	protected void receiveClientUpdateData(ByteArrayDataInput data) {
		remainingBurnTime = data.readInt();	
		currentItemBurnTime = data.readInt();
		super.receiveClientUpdateData(data);
	}

	@Override
	protected boolean handleClientData(int cmdId, ByteArrayDataInput data) {
		if(cmdId == 2) {
			this.remainingBurnTime = data.readInt();
			this.currentItemBurnTime = data.readInt();
			int itemID = data.readShort();
			if(itemID != -1)
				inv[0] = new ItemStack(itemID, data.readByte(), data.readShort());
			return true;
		}
		return false;
	}

	@Override
	protected boolean handleServerData(int cmdId, ByteArrayDataInput data, Player playerEntity) {
		if(cmdId==1) {
			GuiHandler.handleGuiControllerInput(this, data);
			sendClientUpdatePacket();
			worldObj.notifyBlockChange(xCoord, yCoord, zCoord, ModIDs.blockEnergyGenerator);
			return true;
		}
		return false;
	}
	
	@Override
	public void changeSide(ForgeDirection side)  {
		if (((outputSides >> side.ordinal()) & 0x01) == 0x01) {
			outputSides &= ~(0x01 << side.ordinal());
		} else {
			outputSides |= (0x01 << side.ordinal());
		}
	}

	@Override
	protected void update() {
		updateEnergy();
		updateBurning();		
	}
	
	public int getMaxPossibleOutput() {
		return maxOutput;
	}
	
	public int getProductionSpeed() {
		return defaultProdutcionSpeed + productionSpeedBonus;
	}

	public int getMaxStorage() {
		return maxStorage;
	}
	
	public int getCurrentStorage() {
		return storage > maxStorage? maxStorage : storage;
	}
	
	private void updateBurning() {
		boolean updateClient = false;
        if (this.remainingBurnTime > 0 && storage<maxStorage)
        {
            --this.remainingBurnTime;
            storage += 3;
            count += productionSpeedBonus;
            while(count >= 10) {
            	count -= 10;
            	--this.remainingBurnTime;
            	storage += 3;
            }
        }
        if (!this.worldObj.isRemote)
        {
            if (this.remainingBurnTime == 0)
            {
                this.currentItemBurnTime = this.remainingBurnTime = TileEntityFurnace.getItemBurnTime(inv[0]);
                if (this.remainingBurnTime > 0)
                {
                	this.remainingBurnTime++;
                	updateClient = true;
                    if (this.inv[0] != null)
                    {
                        --this.inv[0].stackSize;

                        if (this.inv[0].stackSize == 0)
                        {
                            this.inv[0]  = this.inv[0].getItem().getContainerItemStack(inv[0]);
                        }
                        //this.onInventoryChanged();
                    }
                }
            }
            if(updateClient) {
            	updateClientBurning();
            }
            	
        } else {
        	if(remainingBurnTime  <= 0) {
        		worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
        	}
        }
	}
	
	public void updateClientBurning() {
		PacketTileEntity tilePacket = new PacketTileEntity(this.xCoord, this.yCoord, this.zCoord);
		try {
			tilePacket.dos.writeByte(2); // update ID
			tilePacket.dos.writeInt(this.remainingBurnTime);
			tilePacket.dos.writeInt(this.currentItemBurnTime);
			if(inv[0]!= null) {
				tilePacket.dos.writeShort((short)inv[0].itemID);
				tilePacket.dos.writeByte((byte)inv[0].stackSize);
				tilePacket.dos.writeShort((short)inv[0].getItemDamage());
			} else {
				tilePacket.dos.writeShort(-1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		PacketDispatcher.sendPacketToAllPlayers(tilePacket.getPacket());
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
	public ConType getConType(ForgeDirection side) {
		if(((outputSides >> side.ordinal()) & 0x01) == 0x01)
			return ConType.MACHINE;
		return ConType.NONE;
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
		int productionSpeedUpgrades = 0;
		for(int i = 1; i < 5; i++)  {
			if(inv[i] != null && inv[i].getItem() instanceof ItemUpgrade) {
				if(inv[i].getItemDamage() == 0) {
					hasOutputControl = true;
				} else if(inv[i].getItemDamage() == 1) {
					hasPrioControl = true;
				} else if(inv[i].getItemDamage() == 2) {
					hasSideControl = true;
				} else if(inv[i].getItemDamage() == 3) {
					increaseUpgrades += inv[i].stackSize;
				} else if(inv[i].getItemDamage() == 4) {
					productionSpeedUpgrades += inv[i].stackSize;
				}
			}
		}
		productionSpeedBonus = productionSpeedUpgrades;
		maxOutput = (int)(defaultMaxOutput * (1+(increaseUpgrades * 0.1)));
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
			inputSides = 0x00;
		}
		super.onInventoryChanged();
		if(!worldObj.isRemote) {
			worldObj.notifyBlockChange(xCoord, yCoord, zCoord, ModIDs.blockEnergyStorage);
			sendClientUpdatePacket();
		}
	}

	@Override
	public SideType getSideType(ForgeDirection side) {
		if (((outputSides >> side.ordinal()) & 0x01) == 0x01) {
			return SideType.OUTPUT_SIDE;
		}
		return SideType.NORMAL_SIDE;
	}
	
	@Override
	public boolean isMachine() {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

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
	public int getOutput(ForgeDirection side) {
		return maxOutputPerSide[side.ordinal()];
	}
}
