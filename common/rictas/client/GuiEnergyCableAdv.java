package rictas.client;

import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import rictas.energy.ContainerCableAdv;
import rictas.energy.TileEntityEnergyCableAdv;
import rictas.helper.Textures;


public class GuiEnergyCableAdv extends GuiController {

	private TileEntityEnergyCableAdv tile;

	public GuiEnergyCableAdv(InventoryPlayer inventoryPlayer, TileEntityEnergyCableAdv tile) {
		super(new ContainerCableAdv(inventoryPlayer, tile),tile,117,-11);
		this.tile = tile;
		//sideControl.getTile().requestClientUpdate();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		// draw your Gui here, only thing you need to change is the path
		int texture = mc.renderEngine.getTexture(Textures.guiEnergyCableSided);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		super.drawGuiContainerBackgroundLayer(var1, var2, var3);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("Upgrades", 64, 5, 4210752);
		super.drawGuiContainerForegroundLayer(par1, par2);
	}
	
	


}
