import java.util.ArrayList; // ArrayList
import java.io.IOException; // IO exception
import java.util.concurrent.TimeUnit; // time

public class Utilities {
    // method to clear the screen
    public void clearScreen(int sleepBefore, int sleepAfter) {
        sleep(sleepBefore);

        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ignored) {
        }

        sleep(sleepAfter);
    }

    // method to sleep the program for specific time in seconds
    public void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (java.lang.InterruptedException ignored) {

        }
    }

    // method to copy array
    public String[][] copy(String[][] src) {
        int rowsOfMatrix = src.length;

        String[][] temp = new String[rowsOfMatrix][rowsOfMatrix];

        for (int i = 0; i < rowsOfMatrix; i++)
            for (int j = 0; j < rowsOfMatrix; j++)
                temp[i][j] = src[i][j];

        return temp;
    }

    // method to copy arrayList
    public ArrayList<String> copy(ArrayList<String> src) {
        ArrayList<String> temp = new ArrayList<>();

        for (String item : src)
            temp.add(item);

        return temp;
    }

    // method to center the given string
    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }

        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }

        return sb.toString();
    }
}

class Color {
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
}