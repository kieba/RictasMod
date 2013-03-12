package rictas.client;


import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import rictas.core.PacketTileEntity;
import rictas.core.RictasMain;
import rictas.energy.TileEntityEnergyCable;
import rictas.energy.WallLogic;
import rictas.helper.Textures;


import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiWalls extends GuiContainer {

	public static int guiWallId = 1;
	public static final int BUTTON_ID_START_INDEX = 0;
	private GuiButtonSideControl[] btnControlls = new GuiButtonSideControl[5];
	private GuiButtonSideControl[] btnRenderBlocks = new GuiButtonSideControl[54];
	private TileEntityEnergyCable tile;
	private int top;
	private int left;
	private int amountPages;
	private int selectedPage;
	private WallLogic wallLogic;
	private int clickedSide;
	private int selectedRenderBlock = 0;

	public GuiWalls(InventoryPlayer inventoryPlayer,  TileEntityEnergyCable tile) {
		super(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer var1) {
				return true;
			}
		});
		this.tile = tile;
		wallLogic = tile.getWallLogic();
		clickedSide = wallLogic.clickedSide;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.left = (this.width - this.xSize) / 2;
		this.top = (this.height - this.ySize) / 2;
		int buttonId = BUTTON_ID_START_INDEX;
		btnControlls[0] = new GuiButtonSideControl(buttonId++, left + 14, top + 46, 6, 7, /*page-*/0, 230, Textures.guiWalls);
		btnControlls[1] = new GuiButtonSideControl(buttonId++, left + 53, top + 46, 6, 7, /*page+*/6, 230, Textures.guiWalls);
		btnControlls[2] = new GuiButtonSideControl(buttonId++, left + 63, top + 16, 8, 34, /*rotateLeft */176, 0, Textures.guiWalls);
		btnControlls[3] = new GuiButtonSideControl(buttonId++, left + 105, top + 16, 8, 34, /*rotateRight*/184, 0, Textures.guiWalls);
		btnControlls[4] = new GuiButtonSideControl(buttonId++, left + 118, top + 25, 16, 16, /*wallOption */0, 166, Textures.guiWalls);
		controlList.add(btnControlls[0]);
		controlList.add(btnControlls[1]);
		controlList.add(btnControlls[2]);
		controlList.add(btnControlls[3]);
		controlList.add(btnControlls[4]);
		btnControlls[0].drawButton = true;
		btnControlls[1].drawButton = true;
		btnControlls[2].drawButton = true;
		btnControlls[3].drawButton = true;
		btnControlls[4].drawButton = true;
		int index = 0;
		for (int row = 0; row < 6; row++) {
			for (int column = 0; column < 9; column++) {
				btnRenderBlocks[index] = new GuiButtonSideControl(buttonId++, left + 16 + 16 * column, top + 57 + 16 * row, 16, 16, /*page-*/0, 198, Textures.guiWalls);
				controlList.add(btnRenderBlocks[index]);
				btnRenderBlocks[index].drawButton = true;
				index++;
			}
		}
		for (int j = 0; j < WallLogic.validWallBlocks.size(); j++) {
			if(wallLogic.getRenderBlockId(clickedSide) == WallLogic.validWallBlocks.get(j).blockID) {
				selectedRenderBlock = j;
				break;
			}
		}
		if(selectedRenderBlock >= (selectedPage * 54) && selectedRenderBlock < ((selectedPage + 1) * 54)) {
			btnRenderBlocks[selectedRenderBlock - selectedPage * 54].setSelected(true);
		}
		amountPages = WallLogic.validWallBlocks.size() / 54;
		if(WallLogic.validWallBlocks.size() % 54 > 0) {
			amountPages++;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		int texture = mc.renderEngine.getTexture(Textures.guiWalls);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == BUTTON_ID_START_INDEX) {
			switchPage(false);
			return;
		} else if(par1GuiButton.id == BUTTON_ID_START_INDEX + 1) {
			switchPage(true);
			return;
		}
		PacketTileEntity tilePacket = new PacketTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
		try {
			tilePacket.dos.writeByte(PacketTileEntity.WALL_CONTROL);
			tilePacket.dos.writeInt(par1GuiButton.id);
			int index = par1GuiButton.id - GuiWalls.BUTTON_ID_START_INDEX - 5;
			if(index >= 0 && index < 54) {
				Block b = WallLogic.validWallBlocks.get(index + selectedPage * 54);
				if(b != null) 
					tilePacket.dos.writeInt(b.blockID);
				else
					tilePacket.dos.writeInt(-1);
				if(selectedRenderBlock >= (selectedPage * 54) && selectedRenderBlock < ((selectedPage + 1) * 54)) {
					btnRenderBlocks[selectedRenderBlock - selectedPage * 54].setSelected(false);
				}
				selectedRenderBlock = index + selectedPage * 54;
				btnRenderBlocks[selectedRenderBlock - selectedPage * 54].setSelected(true);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		PacketDispatcher.sendPacketToServer(tilePacket.getPacket());
	}
	
	private void switchPage(boolean forward) {
		if(amountPages == 0) {
			return;
		}
		if(selectedRenderBlock >= (selectedPage * 54) && selectedRenderBlock < ((selectedPage + 1) * 54)) {
			btnRenderBlocks[selectedRenderBlock - selectedPage * 54].setSelected(false);
		}
		if(forward) {
			selectedPage = (selectedPage + 1) % amountPages;
		} else {
			selectedPage = (selectedPage - 1) == -1 ? (amountPages - 1) : (selectedPage - 1);
		}
		if(selectedRenderBlock >= (selectedPage * 54) && selectedRenderBlock < ((selectedPage + 1) * 54)) {
			btnRenderBlocks[selectedRenderBlock - selectedPage * 54].setSelected(true);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("Wall Painter", 60, 6, 4210752);
		String str = (selectedPage + 1) + "/" + amountPages;
		int i  = fontRenderer.getStringWidth(str);
		fontRenderer.drawString(str, 37 - (i/2), 46, 4210752);
		drawRenderBlock( 88, 33);
		btnControlls[4].setButtonIndex(wallLogic.getWallState(clickedSide).ordinal());
		GL11.glPushMatrix();
		GL11.glTranslatef(-8.0F, -8.0F, 32.0F);
		int index = selectedPage * 54;
		for (int row = 0; row < 6; row++) {
			for (int column = 0; column < 9; column++) {
				if(index >= WallLogic.validWallBlocks.size()) {
					btnRenderBlocks[index - selectedPage * 54].drawButton = false;
				} else {
					btnRenderBlocks[index - selectedPage * 54].drawButton = true;
					drawItemStack(new ItemStack(WallLogic.validWallBlocks.get(index)), 24 + 16 * column, 65 + 16 * row);
				}
				index++;
			}
		}
		GL11.glPopMatrix();
	}

	private void drawRenderBlock(int x, int y) {
		RictasMain.energyCableDummyRender.metadata = tile.getBlockMetadata();
		RictasMain.energyCableDummyRender.wallLogic = wallLogic;
		ItemStack par1ItemStack = new ItemStack(RictasMain.energyCableDummyRender);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-x - 16, -y - 16, 32.0F);
		GL11.glScaled(2.0, 2.0, 2.0);
		this.zLevel = 200.0F;
		itemRenderer.zLevel = 200.0F;
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, x, y);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, x, y - 16);
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
		GL11.glPopMatrix();
	}
	
	private void drawItemStack(ItemStack par1ItemStack, int x, int y) {
		this.zLevel = 200.0F;
		itemRenderer.zLevel = 200.0F;
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, x, y);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, x, y - 8);
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
	}

}
