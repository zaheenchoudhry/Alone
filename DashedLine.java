import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class DashedLine extends BackgroundObject {

    private Color color;
    Stroke dashed;
    double yShift = 0;
    
    public DashedLine(Game game) {
        super(300 + (game.BREAK_DISTANCE / 10) * 50, -300, 0, game.WINDOW_HEIGHT, game);
        color = new Color(230, 230, 230);
        dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9},0);
    }
    
    public void reset() {
        color = new Color(230, 230, 230);
        x = (game.BREAK_DISTANCE / 10) * 50 + x;     
    }
    
    @Override
    public void update() {
        if (this.x < 0) {
            reset();
        } else if (this.x <= 322) {
            color = new Color(100, 100, 100);
        }
        x -= game.getSpeed(1);
        yShift += game.getYOffset(1, yShift);
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setStroke(this.dashed);
        g2d.setColor(color);
        g2d.drawLine((int) x, (int) y + (int) yShift, (int) x, game.WINDOW_HEIGHT);
    }
}