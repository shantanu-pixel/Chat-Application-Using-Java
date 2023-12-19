import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class Server extends JFrame{
    
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Server Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);
    
    // Constructor..
    public Server(){

        try {
            server = new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting...");
            socket = server.accept();


            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            //startWriting();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void handleEvents(){

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key released "+e.getKeyCode());
                 if(e.getKeyCode()==10){
                    //System.out.println("you pressed enter");
                    String contentToSend = messageInput.getText();
                    messagArea.append("Server :"+ contentToSend +"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
    }

    private void createGUI(){
        // gui

        this.setTitle("Server Messager[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("Server1.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // set the frame layout
        this.setLayout(new BorderLayout());

        //adding the component to frame
        this.add(heading, BorderLayout.NORTH);
        this.add(messagArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);


        this.setVisible(true);
    }
    
    
    public void startReading() {
        // thread-read

        Runnable r1=()->{

            System.out.println("reader started..");

            try {
                
            while (true) {
                
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client ternminated the chat");
                        JOptionPane.showMessageDialog(this, "Client terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                //System.out.println("Client : "+msg);
                messagArea.append("Client : "+msg+"\n");

            }
            } catch (Exception e) {
                // TODO: handle exception
                //e.printStackTrace();
                System.out.println("Connection Closed..");
            }
            
            
        };

        new Thread(r1).start();
    }


    public void startWriting() {
        // thread send to client

        Runnable r2=()->{
            System.out.println("Writer started..");
            
            try {
                while (!socket.isClosed()){
                BufferedReader br1 = new BufferedReader(new 
                InputStreamReader(System.in));

                String content=br1.readLine();


                out.println(content);
                out.flush();

                if(content.equals("exit")){

                    socket.close();
                    break;
                }
                }
            } catch (Exception e) {
                // TODO: handle exception
                //e.printStackTrace();
                System.out.println("Connection Closed..");
            }
            
            System.out.println("Connection Closed..");
            
        };

        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("This is server..going to start server");
        new Server();

    }
}