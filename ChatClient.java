//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Consumer<String> onMessageReceived;

    public ChatClient(String var1, int var2, Consumer<String> var3) throws IOException {
        this.socket = new Socket(var1, var2);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.onMessageReceived = var3;
    }

    public void sendMessage(String var1) {
        this.out.println(var1);
    }

    public void startClient() {
        (new Thread(() -> {
            while(true) {
                try {
                    String var1;
                    if ((var1 = this.in.readLine()) != null) {
                        this.onMessageReceived.accept(var1);
                        continue;
                    }
                } catch (IOException var2) {
                    var2.printStackTrace();
                }

                return;
            }
        })).start();
    }
}
