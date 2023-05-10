import javax.swing.JPanel;
import java.awt.Graphics;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    public Screen() {
        this.setLayout(null);

        this.setFocusable(true);

    }

    public Dimension getPreferredSize() {
        // Sets the size of the panel
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

    }

    public void actionPerformed(ActionEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

}