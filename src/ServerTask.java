import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTask implements Runnable{

    public static ArrayList<ServerTask> serverTasks = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ServerTask(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            serverTasks.add(this);
            send_meesage(clientUsername + " has entered the chat!");
        } catch (IOException e) {
            close_all(socket, bufferedReader, bufferedWriter);
            e.printStackTrace();
        }
    }

    public void send_meesage(String message){
        for (ServerTask st:serverTasks){
            try {
                // Tutaj mozliwa zmiana przy gui
                if(!st.clientUsername.equals(clientUsername)){
                    st.bufferedWriter.write(message);
                    st.bufferedWriter.newLine();
                    st.bufferedWriter.flush();
                }
            }catch (IOException e){
                close_all(socket, bufferedReader, bufferedWriter);
                e.printStackTrace();
            }
        }
    }

    public void remove_serverTask(){
        serverTasks.remove(this);
        send_meesage(clientUsername + " left the chat!");
    }

    public void close_all(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        remove_serverTask();
        try {
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

    @Override
    public void run() {
        String message;

        while (socket.isConnected()){
            try {
                message = bufferedReader.readLine();
                send_meesage(message);
            }catch (IOException e){
                close_all(socket, bufferedReader, bufferedWriter);
                System.out.println("Disconected: " + socket);
                break;
            }
        }
    }
}
