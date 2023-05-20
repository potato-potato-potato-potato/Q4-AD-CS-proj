import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
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

import java.awt.event.MouseEvent;

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

    private Map map;

    private int up, down, left, right, dash, leftmouseState, rightmouseState, mouseX, mouseY;

    private Pair<String, int[]> outPut;
    private MyHashMap<String, int[]> gameData;

    private boolean gameStarted = false;

    private boolean isUnix = false;

    private Thread mainGameLoop;
    private PlayerImages[] playerImages;

    public ClientScreen() {
        this.setLayout(null);

        if (System.getProperty("os.name").equals("Linux")) {
            isUnix = true;
        }

        UsernameInputsetUp();

        drawThread();
        outputSetup();
        mainGameLoop();

        this.addKeyListener(this);
        this.addMouseListener(this);

        this.setFocusable(true);

    }

    // main game loop thread
    // it sends a out put every 18 milliseconds to get 60 fps
    public void mainGameLoop() {
        mainGameLoop = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(18);
                        if (gameStarted) {
                            sendOutput();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mainGameLoop.start();
    }

    public void outputSetup() {
        up = 0;
        down = 0;
        left = 0;
        right = 0;
        dash = 0;
        leftmouseState = 0;
        rightmouseState = 0;
        mouseX = 0;
        mouseY = 0;

        outPut = new Pair<String, int[]>("clientoutput",
                new int[] { up, down, left, right, dash, leftmouseState, rightmouseState, mouseX, mouseY });
        // [Name], [up, down, left, right, dash, leftmouseState,rightmouseState,mouseX,
        // mouseY]

    }

    public void sendOutput() {
        try {
            if (gameStarted == true) {
                outPut.setValue(
                        new int[] { up, down, left, right, dash, leftmouseState, rightmouseState, mouseX, mouseY });
                out.reset();
                out.writeObject(outPut);
                System.out.println("output send: ");
                System.out.println("up: " + outPut.getValue()[0]);
                System.out.println("down: " + outPut.getValue()[1]);
                System.out.println("left: " + outPut.getValue()[2]);
                System.out.println("right: " + outPut.getValue()[3]);
                System.out.println("dash: " + outPut.getValue()[4]);
                System.out.println("mouseState: " + outPut.getValue()[5]);
                System.out.println("mouseX: " + outPut.getValue()[6]);
                System.out.println("mouseY: " + outPut.getValue()[7]);

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
                    // while (true) {
                    // repaint();
                    // Thread.sleep(1);
                    // }
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

        if (isUnix) {
            Toolkit.getDefaultToolkit().sync();
        }

        if (gameStarted) {
            drawObjects(g);

            map.drawMe(g);

        } else {
            map.drawBackground(g);
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
                if (input.getKey().equals("username")) {
                    // ????
                }
                if (input.getKey().equals("StartGame")) {
                    gameStarted = true;
                    usernameButton.setVisible(true);
                    System.out.println("game Started");

                } else if (input.getKey().equals("gameData")) {
                    gameData = (MyHashMap<String, int[]>) input.getValue();
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
                    out.reset();
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
                    out.reset();
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
                out.reset();
                out.writeObject(new Pair<String, Object>("Quit", username));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftmouseState = 1;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightmouseState = 1;
        }

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftmouseState = 0;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightmouseState = 0;
        }

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

    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println(key);
        if (key == 38 || key == 87) {// up
            up = 1;
        }
        if (key == 37 || key == 65) {// left
            left = 1;
        }
        if (key == 40 || key == 83) {// down
            down = 1;
        }
        if (key == 39 || key == 68) {// right
            right = 1;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == 38 || key == 87) {// up
            up = 0;
            System.out.println("Up");
        }
        if (key == 37 || key == 65) {// left
            left = 0;
            System.out.println("left");

        }
        if (key == 40 || key == 83) {// down
            down = 0;
            System.out.println("down");

        }
        if (key == 39 || key == 68) {// right
            right = 0;
            System.out.println("right");

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void drawObjects(Graphics g) {// draws all players or objects given the received gameData

        for (String each : gameData.keySet()) {
            if (each.contains("Thread")) {
                if (each.equals("Thread-0")) {
                    playerImages[0] = new PlayerImages("P1", gameData.get(each));
                } else if (each.equals("Thread-1")) {
                    playerImages[1] = new PlayerImages("P2", gameData.get(each));

                } else if (each.equals("Thread-2")) {
                    playerImages[2] = new PlayerImages("P3", gameData.get(each));

                } else {
                    playerImages[3] = new PlayerImages("ExtraPlayerError", gameData.get(each));
                }
            }
        }
        for (int i = 0; i < playerImages.length; i++) {
            playerImages[i].drawMe(g);
        }
    }
}