import javax.swing.JFrame;

import java.awt.Toolkit;
import java.io.*;

public class Client {

    public static void main(String args[]) throws IOException {

        JFrame frame = new JFrame("Client");

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            Toolkit.getDefaultToolkit().sync();
        }

        ClientScreen sc = new ClientScreen();
        frame.add(sc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        sc.poll();
    }
}