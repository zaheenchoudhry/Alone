
import java.awt.Graphics;

public class Planet extends BackgroundObject {

    int yShift;

    public Planet(Game game) {
        super(0, 0, 0, 0, game);
    }

    public void reset() {
        x = game.WINDOW_WIDTH + 200 + Math.random() * 700;
        width = height = Math.random() * 250 + 150;
        y = Math.random() * (game.WINDOW_HEIGHT - height - 200) + 100;
    }
    
    @Override
    public void update() {
        if (x + width < 0) {
            reset();
        }
        x -= game.getSpeed(0.02);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(game.getBackgroundObjectColor(0));
        g.fillOval((int) x, (int) y + yShift, (int) width, (int) height);
        g.setColor(game.getBackgroundObjectColor(1));
        g.fillOval((int) x, (int) y + yShift, (int) width - 10, (int) height - 10);
    }
}
