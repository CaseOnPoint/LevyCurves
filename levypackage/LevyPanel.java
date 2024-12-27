package levypackage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

public class LevyPanel extends JPanel {
    private List<LevyCurves> curves = new ArrayList<>();
    private List<AnimatedPoint> animatedPoints = new ArrayList<>();
    private int currIndex = 0;

    private final double scalingFactor = 100.0; 

    // colors for diff curves
    private List<Color> colors = List.of(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA);

    public LevyPanel() {

        // initialize mutliple curves
        for (int i = 0; i < colors.size(); i++) {
            curves.add(new LevyCurves());
        }

        int pointsPerTick = 10;

        // Swing timer to increment currIndex and display points slowly
        Timer genTimer = new Timer(5, e -> {
            // increase index to show more points
            for (int i = 0; i < curves.size(); i++) {
                for (int j = 0; j < pointsPerTick; j++) {
                    LevyCurves curve = curves.get(i);
                    LevyCurves.Coords c = curve.calculateCoords();
                    Color color = colors.get(i);
    
                    // create AnimatedPoint
                    AnimatedPoint ap = new AnimatedPoint(c.x(), c.y(), color, 100);
                    animatedPoints.add(ap);
                }
            }
            repaint();
        });
        genTimer.start();

        Timer fadeTimer = new Timer(50, e -> {
            Iterator<AnimatedPoint> iterator = animatedPoints.iterator();
            while(iterator.hasNext()) {
                AnimatedPoint ap = iterator.next();
                ap.update();
                if (ap.isDead()) {
                    iterator.remove();
                }
            }
            repaint();
        });
        fadeTimer.start();
    }
    // This is the method for custom drawing.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Clears the background & housekeeping
        
        // Cast to Graphics2D for better control (optional but common)
        Graphics2D g2d = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Set rendering hints for bettter graphics quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Levy Curve calculations 
        for (AnimatedPoint ap : animatedPoints) {
            double dx = ap.x;
            double dy = ap.y;

            int drawX = (int) (dx * scalingFactor + centerX);
            int drawY = (int) (dy * scalingFactor + centerY);

            // assign color based on curve
            Color colorWithAlpha = new Color(
                ap.color.getRed(), 
                ap.color.getGreen(), 
                ap.color.getBlue(),
                Math.min(255, Math.max(0, (int) (ap.alpha * 255)))
            );
            g2d.setColor(colorWithAlpha);

            // small circle for better visibility 
            g2d.fillOval(drawX, drawY, 4, 4);
        }
    }
}
