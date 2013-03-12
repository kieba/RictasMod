package rictas.client;

import rictas.helper.Textures;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.ForgeHooksClient;

public class ModelEnergyCable extends ModelBase
{
    /*
    [0] = DOWN(0, -1, 0), 
    [1] = UP(0, 1, 0),
    [2] = NORTH(0, 0, -1),
    [3] = SOUTH(0, 0, 1),
    [4] = WEST(-1, 0, 0),
    [5] = EAST(1, 0, 0),
    [6] = CENTER
    [7] = DOWN_END
    [8] = UP_END
    [9] = NORTH_END
    [10] = SOUTH_END
    [11] = WEST_END
    [12] = EAST_END
    */
    ModelRenderer[] models = new ModelRenderer[13];
   
    private static float[][] modelOff = new float[][] {{-2F, 0F, -2F}/*DOWN*/, {-2F, 0F, -2F}/*UP*/, {0F, -2F, -2F}/*NORTH*/, {0F, -2F, -2F}/*SOUTH*/, {-16F, -2F, -2F}/*WEST*/, {-16F, -2F, -2F}/*EAST*/, {-3F, -3F, -3F}/*Center*/, {-3F, -16F, -3F}/*DOWNend*/, {-3F, 14F, -3F}/*UPend*/, {-3F, -3F, -16F}/*NORTHend*/, {-3F, -3F, 14F}/*SOUTHend*/, {-16F, -3F, -3F}/*WESTend*/, {14F, -3F, -3F}/*EASTend*/, {-2F, 0F, -2F}/*DOWN*/, {-2F, 0F, -2F}/*UP*/, {0F, -2F, -2F}/*NORTH*/, {0F, -2F, -2F}/*SOUTH*/, {-16F, -2F, -2F}/*WEST*/, {-16F, -2F, -2F}/*EAST*/, {-3F, -3F, -3F}/*Center*/, {-3F, -16F, -3F}/*DOWNend*/, {-3F, 14F, -3F}/*UPend*/, {-3F, -3F, -16F}/*NORTHend*/, {-3F, -3F, 14F}/*SOUTHend*/, {-16F, -3F, -3F}/*WESTend*/, {14F, -3F, -3F}/*EASTend*/, {-2F, 0F, -2F}/*DOWN*/, {-2F, 0F, -2F}/*UP*/, {0F, -2F, -2F}/*NORTH*/, {0F, -2F, -2F}/*SOUTH*/, {-16F, -2F, -2F}/*WEST*/, {-16F, -2F, -2F}/*EAST*/, {-3F, -3F, -3F}/*Center*/, {-3F, -16F, -3F}/*DOWNend*/, {-3F, 14F, -3F}/*UPend*/, {-3F, -3F, -16F}/*NORTHend*/, {-3F, -3F, 14F}/*SOUTHend*/, {-16F, -3F, -3F}/*WESTend*/, {14F, -3F, -3F}/*EASTend*/};

    private static int[][] modelDim = new int[][] {{4, 16, 4}/*DOWN*/, {4, 16, 4}/*UP*/, {16, 4, 4}/*NORTH*/, {16, 4, 4}/*SOUTH*/, {16, 4, 4}/*WEST*/, {16, 4, 4}/*EAST*/, {6, 6, 6}/*Center*/, {6, 2, 6}/*DOWNend*/, {6, 2, 6}/*UPend*/, {6, 6, 2}/*NORTHend*/, {6, 6, 2}/*SOUTHend*/, {2, 6, 6}/*WESTend*/, {2, 6, 6}/*EASTend*/, {4, 16, 4}/*DOWN*/, {4, 16, 4}/*UP*/, {16, 4, 4}/*NORTH*/, {16, 4, 4}/*SOUTH*/, {16, 4, 4}/*WEST*/, {16, 4, 4}/*EAST*/, {6, 6, 6}/*Center*/, {6, 2, 6}/*DOWNend*/, {6, 2, 6}/*UPend*/, {6, 6, 2}/*NORTHend*/, {6, 6, 2}/*SOUTHend*/, {2, 6, 6}/*WESTend*/, {2, 6, 6}/*EASTend*/, {4, 16, 4}/*DOWN*/, {4, 16, 4}/*UP*/, {16, 4, 4}/*NORTH*/, {16, 4, 4}/*SOUTH*/, {16, 4, 4}/*WEST*/, {16, 4, 4}/*EAST*/, {6, 6, 6}/*Center*/, {6, 2, 6}/*DOWNend*/, {6, 2, 6}/*UPend*/, {6, 6, 2}/*NORTHend*/, {6, 6, 2}/*SOUTHend*/, {2, 6, 6}/*WESTend*/, {2, 6, 6}/*EASTend*/};

