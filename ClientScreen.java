import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;

import myArrayList.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;

public class ClientScreen extends JPanel implements ActionListener {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JTextField usernameField;
    private JButton usernameButton;

    private boolean isHost;

    private MyArrayList<Player> PlayerList = new MyArrayList<Player>();

    private Thread update;

    private String username;

    private Pair<String, Object> input;

    public ClientScreen() {
        this.setLayout(null);

        UsernameInputsetUp();

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

    }

    public void setUsername(String username) {
        this.username = username;
        usernameField.setText("");
        usernameButton.setEnabled(false);
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
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
}
