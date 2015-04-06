package me.johngreen.com.run;

import me.johngreen.com.MuddyRunes;
import me.johngreen.com.Values;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

import java.util.Random;

/**
 * Created by johngreen on 06/04/15.
 */
public class VarrockEastRun  extends RunTask{
    private Long startTime;
    private int idleTime;
    private ClientContext ctx;
    private MuddyRunes main;
    private boolean hasClickedMagicImbude;
    private Long magicImbudeTimer;
    private Random random;
    private boolean hasFoundRunes;

    public VarrockEastRun(ClientContext ctx,MuddyRunes main){
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
    public boolean activate()  {
        return !isDone();
    }

    @Override
    public void execute() {
        if(!hasFoundRunes&&main.hasMudRune()){
            hasFoundRunes = true;
            main.getMath().addToRuneCount(ctx.backpack.select().id(Values.Mud_Rune).poll().stackSize());
        }
        if(ctx.players.local().inMotion()){
            this.idleTime = main.getMath().randInt(1200, 2200);
            return;
        }
        if(System.currentTimeMillis()>(startTime+idleTime)) {
            this.idleTime = main.getMath().randInt(1700, 4500);
            //Anti ban
            if (random.nextInt(125) == 1) {
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

            if(Values.Varock_East_Bank_Outside.contains(ctx.players.local())){
                if(main.hasEssences()){
                    ctx.movement.step(Values.Way_To_Alter_1.getRandomTile());
                }else if(main.hasMudRune()){
                    ctx.camera.turnTo(ctx.bank.nearest());
                    stop();
                }else{
                    stop();
                }
            }else if(Values.Way_To_Alter_1_Outer.contains(ctx.players.local())){
                if(main.hasEssences()){
                    //walk to alter
                    ctx.movement.step(Values.Way_To_Alter_2.getRandomTile());
                }else{
                    //walks back to bank even if it dose not have mud runes or essenses
                    ctx.movement.step(Values.Varock_East_Bank.getRandomTile());
                }
            }else if(Values.Way_To_Alter_2_Outer.contains(ctx.players.local())){
                if(main.hasEssences()){
                    //walk to alter
                    ctx.movement.step(Values.Way_To_Alter_3.getRandomTile());
                }else{
                    //walks back to bank even if it dose not have mud runes or essenses
                    ctx.movement.step(Values.Way_To_Alter_1.getRandomTile());
                }
            }else if(Values.Way_To_Alter_3_Outer.contains(ctx.players.local())){
                if(main.hasEssences()){
                    //walk to alter
                    ctx.movement.step(Values.Alter_Area_1.getRandomTile());
                }else{
                    //walks back to bank even if it dose not have mud runes or essenses
                    ctx.movement.step(Values.Way_To_Alter_2.getRandomTile());
                }
            }else if(Values.Alter_Area_1.contains(ctx.players.local())){
                if(main.hasEssences()){
                    ctx.objects.select().id(Values.Alter_ID_1).nearest().first().poll().click();
                    main.setStatusMessage("Selecting Alter");
                }else{
                    ctx.movement.step(Values.Way_To_Alter_3.getRandomTile());
                }
            }else if(ctx.objects.select().id(Values.Alter_ID_1).nearest().poll().inViewport()){
                if(main.hasEssences()){
                    ctx.objects.select().id(Values.Alter_ID_1).nearest().first().poll().click();
                    main.setStatusMessage("Selecting Alter");
                }else{
                    ctx.movement.step(Values.Way_To_Alter_3.getRandomTile());
                }
            }else if(Values.Alter_Portal_Area.contains(ctx.players.local())){
                if(main.hasEssences()){
                    //walk to alter
                    ctx.camera.turnTo(ctx.objects.select().id(Values.Alter_ID_2).nearest().poll());
                    ctx.movement.step(Values.Alter_Area_2.getRandomTile());
                }else{
                    //walk back
                    final GameObject obj = ctx.objects.select().id(Values.Alter_Portal).nearest().poll();
                    obj.interact("");
                }
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
            }else if(Values.Alter_Area_2.contains(ctx.players.local())&&main.hasMudRune()||ctx.objects.select().id(Values.Alter_ID_2).nearest().poll().inViewport()&&main.hasMudRune()) {
                //start walk back
                ctx.movement.step(Values.Alter_Portal_Area.getRandomTile());
            }


            this.startTime = System.currentTimeMillis();
        }

    }
}
