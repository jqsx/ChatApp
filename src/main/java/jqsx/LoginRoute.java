package jqsx;

import jqsx.Net.NetworkConnectionToClient;
import jqsx.Net.Router.Route;

import java.nio.ByteBuffer;

public class LoginRoute extends Route {
    @Override
    public void ServerClient_IN(NetworkConnectionToClient conn, byte[] data) {
        if (data.length == 4) {
            ByteBuffer buffer = ByteBuffer.wrap(data);

        }
    }
}
