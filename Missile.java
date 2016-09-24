import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Missile extends Obstacle{

    private double yShift = 0;
    private int warningTransparency, counter, flashTransparency, explosionSize;;
    private boolean onScreen;

    public Missile(Game game) {
        super(-200, 500, 40, 40, game);
    }
    
    @Override
    public double getX() {
        if (!this.hit) {
            return this.x;
        } else {
            return - 500;
        }
    }
    
    @Override
    public double getEndX() {
        if (!this.hit) {
            return this.x + this.width + 50;
        } else {
            return - 500;
        }
    }
    
    @Override
    public double getY() {
        if (!this.hit) {
            return this.y + yShift;
        } else {
            return - 500;
        }
    }
    
    @Override
    public double getEndY() {
        if (!this.hit) {
            return this.y + this.height + yShift;
        } else {
            return - 500;
        }
    }
    
    @Override
    public void update() {
        if (x + 10 * 30 < 0 && !this.hit) {
            x = game.WINDOW_WIDTH + Math.random() * 20000 + 20000;
            y += ((game.getPlayerYPos() - 11) - y) / 10;
            counter = 0;
            flashTransparency = 255;
            explosionSize = 0;
            onScreen = false;
            warningTransparency = 0;
        } else if (x < game.WINDOW_WIDTH + 200) {
            if (x > game.WINDOW_WIDTH && x <= game.WINDOW_WIDTH + 40) {
                try {
                    game.playSoundEffect(game.MISSILE_SWOOSH);
                } catch (Exception ex) {
                }
                y -= yShift;
                onScreen = true;
            }
        } else if (x < game.WINDOW_WIDTH + 1500) {
            if (x + 40 >= game.WINDOW_WIDTH + 1500 && !game.isDead()) {
                try {
                    game.playSoundEffect(game.MISSLE_WARNING);
                } catch (Exception ex) {
                }
            }
            if ((int) x / 80 % 2 == 0) {
                warningTransparency = 0;
            } else {
                warningTransparency = 255;
            }
        } else if (x < game.WINDOW_WIDTH + 5000) {
            y += (((game.getPlayerYPos() - 11) - y)) / 10;
            warningTransparency = 255;
        } else if (game.getDistance() % 1000 > 850 || game.getDistance() % 1000 < 50) {
            x += 40;
            warningTransparency = 0;
        }
        x -= 40;
        yShift += game.getYOffset(1, yShift);
        if (this.hit) {
            if (counter < 5) {
                explosionSize += this.width;
            } else if (counter > 150) {
                if (this.playerHit) {
                    game.setDamage(false);
                    this.playerHit = false;
                }
                this.hit = false;
            }
            if (flashTransparency != 0) {
                flashTransparency -= 51;
            }
            counter++;
        }
    }
    
    @Override
    public void paint(Graphics g) {
        if (!this.hit) {
            if (onScreen) {
                for (int i = 0; i < 10; i++) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.rotate(Math.toRadians(Math.random() * 360), x + 30 * i + (width - 2 * i) / 2, y + i + (height - 2 * i) / 2 + yShift);
                    int randomNumber = (int) (Math.random() * 20 * i);
                    g2.setColor(new Color(250 - randomNumber, 250 - randomNumber, 250 - randomNumber, (int) (200 - 10 * i - Math.random() * 50)));
                    //double physicalYOffset =  / (10 * depth) * (game.getYOffset() > 0 ? 1 : -1);
                    g2.fillRect((int) x + 30 * i, (int) (y + i + yShift), (int) width - 2 * i, (int) height - 2 * i);
                }
                g.setColor(new Color(50, 50, 50, (int) (100 - Math.random() * 50)));
                g.fillOval((int) x + 5, (int) (y + 5 + yShift), 30, 30);
            } else {                
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(new Color(200, 200, 200, warningTransparency));
                g2d.drawOval(game.WINDOW_WIDTH - 51, (int) y - 21, (int) width + 2, (int) height + 2);
                g2d.setColor(new Color(255, 255, 255, warningTransparency));
                g2d.drawOval(game.WINDOW_WIDTH - 50, (int) y - 20, (int) width, (int) height);


                g2d.setFont(game.getFont().deriveFont(Font.BOLD, 36f));
                g2d.drawString("!", (int) (game.WINDOW_WIDTH - 30 - g.getFontMetrics(game.getFont().deriveFont(Font.BOLD, 36f)).stringWidth("!") / 2), (int) y + 13);
                g2d.drawLine(game.WINDOW_WIDTH - 60, (int) y, game.WINDOW_WIDTH - 120, (int) y);
                g2d.drawLine(game.WINDOW_WIDTH - 120, (int) y, game.WINDOW_WIDTH - 100, (int) y - 12);
                g2d.drawLine(game.WINDOW_WIDTH - 120, (int) y, game.WINDOW_WIDTH - 100, (int) y + 12);
            }
        } else if (counter < 5) {
            g.setColor(new Color(255, 255, 255, flashTransparency));
            g.fillRect(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
            g.setColor(new Color(255, 255, 255));
            g.fillOval((int) x - explosionSize / 2, (int) y - explosionSize / 2 + (int) yShift, explosionSize, explosionSize);
        }
    }
}
