package rictas.core;

import rictas.client.GuiController;
import rictas.client.GuiEnergyCableAdv;
import rictas.client.GuiEnergyGenerator;
import rictas.client.GuiEnergyStorage;
import rictas.client.GuiWalls;
import rictas.energy.ContainerCableAdv;
import rictas.energy.ContainerGenerator;
import rictas.energy.ContainerStorage;
import rictas.energy.IGuiOutputControl;
import rictas.energy.IGuiPriorityControl;
import rictas.energy.IGuiSideControl;
import rictas.energy.TileEntityEnergyCable;
import rictas.energy.TileEntityEnergyCableAdv;
import rictas.energy.TileEntityEnergyGenerator;
import rictas.energy.TileEntityEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(id == GuiWalls.guiWallId) {
        	return new Container() {
				@Override
				public boolean canInteractWith(EntityPlayer var1) {
					return true;
				}
        	};
        } else if(tileEntity instanceof TileEntityEnergyStorage){
            return new ContainerStorage(player.inventory, (TileEntityEnergyStorage) tileEntity);
        } else if(tileEntity instanceof TileEntityEnergyGenerator){
            ((TileEntityEnergyGenerator) tileEntity).updateClientBurning();
            return new ContainerGenerator(player.inventory, (TileEntityEnergyGenerator) tileEntity);
        } else if(tileEntity instanceof TileEntityEnergyCableAdv){
        	return new ContainerCableAdv(player.inventory, (TileEntityEnergyCableAdv) tileEntity);
        }
        return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		 TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		 if(id == GuiWalls.guiWallId) {
        	 return new GuiWalls(player.inventory,(TileEntityEnergyCable) tileEntity);
         } else if(tileEntity instanceof TileEntityEnergyStorage){
            return new GuiEnergyStorage(player.inventory, (TileEntityEnergyStorage)tileEntity);
         } else if(tileEntity instanceof TileEntityEnergyGenerator) {
        	 return new GuiEnergyGenerator(player.inventory, (TileEntityEnergyGenerator)tileEntity);
         } else if(tileEntity instanceof TileEntityEnergyCableAdv) {
        	 return new GuiEnergyCableAdv(player.inventory, (TileEntityEnergyCableAdv)tileEntity);
         }
         return null;
	}
	
	public static boolean handleGuiControllerInput(TileEntity tile, ByteArrayDataInput data) {
		IGuiSideControl sideTile = null;
		IGuiPriorityControl prioTile = null;
		IGuiOutputControl outputTile = null;
		if(tile instanceof IGuiSideControl) sideTile = (IGuiSideControl) tile;
		if(tile instanceof IGuiPriorityControl) prioTile = (IGuiPriorityControl) tile;
		if(tile instanceof IGuiOutputControl) outputTile = (IGuiOutputControl) tile;
		switch(data.readInt()) {
		case GuiController.BUTTON_ID_START_INDEX + 0:
			if(sideTile!=null) {
				sideTile.changeSide(ForgeDirection.getOrientation(0));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 1:
			if(sideTile!=null){
				sideTile.changeSide(ForgeDirection.getOrientation(1));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 2:
			if(sideTile!=null){
				sideTile.changeSide(ForgeDirection.getOrientation(2));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 3:
			if(sideTile!=null){
				sideTile.changeSide(ForgeDirection.getOrientation(3));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 4:
			if(sideTile!=null){
				sideTile.changeSide(ForgeDirection.getOrientation(4));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 5:
			if(sideTile!=null){
				sideTile.changeSide(ForgeDirection.getOrientation(5));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 8:
			if(prioTile!=null){
				prioTile.increasePrio(ForgeDirection.getOrientation(0));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 9:
			if(prioTile!=null){
				prioTile.increasePrio(ForgeDirection.getOrientation(1));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 10:
			if(prioTile!=null){
				prioTile.increasePrio(ForgeDirection.getOrientation(2));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 11:
			if(prioTile!=null){
				prioTile.increasePrio(ForgeDirection.getOrientation(3));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 12:
			if(prioTile!=null){
				prioTile.increasePrio(ForgeDirection.getOrientation(4));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 13:
			if(prioTile!=null){
				prioTile.increasePrio(ForgeDirection.getOrientation(5));
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 14:
			if(outputTile!=null){
				outputTile.decreaseOutput(ForgeDirection.getOrientation(data.readInt()), 100);
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 15:
			if(outputTile!=null){
				outputTile.decreaseOutput(ForgeDirection.getOrientation(data.readInt()), 10);
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 16:
			if(outputTile!=null){
				outputTile.decreaseOutput(ForgeDirection.getOrientation(data.readInt()), 1);
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 17:
			if(outputTile!=null){
				outputTile.increaseOutput(ForgeDirection.getOrientation(data.readInt()), 1);
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 18:
			if(outputTile!=null){
				outputTile.increaseOutput(ForgeDirection.getOrientation(data.readInt()), 10);
				return true;
			}
		case GuiController.BUTTON_ID_START_INDEX + 19:
			if(outputTile!=null) {
				outputTile.increaseOutput(ForgeDirection.getOrientation(data.readInt()), 100);
				return true;
			}
		}
		return false;
	}
}
