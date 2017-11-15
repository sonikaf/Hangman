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
      
      //check if user is ready
      System.out.println("Ready to start game? (y/n): ");
      String userInput = scanner.nextLine();
      while (!userInput.equals("y") && !userInput.equals("n")) {
        System.out.println("Must respond with y or n.");
        userInput = scanner.nextLine();
      }
      if (userInput.equals("n")) break game;

      //signals start of game to server
      out.println();

      while(true) {
        String status = in.readLine();
        
        //TODO: figure out how to format the data... for now assuming a string concatenation deal
        if (Integer.valueOf(status.charAt(0)) == 0) {
          //play game
          board(status);
          userInput = scanner.next();
          while (!validGuess(userInput)) {
            System.out.println("Error! Please guess one letter.");
            userInput = scanner.next();
          }
          out.println(userInput.toLowerCase());
        
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

  //TODO: implement
  //prints out hangman board for user given status
  private static void board(String status) {
    System.out.println("BOARD");
  }

  //TODO: implement
  //checks if user's guess is a single letter 
  private static boolean validGuess(String userInput) {
    return true;
  }
}