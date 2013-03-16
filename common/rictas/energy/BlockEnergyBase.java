package rictas.energy;

import rictas.core.TileEntityBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnergyBase extends BlockContainer {

	protected BlockEnergyBase(int blockId, Material par3Material) {
		super(blockId, par3Material);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3,
			int par4, int par5) {
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		TileEntityBase tile = (TileEntityBase)par1World.getBlockTileEntity(par2, par3, par4);
		tile.onNeighborBlockChange();
		tile.sendClientUpdatePacket();
	}
}
