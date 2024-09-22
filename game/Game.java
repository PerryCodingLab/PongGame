package game;

// 322877754 Omer Perry
import biuoop.GUI;
import collision.Collidable;
import collision.Velocity;
import events.BallRemover;
import events.BlockRemover;
import events.HitListener;
import events.ScoreTrackingListener;
import shapes.Point;
import shapes.Rectangle;
import biuoop.DrawSurface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import assests.Ball;
import assests.Block;
import assests.Paddle;
import assests.ScoreIndicator;
import assests.Sprite;

//import java.util.ArrayList;
/**
 * The Game class represents the main game loop and holds the game environment,
 * sprites, GUI, and other game-related elements.
 * It includes methods for adding collidables, sprites, initializing the game,
 * and running the game animation loop.
 *
 * @author 322877754 Omer Perry
 */
public class Game {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter blockCounter;
    private Counter ballsCounter;
    private Counter scoreCount;
    private GUI gui;
    private biuoop.Sleeper sleeper;

    /**
     * Returns the sprite collection containing all sprites in the game.
     *
     * @return the sprite collection containing all sprites
     */
    public SpriteCollection getSprites() {
        return sprites;
    }

    /**
     * Adds a collidable to the game environment.
     *
     * @param c The collidable to be added.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Adds a sprite to the sprite collection.
     *
     * @param s The sprite to be added.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Initializes the game by creating blocks, a ball, and a paddle,
     * and adding them to the game.
     */
    public void initialize() {
        this.sleeper = new biuoop.Sleeper();
        this.environment = new GameEnvironment();
        this.sprites = new SpriteCollection();
        this.gui = new GUI("testing", 800, 600);
        this.blockCounter = new Counter();
        this.ballsCounter = new Counter();
        this.scoreCount = new Counter();
        ArrayList<HitListener> hl = new ArrayList<>();
        BlockRemover h = new BlockRemover(this, this.blockCounter);
        BallRemover death = new BallRemover(this, ballsCounter);
        ScoreTrackingListener score = new ScoreTrackingListener(this.scoreCount);
        ScoreIndicator scoreI = new ScoreIndicator(scoreCount);

        hl.add(h);
        hl.add(score);

        borders(h);
        // bottom
        Color c = java.awt.Color.WHITE;
        Block block = new Block(new Rectangle(new Point(0, 580), 800, 40), c);
        block.addToGame(this);
        block.addHitListener(death);

        levelOne(hl);

        Paddle paddle = new Paddle(gui.getKeyboardSensor());
        paddle.addToGame(this);
        scoreI.addToGame(this);
    }

    /**
     * sets up top, right, and left borders for the game.
     *
     * @param h a hitlistener
     */
    public void borders(HitListener h) {
        // top
        Block block = new Block(new Rectangle(new Point(-20, 20), 820, 20), Color.DARK_GRAY);
        block.addToGame(this);

        block = new Block(new Rectangle(new Point(-20, -20), 820, 40), Color.WHITE);
        block.addToGame(this);
        // right
        block = new Block(new Rectangle(new Point(780, 20), 40, 600), Color.DARK_GRAY);
        block.addToGame(this);

        // left
        block = new Block(new Rectangle(new Point(-20, 20), 40, 580), Color.DARK_GRAY);
        block.addToGame(this);
    }

    /**
     * sets up the game in a specific block placement.
     *
     * @param hl arraylist containing the hitlisteners to be added to the blocks
     */
    public void levelOne(ArrayList<HitListener> hl) {
        Random rand = new Random();
        Point upperLeft = new Point(20, 40);
        Point bottomRight = new Point(780, 620);
        Color c = java.awt.Color.RED;
        this.addRow(80, 640, 40, 80, c, hl);
        c = java.awt.Color.CYAN;
        this.addRow(160, 560, 70, 80, c, hl);
        c = java.awt.Color.GRAY;
        this.addRow(240, 480, 100, 80, c, hl);
        c = java.awt.Color.MAGENTA;
        this.addRow(320, 400, 130, 80, c, hl);

        Ball ball = new Ball(new Point(400, 400), 6, java.awt.Color.DARK_GRAY);
        ball.setVelocity(Velocity.fromAngleAndSpeed(rand.nextInt(360), 3));
        ball.setInner(upperLeft, bottomRight);
        Ball ballTwo = new Ball(new Point(450, 400), 6, java.awt.Color.DARK_GRAY);
        ballTwo.setVelocity(Velocity.fromAngleAndSpeed(rand.nextInt(360), 3));
        ballTwo.setInner(upperLeft, bottomRight);
        Ball ballThree = new Ball(new Point(450, 400), 6, java.awt.Color.DARK_GRAY);
        ballThree.setVelocity(Velocity.fromAngleAndSpeed(rand.nextInt(360), 3));
        ballThree.setInner(upperLeft, bottomRight);
        ball.setGame(this.environment);
        ball.addToGame(this);
        this.ballsCounter.increase(1);
        ballTwo.setGame(this.environment);
        ballTwo.addToGame(this);
        this.ballsCounter.increase(1);
        ballThree.setGame(this.environment);
        ballThree.addToGame(this);
        this.ballsCounter.increase(1);
    }

    /**
     * Adds a row of blocks to the game.
     *
     * @param start  The starting x-coordinate of the row.
     * @param end    The ending x-coordinate of the row.
     * @param height The y-coordinate of the row.
     * @param width  The width of each block in the row.
     * @param hl     a list of hit listeners to be assigned to each block.
     * @param c      The color of the blocks in the row.
     */
    public void addRow(int start, int end, int height, int width, java.awt.Color c, ArrayList<HitListener> hl) {
        Block block;
        for (int i = start; i <= end; i = i + width) {
            block = new Block(new Rectangle(new Point(i, height), width, 30), c);
            block.addToGame(this);
            for (HitListener h : hl) {
                block.addHitListener(h);
            }
            this.blockCounter.increase(1);
        }
    }

    /**
     * Runs the game animation loop.
     */
    public void run() {
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;
        while (true) {
            long startTime = System.currentTimeMillis(); // timing

            DrawSurface d = gui.getDrawSurface();
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
            if (this.blockCounter.getValue() == 0) {
                this.scoreCount.increase(100);
                this.gui.close();
                return;
            }
            if (this.ballsCounter.getValue() == 0) {
                this.gui.close();
                return;
            }
        }
    }

    /**
     * Removes the specified collidable from the game environment.
     *
     * @param c the collidable to remove
     */
    public void removeCollidable(Collidable c) {
        this.environment.collidableRemove(c);
    }

    /**
     * Removes the specified sprite from the game.
     *
     * @param s the sprite to remove
     */
    public void removeSprite(Sprite s) {
        this.sprites.spriteRemove(s);
    }

}