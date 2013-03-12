package rictas.client;

import rictas.core.CommonProxy;
import rictas.energy.TileEntityEnergyCable;
import rictas.energy.TileEntityEnergyCableSided;
import rictas.helper.Textures;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	public final static InventoryRenderer energyCableWorldRenderer = new InventoryRenderer();
	
	public void init() {
		initTileEntities();
		Textures.init();
		registerRenderers();
	}

	public void initTileEntities() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyCable.class, new TileEntityEnergyCableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyCableSided.class, new TileEntityEnergyCableRenderer());
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
	public void registerRenderers() {
		CommonProxy.cableRenderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(energyCableWorldRenderer);
	}

}
