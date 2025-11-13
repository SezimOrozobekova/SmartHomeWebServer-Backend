public class Main {
    public static void main(String[] args) {
        int port = 8080;

        System.out.println("Smart Light Server running on port " + port);
        WebServer server = new WebServer(port);
        server.go();
    }
}
