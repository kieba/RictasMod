package rictas.client;


import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import rictas.energy.TileEntityEnergyCable;
import rictas.energy.TileEntityEnergyCableAdv;


public class TileEntityEnergyCableRenderer extends TileEntitySpecialRenderer {
	
	private ModelEnergyCable cableModel;
	private ModelWalls wallModel;
	
	public TileEntityEnergyCableRenderer() {
		cableModel = new ModelEnergyCable();  
		wallModel = new ModelWalls();
	}
	 
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		if(tileEntity instanceof TileEntityEnergyCableAdv) {
			TileEntityEnergyCableAdv cableSideTile = (TileEntityEnergyCableAdv) tileEntity;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
			int conSides = cableSideTile.getConnectedSides();
			int inputSides = cableSideTile.getInputSides();
			int outputSides = cableSideTile.getOutputSides();
			int metadata = cableSideTile.getBlockMetadata();
			cableModel.renderAdvPipe(0.5F/16.0F, conSides,inputSides,outputSides,metadata);
			wallModel.render(1.0F/16.0F, cableSideTile.getWallLogic()); // loads the right texturefile itself
			GL11.glPopMatrix();
		} else if(tileEntity  instanceof TileEntityEnergyCable) {
			TileEntityEnergyCable cableTile = (TileEntityEnergyCable) tileEntity;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
			int metadata = cableTile.getBlockMetadata();
			cableModel.renderBasicPipe(0.5F/16.0F, cableTile.getConnectedSides(), metadata);
			wallModel.render(1.0F/16.0F, cableTile.getWallLogic()); // loads the right texturefile itself
			GL11.glPopMatrix();
		}
	}
}
