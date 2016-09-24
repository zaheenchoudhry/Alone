import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

public class SettingsScreen extends GameObject {

    final Color color, backgroundDimColor;
    boolean confirm;

    public SettingsScreen(Game game) {
        super(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT, game);
        backgroundDimColor = new Color(0, 0, 0, 175);
        color = new Color(180, 180, 180);
    }

    public void setConfirmState(boolean confirm) {
        this.confirm = confirm;
    }
    
    @Override
    public void update() {
        if (game.getCursorXPos() > 400 && game.getCursorXPos() < 440 && game.getCursorYPos() < 230 && game.getCursorYPos() + 22 > 190) {
            game.setHoveredButtonType(70);
        } else if (game.getCursorXPos() > 400 && game.getCursorXPos() < 440 && game.getCursorYPos() < 310 && game.getCursorYPos() + 22 > 270) {
            game.setHoveredButtonType(71);
        } else if (game.getCursorXPos() > 400 && game.getCursorXPos() < 440 && game.getCursorYPos() < 390 && game.getCursorYPos() + 22 > 350) {
            game.setHoveredButtonType(72);
        } else if (game.getCursorXPos() > 400 && game.getCursorXPos() < 440 && game.getCursorYPos() < 470 && game.getCursorYPos() + 22 > 430) {
            game.setHoveredButtonType(73);
        } else if (game.getCursorXPos() > 470 && game.getCursorXPos() < 720 && game.getCursorYPos() < 550 && game.getCursorYPos() + 22 > 510) {
            if (new File("highscores").exists() && !confirm) {
                game.setHoveredButtonType(74);
            } else if (new File("highscores").exists()) {
                game.setHoveredButtonType(75);
            }
        } else {
            game.setHoveredButtonType(-1);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(backgroundDimColor);
        g.fillRect((int) 0,(int) 0,(int) width,(int) height);
        g.setColor(this.color);
        g.setFont(game.getFont().deriveFont(120f));
        g.drawString("S E T T I N G S", (int) (x + width / 2 - g.getFontMetrics(game.getFont().deriveFont(120f)).stringWidth("S E T T I N G S") / 2), 130);
        int yOffset;
        
        //Mute Setting
        yOffset = 190;
        g.setColor(color);
        g.fillRect(400, yOffset, 40, 40);
        g.setFont(game.getFont().deriveFont(30f));
        g.drawString("MUTE", 450, yOffset + 22);
        g.setFont(game.getFont().deriveFont(10f));
        g.drawString("DISABLES ALL MUSIC AND SOUND EFFECTS", 450, yOffset + 40);
        if (game.mute()) {
            g.setColor(new Color(20, 20, 20));
            g.fillPolygon(new int[]{420, 436, 442, 421, 400, 406}, new int[]{yOffset + 28, yOffset - 6, yOffset, yOffset + 40, yOffset + 20, yOffset + 14}, 6);
        }
        
        //Invert Controls Setting
        yOffset = 270;
        g.setColor(color);
        g.fillRect(400, yOffset, 40, 40);
        g.setFont(game.getFont().deriveFont(30f));
        g.drawString("INVERT CONTROLS", 450, yOffset + 22);
        g.setFont(game.getFont().deriveFont(10f));
        g.drawString("INVERT THE Y MOVEMENT OF CONTROLS DURING GAMEPLAY", 450, yOffset + 40);
        if (game.invertControls()) {
            g.setColor(new Color(20, 20, 20));
            g.fillPolygon(new int[]{420, 442, 448, 421, 400, 406}, new int[]{yOffset + 28, yOffset - 6, yOffset, yOffset + 40, yOffset + 20, yOffset + 14}, 6);
        }
        
        //Lag Setting
        yOffset = 350;
        g.setColor(color);
        g.fillRect(400, yOffset, 40, 40);
        g.setFont(game.getFont().deriveFont(30f));
        g.drawString("REDUCE EFFECTS", 450, yOffset + 22);
        g.setFont(game.getFont().deriveFont(10f));
        g.drawString("USE THIS SETTING IF YOU EXPERIENCE LAG DURING GAMEPLAY", 450, yOffset + 40);
        if (game.lag()) {
            g.setColor(new Color(20, 20, 20));
            g.fillPolygon(new int[]{420, 436, 442, 421, 400, 406}, new int[]{yOffset + 28, yOffset - 6, yOffset, yOffset + 40, yOffset + 20, yOffset + 14}, 6);
        }
        
        //Debug Setting
        yOffset = 430;
        g.setColor(color);
        g.fillRect(400, yOffset, 40, 40);
        g.setFont(game.getFont().deriveFont(30f));
        g.drawString("DEBUG MODE", 450, yOffset + 22);
        g.setFont(game.getFont().deriveFont(10f));
        g.drawString("YOU WILL NEVER DIE AND EXTRA INFORMATION IS SHOWN DURING GAMEPLAY", 450, yOffset + 40);
        if (game.debug()) {
            g.setColor(new Color(20, 20, 20));
            g.fillPolygon(new int[]{420, 442, 448, 421, 400, 406}, new int[]{yOffset + 28, yOffset - 6, yOffset, yOffset + 40, yOffset + 20, yOffset + 14}, 6);
        }
        
        
        
        //ResetHighscores
        yOffset = 510;
        if (new File("highscores").exists()) {
            g.setColor(new Color(180, 50, 50));
        } else {
            g.setColor(new Color(50, 50, 50));
        }

        g.fillRect((int) (width - g.getFontMetrics(game.getFont().deriveFont(30f)).stringWidth("ARE YOU SURE")) / 2 - 20, yOffset, (int) g.getFontMetrics(game.getFont().deriveFont(30f)).stringWidth("ARE YOU SURE") + 40, 50);
        g.setFont(game.getFont().deriveFont(30f));
        g.setColor(color);
        if (confirm) {
            g.drawString("ARE YOU SURE", (int) (width - g.getFontMetrics(game.getFont().deriveFont(30f)).stringWidth("ARE YOU SURE")) / 2, yOffset + 36);
        } else {
            g.drawString("RESET SCORES", (int) (width - g.getFontMetrics(game.getFont().deriveFont(30f)).stringWidth("RESET SCORES")) / 2, yOffset + 36);
        }
        
        g.setFont(game.getFont().deriveFont(10f));
        g.drawString("THIS WILL CLEAR THE SCORES, BUT NOT THE MEMORIES", (int) (width - g.getFontMetrics(game.getFont().deriveFont(10f)).stringWidth("THIS WILL CLEAR THE SCORES, BUT NOT THE MEMORIES")) / 2, yOffset + 68);
        g.drawString("(THIS IS PERMANENT)", (int) (width - g.getFontMetrics(game.getFont().deriveFont(10f)).stringWidth("(THIS IS PERMANENT)")) / 2, yOffset + 80);
        
        g.setColor(color);
        g.setFont(game.getFont().deriveFont(16f));
        g.drawString("GAME CREATED BY ADRIAN ENSAN, ZAHEEN CHOUDHRY & VARNAN SARANGIAN", (int) (width - g.getFontMetrics(game.getFont().deriveFont(16f)).stringWidth("GAME CREATED BY ADRIAN ENSAN, ZAHEEN CHOUDHRY & VARNAN SARANGIAN")) / 2, game.WINDOW_HEIGHT - 20);
    }
}
