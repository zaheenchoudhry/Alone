import java.awt.Color;
import java.awt.Graphics;

public class Star extends BackgroundObject{

    private final Color color;
    private final int depth;
    private double yShift = 0;

    public Star(double x, double y, double width, double height, Game game, int depth) {
        super(x, y, width, height, game);
        this.depth = depth;
        if (depth == 1) {
            color = new Color(255, 255, 255);
        } else if (depth == 2) {
            color = new Color(180, 180, 180);
        } else {
            color = new Color(120, 120, 120);
        }
    }
    
    @Override
    public void update() {
        if (x + width < 0) {
            x = game.WINDOW_WIDTH + width;
        } else {
            x -= 0.3 / depth;
        }
        yShift += game.getYOffset(depth * 4, yShift);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        //double physicalYOffset =  / (10 * depth) * (game.getYOffset() > 0 ? 1 : -1);
        g.fillOval((int) x, (int) (y + yShift), (int) width, (int) height);
    }
}
