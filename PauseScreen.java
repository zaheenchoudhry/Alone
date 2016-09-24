import java.awt.Color;
import java.awt.Graphics;

public class PauseScreen extends BackgroundObject{
    
    Color fontColor, backgroundDimColor;
    
    public PauseScreen(Game game) {
        super(game.WINDOW_WIDTH / 2, 0, 0, 0, game);
        this.fontColor = new Color(200, 200, 200);
        this.backgroundDimColor = new Color(0, 0, 0, 200);
    }
    
    @Override
    public void update() {
    	
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(this.backgroundDimColor);
        g.fillRect(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
        g.setColor(this.fontColor);
        g.setFont(game.getFont().deriveFont(150f));
        g.drawString("PAUSED", (int) x - g.getFontMetrics(game.getFont().deriveFont(150f)).stringWidth("PAUSED") / 2, 400);
        g.setFont(game.getFont().deriveFont(20f));
        g.drawString("CLICK ANYWHERE TO RESUME", (int) x - g.getFontMetrics(game.getFont().deriveFont(20f)).stringWidth("CLICK ANYWHERE TO RESUME") / 2, 625);
    }
}
