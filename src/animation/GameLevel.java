package animation;

import biuoop.GUI;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import interfaces.Animation;
import interfaces.Collidable;
import interfaces.Sprite;
import levels.BlockFiller;
import listeners.BallRemover;
import listeners.BlockRemover;
import others.Counter;
import others.GameEnvironment;
import others.SpriteCollection;
import others.Velocity;
import sprites.Ball;
import sprites.Block;
import sprites.Paddle;
import sprites.NameIndicator;
import sprites.Swarm;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


/**
 * .
 * author: Eyal Styskin
 * Class name:GameLevel
 * class operation: the class implements Animation, this class in charge of the gameplay.
 * its initializing the game, create all the sprites and collidable objects in the given
 * level, playing the turn, and counting the number of blocks/lives/balls and score.
 */

public class GameLevel implements Animation {
    private int levelNum;
    private Counter score;
    private Counter numOfLives;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private biuoop.GUI gui;
    private AnimationRunner runner;
    private boolean running;
    private Paddle paddle;
    private KeyboardSensor keyboard;
    private Swarm alienSwarm;
    private double paddleShotSleep;
    private double enemyShotSleep;
    private List<Ball> gameBalls = new ArrayList<>();

    /**
     * .
     * Function Operation: Constructor - creates all the members of the class,
     * according to the given parameters
     *
     * @param ks         - the keyboardSensor used
     * @param ar         - AnimationRunner type to run animations
     * @param numOfLives - number of lives the player has
     * @param levelNum - Integer - the number of the current played level
     * @param score - Counter type - contains the score of the player
     */

    public GameLevel(KeyboardSensor ks, AnimationRunner ar, Counter numOfLives,
                     Counter score, int levelNum) {
        this.levelNum = levelNum;
        this.score = score;
        this.numOfLives = numOfLives;
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.runner = ar;
        this.keyboard = ks;
        this.gui = ar.getGui();
        this.alienSwarm = new Swarm(score, this, 1.0 + levelNum);
    }

    /**
     * .
     * function name: addCollidable
     * Function Operation: adds the object given to the collidables list
     *
     * @param c - new collidable type object to be added
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * getter for this Swarm member.
     * @return - this Swarm type member
     */
    public Swarm getSwarm() {
        return this.alienSwarm;
    }

    /**
     * .
     * function name: addSprite
     * Function Operation: adds the object given to the Sprites list
     *
     * @param s - new collidable type object to be added
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * .
     * function name: removeCollidable
     * Function Operation: removes the collidable object from the collidables list.
     *
     * @param c - Collidable type object
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidables(c);
    }

    /**
     * .
     * function name: removeSprite
     * Function Operation: removes the Sprite object from the Sprites list.
     *
     * @param s - Sprite type object
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * .
     * function name: getGameEnvironment
     * Function Operation: getter for this GameEnvironment
     *
     * @return this.environment - the member
     */
    public GameEnvironment getGameEnvironment() {
        return this.environment;
    }

    /**
     * .
     * function name: getNumOfLives
     * Function Operation: getter for this numOfLives member
     *
     * @return - Counter type of the lives number
     */
    public Counter getNumOfLives() {
        return numOfLives;
    }

    /**
     * .
     * function name: getGui
     * Function Operation: getter for this GUI member
     *
     * @return - GUI type - this gui
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * .
     * function name: initialize
     * Function Operation: Initialize a new GameLevel: create the Blocks(in rows) ,
     * paddle, listeners: for block,ball and score. all according to the LevelInfo
     * (current level played) that set in the constructor.
     */
    public void initialize() {
        double padSpeed = 600;
        double paddlewWidth = 50;
        this.paddle = (new Paddle(new Rectangle(new Point(400 - paddlewWidth / 2, 570),
                paddlewWidth, 20), Color.orange, padSpeed, this));
        this.paddle.addToGame(this);

        buildShields();

        NameIndicator nameIndicator = new NameIndicator("Battle no. " + Integer.toString(this.levelNum));
        nameIndicator.addToGame(this);
        this.borderBlocks(); // create border blocks and add them to game
    }

