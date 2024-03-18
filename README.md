# ChatApp / ShitChat
A simple chat app establishing connections on LAN networks with other clients, using p2p networking. It's kinda bad and I'm not proud of sticking all functions into a single large Chat class rather than separating all the components into their own respective classes. 

The Chat utilizes MulticastSockets to broadcast the locally asigned port on the client, at the same time launching a serversocket. The chat has no encryption and remains rather bare bones as a proof of concept to create network discovery and automatic connections.

The chat also uses my routing networking solution for organizing and sending messages, that I ripped right out of my game engine and slightly modified to allow for connecting to multiple servers at a time.

Using
```
javax.swing
java.net
Java SE 8
```

Download available at [here](out/artifacts/ChatApp_jar/ChatApp.jar)
