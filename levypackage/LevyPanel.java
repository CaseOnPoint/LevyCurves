package levypackage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class LevyPanel extends JPanel {
    private List<LevyCurves> curves = new ArrayList<>();
    private List<AnimatedPoint> animatedPoints = new ArrayList<>();

    private final double scalingFactor = 50.0; 

    // colors for diff curves
    private final List<Color> colors = List.of(
        Color.RED, Color.GREEN, Color.BLUE, 
        Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK
    );

    private final Random rand = new Random();

    private final Timer genTimer;
    private final Timer fadeTimer;
    private Timer curveGeneratorTimer;

    public LevyPanel() {
        setBackground(Color.black);

        int pointsPerTick = 10;

        // Swing timer to increment currIndex and display points slowly
        genTimer = new Timer(5, e -> {
            // increase index to show more points
            for (LevyCurves curve : curves) {
                for (int j = 0; j < pointsPerTick; j++) {
                    LevyCurves.Coords c = curve.calculateCoords();
                    Color color = curve.getColor();
    
                    // create AnimatedPoint
                    AnimatedPoint ap = new AnimatedPoint(c.x(), c.y(), color, 100);
                    animatedPoints.add(ap);
                }
            }
            repaint();
        });
        genTimer.start();

        fadeTimer = new Timer(1, e -> {
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

        // generate curves at random intervals 
        curveGeneratorTimer = new Timer(0, null);
        curveGeneratorTimer.setRepeats(false);

        curveGeneratorTimer.addActionListener(e -> {
                addNewCurve();

                // set the next timer delay to rand int between 1-3 seconds, restart timer 
                int nextDelay = 10 + rand.nextInt(100);
                curveGeneratorTimer.setInitialDelay(nextDelay);
                curveGeneratorTimer.setDelay(nextDelay);
                curveGeneratorTimer.restart();
        });
        curveGeneratorTimer.start();
    }

    // adds a new levy curve with random origin & color

    private void addNewCurve() {
        // ensure panel has a valid size
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        int margin = 50; // margin to ensure curves don't clutter edges 
        double originX = margin + rand.nextDouble() * (getWidth() - 2 * margin);
        double originY = margin + rand.nextDouble() * (getHeight() - 2 * margin);

        Color color = colors.get(rand.nextInt(colors.size()));
        double size = 0.5 + rand.nextDouble() * 1.5;

        // create curve, scale by scaling factor
        LevyCurves curve = new LevyCurves(originX / scalingFactor, originY / scalingFactor, color, size);
        curves.add(curve);
    }


    // This is the method for custom drawing.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Clears the background & housekeeping
        
        // Cast to Graphics2D for better control (optional but common)
        Graphics2D g2d = (Graphics2D) g;

        // Set rendering hints for bettter graphics quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Levy Curve calculations 
        for (AnimatedPoint ap : animatedPoints) {
            double dx = ap.x;
            double dy = ap.y;

            int drawX = (int) (dx * scalingFactor);
            int drawY = (int) (dy * scalingFactor);

            // assign color based on curve
            Color colorWithAlpha = new Color(
                ap.color.getRed(), 
                ap.color.getGreen(), 
                ap.color.getBlue(),
                Math.min(255, Math.max(0, (int) (ap.alpha * 255)))
            );
            g2d.setColor(colorWithAlpha);

            // small circle for better visibility 
            g2d.drawLine(drawX, drawY, drawX, drawY);
        }
    }
}
