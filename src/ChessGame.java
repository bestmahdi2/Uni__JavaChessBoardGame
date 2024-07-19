import java.util.ArrayList; // arraylist
import java.util.Arrays; // Array
import java.util.Locale; // for lowercase
import java.util.Scanner; // scanner

public class ChessGame {
    // scanner
    Scanner scanner = new Scanner(System.in);

    // objects from System, User and Utilities
    Sys system = new Sys();
    User user = new User();
    Utilities U = new Utilities();

    // number of rows and columns of the matrix
    int rowsOfMatrix = 8;

    // winner name keeper
    Player winner = null;

    // selected piece x, y
    int[] selectedPiece = null;

    // integer to keep bare moves
    int moves = 0;

    // main matrix
    String[][] matrix = new String[rowsOfMatrix][rowsOfMatrix];
    // matrix with marks
    String[][] markedMatrix = new String[rowsOfMatrix][rowsOfMatrix];
    // matrix to save values before move
    String[][] backupMatrix = new String[rowsOfMatrix][rowsOfMatrix];


    public static void main(String[] args) {
        // welcome message
        System.out.printf("%s****************************************\n", Color.PURPLE_BOLD_BRIGHT);
        System.out.printf("|%-38s|\n", Utilities.center("Chess Game", 38, ' '));
        System.out.printf("****************************************%s\n", Color.RESET);
        System.out.println();

        ChessGame game = new ChessGame();
        game.firstFillMatrix();
        game.showMenu();
    }

    // method to fill the main matrix
    private void firstFillMatrix() {
        for (int i = 0; i < rowsOfMatrix; i++) {
            for (int j = 0; j < rowsOfMatrix; j++) {
                // last two columns
                if (j > rowsOfMatrix - 3)
                    matrix[i][j] = "+";

                else if (j < 2) // first two columns
                    matrix[i][j] = "*";

                else // other squares
                    matrix[i][j] = " ";
            }
        }

        // copy the main matrix to marked matrix
        markedMatrix = U.copy(matrix);
    }

    // method to show the main menu to user
    private void showMenu() {
        System.out.printf("%s\n***> Please select an option below:\n", Color.YELLOW_BOLD_BRIGHT);
        System.out.print(" 1. Play Game\n 2. Exit\n> " + Color.RESET);

        String input = scanner.next();

        switch (input) {
            case "1": // play the game
                play();
                showMenu();
                break;

            case "2": // exit
                System.out.printf("%s\n*****> Have a nice day ! Exiting ... ! <*****\n", Color.GREEN_BOLD_BRIGHT);
                U.sleep(1);
                break;

            default: // wrong command
                System.out.printf("%s**> You entered wrong command !\n%s", Color.RED_BOLD_BRIGHT, Color.RESET);
                showMenu();
        }
    }

    // play method game
    private void play() {
        // getting the side of the user
        findSide();

        while (true) {
            // copy the main matrix to marked matrix
            markedMatrix = U.copy(matrix);

            // play the user and check if game is ended or not
            userPlay();
            U.sleep(3);
            if (isGameEnded()) break;

            // play the system and check if end is ended or not
            systemPlay();
            if (isGameEnded()) break;
        }

        // if there is a winner
        showWinner();

        System.out.printf("%s\n***> Would you like to play again ? [Y/N]\n> %s", Color.YELLOW_BOLD_BRIGHT, Color.RESET);
        String input = scanner.next();

        if (input.toLowerCase(Locale.ROOT).equals("y"))
            play(); // play again
    }

    // undo the movement or hit
    private void undoMoveHit() {
        printMainBoard(0, 0);

        System.out.printf("%s***> Continue or undo ?\n", Color.YELLOW_BOLD_BRIGHT);
        System.out.print(" 1. Continue\n 2. Undo\n> " + Color.RESET);

        String input = scanner.next();
        if (input.equals("2")) {
            if (moves > 0) moves--;
            System.out.printf("\n%s**> Undo successfully !\n\n%s", Color.GREEN_BOLD_BRIGHT, Color.RESET);
            matrix = U.copy(backupMatrix);
            userPlay();
        }
    }

