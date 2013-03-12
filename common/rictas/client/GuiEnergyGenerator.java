package rictas.client;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import rictas.energy.ContainerGenerator;
import rictas.energy.TileEntityEnergyGenerator;
import rictas.helper.Textures;


public class GuiEnergyGenerator extends GuiController {
	
	private TileEntityEnergyGenerator tile;
	
	public GuiEnergyGenerator(InventoryPlayer inventoryPlayer, TileEntityEnergyGenerator tile) {
		super(new ContainerGenerator(inventoryPlayer, tile), tile,175,0);
		this.tile = tile;
	}

//	public GuiEnergyGenerator(InventoryPlayer inventoryPlayer, GuiController sideControl) {
//		super(new ContainerGenerator(inventoryPlayer, sideControl.getTile()));
//		this.sideControl = sideControl;
//		sideControl.getTile().requestClientUpdate();
//	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		// draw your Gui here, only thing you need to change is the path
		int texture = mc.renderEngine.getTexture(Textures.guiEnergyGenerator);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		int burnProgress = (int) (15 - (15 * tile.getBurnProgress()));
		this.drawTexturedModalRect(x+104, y+37+burnProgress, 176, burnProgress, 15, 15-burnProgress);
		
		int progress = (int) (87 * tile.getProgress()); // 0 - 87
		this.drawTexturedModalRect(x+8, y+57, 0, 166, progress, 12);

		drawStorageInfo(x+8,y+20);
		super.drawGuiContainerBackgroundLayer(var1, var2, var3);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Energy Generator", 50, 6, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 7, ySize - 93, 4210752);
		fontRenderer.drawString("Upgrades", 123, 19, 4210752);
		super.drawGuiContainerForegroundLayer(par1, par2);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		super.drawScreen(mouseX, mouseY, par3);
		if(this.isPointInRegion(8, 57, 87, 12, mouseX, mouseY)) {
			this.drawCreativeTabHoveringText(tile.getCurrentStorage()+"/"+tile.getMaxStorage(), mouseX, mouseY);
		}
	}

	private void drawStorageInfo(int x, int y) {
		float scale = 0.9f;
		float invScale = (1.0f/scale);
		GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1.0f);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.fontRenderer.drawString("Max Prodction: "+tile.getProductionSpeed(), (int)(x*invScale), (int)((y+10)*invScale), 0);
		this.mc.fontRenderer.drawString("Max Output: "+tile.getMaxPossibleOutput(), (int)(x*invScale), (int)((y+19)*invScale), 0);
		this.mc.fontRenderer.drawString("Max Storage: "+tile.getMaxStorage(), (int)(x*invScale), (int)((y+28)*invScale), 0);
		GL11.glPopMatrix();
	}
}
