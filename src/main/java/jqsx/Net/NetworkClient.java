package jqsx.Net;

import jqsx.Chat;
import jqsx.Net.Router.Route;
import jqsx.Net.Router.RouteManager;

import javax.net.SocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkClient implements Runnable {
    private Thread thread;
    public Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    public boolean isRunning = true;

    public final static List<NetworkClient> clients = new ArrayList<>();

    public NetworkClient(String hostName, int port) {
        Connect(hostName, port);

        RouteManager.onClientConnect(socket.getInetAddress());

        thread = new Thread(this);
        thread.start();

        clients.add(this);
    }

    public NetworkClient(InetAddress address, int port) {
        Connect(address, port);

        RouteManager.onClientConnect(socket.getInetAddress());

        thread = new Thread(this);
        thread.start();

        clients.add(this);
    }

    public void Disconnect() {
        if (isConnected()) {
            try {
                socket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                Chat.logErr(e);
            }
        }
    }

    private void Connect(String hostName, int port) {
        System.out.println("[CLIENT] Connecting to server.");

        try {
            this.socket = SocketFactory.getDefault().createSocket(hostName, port);
        } catch (IOException e) {
            Chat.logErr(e);
        }

        System.out.println("[CLIENT] Connected to server.");
    }

    private void Connect(InetAddress address, int port) {
        System.out.println("[CLIENT] Connecting to server.");

        System.out.println(address.getHostAddress());

        try {
            this.socket = SocketFactory.getDefault().createSocket(address, port);
        } catch (IOException e) {
            Chat.logErr(e);
        }

        System.out.println("[CLIENT] Connected to server.");
        Chat.logInfo("connected to " + socket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Chat.logErr(e);
        }

        try {
            while (isRunning && !socket.isClosed()) {
                short ID = in.readShort();
                int length = in.readInt();
                Route route = RouteManager.getRoute(ID);
                if (route == null) {
                    System.out.println("[CLIENT] ROUTE NOT FOUND " + ID);
                    socket.close();
                    return;
                }
                byte[] data = new byte[length];
                for (int i = 0; i < data.length; i++) {
                    data[i] = in.readByte();
                }
                route.Client_IN(data);
            }
            in.close();
            out.close();
            if (!socket.isClosed())
                socket.close();
            NetworkClient.clients.remove(this);
        } catch (IOException e) {
            NetworkClient.clients.remove(this);
            Chat.logErr(e);
        }
        finally {
            RouteManager.onClientDisconnect();
        }
    }


    public void send(short id, byte[] data) {
        try {
            out.writeShort(id);
            out.writeInt(data.length);
            out.write(data);
        } catch (IOException e) {
            System.out.println("[CLIENT] Problem");
            Chat.logErr(e);
        }
    }

    public boolean isConnected() {
        return socket != null && isRunning && !socket.isClosed();
    }
}
