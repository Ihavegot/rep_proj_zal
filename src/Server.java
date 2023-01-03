import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start_server(){
        try {
            System.out.println("Server is running...");
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("Connected: " + socket);

                ServerTask serverTask = new ServerTask(socket);

                Thread thread = new Thread(serverTask);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void close_server(){
//        try {
//            if(serverSocket != null){
//                serverSocket.close();
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(8080));
        server.start_server();
    }

}
