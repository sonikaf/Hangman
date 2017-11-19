import java.io.*;
import java.net.*;

public class server {
    //TODO read port from command line
    static final int PORT = 8080;
    static public int sessions;
    static protected String[] words;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        System.out.println("Server Started");
        System.out.println(args.length);
        words = new String[]{"foo", "bar", "baz", "sonika", "michael", "hummus", "hodor", "sun",
                 "flower", "capital", "amazon", "seattle", "burdell", "computer",
                  "monet"};


        try {
             serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
             e.printStackTrace();
        }
        while(true) {
          System.out.println("sessions: " + sessions);
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
             out.println("17server-overloaded");
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
        out.println("good");

        try {
          String str = in.readLine();
            //word guessed a, b and o
            out.println("" + (char) 0 + (char) 4 + "_o__ab");


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.sessions--;
                socket.close();
            } catch(IOException e) {
                e.printStackTrace();
          }
        }
    }
}
