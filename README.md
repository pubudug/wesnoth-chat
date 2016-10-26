# wesnoth-chat
A Java library to connect to wesnoth lobby chat

To create a thread that connects to the server, instantiate WesnothChatClient

    WesnothChatClient client =  new WesnothChatClient(dependencies...)

The dependencies are self explanatory.

Then do

    new Thread(client).start();
    
to connect to server and start communicating.

Use

    client.whisper(...
    client.sendMessage(...
    
To send whispers/messages.
