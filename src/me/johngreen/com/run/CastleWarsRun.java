package me.johngreen.com.run;

import me.johngreen.com.MuddyRunes;
import me.johngreen.com.Values;
import org.powerbot.script.rt6.Action;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

import java.util.Random;

public class CastleWarsRun extends RunTask{
    private Long startTime;
    private int idleTime;
    private ClientContext ctx;
    private MuddyRunes main;
    private boolean hasClickedMagicImbude;
    private Long magicImbudeTimer;
    private Random random;
    private boolean hasFoundRunes;

    public CastleWarsRun(ClientContext ctx,MuddyRunes main){
        super(ctx,main);
        this.startTime = System.currentTimeMillis();
        this.idleTime = main.getMath().randInt(1500, 4000);
        this.ctx = ctx;
        this.main = main;
        this.hasClickedMagicImbude = false;
        this.magicImbudeTimer = 0L;
        this.random = new Random();
        this.hasFoundRunes = false;
    }

    @Override
    public boolean activate() {
        return !isDone();
    }

    @Override
    public void execute() {
        if(!hasFoundRunes&&main.hasMudRune()){
            hasFoundRunes = true;
            main.getMath().addToRuneCount(ctx.backpack.select().id(Values.Mud_Rune).poll().stackSize());
        }
        if(hasClickedMagicImbude){
            if((magicImbudeTimer+10000)<System.currentTimeMillis()){
                hasClickedMagicImbude = false;
            }
        }
        if(ctx.players.local().inMotion()){
            this.idleTime = main.getMath().randInt(1200, 2200);
            return;
        }
        if(System.currentTimeMillis()>(startTime+idleTime)) {
            this.idleTime = main.getMath().randInt(1700, 4500);
            //Anti ban
            if(random.nextInt(125)==1) {
                switch (random.nextInt(3)) {
                    case 0:
                        this.idleTime = main.getMath().randInt(25000, 65000);
                        break;
                    case 1:
                        this.idleTime = main.getMath().randInt(12000, 25000);
                        break;
                    case 2:
                        this.idleTime = main.getMath().randInt(17000, 40000);
                        break;
                }
                main.setStatusMessage("Sleeping for " + (idleTime / 1000) + " secconds");
                this.startTime = System.currentTimeMillis();
                return;
            }


            if(Values.Castle_Wars.contains(ctx.players.local())||ctx.bank.inViewport()){
                //all castle wars based actions
                if(main.hasEssences()){
                    //walk to baloon
                    ctx.movement.step(Values.Ballon_Inner_Area_1.getRandomTile());
                    main.setStatusMessage("Walking to Baloon");
                }else if(main.hasMudRune()){
                    //get closer to bank before switching to SetupEquipment
                    ctx.movement.step(Values.Castle_Wars_Bank.getRandomTile());
                    main.setStatusMessage("Walking to bank");
                    stop();
                    return;
                }
            }else if(Values.Ballon_Outer_Area_1.contains(ctx.players.local())){
                //interacting with the baloon
                if(ctx.widgets.widget(Values.Ballon_Menu).valid()){
                    //is in menu
                    ctx.widgets.widget(Values.Ballon_Menu).component(Values.Varrock_Button).interact("");
                    this.idleTime = main.getMath().randInt(7500, 10000);
                    main.setStatusMessage("Clicking");
                }else{
                    ctx.objects.select().id(Values.Hot_Air_Baloon).nearest().first().poll().click();
                    ctx.camera.turnTo(ctx.objects.select().id(Values.Hot_Air_Baloon).nearest().poll());
                    main.setStatusMessage("Clicking on baloon");
                }
            }else if(Values.Ballon_Area_2.contains(ctx.players.local())) {
                ctx.movement.step(Values.Alter_Area_1.getRandomTile());
                ctx.camera.turnTo(ctx.objects.select().id(Values.Alter_ID_1).nearest().poll());
                main.setStatusMessage("Walking to alter");
            }else if (ctx.objects.select().id(Values.Alter_ID_1).nearest().poll().inViewport()) {
                ctx.objects.select().id(Values.Alter_ID_1).nearest().first().poll().click();
                main.setStatusMessage("Selecting Alter");
            }else if(Values.Alter_Area_1.contains(ctx.players.local())){
                ctx.objects.select().id(Values.Alter_ID_1).nearest().first().poll().click();
                main.setStatusMessage("Selecting Alter");
            }else if(Values.Alter_Portal_Area.contains(ctx.players.local())){
                    ctx.camera.turnTo(ctx.objects.select().id(Values.Alter_ID_2).nearest().poll());
                    ctx.movement.step(Values.Alter_Area_2.getRandomTile());
            }else if(Values.Alter_Area_2.contains(ctx.players.local())&&!main.hasMudRune()||ctx.objects.select().id(Values.Alter_ID_2).nearest().poll().inViewport()&&!main.hasMudRune()){
                if(hasClickedMagicImbude){
                    main.setStatusMessage("Crafting....");
                    Item item = ctx.backpack.select().id(Values.Water_Rune).poll();
                    final GameObject obj = ctx.objects.select().id(Values.Alter_ID_2).nearest().poll();
                    if(item.interact("Use")){
                        obj.interact("");
                        main.getMath().addNewRun();
                    }
                }else{
                    hasClickedMagicImbude = true;
                    magicImbudeTimer = System.currentTimeMillis();
                    main.setStatusMessage("Clicking Magic Imbue");
                    ctx.widgets.widget(Values.Task_Bar).component(Values.Magic_Imbude).interact("");
                    this.idleTime = main.getMath().randInt(1500, 3500);
                }
            }else if(Values.Alter_Area_2.contains(ctx.players.local())&&main.hasMudRune()||ctx.objects.select().id(Values.Alter_ID_2).nearest().poll().inViewport()&&main.hasMudRune()){
                if(ctx.widgets.widget(Values.Ring_Of_duling_Teleport_Menu).valid()){
                    ctx.widgets.widget(Values.Ring_Of_duling_Teleport_Menu).component(Values.Castle_Wars_Button).interact("");
                    main.setStatusMessage("Clicking Castle Wars");
                    this.idleTime = main.getMath().randInt(5000, 7000);
                }else{
                    ctx.widgets.widget(Values.Task_Bar).component(Values.Ring_Of_Duling_Button).interact("");
                    main.setStatusMessage("Clicking Ring Of Recoil");
                    this.idleTime = main.getMath().randInt(1000, 3000);
                }
            }else if(Values.Castle_Wars.contains(ctx.players.local())&&main.hasMudRune()){
                stop();
            }
            this.startTime = System.currentTimeMillis();
        }
    }
}
