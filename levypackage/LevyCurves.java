package levypackage;

import java.util.Random;

// package private class to return coords (non-public) to other files without compiler warning
class LevyCurves { 
    // keep track of current coords, no need to remember old ones 
    private Coords currCoords;
    private final Random rand = new Random(); // determine which transformation

    // math vars
    double newX = 0;
    double newY = 0;
    double sf = (1 / Math.sqrt(2));
    double theta = Math.PI / 4;
    float delta_x = 0.5f;

    public LevyCurves() {
        currCoords = new Coords(0, 0);
    }

    public Coords calculateCoords() {
        // levy logic
        int r = rand.nextInt(4);
        switch(r) {
            case 0:
                // apply T1 | 45 degrees
                newX = (sf * ((currCoords.x() * Math.cos(theta)) - currCoords.y() * Math.sin(theta)));
                newY = (sf * ((currCoords.x() * Math.sin(theta)) + currCoords.y() * Math.cos(theta)));
            case 1: 
                // apply T2 | -45 degrees
                newX = (sf * ((currCoords.x() * Math.cos(-theta)) - currCoords.y() * Math.sin(-theta)));
                newY = (sf * ((currCoords.x() * Math.sin(-theta)) + currCoords.y() * Math.cos(-theta)));
                newX += delta_x; // small translation
            case 2:
                // apply T3
                double theta3 = 3 * Math.PI / 4; // 135 degrees
                newX = sf * (currCoords.x() * Math.cos(theta3) - currCoords.y() * Math.sin(theta3));
                newY = sf * (currCoords.x() * Math.sin(theta3) + currCoords.y() * Math.cos(theta3));
                newX += delta_x; // Small translation
                break;
            case 3:
                // Apply T4
                double theta4 = -3 * Math.PI / 4; // -135 degrees
                newX = sf * (currCoords.x() * Math.cos(theta4) - currCoords.y() * Math.sin(theta4));
                newY = sf * (currCoords.x() * Math.sin(theta4) + currCoords.y() * Math.cos(theta4));
                newX += delta_x; // Small translation
                break;
            default:
                // Default to T1
                newX = sf * (currCoords.x() * Math.cos(theta) - currCoords.y() * Math.sin(theta));
                newY = sf * (currCoords.x() * Math.sin(theta) + currCoords.y() * Math.cos(theta));

        }

        currCoords = new Coords(newX, newY);

        return currCoords;
    }

    // small class to neatly return coords and encapsulate data
    // doesn't copy when updating coords obj, better mem usage 
    static record Coords(double x, double y) {}
}



