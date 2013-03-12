package rictas.energy;

import rictas.client.GuiWalls;
import rictas.core.RictasMain;
import rictas.helper.Textures;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWallPainter extends Item {

	public ItemWallPainter(int id) {
		super(id);
		this.setItemName("WallPainter");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.maxStackSize = 1;
	}
	
	@Override
	public String getTextureFile() {
		return Textures.itemTextures;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damageValue) {
		return 5;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y,
			int z, int side, float par8, float par9, float par10) {
		TileEntityEnergyCable tile = (TileEntityEnergyCable) par3World.getBlockTileEntity(x, y, z);
		if(tile != null) {
			tile.getWallLogic().clickedSide = side;
			par2EntityPlayer.openGui(RictasMain.instance, GuiWalls.guiWallId, par3World, x, y, z);
			
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

}
