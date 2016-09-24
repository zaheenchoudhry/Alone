import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Player extends CollisionObject{

    private int idleAngle;
    private final int NUM_OF_SMOKE_PARTICLES = 14;
    private double rotation = 0, smokeRotation = 360, blankSmokeParticle = 0;
    private double[]smokeYValues = new double[NUM_OF_SMOKE_PARTICLES];
    private ArrayList<int[]> blackSmoke = new ArrayList<>();

    public Player(double x, double y, double width, double height, Game game) {
        super(x, y, width, height, game);
        idleAngle = 0;
        for (int i = 0; i < NUM_OF_SMOKE_PARTICLES; i++) {
            smokeYValues[i] = -100;
        }
    }
    
    @Override
    public double getX() {
        return x;
    }
    
    @Override
    public double getY() {
        return y;
    }
    
    @Override
    public void update() {
        if (game.isDead()) {
            x -= game.getSpeed(1);
            game.setPlayerXPos(this.x);
        } else {
            if (!game.debug()) {
                Main.checkCollision(this);
            }
            if (x != 300) {
                x += (300 - x) / 45;
                if (300 - x < 0.9) {
                    x = 300;
                }
                game.setPlayerXPos(this.x);
            }
            if (idleAngle > 360) {
                idleAngle -= 360;
            }
            idleAngle += 3;
            double yMove;
            if (game.invertControls()) {
                yMove = ((((game.WINDOW_HEIGHT / 2) - game.getYMousePos()) * 2.5 + (game.WINDOW_HEIGHT / 2) + (Math.sin(Math.toRadians(idleAngle)) * 15) - (height / 2))) - this.y;
            } else {
                yMove = (((game.getYMousePos() - (game.WINDOW_HEIGHT / 2)) * 2.5 + (game.WINDOW_HEIGHT / 2) + (Math.sin(Math.toRadians(idleAngle)) * 15) - (height / 2))) - this.y;
            }

            rotation = yMove / 2;

            this.y += yMove / 8;
            
            
            if (Math.abs(rotation) > 60) {
                rotation = rotation / Math.abs(rotation) * 60;
            }
            if (smokeRotation < 0) {
                smokeRotation += 360;
            } else {
                smokeRotation -= 40;//10 + Math.random() * 10;
            }
            if (blankSmokeParticle < NUM_OF_SMOKE_PARTICLES) {
                blankSmokeParticle += 0.5;
            } else {
                blankSmokeParticle = 0;
            }
            for (int i = NUM_OF_SMOKE_PARTICLES - 1; i > 0; i--) {
                smokeYValues[i] = smokeYValues[i - 1];
            }
            
            game.setPlayerYPos(this.y);
            game.setPlayerRotation(rotation);
            if (!game.isDamaged()) {
                smokeYValues[0] = this.y - yMove / 8 + height / 2 + Math.sin(Math.toRadians(rotation)) * - (10 + width / 2);
            } else {
                if (Math.random() * 10 > 7) {
                    int x = (int) this.x - 40;
                    int y = (int) (this.y - yMove / 8 + height / 2 + Math.sin(Math.toRadians(rotation)) * - (10 + width / 2) + Math.random() * 25 * ((Math.random() * 10 >= 5) ? 1 : -1));
                    int size = (int) (Math.random() * 20 + 10);
                    blackSmoke.add(new int[]{x, y, size});
                }
                smokeYValues[0] = -100;
            }
            for (int i = 0; i < this.blackSmoke.size(); i++) {
                this.blackSmoke.get(i)[0] -= game.getSpeed(1);
                if (this.blackSmoke.get(i)[0] + this.blackSmoke.get(i)[2] < 0) {
                    this.blackSmoke.remove(i);
                    i--;
                }
            }
        }
        
    }
    
    @Override
    public void paint(Graphics g) {
        if (!game.isDead()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(Math.toRadians(rotation), x + width / 2, y + height / 2);
            g2d.setColor(new Color(225, 225, 225, 255 - (int) ((game.isDamaged()) ? Math.random() * 230 : 0)));
            g2d.fillPolygon(new int[]{(int) x, (int) (x + width / 3.5), (int) (x + width), (int) (x + width), (int) (x + width / 3.5), (int) x}, new int[]{(int) (y + height / 6), (int) y, (int) (y + height / 4), (int) (y + height - height / 4), (int) (y + height), (int) (y + height - height / 6)}, 6);
            g2d.setColor(new Color(225, 225, 225, ((!game.isDamaged()) ? 100 + (int) (Math.random() * 150) : 0 + (int) (Math.random() * 120)) ));
            g2d.drawPolygon(new int[]{(int) x - 2, (int) (x + width / 3.5), (int) (x + width) + 2, (int) (x + width) + 2, (int) (x + width / 3.5), (int) x - 2}, new int[]{(int) (y + height / 6) - 1, (int) y - 2, (int) (y + height / 4) - 1, (int) (y + height - height / 4) + 1, (int) (y + height) + 2, (int) (y + height - height / 6) + 1}, 6);
            g2d.drawPolygon(new int[]{(int) x - 3, (int) (x + width / 3.5), (int) (x + width) + 3, (int) (x + width) + 3, (int) (x + width / 3.5), (int) x - 2}, new int[]{(int) (y + height / 6) - 2, (int) y - 3, (int) (y + height / 4) - 2, (int) (y + height - height / 4) + 2, (int) (y + height) + 3, (int) (y + height - height / 6) + 2}, 6);
            g2d.setColor(new Color(50, 50, 50));
            g2d.drawPolygon(new int[]{(int) x, (int) (x + width / 3.5), (int) (x + width), (int) (x + width), (int) (x + width / 3.5), (int) x}, new int[]{(int) (y + height / 6), (int) y, (int) (y + height / 4), (int) (y + height - height / 4), (int) (y + height), (int) (y + height - height / 6)}, 6);
            
            g2d.drawPolygon(new int[]{(int) (x + width / 3.5), (int) (x + width / 3.5)}, new int[]{(int) y, (int) (y + height)}, 2);
            g2d.fillOval((int) (x + width / 1.7), (int) y + 8, 6, 6);
            
            int smokeXStart = (int) x;
            int smokeXSpacing = (330) / NUM_OF_SMOKE_PARTICLES;
            for (int i = 0; i < NUM_OF_SMOKE_PARTICLES; i++) {
                if (i != (int) blankSmokeParticle && i != 0 && i != 1) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.rotate(Math.toRadians(smokeRotation + 10 * i), smokeXStart - smokeXSpacing * i, smokeYValues[i]);
                    int shift = (i - 7 < 0) ? 0 : 1;
                    int smokeSize = (int) (1 / ((Math.pow((i - 7 + shift), 2) + Math.pow(10 / 3, 2)) / 80)) + 3;
                    g2.setColor(new Color(200, 200, 200, 230 - 15 * Math.abs((i - 7 + shift) + (i - 7 + shift))));
                    g2.fillPolygon(new int[]{smokeXStart - smokeXSpacing * i - smokeSize, smokeXStart - smokeXSpacing * i + smokeSize, smokeXStart - smokeXSpacing * i + smokeSize, smokeXStart - smokeXSpacing * i - smokeSize}, new int[]{(int) (smokeYValues[i] - smokeSize), (int) (smokeYValues[i] - smokeSize), (int) (smokeYValues[i] + smokeSize), (int) (smokeYValues[i] + smokeSize)}, 4);
                }
            }
            for (int[] smokeParticle : this.blackSmoke) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.rotate(Math.toRadians(Math.random() * 360), smokeParticle[0] + smokeParticle[2] / 2, smokeParticle[1] + smokeParticle[2] / 2);
                g2.setColor(new Color(10, 10, 10, (int) (180 - Math.random() * 50)));
                g2.fillRect(smokeParticle[0], smokeParticle[1], smokeParticle[2], smokeParticle[2]);
            }
            g2d.setColor(new Color(240, 80, 10));
            g2d.fillPolygon(new int[]{(int) x - 6, (int) x - 6, (int) (x - (Math.random() * 50 + 10))}, new int[]{(int) y + 6, (int) y + (int) height - 6, (int) (y + height / 2)},  3);
        }
    }

    @Override
    public double getEndX() {
        return x + width;
    }

    @Override
    public double getEndY() {
        return y + height;
    }
}
