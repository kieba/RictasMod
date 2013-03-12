package rictas.client;


import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import rictas.core.PacketTileEntity;
import rictas.energy.IGuiOutputControl;
import rictas.energy.IGuiPriorityControl;
import rictas.energy.IGuiSideControl;
import rictas.helper.Textures;

import cpw.mods.fml.common.network.PacketDispatcher;

public abstract class GuiController extends GuiContainer {
	
	public static final int BUTTON_ID_START_INDEX = 0;
	protected int controllerPosX  = 0; // top left x Coord. of the controllerGui
	protected int controllerPosY  = 0; // top left Y Coord. of the controllerGui
	protected int xPosOffset  = 0; // x offset from the top left controllerGui to the top left of the normal gui
	protected int yPosOffset  = 0; // y offset from the top left controllerGui to the top left of the normal gui
	private TileEntity tile;
	private IGuiSideControl sideTile = null;
	private IGuiPriorityControl prioTile = null;
	private IGuiOutputControl outputTile = null;
	private GuiButtonSideControl[] btnPageSwitch = new GuiButtonSideControl[2];
	private GuiButtonSideControl[] btnSide = new GuiButtonSideControl[6];
	private GuiButtonSideControl[] btnPrio = new GuiButtonSideControl[6];
	private GuiButtonSideControl[] btnOutput = new GuiButtonSideControl[12];

	public GuiController(Container par1Container, TileEntity tile, int controllerPosX, int controllerPosY) {
		super(par1Container);
		this.xPosOffset = controllerPosX;
		this.yPosOffset = controllerPosY;
		this.tile = tile;
		if(tile instanceof IGuiSideControl) {
			sideTile = (IGuiSideControl) tile;
		}
		if(tile instanceof IGuiPriorityControl) {
			prioTile = (IGuiPriorityControl) tile;
		}
		if(tile instanceof IGuiOutputControl) {
			outputTile = (IGuiOutputControl) tile;
		}
	}

	/*
	 * [0] = output
	 * [1] = input
	 * [2] = in/output
	 * [3] = side
	 */
	private int selectedPage = -1;
	private int selectedOutputSide = 0;
	private int amountPages = 0;
	
	private boolean hasSideTile;
	private boolean hasPrioTile;
	private boolean hasOutputTile;
	
