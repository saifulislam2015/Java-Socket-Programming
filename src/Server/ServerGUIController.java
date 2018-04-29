package Server;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by saifu on 3/15/2017.
 */
public class ServerGUIController {
    private ServerMain sm;
    @FXML
    private TextField t1;

    @FXML
    private TextField t2;

    @FXML
    void AddAction() {
        ServerMain.addMatch(t1.getText(), t2.getText());
        t1.clear();
        t2.clear();
    }

    public void setServer(ServerMain serverMain) {
        this.sm = serverMain;
    }
}
