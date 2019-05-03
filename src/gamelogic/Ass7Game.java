package gamelogic;

import animation.AnimationRunner;
import animation.HighScoresAnimation;
import animation.KeyPressStoppableAnimation;
import animation.MenuAnimation;
import interfaces.Menu;
import interfaces.Task;

import java.io.File;

/**
 * the main class that runs the game.
 */
public class Ass7Game {

    /**
     * main method- creates new AnimationRunner and a menu with three choices: start game, show highscores, and quit.
     * each option has an anonymous class (of type Task) that runs according to the user's choices
     * @param args - not expecting input
     */
    public static void main(String[] args) {
        final AnimationRunner animationRunner = new AnimationRunner();

        Task<Void> startGame = new Task<Void>() {
            @Override
            public Void run() {
                GameFlow gameFlow = new GameFlow(animationRunner,
                        animationRunner.getGui().getKeyboardSensor());

                gameFlow.runLevels();
                return null;
            }
        };


        Task<Void> highScores = new Task<Void>() {
            public Void run() {
                File file = new File("highscores.txt");
                HighScoresTable table = null;
                if (file.exists()) {
                    table = HighScoresTable.loadFromFile(file);
                } else {
                    table = new HighScoresTable(5);
                }
                animationRunner.run(new KeyPressStoppableAnimation(animationRunner.getGui().getKeyboardSensor(),
                        "space", new HighScoresAnimation(table)));
                return null;
            }
        };


        Task<Void> quit = new Task<Void>() {
            public Void run() {
                animationRunner.getGui().close();
                return null;
            }
        };

        Menu<Task<Void>> mainMenu = new MenuAnimation<>("Arkanoid",
                animationRunner.getGui().getKeyboardSensor(), animationRunner);
        mainMenu.addSelection("s", "start new game", startGame);
        mainMenu.addSelection("h", "show highscores", highScores);
        mainMenu.addSelection("q", "quit", quit);
        while (true) {
            animationRunner.run(mainMenu);
            // wait for user selection
            Task<Void> task = mainMenu.getStatus();
            task.run();
            mainMenu.clearStatus();
        }
    }
}
