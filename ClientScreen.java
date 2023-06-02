import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import HashMap.MyHashMap;

public class ClientScreen extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private static final Font SERIF_FONT = new Font("serif", Font.PLAIN, 100);

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JButton usernameButton;

    private boolean isHost;

    private MyArrayList<PlayerImages> PlayerList = new MyArrayList<PlayerImages>();
    private MyHashMap<Integer, EnergyBallDraw> EnergyBallList = new MyHashMap<Integer, EnergyBallDraw>();

    private Thread update;

    private String username;

    private Pair<String, Object> input;

    private Map map;

    private int up, down, left, right, dash, leftmouseState, rightmouseState, mouseX, mouseY, winningPlayer;

    private Pair<String, int[]> outPut;
    private MyHashMap<String, int[]> gameData;

    private boolean gameStarted = false;
    private boolean gameWon = false;


    private boolean isUnix = false;

    private Thread mainGameLoop;

    public ClientScreen() {
        this.setLayout(null);

        // this is to speed up rendering on linux systems see method paintComponent
        if (System.getProperty("os.name").equals("Linux")) {
            isUnix = true;
        }

        UsernameInputsetUp();

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

            } else {
                System.out.println("did not send because of game not started");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void UsernameInputsetUp() {

        usernameButton = new JButton("Ready");
        usernameButton.setBounds(10, 10, 200, 50);
        usernameButton.addActionListener(this);
        usernameButton.setFocusable(false);
        this.add(usernameButton);

       

        map = new Map();

    }

    public void physicsUpdateTick() {

    }

    // the isUnix it to speed up rendering on linux systems
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isUnix) {
            Toolkit.getDefaultToolkit().sync();
        }
        
        if (gameStarted) {
            map.drawMe(g);
            drawObjects(g);

        } else {
            map.drawBackground(g);
        }
        if(gameWon){
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(SERIF_FONT);
            g.setColor(Color.BLACK);
            g.drawString("Player " + winningPlayer + " Won!", 300, getHeight()/2);
        }
    }

    public void setUsername(String username) {
        this.username = username;
        usernameButton.setEnabled(false);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1200, 720);
    }

    @SuppressWarnings("unchecked")
    public void poll() throws IOException {

        //String hostName = "10.210.102.233";
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
                if (input.getKey().equals("gameData")) {
                    gameData = (MyHashMap<String, int[]>) input.getValue();
                } else if (input.getKey().equals("StartGame")) {
                    gameStarted = true;
                    usernameButton.setVisible(false);

                } else if (input.getKey().equals("newPlayer")) {
                    PlayerList.add(new PlayerImages((int) input.getValue()));

                } else if (input.getKey().equals("newBall")) {
                    String getid = (String) input.getValue();
                    getid = getid.substring(4);
                    int index = Integer.parseInt(getid);
                    EnergyBallList.put(Math.abs(index), new EnergyBallDraw((String) (input.getValue())));
                } else if (input.getKey().equals("deleteBall")) {
                    // EnergyBallList.get((int) input.getValue()).setExploded(true);
                    EnergyBallList.remove(Math.abs((int) input.getValue()));

                } else if (input.getKey().equals("PlayerWon")) {
                    gameWon = true;
                    winningPlayer = (int)input.getValue();
                }else if (input.getKey().equals("GameReset")) {
                    gameWon = false;
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
            if (usernameButton.getText().equals("Ready")) {
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
                    usernameButton.setText("Reset");
                    usernameButton.setVisible(false);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }else if (usernameButton.getText().equals("Reset")) {
                try {
                    out.reset();
                    //out.writeObject(new Pair<String, Object>("Reset", username));
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
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
        //System.out.println(key);
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
        if (key == 16)// shift
        {
            dash = 1;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == 38 || key == 87) {// up
            up = 0;
        }
        if (key == 37 || key == 65) {// left
            left = 0;

        }
        if (key == 40 || key == 83) {// down
            down = 0;

        }
        if (key == 39 || key == 68) {// right
            right = 0;
        }
        if (key == 16)// shift
        {
            dash = 0;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void drawObjects(Graphics g) {// draws all players or objects given the received gameData
        if (gameData == null) {
            return;
        }
        for (String each : gameData.keySet()) {
            int x = gameData.get(each)[0];
            int y = gameData.get(each)[1];
            if (each.contains("Thread")) {
                if (each.contains("Thread-0")) {
                    PlayerList.get(0).updateImg(gameData.get(each)[2]);
                    PlayerList.get(0).draw(g, x, y);
                } else if (each.contains("Thread-1")) {
                    PlayerList.get(1).updateImg(gameData.get(each)[2]);
                    PlayerList.get(1).draw(g, x, y);
                } else if (each.contains("Thread-2")) {
                    PlayerList.get(2).updateImg(gameData.get(each)[2]);
                    PlayerList.get(2).draw(g, x, y);
                }
            } else if (each.contains("Ball")) {
                try {
                    String subString = each.substring(4);
                    int index = Integer.parseInt(subString);
                    EnergyBallList.get(Math.abs(index)).draw(g, x, y,
                            new Vector(gameData.get(each)[3] / 1000.0, gameData.get(each)[4] / 1000.0));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (each.contains("M")) {
                g.drawRect(x, y, Melee.PLAYER_WIDTH, Melee.PLAYER_HEIGHT);
            }
        }
    }
}