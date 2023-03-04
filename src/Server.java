import java.net.*;   // imported all networking classes to be used.
import java.io.*;   // for using bufferreader

public class Server {

    // defining variables to send and receive data
    ServerSocket server;        // for
    Socket socket;              // received data will be stored here
    BufferedReader br;          // for reading data
    PrintWriter out;            // for writing data

    // creating constructor
    public Server()
    {
        // Almost all work will be here

        try {
            server = new ServerSocket(7777);  // if object is successfully created
            System.out.println("Server is ready to accept connections..");
            System.out.println("Waiting...");
            socket = server.accept();  // accepting object of socket(from client) and storing in variable.

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }catch (Exception e)
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
                        System.out.println("Client terminated the chat.");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);

            } }catch(Exception e)
            {
                System.out.println("Connection is closed!");            }
        };
        // creating objects of thread
        new Thread(r1).start();
    }


    public void startWriting()
    {
        // Thread will take data from user and will send it to the client
        Runnable r2 = () -> {
            System.out.println("Writer Started.");
                try {while( !socket.isClosed()) {

                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  // taking input from console
                    String content = br.readLine();
                    out.println(content);
                    out.flush();

                if(content.equals("exit"))
                {
                    socket.close();
                    break;
                }

                }
            }catch (Exception e) {
                System.out.println("Connection is closed!");            }

        };
        new Thread(r2).start();

    }
    public static void main(String[] args)
    {
        // calling constructor
        new Server();
    }
}
