package rictas.core;


import java.io.IOException;

import rictas.helper.ClientServerLogger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public abstract class TileEntityBase extends TileEntity implements IPacketReceiver {

	protected boolean initialized = false;
	private boolean initRequested = false;
	private boolean nbtLoaded = false;
	
	/**
	 * If this is set to true, in the next tick the server will send a clientUpdatePacket
	 */
	protected boolean clientUpdate = false;

	@Override
	public void updateEntity() {
		if(!initRequested) {
			getBlockMetadata();
			metadataInit();
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockType().blockID);
			if(worldObj.isRemote) {
				requestClientUpdate(); //Client Only
			} else {
				initialized = true;
			}
			initRequested = true;
		} else {
			if(initialized) {
				if(clientUpdate) {
					sendClientUpdatePacket();	
					clientUpdate = false;
				}
				update();
			}
		}
	}
	
	protected boolean isNbtLoaded() { return nbtLoaded;} 
	
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		nbtLoaded = true;
	}

	public void receiveData(ByteArrayDataInput data, Player playerEntity, boolean isClient) {
		if(isClient) {
			byte cmdId = data.readByte();
			if(cmdId == PacketTileEntity.CLIENT_UPDATE) {
				receiveClientUpdateData(data);
				initialized = true;
			}  else {
				if(!handleClientData(cmdId, data)) {
					ClientServerLogger.addLog("Something went wrong with handling ClientData!");
				}
			}
		} else {
			byte cmdId = data.readByte();
			if(cmdId == PacketTileEntity.CLIENT_UPDATE) {
				clientUpdate = true;
			} else {
				if(!handleServerData(cmdId, data, playerEntity)) {
					ClientServerLogger.addLog("Something went wrong with handling ServerData!");
				}
			}
		}
	}
	
	public void requestClientUpdate() {
		PacketTileEntity tilePacket = new PacketTileEntity(xCoord, yCoord, zCoord);
		try {
			tilePacket.dos.writeByte(PacketTileEntity.CLIENT_UPDATE);
			PacketDispatcher.sendPacketToServer(tilePacket.getPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean sendClientUpdatePacket() {
		PacketTileEntity packet = new PacketTileEntity(xCoord, yCoord, zCoord);
		try {
			packet.dos.writeByte(PacketTileEntity.CLIENT_UPDATE);
			PacketDispatcher.sendPacketToAllPlayers(createClientUpdateData(packet).getPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
		
	/**
	 * Creates a packet which will be send to the client before the first update()
	 * @return packet
	 */
	protected abstract PacketTileEntity createClientUpdateData(PacketTileEntity packet) throws IOException;
	
	/**
	 * receives a packet which initialize this entity before the first update()
	 */
	protected abstract void receiveClientUpdateData(ByteArrayDataInput data);
	
	/**
	 * This method handles all data that this TileEntity receives on the Client Side
	 * @return false if something went wrong with cmdId
	 */
	protected abstract boolean handleClientData(int cmdId, ByteArrayDataInput data); 
	
	/**
	 * This method handles all data that this TileEntity receives on the Server Side
	 * @return false if something went wrong with cmdId
	 */
	protected abstract boolean handleServerData(int cmdId, ByteArrayDataInput data, Player playerEntity);	
	
	/**
	 * This method is called every tick after the entity has been initialized
	 */
	protected abstract void update();
	
	/**
	 * this method is called before the first update() and metadata is valid
	 * after this function, the server will send an clientUpdatepacket to all clients
	 */
	protected abstract void metadataInit();
	
	
	/**
	 * does what the name says
	 */
	public abstract void onNeighborBlockChange();
}
