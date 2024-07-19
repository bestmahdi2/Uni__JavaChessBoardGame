import java.util.*;
import java.util.concurrent.TimeUnit; // find time
import java.io.IOException; // exception

public class Main {
    // define a scanner
    static Scanner scanner = new Scanner(System.in);

    // side of user, system (* and +)
    static String userSide;
    static String systemSide;

    // string for keeping the winner's name
    static String winner = "";

    // selected piece x, y
    static int[] piece = new int[]{};

    // integer to keep bare moves
    static int moves = 0;

    // arrays to keep main matrix
    static String[][] matrix = new String[8][8];
    // this array used for keeping matrix with marks
    static String[][] tempMatrix = new String[8][8];
    // this array used for keeping the last matrix before move/hit
    static String[][] lastMoves = new String[8][8];

    // pieces
    static ArrayList<String> systemPieces = new ArrayList<>(List.of("f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8"));
    static ArrayList<String> allPieces = new ArrayList<>(List.of("f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8"));
    static ArrayList<String> systemMoves = new ArrayList<>();

    public static void main(String[] args) {
        // welcome message
        System.out.println("Welcome to Chess Game !");

        // call methods
        fillMatrix();
        showMenu();
    }

    // method for clearing screen
    public static void clearScreen() {
        try { // open cmd > type > /c cls
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ignored) {
        }
    }

