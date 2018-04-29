package Server;

import Client.ClientGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by saifu on 3/15/2017.
 */
public class ServerMain extends Application {

    static Stage stage;
    static ServerMain main;
    static ServerGUIController controller;
    static ServerStartPageController startController;
    static ArrayList<Match> matches;
    static int maxMatch;
    static String allowedIds;
    static Server serverThread;
    static ArrayList<Integer> id;
    static ArrayList<Integer> loglist;
    static ArrayList<WorkerThread> wt;
    static HashMap<Integer,ArrayList<WorkerThread>> hm;

    public void start(Stage primaryStage) throws Exception {

        main = this;
        matches = new ArrayList<>();
        wt = new ArrayList<>();
        hm = new HashMap<>();
        loglist = new ArrayList<>();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ServerStartPage.fxml"));
        Parent root = loader.load();

        startController = loader.getController();
        startController.setServer(this);

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.show();
        stage = primaryStage;
    }

    public static void main(String [] args){
        launch(args);
    }

    public static void addMatch(String text, String text1) {
        int id = matches.size()+1;
        matches.add(new Match(id, text, text1));

        for(WorkerThread w:wt){
                w.sendMatches();
        }
        hm.put(id, new ArrayList<WorkerThread>());
    }

    public static void showSecondPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(main.getClass().getResource("ServerGUI.fxml"));
            Parent root = loader.load();

            controller = loader.getController();
            controller.setServer(main);

            stage.setTitle("Server : Add Match");
            stage.setScene(new Scene(root, 600, 400));

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void startServer() {
         serverThread = new Server();
    }


    public static void CreateList(String ID){

        id = new ArrayList<>();

        String[] s1 = ID.split(",");

        for (String s:s1){
            if(s.contains("-")) {
                String[] s2 = s.split("-");
                int low = Integer.parseInt(s2[0]);
                int high = Integer.parseInt(s2[1]);
                for(int j=low; j<=high; j++){
                    id.add(j);
                }
            }
            else {
                id.add(Integer.parseInt(s));
            }

        }
        Collections.sort(id);

    }
}