	@Override
	public void initGui() {
		super.initGui();
		this.controllerPosX = xPosOffset + (this.width - this.xSize) / 2;
		this.controllerPosY = yPosOffset + (this.height - this.ySize) / 2;
		int buttonId = BUTTON_ID_START_INDEX;
		btnSide[0] = new GuiButtonSideControl(buttonId++, controllerPosX+35, controllerPosY+71, 9, 9, /*0BOTTOM*/0, 87, Textures.sideControlGuiV2);
		btnSide[1] = new GuiButtonSideControl(buttonId++, controllerPosX+35, controllerPosY+47, 9, 9, /*1TOP   */0, 87, Textures.sideControlGuiV2);
		btnSide[2] = new GuiButtonSideControl(buttonId++, controllerPosX+23, controllerPosY+47, 9, 9, /*2NORTH */0, 87, Textures.sideControlGuiV2);
		btnSide[3] = new GuiButtonSideControl(buttonId++, controllerPosX+23, controllerPosY+71, 9, 9, /*3SOUTH */0, 87, Textures.sideControlGuiV2);
		btnSide[4] = new GuiButtonSideControl(buttonId++, controllerPosX+11, controllerPosY+59, 9, 9, /*4WEST  */0, 87, Textures.sideControlGuiV2);
		btnSide[5] = new GuiButtonSideControl(buttonId++, controllerPosX+35, controllerPosY+59, 9, 9, /*5EAST  */0, 87, Textures.sideControlGuiV2);
		
		btnPageSwitch[0] = new GuiButtonSideControl(buttonId++, controllerPosX+5, controllerPosY+75, 6, 7, 33, 123, Textures.sideControlGuiV2);
		btnPageSwitch[1] = new GuiButtonSideControl(buttonId++, controllerPosX+48, controllerPosY+75, 6, 7, 39, 123, Textures.sideControlGuiV2);
		
		btnPrio[0] = new GuiButtonSideControl(buttonId++, controllerPosX+35, controllerPosY+71, 9, 9, /*0BOTTOM*/33, 105, Textures.sideControlGuiV2);
		btnPrio[1] = new GuiButtonSideControl(buttonId++, controllerPosX+35, controllerPosY+47, 9, 9, /*1TOP   */33, 105, Textures.sideControlGuiV2);
		btnPrio[2] = new GuiButtonSideControl(buttonId++, controllerPosX+23, controllerPosY+47, 9, 9, /*2NORTH */33, 105, Textures.sideControlGuiV2);
		btnPrio[3] = new GuiButtonSideControl(buttonId++, controllerPosX+23, controllerPosY+71, 9, 9, /*3SOUTH */33, 105, Textures.sideControlGuiV2);
		btnPrio[4] = new GuiButtonSideControl(buttonId++, controllerPosX+11, controllerPosY+59, 9, 9, /*4WEST  */33, 105, Textures.sideControlGuiV2);
		btnPrio[5] = new GuiButtonSideControl(buttonId++, controllerPosX+35, controllerPosY+59, 9, 9, /*5EAST  */33, 105, Textures.sideControlGuiV2);
		
		btnOutput[0] = new GuiButtonSideControl(buttonId++, controllerPosX+10, controllerPosY+25, 15, 7, /*---*/33, 137, Textures.sideControlGuiV2);
		btnOutput[1] = new GuiButtonSideControl(buttonId++, controllerPosX+10, controllerPosY+17, 11, 7, /*--   */48, 137, Textures.sideControlGuiV2);
		btnOutput[2] = new GuiButtonSideControl(buttonId++, controllerPosX+22, controllerPosY+17, 7, 7, /*- */59, 137, Textures.sideControlGuiV2);
		btnOutput[3] = new GuiButtonSideControl(buttonId++, controllerPosX+30, controllerPosY+17, 7, 7, /*+ */66, 137, Textures.sideControlGuiV2);
		btnOutput[4] = new GuiButtonSideControl(buttonId++, controllerPosX+38, controllerPosY+17, 11, 7, /*++  */73, 137, Textures.sideControlGuiV2);
		btnOutput[5] = new GuiButtonSideControl(buttonId++, controllerPosX+34, controllerPosY+25, 15, 7, /*+++*/84, 137, Textures.sideControlGuiV2);
		btnOutput[6] = new GuiButtonSideControl(buttonId++, controllerPosX+13, controllerPosY+33, 33, 9, /*NORTH*/0, 105, Textures.sideControlGuiV2);
		btnOutput[7] = new GuiButtonSideControl(buttonId++, controllerPosX+13, controllerPosY+41, 33, 9, /*EAST   */0, 123, Textures.sideControlGuiV2);
		btnOutput[8] = new GuiButtonSideControl(buttonId++, controllerPosX+13, controllerPosY+49, 33, 9, /*SOUTH */0, 141, Textures.sideControlGuiV2);
		btnOutput[9] = new GuiButtonSideControl(buttonId++, controllerPosX+13, controllerPosY+57, 33, 9, /*WEST */0, 159, Textures.sideControlGuiV2);
		btnOutput[10] = new GuiButtonSideControl(buttonId++, controllerPosX+13, controllerPosY+65, 33, 9, /*TOP  */0, 177, Textures.sideControlGuiV2);
		btnOutput[11] = new GuiButtonSideControl(buttonId++, controllerPosX+13, controllerPosY+73, 33, 9, /*BOTTOM  */0, 195, Textures.sideControlGuiV2);
		
		btnOutput[6].setSelected(true);
		selectedOutputSide = ForgeDirection.NORTH.ordinal();
		
		for (int i = 0; i < 6; i++) {
			controlList.add(btnSide[i]);
			btnSide[i].drawButton = false;
		}
		controlList.add(btnPageSwitch[0]);
		controlList.add(btnPageSwitch[1]);
		for (int i = 0; i < 6; i++) {
			controlList.add(btnPrio[i]);
			btnPrio[i].drawButton = false;
		}
		for (int i = 0; i < 12; i++) {
			controlList.add(btnOutput[i]);
			btnOutput[i].drawButton = false;
		}
		drawButtons();
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		PacketTileEntity tilePacket = new PacketTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
		try {
			tilePacket.dos.writeByte(PacketTileEntity.SIDE_CONTROL);
			tilePacket.dos.writeInt(par1GuiButton.id);
		switch(par1GuiButton.id) {
			case BUTTON_ID_START_INDEX + 6:
				switchPage(false);
				break;
			case BUTTON_ID_START_INDEX + 7:
				switchPage(true);
				break;
			case BUTTON_ID_START_INDEX + 14:
				tilePacket.dos.writeInt(selectedOutputSide);
				break;
			case BUTTON_ID_START_INDEX + 15:
				tilePacket.dos.writeInt(selectedOutputSide);
				break;
			case BUTTON_ID_START_INDEX + 16:
				tilePacket.dos.writeInt(selectedOutputSide);
				break;
			case BUTTON_ID_START_INDEX + 17:
				tilePacket.dos.writeInt(selectedOutputSide);
				break;
			case BUTTON_ID_START_INDEX + 18:
				tilePacket.dos.writeInt(selectedOutputSide);
				break;
			case BUTTON_ID_START_INDEX + 19:
				tilePacket.dos.writeInt(selectedOutputSide);
				break;
			case BUTTON_ID_START_INDEX + 20:
				selectedOutputSide = ForgeDirection.NORTH.ordinal();
				selectOutput(0);
				break;
			case BUTTON_ID_START_INDEX + 21:
				selectedOutputSide = ForgeDirection.EAST.ordinal();
				selectOutput(1);
				break;
			case BUTTON_ID_START_INDEX + 22:
				selectedOutputSide = ForgeDirection.SOUTH.ordinal();
				selectOutput(2);
				break;
			case BUTTON_ID_START_INDEX + 23:
				selectedOutputSide = ForgeDirection.WEST.ordinal();
				selectOutput(3);
				break;
			case BUTTON_ID_START_INDEX + 24:
				selectedOutputSide = ForgeDirection.UP.ordinal();
				selectOutput(4);
				break;
			case BUTTON_ID_START_INDEX + 25:
				selectedOutputSide = ForgeDirection.DOWN.ordinal();
				selectOutput(5);
				break;
			default:
				break;
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
		if(forward) {
			selectedPage = (selectedPage + 1) % 3;
		} else {
			selectedPage = (selectedPage - 1) == -1 ? 2 : (selectedPage - 1) ;
		}
		if(selectedPage == 0 && !hasSideTile)
			switchPage(forward);
		else if(selectedPage == 1 && !hasOutputTile) {
			switchPage(forward);
		} else if(selectedPage == 2 && !hasPrioTile) {
			switchPage(forward);
		} else {
			drawButtons();
		}
	}
	
	private void selectOutput(int index) {
		index += 6;
		for (int i = 6; i < 12; i++) {
			btnOutput[i].setSelected(i == index ? true : false);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int tmpAmount = 0;
		boolean reDrawButton = false;
		if(sideTile != null && sideTile.hasSideControl()) {
			hasSideTile = true;
			tmpAmount++;
			if(selectedPage == -1) {
				selectedPage = 0;
				reDrawButton = true;
			}
		} else { 
			hasSideTile = false;
			if(selectedPage == 0) selectedPage = -1;
		}
		if(prioTile != null && prioTile.hasPrioControl()) {
			hasPrioTile = true;
			tmpAmount++;
			if(selectedPage == -1) {
				selectedPage = 2;
				reDrawButton = true;
			}
		} else { 
			hasPrioTile = false; 
			if(selectedPage == 2) selectedPage = -1;
		}
		if(outputTile != null && outputTile.hasOutputControl()) {
			hasOutputTile = true;
			tmpAmount++;
			if(selectedPage == -1) {
				selectedPage = 1;
				reDrawButton = true;
			}
		} else { 
			hasOutputTile = false;
			if(selectedPage == 1) selectedPage = -1;
		}
		if(amountPages != tmpAmount) {
			reDrawButton = true;
			amountPages = tmpAmount;
			if(amountPages == 0) {
				selectedPage = -1;
			}
		}
		int texture = this.mc.renderEngine.getTexture(Textures.sideControlGuiV2);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		if(selectedPage == 0) {
			// draw side
			btnSide[0].setButtonIndex(sideTile.getSideType(ForgeDirection.DOWN).ordinal());
			btnSide[1].setButtonIndex(sideTile.getSideType(ForgeDirection.UP).ordinal());
			btnSide[2].setButtonIndex(sideTile.getSideType(ForgeDirection.NORTH).ordinal());
			btnSide[3].setButtonIndex(sideTile.getSideType(ForgeDirection.SOUTH).ordinal());
			btnSide[4].setButtonIndex(sideTile.getSideType(ForgeDirection.WEST).ordinal());
			btnSide[5].setButtonIndex(sideTile.getSideType(ForgeDirection.EAST).ordinal());
			this.drawTexturedModalRect(controllerPosX, controllerPosY, 0, 0, 59, 87);
		} else if(selectedPage == 1) {
			// draw output
			this.drawTexturedModalRect(controllerPosX, controllerPosY, 59, 0, 59, 87);
		} else if(selectedPage == 2) {
			// draw prio
			btnPrio[0].setButtonIndex(prioTile.getPriority(ForgeDirection.DOWN));
			btnPrio[1].setButtonIndex(prioTile.getPriority(ForgeDirection.UP));
			btnPrio[2].setButtonIndex(prioTile.getPriority(ForgeDirection.NORTH));
			btnPrio[3].setButtonIndex(prioTile.getPriority(ForgeDirection.SOUTH));
			btnPrio[4].setButtonIndex(prioTile.getPriority(ForgeDirection.WEST));
			btnPrio[5].setButtonIndex(prioTile.getPriority(ForgeDirection.EAST));
			this.drawTexturedModalRect(controllerPosX, controllerPosY, 118, 0, 59, 87);
		}	
		if(reDrawButton) drawButtons();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		 if(selectedPage == 1) {
			 fontRenderer.drawString(outputTile.getOutput(ForgeDirection.NORTH)+"",  xPosOffset+21, yPosOffset+34, 4210752);
			 fontRenderer.drawString(outputTile.getOutput(ForgeDirection.EAST)+"",  xPosOffset+21, yPosOffset+42, 4210752);
			 fontRenderer.drawString(outputTile.getOutput(ForgeDirection.SOUTH)+"",  xPosOffset+21, yPosOffset+50, 4210752);
			 fontRenderer.drawString(outputTile.getOutput(ForgeDirection.WEST)+"",  xPosOffset+21, yPosOffset+58, 4210752);
			 fontRenderer.drawString(outputTile.getOutput(ForgeDirection.UP)+"",  xPosOffset+21, yPosOffset+66, 4210752);
			 fontRenderer.drawString(outputTile.getOutput(ForgeDirection.DOWN)+"",  xPosOffset+21, yPosOffset+74, 4210752);
		}
	}

	private void drawButtons() {
		btnPageSwitch[0].drawButton = amountPages > 1 ? true : false;
		btnPageSwitch[1].drawButton = amountPages > 1 ? true : false;
		for (int i = 0; i < 6; i++) {
			btnSide[i].drawButton = selectedPage == 0 ? true : false;
		}
		for (int i = 0; i < 6; i++) {
			btnPrio[i].drawButton = selectedPage == 2 ? true : false;
		}
		for (int i = 0; i < 12; i++) {
			btnOutput[i].drawButton = selectedPage == 1 ? true : false;
		}
	}
	
	
}
