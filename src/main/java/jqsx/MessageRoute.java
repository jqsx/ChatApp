package jqsx;

import jqsx.Net.NetworkClient;
import jqsx.Net.NetworkConnectionToClient;
import jqsx.Net.NetworkServer;
import jqsx.Net.Router.Route;

import java.nio.ByteBuffer;

public class MessageRoute extends Route {

    private Chat chat;

    public MessageRoute(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void ServerClient_IN(NetworkConnectionToClient conn, byte[] data) {
        String text = new String(data);
        chat.createNewText(text);
    }

    @Override
    public void Client_IN(byte[] data) {
        String text = new String(data);
        chat.createNewText(text);
    }

    public void sendMessage(String text) {
        byte[] data = text.getBytes();
        sendToServer(data);
        for (NetworkConnectionToClient client : NetworkServer.clients) {
            sendToClient(client, data);
        }
    }
}
