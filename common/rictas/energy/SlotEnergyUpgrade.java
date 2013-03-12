package rictas.energy;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnergyUpgrade extends Slot {
	
	private boolean[] allowedUpgrades = new boolean[ItemUpgrade.subNames.length];

	public SlotEnergyUpgrade(IInventory par2IInventory, int par3, int par4, int par5, boolean[] allowedUpgrades)
    {
        super(par2IInventory, par3, par4, par5);
        this.allowedUpgrades = allowedUpgrades;
    }
	
	public int getSlotStackLimit()
    {
		if(this.getStack() != null) {
			int metadata = this.getStack().getItemDamage();
			if(metadata == 3) {
				return 10;
			} else if(metadata == 4) {
				return 10;
			}
		}
        return 1;
    }

	public boolean isItemValid(ItemStack stack)
    {
        return stack == null ? false : (stack.getItem() instanceof ItemUpgrade && allowedUpgrades[stack.getItemDamage()]);
    }
}