    // method to find side and piece type of the user and system
    private void findSide() {
        System.out.printf("%s\n***> Please select a side for play:\n", Color.YELLOW_BOLD_BRIGHT);
        System.out.print(" 1. Left\n 2. Right\n> " + Color.RESET);

        String input = scanner.next();

        if (!input.equals("1") && !input.equals("2")) { // if not selected 1 and 2
            System.out.printf("%s**> You entered wrong command !\n%s", Color.RED_BOLD_BRIGHT, Color.RESET);
            findSide();
            return;
        }

        if (input.equals("1")) {
            user.setSide("left");
            user.setPieceType("\\*");

            system.setSide("right");
            system.setPieceType("\\+");

        } else {
            user.setSide("right");
            user.setPieceType("\\+");

            system.setSide("left");
            system.setPieceType("\\*");
        }
    }

    // method to show the turn of current player
    private void showTurn(Player player) {
        U.clearScreen(0, 0);

        System.out.printf("%s________________________________________\n", Color.PURPLE_BOLD_BRIGHT);
        System.out.print(String.format("|%-38s|\n", " ").repeat(4));

        if (player instanceof User)
            System.out.printf("|%-38s|\n", Utilities.center("User Turn", 38, ' '));

        else
            System.out.printf("|%-38s|\n", Utilities.center("System Turn", 38, ' '));

        System.out.print(String.format("|%-38s|\n", " ").repeat(4));
        System.out.printf("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾%s\n", Color.RESET);

        U.clearScreen(2, 0);
    }

    // method to show the winner
    private void showWinner() {
        U.clearScreen(0, 0);

        if (winner instanceof User) {
            System.out.printf("%s________________________________________\n", Color.GREEN_BOLD_BRIGHT);
            System.out.print(String.format("|%-38s|\n", " ").repeat(4));
            System.out.printf("|%-38s|", Utilities.center("You win !", 38, ' '));
            System.out.print(String.format("|%-38s|\n", " ").repeat(4));

        } else if (winner instanceof Sys) {
            System.out.printf("%s________________________________________\n", Color.RED_BOLD_BRIGHT);
            System.out.print(String.format("|%-38s|\n", " ").repeat(4));
            System.out.printf("|%-38s|", Utilities.center("You loose !", 38, ' '));
            System.out.print(String.format("|%-38s|\n", " ").repeat(4));

        } else {
            System.out.printf("%s________________________________________\n", Color.CYAN_BOLD_BRIGHT);
            System.out.print(String.format("|%-38s|\n", " ").repeat(4));
            System.out.printf("|%-38s|", Utilities.center("The match ended in a draw !", 38, ' '));
            System.out.print(String.format("|%-38s|\n", " ").repeat(4));
        }

        System.out.printf("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾%s\n", Color.RESET);
        U.sleep(2);
    }

    // method to find out if end of game reached or not
    private boolean isGameEnded() {
        if (moves == 10 || findScore(user) == rowsOfMatrix * 2 || findScore(user) == rowsOfMatrix * 2)
            findWinner(); // find the winner

        return (winner != null);
    }

    // method to find the winner of the game
    private void findWinner() {
        if (findScore(user) > findScore(user)) // user is winner
            winner = user;

        else if (findScore(user) < findScore(user)) // system is winner
            winner = system;

        else // draw
            winner = null;
    }

