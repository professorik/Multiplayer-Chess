package client;

import client.ui.GUI;

import javax.swing.*;
import java.awt.*;

public class Client {

    public static String ipAddr = "localhost";
    public static int port = 8080;
    public static ClientHandler client;
    public static GUI gui = new GUI();

    public static void main(String[] args) {
        client = new ClientHandler(ipAddr, port);

        Runnable r = () -> {
            JFrame f = new JFrame("Socket-Chess");
            f.add(gui.getGUI());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);
            f.pack();
            Dimension size = new Dimension(Math.max(f.getWidth(), 1024), Math.max(f.getHeight(), 710));
            f.setMinimumSize(size);
            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }
}
