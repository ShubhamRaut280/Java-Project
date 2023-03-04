import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;   // imported all networking classes to be used.
import java.io.*;   // for using bufferreader

public class Server extends JFrame {

    // defining variables to send and receive data
    ServerSocket server;        // for
    Socket socket;              // received data will be stored here
    BufferedReader br;          // for reading data
    PrintWriter out;            // for writing data

    // creating constructor
    private JLabel heading = new JLabel("Server Area ");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInp  = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);
    public Server()
    {
        // Almost all work will be here
        createGUI();

        try {
            server = new ServerSocket(7777);  // if object is successfully created
            System.out.println("Server is ready to accept connections..");
            System.out.println("Waiting...");
            socket = server.accept();  // accepting object of socket(from client) and storing in variable.

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());


            handleEvents();
            startReading();
//            startWriting();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void handleEvents() {
        messageInp.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("Key Released : " + e.getKeyCode()); // --> to get the code of key which is been used/released.
                if(e.getKeyCode() == 10)
                {
                    String msgToSnd = messageInp.getText();
                    messageArea.append("Me : " + msgToSnd+"\n");
                    out.println(msgToSnd);
                    out.flush();
                    messageInp.setText("");   //clear msg
                    messageInp.requestFocus();
                }
            }
        });
    }

    private void createGUI() {
        // code for creating GUI
        this.setTitle(" Server's Messenger ");
        this.setSize(500,650);
        this.setLocationRelativeTo(null);
//                this.setLocation(700,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // fonts
        heading.setFont(font);
        messageArea.setFont(font);
        messageInp.setFont(font);

        heading.setIcon(new ImageIcon("logor.png"));
        // modifications in heading
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);  // -->  alignment set to centre
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20 ,20));

        messageArea.setEditable(false);  //
        messageArea.setSelectionColor(Color.LIGHT_GRAY);
        messageInp.setHorizontalAlignment(SwingConstants.CENTER);

        // Setting frame layout
        this.setLayout(new BorderLayout());
        // Adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane =  new JScrollPane(messageArea);     // for getting scroll bar
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInp, BorderLayout.SOUTH);

        this.setVisible(true);
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
                        JOptionPane.showMessageDialog(this,"Server terminated the chat." );
                        messageInp.setEnabled(false);  // set disabled.
                        socket.close();
                        break;
                    }
//                    System.out.println("Client: " + msg + "\n");

                messageArea.append("Client : " + msg + "\n");     // for GUI
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
