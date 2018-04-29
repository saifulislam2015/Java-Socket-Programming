package Client;

/**
 * Created by saifu on 3/12/2017.
 */
import Server.ServerMain;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class ClientGUIController {

    private Client client;

    @FXML
    private TextField Ip;

    @FXML
    private TextField Port;

    @FXML
    private TextField Sid;

    @FXML
    private TextArea Notice;


    public void ConnectAction() {
        if(Client.connected){
            tryAgain();
            return;
        }
            String ValidIP = "127.0.0.1";
            String IP = Ip.getText();
            String ValidPort = "5000";
            String port = Port.getText();
            int ID = Integer.parseInt(Sid.getText());

            if (IP.equals(ValidIP) && port.equals(ValidPort) ){
                //System.out.println("Client connection completed");
                Client.connectToServer(IP, port, ID);
                try {
                    //client.showMatches();
                    Ip.setEditable(false);
                    Port.setEditable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else{

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect Credentials");
                alert.setHeaderText("Incorrect Credentials");
                alert.setContentText("Wrong Entries");
                alert.showAndWait();
            }
    }

    @FXML
    public void ResetAction() {
            Ip.setText(null);
            Port.setText(null);
            Sid.setText(null);
    }

    void setClient(Client client){
        this.client = client;
    }

    public void ShowNote(String note){
        Notice.setText(Notice.getText()+"\n"+note);
    }


    void tryAgain(){
        Client.clientThread.pr.println("Registration "+Sid.getText());
        Client.clientThread.pr.flush();
    }
}