package rictas.energy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerGenerator extends Container {

	private TileEntityEnergyGenerator tile;
	private boolean[] allowedUpgrades = new boolean[ItemUpgrade.subNames.length];

	public ContainerGenerator(InventoryPlayer inventoryPlayer, TileEntityEnergyGenerator te) {
		tile = te;
		int upgrades = 0x1F;
        for(int i = 0; i < this.allowedUpgrades.length; i++) {
        	this.allowedUpgrades[i] = ((upgrades & (0x01 << i)) > 0);
        }
		// the Slot constructor takes the IInventory and the slot number in that
		// it binds to
		// and the x-y coordinates it resides on-screen
		addSlotToContainer(new Slot(tile, 0, 103, 53));
		addSlotToContainer(new SlotEnergyUpgrade(tile, 1, 128, 30, allowedUpgrades));
		addSlotToContainer(new SlotEnergyUpgrade(tile, 2, 152, 30, allowedUpgrades));
		addSlotToContainer(new SlotEnergyUpgrade(tile, 3, 128, 53, allowedUpgrades));
		addSlotToContainer(new SlotEnergyUpgrade(tile, 4, 152, 53, allowedUpgrades));
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
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
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
			if (i < 5) {
				if (!mergeItemStack(itemstack1, 5, inventorySlots.size(), true))
					return null;
			} else {
				if(isValidUpgradeItem(itemstack1)) {
					if (!mergeUpgradeStack(itemstack1, 1, 5))
						return null;
				} else if(TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, 1, true)) 
						return null;
				} else {
					return null;
				}
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
	
	public boolean isValidUpgradeItem(ItemStack stack)
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
					slot.onSlotChanged();
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
