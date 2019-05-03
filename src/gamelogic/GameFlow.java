package gamelogic;


import animation.HighScoresAnimation;
import animation.AnimationRunner;
import animation.EndScreen;
import animation.GameLevel;
import animation.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import others.Counter;
import sprites.LivesIndicator;
import sprites.ScoreIndicator;


import java.io.File;
import java.io.IOException;


/**
 * author: Eyal Styskin
 * Class name: GameFlow
 * class operation: class in charge of the GameFlow.
 * moving from one level to another, and remembers the score and number of lives.
 */
public class GameFlow {
    private AnimationRunner animationRunner;
    private KeyboardSensor keyboardSensor;
    private HighScoresTable highScoresTable;
    private boolean isWon = false;
    private boolean isToAdd = false;

    /**
     * .
     * Function Operation: Constructor- sets the members of the class
     *
     * @param ar - KeyboardSensor Type
     * @param ks - the AnimationRunner from main class
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks) {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.highScoresTable = new HighScoresTable(5);
    }

    /**
     * Function Name:runLevels
     * Function Operation: creates new Counters for the score and for the number
     * of lives. then runs every level.
     **/

    public void runLevels() {
        Counter score = new Counter(); //score
        ScoreIndicator scoreIndicator = new ScoreIndicator(score);
        Counter numOfLives = new Counter(); //lives
        LivesIndicator livesIndicator = new LivesIndicator(numOfLives);
        numOfLives.increase(3);
        int levelNum = 0;
        while (true) { //run levels
            levelNum++;
            GameLevel level = new GameLevel(this.keyboardSensor,
                    this.animationRunner, numOfLives, score, levelNum);

            scoreIndicator.addToGame(level);
            livesIndicator.addToGame(level);
            level.initialize();
            //stop logic
            while (level.getSwarm().getAliensNum().getValue() != 0 && level.getNumOfLives().getValue() != 0) {
                level.playOneTurn();
            }
            if (level.getNumOfLives().getValue() == 0) {
                break;
            }
        }


        File file = new File("highscores.txt");
        if (!file.exists()) {
            this.isToAdd = true;
        } else {
            this.highScoresTable = HighScoresTable.loadFromFile(file);
            if (this.highScoresTable.getRank(score.getValue())
                    <= this.highScoresTable.getTableCapacity()) {
                this.isToAdd = true;
            } else {
                this.isToAdd = false;
            }
        }
        if (isToAdd) {
            DialogManager dialog = this.animationRunner.getGui().getDialogManager();
            String name = dialog.showQuestionDialog("Name", "What is your name?", "");
            int maxLengthName = 12;
            if (name.length() > maxLengthName) {
                name = name.substring(0, 12);
            }
            this.highScoresTable.add(new ScoreInfo(name, score.getValue()));
            try {
                this.highScoresTable.save(file);
            } catch (IOException e) {
                System.out.println("Failed saving file");
            }
        }
        if (this.isWon) {
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new EndScreen(true, score)));
        } else {
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new EndScreen(false, score)));
        }
        this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                "space", new HighScoresAnimation(this.highScoresTable)));
    }
}
