import java.awt.Color;
import java.awt.Graphics;

public class Laser extends BackgroundObject {

    private Color color;
    private int depth;

    public Laser(Game game) {
        super(0, 0, 0, 0, game);
        depth = (int) (Math.random() * 3 + 1);
    	reset();
    }
    
    public final void reset() {
        depth = (int) (Math.random() * 3 + 1);
        x = game.WINDOW_WIDTH + (int)(Math.random() * 8000);
        y = Math.random() * game.WINDOW_HEIGHT;
        width = 500 / depth;
        height = 1;
        color = new Color(230 - 30 * depth, 235 - 30 * depth, 235 - 30 * depth);
    }
    
    @Override
    public void update() {
        if (x + width < 0) {
            reset();
        } else {
            x -= game.getSpeed(2 + 1.0 / depth);
        }
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, (int) width, (int) height);
    }
}