import java.awt.Graphics;

public abstract class Obstacle extends GameObject{
    
    boolean hit, playerHit;
    
    public Obstacle(double x, double y, double width, double height, Game game){
        super(x, y, width, height, game);
    }
    
    public void hit() {
        this.hit = true;
    }
    
    public void playerHit() {
        this.playerHit = true;
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