package Client;

import javafx.application.Platform;

import java.io.*;
import java.lang.management.PlatformLoggingMXBean;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by saifu on 3/15/2017.
 */
public class ClientThread implements Runnable{

    Thread thread;
    Socket socket;
    BufferedReader br;
    PrintWriter pr;
    OutputStream os;
    InputStream is;
    int cid;



    public ClientThread(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run(){

        try{
            socket = new Socket(Client.serverIP, Client.port);
            Client.connected = true;

            is = socket.getInputStream();
            os = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(is));
            pr = new PrintWriter((os));


            new Thread(){
                @Override
                public void run(){
                    try {
                        Thread.sleep(2000);
                        pr.println("Registration "+Client.Sid);
                        pr.flush();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            while(true){

                String msg = br.readLine();
                if(msg==null || msg.equals("")) continue;
                System.out.println(msg);
                if(msg.equals("Registration Suceeded")){

                        Platform.runLater(() -> {
                            try {
                                Client.showMatches();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                }
                else if (msg.equals("ID out of bound")){
                    Client.controller.ShowNote("Enter valid ID");
                }

                else if(msg.contains("ID")){
                    String[] s = msg.split(" ");
                    cid = Integer.parseInt(s[1]);

                }


                else if(msg.contains("Match")){
                    String[] s1 = msg.split(";");
                    String s2="Mathces:\n";
                    for(int i=1;i<s1.length; i++){
                            s2+=s1[i]+"\n";
                            String intV = s1[i].replaceAll("[^0-9]","");
                            int t = Integer.parseInt(intV);
                            Client.idl.add(t);
                    }

                    final String s3 = s2;

                    new Thread() {
                        public void run(){

                            while(Client.mvcontroller==null){
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Platform.runLater(()->

                                {
                                    try {
                                        Client.mvcontroller.showList(s3);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                         }
                    }.start();
                }


                else if(msg.contains("Max")){
                    String intValue = msg.replaceAll("[^0-9]", "");
                    Client.max = Integer.parseInt(intValue);
                }

                else if (msg.contains("File ")){
                    String[] s = msg.split(" ");
                    String fn = s[1];
                    int fl = Integer.parseInt(s[2]);
                    ReceiveFile(fn,fl);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ReceiveFile(String fn, int fl) {

        try
        {
            byte[] contents;

            FileOutputStream fos = new FileOutputStream(fn+"client.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            int bytesRead = 0;
            int total=0;

            pr.println("Ack");
            pr.flush();

            while(total!=fl)
            {

                String nextChunk = br.readLine();
                String[] x = nextChunk.split("::");
                String id = x[0];
                String current = x[1];
                String size = x[2];

                contents = new byte[Integer.parseInt(size)];
                bytesRead=is.read(contents);
                total+=bytesRead;
                if(bytesRead<=0) break;

                bos.write(contents, 0, bytesRead);
                System.out.println(contents);
                bos.flush();

                pr.println("Ack");
                pr.flush();
            }
            bos.close();
        }
        catch(Exception e)
        {
            System.err.println("Could not transfer file.");
        }
    }
}
