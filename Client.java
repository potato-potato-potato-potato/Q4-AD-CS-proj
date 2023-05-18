import javax.swing.JFrame;

import java.awt.Toolkit;
import java.io.*;

public class Client {

    public static void main(String args[]) throws IOException {

        JFrame frame = new JFrame("Client");
        System.setProperty("sun.java2d.opengl", "true");

        ClientScreen sc = new ClientScreen();
        frame.add(sc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        sc.poll();
    }
}