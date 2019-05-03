package sprites;

import animation.GameLevel;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;
import interfaces.Collidable;
import interfaces.HitListener;
import interfaces.HitNotifier;
import interfaces.Sprite;
import levels.BlockFiller;
import java.util.ArrayList;
import java.awt.Color;
import java.util.List;
import java.util.Map;


/**
 * .
 * author: Eyal Styskin
 * Class name:Block
 * class operation: the class "Block" implements the collidable and Sprite interfaces and has its methods.
 * the created block object is a Rectangle that has color, row number and a hitcount(how many times it needs to
 * be hit).
 * the block has the ability to change the direction (and velocity) of the ball that hits him, and to be drawn on the
 * given surface.
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private ArrayList<HitListener> hitListeners = new ArrayList<HitListener>();
    private Rectangle rectangle;
    private int hitPoints;
    private Map<Integer, BlockFiller> colorsMap;
    private Color stroke;

    /**
     * Function Operation: constructor - creates new block from the rectangle class and sets its color.
     *
     * @param rectangle - Rectangle type rectangle to create the block
     */
    public Block(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Setting the stroke member of this block.
     * if not stroke its null
     *
     * @param strokeColor - the Color of the Stroke
     */
    public void setStroke(Color strokeColor) {
        this.stroke = strokeColor;
    }

    /**
     * sets this rectangle member.
     * @param rectangle1 - Rectangle type
     */
    public void setRectangle(Rectangle rectangle1) {
        this.rectangle = rectangle1;
    }

    /**
     * sets the colorsMap member of this block.
     *
     * @param colors - map of integers (hitpoints) and Colors
     */
    public void setColorsMap(Map<Integer, BlockFiller> colors) {
        this.colorsMap = colors;
    }

    /**
     * .
     * function name: setHitPoints
     * Function Operation: set the hitPoints ("lives) member of this block
     *
     * @param newHitPoints - Integer with number of hit points block has
     */
    public void setHitPoints(int newHitPoints) {
        this.hitPoints = newHitPoints;
    }

    /**
     * .
     * function name: getCollisionRectangle
     * Function Operation: getter for this rectangle
     *
     * @return this.rectangle - the member
     */
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    /**
     * .
     * function name: addHitListener
     * Function Operation: adds a new HitListener to the list of hitListeners that
     * this block has
     *
     * @param hl - new HitListener
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * .
     * function name: removeHitListener
     * Function Operation: remove the given hitListener from the hitListeners list
     * of this block.
     *
     * @param hl - HitListener to be removed
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * .
     * function name: notifyHit
     * Function Operation: update the HitListeners list and call all the hitEvent methods
     * of the hitListeners of this block
     *
     * @param hitter - the ball that hit the block
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners); // update current list
        for (HitListener hl : listeners) {  // Notify all listeners about a hit event
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * .
     * function name: hit
     * Function Operation: this method gets the collision Point and the current Velocity of the ball.
     * it checks the location of the collision according to this block (rectangle) and according to that,
     * the velocity of the ball is being changed
     *
     * @param collisionPoint  - the Point where the collision happened
     * @param hitter          - the ball that hits the block
     */
    public void hit(Ball hitter, Point collisionPoint) {
        this.hitPoints--;
        this.notifyHit(hitter);
    }

    /**
     * .
     * function name: drawOn
     * Function Operation: sprite's interface method. this method draws the block (a rectangle) on the given
     * drawsurface according to its color member. also, the method draws the number of "life" the block has,
     * when it reaches 0 it turns to 'X'.
     *
     * @param surface - a given draw surface on which we draw the block and the game
     */

    public void drawOn(DrawSurface surface) {
        int i1 = (int) this.rectangle.getUpperLeft().getX();
        int i2 = (int) this.rectangle.getUpperLeft().getY();
        int i3 = (int) this.rectangle.getWidth();
        int i4 = (int) this.rectangle.getHeight();
        int hits = this.hitPoints;
        if (this.colorsMap.containsKey(hits)) {
            this.colorsMap.get(hits).drawBlock(surface, this.rectangle);
        } else {
            this.colorsMap.get(1).drawBlock(surface, this.rectangle);
            BlockFiller filler = (BlockFiller) this.colorsMap.values().toArray()[0];
            filler.drawBlock(surface, this.rectangle);
        }
        if (this.stroke != null) {
            surface.setColor(this.stroke);
            surface.drawRectangle(i1, i2, i3, i4); //draw borders around the filled rectangle
        }
    }

    /**
     * .
     * function name: addToGame
     * Function Operation: add the block to the game: to the sprites collection and to the collidables
     * collection
     *
     * @param g - the game object that owns the sprites and collidables collection
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * .
     * function name: removeFromGame
     * Function Operation: remove this block from the sprites and collidables
     * in the current game
     *
     * @param game - - current game to remove the block from
     */
    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }


    /**
     * function name: timePassed
     * Function Operation:sprite's interface method, currently does nothing.
     * @param dt - does nothing
     */
    public void timePassed(double dt) {
    }
}
