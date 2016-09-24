import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Main {

    private static final JFrame frame = new JFrame("LAZER DOG");
    private static ArrayList<GameObject> gameObjects;
    private static ContentPane contentPane;
    private static Game game;
    private static double previousMouseYPos;

    public static void main(String[] args){
    	try {
        	game = new Game();
    	} catch (Exception ex) {
    		
    	}
        gameObjects = new ArrayList<>();
        game.init();
        contentPane = new ContentPane(gameObjects, game);
        contentPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent event) {
                game.setMousePos(event.getX(), event.getY());
            }
            @Override
            public void mouseDragged(MouseEvent event) {
                game.setMousePos(event.getX(), event.getY());
            }
        });
        contentPane.addMouseListener(new MouseClicked());
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane.setPreferredSize(new Dimension(game.WINDOW_WIDTH, game.WINDOW_HEIGHT));

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setCursor(getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "Blank Cursor"));
        frame.addKeyListener(new KeyPressed());
        frame.addFocusListener(new checkFocus());
        try {
            frame.setIconImage(ImageIO.read(new File("icon.png")));
    	} catch (IOException ex) {
        	System.out.println("No Icon Image for you!");
   	 	}
        if (new File("highscores").exists()) {
            try (FileInputStream fileIn = new FileInputStream("highscores"); ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                game.restoreHighscores((Object[]) objectIn.readObject());
            } catch (Exception ex) {
                Main.errorScreen("ERROR LOADING HIGHSCORES", "THE HIGHSCORES FILE MAY BE CORRUPTED, TRY RESTARTING THE GAME AND/OR DELETEING THE HIGHSCORES FILE");
            }
        }
        
        new Timer(game.ANIMATION_DELAY, (ActionEvent event) -> {
            contentPane.repaint();
            if (game.getCurrentScreen() != game.PAUSED_SCREEN && game.getCurrentScreen() != game.ERROR_SCREEN) {
                if (!game.isDead() && game.getCurrentScreen() == game.GAMEPLAY) {
                    game.update();
                }
                for (int i = 0; i < gameObjects.size(); i++) {
                    gameObjects.get(i).update();
                }
            } else {
                gameObjects.get(gameObjects.size() - 1).update();
                gameObjects.get(gameObjects.size() - 2).update();
                gameObjects.get(gameObjects.size() - 3).update();
            }
        }).start();
        if (game.getCurrentScreen() != game.ERROR_SCREEN) {
        	try {
                game.playSoundEffect(game.WOOF);
            } catch (Exception ex) {
            }
            introScreen();
        }
    }

    public static void checkCollision(GameObject object) {
        for (int x = gameObjects.size() - 1; x > 0; x--) {
            if (gameObjects.get(x) instanceof Spikes && object instanceof Player) {
                for (int i = 0; i < 2; i++) {
                    Spike spike = ((Spikes) gameObjects.get(x)).getFirst()[i];
                    while (spike != null && spike.getXPeak() < 330) {
                        if (spike.getXPeak() > 300) {
                            if (spike.getYPeak() * spike.getType() > (((CollisionObject) object).getY() + 11) * spike.getType()) {
                                gameObjects.add(new Explosion(315, ((Player) object).getY(), game));
                                gameObjects.add(new ScreenWipe(game));
                                gameObjects.add(new DeathScreen(game));
                                game.died((DeathScreen) gameObjects.get(gameObjects.size() - 1));
                                game.setAnimating(true);
                                gameObjects.add(new MainMenuButton(25, 25, 50, game, 0));
                                gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
                                return;
                            } else if (!game.lag() && spike.getYPeak() * spike.getType() + 50 > (((CollisionObject) object).getY() + 11) * spike.getType()) {
                                for (int y = gameObjects.size() - 1; y > 0; y--) {
                                    if (gameObjects.get(y) instanceof MiniRock) {
                                        if (((MiniRock) gameObjects.get(y)).isAvailable() && (Math.random() * 10) > 8) {
                                            ((MiniRock) gameObjects.get(y)).start(spike.getType());
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        spike = spike.getNext();
                    }
                }
                return;
            } else if (gameObjects.get(x) instanceof Obstacle) {
                if (((Obstacle) gameObjects.get(x)).getX() < ((CollisionObject) object).getEndX() &&
                        ((Obstacle) gameObjects.get(x)).getEndX() > ((CollisionObject) object).getX() &&
                        ((Obstacle) gameObjects.get(x)).getY() < ((CollisionObject) object).getEndY() &&
                        ((Obstacle) gameObjects.get(x)).getEndY() > ((CollisionObject) object).getY()) {
                    if (!game.isDamaged() || object instanceof WeaponLaser) {
                        if (object instanceof Player) {
                            game.setDamage(true);
                            ((Obstacle) gameObjects.get(x)).playerHit();
                            try {
                                game.playSoundEffect(game.ROCK_EXPLODE);
                            } catch (Exception ex) {
                            }
                        }
                        ((Obstacle) gameObjects.get(x)).hit();
                        try {
                            game.playSoundEffect(game.ROCK_EXPLODE);
                        } catch (Exception ex) {
                        }
                    } else {
                        ((Obstacle) gameObjects.get(x)).hit();
                        ((Obstacle) gameObjects.get(x)).playerHit();
                        gameObjects.add(new Explosion(311, ((Player) object).getY(), game));
                        gameObjects.add(new ScreenWipe(game));
                        gameObjects.add(new DeathScreen(game));
                        game.died((DeathScreen) gameObjects.get(gameObjects.size() - 1));
                        game.setAnimating(true);
                        gameObjects.add(new MainMenuButton(25, 25, 50, game, 0));
                        gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
                        return;
                    }
                }
            }
        }
    }
    
    public static void startGame(GameModes gameMode) {
        gameObjects.clear();
        game.setGameMode(gameMode);
        game.setCurrentScreen(game.GAMEPLAY);
        for (int i = 0; i < game.NUM_OF_STARS; i++) {
            double xPos = (i % (game.NUM_OF_STARS / 3)) * (game.WINDOW_WIDTH / (game.NUM_OF_STARS / 3)) + Math.random() * (game.WINDOW_WIDTH / (game.NUM_OF_STARS / 3));
            double yPos = (i / (game.NUM_OF_STARS / 3)) * (game.WINDOW_HEIGHT / 3) + Math.random() * (game.WINDOW_HEIGHT / 3);
            int depth = (int) (Math.random() * 3 + 1);
            gameObjects.add(new Star(xPos, yPos, 3, 3, game, depth));
        }
        gameObjects.add(new Planet(game));
        if (!game.lag()) {
            for (int i = 0; i < game.NUM_OF_BEAMS; i++){
                gameObjects.add(new LightBeam(game.WINDOW_WIDTH + i * game.WINDOW_WIDTH / game.NUM_OF_BEAMS, 50, 3, game));
            }
        }
        
        gameObjects.add(new Spikes(game, new Spike(0, 125, game.getGameMode().minYChange / 3, game.getGameMode().minSpikeHeight / 2, game, 1, 0, 0, 3), new Spike(0, 550, game.getGameMode().minYChange / 3, game.getGameMode().minSpikeHeight / 2, game, -1, 0, 0, 3)));
        if (!game.lag()) {
            for (int i = 0; i < game.NUM_OF_BEAMS; i++){
                    gameObjects.add(new LightBeam(game.WINDOW_WIDTH + i * game.WINDOW_WIDTH / game.NUM_OF_BEAMS, 40, 2, game));
            }
        }
        for (int i = 0; i < game.NUM_OF_LAZERS; i++) {
            gameObjects.add(new Laser(game));
        }
        gameObjects.add(new Spikes(game, new Spike(0, 75, game.getGameMode().minSpikeWidth / 2, game.getGameMode().minSpikeHeight / 2, game, 1, 0, 0, 2), new Spike(0, 600, game.getGameMode().minYChange / 2, game.getGameMode().minSpikeHeight / 2, game, -1, 0, 0, 2)));
        if (!game.lag()) {
            for (int i = 0; i < game.NUM_OF_BEAMS; i++){
                    gameObjects.add(new LightBeam(game.WINDOW_WIDTH + i * game.WINDOW_WIDTH / game.NUM_OF_BEAMS, 30, 1, game));
            }
        }
        gameObjects.add(new DashedLine(game));
        gameObjects.add(new Player(-50, game.WINDOW_HEIGHT / 2, 23, 22, game));
        gameObjects.add(new Spikes(game, new Spike(game.WINDOW_WIDTH, -400, game.getGameMode().minSpikeWidth, game.getGameMode().minSpikeHeight, game, 1, 1, 10, 1), new Spike(game.WINDOW_WIDTH, 1075, game.getGameMode().minSpikeWidth, game.getGameMode().minSpikeHeight, game, -1, 1, 10, 1)));
        if (!game.lag()) {
            for (int i = 0; i < game.NUM_OF_MINIROCKS; i++){
                gameObjects.add(new MiniRock(0, game));
            }
            for (int i = 0; i < game.NUM_OF_MINIROCKS / 2; i++){
                gameObjects.add(new MiniRock(1, game));
            }
        }
        for (int i = 0; i < game.getGameMode().NUM_OF_ROCKS; i++){
            gameObjects.add(new Rock(game));
        }
        if (game.getGameMode().equals(GameModes.DARK)) {
            gameObjects.add(new Flashlight(game));
        }
        for (int i = 0; i < 1; i++){
            gameObjects.add(new Missile(game));
        }
        gameObjects.add(new WeaponLaser(game));
        gameObjects.add(new Distance(game));
        double x = MouseInfo.getPointerInfo().getLocation().getX() - game.getXMousePos() + game.WINDOW_WIDTH / 2;
        double y = MouseInfo.getPointerInfo().getLocation().getY() - game.getRealYMousePos() + game.WINDOW_HEIGHT / 2;
        if (game.debug()) {
            gameObjects.add(new DebugInfo(game));
        }
        try {
            game.loopSoundEffect(game.ENGINE_REGULAR);
        } catch (Exception ex) {
        }
        try {
            Robot robot = new Robot();
            robot.mouseMove((int) x, (int) y);
        } catch (AWTException e) {
        }
    }
    
    public static void introScreen() {
    	gameObjects.clear();
    	game.setAnimating(true);
    	game.setCurrentScreen(game.INTRO_SCREEN);
    	gameObjects.add(new IntroScreen(game));
    }
    
    public static void mainMenu() {
        gameObjects.clear();
        game.setGameMode(GameModes.NORMAL);
        game.reset();
        game.setAnimating(true);
        game.setHoveredButtonType(-1);
        game.setCurrentScreen(game.MAIN_MENU);
        for (int i = 0; i < game.NUM_OF_STARS; i++) {
            double xPos = (i % (game.NUM_OF_STARS / 3)) * (game.WINDOW_WIDTH / (game.NUM_OF_STARS / 3)) + Math.random() * (game.WINDOW_WIDTH / (game.NUM_OF_STARS / 3));
            double yPos = (i / (game.NUM_OF_STARS / 3)) * (game.WINDOW_HEIGHT / 3) + Math.random() * (game.WINDOW_HEIGHT / 3);
            int depth = (int) (Math.random() * 3 + 1);
            gameObjects.add(new Star(xPos, yPos, 3, 3, game, depth));
        }
        
        gameObjects.add(new Spikes(game, new Spike(0, 125, game.getGameMode().minYChange / 3, game.getGameMode().minSpikeHeight / 2, game, 1, 0, 0, 3), new Spike(0, 550, game.getGameMode().minYChange / 3, game.getGameMode().minSpikeHeight / 2, game, -1, 0, 0, 3)));
        gameObjects.add(new Spikes(game, new Spike(0, 75, game.getGameMode().minSpikeWidth / 2, game.getGameMode().minSpikeHeight / 2, game, 1, 0, 0, 2), new Spike(0, 600, game.getGameMode().minYChange / 2, game.getGameMode().minSpikeHeight / 2, game, -1, 0, 0, 2)));
        for (int i = 0; i < game.NUM_OF_LAZERS; i++) {
            gameObjects.add(new Laser(game));
        }
        gameObjects.add(new MainMenu(game));
    	gameObjects.add(new MainMenuButton(100, 25, 50, game, 1));
    	gameObjects.add(new MainMenuButton(25, 25, 50, game, 2));
        gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
        gameObjects.add(new Cursor(game));
    }

    public static void gameModeSelectScreen() {
        gameObjects.subList(gameObjects.size() - 5, gameObjects.size()).clear();
        game.reset();
        game.setCurrentScreen(game.GAME_MODE_SELECT_SCREEN);
        gameObjects.add(new GameModeSelectScreen(game));
    	gameObjects.add(new MainMenuButton(100, 25, 50, game, 1));
    	gameObjects.add(new MainMenuButton(25, 25, 50, game, 2));
    	gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
        gameObjects.add(new Cursor(game));
    }
    
    public static void highScoresScreen() {
        gameObjects.clear();
        game.reset();
        game.setCurrentScreen(game.HIGH_SCORES_SCREEN);
        gameObjects.add(new HighScoresScreen(game));
        gameObjects.add(new MainMenuButton(25, 25, 50, game, 0));
        gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
        gameObjects.add(new Cursor(game));
    }
    
    public static void settingsScreen() {
        gameObjects.clear();
        game.reset();
        game.setCurrentScreen(game.SETTINGS_SCREEN);
        gameObjects.add(new SettingsScreen(game));
        gameObjects.add(new MainMenuButton(25, 25, 50, game, 0));
        gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
        gameObjects.add(new Cursor(game));
    }
    
    public static void deathScreen() {
        gameObjects.subList(0, gameObjects.size() - 3).clear();
        game.setBaseColor(ColorSchemes.BLACK);
        game.setAnimating(false);
        gameObjects.add(new MainMenuButton(25, 25, 50, game, 0));
        gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
        gameObjects.add(new Cursor(game));
        game.reset();
    }
    
    public static void errorScreen(String error, String message) {
        gameObjects.clear();
        game.reset();
        game.setCurrentScreen(game.ERROR_SCREEN);
        game.setBaseColor(ColorSchemes.BLACK);
        gameObjects.add(new ErrorScreen(error, message, game));
        game.setAnimating(false);
        gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
        gameObjects.add(new Cursor(game));
        
    }
    
    public static class KeyPressed implements KeyListener{

        @Override
        public void keyTyped(KeyEvent key) {
        }

        @Override
        public void keyPressed(KeyEvent key) {
            if (key.getKeyCode() == KeyEvent.VK_ESCAPE && game.getCurrentScreen() == game.GAMEPLAY) {
                gameObjects.add(new PauseScreen(game));
                previousMouseYPos = game.getRealYMousePos();
                game.setCurrentScreen(game.PAUSED_SCREEN);
                gameObjects.add(new MainMenuButton(25, 25, 50, game, 0));
                gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
                gameObjects.add(new Cursor(game));
                try {
                    if (game.isDamaged()) {
                        game.stopSoundEffect(game.ENGINE_DAMAGED);
                    } else {
                        game.stopSoundEffect(game.ENGINE_REGULAR);
                    }
                    game.playSoundEffect(game.OPEN_SCREEN);
                    game.stopAudioClip(-1);
                } catch (Exception ex) {
                }
            } else if (game.getCurrentScreen() == game.ENTER_NIGH_SCORE_NAME && !game.animating()) {
                String name = ((DeathScreen) gameObjects.get(0)).getName();
                if (key.getKeyCode() == KeyEvent.VK_ENTER && name.length() > 0) {
                    Object[] newHighscore = new Object[2];
                    newHighscore[0] = name;
                    newHighscore[1] = game.getDistance();
                    game.getHighscores()[game.getGameMode().ordinal()].add(((DeathScreen) gameObjects.get(0)).getHighscoreIndex(), newHighscore);
                    if (game.getHighscores()[game.getGameMode().ordinal()].size() > 5) {
                        game.getHighscores()[game.getGameMode().ordinal()].remove(5);
                    }
                    try (FileOutputStream fileOut = new FileOutputStream("highscores"); ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
                        objectOut.writeObject(game.getHighscores());
                    } catch (FileNotFoundException ex) {
                    } catch (IOException ex) {
                    }
                    ((DeathScreen) gameObjects.get(0)).nameEntered();
                } else if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE && name.length() > 0) {
                    ((DeathScreen) gameObjects.get(0)).editName(name.substring(0, name.length() - 1));
                } else if (name.length() < 25) {
                    char keyValue = key.getKeyChar();
                    if (keyValue >= 'A' && keyValue <= 'Z' || keyValue >= 'a' && keyValue <= 'z' || keyValue >= '0' && keyValue <= '9' || keyValue == ' ' || keyValue == '\'' || keyValue == '_'|| keyValue == '\"'|| keyValue == '-') {
                        ((DeathScreen) gameObjects.get(0)).editName(name + keyValue);
                    }
                }
            } else if (key.getKeyCode() == KeyEvent.VK_SPACE && game.getCurrentScreen() == game.GAMEPLAY && game.laserAvailable()) {
                game.togglelaserShot();
            } else if (key.getKeyCode() == KeyEvent.VK_SPACE && game.getCurrentScreen() == game.DEATH_SCREEN && !game.animating()) {
                if (game.getCurrentScreen() == game.GAME_MODE_SELECT_SCREEN) {
                    try {
                        game.stopAudioClip(0);
                        game.loopAudioClip(-1);
                    } catch (Exception ex) {
                    }
                }
                startGame(game.getGameMode());
                try {
                    game.playSoundEffect(game.OPEN_SCREEN);
                } catch (Exception ex) {
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent key) {
        }
        
    }
    
    public static class MouseClicked implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent event) {}

        @Override
        public void mousePressed(MouseEvent event) {
            if (game.getHoveredButtonType() == 0 && game.getCurrentScreen() != game.INTRO_SCREEN) {
                if (game.getCurrentScreen() == game.DEATH_SCREEN || game.getCurrentScreen() == game.PAUSED_SCREEN) {
                    try {
                        game.stopAudioClip(-1);
                        game.loopAudioClip(0);
                    } catch (Exception ex) {
                    }
                }
                try {
                    game.playSoundEffect(game.CLOSE_SCREEN);
                } catch (Exception ex) {
                }
                mainMenu();
            } else if (game.getHoveredButtonType() == 1) {
                highScoresScreen();
                try {
                    game.playSoundEffect(game.OPEN_SCREEN);
                } catch (Exception ex) {
                }
            } else if (game.getHoveredButtonType() == 2) {
                settingsScreen();
                try {
                    game.playSoundEffect(game.OPEN_SCREEN);
                } catch (Exception ex) {
                }
            } else if (game.getHoveredButtonType() == 3) {
                frame.dispose();
                System.exit(0);
            } else if (game.getHoveredButtonType() == 4 || game.getHoveredButtonType() == 5) {
                try {
                    game.playSoundEffect(game.GAMEMODE_SELECT);
                } catch (Exception ex) {
                }
                game.setAnimating(true);
            } else if ((game.getCurrentScreen() == game.GAME_MODE_SELECT_SCREEN || game.getCurrentScreen() == game.DEATH_SCREEN) && !game.animating()) {
                if (game.getCurrentScreen() == game.GAME_MODE_SELECT_SCREEN) {
                    try {
                        game.stopAudioClip(0);
                        game.loopAudioClip(-1);
                    } catch (Exception ex) {
                    }
                }
                startGame(game.getGameMode());
                try {
                    game.playSoundEffect(game.OPEN_SCREEN);
                } catch (Exception ex) {
                }
            } else if (game.getCurrentScreen() == game.MAIN_MENU && !game.animating()) {
                gameModeSelectScreen();
                try {
                    game.playSoundEffect(game.OPEN_SCREEN);
                } catch (Exception ex) {
                }
            } else if (game.getCurrentScreen() == game.PAUSED_SCREEN) {
                gameObjects.remove(gameObjects.size() - 1);
                gameObjects.remove(gameObjects.size() - 1);
                gameObjects.remove(gameObjects.size() - 1);
                gameObjects.remove(gameObjects.size() - 1);
                double x = MouseInfo.getPointerInfo().getLocation().getX() - game.getXMousePos() + game.WINDOW_WIDTH / 2;
                double y = MouseInfo.getPointerInfo().getLocation().getY() - game.getRealYMousePos() + previousMouseYPos;
                try {
                    Robot robot = new Robot();
                    robot.mouseMove((int) x, (int) y);
                } catch (AWTException e) {
                }
                game.setCurrentScreen(game.GAMEPLAY);
                try {
                    if (game.isDamaged()) {
                        game.loopSoundEffect(game.ENGINE_DAMAGED);
                    } else {
                        game.loopSoundEffect(game.ENGINE_REGULAR);
                    }
                    game.playSoundEffect(game.CLOSE_SCREEN);
                    game.resumeMusic();
                } catch (Exception ex) {
                }
            } else if (game.getCurrentScreen() == game.SETTINGS_SCREEN) {
                ((SettingsScreen) gameObjects.get(gameObjects.size() - 4)).setConfirmState(false);
                if (game.getHoveredButtonType() == 70) {
                    game.toggleMute();
                    try {
                        if (game.mute()) {
                            game.stopAudioClip(0);
                        } else {
                            game.loopAudioClip(0);
                        }
                    } catch (Exception ex) {
                    }
                } else if (game.getHoveredButtonType() == 71) {
                    game.toggleInvertControls();
                } else if (game.getHoveredButtonType() == 72) {
                    game.toggleLag();
                } else if (game.getHoveredButtonType() == 73) {
                    game.toggleDebug();
                } else if (game.getHoveredButtonType() == 74) {
                    ((SettingsScreen) gameObjects.get(gameObjects.size() - 4)).setConfirmState(true);
                } else if (game.getHoveredButtonType() == 75) {
                    new File("highscores").delete();
                    for (int i = 0; i < GameModes.values().length; i++) {
                        game.getHighscores()[i].clear();
                    }
                }
            } else if (game.getCurrentScreen() == game.GAMEPLAY && game.laserAvailable()) {
                game.togglelaserShot();
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {}

        @Override
        public void mouseEntered(MouseEvent event) {}

        @Override
        public void mouseExited(MouseEvent event) {}

    }
   
    public static class checkFocus implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (game.getCurrentScreen() == game.GAMEPLAY) {
                gameObjects.add(new PauseScreen(game));
                previousMouseYPos = game.getRealYMousePos();
                game.setCurrentScreen(game.PAUSED_SCREEN);
                gameObjects.add(new MainMenuButton(25, 25, 50, game, 0));
                gameObjects.add(new MainMenuButton(game.WINDOW_WIDTH - 75, 25, 50, game, 3));
                gameObjects.add(new Cursor(game));
                try {
                    if (game.isDamaged()) {
                        game.stopSoundEffect(game.ENGINE_DAMAGED);
                    } else {
                        game.stopSoundEffect(game.ENGINE_REGULAR);
                    }
                    game.playSoundEffect(game.OPEN_SCREEN);
                    game.stopAudioClip(-1);
                } catch (Exception ex) {
                }
            }
        }
    
    }
}
