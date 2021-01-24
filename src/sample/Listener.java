package sample;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;

public class Listener extends Thread {

    private final BufferedReader rd;
    private Chat chat;
    private String msg;


    public Listener(BufferedReader rd, Chat chat) {
        this.rd = rd;
        this.chat = chat;


    }

  @Override
    public void run() {
        while (true) {
            try {
                msg = rd.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            Platform.runLater(() -> {
                chat.publicMessage(msg);

        });
        }
    }

}
