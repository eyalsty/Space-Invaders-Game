package sprites;

import animation.GameLevel;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;
import interfaces.Collidable;
import interfaces.HitNotifier;
import interfaces.Sprite;

import java.awt.Image;

/**
 * Author: Eyal Styskin
 * a class of single Alien Object that extends the Block.
 * the Alien can move and shoot (in formation) and needs to be destroyed
 */

public class Alien extends Block implements Collidable, Sprite, HitNotifier {
    static final int DEFAULT_WIDTH = 40;
    static final int DEFAULT_HEIGHT = 30;
    private double originalStartX;
    private double originalStartY;
    private Image image;
    private int column;

    /**
     * Constructor.
     *
     * @param rectangle - the rectangle of the Alien (inherits from super class)
     * @param image     - the image of thr alien
     * @param column    - the column, that the alien located at
     */
    public Alien(Rectangle rectangle, Image image, int column) {
        super(rectangle);
        this.originalStartX = rectangle.getUpperLeft().getX();
        this.originalStartY = rectangle.getUpperLeft().getY();
        this.image = image;
        this.column = column;
    }

    /**
     * draws the alien on the scrren according to its rectangle and its image.
     *
     * @param d - the surface to draw on
     */
    @Override
    public void drawOn(DrawSurface d) {
        int xPos = (int) super.getCollisionRectangle().getUpperLeft().getX();
        int yPos = (int) super.getCollisionRectangle().getUpperLeft().getY();
        d.drawImage(xPos, yPos, this.image);
    }

    /**
     * returns the alien to its original starting position.
     */
    public void resetPlace() {
        super.setRectangle(new Rectangle(new Point(this.originalStartX,
                this.originalStartY), DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    /**
     * moves the alien on X scale, left and right according to the dx received.
     *
     * @param dx - double type moving progress (from Swarm)
     */
    public void moveByX(double dx) {
        double xPos = super.getCollisionRectangle().getUpperLeft().getX() + dx; //moves in x
        double yPos = super.getCollisionRectangle().getUpperLeft().getY(); // doesn't move in y
        super.setRectangle(new Rectangle(new Point(xPos, yPos), super.getCollisionRectangle().getWidth(),
                super.getCollisionRectangle().getHeight()));
    }

    /**
     * moves the alien one step down when hits the borders.
     *
     * @param dy - how much to move down (from Swarm)
     */
    public void moveDown(double dy) {
        double xPos = super.getCollisionRectangle().getUpperLeft().getX(); //moves in x
        double yPos = super.getCollisionRectangle().getUpperLeft().getY() + dy; // doesn't move in y
        super.setRectangle(new Rectangle(new Point(xPos, yPos), super.getCollisionRectangle().getWidth(),
                super.getCollisionRectangle().getHeight()));
    }

    /**
     * check if the alien inside the screen and did not reach the left or right borders.
     *
     * @return - true - if this alien reached the border, false - if not
     */
    public Boolean checkBoundX() {
        if (super.getCollisionRectangle().getUpperLeft().getX() + DEFAULT_WIDTH >= 775
                || super.getCollisionRectangle().getUpperLeft().getX() <= 25) {
            return true;
        }
        return false;
    }

    /**
     * removes the Alien from the game (he was hit).
     *
     * @param game - current game to remove the block from
     */
    @Override
    public void removeFromGame(GameLevel game) {
        super.removeFromGame(game);
        game.getSwarm().removeFromSwarm(this, this.column);
        game.getSwarm().getAliensNum().decrease(1);
    }

    /**
     * check in the alien reached the lowest point (to the shields).
     *
     * @return - true - if reached shields, false - if not reached yet
     */
    public Boolean checkBoundY() {
        if (super.getCollisionRectangle().getUpperLeft().getY() + DEFAULT_HEIGHT >= 500) {
            return true;
        }
        return false;
    }
}
