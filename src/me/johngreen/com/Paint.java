package me.johngreen.com;

import java.awt.*;

import org.powerbot.bot.rt6.client.input.Mouse;
import org.powerbot.script.PaintListener;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Equipment;


public class Paint implements PaintListener {
    private static final Color fontColor = Color.WHITE;
    private static final Color rectColor = new Color(255, 0, 0, 100);
    private static final Font font = new Font("Verdana", 0, 12);
    private String message;
    private MuddyRunes main;

    public Paint(MuddyRunes main){
        this.main = main;
        this.message = "Booting up!";
    }

    public void repaint(Graphics g) {
        if(main.isDebug()){
            drawArea(g);
        }

        g.setColor(Color.lightGray);
        g.fillRect(10, 10, 300, 110);

        g.setColor(Color.GRAY);
        g.fillRect(15, 25, 290, 80);
        g.drawString("Muddy Runes", 12, 22);
        g.drawString("By:John Green", 12, 117);

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Time Running:" + main.getMath().getTime(), 17, 35);
        g.drawString("Status:" + message, 17, 45);
        g.drawString("Run Count:"+main.getMath().getRunCount(), 17, 55);
        g.drawString("Mud Rune Count:"+main.getMath().getCollectedRuneCount(), 17, 65);
        g.drawString("Overall Profit:" + (main.getMath().getMudRunePrice() * main.getMath().getCollectedRuneCount()), 17, 75);


        Point point = main.getCTX().mouse.getLocation();
        g.setColor(Color.CYAN);
        g.drawLine(point.x - 7, point.y, point.x + 7, point.y);
        g.drawLine(point.x, point.y - 7, point.x, point.y + 7);
    }

    public void setStatusMessage(String message){
        this.message = message;
    }


    private void drawArea(Graphics g){
        //Will draw areas soon


        //Draw Outer Area


        //Draw Inner Area


    }

}