    /**
     * building 3 shields that contain small blocks 5*5 in cyan color, being destroyed when ball hits them.
     */
    public void buildShields() {
        Map<Integer, BlockFiller> shieldsMap = new TreeMap<Integer, BlockFiller>();
        BlockFiller blockFiller = new BlockFiller(Color.cyan);
        shieldsMap.put(1, blockFiller);
        for (int k = 0; k < 3; ++k) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 24; ++j) {
                    Block block = new Block(new Rectangle(new Point(125 + (j * 5) + (k * 215),
                            500 + i * 5), 5, 5));
                    BlockRemover blockRemover = new BlockRemover(this);
                    block.addHitListener(blockRemover);

                    BallRemover ballRemover = new BallRemover(this);
                    block.addHitListener(ballRemover);

                    block.setColorsMap(shieldsMap);
                    block.addToGame(this);
                }
            }
        }
    }

    /**
     * Create the Blocks the borders the game screen, sets its characteristics and
     * adds it to the game.
     */
    public void borderBlocks() {
        Block block1 = new Block(new Rectangle(new Point(0, 20), 800, 25)); //up block
        Block block2 = new Block(new Rectangle(new Point(0, 20), 25, 580)); //left block
        Block block3 = new Block(new Rectangle(new Point(775, 20), 25, 580)); //right block
        BlockFiller blockFiller = new BlockFiller(Color.gray);
        Map<Integer, BlockFiller> bordersMap = new TreeMap<Integer, BlockFiller>();
        bordersMap.put(1, blockFiller);
        block1.setColorsMap(bordersMap);
        block2.setColorsMap(bordersMap);
        block3.setColorsMap(bordersMap);
        block1.setStroke(Color.BLACK);
        block2.setStroke(Color.BLACK);
        block3.setStroke(Color.BLACK);
        block1.addToGame(this);
        block2.addToGame(this);
        block3.addToGame(this);
        Block deathBlock = new Block(new Rectangle(
                new Point(0, 600), 800, 10)); //down block (death)
        deathBlock.setColorsMap(bordersMap);
        deathBlock.addToGame(this);
        BallRemover ballRemover = new BallRemover(this); //removes balls
        block1.addHitListener(ballRemover);
        block2.addHitListener(ballRemover);
        block3.addHitListener(ballRemover);
        deathBlock.addHitListener(ballRemover);
    }

    /**
     * function name: playOneTurn
     * Function Operation: plays one turn of the game. creates the balls, runs the countdown
     * animation and runs the logic of the game.
     */
    public void playOneTurn() {
        this.alienSwarm.resetFormation();
        if (this.gameBalls.size() != 0) {
            int ballsNum = this.gameBalls.size();
            for (int i = 0; i < ballsNum; ++i) {
                gameBalls.get(0).removeFromGame(this);
                this.gameBalls.remove(gameBalls.get(0));
            }
        }
        this.runner.run(new

                CountdownAnimation(2, 3, this.sprites)); // countdown before turn starts.
        this.running = true;

        this.runner.run(this);
    }


    /**
     * function name: shouldStop
     * Function Operation: returns the boolean member, if the game
     * should be stopped.
     *
     * @return - this.running - boolean type if to stop
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * function name: doOneFrame
     * Function Operation: draws all the sprites and plays them.
     * allows to pause the game if "p" pressed, and orders to finish the turn,
     * if the player looses.
     *
     * @param d  - Drawsurface type - to draw number on it
     * @param dt - frames per second
     */

    public void doOneFrame(DrawSurface d, double dt) {
        this.enemyShotSleep = this.enemyShotSleep - dt;
        this.paddleShotSleep = this.paddleShotSleep - dt;
        d.setColor(Color.BLACK);
        d.fillRectangle(0, 0, 800, 600);
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        this.alienSwarm.moveSwarm();
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(
                    this.keyboard, "space", new PauseScreen()));
        }
        if (this.keyboard.isPressed(keyboard.SPACE_KEY) && this.paddleShotSleep < 0) {
            this.shootBullet();
        }
        if (this.enemyShotSleep < 0) {
            this.enemyShoot();
        }
        if (this.alienSwarm.getIsLost() || this.paddle.getIsPaddleHit()) {
            this.numOfLives.decrease(1);
            this.paddle.moveToMiddle();
            this.running = false;
            this.paddle.resetIsPaddleHit();
        }
        if (this.alienSwarm.getAliensNum().getValue() == 0) { // if out of aliens - next level!
            this.paddle.moveToMiddle();
            this.running = false;
        }
    }

    /**
     *
     */
    public void enemyShoot() {
        this.enemyShotSleep = 0.5;
        Random rand = new Random();
        int column = rand.nextInt(10);
        while (true) { //find non empty column
            if (this.alienSwarm.getAliensSwarm().get(column).size() == 0) {
                column = rand.nextInt(10);
            } else {
                break;
            }
        }
        double lowestY = 0;
        double shotLocation = 0;
        for (int i = 0; i < this.alienSwarm.getAliensSwarm().get(column).size(); ++i) {
            double checkedY = this.alienSwarm.getAliensSwarm().get(column).get(i).
                    getCollisionRectangle().getDownRight().getY();
            if (checkedY > lowestY) {
                lowestY = checkedY;
                shotLocation = this.alienSwarm.getAliensSwarm().get(column).get(i).
                        getCollisionRectangle().getDownRight().getX() - 20;
            }
        }
        Ball ball = new Ball(new Point(shotLocation, lowestY + 5), 3, Color.RED);
        ball.setVelocity(Velocity.fromAngleAndSpeed(180, 400));
        this.gameBalls.add(ball);
        ball.addToGame(this);

    }

    /**
     * creates new ball from middle of the paddle that goes straight,
     * shoots max rate of 0.35 for a ball.
     */
    public void shootBullet() {
        double paddleBulletX = this.paddle.getCollisionRectangle().getUpperLeft().getX()
                + this.paddle.getCollisionRectangle().getWidth() / 2;
        Ball ball = new Ball(new Point(paddleBulletX, 569), 3, Color.white);
        ball.setVelocity(Velocity.fromAngleAndSpeed(0, 800));
        ball.addToGame(this);
        this.paddleShotSleep = 0.350;
        this.gameBalls.add(ball);
    }
}
