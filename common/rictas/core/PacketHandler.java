package rictas.core;

import rictas.helper.ClientServerLogger;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler {
	
	public static final String channel = "RictasMod";
	public static final byte TILE_ENTITY = 0;
	public static final byte notUsed = 1; // i dont know if i need this, can be used to send packet which dont go to a tile entity

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {		
		if (packet.channel.equals(PacketHandler.channel)) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if (side == Side.SERVER) {
				handleServerPacket(manager, packet, playerEntity);
			} else if (side == Side.CLIENT) {
				handleClientPacket(manager, packet, playerEntity);
			}
		}		
	}
	
	private void handleServerPacket(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
		ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
		byte id = data.readByte();
		switch(id) {
			case TILE_ENTITY:
				World world = ((EntityPlayerMP)playerEntity).getServerForPlayer();
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if (tile instanceof IPacketReceiver) {
					((IPacketReceiver)tile).receiveData(data, playerEntity, false);
				} else {
					ClientServerLogger.addLog("No PacketReceiver found at: x="+x +" y="+ y +" z="+ z);
				}
				break;
			default:
				ClientServerLogger.addLog("Received Packet with id "+id);
				break;
		}
	}
	
	private void handleClientPacket(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity) {
		ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
		byte id = data.readByte();
		switch(id) {
		case TILE_ENTITY:
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			TileEntity tile = RictasMain.proxy.getClientWorld().getBlockTileEntity(x, y, z);
			if (tile instanceof IPacketReceiver) {
				((IPacketReceiver)tile).receiveData(data, playerEntity, true);
			} else {
				ClientServerLogger.addLog("No PacketReceiver found at: x="+x +" y="+ y +" z="+ z);
			}
			break;
		default:
			ClientServerLogger.addLog("Received Packet with id "+id);
			break;
		}
	}
}
