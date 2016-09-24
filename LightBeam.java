import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class LightBeam extends BackgroundObject{

    double transparency, depth;
    GradientPaint gPaint;

    public LightBeam(double x, double width, int depth, Game game) {
    	super(x, -100, width, game.WINDOW_HEIGHT + 200, game);
    	this.depth = depth;
        gPaint = new GradientPaint((int) x,(int) y, new Color(230, 230, 230, (int) (100 - depth * 30)),(int) x, (int) (y + height), new Color (230, 230, 230, 0), false);
    }
    
    public void reset() {
        x = game.WINDOW_WIDTH + 400 + Math.random() / depth * 150;
        width = 20 + Math.random() * 80;
    }
    
    @Override
    public void update() {
    	if (x + width < 0) {
            reset();
    	} else {
            x -= 8 / depth;
    	}
    }
    
    @Override
    public void paint(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(45), x, y);
        g2d.setPaint(gPaint);
    	g2d.fill(new Rectangle2D.Double(x, y, width, height));
    }
}