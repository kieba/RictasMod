package rictas.energy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCableSided extends Container {

	private TileEntityEnergyCableSided tile;
	private boolean[] allowedUpgrades = new boolean[ItemUpgrade.subNames.length];

	public ContainerCableSided(InventoryPlayer inventoryPlayer, TileEntityEnergyCableSided tile) {
		this.tile = tile;
		int upgrades = 0x0F;
        for(int i = 0; i < this.allowedUpgrades.length; i++) {
        	this.allowedUpgrades[i] = ((upgrades & (0x01 << i)) > 0);
        }
		// the Slot constructor takes the IInventory and the slot number in that
		// it binds to
		// and the x-y coordinates it resides on-screen
		addSlotToContainer(new SlotEnergyUpgrade(tile, 0, 68, 16, allowedUpgrades));
		addSlotToContainer(new SlotEnergyUpgrade(tile, 1, 92, 16, allowedUpgrades));
		addSlotToContainer(new SlotEnergyUpgrade(tile, 2, 68, 39, allowedUpgrades));
		addSlotToContainer(new SlotEnergyUpgrade(tile, 3, 92, 39, allowedUpgrades));
		// commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		 return tile.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < 4) {
				if (!mergeItemStack(itemstack1, 4, inventorySlots.size(), true))
					return null;
			} else {
				if(!isItemValid(itemstack1)) 
					return null;
				if (!mergeUpgradeStack(itemstack1, 0, 4))
					return null;
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
	
	public boolean isItemValid(ItemStack stack)
    {
        return stack == null ? false : (stack.getItem() instanceof ItemUpgrade && allowedUpgrades[stack.getItemDamage()]);
    }
	
	public boolean mergeUpgradeStack(ItemStack stack, int begin, int end) {
		Slot slot;
		ItemStack slotStack;
		for(int i = begin; i < end; i++) {
			slot = (Slot)this.inventorySlots.get(i);
			slotStack = slot.getStack();
			if(slotStack != null)  {
				if(slotStack.getItem() == stack.getItem() && slotStack.getItemDamage() == stack.getItemDamage()) {
					if(slot.getSlotStackLimit() <= slotStack.stackSize) continue;
					int neededItems = slot.getSlotStackLimit() - slotStack.stackSize;
					while(neededItems > 0 && stack.stackSize  > 0) {
						slotStack.stackSize++;
						stack.stackSize--;
						neededItems--;
					}
					if(stack.stackSize == 0) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		for(int i = begin; i < end; i++) {
			slot = (Slot)this.inventorySlots.get(i);
			slotStack = slot.getStack();
			if(slotStack == null)  {
				int stackSize = stack.stackSize;
				int maxSlotSize = slot.getSlotStackLimit();
				if(stackSize < maxSlotSize) {
					slot.putStack(stack.copy());
					stack.stackSize = 0;
				}  else {
					int rest = stackSize - maxSlotSize;
					stack.stackSize = maxSlotSize;
					slot.putStack(stack.copy());
					stack.stackSize = rest;
				}
				slot.onSlotChanged();
				if(stack.stackSize == 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
}
