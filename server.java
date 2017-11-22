/* Created by Sonika Finch and Michael Chen */
import java.io.*;
import java.net.*;
import java.lang.Exception;

public class server {
    //TODO read port from command line
    static int PORT;
    static public int sessions;
    static protected String[] words;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;

        System.out.println("Server Started");
        if(args.length < 1 || args.length > 2) {
            System.out.println("Incorrect number of arguments.");
            System.exit(0);
        } else {
                String portAsString = args[0];
            try {
                PORT = Integer.parseInt(args[0]);
            } catch(NumberFormatException e) {
                System.out.println("First argument not a number");
                System.exit(0);
            }
            if(args.length == 2) {
                String filename = args[1];
                File file = new File(filename);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String header = br.readLine();
                String[] headerInfo = header.split(" ");
                int wordSize = Integer.parseInt(headerInfo[0]);
                int arraySize = Integer.parseInt(headerInfo[1]);
                words = new String[arraySize];
                String str;
                int idx = 0;
                while((str = br.readLine()) != null) {
                    words[idx] = str;
                }
            } else {
                words = new String[]{"foo", "bar", "baz", "sonika", "michael",
                    "hummus", "hodor", "sun", "flower", "capital", "amazon",
                    "seattle", "burdell", "computer", "monet"};
            }
        }

        try {
             serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
             e.printStackTrace();
        }
        while(true) {
           try {
             socket = serverSocket.accept();
             sessions++;
           } catch (IOException e) {
              e.printStackTrace();
           }

           if (sessions <= 3) {
             new PlayerThread(socket).start();
           } else {
             sessions--;
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
             out.println("" + (char) 17 + "server-overloaded");
             out.close();
             socket.close();
           }
        }
    }
}

class PlayerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public PlayerThread(Socket s) throws IOException {
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    public void run() {

        game: try {
            //send server ready signal
            out.println("good");

            //wait for user's ready signal
            String str = in.readLine();

            //pick random word
            String word = server.words[(int) Math.floor(Math.random() * server.words.length)];

            //set up relevent vars
            int wordLength = word.length();
            String incorrectGuesses = "";
            String gameStatus = "";
            for (int i = 0; i < wordLength; i++) gameStatus += "_";
            char guess;

            //play game
            while (gameStatus.indexOf('_') != -1 && incorrectGuesses.length() < 6) {

                out.println("" + (char) 0 + (char) wordLength + (char) incorrectGuesses.length() + gameStatus + incorrectGuesses + "");
                guess = in.readLine().charAt(0);

                //if already guessed
                if (gameStatus.indexOf(guess) != -1 || incorrectGuesses.indexOf(guess) != -1) continue;

                int index = word.indexOf(guess);
                if (index != -1) { //correct guess

                    String front = "";
                    String back = "";

                    //find all indexes of the guess in the word
                    while (index >= 0) {
                        front = gameStatus.substring(0, index);
                        back = gameStatus.substring(index + 1);
                        gameStatus = front + guess + back;

                        index = word.indexOf(guess, index + 1);
                    }
                } else { //incorrect guess
                    incorrectGuesses += guess;
                }
            }

            if (gameStatus.indexOf('_') == -1) { //user won
                out.println("" + (char) 8 + "You Win!");

            } else { //user lost
                out.println("" + (char) 11 + "You Lose :(");
            }
            in.readLine();
            out.println("" + (char) 10 + "Game Over!");

            break game;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                server.sessions--;
                socket.close();
            } catch(IOException e) {
                e.printStackTrace();
          }
        }
    }
}
