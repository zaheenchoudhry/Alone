import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class MainMenuButton extends GameObject {

    final int BUTTON_TYPE;
    int borderTransparency, fillTransparency, colorChange = 0, counter = 0;

    public MainMenuButton(double x, double y, double width, Game game, int buttonId) {
        super(x, y, width, width, game);
        BUTTON_TYPE = buttonId;
        if (game.animating() || game.getCurrentScreen() == game.ENTER_NIGH_SCORE_NAME) {
            borderTransparency = 0;
        } else {
            borderTransparency = 255;
        }
        fillTransparency = 0;
    }

    @Override
    public void update() {
        if (game.animating() && counter < 100 - game.getCurrentScreen() * 20) {
            counter++;
        } else if (game.animating() && game.getCurrentScreen() != game.ENTER_NIGH_SCORE_NAME && borderTransparency != 255) {
            borderTransparency += 15 - game.getCurrentScreen() * 2;
            counter++;
        }
        if (!game.animating() && game.getCursorXPos() > x && game.getCursorXPos() < x + width && game.getCursorYPos() < y + height && game.getCursorYPos() + 22 > y && game.getCurrentScreen() != game.ENTER_NIGH_SCORE_NAME) {
            if (fillTransparency != 255) {
                fillTransparency += 17;
                colorChange += 12;
            }
            game.setHoveredButtonType(BUTTON_TYPE);
        } else if (fillTransparency != 0) {
            fillTransparency -= 17;
            colorChange -= 12;
            if (game.getHoveredButtonType() == BUTTON_TYPE) {
                game.setHoveredButtonType(-1);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        
        g.setColor(new Color(240, 240, 240, borderTransparency));
        g.drawRect((int) x, (int) y, (int) width, (int) height);
        g.setColor(new Color(240, 240, 240, fillTransparency));
        g.fillRect((int) x, (int) y, (int) width, (int) height);
        if (BUTTON_TYPE == 0) {
            g.setColor(new Color(240 - colorChange, 240 - colorChange, 240 - colorChange, borderTransparency));
            g.fillPolygon(new int[]{(int) x + 10, (int) x + 25, (int) x + 40, (int) x + 35, (int) x + 35, (int) x + 28, (int) x + 28, (int) x + 22, (int) x + 22, (int) x + 15, (int) x + 15}, new int[]{(int) y + 25, (int) y + 10, (int) y + 25, (int) y + 25, (int) y + 40, (int) y + 40, (int) y + 30, (int) y + 30, (int) y + 40, (int) y + 40, (int) y + 25}, 11);
        } else if (BUTTON_TYPE == 2) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(240, 240, 240));
            int[] xPoints = {(int) x + 23, (int) x + 27, (int) x + 30, (int) x + 20};
            int[] yPoints = {(int) y + 5, (int) y + 5, (int) y + 18, (int) y + 18};
            for (int i = 0; i < 8; i++) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(240 - colorChange, 240 - colorChange, 240 - colorChange, borderTransparency));
                g2.rotate(Math.toRadians(22.5 + 45 * i), (int) x + 25, (int) y + 25);
                g2.fillPolygon(xPoints, yPoints, 4);
            }
        } else if (BUTTON_TYPE == 1) {
            g.setColor(new Color(240 - colorChange, 240 - colorChange, 240 - colorChange, borderTransparency));
            g.fillPolygon(new int[]{(int) x + 10, (int) x + 20, (int) x + 20, (int) x + 30, (int) x + 30, (int) x + 40, (int) x + 40, (int) x + 10}, new int[]{(int) y + 18, (int) y + 18, (int) y + 10, (int) y + 10, (int) y + 26, (int) y + 26, (int) y + 40, (int) y + 40}, 8);
            g.setColor(new Color(20 + colorChange, 20 + colorChange, 20 + colorChange, borderTransparency));
            g.setFont(game.getFont().deriveFont(10f));
            g.drawString("1", (int) (x + 25 - g.getFontMetrics(game.getFont().deriveFont(10f)).stringWidth("1") / 2), (int) y + 20);
            g.drawString("2", (int) (x + 15 - g.getFontMetrics(game.getFont().deriveFont(10f)).stringWidth("1") / 2), (int) y + 28);
            g.drawString("3", (int) (x + 35 - g.getFontMetrics(game.getFont().deriveFont(10f)).stringWidth("1") / 2), (int) y + 36);
        } else if (BUTTON_TYPE == 3){
            g.setColor(new Color(240 - colorChange, 240 - colorChange, 240 - colorChange, borderTransparency));
            g.fillPolygon(new int[]{(int) x + 10, (int) x + 14, (int) x + 40, (int) x + 36}, new int[]{(int) y + 14, (int) y + 10, (int) y + 36, (int) y + 40}, 4);
            g.fillPolygon(new int[]{(int) x + 10, (int) x + 14, (int) x + 40, (int) x + 36}, new int[]{(int) y + 36, (int) y + 40, (int) y + 14, (int) y + 10}, 4);
        }
    }
}