    private static float[][] modelRotations = new float[][] {{3.142F, 0.7853982F, 0F}/*DOWN*/, {0F, 0.7853982F, 0F}/*UP*/, {0.7853982F, 1.570796F, 0F}/*NORTH*/, {0.7853982F, -1.570796F, 0F}/*SOUTH*/, {0.7853982F, 0F, 0F}/*WEST*/, {0.7853982F, 3.141593F, 0F}/*EAST*/, {0F, 0F, 0F}/*Center*/, {0F, 0F, 0F}/*DOWNend*/, {0F, 0F, 0F}/*UPend*/, {0F, 0F, 0F}/*NORTHend*/, {0F, 0F, 0F}/*SOUTHend*/, {0F, 0F, 0F}/*WESTend*/, {0F, 0F, 0F}/*EASTend*/, {3.142F, 0.7853982F, 0F}/*DOWN*/, {0F, 0.7853982F, 0F}/*UP*/, {0.7853982F, 1.570796F, 0F}/*NORTH*/, {0.7853982F, -1.570796F, 0F}/*SOUTH*/, {0.7853982F, 0F, 0F}/*WEST*/, {0.7853982F, 3.141593F, 0F}/*EAST*/, {0F, 0F, 0F}/*Center*/, {0F, 0F, 0F}/*DOWNend*/, {0F, 0F, 0F}/*UPend*/, {0F, 0F, 0F}/*NORTHend*/, {0F, 0F, 0F}/*SOUTHend*/, {0F, 0F, 0F}/*WESTend*/, {0F, 0F, 0F}/*EASTend*/, {3.142F, 0.7853982F, 0F}/*DOWN*/, {0F, 0.7853982F, 0F}/*UP*/, {0.7853982F, 1.570796F, 0F}/*NORTH*/, {0.7853982F, -1.570796F, 0F}/*SOUTH*/, {0.7853982F, 0F, 0F}/*WEST*/, {0.7853982F, 3.141593F, 0F}/*EAST*/, {0F, 0F, 0F}/*Center*/, {0F, 0F, 0F}/*DOWNend*/, {0F, 0F, 0F}/*UPend*/, {0F, 0F, 0F}/*NORTHend*/, {0F, 0F, 0F}/*SOUTHend*/, {0F, 0F, 0F}/*WESTend*/, {0F, 0F, 0F}/*EASTend*/};

    private static float[][] modelRotationPoint = new float[][] {{0F, 0F, 0F}/*DOWN*/, {0F, 0F, 0F}/*UP*/, {0F, 0F, 0F}/*NORTH*/, {0F, 0F, 0F}/*SOUTH*/, {0F, 0F, 0F}/*WEST*/, {0F, 0F, 0F}/*EAST*/, {0F, 0F, 0F}/*Center*/, {0F, 0F, 0F}/*DOWNend*/, {0F, 0F, 0F}/*UPend*/, {0F, 0F, 0F}/*NORTHend*/, {0F, 0F, 0F}/*SOUTHend*/, {0F, 0F, 0F}/*WESTend*/, {0F, 0F, 0F}/*EASTend*/, {0F, 0F, 0F}/*DOWN*/, {0F, 0F, 0F}/*UP*/, {0F, 0F, 0F}/*NORTH*/, {0F, 0F, 0F}/*SOUTH*/, {0F, 0F, 0F}/*WEST*/, {0F, 0F, 0F}/*EAST*/, {0F, 0F, 0F}/*Center*/, {0F, 0F, 0F}/*DOWNend*/, {0F, 0F, 0F}/*UPend*/, {0F, 0F, 0F}/*NORTHend*/, {0F, 0F, 0F}/*SOUTHend*/, {0F, 0F, 0F}/*WESTend*/, {0F, 0F, 0F}/*EASTend*/, {0F, 0F, 0F}/*DOWN*/, {0F, 0F, 0F}/*UP*/, {0F, 0F, 0F}/*NORTH*/, {0F, 0F, 0F}/*SOUTH*/, {0F, 0F, 0F}/*WEST*/, {0F, 0F, 0F}/*EAST*/, {0F, 0F, 0F}/*Center*/, {0F, 0F, 0F}/*DOWNend*/, {0F, 0F, 0F}/*UPend*/, {0F, 0F, 0F}/*NORTHend*/, {0F, 0F, 0F}/*SOUTHend*/, {0F, 0F, 0F}/*WESTend*/, {0F, 0F, 0F}/*EASTend*/};

