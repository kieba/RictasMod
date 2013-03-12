package rictas.energy;


import java.util.List;

import rictas.helper.Textures;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {
	
	public final static String[] subNames = {"Output Control","Prio Control","Side Control", "Increased In/Output", "Increased Speed"};

	public ItemUpgrade(int id) {
		super(id);
		setHasSubtypes(true);
		this.setItemName("Upgrade");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public String getTextureFile() {
		return Textures.itemTextures;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List subItems) {
		for (int i = 0; i < subNames.length; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
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

	
}
