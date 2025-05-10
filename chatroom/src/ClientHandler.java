import java.io.*;
import java.util.ArrayList;
import java.net.Socket;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers =new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try{
            this.socket=socket;
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.clientUsername = bufferedReader.readLine();
           clientHandlers.add(this);
           broadcastMessage("\u001B[34mServer\u001B[0m "+ clientUsername+" has entered the chat !");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void run(){
        String messageFromClient;
        while (socket.isConnected()){
            try{
                messageFromClient=bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadcastMessage(String messageToSend) {
        if (messageToSend.contains("/msg")) {
            // Extract and process private messages
            int commandIndex = messageToSend.indexOf("/msg");
            String commandString = messageToSend.substring(commandIndex); // Extract `/msg b hi`
            String[] arrStr = commandString.split(" ", 3);

            if (arrStr.length < 3) {
                try {
                    bufferedWriter.write("Invalid private message format. Use: /msg <username> <message>");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
                return;
            }

            String targetPerson = arrStr[1]; // Extract username
            String privateMessage = arrStr[2]; // Extract private message

            boolean userFound = false;
            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    if (clientHandler.clientUsername.equals(targetPerson)) {
                        clientHandler.bufferedWriter.write("\u001B[31m[Private]\u001B[0m " + clientUsername + ": " + privateMessage);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                        userFound = true;
                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }

            if (!userFound) {
                try {
                    bufferedWriter.write("User " + targetPerson + " not found.");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        } else {
            // Handle public messages
            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    if (!clientHandler.clientUsername.equals(this.clientUsername)) { // Skip the sender
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    closeEverything(clientHandler.socket, clientHandler.bufferedReader, clientHandler.bufferedWriter);
                }
            }
        }
    }


    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server "+ clientUsername+" has left the chat !");
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
