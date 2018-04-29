package Server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by saifu on 3/15/2017.
 */
public class ServerStartPageController {

    public String text;
    @FXML
    private TextField maxMatches;

    @FXML
    private TextField allowedIds;
    private ServerMain server;

    @FXML
     void StartAction() {
        text = maxMatches.getText();
        ServerMain.maxMatch = Integer.parseInt(maxMatches.getText());
        ServerMain.allowedIds = allowedIds.getText();
        ServerMain.CreateList(allowedIds.getText());
        ServerMain.showSecondPage();
        ServerMain.startServer();
    }

    public void setServer(ServerMain server) {
        this.server = server;
    }
}
