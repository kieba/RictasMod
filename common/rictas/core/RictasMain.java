package rictas.core;

import rictas.energy.BlockEnergyCable;
import rictas.energy.BlockEnergyCableDummyRender;
import rictas.energy.BlockEnergyCableAdv;
import rictas.energy.BlockEnergyGenerator;
import rictas.energy.BlockEnergyStorage;
import rictas.energy.ItemBlockEnergyCable;
import rictas.energy.ItemBlockEnergyCableAdv;
import rictas.energy.ItemBlockEnergyStorage;
import rictas.energy.ItemUpgrade;
import rictas.energy.ItemWallPainter;
import rictas.energy.TileEntityEnergyCable;
import rictas.energy.TileEntityEnergyCableAdv;
import rictas.energy.TileEntityEnergyGenerator;
import rictas.energy.TileEntityEnergyStorage;
import rictas.energy.WallLogic;
import rictas.helper.ModIDs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "RictasMod", name = "name", version = "xyz"/*, dependencies = "required-after:BuildCraft|Core@3.4.3"*/)
@NetworkMod(channels = { PacketHandler.channel }, clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class)
public class RictasMain {

	@SidedProxy(clientSide = "rictas.client.ClientProxy", serverSide = "rictas.core.CommonProxy")
	public static CommonProxy proxy;

	public static GuiHandler guiHandler = new GuiHandler();
	public static final Block energyCable = new BlockEnergyCable(ModIDs.blockEnergyCable, Material.glass);
	public static final Block energyCableSided = new BlockEnergyCableAdv(ModIDs.blockEnergyCableSided, Material.glass);
	public static final Block energyStorage = new BlockEnergyStorage(ModIDs.blockEnergyStorage, Material.rock);
	public static final Block energyGenerator = new BlockEnergyGenerator(ModIDs.blockEnergyGenerator, Material.rock);
	public static final BlockEnergyCableDummyRender energyCableDummyRender = new BlockEnergyCableDummyRender(ModIDs.blockEnergyCableDummy, Material.glass);
	public static final Item upgrades = new ItemUpgrade(ModIDs.itemUpgrades);
	public static final Item wallPainter = new ItemWallPainter(ModIDs.itemWallPainter);

	@Instance("RictasMod")
	public static RictasMain instance = new RictasMain();

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		//test
	}

	@Init
	public void init(FMLInitializationEvent event) {
		proxy.init();
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);		
		GameRegistry.registerBlock(energyCable, ItemBlockEnergyCable.class, "Energy Cable");
		GameRegistry.registerBlock(energyCableDummyRender, ItemBlockEnergyCable.class, "Energy Cable Dummy");
		GameRegistry.registerBlock(energyCableSided, ItemBlockEnergyCableAdv.class, "Energy Cable Sided");
		GameRegistry.registerBlock(energyStorage, ItemBlockEnergyStorage.class, "Energy Storage");
		GameRegistry.registerBlock(energyGenerator, "Energy Generator");
		
		GameRegistry.registerItem(upgrades, "Upgrade");
		GameRegistry.registerItem(wallPainter, "WallPainter");
		
		GameRegistry.registerTileEntity(TileEntityEnergyCable.class, "tileEnergyCable");
		GameRegistry.registerTileEntity(TileEntityEnergyCableAdv.class, "tileEnergyCableSided");
		GameRegistry.registerTileEntity(TileEntityEnergyStorage.class, "tileEnergyStorage");
		GameRegistry.registerTileEntity(TileEntityEnergyGenerator.class,"tileEnergyGenerator");
		LanguageRegistry.addName(energyGenerator, "Energy Generator");
		for(int i=0; i<4;i++) {
			ItemStack multiBlockCable = new ItemStack(energyCable, 1, i);
			LanguageRegistry.addName(multiBlockCable, ItemBlockEnergyCable.subNames[multiBlockCable.getItemDamage()]);
			
			ItemStack multiBlockCableSided = new ItemStack(energyCableSided, 1, i);
			LanguageRegistry.addName(multiBlockCableSided, ItemBlockEnergyCableAdv.subNames[multiBlockCableSided.getItemDamage()]);
			
			ItemStack multiBlockStorage = new ItemStack(energyStorage, 1, i);
			LanguageRegistry.addName(multiBlockStorage, ItemBlockEnergyStorage.subNames[multiBlockStorage.getItemDamage()]);
		}
		for(int i = 0; i < ItemUpgrade.subNames.length; i++) {
			ItemStack multiUpgrades = new ItemStack(upgrades, 1, i);
			LanguageRegistry.addName(multiUpgrades, ItemUpgrade.subNames[multiUpgrades.getItemDamage()]);
		}
		LanguageRegistry.addName(wallPainter, "Wall Painter");
	}

	@PostInit
	public static void postInit(FMLPostInitializationEvent event) {
		WallLogic.initValidWalls();
	}
}
