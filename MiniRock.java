import java.awt.Graphics;
import java.awt.Graphics2D;

public class MiniRock extends BackgroundObject{

    private double yShift = 0, xSpeed = 0, ySpeed = 0, rotationSpeed = 0, rotation = 0;
    private int[] xPoints = new int[6], yPoints = new int[6];
    private int type;

    public MiniRock(int type, Game game) {
        super(-600, game.getPlayerYPos(), 0, 0, game);  
        this.type = type;
    }
    
    public void reset() {
        if (this.type == 0) {
            this.x = game.WINDOW_WIDTH + 200 + Math.random() * 5000;
            this.y = -500;
            xSpeed = Math.random() * (game.getSpeed(.5)) + 15;
            ySpeed = Math.random() * 5 + 5;
            rotationSpeed = Math.random() * 3 * ((Math.random() * 10) >= 5 ? 1 : -1);
        } else {
            this.y = game.getPlayerYPos() - 80 * type;
            this.x = game.getPlayerXPos();
            ySpeed = (Math.random() * 5 + 8) * type;
            xSpeed = Math.random() * (game.getSpeed(1)) - (game.getSpeed(.5));
            rotationSpeed = Math.random() * 5 * ((Math.random() * 10) >= 5 ? 1 : -1);
        }
        width = height = 10 + Math.random() * 10;
        xPoints[0] = (int)(this.x - width / 6 - Math.random() * (width / 3));
        xPoints[1] = (int)(this.x + width / 6 - Math.random() * (width / 3));
        xPoints[2] = (int)(this.x + width / 6 + Math.random() * (width / 3));
        xPoints[3] = (int)(this.x + width / 6 + Math.random() * (width / 3));
        xPoints[4] = (int)(this.x + width / 6 - Math.random() * (width / 3));
        xPoints[5] = (int)(this.x - width / 6 - Math.random() * (width / 3));
        
        yPoints[0] = (int)(y -  Math.random() * (height / 2));
        yPoints[1] = (int)(yPoints[0] - Math.random() * (height / 2 - (y - yPoints[0])));
        yPoints[2] = (int)(y - Math.random() * (height / 2));
        yPoints[3] = (int)(y + Math.random() * (height / 2));
        yPoints[4] = (int)(yPoints[3] + Math.random() * (height / 2 - (yPoints[3] - y)));
        yPoints[5] = (int)(y + Math.random() * (height / 2));
    }
    
    @Override
    public void update() {
        if (x < -500 || y < - 500 || y > game.WINDOW_HEIGHT + 500) {
            if (this.type == 0) {
                reset();
            }
        } else {
            x -= xSpeed;
            xPoints[0] -= xSpeed;
            xPoints[1] -= xSpeed;
            xPoints[2] -= xSpeed;
            xPoints[3] -= xSpeed;
            xPoints[4] -= xSpeed;
            xPoints[5] -= xSpeed;
            
            y += ySpeed;
            yPoints[0] += ySpeed;
            yPoints[1] += ySpeed;
            yPoints[2] += ySpeed;
            yPoints[3] += ySpeed;
            yPoints[4] += ySpeed;
            yPoints[5] += ySpeed;
            yShift = game.getYOffset(1, yShift);
            rotation += this.rotationSpeed;
            if (rotation > 360) {
                rotation -= 360;
            }
        }     
    }
    
    public void start(int type) {
        this.type = type;
        reset();
    }
    
    public boolean isAvailable() {
        if (type != 0 && (x < -500 || y < - 500 || y > game.WINDOW_HEIGHT + 500)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(rotation), (xPoints[0] + xPoints[3]) / 2, (yPoints[1] + yPoints[4]) / 2 + yShift);
        g2d.setColor(game.getSpikesColor(1));
        g2d.fillPolygon(xPoints, new int[]{yPoints[0] + (int) yShift, yPoints[1] + (int) yShift, yPoints[2] + (int) yShift, yPoints[3] + (int) yShift, yPoints[4] + (int) yShift, yPoints[5] + (int) yShift}, 6);
    }
}