    // method to mark all moves and hits
    private void markMatrix(Player player) {
        int i = selectedPiece[0];
        int j = selectedPiece[1];

        // distinguish the selected piece
        if (!markedMatrix[i][j].contains("*"))
            markedMatrix[i][j] = "*" + markedMatrix[i][j];

        // add available moves
        if (player.getSide().equals("left")) {
            // | * |   |
            // |   |   |
            // |   | b |
            if (isValidMove(i + 2, j + 1) && isFilled(" ", i + 2, j + 1)) // (if the move is movable in matrix) && (if the selected squares is empty)
                markedMatrix[i + 2][j + 1] = "b";

            // |   | t |
            // |   |   |
            // | * |   |
            if (isValidMove(i - 2, j + 1) && isFilled(" ", i - 2, j + 1))
                markedMatrix[i - 2][j + 1] = "t";

            // | * |   |   |
            // |   |   | l |
            if (isValidMove(i + 1, j + 2) && isFilled(" ", i + 1, j + 2))
                markedMatrix[i + 1][j + 2] = "l";

            // |   |   | r |
            // | * |   |   |
            if (isValidMove(i - 1, j + 2) && isFilled(" ", i - 1, j + 2))
                markedMatrix[i - 1][j + 2] = "r";

        } else {
            if (isValidMove(i - 2, j - 1) && isFilled(" ", i - 2, j - 1))
                markedMatrix[i - 2][j - 1] = "t";

            if (isValidMove(i + 2, j - 1) && isFilled(" ", i + 2, j - 1))
                markedMatrix[i + 2][j - 1] = "b";

            if (isValidMove(i - 1, j - 2) && isFilled(" ", i - 1, j - 2))
                markedMatrix[i - 1][j - 2] = "r";

            if (isValidMove(i + 1, j - 2) && isFilled(" ", i + 1, j - 2))
                markedMatrix[i + 1][j - 2] = "l";
        }

        // add available hit positions
        if (player.getSide().equals("left")) {
            // |   | ~ |
            // | * |   |
            // |   |   |
            if (isValidMove(i - 1, j + 1) && isFilled("+", i - 1, j + 1)) // (if the move is movable in matrix) && (if the selected squares is filled with competitor)
                markedMatrix[i - 1][j + 1] = "~1";

            // |   |   |
            // | * | ~ |
            // |   |   |
            if (isValidMove(0, j + 1) && isFilled("+", i, j + 1))
                markedMatrix[i][j + 1] = "~2";

            // |   |   |
            // | * |   |
            // |   | ~ |
            if (isValidMove(i + 1, j + 1) && isFilled("+", i + 1, j + 1))
                markedMatrix[i + 1][j + 1] = "~3";

        } else {
            if (isValidMove(i - 1, j - 1) && isFilled("*", i - 1, j - 1))
                markedMatrix[i - 1][j - 1] = "~1";

            if (isValidMove(0, j - 1) && isFilled("*", i, j - 1))
                markedMatrix[i][j - 1] = "~2";

            if (isValidMove(i + 1, j - 1) && isFilled("*", i + 1, j - 1))
                markedMatrix[i + 1][j - 1] = "~3";
        }
    }

    // method to choose a piece for user
    private void choosePieceUser() {
        labelPieces(user.getSide());  // label the pieces of the user
        printMarkedBoard();  // print marked matrix

        // try to find the selected piece
        System.out.printf("%s*> [%sYou%s] Choose a piece > ", Color.BLUE_BOLD_BRIGHT, Color.GREEN_BOLD_BRIGHT, Color.BLUE_BOLD_BRIGHT);
        String input = scanner.next();
        findPiece(input);

        if (selectedPiece == null) { // couldn't find the piece
            System.out.printf("\n%s**> Can't find selected piece !\n%s", Color.RED_BOLD_BRIGHT, Color.RESET);
            choosePieceUser();
        }
    }

