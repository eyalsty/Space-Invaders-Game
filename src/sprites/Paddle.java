package sprites;

import animation.GameLevel;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Line;
import geometry.Rectangle;
import interfaces.Collidable;
import interfaces.Sprite;
import java.awt.Color;

/**
 * .
 * author: Eyal Styskin
 * Class name:Paddle
 * class operation: the class "Paddle" implements the collidable and Sprite interfaces and has its methods.
 * the created Paddle object is a Rectangle that can move right and left (according to the KeyboardSensor member),
 * is has also a color member.
 * the Paddle has the ability to change the direction (and velocity) of the ball that hits him
 * according to the hit location on the paddle, and to be drawn on the given surface.
 */
public class Paddle implements Sprite, Collidable {
    private final double borderBlockSize = 25;
    private final double screenWidth = 800;
    private double paddleSpeed;
    private Line movementLine;
    private biuoop.KeyboardSensor keyboard;
    private Rectangle rectangle;
    private Color color;
    private Double dt;
    private GameLevel gameLevel;
    private Boolean isPaddleHit;


    /**
     * Function Operation: constructor - creates new Paddle from the rectangle class and sets its color.
     *
     * @param rectangle - Rectangle type rectangle to create the Paddle
     * @param color     - color given for the Paddle
     * @param speed     - double type movement speed of the paddle
     * @param gameLevel - the game od the paddle (GameLevel type)
     */
    public Paddle(Rectangle rectangle, Color color, double speed, GameLevel gameLevel) {
        this.rectangle = rectangle;
        this.color = color;
        this.paddleSpeed = speed;
        this.gameLevel = gameLevel;
        this.resetIsPaddleHit();
    }

    /**
     * reset the isPaddleHit member to be false (beginning of every turn).
     */
    public void resetIsPaddleHit() {
        this.isPaddleHit = false;
    }

    /**
     * .
     * function name: moveLeft
     * Function Operation:move the paddle to the left (10 pixels every time) until it reaches the left edge
     * (most left block)
     */
    public void moveLeft() {
        double speed = this.paddleSpeed * this.dt;
        Point p = new Point(this.rectangle.getUpperLeft().getX() - speed,
                this.rectangle.getUpperLeft().getY());
        this.rectangle = new Rectangle(p, this.rectangle.getWidth(), this.rectangle.getHeight());
        if (this.rectangle.getUpperLeft().getX() <= this.movementLine.start().getX()) {
            Point edge = new Point(borderBlockSize, this.rectangle.getUpperLeft().getY());
            this.rectangle = new Rectangle(edge, this.rectangle.getWidth(), this.rectangle.getHeight());
        }
    }

    /**
     * .
     * function name: moveRight
     * Function Operation: move the paddle to the right (10 pixels every time) until it reaches the right edge
     * (most right block)
     */
    public void moveRight() {
        double speed = this.paddleSpeed * this.dt;
        Point p = new Point(this.rectangle.getUpperLeft().getX() + speed,
                this.rectangle.getUpperLeft().getY());
        this.rectangle = new Rectangle(p, this.rectangle.getWidth(),
                this.rectangle.getHeight());
        if (this.rectangle.getUpperLeft().getX()
                + this.rectangle.getWidth() >= this.movementLine.end().getX()) {
            Point edge = new Point(screenWidth - borderBlockSize - this.rectangle.getWidth(),
                    this.rectangle.getUpperLeft().getY());
            this.rectangle = new Rectangle(edge, this.rectangle.getWidth(), this.rectangle.getHeight());
        }
    }

    /**
     * sets the dt member of this paddle.
     *
     * @param fps - frames per second
     */
    public void setDt(Double fps) {
        this.dt = fps;
    }

    /**
     * function name: timePassed
     * Function Operation: Sprite's interface method, notify the paddle that time passed and check if left/right
     * key (on keyboard) was pressed, if yes - order the paddle to move to its new location.
     *
     * @param fps - frames per second
     */
    public void timePassed(double fps) {
        this.setDt(fps);
        if (this.keyboard.isPressed(keyboard.LEFT_KEY)) {
            moveLeft();
        }
        if (this.keyboard.isPressed(keyboard.RIGHT_KEY)) {
            moveRight();
        }
    }

    /**
     * function name: drawOn
     * Function Operation: Sprite's interface method, notify the paddle that it needs to be drawn on the
     * given DrawSurface. the paddle has its own size (a rectangle) and a color.
     *
     * @param d - the given draw surface on which the paddle should be drawn
     */
    public void drawOn(DrawSurface d) {
        int i1 = (int) this.rectangle.getUpperLeft().getX();
        int i2 = (int) this.rectangle.getUpperLeft().getY();
        int i3 = (int) this.rectangle.getWidth();
        int i4 = (int) this.rectangle.getHeight();
        d.setColor(this.color);
        d.fillRectangle(i1, i2, i3, i4);
        d.setColor(Color.BLACK);
        d.drawRectangle(i1, i2, i3, i4); //draw black borders around the filled rectangle
    }

    /**
     * function name: getCollisionRectangle
     * Function Operation: getter for this paddle's rectangle.
     *
     * @return this.rectangle - its member
     */
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    /**
     * .
     * function name: hit
     * Function Operation: Collidable's interface method. activated with a collision between the paddle and a ball
     * was found. the paddle being divided to 5 parts: every hit change the ball's velocity according to the
     * hitting spot.
     *
     * @param collisionPoint  - the collision point the two lines
     * @param hitter          - the Ball the hit the Paddle
     */
    public void hit(Ball hitter, Point collisionPoint) {
        this.isPaddleHit = true;
    }

    /**
     * returns this isPaddleHit member.
     * @return - Boolean type - true or false.
     */
    public Boolean getIsPaddleHit() {
        return this.isPaddleHit;
    }

    /**
     * .
     * function name: addToGame
     * Function Operation: add the paddle to the game: to the sprites collection and to the sprites
     * collection
     *
     * @param g - the game object that owns the sprites and collidables collection
     */
    public void addToGame(GameLevel g) {
        this.keyboard = g.getGui().getKeyboardSensor();
        g.addSprite(this);
        g.addCollidable(this);
        this.movementLine = new Line(new Point(borderBlockSize, this.rectangle.getUpperLeft().getY()),
                new Point(screenWidth - borderBlockSize, this.rectangle.getUpperLeft().getY()));
    }

    /**
     * .
     * function name: moveToMiddle
     * Function Operation:sets the paddle to the starting point in the middle
     * of the screen
     **/
    public void moveToMiddle() {
        this.rectangle = new Rectangle(
                new Point(400 - this.rectangle.getWidth() / 2, 570),
                this.rectangle.getWidth(), 20);
    }
}