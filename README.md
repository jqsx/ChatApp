# ChatApp / ShitChat
A simple chat app establishing connections on LAN networks with other clients, using p2p networking. It's kinda bad and I'm not proud of sticking all functions into a single large Chat class rather than separating all the components into their own respective classes. 

The Chat utilizes MulticastSockets to broadcast the locally asigned port on the client, at the same time launching a serversocket. The chat has no encryption and remains rather bare bones as a proof of concept to create network discovery and automatic connections.
Using Java Swing, and Java Net, made for Java SE 8.
