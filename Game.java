import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Game {

    //Sound Effects
    final int MISSLE_WARNING = 0;
    final int DEATH = 1;
    final int ENABLE_SETTING = 2;
    final int DISABLE_SETTING = 3;
    final int OPEN_SCREEN = 4;
    final int CLOSE_SCREEN = 5;
    final int MISSILE_SWOOSH = 6;
    final int GAMEMODE_SELECT = 7;
    final int ROCK_EXPLODE = 8;
    final int DAMAGED = 9;
    final int THOUSAND_M = 10;
    final int HIGH_SCORE = 11;
    final int ENGINE_REGULAR = 12;
    final int ENGINE_DAMAGED = 13;
    final int WOOF = 14;
    final int RECHARGE_COMPLETE = 15;
    
    final int WINDOW_WIDTH = 1200;
    final int WINDOW_HEIGHT = 675;
    final int NUM_OF_STARS = 45;
    final int NUM_OF_LAZERS = 10;
    final int NUM_OF_BEAMS = 2;
    final int NUM_OF_MINIROCKS = 20;
    final int ANIMATION_DELAY = 14;
    final int BREAK_DISTANCE = 1000;
    final int MAIN_MENU = 0;
    final int GAME_MODE_SELECT_SCREEN = 1;
    final int GAMEPLAY = 2;
    final int PAUSED_SCREEN = 3;
    final int ENTER_NIGH_SCORE_NAME = 4;
    final int DEATH_SCREEN = 5;
    final int HIGH_SCORES_SCREEN = 6;
    final int SETTINGS_SCREEN = 7;
    final int ERROR_SCREEN = 8;
    final int INTRO_SCREEN = 9;
    private int currentScreen, hoveredButtonType;
    private double speed, distance, fadePercentage, playerXPos, playerYPos, cursorXPos, cursorYPos,yMousePos = WINDOW_HEIGHT / 2 - 7.6, xMousePos = WINDOW_WIDTH / 2 + 75, realYMousePos = WINDOW_HEIGHT / 2, playerRotation = 0;
    private boolean dead, animationOccuring, showDeathScreen, lightningFlash = false, lag, debug, damaged, mute, laserAvailable, laserShot, invertControls;
    private Font font;
    private ArrayList[] highscores = new ArrayList[GameModes.values().length];
    private GameModes gameMode;
    private ColorSchemes colorScheme = ColorSchemes.BLACK, nextColorScheme;
    private double[] backgroundFadeAmount = new double[3];
    private double[][] spikesFadeAmount = new double[3][3];
    private Clip[] gameMusic = new Clip[2], soundEffects = new Clip[16];

    public Game() throws Exception {
        this.dead = false;
        this.animationOccuring = false;
    }
    
    public void init() {
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf"));
        } catch (FontFormatException | IOException e) {
            this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 150);
            Main.errorScreen("ERROR LOADING FONT", "THE FONT.TTF FILE COULD NOT BE FOUND, PLEASE ENSURE IT EXISSTS IN THE ROOT GAME FOLDER");
        }
        for (int i = 0; i < highscores.length; i++) {
            highscores[i] = new ArrayList<>();
        }
        try {
            gameMusic[0] = AudioSystem.getClip();
            gameMusic[0].open(AudioSystem.getAudioInputStream(new File("audio/music/MAIN_MENU.wav")));
        } catch (Exception ex) {
            Main.errorScreen("ERROR LOADING MUSIC", "PLEASE ENSURE 2 WAV FILES EXISTS IN THE AUDIO/MUSIC/ FOLDER");
        }
        for (int i = 1; i < gameMusic.length; i++) {
            try {
                gameMusic[i] = AudioSystem.getClip();
                gameMusic[i].open(AudioSystem.getAudioInputStream(new File("audio/music/" + GameModes.values()[i] + ".wav")));
            } catch (Exception ex) {
                Main.errorScreen("ERROR LOADING MUSIC", "PLEASE ENSURE 2 WAV FILES EXISTS IN THE AUDIO/MUSIC/ FOLDER");
            }
        }
        for (int i = 0; i < soundEffects.length; i++) {
            try {
                soundEffects[i] = AudioSystem.getClip();
                soundEffects[i].open(AudioSystem.getAudioInputStream(new File("audio/soundEffects/" + i +".wav")));
            } catch (Exception ex) {
                Main.errorScreen("ERROR LOADING SOUND EFFECTS", "PLEASE ENSURE 14 WAV FILES EXISTS IN THE AUDIO/SOUNDEFFECTS/ FOLDER");
            }
        }
    }
    
    public Font getFont() {
        return this.font;
    }
    
    public void playSoundEffect(int clipNumber) throws Exception {
        if (!mute) {
            soundEffects[clipNumber].setMicrosecondPosition(0);
            soundEffects[clipNumber].start();
        }
    }
    
    public void loopSoundEffect(int clipNumber) throws Exception {
        if (!mute) {
            soundEffects[clipNumber].setMicrosecondPosition(0);
            soundEffects[clipNumber].loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stopSoundEffect(int clipNumber) throws Exception {
        if (!mute) {
            soundEffects[clipNumber].stop();
        }
    }
    
    public void loopAudioClip(int clipNumber) throws Exception {
        if (!mute) {
            if (clipNumber == 0) {
                gameMusic[clipNumber].setMicrosecondPosition(0);
                gameMusic[clipNumber].loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                gameMusic[1].setMicrosecondPosition(0);
                gameMusic[1].loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }
    
    public void stopAudioClip(int clipNumber) throws Exception {
        if (clipNumber == 0) {
            gameMusic[clipNumber].stop();
        } else {
            gameMusic[1].stop();
        }
    }
    
    public void resumeMusic() throws Exception {
        gameMusic[1].loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public double getSpeed(double speedFactor) {
        return this.speed * speedFactor;
    }
    
    public double getYOffset(double paralaxFactor, double yShift) {
        if (this.getCurrentScreen() == this.GAMEPLAY) {
            if (this.invertControls) {
                return (Math.pow((this.WINDOW_HEIGHT / 2 - this.yMousePos) * 4, 2) / 800 * (this.yMousePos - this.WINDOW_HEIGHT / 2> 0 ? 1 : -1) * (1f / paralaxFactor) - yShift) / 8;
            } else {
                return (Math.pow((this.WINDOW_HEIGHT / 2 - this.yMousePos) * 4, 2) / 800 * (this.WINDOW_HEIGHT / 2 - this.yMousePos > 0 ? 1 : -1) * (1f / paralaxFactor) - yShift) / 8;
            }
        } else {
            return 0;
        }
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void died(DeathScreen deathScreen) {
        dead = true;
        for (int i = 0; i < ((ArrayList) this.highscores[gameMode.ordinal()]).size(); i++) {
            if (getDistance() > (int) ((Object[]) this.highscores[gameMode.ordinal()].get(i))[1]) {
                deathScreen.setHighscoreIndex(i);
                this.currentScreen = this.ENTER_NIGH_SCORE_NAME;
                break;
            } else if (((ArrayList) this.highscores[gameMode.ordinal()]).size() < 5 && i + 1 == ((ArrayList) this.highscores[gameMode.ordinal()]).size()) {
                deathScreen.setHighscoreIndex(i + 1);
                this.currentScreen = this.ENTER_NIGH_SCORE_NAME;
                break;
            }
            if (i == 4) {
                deathScreen.setHighscoreIndex(-1);
                this.currentScreen = this.DEATH_SCREEN;
            }
        }
        if (((ArrayList) this.highscores[gameMode.ordinal()]).isEmpty()) {
            deathScreen.setHighscoreIndex(0);
            this.currentScreen = this.ENTER_NIGH_SCORE_NAME;
        }
        this.nextColorScheme = null;
        this.lightningFlash = false;
        try {
            playSoundEffect(this.DEATH);
            stopSoundEffect(this.ENGINE_REGULAR);
            stopSoundEffect(this.ENGINE_DAMAGED);
        } catch (Exception ex) {
        }
    }
    
    public void setAnimating(boolean animating) {
        this.animationOccuring = animating;
    }
    
    public boolean animating() {
        return this.animationOccuring;
    }
    
    public void update() {
        if (this.currentScreen == GAMEPLAY) {
            this.distance += this.speed;
            if ((int) (getDistance() / (BREAK_DISTANCE / 10)) != (int) ((getDistance() - this.speed / 50) / (BREAK_DISTANCE / 10))) {
                this.speed += 0.2;
            }
        }
        if (getDistance() % BREAK_DISTANCE > BREAK_DISTANCE - 25 && this.nextColorScheme == null) {
            do {
                this.nextColorScheme = ColorSchemes.values()[(int) (Math.random() * (ColorSchemes.values().length - 1)) + 1];
            } while (this.colorScheme.equals(this.nextColorScheme));
            this.backgroundFadeAmount[0] = 0;
            this.backgroundFadeAmount[1] = 0;
            this.backgroundFadeAmount[2] = 0;
            for (int i = 0; i < 3; i++) {
                this.spikesFadeAmount[i][0] = 0;
                this.spikesFadeAmount[i][1] = 0;
                this.spikesFadeAmount[i][2] = 0;
            }
            this.fadePercentage = 0;
        } else if (!lightningFlash && this.nextColorScheme == null && !dead && Math.random() * 400 < 1 && getDistance() % BREAK_DISTANCE < BREAK_DISTANCE - 50 - 40 * this.speed / 50) {
            this.backgroundFadeAmount[0] = 0;
            this.backgroundFadeAmount[1] = 0;
            this.backgroundFadeAmount[2] = 0;
            lightningFlash = true;
            this.fadePercentage = -1;
        }
        if (lightningFlash) {
            if ((int) this.fadePercentage == -1) {
                this.fadePercentage -= 0.25;
            } else if ((int) this.fadePercentage == -2) {
                this.backgroundFadeAmount[0] = (this.colorScheme.BACKGROUND.getRed() - 255) / 1.2;
                this.backgroundFadeAmount[1] = (this.colorScheme.BACKGROUND.getGreen() - 255) / 1.2;
                this.backgroundFadeAmount[2] = (this.colorScheme.BACKGROUND.getBlue() - 255) / 1.2;
                this.fadePercentage -= 0.25;
            } else if ((int) this.fadePercentage == -3) {
                this.backgroundFadeAmount[0] = 0;
                this.backgroundFadeAmount[1] = 0;
                this.backgroundFadeAmount[2] = 0;
                this.fadePercentage = 0;
            } else {
                this.fadePercentage += 1;
                this.backgroundFadeAmount[0] = (this.colorScheme.BACKGROUND.getRed() - 255) / (40 / this.fadePercentage);
                this.backgroundFadeAmount[1] = (this.colorScheme.BACKGROUND.getGreen() - 255) / (40 / this.fadePercentage);
                this.backgroundFadeAmount[2] = (this.colorScheme.BACKGROUND.getBlue() - 255) / (40 / this.fadePercentage);
                if (this.fadePercentage > 40) {
                    lightningFlash = false;
                }
            }
        } else if (this.nextColorScheme != null) {
            this.backgroundFadeAmount[0] += (this.nextColorScheme.BACKGROUND.getRed() - this.colorScheme.BACKGROUND.getRed()) / (5000 / (this.speed));
            this.backgroundFadeAmount[1] += (this.nextColorScheme.BACKGROUND.getGreen() - this.colorScheme.BACKGROUND.getGreen()) / (5000 / (this.speed));
            this.backgroundFadeAmount[2] += (this.nextColorScheme.BACKGROUND.getBlue() - this.colorScheme.BACKGROUND.getBlue()) / (5000 / (this.speed));
            for (int i = 0; i < 3; i++) {
                this.spikesFadeAmount[i][0] += (this.nextColorScheme.SPIKES[i].getRed() - this.colorScheme.SPIKES[i].getRed()) / (5000 / (this.speed));
                this.spikesFadeAmount[i][1] += (this.nextColorScheme.SPIKES[i].getGreen() - this.colorScheme.SPIKES[i].getGreen()) / (5000 / (this.speed));
                this.spikesFadeAmount[i][2] += (this.nextColorScheme.SPIKES[i].getBlue() - this.colorScheme.SPIKES[i].getBlue()) / (5000 / (this.speed));
            }
            this.fadePercentage += 10 / (5000 / this.speed);
            if (this.fadePercentage >= 10) {
                this.colorScheme = this.nextColorScheme;
                this.nextColorScheme = null;
            }
        }
    }
    
    public void setGameMode(GameModes gameMode) {
        this.distance = 0;
        this.dead = false;
        this.showDeathScreen = false;
        this.damaged = false;
        this.gameMode = gameMode;
        this.speed = gameMode.speed;
        this.colorScheme = ColorSchemes.values()[this.gameMode.ordinal() + 1];
        //this.colorScheme = ColorSchemes.RED;
        this.nextColorScheme = null;
        this.backgroundFadeAmount[0] = 0;
        this.backgroundFadeAmount[1] = 0;
        this.backgroundFadeAmount[2] = 0;
        for (int i = 0; i < 3; i++) {
            this.spikesFadeAmount[i][0] = 0;
            this.spikesFadeAmount[i][1] = 0;
            this.spikesFadeAmount[i][2] = 0;
        }
    }
    
    public void reset() {
        this.laserAvailable = false;
        this.laserShot = false;
        this.nextColorScheme = null;
        this.lightningFlash = false;
        this.hoveredButtonType = -1;
        this.animationOccuring = false;
        this.backgroundFadeAmount[0] = 0;
        this.backgroundFadeAmount[1] = 0;
        this.backgroundFadeAmount[2] = 0;
        for (int i = 0; i < 3; i++) {
            this.spikesFadeAmount[i][0] = 0;
            this.spikesFadeAmount[i][1] = 0;
            this.spikesFadeAmount[i][2] = 0;
        }
        
    }
    
    public GameModes getGameMode() {
        return this.gameMode;
    }
    
    public double getExactDistance() {
        return distance / 50;
    }
    
    public int getDistance() {
        return (int) (this.distance / 50);
    }
    
    public void setMousePos(double xMousePos, double yMousePos) {
        realYMousePos = yMousePos;
        if (Math.abs(xMousePos - WINDOW_WIDTH / 2) > 235) {
            double x = MouseInfo.getPointerInfo().getLocation().getX() - xMousePos;
            this.xMousePos = 230 * (Math.abs(xMousePos - WINDOW_WIDTH / 2) / (xMousePos - WINDOW_WIDTH / 2)) + WINDOW_WIDTH / 2;
            x += this.xMousePos;
            double y = MouseInfo.getPointerInfo().getLocation().getY();
            try {
                Robot robot = new Robot();
                robot.mouseMove((int) x, (int) y);
            } catch (AWTException e) {
            }
        } else {
            this.xMousePos = xMousePos;
        }
        if (Math.abs(yMousePos - WINDOW_HEIGHT / 2) > 130) {
            double y = MouseInfo.getPointerInfo().getLocation().getY() - yMousePos;
            this.yMousePos = 125 * (Math.abs(yMousePos - WINDOW_HEIGHT / 2) / (yMousePos - WINDOW_HEIGHT / 2)) + WINDOW_HEIGHT / 2;
            y += this.yMousePos;
            double x = MouseInfo.getPointerInfo().getLocation().getX();
            try {
                Robot robot = new Robot();
                robot.mouseMove((int) x, (int) y);
            } catch (AWTException e) {
            }
        } else {
            this.yMousePos = yMousePos;
        }
    }
    
    public double getYMousePos() {
        return this.yMousePos;
    }
    
    public double getRealYMousePos() {
        return this.realYMousePos;
    }
    
    public double getXMousePos() {
        return this.xMousePos;
    }
    
    public void showDeathScreen() {
        this.showDeathScreen = true;
    }
    
    public boolean deathScreen() {
        return this.showDeathScreen;
    }
    
    public void setBaseColor(ColorSchemes colorScheme) {
        this.colorScheme = colorScheme;
    }
    
    public void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
    }
    
    public int getCurrentScreen() {
        return this.currentScreen;
    }
    
    public ArrayList[] getHighscores() {
        return this.highscores;
    }
    
    public void setHighscores(int index, Object[] newHighscore) {
        this.highscores[gameMode.ordinal()].add(index, newHighscore);
    }
    
    public void restoreHighscores(Object[] highscores) {
        try {
            for (int i = 0; i < highscores.length; i++) {
                this.highscores[i] = (ArrayList<Object[]>) highscores[i];
            }
        } catch (Exception ex) {
            Main.errorScreen("ERROR LOADING HIGHSCORES", "THE HIGHSCORES FILE MAY BE CORRUPTED, TRY RESTARTING THE GAME AND/OR DELETEING THE HIGHSCORES FILE");
        }
    }
    
    public Color getBackgroundColor() {
        if (this.lightningFlash) {
            return new Color(255 + (int) this.backgroundFadeAmount[0], 255 + (int) this.backgroundFadeAmount[1], 255 + (int) this.backgroundFadeAmount[2]);
        } else if (this.nextColorScheme != null) {
            return new Color(this.colorScheme.BACKGROUND.getRed() + (int) this.backgroundFadeAmount[0], this.colorScheme.BACKGROUND.getGreen() + (int) this.backgroundFadeAmount[1], this.colorScheme.BACKGROUND.getBlue() + (int) this.backgroundFadeAmount[2]);
        } else 
        return this.colorScheme.BACKGROUND;
    }
    
    public Color getSpikesColor(int depth) {
        if (this.nextColorScheme != null) {
            return new Color(this.colorScheme.SPIKES[depth - 1].getRed() + (int) this.spikesFadeAmount[depth - 1][0], this.colorScheme.SPIKES[depth - 1].getGreen() + (int) this.spikesFadeAmount[depth - 1][1], this.colorScheme.SPIKES[depth - 1].getBlue() + (int) this.spikesFadeAmount[depth - 1][2]);
        }
        return this.colorScheme.SPIKES[depth - 1];
    }
    
    public Color getBorderColor() {
        if (getDistance() % BREAK_DISTANCE < BREAK_DISTANCE / 10 && this.nextColorScheme != null) {
            return this.nextColorScheme.BORDER;
        }
        return this.colorScheme.BORDER;
    }
    
    public Color getBackgroundObjectColor(int layer) {
        return this.colorScheme.BACKGROUND_OBJECTS[layer];
    }
    
    public void setPlayerXPos(double playerXPos) {
        this.playerXPos = playerXPos;
    }
    
    public void setPlayerYPos(double playerYPos) {
        this.playerYPos = playerYPos;
    }
    
    public void setCursorXPos(double cursorXPos) {
        this.cursorXPos = cursorXPos;
    }
    
    public void setCursorYPos(double cursorYPos) {
        this.cursorYPos = cursorYPos;
    }
    
    public double getCursorXPos() {
        return this.cursorXPos;
    }
    
    public double getCursorYPos() {
        return this.cursorYPos;
    }
    
    public double getPlayerXPos() {
        return this.playerXPos;
    }
    
    public double getPlayerYPos() {
        return this.playerYPos;
    }
    
    public void setPlayerRotation(double playerRotation) {
        this.playerRotation = playerRotation;
    }
    
    public double getPlayerRotation() {
        return this.playerRotation;
    }
    
    public void setHoveredButtonType(int hoveredButtonType) {
        this.hoveredButtonType = hoveredButtonType;
    }
    
    public int getHoveredButtonType() {
        return this.hoveredButtonType;
    }
    
    public boolean lag() {
        return this.lag;
    }
    
    public void toggleLag() {
        this.lag = !this.lag;
        try {
            if (this.lag) {
                playSoundEffect(this.ENABLE_SETTING);
            } else {
                playSoundEffect(this.DISABLE_SETTING);
            }
        } catch (Exception ex) {
        }
    }
    
    public boolean debug() {
        return this.debug;
    }
    
    public void toggleMute() {
        try {
            if (!this.mute) {
                playSoundEffect(this.ENABLE_SETTING);
            }
        } catch (Exception ex) {
        }
        this.mute = !this.mute;
        try {
            if (!this.mute) {
                playSoundEffect(this.DISABLE_SETTING);
            }
        } catch (Exception ex) {
        }
    }
    
    public boolean mute() {
        return this.mute;
    }

    public boolean invertControls() {
        return this.invertControls;
    }
    
    public void toggleInvertControls() {
        this.invertControls = !this.invertControls;
        try {
            if (this.invertControls) {
                playSoundEffect(this.ENABLE_SETTING);
            } else {
                playSoundEffect(this.DISABLE_SETTING);
            }
        } catch (Exception ex) {
        }
    }
    
    public void toggleDebug() {
        this.debug = !this.debug;
        try {
            if (this.debug) {
                playSoundEffect(this.ENABLE_SETTING);
            } else {
                playSoundEffect(this.DISABLE_SETTING);
            }
        } catch (Exception ex) {
        }
    }
    
    public boolean isDamaged() {
        return this.damaged;
    }
    
    public boolean laserAvailable() {
        return this.laserAvailable;
    }
    
    public void setLaserAvailability(boolean laserAvailability) {
        this.laserAvailable = laserAvailability;
    }
    
    public boolean laserShot() {
        return this.laserShot;
    }
    
    public void togglelaserShot() {
        this.laserShot = !this.laserShot;
    }
    
    public void setDamage(boolean damaged) {
        this.damaged = damaged;
        try {
            if (this.damaged) {
                loopSoundEffect(this.ENGINE_DAMAGED);
                stopSoundEffect(this.ENGINE_REGULAR);
            } else if (!isDead()) {
                loopSoundEffect(this.ENGINE_REGULAR);
                stopSoundEffect(this.ENGINE_DAMAGED);
            }
        } catch (Exception ex) {
        }
    }
    
    public void gameModeSelectionTransision(GameModes nextGameMode) {
        this.nextColorScheme = ColorSchemes.values()[nextGameMode.ordinal() + 1];
        this.backgroundFadeAmount[0] += (this.nextColorScheme.BACKGROUND.getRed() - this.colorScheme.BACKGROUND.getRed()) / 20;
        this.backgroundFadeAmount[1] += (this.nextColorScheme.BACKGROUND.getGreen() - this.colorScheme.BACKGROUND.getGreen()) / 20;
        this.backgroundFadeAmount[2] += (this.nextColorScheme.BACKGROUND.getBlue() - this.colorScheme.BACKGROUND.getBlue()) / 20;
        for (int i = 0; i < 3; i++) {
            this.spikesFadeAmount[i][0] += (this.nextColorScheme.SPIKES[i].getRed() - this.colorScheme.SPIKES[i].getRed()) / 20;
            this.spikesFadeAmount[i][1] += (this.nextColorScheme.SPIKES[i].getGreen() - this.colorScheme.SPIKES[i].getGreen()) / 20;
            this.spikesFadeAmount[i][2] += (this.nextColorScheme.SPIKES[i].getBlue() - this.colorScheme.SPIKES[i].getBlue()) / 20;
        }
        this.speed += (nextGameMode.speed - gameMode.speed) / 15;
    }
}
