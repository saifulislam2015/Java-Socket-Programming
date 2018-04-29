package Server;

import Client.ClientThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by saifu on 3/15/2017.
 */
public class Match {
    int id;
    String team1;
    String team2;
    MatchSimulator mt;
    File file;
    String result="";

    public Match(int id, String team1, String team2) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        mt = new MatchSimulator(this);
        file = new File(System.getProperty("user.dir")+"\\"+id+".txt");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    @Override
    public String toString() {
        return id+" "+team1+" vs "+team2;

    }
}


class MatchSimulator implements Runnable{
    Thread t;
    Match match;
    int score1=0;
    int score2=0;
    int target;
    int bat;
    int w1,w2;


    public MatchSimulator(Match match){


        this.match = match;


        t = new Thread(this);
        t.start();

    }

    public void run(){
        Random rand = new Random();
        int toss = (rand.nextInt(Integer.MAX_VALUE)+1)%2;
        String winner,looser;

        if(toss==0){
                      winner = match.team1;
                      looser = match.team2;
        }
        else {
            winner = match.team2;
            looser = match.team1;
        }

        delay(1);

        match.result+= winner +" won the toss. They are batting first.\n\n";
        innings1(toss);
        match.result+=winner+": "+target+"/"+w1+"\n";
        match.result += looser + " need " + (target+1) + " runs to win.\n\n";
        innings2(toss);
        match.result+=looser +": "+bat+"/"+w2+"\n\n";
        
        if(score1>score2) match.result+=match.team1+ " Won the match";
        else match.result+=match.team2 + " Won the match";


        System.out.println(match.result);
        SendToFile();

    }

    void innings1(int toss){
        int wicket = 0;
        int over = 0;
        int score = 0;

        while(wicket<10 && over<20){

            Random random = new Random();
            over++;

            match.result+=over +" th Over: ";

            for(int i=0;i<6 && wicket<10;i++) {
                int t = (random.nextInt(Integer.MAX_VALUE)+1) % 7;
                if (t != 5) {
                    match.result+=" "+t;
                    score+=t;
                }
                else if (t == 5) {
                    match.result+=" W";
                    wicket++;
                }

            }
            match.result+="\n";

            SendToFile();
            delay(1);


        }
        w1=wicket;
        if(toss==0) {
            score1=score;
            target = score1;
        }
        else {
            score2 =score;
            target = score2;
        }

        match.result+="\n";

    }
    void innings2(int toss){
        int wicket = 0;
        int over = 0;
        int score = 0;

         while (wicket<10 && over <20 && target>=score) {

             Random random = new Random();
             over++;
             match.result+= over + " th Over";

             for(int i=0;i<6 && wicket<10 && target>=score;i++){
                 int t = (random.nextInt(Integer.MAX_VALUE)+1)%7;
                 if(t!=5) {
                     match.result+=" "+t;
                     score+=t;
                 }
                 else {
                     wicket++;
                     match.result+=" W";
                 }
             }
             match.result+="\n";
             SendToFile();
             delay(1);

         }
         w2=wicket;

         if(toss==0) {
             score2 = score;
             bat = score2;
         }
         else {
             score1 = score;
             bat = score1;
         }
         match.result+="\n";

    }


    public void delay(int sec){
        try {
            Thread.sleep(sec*1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SendToFile(){

        byte[] message = match.result.getBytes();

        try {
            FileOutputStream fos = new FileOutputStream(match.file);

            fos.write(message);
            fos.flush();
            fos.close();

            for(WorkerThread wt: ServerMain.hm.get(match.id)){
                wt.sendScore(match.file,match.id);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}