    // method to choose a move for user
    private void chooseMoveUser() {
        markMatrix(user);  // mark the matrix
        printMarkedBoard();  // print marked matrix

        // try to find the selected move
        System.out.printf("%s*> [%sYou%s] Choose a move > ", Color.BLUE_BOLD_BRIGHT, Color.GREEN_BOLD_BRIGHT, Color.BLUE_BOLD_BRIGHT);
        String input = scanner.next();

        // if move is hitting
        if ("~1,~2,~3".contains(input)) {
            if (!hitPiece(user.getSide(), input)) { // if couldn't hit ant piece
                System.out.printf("\n%s**> Can't hit piece in this direction !\n%s", Color.RED_BOLD_BRIGHT, Color.RESET);
                labelPieces(user.getSide());
                chooseMoveUser();

            } else {// user can play again if hit a piece
                undoMoveHit();
                userPlay();
            }

        } else if ("t,b,r,l".contains(input)) { // if piece is moving
            if (!movePiece(user.getSide(), input)) {
                System.out.printf("\n%s**> Can't move piece in this direction !\n%s", Color.RED_BOLD_BRIGHT, Color.RESET);
                labelPieces(user.getSide());
                chooseMoveUser();
            } else
                undoMoveHit();

        } else { // wrong direction
            System.out.printf("\n%s**> Wrong direction !\n%s", Color.RED_BOLD_BRIGHT, Color.RESET);
            chooseMoveUser();
        }
    }

    // method to play the user
    private void userPlay() {
        // show user turn
        showTurn(user);

        // print the main matrix
        printMainBoard(0, 1);
        U.clearScreen(0, 0);

        // choose a piece
        choosePieceUser();
        U.clearScreen(0, 0);

        // choose a move
        chooseMoveUser();
        printMainBoard(0, 0);
    }

    // method to play the system
    private void systemPlay() {
        // show system turn
        showTurn(system);

        // choose a piece
        choosePieceSystem();
        U.clearScreen(2, 0);

        // choose a move
        chooseMoveSystem();
        System.out.println(4);
        printMainBoard(2, 3); // print the main matrix
    }

    // method to choose a move for system
    private void choosePieceSystem() {
        labelPieces(system.getSide());  // label the pieces of the user
        printMarkedBoard();  // print matrix with signs

        // find a random piece for system
        System.out.printf("%s*> [%System%s] Choose a piece > ", Color.BLUE_BOLD_BRIGHT, Color.CYAN_BOLD_BRIGHT, Color.BLUE_BOLD_BRIGHT);
        U.sleep(1);
        String input = system.pieces.get((int) (system.pieces.size() * Math.random()));

        // print the selected piece
        System.out.println(input);
        findPiece(input);
    }

    // method to choose a move for system
    private void chooseMoveSystem() {
        markMatrix(system);  // mark the matrix
        printMarkedBoard();  // print matrix with signs

        // try to find the selected move
        System.out.printf("%s*> [%System%s] Choose a move > ", Color.BLUE_BOLD_BRIGHT, Color.CYAN_BOLD_BRIGHT, Color.BLUE_BOLD_BRIGHT);
        U.sleep(1);

        // find all the moves and hits
        system.moves.clear();
        for (int i = 0; i < rowsOfMatrix; i++) {
            for (int j = 0; j < rowsOfMatrix; j++) {
                if ("t,b,r,l,~1,~2,~3".contains(markedMatrix[i][j])) {
                    system.moves.add(markedMatrix[i][j]);
                }
            }
        }

        // find a random move or hit for system
        String input = system.moves.get((int) (system.moves.size() * Math.random()));

        if ("~1,~2,~3".contains(input)) { // if piece is hitting
            System.out.println(input);
            hitPiece(system.getSide(), input);
            systemPlay();

        } else if ("t,b,r,l".contains(input)) { // if piece is moving
            System.out.println(input);
            movePiece(system.getSide(), input);

        } else // if this piece can't do any move or hit
            System.out.println("X");
    }

    // method to print the main matrix board
    private void printMainBoard(int sleepBefore, int sleepAfter) {
        U.sleep(sleepBefore);

        System.out.printf("%s________________________________________\n", Color.CYAN_BOLD_BRIGHT);
        String you = String.format("%sYou: %d%s", Color.GREEN_BOLD_BRIGHT, findScore(user), Color.CYAN_BOLD_BRIGHT);
        String sys = String.format("%sSystem: %d%s", Color.RED_BOLD_BRIGHT, findScore(system), Color.CYAN_BOLD_BRIGHT);
        String move = String.format("Moves: %d", moves);
        System.out.printf("| %-51s|\n", you);
        System.out.printf("| %-51s|\n", sys);
        System.out.printf("| %-37s|\n", move);
        System.out.print("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n");

        System.out.println(Color.YELLOW_BOLD_BRIGHT);

        for (int i = 0; i < rowsOfMatrix; i++) {
            for (int j = 0; j < rowsOfMatrix; j++) {
                System.out.printf("|%-7s", Utilities.center(matrix[i][j], 7, ' '));
                if (j == 3)
                    System.out.print(" |");
            }
            System.out.println("|");
        }
        System.out.println();

        System.out.println(Color.RESET);
        U.sleep(sleepAfter);
    }

