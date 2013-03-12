package rictas.client;

import rictas.energy.WallLogic;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.ForgeHooksClient;

public class ModelWalls {
	
    /** 
    -X 
    WEST(-1, 0, 0),

    +X 
    EAST(1, 0, 0),
    
    -Y 
    DOWN(0, -1, 0),

    +Y 
    UP(0, 1, 0),

    -Z 
    NORTH(0, 0, -1),

    +Z 
    SOUTH(0, 0, 1),
	*/
	// WEST - DOWN - NORTH
	private static PositionTextureVertex pos10 = new PositionTextureVertex(-8.0F, -8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos11 = new PositionTextureVertex(-6.0F, -8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos12 = new PositionTextureVertex(-8.0F, -6.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos13 = new PositionTextureVertex(-8.0F, -8.0F, -6.0F, 0.0F, 0.0F);
	
	// WEST - DOWN - SOUTH
	private static PositionTextureVertex pos20 = new PositionTextureVertex(-8.0F, -8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos21 = new PositionTextureVertex(-6.0F, -8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos22 = new PositionTextureVertex(-8.0F, -6.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos23 = new PositionTextureVertex(-8.0F, -8.0F, 6.0F, 0.0F, 0.0F);
		
	// WEST - UP - NORTH
	private static PositionTextureVertex pos30 = new PositionTextureVertex(-8.0F, 8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos31 = new PositionTextureVertex(-6.0F, 8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos32 = new PositionTextureVertex(-8.0F, 6.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos33 = new PositionTextureVertex(-8.0F, 8.0F, -6.0F, 0.0F, 0.0F);
		
	// WEST - UP - SOUTH
	private static PositionTextureVertex pos40 = new PositionTextureVertex(-8.0F, 8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos41 = new PositionTextureVertex(-6.0F, 8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos42 = new PositionTextureVertex(-8.0F, 6.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos43 = new PositionTextureVertex(-8.0F, 8.0F, 6.0F, 0.0F, 0.0F);
	
	// EAST - DOWN - NORTH
	private static PositionTextureVertex pos50 = new PositionTextureVertex(8.0F, -8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos51 = new PositionTextureVertex(6.0F, -8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos52 = new PositionTextureVertex(8.0F, -6.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos53 = new PositionTextureVertex(8.0F, -8.0F, -6.0F, 0.0F, 0.0F);
		
	// EAST - DOWN - SOUTH
	private static PositionTextureVertex pos60 = new PositionTextureVertex(8.0F, -8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos61 = new PositionTextureVertex(6.0F, -8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos62 = new PositionTextureVertex(8.0F, -6.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos63 = new PositionTextureVertex(8.0F, -8.0F, 6.0F, 0.0F, 0.0F);
			
	// EAST - UP - NORTH
	private static PositionTextureVertex pos70 = new PositionTextureVertex(8.0F, 8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos71 = new PositionTextureVertex(6.0F, 8.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos72 = new PositionTextureVertex(8.0F, 6.0F, -8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos73 = new PositionTextureVertex(8.0F, 8.0F, -6.0F, 0.0F, 0.0F);
			
	// EAST - UP - SOUTH
	private static PositionTextureVertex pos80 = new PositionTextureVertex(8.0F, 8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos81 = new PositionTextureVertex(6.0F, 8.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos82 = new PositionTextureVertex(8.0F, 6.0F, 8.0F, 0.0F, 0.0F);
	private static PositionTextureVertex pos83 = new PositionTextureVertex(8.0F, 8.0F, 6.0F, 0.0F, 0.0F);
		
	//first index: blockSide second rectSide
	private TexturedQuad[][] walls =  new TexturedQuad[6][6];
	
	private static int[][] defaultTextures = new int[][] {
			{ 1, 1, 1, 1, 1, 1}, { 1, 1, 1, 1, 1, 1},
			{ 1, 1, 1, 1, 1, 1}, { 1, 1, 1, 1, 1, 1},
			{ 1, 1, 1, 1, 1, 1}, { 1, 1, 1, 1, 1, 1} };

	public ModelWalls() {
		initWalls(defaultTextures);
	}
	
	private void reRenderWall(int side, int[] texturePositions) {
		switch(side) {
			case 0:
				prepareDown(texturePositions);
				break;
			case 1:
				prepareUp(texturePositions);
				break;
			case 2:
				prepareNorth(texturePositions);
				break;
			case 3:
				prepareSouth(texturePositions);
				break;
			case 4:
				prepareWest(texturePositions);
				break;
			case 5:
				prepareEast(texturePositions);
				break;
		}
	}
	
	private void initWalls(int[][] texturePositions) {
		prepareDown(texturePositions[0]);
		prepareUp(texturePositions[1]);
		prepareNorth(texturePositions[2]);
		prepareSouth(texturePositions[3]);
		prepareWest(texturePositions[4]);
		prepareEast(texturePositions[5]);
	}
	
	private int[] getTextureBounds(int texturePos) {
		return new int[] {/*TOP*/(texturePos / 16),/*BOTTOM*/(texturePos / 16 + 1),/*LEFT*/ (texturePos % 16),/*RIGHT*/(texturePos % 16 + 1)};
	}
	
	private void prepareDown(int[] texturePos) {
		//DOWN - DOWN (blockSide - rectSide)
		int[] texBounds = getTextureBounds(texturePos[0]);
		walls[0][0] = new TexturedQuad(new PositionTextureVertex[] { pos60, pos20, pos10, pos50}, 
				texBounds[2] * 16, texBounds[1] * 16, texBounds[3] * 16, texBounds[0] * 16, 256F, 256F);
		
		//DOWN - UP (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[1]);
		walls[0][1] = new TexturedQuad(new PositionTextureVertex[] { pos22, pos62, pos52, pos12}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//DOWN - NORTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[2]);
		walls[0][2] = new TexturedQuad(new PositionTextureVertex[] { pos50, pos10, pos12, pos52}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16 + 14, 256F, 256F);
		
		//DOWN - SOUTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[3]);
		walls[0][3] = new TexturedQuad(new PositionTextureVertex[] { pos20, pos60, pos62, pos22}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16 + 14, 256F, 256F);
		
		//DOWN - WEST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[4]);
		walls[0][4] = new TexturedQuad(new PositionTextureVertex[] { pos10, pos20, pos22, pos12}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16 + 14, 256F, 256F);
		
		//DOWN - EAST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[5]);
		walls[0][5] = new TexturedQuad(new PositionTextureVertex[] { pos60, pos50, pos52, pos62}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16 + 14, 256F, 256F);
	}

	private void prepareUp(int[] texturePos) {
		//UP - DOWN (blockSide - rectSide)
		int[] texBounds = getTextureBounds(texturePos[0]);
		walls[1][0] = new TexturedQuad(new PositionTextureVertex[] { pos82, pos42, pos32, pos72}, 
				texBounds[2] * 16, texBounds[1] * 16, texBounds[3] * 16, texBounds[0] * 16, 256F, 256F);
		
		//UP - UP (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[1]);
		walls[1][1] = new TexturedQuad(new PositionTextureVertex[] { pos40, pos80, pos70, pos30}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//UP - NORTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[2]);
		walls[1][2] = new TexturedQuad(new PositionTextureVertex[] { pos72, pos32, pos30, pos70}, 
				texBounds[3] * 16, texBounds[1] * 16 - 14, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//UP - SOUTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[3]);
		walls[1][3] = new TexturedQuad(new PositionTextureVertex[] { pos42, pos82, pos80, pos40}, 
				texBounds[3] * 16, texBounds[1] * 16 - 14, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//UP - WEST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[4]);
		walls[1][4] = new TexturedQuad(new PositionTextureVertex[] { pos32, pos42, pos40, pos30}, 
				texBounds[3] * 16, texBounds[1] * 16 - 14, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//UP - EAST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[5]);
		walls[1][5] = new TexturedQuad(new PositionTextureVertex[] { pos82, pos72, pos70, pos80}, 
				texBounds[3] * 16, texBounds[1] * 16 - 14, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);	
	}
	
	private void prepareNorth(int[] texturePos) {
		//NORTH - DOWN (blockSide - rectSide)
		int[] texBounds = getTextureBounds(texturePos[0]);
		walls[2][0] = new TexturedQuad(new PositionTextureVertex[] { pos53, pos13, pos10, pos50}, 
				texBounds[2] * 16, texBounds[1] * 16 - 14, texBounds[3] * 16, texBounds[0] * 16, 256F, 256F);

		//NORTH - UP (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[1]);
		walls[2][1] = new TexturedQuad(new PositionTextureVertex[] { pos33, pos73, pos70, pos30}, 
				texBounds[3] * 16, texBounds[1] * 16 - 14, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//NORTH - NORTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[2]);
		walls[2][2] = new TexturedQuad(new PositionTextureVertex[] { pos50, pos10, pos30, pos70}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//NORTH - SOUTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[3]);
		walls[2][3] = new TexturedQuad(new PositionTextureVertex[] { pos13, pos53, pos73, pos33}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//NORTH - WEST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[4]);
		walls[2][4] = new TexturedQuad(new PositionTextureVertex[] { pos10, pos13, pos33, pos30}, 
				texBounds[3] * 16 - 14, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//NORTH - EAST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[5]);
		walls[2][5] = new TexturedQuad(new PositionTextureVertex[] { pos53, pos50, pos70, pos73}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16 + 14, texBounds[0] * 16, 256F, 256F);	
	}
	
	private void prepareSouth(int[] texturePos) {
		//SOUTH - DOWN (blockSide - rectSide)
		int[] texBounds = getTextureBounds(texturePos[0]);
		walls[3][0] = new TexturedQuad(new PositionTextureVertex[] { pos60, pos20, pos23, pos63}, 
				texBounds[2] * 16, texBounds[1] * 16, texBounds[3] * 16, texBounds[0] * 16 + 14, 256F, 256F);
		
		//SOUTH - UP (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[1]);
		walls[3][1] = new TexturedQuad(new PositionTextureVertex[] { pos40, pos80, pos83, pos43}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16 + 14, 256F, 256F);
		
		//SOUTH - NORTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[2]);
		walls[3][2] = new TexturedQuad(new PositionTextureVertex[] { pos63, pos23, pos43, pos83}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//SOUTH - SOUTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[3]);
		walls[3][3] = new TexturedQuad(new PositionTextureVertex[] { pos20, pos60, pos80, pos40}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//SOUTH - WEST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[4]);
		walls[3][4] = new TexturedQuad(new PositionTextureVertex[] { pos23, pos20, pos40, pos43}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16 + 14, texBounds[0] * 16, 256F, 256F);
		
		//SOUTH - EAST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[5]);
		walls[3][5] = new TexturedQuad(new PositionTextureVertex[] { pos60, pos63, pos83, pos80}, 
				texBounds[3] * 16 - 14, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
	}
	
	private void prepareWest(int[] texturePos) {
		//WEST - DOWN (blockSide - rectSide)
		int[] texBounds = getTextureBounds(texturePos[0]);
		walls[4][0] = new TexturedQuad(new PositionTextureVertex[] { pos21, pos20, pos10, pos11}, 
				texBounds[2] * 16, texBounds[1] * 16, texBounds[3] * 16 - 14, texBounds[0] * 16, 256F, 256F);
		
		//WEST - UP (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[1]);
		walls[4][1] = new TexturedQuad(new PositionTextureVertex[] { pos40, pos41, pos31, pos30}, 
				texBounds[3] * 16 - 14, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//WEST - NORTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[2]);
		walls[4][2] = new TexturedQuad(new PositionTextureVertex[] { pos11, pos10, pos30, pos31}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16 + 14, texBounds[0] * 16, 256F, 256F);
		
		//WEST - SOUTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[3]);
		walls[4][3] = new TexturedQuad(new PositionTextureVertex[] { pos20, pos21, pos41, pos40}, 
				texBounds[3] * 16 - 14, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//WEST - WEST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[4]);
		walls[4][4] = new TexturedQuad(new PositionTextureVertex[] { pos10, pos20, pos40, pos30}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
		
		//WEST - EAST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[5]);
		walls[4][5] = new TexturedQuad(new PositionTextureVertex[] { pos21, pos11, pos31, pos41}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
	}
	
	private void prepareEast(int[] texturePos) {
		//EAST - DOWN (blockSide - rectSide)
		int[] texBounds = getTextureBounds(texturePos[0]);
		walls[5][0] = new TexturedQuad(new PositionTextureVertex[] { pos60, pos61, pos51, pos50}, 
				texBounds[2] * 16 + 14, texBounds[1] * 16, texBounds[3] * 16, texBounds[0] * 16, 256F, 256F);

		texBounds = getTextureBounds(texturePos[1]);
		walls[5][1] = new TexturedQuad(new PositionTextureVertex[] { pos81, pos80, pos70, pos71}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16 + 14, texBounds[0] * 16, 256F, 256F);
				
		//EAST - NORTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[2]);
		walls[5][2] = new TexturedQuad(new PositionTextureVertex[] { pos50, pos51, pos71, pos70}, 
				texBounds[3] * 16 - 14, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
					
		//EAST - SOUTH (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[3]);
		walls[5][3] = new TexturedQuad(new PositionTextureVertex[] { pos61, pos60, pos80, pos81}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16 + 14, texBounds[0] * 16, 256F, 256F);
		
		//EAST - WEST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[4]);
		walls[5][4] = new TexturedQuad(new PositionTextureVertex[] { pos51, pos61, pos81, pos71}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
						
		//EAST - EAST (blockSide - rectSide)
		texBounds = getTextureBounds(texturePos[5]);
		walls[5][5] = new TexturedQuad(new PositionTextureVertex[] { pos60, pos50, pos70, pos80}, 
				texBounds[3] * 16, texBounds[1] * 16, texBounds[2] * 16, texBounds[0] * 16, 256F, 256F);
	}

	public void render(float scale, WallLogic wL) {
		for (int blockSide = 0; blockSide < 6; blockSide++) {
			if(wL.reRenderWall(blockSide)) reRenderWall(blockSide, wL.getWallTextures(blockSide));
			if(!wL.renderWall(blockSide)) continue;
			ForgeHooksClient.bindTexture(wL.getWallTextureFile(blockSide),0);
			for (int rectSide = 0; rectSide < 6; rectSide++) {
				if(wL.renderWall(rectSide) && blockSide != rectSide) continue;
				walls[blockSide][rectSide].draw(Tessellator.instance, scale);	
			}
		}
	}

}
