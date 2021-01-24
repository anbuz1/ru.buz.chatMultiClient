package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sample.Main.propPath;
import static sample.Main.properties;

public class Registration {

    private Logger LOG = null;
    private BufferedReader rd;
    private PrintWriter wr;
    private String answer;
    private final String SECOND_TEXT = "This name is already in use. Try with a different name";

    @FXML
    private Label text;

    @FXML
    private Button sendButton;

    @FXML
    private TextField textField;

    public Registration() {

    }

    @FXML
    void initialize() {
        this.rd = Main.getReader();
        this.wr = Main.getWriter();
        text.setAlignment(Pos.CENTER);
        text.setTextFill(Color.RED);
        LOG = Logger.getLogger(Registration.class.getName());


    }

    @FXML
    void sendData(ActionEvent event) {

        wr.println(textField.getText());

        try {
            answer = Codes.getStatus(rd.readLine());
            if (!answer.equals("STATUS_OK")) {
                text.setText(SECOND_TEXT);
            }else {
                createUser();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }


    }

    void createUser() throws IOException {
        wr.println("@200");
        String key = rd.readLine();
        StringBuilder stb = new StringBuilder();
        if (Files.exists(Paths.get(propPath))){

           BufferedReader reader = Files.newBufferedReader(Paths.get(propPath));
           reader.lines().forEach(x -> {
               String oldKey = properties.getProperty("personal_key");

               if(x.contains("personal_key =")){
                   x = x.replace(oldKey,"");
                   stb.append(x + " " + key + "\n");


               }
               else stb.append(x + "\n");
           });
           reader.close();
           BufferedWriter writer = Files.newBufferedWriter(Paths.get(propPath));
           writer.write(stb.toString());
           writer.flush();
           writer.close();


        }else  LOG.log(Level.SEVERE, "Can't find property file");

        startChat();
    }

    void startChat() throws IOException {

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("chat.fxml"));
        primaryStage.setTitle("MultichatClient");
        primaryStage.setScene(new Scene(root));

        primaryStage.setMaxHeight(1200);
        primaryStage.setMaxWidth(1200);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);


        primaryStage.show();
        Main.mainStage.close();

    }
    void writeKey(String key){

    }

}