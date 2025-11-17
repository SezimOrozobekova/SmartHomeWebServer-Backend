import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer implements Runnable {

    private int port;
    private Thread thread;
    private SmartHomeState state;

    public WebServer(int port) {
        this.port = port;
        thread = new Thread(this);
        state = new SmartHomeState();
    }

    public void go() {
        thread.start();
    }

    public void run() {
        try {
            startServer();
        } catch (IOException e) {
            System.out.println("Error starting server: " + e);
        }
    }

    private void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();
            Client client = new Client(socket, state);
            client.go();
        }
    }
}
