import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public Server(ServerSocket serverSocket){
        this.serverSocket=serverSocket;
    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket= serverSocket.accept();
                System.out.println(socket+" kh");
                System.out.println("\u001B[32m[A new Client has Connected !]\u001B[0m ");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket= new ServerSocket(1234);
        System.out.println(serverSocket);
        Server server = new Server(serverSocket);
        System.out.println(server);
        server.startServer();
    }

}
