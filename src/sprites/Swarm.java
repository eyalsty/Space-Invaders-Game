package sprites;

import animation.GameLevel;
import geometry.Point;
import geometry.Rectangle;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import others.Counter;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Eyal Styskin.
 * the Class contains the information (and also creates) about the swarm of the aliens
 */
public class Swarm {
    private List<List<Alien>> aliensSwarm;
    private GameLevel gameLevel;
    private Counter aliensNum;
    private ScoreTrackingListener scoreTrackingListener;
    private double dx;
    private double originalDx;
    private Boolean isLost;

    /**
     * Constructor.
     *
     * @param score     Counter type score of the game
     * @param gameLevel - the GameLevel played this swarm
     * @param dx        - movement path (double type)
     */
    public Swarm(Counter score, GameLevel gameLevel, double dx) {
        this.gameLevel = gameLevel;
        this.aliensNum = new Counter();
        this.scoreTrackingListener = new ScoreTrackingListener(score);
        this.aliensSwarm = createSwarm();
        this.isLost = false;
        this.dx = dx;
        this.originalDx = dx;
    }

    /**
     * resets the formation of the Aliens, moves every alien to its starting position.
     * also resets the speed of the Swarm and the isLost status.
     */
    public void resetFormation() {
        this.resetSpeed();
        this.isLost = false;
        for (List<Alien> aliens : this.aliensSwarm) {
            for (Alien alien : aliens) {
                alien.resetPlace();
            }
        }
    }

    /**
     * getter for this Alien matrix.
     *
     * @return - List of lists with Alien objects
     */
    public List<List<Alien>> getAliensSwarm() {
        return aliensSwarm;
    }

    /**
     * resets the speed of this swarm to its original.
     */
    public void resetSpeed() {
        this.dx = this.originalDx;
    }

    /**
     * returns this aliens number counter.
     *
     * @return - Counter type holds the number of the aliens
     */
    public Counter getAliensNum() {
        return this.aliensNum;
    }

    /**
     * creates a new Swarm of aliens in a matrix formation of 5 rows and 10 columns,
     * sets its images, listeners and adds all to the current game played.
     *
     * @return - the new matrix - List of lists with Aliens type aliens
     */

    public List<List<Alien>> createSwarm() {
        List<List<Alien>> swarm = new ArrayList<List<Alien>>();
        try {
            File file = new File("blabla");

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("enemy.png");
            Image image = ImageIO.read(is);
            BlockRemover blockRemover = new BlockRemover(this.gameLevel);
            BallRemover ballRemover = new BallRemover(this.gameLevel);
            for (int column = 0; column < 10; ++column) {
                List<Alien> aliensColumn = new ArrayList<Alien>();
                for (int row = 0; row < 5; ++row) {
                    Alien alien = new Alien(new Rectangle(new Point(50 + column * 50, 50 + row * 40),
                            40, 30), image, column);
                    alien.addHitListener(blockRemover);
                    alien.addHitListener(ballRemover);
                    alien.addHitListener(this.scoreTrackingListener);

                    alien.addToGame(this.gameLevel);
                    aliensColumn.add(alien);
                    this.aliensNum.increase(1);
                }
                swarm.add(aliensColumn);
            }
        } catch (Exception e) {
            System.out.println("Cant load image");
        }
        return swarm;
    }

    /**
     * checks if any alien in the swarm reached the right or left borders of the screen.
     *
     * @return - true if reached the border, else - otherwise
     */
    public Boolean checkBoundsX() {
        for (List<Alien> aliens : aliensSwarm) {
            for (Alien alien : aliens) {
                if (alien.checkBoundX()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * removes specific Alien (after got hit) from the current swarm.
     *
     * @param alien  - Alien type alien that was hit
     * @param column - the alien's column to search from and remove
     */
    public void removeFromSwarm(Alien alien, int column) {
        this.aliensSwarm.get(column).remove(alien);
    }

    /**
     * checks if any alien from the swarm reached the Y position of the shields.
     * @return - true if reached (needs to restart the turn) or false - continue to play
     */
    public Boolean checkBoundsY() {
        for (List<Alien> aliens : aliensSwarm) {
            for (Alien alien : aliens) {
                if (alien.checkBoundY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getter for isLost member.
     * @return - true or false (Boolean)
     */
    public Boolean getIsLost() {
        return this.isLost;
    }

    /**
     * moves the Swarm by one step according to its location.
     */
    public void moveSwarm() {
        if (checkBoundsY()) {
            this.isLost = true;
            return;
        }
        if (checkBoundsX()) {
            for (List<Alien> aliens : aliensSwarm) {
                for (Alien alien : aliens) {
                    alien.moveDown(25.0);
                }
            }
            this.dx = this.dx * (-1) * 11 / 10;
        }
        for (List<Alien> aliens : this.aliensSwarm) {
            for (Alien alien : aliens) {
                alien.moveByX(this.dx);
            }
        }
    }
}