    // method to print the marked matrix board
    private void printMarkedBoard() {
        System.out.printf("%s________________________________________\n", Color.CYAN_BOLD_BRIGHT);
        String you = String.format("%sYou: %d%s", Color.GREEN_BOLD_BRIGHT, findScore(user), Color.CYAN_BOLD_BRIGHT);
        String sys = String.format("%sSystem: %d%s", Color.RED_BOLD_BRIGHT, findScore(system), Color.CYAN_BOLD_BRIGHT);
        String move = String.format("Moves: %d", moves);
        System.out.printf("| %-51s|\n", you);
        System.out.printf("| %-51s|\n", sys);
        System.out.printf("| %-37s|\n", move);
        System.out.printf("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾%s\n", Color.RESET);

        System.out.println(Color.YELLOW_BOLD_BRIGHT);

        for (int i = 0; i < rowsOfMatrix; i++) {
            for (int j = 0; j < rowsOfMatrix; j++) {
                System.out.printf("|%-7s", Utilities.center(markedMatrix[i][j], 7, ' '));
                if (j == 3)
                    System.out.print(" |");
            }
            System.out.println("|");
        }
        System.out.println();

        System.out.println(Color.RESET);
    }


    // method to find the piece by its label
    private void findPiece(String input) {
        for (int i = 0; i < rowsOfMatrix; i++) {
            for (int j = 0; j < rowsOfMatrix; j++) {
                if (markedMatrix[i][j].equals(input)) {
                    selectedPiece = new int[]{i, j};
                    return;
                }
            }
        }

        // if no piece were found
        selectedPiece = null;
    }

