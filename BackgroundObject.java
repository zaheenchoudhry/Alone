import java.awt.Graphics;

public abstract class BackgroundObject extends GameObject{
    
    public BackgroundObject(double x, double y, double width, double height, Game game){
        super(x, y, width, height, game);
    }
    
    @Override
    public abstract void update();
    
    @Override
    public abstract void paint(Graphics g);
}