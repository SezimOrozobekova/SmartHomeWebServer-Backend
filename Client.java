import java.io.*;
import java.net.Socket;
import com.google.gson.Gson;

public class Client implements Runnable {

    private Socket socket;
    private Thread thread;
    private SmartHomeState state;
    private Gson gson = new Gson();

    public Client(Socket socket, SmartHomeState state) {
        this.socket = socket;
        this.state = state;
        thread = new Thread(this);
    }

    public void go() {
        thread.start();
    }

    @Override
    public void run() {
        handleRequest();
    }

    private void handleRequest() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            OutputStream out = socket.getOutputStream();

            String firstLine = reader.readLine();
            if (firstLine == null) return;

            String path = firstLine.split(" ")[1];

            if (path.equals("/toggle/light")) {
                state.light.toggle();
                sendJson(out, "{\"ok\":true}");
            }
            else if (path.equals("/toggle/kettle")) {
                state.kettle.toggle();
                sendJson(out, "{\"ok\":true}");
            }
            else if (path.equals("/toggle/fridge")) {
                state.fridge.toggle();
                sendJson(out, "{\"ok\":true}");
            }
            else if (path.equals("/state")) {
                sendJson(out, gson.toJson(state));
            }
            else {
                sendHtmlFile(out, "static/index.html");
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e);
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void sendHtmlFile(OutputStream out, String filePath) throws IOException {
        File file = new File(filePath);
        String html = new String(java.nio.file.Files.readAllBytes(file.toPath()));

        PrintWriter writer = new PrintWriter(out, true);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println("Content-Length: " + html.length());
        writer.println();
        writer.flush();

        out.write(html.getBytes());
        out.flush();
    }

    private void sendJson(OutputStream out, String json) throws IOException {
        PrintWriter writer = new PrintWriter(out, true);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: application/json");
        writer.println("Content-Length: " + json.length());
        writer.println();
        writer.flush();

        out.write(json.getBytes());
        out.flush();
    }
}
