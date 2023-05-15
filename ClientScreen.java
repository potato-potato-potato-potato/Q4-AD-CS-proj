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
import java.awt.event.MouseMotionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import HashMap.MyHashMap;

public class ClientScreen extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JTextField usernameField;
    private JButton usernameButton, quit;

    private boolean isHost;

    private MyArrayList<Player> PlayerList = new MyArrayList<Player>();

    private Thread update;

    private String username;

    private Pair<String, Object> input;

    private PhysicsObjects startScreen;
    private BufferedImage startImg;

    private boolean startScreenAnimation = false;

    private Map map;

    private int up, down, left, right, dash, mouseState, mouseX, mouseY;

    private Pair<String, int[]> outPut;
    private MyHashMap<String, int[]> gameData;

    private boolean gameStarted = false;

    public ClientScreen() {
        this.setLayout(null);

        UsernameInputsetUp();
        try {
            startImg = ImageIO.read(new File("assets/dcfldvz-dd1793cb-dde2-447f-803d-5341f2d8cbf3.jpg"));
            startScreen = new PhysicsObjects<BufferedImage>(startImg);
            startScreen.setPosition(new Point(0, 0));
            gameData = new MyHashMap<String, int[]>();

        } catch (Exception e) {
            e.printStackTrace();
        }
        drawThread();
        outputSetup();

        this.addKeyListener(this);
        this.addMouseListener(this);

        this.setFocusable(true);

    }

    public void outputSetup() {
        up = 0;
        down = 0;
        left = 0;
        right = 0;
        dash = 0;
        mouseState = 0;
        mouseX = 0;
        mouseY = 0;

        outPut = new Pair<String, int[]>("clientoutput",
                new int[] { up, down, left, right, dash, mouseState, mouseX, mouseY });
        // [Name], [up, down, left, right, dash, mouseState,mouseX, mouseY]

    }

    public void sendOutput() {
        try {
            if (gameStarted == true) {
                out.writeObject(outPut);
                System.out.println("output send");
            } else {
                System.out.println("did not send because of game not started");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void drawThread() {
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

    public void UsernameInputsetUp() {
        usernameField = new JTextField();
        usernameField.setBounds(10, 10, 200, 50);
        usernameField.addActionListener(this);
        usernameField.setText("Username");
        this.add(usernameField);

        usernameButton = new JButton("Enter");
        usernameButton.setBounds(10, 70, 200, 50);
        usernameButton.addActionListener(this);
        usernameButton.setFocusable(false);
        this.add(usernameButton);

        quit = new JButton("Quit");
        quit.setBounds(1100, 10, 200, 50);
        quit.addActionListener(this);
        quit.setFocusable(false);
        this.add(quit);

        map = new Map();

    }

    public void physicsUpdateTick() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(gameStarted){
            drawObjects(g);
        }
        else{
            startScreen.draw(g);
            map.drawMe(g);
        }
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
            input = (Pair<String, Object>) in.readObject();
            if (input.getKey().equals("isHost")) {
                isHost = (boolean) input.getValue();
            }
            while (true) {
                input = (Pair<String, Object>) in.readObject();
                System.out.println("Input: " + input.getKey());
                if (input.getKey().equals("username")) {
                    PlayerList.add(new Player((String) input.getValue()));
                }
                if (input.getKey().equals("StartGame")) {
                    gameStarted = true;
                    usernameButton.setVisible(true);
                    System.out.println("game Started");

                } else if(input.getKey().equals("gameData")){
                    gameData = (MyHashMap<String, int[]>)input.getValue();
                    System.out.println("client y pos:" + gameData.get("Thread-0")[1]);
                }
                repaint();
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
            if (usernameButton.getText().equals("Enter")) {
                username = usernameField.getText();
                usernameField.setVisible(false);
                usernameButton.setText("Start Game");
                try {
                    out.writeObject(new Pair<String, Object>("threadname", username));
                    if (isHost) {
                        usernameButton.setText("Start Game");
                    } else {
                        usernameButton.setVisible(false);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (usernameButton.getText().equals("Start Game")) {
                try {
                    out.writeObject(new Pair<String, Object>("StartGame", username));
                    gameStarted = true;
                    usernameButton.setVisible(false);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        if (e.getSource() == quit) {
            try {
                out.writeObject(new Pair<String, Object>("Quit", username));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {

        if (!startScreenAnimation) {
            startScreenAnimation = true;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (startScreen.getPosition().getY() > -700) {
                            Vector v = new Vector(0, 0);
                            v = Vector.addVectors(startScreen.getVector(), new Vector(0, -2));
                            startScreen.setVector(v);
                            startScreen.update();
                            Thread.sleep(16);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        mouseX = e.getX();
        mouseY = e.getY();
        mouseState = 1;
        sendOutput();
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseState = 0;
        sendOutput();
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

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        sendOutput();
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                up = 1;
                break;
            case KeyEvent.VK_A:
                left = 1;
                break;
            case KeyEvent.VK_S:
                down = 1;
                break;
            case KeyEvent.VK_D:
                right = 1;
                break;
        }
        sendOutput();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                up = 0;
                break;
            case KeyEvent.VK_A:
                left = 0;
                break;
            case KeyEvent.VK_S:
                down = 0;
                break;
            case KeyEvent.VK_D:
                right = 0;
                break;
        }
        sendOutput();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void drawObjects(Graphics g){//draws all players or objects given the received gameData
        System.out.println("Gamedata: " + gameData.keySet());
        for(String each: gameData.keySet()){
            System.out.println(gameData.get(each));
            int x = gameData.get(each)[0];
            int y = gameData.get(each)[1];
            if(each.contains("Thread")){
                g.fillRect(x, y, 10, 50);
                g.drawString(each, x, y);
                System.out.println("Drawing " + each + " at " + x + ", " + y + "");
            }
        }
    }
}