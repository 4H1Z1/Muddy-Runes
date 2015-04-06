package me.johngreen.com.run;

import me.johngreen.com.MuddyRunes;
import me.johngreen.com.Values;
import org.powerbot.script.rt6.Bank;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

import java.util.Random;

public class SetupEquipment extends RunTask{
    private Long startTime;
    private int idleTime;
    private MuddyRunes main;

    //has filled
    private boolean small;
    private boolean medium;
    private boolean large;
    private boolean giant;
    private boolean massive;
    private int withdrawMode;
    private boolean scroledDown;

    private int scrollCounter;
    private int runType;
    private boolean npcContact;
    private int talasmanType;

    public SetupEquipment(ClientContext ctx, MuddyRunes main){
        super(ctx,main);
        this.main = main;
        this.startTime = System.currentTimeMillis();
        this.idleTime = main.getMath().randInt(1500, 4000);
        this.small = false;
        this.medium = false;
        this.large = false;
        this.giant = false;
        this.massive = false;
        this.scroledDown = false;
        this.scrollCounter = 0;
        this.runType = main.getFrame().getRoute();
        this.talasmanType = main.getFrame().getTalasmanType();
        this.npcContact = main.getFrame().npcContact();
        Random r = new Random();
        this.withdrawMode = r.nextInt(3);
    }

    @Override
    public boolean activate() {
        return !isDone();
    }

