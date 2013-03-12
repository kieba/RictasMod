package rictas.energy;


import java.io.IOException;
import java.util.ArrayList;

import rictas.client.GuiWalls;
import rictas.core.PacketTileEntity;
import rictas.helper.ClientServerLogger;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

public class WallLogic {
	
	public enum WallState {
		NONE,
		OPEN_CONNECTION,
		CLOSED_CONNECTION
	}

	public static ArrayList<Block> validWallBlocks = new ArrayList<Block>();
	private boolean[] renderWall = new boolean[6];
	private boolean[] reRenderWall = new boolean[6];
	private int[] renderBlockIds = new int[] { 1, 1, 1, 1, 1, 1 };
	private String[] textureFiles = new String[6];
	private int[] rotations = new int[] { 0, 0, 0, 0, 0, 0 };
	public int clickedSide;
	private int[][] bufferedTexPos = new int[6][6];
	private WallState[] wallStates = new WallState[] {
			WallState.NONE, WallState.NONE, WallState.NONE,
			WallState.NONE, WallState.NONE, WallState.NONE };
	private IWalls wallTile;
	
	public WallLogic(IWalls wallTile) {
		this.wallTile = wallTile;
	}
	
	public static void initValidWalls() {
		for(Block b : Block.blocksList) {
			if(b != null) {
				if(b.isOpaqueCube() && b.getBlockName() != null && b != Block.lockedChest) {
					ClientServerLogger.addLog(b.getBlockName());
					validWallBlocks.add(b);
				}
			}
		}
	}
	
	public WallState getWallState(int side) {
		return wallStates[side];
	}
	
	private void nbtTagInit() {
		for (int i = 0; i < 6; i++) {
			Block renderBlock = null;
			for (Block b : validWallBlocks) {
				if(b.blockID == renderBlockIds[i]) {
					renderBlock  = b;
					break;
				}
			}
			if(renderBlock == null) {
				ClientServerLogger.addLog("NBT TagInit: Wrong blockId detected!" + renderBlockIds[i]);
				return;
			}
			textureFiles[i] = renderBlock.getTextureFile();
			for (int j = 0; j < 6; j++) {
				bufferedTexPos[i][j]= renderBlock.getBlockTextureFromSideAndMetadata(j, 0);
			}
			for (int j = 0; j < Math.abs(rotations[i]); j++) {
				if(rotations[i]>0) {
					rotateTextures(i, true);
				} else {
					rotateTextures(i, false);
				}
			}
			reRenderWall[i] = true;
		}
	}
	
	public boolean renderWall(int side) { 
		return renderWall[side]; 
	}
	
	public boolean reRenderWall(int side) { 
		return reRenderWall[side]; 
	}
	
	public int[] getWallTextures(int side) { 
		return bufferedTexPos[side]; 
	}
	
	public String getWallTextureFile(int side) { 
		return textureFiles[side]; 
	}
	
	public int getRenderBlockId(int side) {
		return renderBlockIds[side];
	}

	public void rotateTextures(int side, boolean left) {
		if(left) {
			int oldNorth = bufferedTexPos[side][2];
			bufferedTexPos[side][2] = bufferedTexPos[side][5];
			bufferedTexPos[side][5] = bufferedTexPos[side][3];
			bufferedTexPos[side][3] = bufferedTexPos[side][4];
			bufferedTexPos[side][4] = oldNorth;
		} else {
			int oldNorth = bufferedTexPos[side][2];
			bufferedTexPos[side][2] = bufferedTexPos[side][4];
			bufferedTexPos[side][4] = bufferedTexPos[side][3];
			bufferedTexPos[side][3] = bufferedTexPos[side][5];
			bufferedTexPos[side][5] = oldNorth;
		}
		reRenderWall[side] = true;
	}
	
	public void enableWall(int side, boolean cutConnection) {
		renderWall[side] = true;
		wallTile.enableWall(side, cutConnection);
	}
	
