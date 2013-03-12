package rictas.energy;

import rictas.core.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockEnergyCableDummyRender extends Block {
	
	public int metadata;
	public WallLogic wallLogic;
	public float renderBlockRotation = 0.0F;
	private long time;
	
	public BlockEnergyCableDummyRender(int par1, Material par2Material) {
		super(par1, par2Material);
	}
	
	public float getRotation() {
		long newTime = System.currentTimeMillis();
		long timeDif = newTime - time;
		time = newTime;
		renderBlockRotation += 0.36F * (timeDif / 5);
		if(renderBlockRotation > 360.0F) renderBlockRotation = 0.0F;
		return renderBlockRotation;
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
	
	
}
