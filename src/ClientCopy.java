import java.io.*;
import java.net.*;

public class ClientCopy {

    Socket socket;
    BufferedReader br;          // for reading data
    PrintWriter out;            // for writing data
    public ClientCopy(){
    try {
        System.out.println("Sending request to Server..");
        Socket socket = new Socket("127.0.0.1", 7777); //[ ip and port ]
        System.out.println("Connection done.");

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        startReading();
        startWriting();
    }catch(Exception e)
    {
        e.printStackTrace();
        }
    }

    public void startReading()
    {
        // Thread will read and give the data
        Runnable r1 = ()->{
            System.out.println("Reader Started. ");
            try {while(true)
            {

                    String msg = br.readLine();     // it will come from client in case of server and viseversa
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat.");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            }catch(Exception e)
            {
                System.out.println("Connection closed!");
            }
        };
        // creating objects of thread
        new Thread(r1).start();
    }


    public void startWriting()
    {
        // Thread will take data from user and will send it to the client
        Runnable r2 = () -> {
            System.out.println("Writer Started.");
            try {while(true){
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  // taking input from console
                String content = br.readLine();
                out.println(content);
                out.flush();
                if(content.equals("exit"))
                {
                    socket.close();
                    break;
                }

            }}catch(Exception e)
            {
                System.out.println("Connection closed!");;
            }

        };
        new Thread(r2).start();

    }


    public static void main(String[] args) {
        System.out.println("this is client.");
        new ClientCopy();

    }
}
