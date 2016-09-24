import java.awt.Color;
import java.awt.Graphics;

public class ScreenWipe extends BackgroundObject{

    int transparency;
    int numOfSpikes = 10;
    Color color;

    public ScreenWipe(Game game) {
        super(1600, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT, game);
        transparency = 0;
        color = new Color(0, 0, 0, 0);
    }
    
    @Override
    public void update() {
        if (transparency < 255) {
            transparency += 3;
        } else {
            Main.deathScreen();
        }
        color = new Color(0, 0, 0, transparency);
    }
    
    @Override
    public void paint(Graphics g) {
        //g.setColor(new Color(0, 0, 0, transparency));
        g.setColor(color);
        /*
        int[] xCoordinates = new int[numOfSpikes * 2 + 3];
        int[] yCoordinates = new int[numOfSpikes * 2 + 3];
        xCoordinates[0] = (int) x;
        xCoordinates[1] = 1200;
        xCoordinates[2] = 1200;
        yCoordinates[0] = 676;
        yCoordinates[1] = 675;
        yCoordinates[2] = 0;
        for(int i = 0; i < numOfSpikes; i++) {
            xCoordinates[i * 2 + 3] = (int) x;
            yCoordinates[i * 2 + 3] = 675 / numOfSpikes * i;
            xCoordinates[i * 2 + 4] = (int) x - 50;
            yCoordinates[i * 2 + 4] = 675 / numOfSpikes * i + 675 / (numOfSpikes * 2);
        }
        g.fillPolygon(xCoordinates, yCoordinates, numOfSpikes * 2 + 3);
                */
        g.fillRect(0, 0, (int) width, (int) height);
    }
}
