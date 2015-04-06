package me.johngreen.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class Math {
    private int secconds;
    private int minnuts;
    private int hours;

    private int runCount;
    private int runeCount;

    private int mudRunePrice;

    private String time;

    private Long startMili;

    private boolean GELookup;

    public Math() {
        this.startMili = System.currentTimeMillis();
        this.secconds = 0;
        this.minnuts = 0;
        this.hours = 0;
        this.runCount = 0;
        this.time = "0:0:0";
        this.GELookup = false;
    }

    public String getTime() {
        if (System.currentTimeMillis() - startMili > 1000) {
            String temp = hours+":";
            this.secconds++;
            if (this.secconds > 59) {
                this.minnuts++;
                this.secconds = 0;
            }
            if (this.minnuts > 59) {
                this.hours++;
                this.minnuts = 0;
            }
            if(this.minnuts<10){
                temp+="0"+this.minnuts+":";
            }else {
                temp += this.minnuts + ":";
            }
            if(this.secconds<10){
                temp+="0"+this.secconds;
            }else{
                temp+=this.secconds;
            }
            this.time = temp;
            this.startMili = System.currentTimeMillis();
        }
        return this.time;
    }

    public int getCollectedRuneCount(){
        return runeCount;
    }
    public void addToRuneCount(int count){
        runeCount += count;
    }

    public int getRunCount(){
        return runCount;
    }
    public int getMudRunePrice(){
        if(!GELookup){
            try{
                mudRunePrice = getPrice(Values.Mud_Rune);
                GELookup = true;
            }catch(IOException e){
                e.printStackTrace();

            }

        }
        return mudRunePrice;
    }

    public void addNewRun(){
        this.runCount++;
    }
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    private int getPrice(int id) throws IOException {
        URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + id);
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String line = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            line += inputLine;
        }
        in.close();
        line = line.substring(line.indexOf("mark_price\":\"")
                + "mark_price\":\"".length());
        line = line.substring(0, line.indexOf("\""));
        return Integer.parseInt(line.replaceAll(",", ""));
    }

}
