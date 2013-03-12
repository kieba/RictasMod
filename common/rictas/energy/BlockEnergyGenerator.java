package rictas.energy;


import java.util.Random;

import rictas.core.RictasMain;
import rictas.helper.Textures;


import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnergyGenerator extends BlockContainer {

	public BlockEnergyGenerator(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setHardness(0.5F);
		this.setBlockName("Generator");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public String getTextureFile() {
		return Textures.blockTextures;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityEnergyGenerator();
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
		TileEntityEnergyGenerator tile = (TileEntityEnergyGenerator)iblockaccess.getBlockTileEntity(i, j, k);
		return tile.getTextureFromSide(side);
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		return TileEntityEnergyGenerator.getDefaultTextureFromSide(par1);
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4,
			int par5, int par6) {
		dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(RictasMain.energyGenerator,1,0));
		dropItems(par1World, par2, par3, par4);
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y,
			int z, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);
        if (tileEntity == null || par5EntityPlayer.isSneaking()) {
                return false;
        }
        par5EntityPlayer.openGui(RictasMain.instance, 0, par1World, x, y, z);
        return true;
	}
	
	private void dropItems(World world, int x, int y, int z){
        Random rand = new Random();
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
                return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack item = inventory.getStackInSlot(i);

                if (item != null && item.stackSize > 0) {
                        float rx = rand.nextFloat() * 0.8F + 0.1F;
                        float ry = rand.nextFloat() * 0.8F + 0.1F;
                        float rz = rand.nextFloat() * 0.8F + 0.1F;

                        EntityItem entityItem = new EntityItem(world,
                                        x + rx, y + ry, z + rz,
                                        new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                        if (item.hasTagCompound()) {
                        	//TODO: fix this
                         //       entityItem.item.setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                        }

                        float factor = 0.05F;
                        entityItem.motionX = rand.nextGaussian() * factor;
                        entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                        entityItem.motionZ = rand.nextGaussian() * factor;
                        world.spawnEntityInWorld(entityItem);
                        item.stackSize = 0;
                }
        }
}

}
