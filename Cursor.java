import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Robot;

public class Cursor extends GameObject{

    private int counter = 0, transparency;
    private double rotation = 0, speed = 0;

    public Cursor(Game game) {
        super(game.WINDOW_WIDTH / 2 - 20, game.WINDOW_HEIGHT / 2 - 30, 23, 22, game);
        if (game.animating() || game.getCurrentScreen() == game.ENTER_NIGH_SCORE_NAME) {
            transparency = 0;
        } else {
            transparency = 255;
            counter = 200;
        }
        game.setCursorXPos(game.WINDOW_WIDTH / 2);
        game.setCursorYPos(game.WINDOW_HEIGHT / 2);
    }
    
    public double getY() {
        return this.y;
    }
    
    @Override
    public void update() {
        if (counter < 100) {
            counter++;
        } else if (counter < 130 && game.getCurrentScreen() != game.ENTER_NIGH_SCORE_NAME) {
            transparency += 10;
            counter++;
            if (counter == 125 && game.getCurrentScreen() == game.MAIN_MENU) {
                transparency = 255;
                try {
                    Robot robot = new Robot();
                    robot.mouseMove((int) (MouseInfo.getPointerInfo().getLocation().getX() - game.getXMousePos() + game.WINDOW_WIDTH / 2 + 75), (int) (MouseInfo.getPointerInfo().getLocation().getY() - game.getRealYMousePos() + game.WINDOW_HEIGHT / 2 - 8));
                } catch (AWTException e) {
                }
            } else if (counter > 125) {
                double yMove = (((game.getYMousePos() - (game.WINDOW_HEIGHT/ 2)) * 2.5 + (game.WINDOW_HEIGHT / 2) - (height / 2))) - this.y;
                double xMove = (((game.getXMousePos() - (game.WINDOW_WIDTH / 2)) * 2.5 + (game.WINDOW_WIDTH / 2) - (width / 2))) - this.x - 0.00001;
                rotation = 0;
                transparency = 255;
                this.y += yMove;
                this.x += xMove;
            }
        } else {
            double yMove = (((game.getYMousePos() - (game.WINDOW_HEIGHT/ 2)) * 2.5 + (game.WINDOW_HEIGHT / 2) - (height / 2))) - this.y;
            double xMove = (((game.getXMousePos() - (game.WINDOW_WIDTH / 2)) * 2.5 + (game.WINDOW_WIDTH / 2) - (width / 2))) - this.x;    
            this.y += yMove / 3;
            this.x += xMove / 3;
            this.speed = Math.sqrt(xMove * xMove + yMove * yMove);
            if (this.speed > 60) {
                this.speed = 60;
            }
            rotation = Math.toDegrees(Math.atan(yMove / (xMove))) + ((xMove < 0) ? 180 : 0);
            game.setCursorXPos(this.x + width * ((xMove < 0) ? 0 : 1));
            game.setCursorYPos(this.y);
            
        }
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(rotation), x + width / 2, y + height / 2);
        g2d.setColor(new Color(230, 230, 230, transparency));
        g2d.fillPolygon(new int[]{(int) x, (int) (x + width / 3.5), (int) (x + width), (int) (x + width), (int) (x + width / 3.5), (int) x}, new int[]{(int) (y + height / 6), (int) y, (int) (y + height / 4), (int) (y + height - height / 4), (int) (y + height), (int) (y + height - height / 6)}, 6);
        g2d.setColor(new Color(50, 50, 50, transparency));
        g2d.drawPolygon(new int[]{(int) x, (int) (x + width / 3.5), (int) (x + width), (int) (x + width), (int) (x + width / 3.5), (int) x}, new int[]{(int) (y + height / 6), (int) y, (int) (y + height / 4), (int) (y + height - height / 4), (int) (y + height), (int) (y + height - height / 6)}, 6);
        g2d.drawPolygon(new int[]{(int) (x + width / 3.5), (int) (x + width / 3.5)}, new int[]{(int) y, (int) (y + height)}, 2);
        g2d.fillOval((int) (x + width / 1.7), (int) y + 8, 6, 6);
        g2d.setColor(new Color(225, 75, 55, transparency));
        g2d.fillPolygon(new int[]{(int) x - 5, (int) x - 5, (int) (x - (Math.random() * (this.speed + 5) + 10))}, new int[]{(int) y + 6, (int) y + (int) height - 6, (int) (y + height / 2)},  3);
    }
}
