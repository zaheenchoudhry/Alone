import java.awt.Color;
import java.awt.Graphics;

public class Distance extends BackgroundObject{
    
    int transparency = 150, overlayTransparency, countdown;
    boolean overlay;
    String currentDistance = "0M", overlayText = "1000M";
    Color currentDistanceColor, overlayColor;
    
    public Distance(Game game)  {
        super(game.WINDOW_WIDTH - 20, 38, 200, 100, game);
        currentDistanceColor = new Color(255, 255, 255, this.transparency);
    }
    
    @Override
    public void update() {
    	if ((int) (game.getDistance() / (game.BREAK_DISTANCE / 10)) != (int) ((game.getDistance() - game.getSpeed(1 / 50) - 1) / (game.BREAK_DISTANCE / 10))) {
            this.transparency = 255;
        } else if (transparency > 245) {
            this.transparency -= 1;
        } else if (transparency > 150) {
            this.transparency -= 10;
        }
        if ((int) (game.getDistance() / game.BREAK_DISTANCE) != (int) ((game.getDistance() - game.getSpeed(1 / 50) - 1) / game.BREAK_DISTANCE)) {
            overlay = true;
            overlayText = ((int) (game.getDistance() / game.BREAK_DISTANCE) * game.BREAK_DISTANCE) + "M";
            countdown = 140;
            this.overlayTransparency = 255;
            try {
                game.playSoundEffect(game.THOUSAND_M);
            } catch (Exception ex) {
            }
        }
        
        if (overlay) {
            if (countdown == 0) {
                overlay = false;
            } else if (countdown < 60) {
                if (countdown / 2 % 2 == 0) {
                    this.overlayTransparency = 0;
                } else {
                    this.overlayTransparency = 25 + 35 * (countdown) / 10;
                }
            }
            countdown--;
            this.overlayColor = new Color(240, 240, 240, this.overlayTransparency);
        }
        if (game.getHighscores()[game.getGameMode().ordinal()].size() > 0 && game.getDistance() > (int) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(0))[1] && (int) (game.getDistance() - game.getSpeed(1 / 50) - 1) <= (int) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(0))[1]) {
            overlay = true;
            overlayText = "NEW HIGH SCORE";
            countdown = 140;
            this.overlayTransparency = 255;
            try {
                game.playSoundEffect(game.HIGH_SCORE);
            } catch (Exception ex) {
            }
        }
        currentDistance = (game.getDistance() + "M");
        currentDistanceColor = new Color(255, 255, 255, this.transparency);
    }
    
    @Override
    public void paint(Graphics g) {
        
        g.setFont(game.getFont().deriveFont(35f));
        g.setColor(this.currentDistanceColor);
        for (int i = 0; i < currentDistance.length(); i++) {
            g.drawString(currentDistance.substring(i, i + 1), (int) x - 24 * (currentDistance.length() - i), (int) y);
        }
        if (game.getHighscores()[game.getGameMode().ordinal()].size() > 0 && game.getDistance() < (int) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(0))[1]) {
            String distanceToHighscore = (game.getDistance() - (int) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(0))[1]) + "M";
            g.setFont(game.getFont().deriveFont(15f));
            g.setColor(new Color(255, 255, 255, 150));
            for (int i = 0; i < distanceToHighscore.length(); i++) {
                g.drawString(distanceToHighscore.substring(i, i + 1), (int) x - 10 * (distanceToHighscore.length() - i), (int) y + 20);
            }
        }
        if (overlay) {
            if (overlayText.equals("NEW HIGH SCORE")) {
                g.setFont(game.getFont().deriveFont(100f));
                g.setColor(this.overlayColor);
                g.drawString(overlayText, (int) (game.WINDOW_WIDTH - g.getFontMetrics(game.getFont().deriveFont(100f)).stringWidth(overlayText)) / 2, game.WINDOW_HEIGHT / 2 + 40);
            } else {
                g.setFont(game.getFont().deriveFont(150f));
                g.setColor(this.overlayColor);
                for (int i = 0; i < overlayText.length(); i++) {
                    g.drawString(overlayText.substring(i, i + 1), (int) game.WINDOW_WIDTH / 2 - ((overlayText.length() % 2 > 0) ? 50 : 0) - 100 * (overlayText.length() / 2 - i), game.WINDOW_HEIGHT / 2 + 50);
                }
            }
        }
    }
}
