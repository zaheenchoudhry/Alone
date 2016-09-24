import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class DeathScreen extends BackgroundObject{
    
    int transparency, cursorBlink = 0, highscoreIndex;
    boolean nameEntered;
    String currentName = "";
    
    public DeathScreen(Game game) {
        super(game.WINDOW_WIDTH / 2, 0, 0, 0, game);
        transparency = 255;
        nameEntered = false;
    }
    
    public void nameEntered() {
        this.nameEntered = true;
    }
    
    public String getName() {
        return this.currentName;
    }
    
    public void editName(String name) {
        this.currentName = name;
    }
    
    public void setHighscoreIndex(int index) {
        this.highscoreIndex = index;
    }
    
    public int getHighscoreIndex() {
        return this.highscoreIndex;
    }
    
    @Override
    public void update() {
        if (game.getCurrentScreen() == game.DEATH_SCREEN) {
            if (transparency < 255) {
                transparency += 10;
                if (transparency == 255) {
                    game.setAnimating(false);
                }
            }
        } else if (game.getCurrentScreen() == game.ENTER_NIGH_SCORE_NAME) {
            if (transparency == 5) {
                game.setCurrentScreen(game.DEATH_SCREEN);
            } else if (this.nameEntered) {
                transparency -= 10;
                game.setAnimating(true);
            }
            cursorBlink++;
            if (cursorBlink > 150) {
                cursorBlink -= 150;
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
        if (game.deathScreen()) {
            g.setFont(game.getFont().deriveFont(150f));
            String currentScore = game.getDistance() + "M";
            for (int i = 0; i < currentScore.length(); i++) {
                if (i + 1 == currentScore.length()) {
                    g.setColor(new Color(200, 150, 50));
                } else {
                    g.setColor(new Color(200, 200, 200));
                }
                g.drawString(currentScore.substring(i, i + 1), (int) x - ((currentScore.length() % 2 > 0) ? 50 : 0) - 100 * (currentScore.length() / 2 - i), 200);
            }
            
            if (game.getCurrentScreen() == game.DEATH_SCREEN) {
                g.setFont(game.getFont().deriveFont(26f));
                g.drawString("TOP SCORES FOR " + game.getGameMode() + " MODE", (int) x - g.getFontMetrics(game.getFont().deriveFont(26f)).stringWidth("TOP SCORES FOR " + game.getGameMode() + " MODE") / 2, 310);
                g.setFont(game.getFont().deriveFont(24f));
                for (int i = 0; i < 5; i++) {
                    g.setColor(new Color(150 - 25 * i, 150 - 25 * i, 150 - 25 * i, this.transparency));
                    if (i != 0) {
                        g.drawLine(350, 330 + 40 * i, 850, 330 + 40 * i);
                    }
                    String name;
                    String score;
                    if (game.getHighscores()[game.getGameMode().ordinal()].size() > i) {
                        name = (String) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(i))[0];
                        score = (Integer) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(i))[1] + "M";
                        if (i== this.highscoreIndex) {
                            g.setColor(new Color(150, 150, 220, this.transparency));
                        }
                    } else {
                        name = "-  -  -";
                        score = "---M";
                    }
                    g.drawString((i + 1) + ".", 360, 358 + 40 * i);
                    g.drawString(name, 400, 358 + 40 * i);
                    for (int x = 0; x < score.length(); x++) {
                        if (x + 1 == score.length()) {
                            g.setColor(new Color(200 - 30 * i, 150 - 20 * i, 50 - 10 * i, this.transparency));
                        }
                        g.drawString(score.substring(x, x + 1), 850 - 20 * (score.length() - x), 358 + 40 * i);
                    }
                }

                //g.drawString("YOU ARE DEAD", (int) x - 350, 300);
                g.setFont(game.getFont().deriveFont(20f));
                g.setColor(new Color(200, 200, 200, this.transparency));
                g.drawString("CLICK ANYWHERE TO TRY AGAIN", (int) x - g.getFontMetrics(game.getFont().deriveFont(20f)).stringWidth("CLICK ANYWHERE TO TRY AGAIN") / 2, 625);
            } else {
                g.setColor(new Color(200, 150, 50, this.transparency));
                g.setFont(game.getFont().deriveFont(50f));
                g.drawString("NEW HIGH SCORE!", (int) x - g.getFontMetrics(game.getFont().deriveFont(50f)).stringWidth("NEW HIGH SCORE!") / 2, 350);
                if (currentName.equals("")) {
                    g.setColor(new Color(50, 50, 50, this.transparency));
                    g.setFont(game.getFont().deriveFont(20f));
                    g.drawString("PLEASE ENTER YOUR NAME", (int) x - g.getFontMetrics(game.getFont().deriveFont(20f)).stringWidth("PLEASE ENTER YOUR NAME") / 2, 500);
                }
                g.setFont(game.getFont().deriveFont(24f));
                g.setColor(new Color(200, 200, 200, this.transparency));
                g.drawString(currentName, 600 - g.getFontMetrics(game.getFont().deriveFont(24f)).stringWidth(currentName) / 2, 500);
                if ((cursorBlink / 15) % 2 > 0) {
                    g.drawString("|", 600 + g.getFontMetrics(game.getFont().deriveFont(24f)).stringWidth(currentName) / 2, 500);
                }
                g.drawLine(425, 510, 775, 510);
            }
        }
    }
}
