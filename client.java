import java.net.*;
import java.io.*;
import java.util.*;

public class client {

    public static void main(String[] args) throws IOException {

        // TODO: fix addr
        InetAddress addr = InetAddress.getByName(null);
        Socket socket = new Socket(addr, server.PORT);
        Scanner scanner = new Scanner(System.in);

        game: try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            //status is the message from server
            String status = in.readLine();

            //check if server is ready
            if (status.equals("17server-overloaded")) {
              System.out.println(status);
              break game;
            }

            //check if user is ready
            System.out.print("\nReady to start game? (y/n): ");
            String userInput = scanner.nextLine();
            while (!userInput.equals("y") && !userInput.equals("n")) {
                System.out.println("Must respond with y or n.");
                userInput = scanner.nextLine();
            }
            if (userInput.equals("n")) break game;

            //signals start of game to server
            System.out.println();
            out.println();

            while(true) {
                status = in.readLine();
                if ((int) status.charAt(0) == 0) {
                    //play game
                    board(status);
                    userInput = scanner.next().toLowerCase();
                    while (!validGuess(userInput)) {
                        System.out.print("Error! Please guess one letter: ");
                        userInput = scanner.next();
                    }
                    out.println(userInput);

                } else {
                    //print out message (minus message flag)
                    String message = status.substring(1);
                    System.out.println(message);
                    if (message.equals("Game Over!")) break game;
                }
            }

        } finally {
            socket.close();
        }
      }

      //prints out hangman board for user given status
    private static void board(String status) {

        int length = (int) status.charAt(1);
        //updated word
        for (int i = 2; i < length + 2; i++) {
            System.out.print(status.charAt(i) + " ");
        }
        //updated incorrect guesses
        System.out.print("\nIncorrect Guesses: ");
        for (int i = length + 2; i < status.length(); i++) {
            System.out.print(status.charAt(i) + " ");
        }
        //new guess
        System.out.print("\nLetter to guess: ");
    }

    //checks if user's guess is a single letter
    private static boolean validGuess(String userInput) {
        return userInput.length() == 1 && userInput.charAt(0) <= 'z' && userInput.charAt(0) >= 'a';
    }
}
