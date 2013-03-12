package rictas.helper;

import net.minecraftforge.client.MinecraftForgeClient;

public class Textures {
	
	private static String texturePath = "/rictas/";
	public static String testPfeil = "testPfeil.png";
	public static String guiWalls = "wallGui.png";
	public static String blockTextures = "blockTextures.png";
	public static String itemTextures = "itemTextures.png";
	public static String basicPipeModel = "PipeTestModel.png";
	public static String guiEnergyStorage = "energyStorageGui.png";
	public static String guiEnergyGenerator = "energyGeneratorGui.png";
	public static String guiEnergyCableSided = "energyCableSidedGui.png";
	public static String sideControlGui = "sideControlGui.png";
	public static String sideControlGuiV2 = "sideControlGuiV2.png";
	public static String[] basicPipes = {"PipeV1.png","PipeV2.png","PipeV3.png","PipeV4.png"};
	public static String[][] sidedPipes = {
		{"SidedPipeV1_O.png","SidedPipeV1_I.png","SidedPipeV1_IO.png"},
		{"SidedPipeV2_O.png","SidedPipeV2_I.png","SidedPipeV2_IO.png"},
		{"SidedPipeV3_O.png","SidedPipeV3_I.png","SidedPipeV3_IO.png"},
		{"SidedPipeV4_O.png","SidedPipeV4_I.png","SidedPipeV4_IO.png"}};

	
	public static void init() {
		testPfeil = prepareAndPreloadTexture(testPfeil);
		guiWalls = prepareAndPreloadTexture(guiWalls);
		blockTextures = prepareAndPreloadTexture(blockTextures);
		itemTextures = prepareAndPreloadTexture(itemTextures);
		basicPipeModel = prepareAndPreloadTexture(basicPipeModel);
		guiEnergyStorage = prepareAndPreloadTexture(guiEnergyStorage);
		guiEnergyGenerator = prepareAndPreloadTexture(guiEnergyGenerator);
		guiEnergyCableSided = prepareAndPreloadTexture(guiEnergyCableSided);
		sideControlGui = prepareAndPreloadTexture(sideControlGui);
		sideControlGuiV2 = prepareAndPreloadTexture(sideControlGuiV2);
		for (int i = 0; i < 4; i++) {
			basicPipes[i] = prepareAndPreloadTexture(basicPipes[i]);
			for (int a = 0; a < 3; a++) {
				sidedPipes[i][a] = prepareAndPreloadTexture(sidedPipes[i][a]);
			}
		}
	}
	
	private static String prepareAndPreloadTexture(String texture)  {
		texture = texturePath + texture;
		MinecraftForgeClient.preloadTexture(texture);
		return texture;
	}

}