    // method for sleeping the program for specific time (second)
    private static void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (java.lang.InterruptedException ignored) {
        }
    }

    // method for filling the bare matrix
    private static void fillMatrix() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j < 2)
                    matrix[i][j] = "*";

                else if (j > 5)
                    matrix[i][j] = "+";

                else
                    matrix[i][j] = " ";
            }
        }

        // copy the main matrix values to temp matrix
        tempMatrix = copy(matrix);
    }

    // method for showing the main menu to user
    private static void showMenu() {
        System.out.println("\nPlease select an option below");
        System.out.print(" 1. Play Game\n 2. Exit\n> ");

        String input = scanner.next();

        switch (input) { // play the game
            case "1":
                play();
                showMenu();
                break;

            case "2": // exit
                System.out.print("\nExiting the game ... !\n");
                sleep(1);
                break;

            default: // wrong command
                System.out.print("You entered wrong command !\n");
                showMenu();
        }
    }

    // method to find out if the game need to be ended
    private static boolean endGame() {
        if (findScore("user") == 16 || findScore("system") == 16 || moves == 10)
            findWinner(); // find the winner

        return (!winner.equals(""));
    }

    // method for finding the winner of the game
    private static void findWinner() {
        if (findScore("user") > findScore("system"))
            winner = "user";

        else if (findScore("user") == findScore("system"))
            winner = "draw";

        else
            winner = "system";
    }

    // method for looping the game
    private static void play() {
        // getting the side of the user
        System.out.println("\nPlease select a side");
        System.out.print(" 1. Left\n 2. Right\n> ");

        String input = scanner.next();

        if (!input.equals("1") && !input.equals("2")) { // if not selected 1 and 2
            System.out.println("Wrong command !");
            return;
        }

        if (input.equals("1"))
            userSide = "left";
        else
            userSide = "right";

        if (input.equals("1"))
            systemSide = "right";
        else
            systemSide = "left";

        while (true) {
            // copy the main matrix to temp matrix
            tempMatrix = copy(matrix);

            // show user turn
            clearScreen();
            System.out.println("User turn !");
            sleep(1);
            clearScreen();

            // play the user and check if end is ended or not
            userPlay();
            if (endGame())
                break;
            sleep(3);

            // show system turn
            clearScreen();
            System.out.println("System turn !");
            sleep(1);
            clearScreen();

            // play the system and check if end is ended or not
            systemPlay();
            if (endGame())
                break;
            sleep(3);
        }

        // show the winner in the screen
        if (winner.equals("user"))
            System.out.println("You win !");

        else if (winner.equals("draw"))
            System.out.println("The match ended in a draw !");

        else
            System.out.println("You loose !");
    }

    // method for marking all signs of the main matrix in temp matrix
    private static void markAllSigns(String side) {
        int i = piece[0], j = piece[1];

        // distinguish the selected piece
        if (!tempMatrix[i][j].contains("*"))
            tempMatrix[i][j] = "*" + tempMatrix[i][j];

        if (side.equals("left")) {
            // show available moves
            // (if the move is movable in matrix)     (if the selected index is empty)
            if (validMove(i - 2, j + 1) && filled(" ", i - 2, j + 1))
                tempMatrix[i - 2][j + 1] = "t"; // up

            if (validMove(i + 2, j + 1) && filled(" ", i + 2, j + 1))
                tempMatrix[i + 2][j + 1] = "b"; // down

            if (validMove(i - 1, j + 2) && filled(" ", i - 1, j + 2))
                tempMatrix[i - 1][j + 2] = "r"; // semi-up

            if (validMove(i + 1, j + 2) && filled(" ", i + 1, j + 2))
                tempMatrix[i + 1][j + 2] = "l"; // semi-down

        } else {
            // show available moves
            if (validMove(i - 2, j - 1) && filled(" ", i - 2, j - 1))
                tempMatrix[i - 2][j - 1] = "t";

            if (validMove(i + 2, j - 1) && filled(" ", i + 2, j - 1))
                tempMatrix[i + 2][j - 1] = "b";

            if (validMove(i - 1, j - 2) && filled(" ", i - 1, j - 2))
                tempMatrix[i - 1][j - 2] = "r";

            if (validMove(i + 1, j - 2) && filled(" ", i + 1, j - 2))
                tempMatrix[i + 1][j - 2] = "l";
        }

        // show available hit positions
        if (side.equals("left")) {
            // (if the move is movable in matrix)     (if the selected index is filled with competitor)
            if (validMove(i - 1, j + 1) && filled("+", i - 1, j + 1)) tempMatrix[i - 1][j + 1] = "ht";
            if (validMove(0, j + 1) && filled("+", i, j + 1)) tempMatrix[i][j + 1] = "hs";
            if (validMove(i + 1, j + 1) && filled("+", i + 1, j + 1)) tempMatrix[i + 1][j + 1] = "hb";

        } else {
            if (validMove(i - 1, j - 1) && filled("*", i - 1, j - 1)) tempMatrix[i - 1][j - 1] = "ht";
            if (validMove(0, j - 1) && filled("*", i, j - 1)) tempMatrix[i][j - 1] = "hs";
            if (validMove(i + 1, j - 1) && filled("*", i + 1, j - 1)) tempMatrix[i + 1][j - 1] = "hb";
        }
    }

    // method for user to choose a piece
    private static void choosePiece() {
        numbering(userSide);  // label the pieces of the user
        printTempBoard();  // print matrix with signs

        // try to find the selected piece
        System.out.print("[Your turn] Choose a piece > ");
        String input = scanner.next();
        piece = findPiece(input);

        if (piece.length == 0) { // couldn't find the piece
            System.out.print("\nCount find the piece !\n");
            choosePiece();
        }
    }

    // method for user to choose a move
    private static void chooseMove() {
        markAllSigns(userSide);  // make all signs
        printTempBoard();  // print matrix with signs

        // try to find the selected move
        System.out.print("[Your turn] Choose a move > ");
        String input = scanner.next();

        if (!input.equals("-")) {
            if ("t,b,r,l".contains(input)) { // if move is moving
                if (!movePiece(userSide, input, piece[0], piece[1])) {
                    System.out.println("\nCan't move piece in this direction !");
                    tempMatrix = copy(matrix);
                    numbering(userSide);
                    chooseMove();

                } else
                    chooseToUndo();

            } else if ("ht,hs,hb".contains(input)) { // if move is hitting
                if (!hitPiece(userSide, input, piece[0], piece[1])) {
                    System.out.println("\nCan't hit piece in this direction !");
                    tempMatrix = copy(matrix);
                    numbering(userSide);
                    chooseMove();

                } else {// if user hit a piece, can play again
                    chooseToUndo();
                    userPlay();
                }

            } else {
                System.out.println("\nWrong direction !");
                chooseMove();
            }

        } else {
            System.out.println("\nSkip this piece ! Try another piece !");
            userPlay();
        }
    }

    // method for user to play
    private static void userPlay() {
        // print the bare matrix
        printBoard();
        sleep(1);
        clearScreen();

        // choose a piece
        choosePiece();
        clearScreen();

        // choose a move
        chooseMove();
        printBoard();
    }

    private static void chooseToUndo() {
        printBoard();

        System.out.print("Would you like to continue ? [yes/undo] > ");
        String input = scanner.next();
        if (input.toLowerCase(Locale.ROOT).equals("undo")) {
            undo();
            System.out.println("\nEverything back to previous position !");
            if (moves > 0) moves--;
            userPlay();
        }
    }

    // method for system to play
    private static void systemPlay() {
        clearScreen();

        numbering(systemSide);  // label the pieces of the user
        printTempBoard();  // print matrix with signs

        // find a random piece for system
        System.out.print("\n[System turn] Choose a piece > ");
        sleep(1);
        String input = systemPieces.get((int) (Math.random() * systemPieces.size()));

        // print the selected piece
        System.out.println(input);
        piece = findPiece(input);

        sleep(2);
        clearScreen();

        markAllSigns(systemSide);  // make all signs
        printTempBoard();  // print matrix with signs

        // try to find the selected move
        System.out.print("\n[System turn] Choose a move > ");
        sleep(1);

        // find all the moves and hits
        systemMoves.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ("t,b,r,l,ht,hs,hb".contains(tempMatrix[i][j])) {
                    systemMoves.add(tempMatrix[i][j]);
                }
            }
        }

        // find a random move or hit for system
        input = systemMoves.get((int) (Math.random() * systemMoves.size()));

        if ("t,b,r,l".contains(input)) { // if move is moving
            System.out.println(input);
            movePiece(systemSide, input, piece[0], piece[1]);

        } else if ("ht,hs,hb".contains(input)) { // if move is hitting
            System.out.println(input);
            if (hitPiece(systemSide, input, piece[0], piece[1]))
                systemPlay();

        } else // if this piece can't do any move or hit
            System.out.println("-");

        sleep(2);
        printBoard(); // print the bare matrix
    }

    // method for printing the bare bord
    private static void printBoard() {
        System.out.printf("\nScore >> You: %d, System: %d, Moves: %d\n", findScore("user"), findScore("system"), moves);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print("|");
                System.out.print("   " + matrix[i][j] + "   ");
            }
            System.out.println("|");
        }
        System.out.println();
    }

    // method for printing the bord with signs
    private static void printTempBoard() {
        System.out.printf("\nScore >> You: %d, System: %d, Moves: %d\n", findScore("user"), findScore("system"), moves);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print("|");

                if (tempMatrix[i][j].length() == 1)
                    System.out.print("   " + tempMatrix[i][j] + "   ");

                else if (tempMatrix[i][j].length() == 2)
                    System.out.print("   " + tempMatrix[i][j] + "  ");

                else
                    System.out.print("  " + tempMatrix[i][j] + "  ");
            }

            System.out.println("|");

        }

        System.out.println();
    }

    // method for finding the piece by label
    private static int[] findPiece(String input) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tempMatrix[i][j].equals(input))
                    return new int[]{i, j};
            }
        }

        return new int[]{};
    }


    // method for move a piece
    private static boolean movePiece(String position, String direction, int i, int j) {
        moves++; // add to bare moves

        lastMoves = copy(matrix);

        if (position.equals("left")) {
            switch (direction) {
                case "t":
                    // (if the move is movable in matrix)     (if the selected index is empty)
                    if (validMove(i - 2, j + 1) && filled(" ", i - 2, j + 1)) {
                        matrix[i][j] = " "; // empty the previous piece
                        matrix[i - 2][j + 1] = "*"; // fill the next piece
                        return true;
                    }
                    break;

                case "b":
                    if (validMove(i + 2, j + 1) && filled(" ", i + 2, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 2][j + 1] = "*";
                        return true;
                    }
                    break;

                case "r":
                    if (validMove(i - 1, j + 2) && filled(" ", i - 1, j + 2)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j + 2] = "*";
                        return true;
                    }
                    break;

                case "l":
                    if (validMove(i + 1, j + 2) && filled(" ", i + 1, j + 2)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j + 2] = "*";
                        return true;
                    }
                    break;
            }

        } else {
            switch (direction) {
                case "t":
                    if (validMove(i - 2, j - 1) && filled(" ", i - 2, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i - 2][j - 1] = "+";
                        return true;
                    }
                    break;

                case "b":
                    if (validMove(i + 2, j - 1) && filled(" ", i + 2, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 2][j - 1] = "+";
                        return true;
                    }
                    break;

                case "r":
                    if (validMove(i - 1, j - 2) && filled(" ", i - 1, j - 2)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j - 2] = "+";
                        return true;
                    }
                    break;

                case "l":
                    if (validMove(i + 1, j - 2) && filled(" ", i + 1, j - 2)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j - 2] = "+";
                        return true;
                    }
                    break;
            }
        }

        tempMatrix = copy(matrix);
        moves--; // if not any moved happened
        return false;
    }

    // method for hitting a piece
    private static boolean hitPiece(String position, String direction, int i, int j) {
        lastMoves = copy(matrix);

        if (position.equals("left")) {
            switch (direction) {
                case "ht":
                    if (validMove(i - 1, j + 1) && filled("+", i - 1, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j + 1] = "*";
                        moves = 0; // hit completed
                        systemPieces.remove(systemPieces.size() - 1); // remove a piece from system
                        return true;
                    }
                    break;

                case "hs":
                    if (validMove(i, j + 1) && filled("+", i, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i][j + 1] = "*";
                        moves = 0;
                        systemPieces.remove(systemPieces.size() - 1);
                        return true;
                    }
                    break;

                case "hb":
                    if (validMove(i + 1, j + 1) && filled("+", i + 1, j + 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j + 1] = "*";
                        moves = 0;
                        systemPieces.remove(systemPieces.size() - 1);
                        return true;
                    }
                    break;
            }

        } else {
            switch (direction) {
                case "ht":
                    if (validMove(i - 1, j - 1) && filled("*", i - 1, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i - 1][j - 1] = "+";
                        moves = 0;
                        systemPieces.remove(systemPieces.size() - 1);
                        return true;
                    }
                    break;

                case "hs":
                    if (validMove(i, j - 1) && filled("*", i, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i][j - 1] = "+";
                        moves = 0;
                        systemPieces.remove(systemPieces.size() - 1);
                        return true;
                    }
                    break;

                case "hb":
                    if (validMove(i + 1, j - 1) && filled("*", i + 1, j - 1)) {
                        matrix[i][j] = " ";
                        matrix[i + 1][j - 1] = "+";
                        moves = 0;
                        systemPieces.remove(systemPieces.size() - 1);
                        return true;
                    }
                    break;
            }
        }

        tempMatrix = copy(matrix);
        return false;
    }

    private static void undo() {
        if (Arrays.deepEquals(lastMoves, matrix))
            System.out.println("There is no previous move/hit");

        else
            matrix = copy(lastMoves);
    }

    // method for finding if an index is filled with a specific piece
    private static boolean filled(String piece, int i, int j) {
        return matrix[i][j].equals(piece);
    }

    // method for finding if a move is valid in the matrix
    private static boolean validMove(int i, int j) {
        return i > -1 && i < 8 && j > -1 && j < 8;
    }

    // method for labeling the pieces of the user or system
    private static void numbering(String position) {
        // create a temp array list for all pieces
        ArrayList<String> tempPieces = copy(allPieces);
        tempMatrix = copy(matrix);

        if (position.equals("left")) {
            for (int j = 7; j > -1; j--) {
                for (int i = 0; i < 8; i++) {
                    if (tempMatrix[i][j].equals("*")) {
                        // label the piece if it's *
                        tempMatrix[i][j] = tempPieces.get(0);
                        tempPieces.remove(0);
                    }
                }
            }

        } else {
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    if (tempMatrix[i][j].equals("+")) {
                        // label the piece if it's +
                        tempMatrix[i][j] = tempPieces.get(0);
                        tempPieces.remove(0);
                    }
                }
            }
        }
    }

    // method for finding the score of a player
    private static int findScore(String player) {
        // find the system and user piece's type
        String system = systemSide.equals("right") ? "\\*" : "\\+";
        String user = userSide.equals("right") ? "\\*" : "\\+";

        int count;

        // find the all remained pieces for user
        if (player.equals("user"))
            count = Arrays.deepToString(matrix).length() - Arrays.deepToString(matrix).replaceAll(user, "").length();

        else // find the all remained pieces for system
            count = Arrays.deepToString(matrix).length() - Arrays.deepToString(matrix).replaceAll(system, "").length();

        return 16 - count; // score
    }

    // method for copying a 2d string array
    private static String[][] copy(String[][] src) {
        String[][] copy = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = src[i][j];
            }
        }

        return copy;
    }

    // method for copying a string arrayList
    private static ArrayList<String> copy(ArrayList<String> src) {
        ArrayList<String> copy = new ArrayList<>();

        for (String item : src) {
            copy.add(item);
        }

        return copy;
    }
}