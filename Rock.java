import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class Rock extends Obstacle{

    private double yShift = 0, xSpeed = 0, ySpeed = 0, rotationSpeed = 0, rotation = 0;
    private int[] xPoints = new int[6], yPoints = new int[6];
    private double lowestPoint, highestPoint, leftMostPoint, rightMostPoint;
    private int counter, flashTransparency, explosionSize;

    public Rock(Game game) {
        super(0, 0, 0, 0, game);
        reset();
    }
    
    public void reset() {
        x = game.WINDOW_WIDTH + 1000 + Math.random() * 5000;
        y = Math.random() * 475 + 100;
        this.hit = false;
        counter = 0;
        flashTransparency = 255;
        explosionSize = 0;
        ySpeed = (game.WINDOW_HEIGHT / 2 - y) / 50 ;//+ Math.abs(game.WINDOW_HEIGHT / 2 - y) / (game.WINDOW_HEIGHT / 2 - y) * 5;
        xSpeed = Math.random() * (game.getSpeed(.5)) + 10;
        rotationSpeed = Math.random() * 3 * ((Math.random() * 10) >= 5 ? 1 : -1);
        width = height = 75 + Math.random() * 100;
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
        
        lowestPoint = yPoints[4];
        highestPoint = yPoints[1];
        leftMostPoint = xPoints[0];
        rightMostPoint = xPoints[2];
    }
    
    @Override
    public double getX() {
        if (!this.hit) {
            return leftMostPoint;
        } else {
            return - 500;
        }
    }
    
    @Override
    public double getEndX() {
        if (!this.hit) {
            return rightMostPoint;
        } else {
            return - 500;
        }
    }
    
    @Override
    public double getY() {
        if (!this.hit) {
            return highestPoint + yShift;
        } else {
            return - 500;
        }
    }
    
    @Override
    public double getEndY() {
        if (!this.hit) {
            return lowestPoint + yShift;
        } else {
            return - 500;
        }
    }
    
    @Override
    public void update() {
        yShift += game.getYOffset(1, yShift);
        rotation += this.rotationSpeed;
        if (rotation > 360) {
            rotation -= 360;
        }
        if (rightMostPoint + 100 < 0 && !this.hit) {
            reset();
        }
        if ((game.getDistance() % 1000 < 850 && game.getDistance() % 1000 > 50) || leftMostPoint < game.WINDOW_WIDTH + 200) {
            x -= xSpeed;
            xPoints[0] -= xSpeed;
            xPoints[1] -= xSpeed;
            xPoints[2] -= xSpeed;
            xPoints[3] -= xSpeed;
            xPoints[4] -= xSpeed;
            xPoints[5] -= xSpeed;
        }
        if (leftMostPoint < game.WINDOW_WIDTH + 200) {
            yPoints[0] += ySpeed;
            yPoints[1] += ySpeed;
            yPoints[2] += ySpeed;
            yPoints[3] += ySpeed;
            yPoints[4] += ySpeed;
            yPoints[5] += ySpeed;
        }
        lowestPoint = yPoints[4];
        highestPoint = yPoints[1];
        leftMostPoint = xPoints[0];
        rightMostPoint = xPoints[2];
        for (int i = 0; i < 6; i++) {
            double[] point = {xPoints[i], yPoints[i]};
            AffineTransform.getRotateInstance(Math.toRadians(rotation), (xPoints[0] + xPoints[3]) / 2, (yPoints[1] + yPoints[4]) / 2).transform(point, 0, point, 0, 1);

            if (point[0] < leftMostPoint) {
                leftMostPoint = point[0];
            }
            if (point[0] > rightMostPoint) {
                rightMostPoint = point[0];
            }
            if (point[1] < highestPoint) {
                highestPoint = point[1];
            }
            if (point[1] > lowestPoint) {
                lowestPoint = point[1];
            }
        }
        if (this.hit) {
            if (counter < 1) {
                x = (leftMostPoint + rightMostPoint) / 2;
                y = (lowestPoint + highestPoint) / 2 + yShift;
            } else if (counter < 6) {
                explosionSize += this.width / 3;
                flashTransparency -= 51;
            } else if (counter > 150) {
                if (this.playerHit) {
                    game.setDamage(false);
                    this.playerHit = false;
                }
                this.hit = false;
            }
            counter++;
        }
    }
    
    @Override
    public void paint(Graphics g) {
        if (!this.hit) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(Math.toRadians(rotation), (xPoints[0] + xPoints[3]) / 2, (yPoints[1] + yPoints[4]) / 2 + yShift);
            g2d.setColor(game.getSpikesColor(1));
            g2d.fillPolygon(xPoints, new int[]{yPoints[0] + (int) yShift, yPoints[1] + (int) yShift, yPoints[2] + (int) yShift, yPoints[3] + (int) yShift, yPoints[4] + (int) yShift, yPoints[5] + (int) yShift}, 6);

            GeneralPath border = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
            g2d.setColor(game.getBorderColor());
            g2d.setStroke(new BasicStroke(8));
            border.moveTo(xPoints[5], yPoints[5] + yShift);
            for (int i = 0; i < xPoints.length; i++) {
                border.lineTo(xPoints[i], yPoints[i] + yShift);
            }
            g2d.draw(border);
            if (game.debug()) {
                g.setColor(new Color(255, 255, 255));
                g.fillOval((int) (leftMostPoint + rightMostPoint) / 2 - 5, (int) (getY() - 5), 10, 10);
                g.fillOval((int) (leftMostPoint + rightMostPoint) / 2 - 5,  (int) (getEndY() - 5), 10, 10);
                g.fillOval((int) getX(), (int) ((lowestPoint + highestPoint) / 2 + yShift - 5), 10, 10);
                g.fillOval((int) getEndX(), (int) ((lowestPoint + highestPoint) / 2 + yShift - 5), 10, 10);
            }
        } else if (counter < 6) {
            g.setColor(new Color(255, 255, 255, flashTransparency));
            g.fillRect(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
            g.setColor(new Color(255, 255, 255));
            g.fillOval((int) x - explosionSize / 2, (int) y - explosionSize / 2 + (int) yShift, explosionSize, explosionSize);
        }
    }
}
