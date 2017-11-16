import java.io.*;
import java.net.*;
 
public class server {  
  
    static final int PORT = 8080;
    static int sessions;
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server Started");
        sessions = 0;
       
       threads: try {
            while(true) {
                // Blocks until a connection occurs:
                Socket socket = s.accept();
                sessions++;
                try {
                  
                    if (sessions <= 3) new PlayerThread(socket);
                    else {
                        //server overload message
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        out.println("17server-overloaded");
                        break threads;
                    }
                } catch(IOException e) {
                    // If it fails, close the socket,
                    // otherwise the thread will close it:
                    break threads;
                }
            }
        
        } finally {
            s.close();
            sessions--;
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
            String str = in.readLine();
            //word guessed a, b and o
            out.println("" + (char) 0 + (char) 4 + "_o__ab");
        
        } catch (IOException e) {
        
        } finally {
            try {
                socket.close();
            } catch(IOException e) {}
        }
    }
}