import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;

public class DebugInfo extends BackgroundObject{
    
    Color color;
    long previousTime;
    int counter = 0;
    String fps;
    
    public DebugInfo(Game game) {
        super(0, 0, 0, 0, game);
        color = new Color(255, 255, 255, 180);
        previousTime = System.currentTimeMillis();
        fps = "";
    }
    
    @Override
    public void update() {
        if (counter == 8) {
            fps = "" + (int) (1000 / (System.currentTimeMillis() - this.previousTime + 0.0001));
        }
        this.previousTime = System.currentTimeMillis();
        if (counter > 8) {
            counter -= 8;
        }
        counter++;
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.setFont(game.getFont().deriveFont(20f));
        g.drawString(fps + " <- FPS", 10, (int) y + 20);
        g.drawString(game.getRealYMousePos() + " <- Y MOUSE POS RELATIVE TO CONTAINER", 10, (int) y + 40);
        g.drawString(MouseInfo.getPointerInfo().getLocation().getY() + " <- Y MOUSE POS RELATIVE TO SCREEN", 10, (int) y + 60);
    }
}
