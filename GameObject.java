import java.awt.Graphics;

public abstract class GameObject{
    
    protected double x, y, height, width;
    public Game game;
    
    public GameObject(double x, double y, double width, double height, Game game){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.game = game;
    }
    
    public abstract void update();
    
    public abstract void paint(Graphics g);
}