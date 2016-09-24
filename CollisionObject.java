import java.awt.Graphics;

public abstract class CollisionObject extends GameObject{
    
    public CollisionObject(double x, double y, double width, double height, Game game){
        super(x, y, width, height, game);
    }

    public abstract double getX();
    
    public abstract double getEndX();
    
    public abstract double getY();
    
    public abstract double getEndY();
    
    @Override
    public abstract void update();
    
    @Override
    public abstract void paint(Graphics g);
}