package jqsx.MNET;

import jqsx.Chat;
import jqsx.Net.NetworkClient;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;

    public boolean isRunning = true;
    protected byte[] buf = new byte[4];

    public void run() {
        try {
            socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("224.0.2.0");
            System.out.println("Multicast socket created");
            socket.joinGroup(group);
            while (isRunning) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                ByteBuffer buffer = ByteBuffer.wrap(packet.getData());

                System.out.println(Arrays.toString(packet.getData()));
                if (buffer.capacity() == 4) {
                    int port = buffer.getInt();
                    if (port >= 6000 && port <= 15000) {
                        new NetworkClient(packet.getAddress(), port);
                        System.out.println("Got port");
                    }
                    else {
                        System.out.println("Trolling");
                    }
                }
                System.out.println(packet.getAddress().getHostAddress());
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            Chat.logErr(e);
        }
    }
}
