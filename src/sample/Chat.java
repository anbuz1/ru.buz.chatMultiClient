package sample;


import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static sample.Main.properties;

public class Chat {

    private static StringBuilder chatText;
    private WebEngine webEngine;
    private WebEngine webEngine2;
    private PrintWriter wr;
    private BufferedReader rd;

    @FXML
    private TextArea text_area;

    @FXML
    private Button send_mess;

    @FXML
    private AnchorPane chat_area;

    @FXML
    private WebView list_area;

    @FXML
    private AnchorPane general;

    @FXML
    private WebView web_view;


    @FXML
    void initialize() {


        chatText = new StringBuilder();
        chatText.append(scrollWebView());
        text_area.setOnKeyPressed(event -> {
            try {
                Robot robot = new Robot();

                if (event.getCode() == KeyCode.ENTER) {
                    try {

                        sendMessage();
                        robot.keyPress(KeyEvent.VK_BACK_SPACE);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } catch (AWTException e) {
                e.printStackTrace();
            }

        });


        webEngine = web_view.getEngine();
        webEngine2 = list_area.getEngine();
        wr = Main.getWriter();
        rd = Main.getReader();
        Listener listener = new Listener(rd, this);
        listener.start();
        wr.println(properties.getProperty("service_key"));
        try {
            publicMessage(rd.readLine());
        } catch (IOException e) {
            e.printStackTrace();

        }
        wr.println(properties.getProperty("list_client"));

    }

    @FXML
    void sendMessage() throws IOException {
        String sendText = text_area.getText();

        if(!sendText.equals("")) wr.println(sendText);

        text_area.clear();

    }


    void publicMessage(String mess) {
        if (mess.startsWith("000111")) {
            webEngine2.loadContent(mess.replace("000111", ""));
        } else {
            chatText.append(mess);
            webEngine.loadContent(chatText.toString());
        }

    }


    public void setChatText(StringBuilder chatText) {
        this.chatText.append(chatText);
    }

    public static StringBuilder scrollWebView() {
        StringBuilder script = new StringBuilder().append("<html>");
        script.append("<head>");
        script.append("   <script language=\"javascript\" type=\"text/javascript\">");
        script.append("       function toBottom(){");
        script.append("           window.scrollTo(0,  document.body.scrollHeight );");
        script.append("       }");
        script.append("   </script>");
        script.append("</head>");
        script.append("<body onload='toBottom()'>");

        return script;
    }

}
