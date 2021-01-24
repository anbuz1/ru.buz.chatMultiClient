package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static Logger LOG = null;
    private static String address = null;
    private static String resource = "";
    private static String port = null;
    private static PrintWriter writer = null;
    private static BufferedReader reader = null;
    private static int auth = 0;

    static Stage mainStage;
    static Properties properties = new Properties();
    static String startMessage = null;
    static String propPath = null;
    static StringBuilder chatText;


    private static java.util.logging.LogManager LogManager = null;

    static {
        chatText = new StringBuilder();
        try (FileInputStream ins = new FileInputStream(System.getProperty("user.dir") + "/properties/logging.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOG = Logger.getLogger(Main.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }


    }


    @Override

    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(resource));
        primaryStage.setTitle("MultichatClient");
        primaryStage.setScene(new Scene(root));
        if (auth == 1) {
            primaryStage.setMaxHeight(1200);
            primaryStage.setMaxWidth(1200);
            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(600);
        } else primaryStage.setResizable(false);
        mainStage = primaryStage;
        primaryStage.show();

    }


    public static void main(String[] args) {

        propPath = System.getProperty("user.dir") + "/properties/config.properties";

        if (Files.exists(Paths.get(propPath))) {
            try {
                properties.load(new FileInputStream(propPath));
                LOG.log(Level.SEVERE, "Load property from: " + propPath);

            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Load property exception: ", e);
                startMessage = "102: error read Properties!";
            }
        } else {
            LOG.log(Level.SEVERE, "Properties load failed!");
            startMessage = "101: Properties not found!";
        }
        if (startMessage == null) {
            address = properties.getProperty("address");
            port = properties.getProperty("port");

            try (Socket socket = new Socket(address, Integer.parseInt(port))) {
                try (
                        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter wr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)
                ) {
                    writer = wr;
                    reader = rd;

                    auth = auth(wr, rd);
                    int x = 0;
                    launch(args);


                }

            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage());
                startMessage = "201: Failed server connection! Check connection properties!";
                resource = "initialize.fxml";
                launch(args);
            }
        } else launch(args);

    }

    private static int auth(PrintWriter wr, BufferedReader rd) {
        String answer = "";
        String serverKey = properties.getProperty("server_key");
        String personalKey = properties.getProperty("personal_key") != null
                &&
                !properties.getProperty("personal_key").equals("")
                ?
                "|" + properties.getProperty("personal_key") : "|0";

        wr.println(serverKey + personalKey);
        try {
            answer = rd.readLine();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        switch (Codes.getStatus(answer)) {
            case "STATUS_OK": {
                resource = "chat.fxml";
                return 1;
            }

            case "STATUS_REGISTRATION": {
                resource = "registration.fxml";
                return 2;
            }
            case "STATUS_ERROR1": {
                startMessage = "301: Not valid server key! You can't use this chat";
                resource = "initialize.fxml";
                return 3;
            }
            case "STATUS_ERROR2": {
                startMessage = "302: Not valid client key! Contact server administrator";
                resource = "initialize.fxml";
                return 3;
            }

        }
        return 1;
    }


    public static PrintWriter getWriter() {
        return writer;
    }

    public static BufferedReader getReader() {
        return reader;
    }
}
