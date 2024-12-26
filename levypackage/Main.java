package levypackage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    // Main method to show the JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Levy Curve Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create an instance of our custom panel
            LevyPanel panel = new LevyPanel();
            frame.add(panel);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
