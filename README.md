# wesnoth-chat
A Java library to connect to wesnoth lobby chat

To create a thread that connects to the server, instantiate WesnothChatClient

    WesnothChatClient client =  new WesnothChatClient(host, port, dependencies...)

The dependencies are self explanatory.

Then do

    new Thread(client).start();
    
to connect to server and start communicating.

Use

    client.whisper(...
    client.sendMessage(...
    
To send whispers/messages.

See [wesnoth-chat-swing](https://github.com/pubudug/wesnoth-chat-swing) which is a Java Swing client that uses this library.
