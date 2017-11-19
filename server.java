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

        try {
            //send server ready signal
            out.println("good");
            
            //wait for user's ready signal
            String str = in.readLine();

            //pick word
            out.println("" + (char) 0 + (char) 4 + (char) 2 + "_o__ab");


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
