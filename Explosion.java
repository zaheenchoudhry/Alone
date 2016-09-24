import java.awt.Color;
import java.awt.Graphics;

public class Explosion extends BackgroundObject{

    boolean animationComplete;
    int transparency;
    Color color;

    public Explosion(double x, double y, Game game) {
        super(x, y, 10, 10, game);
        transparency = 255;
        animationComplete = false;
        color = new Color(220, 220, 220, transparency);
    }
    
    @Override
    public void update() {
        if (!animationComplete) {
            x -= game.getSpeed(1);
            if (game.WINDOW_WIDTH - x < 3000) {
                width += 250;
                height += 250;
            }
            if (transparency > 100 && width > 2500) {
                transparency -= 50;
            } else if (width > 2500) {
                transparency = 0;
                animationComplete = true;
            }
            color = new Color(220, 220, 220, transparency);
        } else {
            game.showDeathScreen();
        }
    }
    
    @Override
    public void paint(Graphics g) {
        if (!animationComplete) {
            g.setColor(color);
            //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g.fillOval((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);
        }
    }
}
