# Task 2 - Number Guessing Game

A console game where the computer picks a random number and the player guesses it using Higher / Lower hints.

**Tech stack:** Java 11+ (`java.util.Random`, `Scanner`, `while` loops, `if-else`)

## Features
- Random number generated at the start of every round
- Hints after each guess: **Too High!**, **Too Low!** or **Correct!**
- Attempt counter shown throughout ("Attempt 3 of 7", "Attempts left")
- Attempt limit: **You Lost!** message that reveals the number
- **Play Again** prompt after each round (y/n)
- Score tracking across rounds: "Round X - guessed in Y attempts", plus a final summary with wins, losses and best round
- **Difficulty levels (bonus):**

| Level | Range | Attempts |
|-------|-------|----------|
| Easy | 1 - 50 | 10 |
| Medium | 1 - 100 | 7 |
| Hard | 1 - 200 | 5 |

Invalid input (letters, or a number outside the range) is rejected and does not use up an attempt.

## How to run
- **VS Code:** open the `Task2-Number-Guessing-Game` folder, open `src/NumberGuessingGame.java` and click **Run** above `main`. Type your guesses in the terminal panel.
- **Terminal (JDK needed):**
  ```
  javac -d out src/NumberGuessingGame.java
  java -cp out NumberGuessingGame
  ```

## Code structure (`src/NumberGuessingGame.java`)
- `main` runs the round loop and asks to play again
- `chooseLevel()` shows the difficulty menu
- `playRound()` generates the number, reads guesses, gives hints and counts attempts
- `Level` holds the range and attempts; `RoundResult` stores each round for the summary
- `printRoundSummary()` and `printFinalSummary()` show the scores

## Sample run
```
Choose a difficulty:
  1. Easy (1-50, 10 attempts)
  2. Medium (1-100, 7 attempts)
  3. Hard (1-200, 5 attempts)
Enter 1, 2 or 3: 2

Round 1 (Medium): I'm thinking of a number from 1 to 100.
You have 7 attempts.

Attempt 1 of 7 - enter your guess: 50
Too Low!
Attempts left: 6

Attempt 2 of 7 - enter your guess: 75
Too High!
Attempts left: 5

Attempt 3 of 7 - enter your guess: 62
Correct!

Round 1 - guessed in 3 attempts

Play again? (y/n): n

===== Score summary =====
Round 1 (Medium) - guessed in 3 attempts
Rounds played: 1 | Won: 1 | Lost: 0
Best round: 3 attempts
```
