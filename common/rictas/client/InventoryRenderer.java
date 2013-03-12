package rictas.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import rictas.core.CommonProxy;
import rictas.core.RictasMain;
import rictas.energy.BlockEnergyCableDummyRender;
import rictas.helper.Textures;


import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class InventoryRenderer implements ISimpleBlockRenderingHandler {
	
	public ModelEnergyCable model = new ModelEnergyCable();
	public ModelWalls modelWalls = new ModelWalls();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if(block == RictasMain.energyCable) {
			ForgeHooksClient.bindTexture(Textures.basicPipes[metadata], 0);
			model.renderBasicPipe(0.5F/16.0F, 0xFFFF, metadata);
		} else if(block == RictasMain.energyCableSided) {
			model.renderAdvPipe(0.5F/16.0F, 0xFFFF, 0xFFFF, 0xFFFF, metadata);
		} else if(block == RictasMain.energyCableDummyRender) {
			BlockEnergyCableDummyRender dummyBlock = (BlockEnergyCableDummyRender) block;
			GL11.glPushMatrix();
			GL11.glRotatef(dummyBlock.getRotation(), 0.0F, 1.0F, 0.0F);
			modelWalls.render(1.0F/16.0F, dummyBlock.wallLogic);
			model.renderBasicPipe(0.5F/16.0F, 0xFFFF, dummyBlock.metadata);		
			GL11.glPopMatrix();
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,Block block, int modelId, RenderBlocks renderer) {
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.cableRenderId;
	}

}
