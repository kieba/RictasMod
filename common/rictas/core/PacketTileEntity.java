package rictas.core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketTileEntity {
	
	public static byte CLIENT_UPDATE = 0;
	public static byte SIDE_CONTROL = 1;
	public static byte WALL_CONTROL = 2;

	private ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
	public DataOutputStream dos = new DataOutputStream(bos);
	
	public PacketTileEntity(int x, int y, int z) {
		try {
			dos.writeByte(0); // marks this packet as a tileEntity packet
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Packet getPacket() {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = PacketHandler.channel;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		return packet;
	}
}