	public void disableWall(int side){
		renderWall[side] = false;
		wallTile.disableWall(side);
	}
	
	public void changeWallOption(int side) {
		if(wallStates[side] == WallState.NONE) {
			wallStates[side] = WallState.OPEN_CONNECTION;
			enableWall(side, false);
		} else if(wallStates[side] == WallState.OPEN_CONNECTION) {
			wallStates[side] = WallState.CLOSED_CONNECTION;
			enableWall(side, true);
		} else if(wallStates[side] == WallState.CLOSED_CONNECTION) {
			wallStates[side] = WallState.NONE;
			disableWall(side);
		}
	}
		
	public boolean setRenderBlock(int blockId, int side) {
		if(blockId != -1) {
			renderBlockIds[side] = blockId;
			Block renderBlock = null;
			for (Block b : validWallBlocks) {
				if(b.blockID == renderBlockIds[side]) {
					renderBlock = b;
					break;
				}
			}
			if(renderBlock == null) {
				ClientServerLogger.addLog("Wrong blockId detected!" + renderBlockIds[side]);
				return false;
			}
			textureFiles[side] = renderBlock.getTextureFile();
			rotations[side] = 0;
			for (int i = 0; i < bufferedTexPos[side].length; i++) {
				bufferedTexPos[side][i]= renderBlock.getBlockTextureFromSideAndMetadata(i, 0);
			}
			reRenderWall[side] = true;
			return true;
		}
		return false;
	}
	
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		for (int i = 0; i < 6; i++) {
			renderBlockIds[i] = par1nbtTagCompound.getInteger("renderBlockIds"+i);
			rotations[i] = par1nbtTagCompound.getInteger("rotations"+i);
			renderWall[i] = par1nbtTagCompound.getBoolean("renderWall"+i);
			wallStates[i] = WallState.values()[par1nbtTagCompound.getInteger("wallState"+i)];
		}
		nbtTagInit();
	}

	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		for (int i = 0; i < 6; i++) {
			par1nbtTagCompound.setInteger("renderBlockIds"+i, renderBlockIds[i]);
			par1nbtTagCompound.setInteger("rotations"+i, rotations[i]);
			par1nbtTagCompound.setBoolean("renderWall"+i, renderWall[i]);
			par1nbtTagCompound.setInteger("wallState"+i, wallStates[i].ordinal());
		}
	}
	

	public PacketTileEntity createClientUpdateData(PacketTileEntity packet) throws IOException {
		for (int i = 0; i < 6; i++) {
			packet.dos.writeInt(renderBlockIds[i]);
			packet.dos.writeInt(rotations[i]);
			packet.dos.writeBoolean(renderWall[i]);
			packet.dos.writeInt(wallStates[i].ordinal());
		}
		return packet;
	}


	public void receiveClientUpdateData(ByteArrayDataInput data) {
		for (int i = 0; i < 6; i++) {
			renderBlockIds[i] = data.readInt();
			rotations[i] = data.readInt();
			renderWall[i] = data.readBoolean();
			wallStates[i]  = WallState.values()[data.readInt()];
		}
		nbtTagInit();
	}
	
	public boolean handleGuiWallInput(IWalls wall, ByteArrayDataInput data) {
		int buttonIndex = data.readInt();
		switch(buttonIndex) {
			case GuiWalls.BUTTON_ID_START_INDEX + 2:
				this.rotateTextures(clickedSide, true);
				rotations[clickedSide]++;
				return true;
			case GuiWalls.BUTTON_ID_START_INDEX + 3:
				this.rotateTextures(clickedSide, false);
				rotations[clickedSide]--;
				return true;
			case GuiWalls.BUTTON_ID_START_INDEX + 4:
				this.changeWallOption(clickedSide);
				return true;
		}
		int index = buttonIndex - GuiWalls.BUTTON_ID_START_INDEX - 5;
		if(index >= 0 && index < 54) {
			int blockId = data.readInt();
			setRenderBlock(blockId, clickedSide);
			return true;
		}
		return false;
	}
}
