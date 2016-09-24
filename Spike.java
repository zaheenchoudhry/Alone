import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class Spike extends Obstacle {

    private Color color;
    private final int type, depth;
    private int[] xPoints = new int[5], yPoints = new int[5];
    private double spikePeakX, yChange, yShift = 0;
    private Spike previous, next;

    public Spike(double x, double y, double width, double height, Game game, int type, double yChange, double spikePeakX, int depth) {
        super(x, y, width, height, game);
        this.type = type;
        this.yChange = yChange;
        this.spikePeakX = spikePeakX;
        this.depth = depth;
        previous = null;
        next = null;
    }

    public void change(double x, double y, double width, double height, double yChange, double spikePeakX) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.yChange = yChange;
        this.spikePeakX = spikePeakX;
    }

    public void setPrevious(Spike previous) {
        this.previous = previous;
    }

    public void setNext(Spike next) {
        this.next = next;
    }

    public Spike getPrevious() {
        return previous;
    }

    public Spike getNext() {
        return next;
    }

    @Override
    public double getX() {
        return x;
    }
    
    @Override
    public double getY() {
        return y;
    }

    public double getXPeak() {
        return x + spikePeakX;
    }

    public double getYPeak() {
        return y + height * type + yShift;
    }

    @Override
    public double getEndY() {
        return y + yChange;
    }

    @Override
    public double getEndX() {
        return x + width;
    }

    public int getType() {
        return type;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public void update() {
        x -= game.getSpeed(1f / depth);
        yShift += game.getYOffset(depth, yShift);
        xPoints[0] = (int) x;
        xPoints[1] = (int) (x + width);
        xPoints[2] = (int) (x + width);
        xPoints[3] = (int) (x + spikePeakX);
        xPoints[4] = (int) x;

        yPoints[0] = (type > 0) ? -1 : game.WINDOW_HEIGHT + 1;
        yPoints[1] = (type > 0) ? -1 : game.WINDOW_HEIGHT + 1;
        yPoints[2] = (int) (y + yChange + yShift);
        yPoints[3] = (int) (y + height * type + yShift);
        yPoints[4] = (int) (y + yShift);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(game.getSpikesColor(depth));
        g.fillPolygon(this.xPoints, this.yPoints, 5);
        
        if (depth ==  1) {
            Graphics2D g2d = (Graphics2D) g.create();
            GeneralPath border = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
            g2d.setColor(game.getBorderColor());
            g2d.setStroke(new BasicStroke(8));
            border.moveTo(xPoints[2], yPoints[2]);
            border.lineTo(xPoints[3], yPoints[3]);
            border.lineTo(xPoints[4], yPoints[4]);
            g2d.draw(border);
        }
    }
}