    @Override
    public void execute(){
        if(System.currentTimeMillis()>(startTime+idleTime)) {
            this.idleTime = main.getMath().randInt(2000, 3200);
            this.startTime = System.currentTimeMillis();

            if(main.contains(Values.Cosmic_Rune)&&main.contains(Values.Air_Rune)){
                //Fix Pouches
                if(!ctx.widgets.widget(88).valid()){
                    ctx.widgets.widget(Values.Task_Bar).component(Values.Npc_Contact).interact("");
                    return;
                }
            }

            //open bank if required
            if(!ctx.bank.opened()){
                if(npcContact&&main.hasDamagedPouch()){
                    //npc contact
                    if(ctx.widgets.widget(88).valid()){
                        //clicking on portrate
                        if(scroledDown){
                            ctx.widgets.widget(88).component(6).component(14).interact("");
                            this.idleTime = main.getMath().randInt(6000, 6500);

                            this.scrollCounter = 0;
                        }else{
                            if(scrollCounter<6){
                                ctx.widgets.widget(88).component(7).component(5).interact("");
                                scrollCounter++;
                                this.idleTime = main.getMath().randInt(500, 1500);
                            }else{
                                scroledDown = true;
                            }
                        }
                    }else if(ctx.widgets.widget(1191).valid()){
                        ctx.widgets.widget(1191).component(9).interact("");
                        this.idleTime = main.getMath().randInt(700, 1200);
                    }else if(ctx.widgets.widget(1184).valid()){
                        ctx.widgets.widget(1184).component(11).interact("");
                        this.idleTime = main.getMath().randInt(700, 1200);
                    }else if(ctx.widgets.widget(1188).valid()){
                        ctx.widgets.widget(1188).component(12).interact("");
                        this.idleTime = main.getMath().randInt(700, 1200);
                    }else{
                        if(!ctx.bank.inViewport()){
                            main.setStatusMessage("Can't see bank");
                            ctx.camera.turnTo(ctx.objects.select().id(Values.Bank_Chest).nearest().poll());
                        }else if(!ctx.bank.opened()){
                            ctx.bank.open();
                            main.setStatusMessage("Opening bank");
                        }
                    }
                }else{
                    //Checks for items
                    if(!main.hasEssences()||main.hasMudRune()||!main.hasWicketHood()&&talasmanType==0||!main.hasEarthTiara()&&talasmanType==1||!main.hasWillowLog()&&runType==0||!main.isNecklassOkay()||!main.isRingOkay()||!main.isSteamBattleStaffOkay()){
                        if(!ctx.bank.inViewport()){
                            main.setStatusMessage("Can't see bank");
                            ctx.camera.turnTo(ctx.objects.select().id(Values.Bank_Chest).nearest().poll());
                        }else if(!ctx.bank.opened()){
                            ctx.bank.open();
                            main.setStatusMessage("Opening bank");
                        }
                    }else{
                        stop();
                    }
                }
            }else{
                if(main.hasMudRune()) {
                    ctx.bank.deposit(Values.Mud_Rune, Bank.Amount.ALL);
                }else if(!main.isRingOkay()){
                    Item item = ctx.bank.select().id(Values.Ring_IDs[0]).poll();
                    item.interact("Wear");
                }else if(!main.isSteamBattleStaffOkay()){
                    Item item = ctx.bank.select().id(Values.Steam_Battle_Staff).poll();
                    item.interact("Wield");
                }else if(talasmanType==0&&!main.hasWicketHood()){
                    Item item = ctx.bank.select().id(Values.Wicked_Hood).poll();
                    item.interact("Wear");
                }else if(talasmanType==1&&!main.hasEarthTiara()){
                    Item item = ctx.bank.select().id(Values.Earth_Tiara).poll();
                    item.interact("Wear");
                }else if(!main.isNecklassOkay()){
                    Item item = ctx.bank.select().id(Values.Necklass).poll();
                    item.interact("Wear");
                }else if(main.hasSmallPouch()&&!small){  //Fills pouches before checking for broken ones because they break during filling
                    small = true;
                    final Item pouch = ctx.backpack.select().id(Values.Small_Pouch).poll();
                    pouch.interact("Fill");
                    return;
                }else if(main.hasMediumPouch()&&!medium){
                    medium = true;
                    final Item pouch = ctx.backpack.select().id(Values.Medium_Pouch_Fixed).poll();
                    pouch.interact("Fill");
                    return;
                }else if(main.hasLargePouch()&&!large){
                    large = true;
                    final Item pouch = ctx.backpack.select().id(Values.Large_Pouch_Fixed).poll();
                    pouch.interact("Fill");
                    return;
                }else if(main.hasGiantPouch()&&!giant){
                    giant = true;
                    final Item pouch = ctx.backpack.select().id(Values.Giant_Pouch_Fixed).poll();
                    pouch.interact("Fill");
                    return;
                }else if(main.hasMassivePouch()&&!massive){
                    massive = true;
                    final Item pouch = ctx.backpack.select().id(Values.Massive_Pouch_Fixed).poll();
                    pouch.interact("Fill");
                    return;
                }else if(main.hasDamagedPouch()){//Check for broken pouches
                    if(npcContact){
                        //gets items for npc contact
                        if(!main.contains(Values.Air_Rune)){
                            if(main.count(Values.Air_Rune)==1){
                                ctx.bank.withdraw(Values.Air_Rune, Bank.Amount.ONE);
                            }else{
                                ctx.bank.withdraw(Values.Air_Rune,2);
                            }
                        }else if(!main.hasCosmicRune()){
                            ctx.bank.withdraw(Values.Cosmic_Rune, Bank.Amount.ONE);
                        }else{
                            ctx.bank.close();
                        }
                    }else{
                        //Banks Damaged Pouches
                        bankDamagedPouch();
                    }
                }else if(runType==0&&!main.contains(Values.Willow_Log)){
                    ctx.bank.withdraw(Values.Willow_Log, Bank.Amount.ONE);
                }else if(!main.contains(Values.Essence)){
                    ctx.bank.withdraw(Values.Essence, Bank.Amount.ALL);
                }else{
                    stop();
                }





            }
        }
    }

    public void bankDamagedPouch(){
        if(main.contains(Values.Medium_Pouch_Damaged)){
            ctx.bank.deposit(Values.Medium_Pouch_Damaged, Bank.Amount.ALL);
        }else if(main.contains(Values.Large_Pouch_Damaged)) {
            ctx.bank.deposit(Values.Large_Pouch_Damaged, Bank.Amount.ALL);
        }else if(main.contains(Values.Giant_Pouch_Damaged)) {
            ctx.bank.deposit(Values.Giant_Pouch_Damaged, Bank.Amount.ALL);
        }else if(main.contains(Values.Massive_Pouch_Damaged)) {
            ctx.bank.deposit(Values.Massive_Pouch_Damaged, Bank.Amount.ALL);
        }
    }
}
