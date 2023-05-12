import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.*;

import myArrayList.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.w3c.dom.events.MouseEvent;

import java.io.File;
import java.io.IOException;

import java.awt.event.MouseListener;

public class ClientScreen extends JPanel implements ActionListener, MouseListener {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JTextField usernameField;
    private JButton usernameButton;

    private boolean isHost;

    private MyArrayList<Player> PlayerList = new MyArrayList<Player>();

    private Thread update;

    private String username;

    private Pair<String, Object> input;

    private PhysicsObjects startScreen;
    private BufferedImage startImg;

    private boolean isGameStarted = false;

    public ClientScreen() {
        this.setLayout(null);

        UsernameInputsetUp();
        try {
            startImg = ImageIO.read(new File("assets/dcfldvz-dd1793cb-dde2-447f-803d-5341f2d8cbf3.jpg"));
            startScreen = new PhysicsObjects<BufferedImage>(startImg);
            startScreen.setPosition(new Point(300, 200));

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.addMouseListener(this);

        this.setFocusable(true);

    }

    public void UsernameInputsetUp() {
        usernameField = new JTextField();
        usernameField.setBounds(300, 300, 200, 50);
        usernameField.addActionListener(this);
        this.add(usernameField);

        usernameButton = new JButton("Enter");
        usernameButton.setBounds(300, 400, 200, 50);
        usernameButton.addActionListener(this);
        this.add(usernameButton);

    }

    public void physicsUpdateTick() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        startScreen.draw(g);

    }

    public void setUsername(String username) {
        this.username = username;
        usernameField.setText("");
        usernameButton.setEnabled(false);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1920, 1080);
    }

    @SuppressWarnings("unchecked")
    public void poll() throws IOException {

        // String hostName = "10.210.102.233";

        String hostName = "localhost";

        int portNumber = 1024;
        Socket serverSocket = new Socket(hostName, portNumber);

        // listens for inputs
        try {
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            in = new ObjectInputStream(serverSocket.getInputStream());

            while (true) {
                input = (Pair<String, Object>) in.readObject();
                if (input.getKey().equals("isHost")) {
                    isHost = (boolean) input.getValue();
                    System.out.println("isHost: " + isHost);
                }
                if (input.getKey().equals("username")) {
                    PlayerList.add(new Player((String) input.getValue()));

                }

            }
        } catch (UnknownHostException e) {
            System.err.println("Host unkown: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Type Cast Error");
            e.printStackTrace();
            System.exit(1);
        }
        serverSocket.close();
        out.close();
        in.close();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == usernameButton) {
            username = usernameField.getText();
            try {
                out.writeObject(new Pair<String, Object>("threadname", username));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {

        if (!isGameStarted) {
            isGameStarted = true;
            System.out.println("Game Started");
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            Vector v = new Vector(0, 0);
                            v = Vector.addVectors(startScreen.getVector(), new Vector(0, -0.1));
                            startScreen.setVector(v);
                            startScreen.update();

                            Thread.sleep(4);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });
            t.start();

            Thread t2 = new Thread(new Runnable() {
                public void run() {

                    try {
                        while (true) {
                            repaint();
                            Thread.sleep(1);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
            t2.start();

        }
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
    }

}
