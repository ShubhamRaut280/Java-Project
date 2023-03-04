import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import static javax.swing.SwingConstants.NORTH;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;          // for reading data
    PrintWriter out;            // for writing data

    // Declaring components
    private JLabel heading = new JLabel("Client Area ");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInp  = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client(){
    try {
        System.out.println("Sending request to Server..");
        Socket socket = new Socket("127.0.0.1", 7777); //[ ip and port ]
        System.out.println("Connection done.");

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        
        createGUI();
        handleEvents();
        startReading();
//        startWriting();   --> used for console
    }catch(Exception e)
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
        this.setTitle(" Client's Messenger ");
        this.setSize(500,650);
        this.setLocationRelativeTo(null);
       //        this.setLocation(400,200);
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
                        System.out.println("Server terminated the chat.");
//                        JOptionPane.showMessageDialog(this,"Server terminated the chat." );
                        messageInp.setEnabled(false);  // set disabled.
                        socket.close();
                        break;
                    }
//                    System.out.println("Server: " + msg);  --> for console
                messageArea.append("Server : " + msg + "\n");     // for GUI
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
        new Client();

    }
}
