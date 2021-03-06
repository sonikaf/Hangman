/* Created by Sonika Finch and Michael Chen */
import java.net.*;
import java.io.*;
import java.util.*;
import java.net.ConnectException;

public class client {
    public static void main(String[] args) throws IOException {

        if(args.length != 2) {
            System.out.println("Incorrect number of arguments.");
            System.exit(0);
        }
        String addressString = args[0];
        InetAddress addr = InetAddress.getByName(args[0]);
        int PORT = Integer.parseInt(args[1]);
        Socket socket = new Socket(addr, PORT);

        Scanner scanner = new Scanner(System.in);

        game: try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            //status is the message from server
            String status = in.readLine();

            //check if server is ready
            if (status.equals((char) 17 + "server-overloaded")) {
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
                int flag = in.read();
                if (flag == 0) {
                    //play game
                    board(in.read(), in.read(), in.readLine());
                    userInput = scanner.next().toLowerCase();
                    while (!validGuess(userInput)) {
                        System.out.print("Error! Please guess one letter: ");
                        userInput = scanner.next();
                    }
                    out.println(userInput);

                } else {
                    String message = in.readLine();
                    System.out.println(message);
                    if (message.equals("Game Over!")) break game;
                    else out.println("Ok");
                }
            }

        } finally {
            socket.close();
        }
      }

      //prints out hangman board for user given status
    private static void board(int length, int numIncorrectGuesses, String status) {

        //updated word
        for (int i = 0; i < length; i++) {
            System.out.print(status.charAt(i) + " ");
        }
        //updated incorrect guesses
        System.out.print("\nIncorrect Guesses: ");
        for (int i = length
            ; i < status.length(); i++) {
            System.out.print(status.charAt(i) + " ");
        }
        //new guess
        System.out.print("\n\nLetter to guess: ");
    }

    //checks if user's guess is a single letter
    private static boolean validGuess(String userInput) {
        return userInput.length() == 1 && userInput.charAt(0) <= 'z' && userInput.charAt(0) >= 'a';
    }
}
