package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by saifu on 3/12/2017.
 */
public class Client extends Application{

    static Stage stage;
    static ArrayList<Integer> idl = new ArrayList<>();
    static ClientGUIController controller;
    static MatchViewController mvcontroller;
    static ClientThread clientThread;
    static String serverIP;
    static int port;
    static int Sid;
    static int max;
    static Client c;
    static boolean connected=false;


    public void start(Stage primaryStage) throws Exception {
        c=this;
        stage = primaryStage;
        //could be a problem
        showClientGUI();
    }

    //addition
    public void showClientGUI() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ClientGUI.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        controller.setClient(this);

        stage.setTitle("Client");
        stage.setScene(new Scene(root,600,400));
        stage.show();
    }


    static   void  showMatches() throws Exception{
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(c.getClass().getResource("MatchView.fxml"));
            Parent root = loader.load();

            mvcontroller = loader.getController();
            mvcontroller.setClient(c);

            stage.setTitle("Welcome Client "+  clientThread.cid);
            stage.setScene(new Scene(root, 650, 600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    public static void main(String [] args){
        launch(args);
    }

    public static void connectToServer(String ip, String port, int id) {
        Client.serverIP = ip;
        Client.port = Integer.parseInt(port);
        Client.Sid = id;


        clientThread = new ClientThread();
    }

}