    private static int[][] texturesOff = new int[][] {{0, 0}/*DOWN*/, {0, 0}/*UP*/, {24, 0}/*NORTH*/, {24, 8}/*SOUTH*/, {24, 16}/*WEST*/, {24, 24}/*EAST*/, {0, 20}/*Center*/, {0, 20}/*DOWNend*/, {0, 20}/*UPend*/, {0, 20}/*NORTHend*/, {0, 20}/*SOUTHend*/, {0, 20}/*WESTend*/, {0, 20}/*EASTend*/};

  public ModelEnergyCable()
  {
	textureWidth = 64;
	textureHeight = 32;
    for(int i=0; i<13; i++) {
    	models[i] = new ModelRenderer(this, texturesOff[i][0], texturesOff[i][1]);
        models[i].addBox(modelOff[i][0],modelOff[i][1],modelOff[i][2],modelDim[i][0],modelDim[i][1],modelDim[i][2]);
        models[i].setRotationPoint(0F, 0F, 0F);
        models[i].setTextureSize(textureWidth, textureHeight);
        models[i].rotateAngleX = modelRotations[i][0];
        models[i].rotateAngleY = modelRotations[i][1];
        models[i].rotateAngleZ = modelRotations[i][2];
    }
  }
  
  public void renderAdvPipe(float scale, int conSides, int inputSides, int outputSides, int metadata)
  {
	ForgeHooksClient.bindTexture(Textures.sidedPipes[metadata][2],0);
    int sideBit = 0x01;
    for(int i=0;i<13;i++) {
    	if(i==6) {
    		sideBit <<= 1;
    		continue;
    	}
    	if((conSides & sideBit) == sideBit) {
    		if(i<6) {
    			boolean isInput = ((inputSides >> i) & 0x01) == 0x01;
    			boolean isOutput = ((outputSides  >> i) & 0x01) == 0x01;
    			if(isInput && isOutput) {
    				ForgeHooksClient.bindTexture(Textures.sidedPipes[metadata][2],0);
    			} else if(isOutput) {
    				ForgeHooksClient.bindTexture(Textures.sidedPipes[metadata][0],0);
    			} else if(isInput) {
    				ForgeHooksClient.bindTexture(Textures.sidedPipes[metadata][1],0);
    			}
    		} else {
    			ForgeHooksClient.bindTexture(Textures.basicPipes[metadata],0);
    		}
    		models[i].render(scale);
    	}
    	sideBit <<= 1;
    }
    conSides &= 0x3F; //just set the first 6 bit
    if(conSides != 0x03 && conSides != 0x0C && conSides != 0x30) {
    	models[6].render(scale);
    }

  }
  
  public void renderBasicPipe(float scale, int conSides, int metadata)
  {
	  ForgeHooksClient.bindTexture(Textures.basicPipes[metadata],0);
    int sideBit = 0x01;
    for(int i=0;i<13;i++) {
    	if(i==6) {
    		sideBit <<= 1;
    		continue;
    	}
    	if((conSides & sideBit) == sideBit) {
    		models[i].render(scale);
    	}
    	sideBit <<= 1;
    }
    conSides &= 0x3F; //just set the first 6 bit
    if(conSides != 0x03 && conSides != 0x0C && conSides != 0x30) {
    	models[6].render(scale);
    }
  }

}