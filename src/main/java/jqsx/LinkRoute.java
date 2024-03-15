package jqsx;

import jqsx.Net.NetworkConnectionToClient;
import jqsx.Net.NetworkServer;
import jqsx.Net.Router.Route;

import java.nio.ByteBuffer;

public class LinkRoute extends Route {
    @Override
    public void ServerClient_IN(NetworkConnectionToClient conn, byte[] data) {
        msg(data);
    }

    @Override
    public void Client_IN(byte[] data) {
        msg(data);
    }

    private void msg(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int name_l = Math.abs(buffer.getInt());
        if (name_l > 16)
            return;
        byte[] name_b = new byte[name_l];
        buffer.get(name_b);
        String name = new String(name_b);

        int link_l = Math.abs(buffer.getInt());

        byte[] link_b = new byte[link_l];
        buffer.get(link_b);
        String link = new String(link_b);

        Chat.addLink(name + " > " + link, link);
    }

    public void send(String name, String link) {
        Chat.addLink(name + " > " + link, link);

        byte[] bName = name.getBytes();
        byte[] bLink = link.getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(bName.length + bLink.length + 8);
        buffer.putInt(bName.length);
        buffer.put(bName);
        buffer.putInt(bLink.length);
        buffer.put(bLink);

        byte[] data = buffer.array();

        sendToServer(data);
        for (NetworkConnectionToClient conn : NetworkServer.clients) {
            sendToClient(conn, data);
        }
    }
}
