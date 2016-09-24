import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JComponent;


public class ContentPane extends JComponent{
    
    private ArrayList<GameObject> gameObjects;
    private Game game;
    
    public ContentPane(ArrayList<GameObject> gameObjects, Game game){
        this.gameObjects = gameObjects;
        this.game = game;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(game.getBackgroundColor());
        super.paintComponent(g);
        g.fillRect(0, 0, 1200, 675);
        
        for (int i = 0; i < gameObjects.size(); i++)  {
            gameObjects.get(i).paint(g);
        }
    }
    
}