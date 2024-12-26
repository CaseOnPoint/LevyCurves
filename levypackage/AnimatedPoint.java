package levypackage;

import java.awt.Color;

class AnimatedPoint {
    double x, y;
    Color color;
    float alpha; // opacity (0.0f to 1.0f)
    int lifespan; // number of frames before fading away

    public AnimatedPoint(double x, double y, Color color, int lifespan) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.lifespan = lifespan;
        this.alpha = 1.0f;
    }

    // update opacity and lifespan
    public void update() {
        if (lifespan > 0) {
            lifespan--;
            alpha = Math.max(0.0f, alpha - 0.01f); 
        }
    }

    public boolean isDead() {
        return lifespan <= 0;
    }
}