    // method to move a piece
    private boolean movePiece(String position, String direction) {
        backupMatrix = U.copy(matrix);

        int i = selectedPiece[0];
        int j = selectedPiece[1];

        if (position.equals("left")) {
            switch (direction) {
                case "t":
                    // if the i,j is movable in matrix && if the selected squares is empty
                    if (isValidMove(i - 2, j + 1) && isFilled(" ", i - 2, j + 1)) {
                        matrix[i][j] = " "; // empty the previous piece
                        matrix[i - 2][j + 1] = "*"; // fill the next piece
                        moves++;
                        return true;
                    }
                    break;

                case "b":
                    if (isValidMove(i + 2, j + 1) && isFilled(" ", i + 2, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 2][j + 1] = "*";
                        moves++;
                        return true;
                    }
                    break;

                case "r":
                    if (isValidMove(i - 1, j + 2) && isFilled(" ", i - 1, j + 2)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j + 2] = "*";
                        moves++;
                        return true;
                    }
                    break;

                case "l":
                    if (isValidMove(i + 1, j + 2) && isFilled(" ", i + 1, j + 2)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j + 2] = "*";
                        moves++;
                        return true;
                    }
                    break;
            }

        } else {
            switch (direction) {
                case "t":
                    if (isValidMove(i - 2, j - 1) && isFilled(" ", i - 2, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i - 2][j - 1] = "+";
                        moves++;
                        return true;
                    }
                    break;

                case "b":
                    if (isValidMove(i + 2, j - 1) && isFilled(" ", i + 2, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 2][j - 1] = "+";
                        moves++;
                        return true;
                    }
                    break;

                case "r":
                    if (isValidMove(i - 1, j - 2) && isFilled(" ", i - 1, j - 2)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j - 2] = "+";
                        moves++;
                        return true;
                    }
                    break;

                case "l":
                    if (isValidMove(i + 1, j - 2) && isFilled(" ", i + 1, j - 2)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j - 2] = "+";
                        moves++;
                        return true;
                    }
                    break;
            }
        }

        markedMatrix = U.copy(matrix);
        return false;
    }

    // method to hit a piece
    private boolean hitPiece(String position, String direction) {
        backupMatrix = U.copy(matrix);

        int i = selectedPiece[0];
        int j = selectedPiece[1];

        if (position.equals("left")) {
            switch (direction) {
                case "~1":
                    if (isValidMove(i - 1, j + 1) && isFilled("+", i - 1, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j + 1] = "*";
                        moves = 0; // hit completed
                        system.pieces.remove(system.pieces.size() - 1); // remove a piece from system
                        return true;
                    }
                    break;

                case "~2":
                    if (isValidMove(i, j + 1) && isFilled("+", i, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i][j + 1] = "*";
                        moves = 0;
                        system.pieces.remove(system.pieces.size() - 1);
                        return true;
                    }
                    break;

                case "~3":
                    if (isValidMove(i + 1, j + 1) && isFilled("+", i + 1, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j + 1] = "*";
                        moves = 0;
                        system.pieces.remove(system.pieces.size() - 1);
                        return true;
                    }
                    break;
            }

        } else {
            switch (direction) {
                case "~1":
                    if (isValidMove(i - 1, j - 1) && isFilled("*", i - 1, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j - 1] = "+";
                        moves = 0;
                        system.pieces.remove(system.pieces.size() - 1);
                        return true;
                    }
                    break;

                case "~2":
                    if (isValidMove(i, j - 1) && isFilled("*", i, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i][j - 1] = "+";
                        moves = 0;
                        system.pieces.remove(system.pieces.size() - 1);
                        return true;
                    }
                    break;

                case "~3":
                    if (isValidMove(i + 1, j - 1) && isFilled("*", i + 1, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j - 1] = "+";
                        moves = 0;
                        system.pieces.remove(system.pieces.size() - 1);
                        return true;
                    }
                    break;
            }
        }

        markedMatrix = U.copy(matrix);
        return false;
    }

    // method to check if a square is filled with a specific piece
    private boolean isFilled(String piece, int i, int j) {
        return matrix[i][j].equals(piece);
    }

    // method to check if a move is valid in the matrix
    private boolean isValidMove(int i, int j) {
        return i < rowsOfMatrix && i > -1 && j < rowsOfMatrix && j > -1;
    }

    // method to label the pieces of the user or system
    private void labelPieces(String position) {
        markedMatrix = U.copy(matrix);
        ArrayList<String> piecies = U.copy(user.pieces); // create a temp array list for all pieces

        if (position.equals("left")) {
            for (int j = 7; j > -1; j--) {
                for (int i = 0; i < rowsOfMatrix; i++) {
                    if (markedMatrix[i][j].equals("*")) {
                        // label the piece if it's *
                        markedMatrix[i][j] = piecies.get(0);
                        piecies.remove(0);
                    }
                }
            }

        } else {
            for (int j = 0; j < rowsOfMatrix; j++) {
                for (int i = 0; i < rowsOfMatrix; i++) {
                    if (markedMatrix[i][j].equals("+")) {
                        // label the piece if it's +
                        markedMatrix[i][j] = piecies.get(0);
                        piecies.remove(0);
                    }
                }
            }
        }
    }

    // method to find the score of a player
    private int findScore(Player player) {
        int count;

        // find the all remained pieces for user
        if (player instanceof Sys
        )
            count = Arrays.deepToString(matrix).length() - Arrays.deepToString(matrix).replaceAll(user.getPieceType(), "").length();

        else // find the all remained pieces for system
            count = Arrays.deepToString(matrix).length() - Arrays.deepToString(matrix).replaceAll(system.getPieceType(), "").length();

        return (rowsOfMatrix * 2) - count; // score
    }
}