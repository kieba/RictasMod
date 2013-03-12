package rictas.energy;



import java.util.List;

import rictas.core.CommonProxy;
import rictas.helper.ClientServerLogger;
import rictas.helper.Textures;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEnergyCable extends BlockContainer {

	public BlockEnergyCable(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setHardness(0.5F);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs tab, List subItems) {
		for (int i = 0; i < 4; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return CommonProxy.cableRenderId;
	}

	@Override
	public String getTextureFile() {
		return Textures.basicPipes[0];
	}	
	
	@Override
	public int getBlockTextureFromSideAndMetadata (int side, int metadata) {
		return metadata;
	}
	
	@Override
	public int damageDropped (int metadata) {
		return metadata;
	}

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int x, int y, int z)
    {
    	TileEntityEnergyCable tile = (TileEntityEnergyCable)par1IBlockAccess.getBlockTileEntity(x, y, z);
    	int sides = tile.getConnectedSides();
    	if((sides & 0x01) == 0x01)
    		this.minY = 0F;
    	else
    		this.minY = 0.35F;
    	if((sides & 0x02) == 0x02)
    		this.maxY = 1F;
    	else
    		this.maxY = 0.65F;
    	if((sides & 0x04) == 0x04)
    		this.minZ = 0F;
    	else
    		this.minZ = 0.35F;
    	if((sides & 0x08) == 0x08)
    		this.maxZ = 1F;
    	else
    		this.maxZ = 0.65F;
    	if((sides & 0x10) == 0x10)
    		this.minX = 0F;
    	else
    		this.minX = 0.35F;
    	if((sides & 0x20) == 0x20)
    		this.maxX = 1F;
    	else
    		this.maxX = 0.65F;
    	if(tile.getWallLogic().renderWall(ForgeDirection.DOWN.ordinal())) {
    		minX = 0.0F;
    		maxX = 1.0F;
    		minZ = 0.0F;
    		maxZ = 1.0F;
    		minY = 0.0F;
    	}
    	if(tile.getWallLogic().renderWall(ForgeDirection.UP.ordinal())) {
    		minX = 0.0F;
    		maxX = 1.0F;
    		minZ = 0.0F;
    		maxZ = 1.0F;
    		maxY = 1.0F;
    	}
    	if(tile.getWallLogic().renderWall(ForgeDirection.NORTH.ordinal())) {
    		minX = 0.0F;
    		maxX = 1.0F;
    		minY = 0.0F;
    		maxY = 1.0F;
    		minZ = 0.0F;
    	}
    	if(tile.getWallLogic().renderWall(ForgeDirection.SOUTH.ordinal())) {
    		minX = 0.0F;
    		maxX = 1.0F;
    		minY = 0.0F;
    		maxY = 1.0F;
    		maxZ = 1.0F;
    	}
    	if(tile.getWallLogic().renderWall(ForgeDirection.WEST.ordinal())) {
    		minZ = 0.0F;
    		maxZ = 1.0F;
    		minY = 0.0F;
    		maxY = 1.0F;
    		minX = 0.0F;
    	}
    	if(tile.getWallLogic().renderWall(ForgeDirection.EAST.ordinal())) {
    		minZ = 0.0F;
    		maxZ = 1.0F;
    		minY = 0.0F;
    		maxY = 1.0F;
    		maxX = 1.0F;
    	}
    }

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityEnergyCable();
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4,
			int id, int meta) {
		dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(this,1,meta));
		super.breakBlock(par1World, par2, par3, par4, id, meta);
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3,
			int par4, int par5) {
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		TileEntityEnergyCable tile = (TileEntityEnergyCable)par1World.getBlockTileEntity(par2, par3, par4);
		tile.updateSides();
		tile.sendClientUpdatePacket();
	}

	@Override
	public void onBlockClicked(World par1World, int par2, int par3, int par4,
			EntityPlayer par5EntityPlayer) {
		super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
		TileEntityEnergyCable tile = (TileEntityEnergyCable)par1World.getBlockTileEntity(par2, par3, par4);
		if(tile.meassure) 
			ClientServerLogger.addLog(tile.stopMeassure());
		else
			tile.startMeassure();
	}
	
	
}