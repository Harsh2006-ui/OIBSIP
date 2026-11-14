import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Number Guessing Game (console version).
 * Features: random number, Too High / Too Low / Correct hints, attempt counter,
 * attempt limit, play again, score tracking across rounds, and difficulty levels.
 */
public class NumberGuessingGame {

    private static final Scanner INPUT = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    /** A difficulty level: number range 1..max and the attempts allowed. */
    private static class Level {
        final String name;
        final int max;
        final int maxAttempts;

        Level(String name, int max, int maxAttempts) {
            this.name = name;
            this.max = max;
            this.maxAttempts = maxAttempts;
        }
    }

    /** The outcome of one finished round, kept for the score summary. */
    private static class RoundResult {
        final int round;
        final String level;
        final boolean won;
        final int attempts;
        final int secret;

        RoundResult(int round, String level, boolean won, int attempts, int secret) {
            this.round = round;
            this.level = level;
            this.won = won;
            this.attempts = attempts;
            this.secret = secret;
        }
    }

    private static final Level[] LEVELS = {
        new Level("Easy", 50, 10),
        new Level("Medium", 100, 7),
        new Level("Hard", 200, 5)
    };

    public static void main(String[] args) {
        System.out.println("=== Number Guessing Game ===");
        List<RoundResult> results = new ArrayList<>();

        boolean playAgain = true;
        while (playAgain) {
            Level level = chooseLevel();
            if (level == null) {
                break; // input ended
            }
            RoundResult result = playRound(results.size() + 1, level);
            if (result == null) {
                break; // input ended
            }
            results.add(result);
            printRoundSummary(result);
            playAgain = askYesNo("\nPlay again? (y/n): ");
        }

        printFinalSummary(results);
        System.out.println("Thanks for playing!");
    }

    /** Reads one trimmed line, or returns null if there is no more input. */
    private static String readLine() {
        if (!INPUT.hasNextLine()) {
            return null;
        }
        return INPUT.nextLine().trim();
    }

    private static Level chooseLevel() {
        while (true) {
            System.out.println("\nChoose a difficulty:");
            for (int i = 0; i < LEVELS.length; i++) {
                Level l = LEVELS[i];
                System.out.printf("  %d. %s (1-%d, %d attempts)%n", i + 1, l.name, l.max, l.maxAttempts);
            }
            System.out.print("Enter 1, 2 or 3: ");
            String line = readLine();
            if (line == null) {
                return null;
            }
            if (line.equals("1") || line.equals("2") || line.equals("3")) {
                return LEVELS[Integer.parseInt(line) - 1];
            }
            System.out.println("Invalid choice. Please enter 1, 2 or 3.");
        }
    }

    /** Plays one round. Returns the result, or null if the input ended. */
    private static RoundResult playRound(int round, Level level) {
        int secret = RANDOM.nextInt(level.max) + 1;
        int attempts = 0;

        System.out.printf("%nRound %d (%s): I'm thinking of a number from 1 to %d.%n",
                round, level.name, level.max);
        System.out.printf("You have %d attempts.%n", level.maxAttempts);

        while (attempts < level.maxAttempts) {
            System.out.printf("%nAttempt %d of %d - enter your guess: ", attempts + 1, level.maxAttempts);
            String line = readLine();
            if (line == null) {
                return null;
            }

            int guess;
            try {
                guess = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number (this did not use an attempt).");
                continue;
            }
            if (guess < 1 || guess > level.max) {
                System.out.printf("Your guess must be between 1 and %d (this did not use an attempt).%n", level.max);
                continue;
            }

            attempts++;
            if (guess == secret) {
                System.out.println("Correct!");
                return new RoundResult(round, level.name, true, attempts, secret);
            }
            System.out.println(guess > secret ? "Too High!" : "Too Low!");
            int left = level.maxAttempts - attempts;
            if (left > 0) {
                System.out.println("Attempts left: " + left);
            }
        }

        System.out.println("You Lost! The number was " + secret + ".");
        return new RoundResult(round, level.name, false, attempts, secret);
    }

    private static void printRoundSummary(RoundResult r) {
        if (r.won) {
            System.out.printf("%nRound %d - guessed in %d attempt%s%n",
                    r.round, r.attempts, r.attempts == 1 ? "" : "s");
        } else {
            System.out.printf("%nRound %d - not guessed (the number was %d)%n", r.round, r.secret);
        }
    }

    private static boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            if (line == null) {
                return false;
            }
            if (line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes")) {
                return true;
            }
            if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Please type y or n.");
        }
    }

    private static void printFinalSummary(List<RoundResult> results) {
        if (results.isEmpty()) {
            return;
        }
        System.out.println("\n===== Score summary =====");
        int wins = 0;
        int best = Integer.MAX_VALUE;
        for (RoundResult r : results) {
            if (r.won) {
                wins++;
                best = Math.min(best, r.attempts);
                System.out.printf("Round %d (%s) - guessed in %d attempt%s%n",
                        r.round, r.level, r.attempts, r.attempts == 1 ? "" : "s");
            } else {
                System.out.printf("Round %d (%s) - lost, number was %d%n", r.round, r.level, r.secret);
            }
        }
        System.out.printf("Rounds played: %d | Won: %d | Lost: %d%n",
                results.size(), wins, results.size() - wins);
        if (wins > 0) {
            System.out.println("Best round: " + best + " attempt" + (best == 1 ? "" : "s"));
        }
    }
}
