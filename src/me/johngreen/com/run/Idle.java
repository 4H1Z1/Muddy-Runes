package me.johngreen.com.run;

import me.johngreen.com.MuddyRunes;
import org.powerbot.script.rt6.ClientContext;

import java.util.Random;

public class Idle extends RunTask{
    private Long startTime;
    private int idleTime;
    public Idle(ClientContext ctx, MuddyRunes main){
        super(ctx,main);
        this.startTime = System.currentTimeMillis();
        this.idleTime = main.getMath().randInt(1500,8000);
    }

    @Override
    public boolean activate() {
        return !isDone();
    }

    @Override
    public void execute() {
        if(System.currentTimeMillis()>(startTime+idleTime)){
            stop();
        }
    }
}
