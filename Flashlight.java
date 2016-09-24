import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Flashlight extends BackgroundObject{

    int transparencyFlicker;
    double rotation = 0, innerLightRotation = 0;

    public Flashlight(Game game) {
        super(-400, -900, game.WINDOW_WIDTH + 700, game.WINDOW_HEIGHT + 900, game);
        transparencyFlicker = 0;
    }
    
    @Override
    public void update() {
        transparencyFlicker = (int) (Math.random() * 30);
        innerLightRotation += (game.getPlayerRotation() - this.rotation) / 3.5;
        rotation += (game.getPlayerRotation() - this.rotation) / 3;
    }
    
    @Override
    public void paint(Graphics g) {
        if (!game.isDead()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(Math.toRadians(rotation), game.getPlayerXPos() + 11, game.getPlayerYPos() + 11);
            g2d.setColor(new Color(10, 10, 10, 245 + transparencyFlicker / 3));
            g2d.fillPolygon(new int[]{(int) x, (int) width, (int) game.getPlayerXPos() + 16, (int) width, (int) x}, new int[]{(int) y, (int) y, (int) game.getPlayerYPos() + 11, (int) height, (int) height}, 5);
            int innerlightOffset = (int) ((rotation - innerLightRotation) * 60);
            g2d.setColor(new Color(10, 10, 10, 160 + transparencyFlicker));
            g2d.fillPolygon(new int[]{(int) game.getPlayerXPos() + 16, (int) width, (int) width}, new int[]{(int) game.getPlayerYPos() + 11, (int) y, (int) y + 500 - innerlightOffset}, 3);
            g2d.fillPolygon(new int[]{(int) game.getPlayerXPos() + 16, (int) width, (int) width}, new int[]{(int) game.getPlayerYPos() + 11, (int) height - 500 - innerlightOffset, (int) height}, 3);
            g2d.setColor(new Color(200 - transparencyFlicker * 6, 200 - transparencyFlicker * 6, 200 - transparencyFlicker * 6, 30 + transparencyFlicker * 2));
            g2d.fillPolygon(new int[]{(int) game.getPlayerXPos() + 16, (int) width, (int) width}, new int[]{(int) game.getPlayerYPos() + 11, (int) y + 500 - innerlightOffset, (int) height - 500 - innerlightOffset}, 3);
            g2d.setColor(new Color(250, 250, 250, 230 - transparencyFlicker));
        } else {
            g.setColor(new Color(10, 10, 10, 245));
            g.fillRect(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
        }
    }
}
