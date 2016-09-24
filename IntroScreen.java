import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class IntroScreen extends GameObject {

    int backgroundTransparency, titleTransparency, transparency, counter;
    private int[] xPoints = new int[31], yPoints = new int[31];
    private int dHeight, dWidth, dX, dY;
    private double xUnit, yUnit;
    private int blinkHeight, blinkSign, blinkCounter, xSign = 6;
    private int y2, x2;

    public IntroScreen(Game game) {
        super(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT, game);

        dHeight = 260;
        dWidth = 240;
        dX = 480;
        dY = 200;
        xUnit = dWidth / 7;
        yUnit = dHeight / 8;
        
        xPoints[0] = (int)(dX + (xUnit * 2));
        xPoints[1] = (int)(dX + (xUnit * 3.2));
        xPoints[2] = (int)(dX + (xUnit * 3.2));
        xPoints[3] = (int)(dX + (xUnit * 4.5));
        xPoints[4] = (int)(dX + (xUnit * 5.7));
        xPoints[5] = (int)(dX + (xUnit * 5.7));
        xPoints[6] = (int)(dX + (xUnit * 5.7));
        xPoints[7] = (int)(dX + (xUnit * 6.8));
        xPoints[8] = (int)(dX + (xUnit * 6.75));
        xPoints[9] = (int)(dX + (xUnit * 6));
        xPoints[10] = (int)(dX + (xUnit * 5.2));
        xPoints[11] = (int)(dX + (xUnit * 5.3));
        xPoints[12] = (int)(dX + (xUnit * 4.9));
        xPoints[13] = (int)(dX + (xUnit * 4.7));
        xPoints[14] = (int)(dX + (xUnit * 3.8));
        xPoints[15] = (int)(dX + (xUnit * 3.75));
        xPoints[16] = (int)(dX + (xUnit * 3.4));
        xPoints[17] = (int)(dX + (xUnit * 3.45));
        xPoints[18] = (int)(dX + (xUnit * 2.5));
        xPoints[19] = (int)(dX + (xUnit * 2.4));
        xPoints[20] = (int)(dX + (xUnit * 2));
        xPoints[21] = (int)(dX + (xUnit * 2));
        xPoints[22] = (int)(dX + (xUnit * 1.2));
        xPoints[23] = (int)(dX + (xUnit * 1.1));
        xPoints[24] = (int)(dX + (xUnit * 0.75));
		xPoints[25] = (int)(dX + (xUnit * 0.6));
		xPoints[26] = (int)(dX);
        xPoints[27] = (int)(dX + (xUnit * 1.2));
        xPoints[28] = (int)(dX + (xUnit * 1.4));
        xPoints[29] = (int)(dX + (xUnit * 1.3));
        xPoints[30] = (int)(dX + (xUnit * 2));
        
        yPoints[0] = (int)(dY + (yUnit * 1.7));
        yPoints[1] = (int)(dY);
        yPoints[2] = (int)(dY + (yUnit * 1.5));
        yPoints[3] = (int)(dY + (yUnit * 1.4));
        yPoints[4] = (int)(dY);
        yPoints[5] = (int)(dY + (yUnit * 1.5));
        yPoints[6] = (int)(dY + (yUnit * 3));
        yPoints[7] = (int)(dY + (yUnit * 2.95));
        yPoints[8] = (int)(dY + (yUnit * 4));
        yPoints[9] = (int)(dY + (xUnit * 5.6));
        yPoints[10] = (int)(dY + (xUnit * 6));
        yPoints[11] = (int)(dY + (xUnit * 8));
        yPoints[12] = (int)(dY + (xUnit * 8));
        yPoints[13] = (int)(dY + (xUnit * 6.8));
        yPoints[14] = (int)(dY + (xUnit * 7));
        yPoints[15] = (int)(dY + (xUnit * 7.8));
        yPoints[16] = (int)(dY + (xUnit * 7.8));
        yPoints[17] = (int)(dY + (xUnit * 7));
        yPoints[18] = (int)(dY + (xUnit * 6.7));
        yPoints[19] = (int)(dY + (xUnit * 8));
        yPoints[20] = (int)(dY + (xUnit * 8));
        yPoints[21] = (int)(dY + (xUnit * 6.6));
        yPoints[22] = (int)(dY + (xUnit * 6.75));
        yPoints[23] = (int)(dY + (xUnit * 7.8));
        yPoints[24] = (int)(dY + (xUnit * 7.8));
        yPoints[25] = (int)(dY + (xUnit * 6.35));
        yPoints[26] = (int)(dY + (xUnit * 4));
        yPoints[27] = (int)(dY + (yUnit * 6.1));
        yPoints[28] = (int)(dY + (yUnit * 5.9));
        yPoints[29] = (int)(dY + (yUnit * 5.1));
        yPoints[30] = (int)(dY + (yUnit * 3.5));
        
    }
    
    private void blink() {
    	if(blinkHeight >= 35) {
        	blinkSign = -8;
        } else if (blinkHeight <= 0) {
        	blinkSign = 8;
        	blinkCounter++;
        }
        blinkHeight += blinkSign;
    }
    
    private void resetBlink() {
    	blinkHeight = 1;
    	blinkSign = 8;
    	blinkCounter = 0;
    }

    @Override
    public void update() {
    	
        if (counter < 40) {
        	resetBlink();
        } else if (counter < 50) {
			blink();
        } else if (counter < 65) {
        	resetBlink();
        } else if (counter < 75) {
            blink();
        } else if (counter < 130) {
        }
        
        if (game.animating()) {
            counter++;
            if (counter == 130) {
            	try {
                    game.loopAudioClip(0);
                } catch (Exception ex) {
                }
                Main.mainMenu();
            }
        }

        
        if(x2 >= 40) {
        	xSign = -6;
        } else if (x2 <= -30) {
        	xSign = 6;
        }
        x2 += xSign;
        
        y2 = (int) Math.sqrt(Math.pow(25, 2) - Math.pow(x2, 2)) * -1;
        
        yPoints[26] = (int)(dY + (xUnit * 4) + y2);
        xPoints[26] = (int)(dX + x2);
    }

    @Override
    public void paint(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g.create();
        g.setColor(new Color(250, 250, 250));
        g.fillRect((int) x,(int) y,(int) width,(int) height);

        g2d.setColor(new Color(0, 0, 0));
        g2d.fillPolygon(xPoints, yPoints, 31);
        
        g2d.setColor(new Color(250, 250, 250));
        g2d.fillPolygon(new int[]{(int)(dX + (xUnit * 3)), (int)(dX + (xUnit * 6.2)), (int)(dX + (xUnit * 6.2)), (int)(dX + (xUnit * 3))}, new int[]{(int)(dY + (yUnit * 3.9)), (int)(dY + (yUnit * 3.5)), (int)(dY + (yUnit * 3.6)), (int)(dY + (yUnit * 4))}, 4);
        
        g2d.rotate(Math.toRadians(-5), (int)(dX + (xUnit * 3)) + (14 / 2), (int)(dY + (yUnit * 2.3)) + (25 / 2));
        g2d.fillOval((int)(dX + (xUnit * 3)), (int)(dY + (yUnit * 2.3)), 14, 25);
        g2d.fillOval((int)(dX + (xUnit * 4.5)), (int)(dY + (yUnit * 2.5)), 10, 18);
        
        g2d.setColor(new Color(0, 0, 0));
        g2d.fillPolygon(new int[]{(int)(dX + (xUnit * 2.7)), (int)(dX + (xUnit * 3.7)), (int)(dX + (xUnit * 3.7)), (int)(dX + (xUnit * 2.7))}, new int[]{(int)(dY + (yUnit * 2.1)), (int)(dY + (yUnit * 2.1)), (int)(dY + (yUnit * 2.1) + blinkHeight), (int)(dY + (yUnit * 2.1) + blinkHeight)}, 4);
        g2d.fillPolygon(new int[]{(int)(dX + (xUnit * 4.3)), (int)(dX + (xUnit * 5.2)), (int)(dX + (xUnit * 5.2)), (int)(dX + (xUnit * 4.3))}, new int[]{(int)(dY + (yUnit * 2.3)), (int)(dY + (yUnit * 2.3)), (int)(dY + (yUnit * 2.3) + blinkHeight), (int)(dY + (yUnit * 2.3) + blinkHeight)}, 4);
        
        g.setColor(new Color(0, 0, 0));
        g.setFont(game.getFont().deriveFont(80f));
        g.drawString("WOOF", (int) (width - g.getFontMetrics(game.getFont().deriveFont(80f)).stringWidth("WOOF")) / 2, 570);
    }
}
