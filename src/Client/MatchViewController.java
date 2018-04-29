package Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by saifu on 3/16/2017.
 */
public class MatchViewController {

    private Client client;
    private String s2 = "";

    @FXML
    private TextField MatchID;

    @FXML
    private Button StreamButton;

    @FXML
    private TextArea List;

    @FXML
    private TextArea WBox;

    public void StreamAction( ) {
            String s1 ="";

            String[] s = MatchID.getText().split(" ");

            for(int i=0;i<s.length;i++){
                if(i<Client.max) s1+=s[i]+" ";
            }

            client.clientThread.pr.println("Subscribed "+s1);
            client.clientThread.pr.flush();

            for(int i=0;i<s.length;i++){
                int t = Integer.parseInt(s[i]);
                if(client.idl.contains(t)){
                        s2+="\nCheck Match Results in file\n"+t+"Client.txt file\n";
                        WBox.setText(s2);
                    }
                else {
                    if(s.length>client.max){
                        s2+="Can't Stream more than "+ Client.max +"matches\n";
                        WBox.setText(s2);
                    }
                    else if(!Client.idl.contains(t)){
                        s2+="Can't stream match\n";
                        WBox.setText(s2);
                    }
                }
            }
            MatchID.setText(null);
    }

    public void setClient(Client client) {
        this.client = client;
        s2+="Warning:\nYou can subscribe upto "+client.max+" matches\nEnter match ids separated by space\n";
        WBox.setText(s2);
    }

    public void showList(String s2) {
        List.setText(s2);
    }
}
