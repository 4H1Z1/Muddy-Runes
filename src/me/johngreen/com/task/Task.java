package me.johngreen.com.task;

import me.johngreen.com.MuddyRunes;
import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    private MuddyRunes main;
    public Task(C ctx,MuddyRunes main) {
        super(ctx);
        this.main = main;
    }
    public abstract boolean activate();
    public abstract void execute();
    public void tick(){

    }
}
