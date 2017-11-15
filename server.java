import java.io.*;
import java.net.*;
 
public class server {  
  
  static final int PORT = 8080;
  static int sessions;
  public static void main(String[] args) throws IOException {
    ServerSocket s = new ServerSocket(PORT);
    System.out.println("Server Started");
    sessions = 0;
   
   try {
      while(true) {
        // Blocks until a connection occurs:
        Socket socket = s.accept();
        sessions++;
        try {
          if (sessions <= 3) new PlayerThread(socket);
          else {
            
            //TODO: server overload message
            socket.close();
            sessions--;
          }
        } catch(IOException e) {
          // If it fails, close the socket,
          // otherwise the thread will close it:
          socket.close();
          sessions--;
        }
      }
    
    } finally {
      s.close();
    }
  } 
}

class PlayerThread extends Thread {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  
  public PlayerThread(Socket s) throws IOException {
    socket = s;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

    try {
      System.out.println("made it!");
      out.println("you're playing :)");

      String str = in.readLine();
      System.out.println("now we're talking");
      out.println("9Game Over!");
    
    } catch (IOException e) {
    
    } finally {
      try {
        socket.close();
      } catch(IOException e) {}
    }
  }
}