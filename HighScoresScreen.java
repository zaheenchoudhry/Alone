import java.awt.Color;
import java.awt.Graphics;

public class HighScoresScreen extends GameObject {

    int backgroundTransparency, titleTransparency, transparency, counter, transitionTo;
    int[] fillTransparency = new int[2];
    GameModes[] otherGameModes = new GameModes[2];
    Color textColor, descriptionTextColor;

    public HighScoresScreen(Game game) {
        super(0, 0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT, game);
        backgroundTransparency = 175;
        textColor = new Color(230, 230, 230);
        descriptionTextColor = new Color(218, 165, 32);
        counter = 0;
        fillTransparency[0] = 0;
        fillTransparency[1] = 0;
        if (game.getGameMode().ordinal() - 1 < 0) {
            otherGameModes[0] = GameModes.values()[GameModes.values().length - 1];
        } else {
            otherGameModes[0] = GameModes.values()[game.getGameMode().ordinal() - 1];
        }
        if (game.getGameMode().ordinal() + 1 > GameModes.values().length - 1) {
            otherGameModes[1] = GameModes.values()[0];
        } else {
            otherGameModes[1] = GameModes.values()[game.getGameMode().ordinal() + 1];
        }
    }

    @Override
    public void update() {
        if (game.animating()) {
            if (counter == 0) {
                if (game.getHoveredButtonType() == 4) {
                    transitionTo = 1;
                    game.setHoveredButtonType(-1);
                } else if (game.getHoveredButtonType() == 5) {
                    transitionTo = -1;
                    game.setHoveredButtonType(-1);
                }
            }
            if (counter < 20) {
                if (transitionTo == 1) {
                    game.gameModeSelectionTransision(otherGameModes[0]);
                } else if (transitionTo == -1) {
                    game.gameModeSelectionTransision(otherGameModes[1]);
                }
                x += game.WINDOW_WIDTH / 20 * transitionTo;
                counter++;
            } else {
                if (transitionTo == 1) {
                    game.setGameMode(otherGameModes[0]);
                } else if (transitionTo == -1) {
                    game.setGameMode(otherGameModes[1]);
                }
                if (game.getGameMode().ordinal() - 1 < 0) {
                    otherGameModes[0] = GameModes.values()[GameModes.values().length - 1];
                } else {
                    otherGameModes[0] = GameModes.values()[game.getGameMode().ordinal() - 1];
                }
                if (game.getGameMode().ordinal() + 1 > GameModes.values().length - 1) {
                    otherGameModes[1] = GameModes.values()[0];
                } else {
                    otherGameModes[1] = GameModes.values()[game.getGameMode().ordinal() + 1];
                }
                x = 0;
                counter = 0;
                game.setAnimating(false);
            }
            if (fillTransparency[0] != 0) {
                fillTransparency[0] -= 10;
            } else if (fillTransparency[1] != 0) {
                fillTransparency[1] -= 10;
            }
        } else {
            if (game.getCursorXPos() < 200) {
                if (fillTransparency[0] != 100) {
                    fillTransparency[0] += 10;
                    //colorChange += 12;
                }
                game.setHoveredButtonType(4);
            } else if (fillTransparency[0] != 0) {
                fillTransparency[0] -= 10;
                //colorChange -= 12;
                game.setHoveredButtonType(-1);
            }
            if (game.getCursorXPos() > width - 200) {
                if (fillTransparency[1] != 100) {
                    fillTransparency[1] += 10;
                }
                game.setHoveredButtonType(5);
            } else if (fillTransparency[1] != 0) {
                fillTransparency[1] -= 10;
                game.setHoveredButtonType(-1);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0, 0, 0, backgroundTransparency));
        g.fillRect((int) 0,(int) 0,(int) width,(int) height);
        g.setColor(this.textColor);
        g.setFont(game.getFont().deriveFont(120f));
        g.drawString(game.getGameMode() + "", (int) (x + width / 2 - g.getFontMetrics(game.getFont().deriveFont(120f)).stringWidth(game.getGameMode() + "") / 2), 150);
        g.drawString(this.otherGameModes[0] + "", (int) (x + width / 2 - g.getFontMetrics(game.getFont().deriveFont(120f)).stringWidth(this.otherGameModes[0] + "") / 2) - game.WINDOW_WIDTH, 150);
        g.drawString(this.otherGameModes[1] + "", (int) (x + width / 2 - g.getFontMetrics(game.getFont().deriveFont(120f)).stringWidth(this.otherGameModes[1] + "") / 2) + game.WINDOW_WIDTH, 150);
       
        for (int i = 0; i < 2; i++) {
            g.setColor(new Color(0, 0, 0, this.fillTransparency[i]));
            g.fillRect((int) width * i - 200 * i, 0, 200, (int) height);
            g.setColor(new Color(230, 230, 230, this.fillTransparency[i] * 2));
            g.setFont(game.getFont().deriveFont(26f));
            g.drawString(otherGameModes[i] + "", (int) width * i + 100 * ((i == 0) ? 1 : -1) - (g.getFontMetrics(game.getFont().deriveFont(26f)).stringWidth(otherGameModes[i] + "")) / 2, (int) height - 50);
            g.setColor(textColor);
            g.fillPolygon(new int[]{(int) width * i - 75 * ((i == 0) ? -1 : 1), (int) width * i - 120 * ((i == 0) ? -1 : 1), (int) width * i - 125 * ((i == 0) ? -1 : 1), (int) width * i - 80 * ((i == 0) ? -1 : 1), (int) width * i - 125 * ((i == 0) ? -1 : 1), (int) width * i - 120 * ((i == 0) ? -1 : 1)}, new int[]{(int) height / 2, (int) height / 2 - 60, (int) height / 2 - 60, (int) height / 2, (int) height / 2 + 60, (int) height / 2 + 60}, 6);
            g.setFont(game.getFont().deriveFont(24f));
        }
        for (int y = 0; y < 3; y++) {
            if (y == 0) {
                for (int i = 0; i < 5; i++) {
                    g.setColor(new Color(250 - 25 * i, 250 - 25 * i, 250 - 25 * i));
                    if (i != 0) {
                        g.drawLine((int) x + 350, 330 + 40 * i, (int) x + 850, 330 + 40 * i);
                    }
                    String name;
                    String score;
                    if (game.getHighscores()[game.getGameMode().ordinal()].size() > i) {
                        name = (String) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(i))[0];
                        score = (Integer) ((Object[]) game.getHighscores()[game.getGameMode().ordinal()].get(i))[1] + "M";
                    } else {
                        name = "-  -  -";
                        score = "---M";
                    }
                    g.drawString((i + 1) + ".", (int) x + 360, 358 + 40 * i);
                    g.drawString(name, (int) x + 400, 358 + 40 * i);
                    for (int x = 0; x < score.length(); x++) {
                        if (x + 1 == score.length()) {
                            g.setColor(new Color(200 - 30 * i, 150 - 20 * i, 50 - 10 * i));
                        }
                        g.drawString(score.substring(x, x + 1), (int) this.x + 850 - 20 * (score.length() - x), 358 + 40 * i);
                    }
                }
            } else {
                for (int i = 0; i < 5; i++) {
                g.setColor(new Color(250 - 25 * i, 250 - 25 * i, 250 - 25 * i));
                if (i != 0) {
                    g.drawLine((int) x + 350 + 1200 * ((y == 1) ? -1 : 1), 330 + 40 * i, (int) x + 850 + 1200 * ((y == 1) ? -1 : 1), 330 + 40 * i);
                }
                String name;
                String score;
                if (otherGameModes[y - 1].ordinal() != 6 && game.getHighscores()[otherGameModes[y - 1].ordinal()].size() > i) {
                    name = (String) ((Object[]) game.getHighscores()[otherGameModes[y - 1].ordinal()].get(i))[0];
                    score = (Integer) ((Object[]) game.getHighscores()[otherGameModes[y - 1].ordinal()].get(i))[1] + "M";
                } else {
                    name = "-  -  -";
                    score = "---M";
                }
                g.drawString((i + 1) + ".", (int) x + 360 + 1200 * ((y == 1) ? -1 : 1), 358 + 40 * i);
                g.drawString(name, (int) x + 400 + 1200 * ((y == 1) ? -1 : 1), 358 + 40 * i);
                for (int x = 0; x < score.length(); x++) {
                    if (x + 1 == score.length()) {
                        g.setColor(new Color(200 - 30 * i, 150 - 20 * i, 50 - 10 * i));
                    }
                    g.drawString(score.substring(x, x + 1), (int) this.x + 850 - 20 * (score.length() - x) + 1200 * ((y == 1) ? -1 : 1), 358 + 40 * i);
                }
            }
            }
        }
    }
}
