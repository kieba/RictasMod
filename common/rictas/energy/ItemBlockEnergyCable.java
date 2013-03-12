package rictas.energy;

import rictas.core.RictasMain;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockEnergyCable extends ItemBlock {
	
	public final static String[] subNames = {"Energy Cable V1","Energy Cable V2","Energy Cable V3","Energy Cable V4"};
	

	public ItemBlockEnergyCable(int id) {
		super(id);
		setHasSubtypes(true);
		this.setItemName("Energy Cable");
	}
	
	
	
	@Override
	public int getMetadata (int damageValue) {
		return damageValue;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damageValue) {
		return damageValue;
	}



	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return subNames[itemstack.getItemDamage()];
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int i, int j,
			int k, int side, float par8, float par9, float par10) {
		int blockId = RictasMain.energyCable.blockID;
		int klickedBlockId = par3World.getBlockId(i, j, k) ;
		if (klickedBlockId == Block.snow.blockID) {
			side = 0;
		} else {
			if (side == 0)
				j--;
			if (side == 1)
				j++;
			if (side == 2)
				k--;
			if (side == 3)
				k++;
			if (side == 4)
				i--;
			if (side == 5)
				i++;
		}
		if (par1ItemStack.stackSize == 0)
			return false;
		if (par3World.setBlockAndMetadataWithNotify(i, j, k, blockId ,par1ItemStack.getItemDamage())) {
			Block.blocksList[blockId].onBlockPlacedBy(par3World, i, j, k,  par2EntityPlayer);
			par1ItemStack.stackSize--;
			return true;
		}
		return false;
	}
}
