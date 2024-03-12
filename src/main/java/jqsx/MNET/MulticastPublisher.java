package jqsx.MNET;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class MulticastPublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void multicast(int port) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("224.0.2.0");
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(port);
        buf = buffer.array();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }
}
