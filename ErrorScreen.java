import java.awt.Color;
import java.awt.Graphics;

public class ErrorScreen extends GameObject {

    Color color;
    String error, message;

    public ErrorScreen(String error, String message, Game game) {
        super(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT, game);
        color = new Color(230, 230, 230);
        this.error = error;
        this.message = message;
    }
    
    @Override
    public void update() {
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(this.color);
        g.setFont(game.getFont().deriveFont(120f));
        g.drawString("ERROR", (int) (width / 2 - g.getFontMetrics(game.getFont().deriveFont(120f)).stringWidth("ERROR") / 2), 130);
        g.setFont(game.getFont().deriveFont(60f));
        g.drawString(error, (int) (width / 2 - g.getFontMetrics(game.getFont().deriveFont(60f)).stringWidth(error) / 2), 380);
        g.setFont(game.getFont().deriveFont(18f));
        g.drawString(message, (int) (width / 2 - g.getFontMetrics(game.getFont().deriveFont(18f)).stringWidth(message) / 2), 420);
    }
}
