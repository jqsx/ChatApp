package jqsx.MNET;

import jqsx.Chat;
import jqsx.Net.NetworkClient;
import jqsx.Net.NetworkServer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;

    public boolean isRunning = true;
    protected byte[] buf = new byte[4];

    public void run() {
        try {
            socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("224.0.2.0");
            System.out.println("Multicast socket created");
            setNetworkInterface(socket);
            socket.joinGroup(group);
            while (isRunning) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                ByteBuffer buffer = ByteBuffer.wrap(packet.getData());

                System.out.println(Arrays.toString(packet.getData()));
                if (buffer.capacity() == 4) {
                    int port = buffer.getInt();
                    if (port == NetworkServer.PORT) {
                        continue;
                    }
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

    private void setNetworkInterface(MulticastSocket socket) {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> addressesFromNetworkInterface = networkInterface.getInetAddresses();
            while (addressesFromNetworkInterface.hasMoreElements()) {
                InetAddress inetAddress = addressesFromNetworkInterface.nextElement();
                if (inetAddress.isSiteLocalAddress()
                        && !inetAddress.isAnyLocalAddress()
                        && !inetAddress.isLinkLocalAddress()
                        && !inetAddress.isLoopbackAddress()
                        && !inetAddress.isMulticastAddress()) {
                    try {
                        socket.setNetworkInterface(NetworkInterface.getByName(networkInterface.getName()));
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }
}
