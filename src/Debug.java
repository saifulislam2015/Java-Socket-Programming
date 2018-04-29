import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.SyncFailedException;
import java.util.ArrayList;

/**
 * Created by saifu on 3/15/2017.
 */
public class Debug {
    public static void main(String[] args) {
        String match = "a b c d e f";
        byte[] xyz = match.getBytes();

        String value = "Hello123";
        String intValue = value.replaceAll("[^0-9]", "");
        int t = Integer.parseInt(intValue);
        System.out.println(t);

        ArrayList<Integer> i;
        i = new ArrayList<>();
        i.add(1);
        i.add(2);
        i.add(3);
        if(i.contains(1)){
            System.out.println("Works");
        }

        File file = new File(System.getProperty("user.dir")+"\\match.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(xyz);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
