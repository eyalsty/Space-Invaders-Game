package interfaces;

import geometry.Point;
import geometry.Rectangle;
import sprites.Ball;

/**
 * .
 * author: Eyal Styskin
 * Interface name:Collidable
 * Interface operation: Interface of objects that a ball can collide with.
 * its methods can return the rectangle that the ball collided with, and
 * can change the ball's velocity.
 */

public interface Collidable {

    /**
     * .
     * function name: getCollisionRectangle
     * Function Operation: getter for the Rectangle the coliision happened with
     *
     * @return Rectangle
     */
    Rectangle getCollisionRectangle();

    /**
     * function name: hit
     * Function Operation: change the velocity according to the collision point
     * location.
     *
     * @param collisionPoint - Point type (x,y) of the collision
     * @param hitter         - the Ball hits the collidable object
     */
    void hit(Ball hitter, Point collisionPoint);
}

