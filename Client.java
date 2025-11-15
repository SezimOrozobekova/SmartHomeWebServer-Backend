import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.net.Socket;
import com.google.gson.Gson;

public class Client implements Runnable {

    private final Socket socket;
    private final Thread thread;
    private final SmartHomeState state;
    private final Gson gson;

    public Client(Socket socket, SmartHomeState state) {
        this.socket = socket;
        this.state = state;
        this.thread = new Thread(this);
        this.gson = new Gson();
    }

    public void go() {
        thread.start();
    }

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


            if (path.startsWith("/toggle/")) {
                handleToggle(path, out);
                return;
            }

            if (path.equals("/state")) {
                sendJson(out, gson.toJson(state));
                return;
            }

            if (path.startsWith("/static/")) {
                File file = new File("." + path);

                if (!file.exists()) {
                    send404(out);
                    return;
                }

                sendFile(out, file);
                return;
            }

            File index = new File("static/index.html");
            sendFile(out, index);

        } catch (IOException e) {
            System.out.println("Client error: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void handleToggle(String path, OutputStream out) throws IOException {
        switch (path) {

            case "/toggle/light":
                state.light.toggle();
                break;

            case "/toggle/kettle":
                state.kettle.toggle();
                break;

            case "/toggle/fridge":
                state.fridge.toggle();
                break;

            default:
                send404(out);
                return;
        }

        sendJson(out, gson.toJson(Map.of("ok", true)));
    }

    private void sendJson(OutputStream out, String json) throws IOException {
        byte[] bytes = json.getBytes("UTF-8");

        PrintWriter writer = new PrintWriter(out, true);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: application/json; charset=UTF-8");
        writer.println("Content-Length: " + bytes.length);
        writer.println();
        writer.flush();

        out.write(bytes);
        out.flush();
    }

    private void sendFile(OutputStream out, File file) throws IOException {
        String mime = getMimeType(file.getName());

        PrintWriter writer = new PrintWriter(out, true);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: " + mime);
        writer.println("Content-Length: " + file.length());
        writer.println();
        writer.flush();

        FileInputStream fis = new FileInputStream(file);
        fis.transferTo(out);
        fis.close();
    }

    private void send404(OutputStream out) {
        PrintWriter writer = new PrintWriter(out, true);
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/plain");
        writer.println();
        writer.println("404 Not Found");
    }


    private String getMimeType(String filename) {
        if (filename.endsWith(".html")) return "text/html; charset=UTF-8";
        if (filename.endsWith(".css"))  return "text/css";
        if (filename.endsWith(".js"))   return "application/javascript";
        if (filename.endsWith(".png"))  return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        if (filename.endsWith(".mp3"))  return "audio/mpeg";
        return "application/octet-stream";
    }
}
