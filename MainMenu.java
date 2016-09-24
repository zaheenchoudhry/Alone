import java.awt.Color;
import java.awt.Graphics;

public class MainMenu extends GameObject {

    int backgroundTransparency, titleTransparency, transparency, counter;

    public MainMenu(Game game) {
        super(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT, game);
        backgroundTransparency = 255;
        titleTransparency = 0;
        transparency = 0;
        counter = 0;
    }

    @Override
    public void update() {
        if (counter < 10) {
        } else if (counter < 50) {
            backgroundTransparency -= 2;
        } else if (counter < 55) {
        } else if (counter < 70) {
            titleTransparency += 10;
        } else if (counter < 100) {
        } else if (counter < 125) {
            transparency += 10;
        }
        if (game.animating()) {
            counter++;
            if (counter == 125) {
                game.setAnimating(false);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0, 0, 0, backgroundTransparency));
        g.fillRect((int) x,(int) y,(int) width,(int) height);
        g.setColor(new Color(230, 230, 230, titleTransparency));
        g.setFont(game.getFont().deriveFont(120f));
        g.drawString("ALONE", (int) (width - g.getFontMetrics(game.getFont().deriveFont(120f)).stringWidth("ALONE")) / 2, 360);
        g.setColor(new Color(230, 230, 230, transparency));
        g.setFont(game.getFont().deriveFont(16f));
        g.drawString("CLICK TO BEGIN YOUR INTERGALACTIC JOURNEY", (int) (width - g.getFontMetrics(game.getFont().deriveFont(16f)).stringWidth("CLICK TO BEGIN YOUR INTERGALACTIC JOURNEY")) / 2, 410);
    }
}
