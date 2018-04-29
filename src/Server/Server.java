package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by saifu on 3/12/2017.
 */
public class Server implements Runnable {

    public static int WorkerThreadCount =0;

    int id = 1;
    public Server(){
        new Thread(this).start();

    }

    @Override
    public void run(){

        try {
            ServerSocket ss = new ServerSocket(5000);
            System.out.println("Server connected");

            while (true){
                Socket s = ss.accept();


                WorkerThread wt = new WorkerThread(s,id);
                ServerMain.wt.add(wt);

                Thread t = new Thread(wt);
                t.start();
                WorkerThreadCount++;
                id++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class WorkerThread implements Runnable{

    Socket socket;
    BufferedReader br;
    PrintWriter pr;
    InputStream is;
    OutputStream os;
    ArrayList<Integer> sl = new ArrayList<>();
    boolean acknowledged = false;

    private int id = 0;

    public WorkerThread(Socket s,int id){
        this.socket = s;

        try {
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();

            br = new BufferedReader(new InputStreamReader(is));
            pr = new PrintWriter((os));



        } catch (IOException e) {
            e.printStackTrace();
        }

        this.id = id;
    }

    public void run(){

        try{

            while(true){
                String msg = br.readLine();
                if(msg==null || msg.equals("")) continue;
                System.out.println(msg);

                if(msg.contains("Registration")){
                    String[] s = msg.split(" ");
                    int id = Integer.parseInt(s[1]);
                    if(ServerMain.id.contains(id) && (!ServerMain.loglist.contains(id))){
                        pr.println("Registration Suceeded");
                        pr.flush();
                        ServerMain.loglist.add(id);

                        sendID();
                        sendMatches();
                        sendMaxMatch();


                    }
                    else {
                        pr.println("ID out of bound");
                        pr.flush();
                    }
                }
                else if(msg.contains("Subscribed ")){
                    String[] s = msg.split(" ");
                    for (int i=1;i<s.length;i++){
                        sl.add(Integer.parseInt(s[i]));
                        ServerMain.hm.get(Integer.parseInt(s[i])).add(this);
                    }
                }
                else if(msg.contains("Ack")){
                    acknowledged=true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void sendID() {

        String s = "ID "+ id;

        pr.println(s);
        pr.flush();
    }


    public synchronized void sendScore(File file,int id) {

        String pt = "File "+Integer.toString(id)+" ";
        int fileLength = (int) file.length();

        pt+=Integer.toString(fileLength);
        pr.println(pt);
        pr.flush();
        int current = 0;
        byte [] contents;

        try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                while(current!=fileLength){
                    while(!acknowledged){
                        Thread.sleep(10);
                    }
                    acknowledged=false;

                    int size = 256;
                    if (fileLength - current >= size)
                            current += size;
                    else {
                        size = (int) (fileLength - current);
                        current = fileLength;
                    }

                    pr.println(id+".txt::"+current+"::"+size);
                    pr.flush();
                    Thread.sleep(30);
                    contents = new byte[size];

                    bis.read(contents, 0, size);
                    os.write(contents);
                    os.flush();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMaxMatch() {

        String max ="Max"+Integer.toString(ServerMain.maxMatch);

        pr.println(max);
        pr.flush();

    }

    public void sendMatches(){

        String matchmsg="Match";

        for(int i=0;i<ServerMain.matches.size();i++){
            matchmsg+=";"+ServerMain.matches.get(i).toString();

        }
        pr.println(matchmsg);
        pr.flush();
    }
}