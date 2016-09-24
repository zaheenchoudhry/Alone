import java.awt.Color;
import java.awt.Graphics;

public class WeaponLaser extends CollisionObject {

    private final Color color;
    private double rechargeFill = 75;
    private double yShift = 0;
    private int completeTransprency = 255, change = 0;

    public WeaponLaser(Game game) {
        super(-200, 0, 200, 10, game);
        color = new Color(230, 230, 230);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getEndX() {
        return x + width;
    }

    @Override
    public double getY() {
        return y + yShift - 10;
    }

    @Override
    public double getEndY() {
        return y + height + yShift + 10;
    }
    
    @Override
    public void update() {
        if (x > game.WINDOW_WIDTH) {
            x = -100;
        } else if (x > 0) {
            x += 40;
            Main.checkCollision(this);
        } else if (game.laserShot()) {
            this.rechargeFill = 0;
            completeTransprency = 255;
            x = game.getPlayerXPos() + 11;
            y = game.getPlayerYPos() - height / 2 - yShift;
            game.togglelaserShot();
            game.setLaserAvailability(false);
        }
        
        if (this.rechargeFill < 150) {
            rechargeFill += 0.5;
            if ((int) rechargeFill == 150) {
                game.setLaserAvailability(true);
                try {
	                game.playSoundEffect(game.RECHARGE_COMPLETE);
	            } catch (Exception ex) {
	            }
            }
        } else {
            if (completeTransprency == 255) {
                change = -5;
            } else if (completeTransprency == 150) {
                change = 5;
            }
            completeTransprency += change;
        }
        yShift += game.getYOffset(1, yShift);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(230, 230, 230, completeTransprency));
        g.drawRect(20, 20, 150, 15);
        g.fillRect(20, 20, (int) this.rechargeFill, 15);
        if (x > 0) {
            g.setColor(color);
            g.fillRect((int) x, (int) y + 11 + (int) yShift, (int) width, (int) height);
        }
    }
}