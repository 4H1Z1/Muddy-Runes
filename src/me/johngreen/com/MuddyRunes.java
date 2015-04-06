package me.johngreen.com;

import me.johngreen.com.run.RunManager;
import me.johngreen.com.run.RunType;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Equipment;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="Muddy Runes", description="Dynamic mud rune crafter", properties = "client=6")

public class MuddyRunes extends PollingScript<ClientContext> implements PaintListener{

    private static String vertion = "V1_0_1";


    private Paint paint;
    private Math math;
    private MainFrame frame;

    private RunManager manager;

    private boolean doneSetup;

    private boolean debug;

    @Override
    public void start() {
        this.math = new Math();
        this.paint = new Paint(this);
        this.doneSetup = false;
        this.frame = new MainFrame(this);
        this.debug = true;
    }

    public void setUpManager(){
        if(frame.getRoute()==0){
            if(Values.Castle_Wars.contains(ctx.players.local())&&isBaseEquipmentSetup()&&isCastleWarsEquipmentSetup()||ctx.bank.inViewport()&&isBaseEquipmentSetup()&&isCastleWarsEquipmentSetup()){
                manager = new RunManager(RunType.CastleWarsRun,ctx,this);
            }else if(Values.Castle_Wars.contains(ctx.players.local())&&!isBaseEquipmentSetup()||Values.Castle_Wars.contains(ctx.players.local())&&!isCastleWarsEquipmentSetup()){
                manager = new RunManager(RunType.SetupEquipment,ctx,this);
            }
        }else if(frame.getRoute()==1){
            if(Values.Varock_East_Bank_Outside.contains(ctx.players.local())&&isBaseEquipmentSetup()&&isVarrockEastEquipmentSetup()||ctx.bank.inViewport()&&isBaseEquipmentSetup()&&isVarrockEastEquipmentSetup()){
                manager = new RunManager(RunType.VarrockEastRun,ctx,this);
            }else if(Values.Varock_East_Bank_Outside.contains(ctx.players.local())&&!isBaseEquipmentSetup()||Values.Varock_East_Bank_Outside.contains(ctx.players.local())&&!isVarrockEastEquipmentSetup()){
                manager = new RunManager(RunType.SetupEquipment,ctx,this);
            }
        }
    }
    private boolean isBaseEquipmentSetup(){
        return hasEssences()&&hasWaterRunes()&&hasAstrianRunes()&&isNecklassOkay()&&isRingOkay()&&isSteamBattleStaffOkay()&&!hasMudRune();
    }

    public boolean isCastleWarsEquipmentSetup(){
        if(frame.getTalasmanType()==0){
            return hasWillowLog()&&hasWicketHood();
        }else if(frame.getTalasmanType()==1){
            return hasWillowLog()&&hasEarthTiara();
        }
        return false;
    }

    public boolean isVarrockEastEquipmentSetup(){
        if(frame.getTalasmanType()==0){
            return hasWicketHood();
        }else if(frame.getTalasmanType()==1){
            return hasEarthTiara();
        }
        return false;
    }

    @Override
    public void poll(){
        if(!doneSetup)return;
        if (!ctx.game.loggedIn()) return;
        if(manager!=null){
            if(manager.getTask()!=null){
                manager.getTask().execute();
            }else{
                setUpManager();
                //setStatusMessage("Currently a null task");
                return;
            }
            if(manager.isDone()){
                setUpManager();
            }
        }else{
            setUpManager();
            //setStatusMessage("No manager set");
            return;
        }

    }

    public boolean isDebug(){
        return debug;
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }

    @Override
    public void repaint(Graphics g) {
        if (paint != null) paint.repaint(g);
    }

    public void setStatusMessage(String message){
        if(paint!=null) paint.setStatusMessage(message);
    }

    public Math getMath(){
        return math;
    }
    public static String getVertion(){
        return vertion;
    }
    public ClientContext getCTX(){
        return ctx;
    }
    public boolean isRingOkay(){
        for(Integer i:Values.Ring_IDs){
            if(i==ctx.equipment.itemAt(Equipment.Slot.RING).id()){
                return true;
            }
        }
        return false;
    }
    public boolean isNecklassOkay(){
        if(Values.Necklass==ctx.equipment.itemAt(Equipment.Slot.NECK).id()){
            return true;
        }
        return false;
    }
    public boolean isSteamBattleStaffOkay(){
        if(ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id()==Values.Steam_Battle_Staff){
            return true;
        }
        return false;
    }
    public boolean hasWicketHood(){
        if(ctx.equipment.itemAt(Equipment.Slot.HEAD).id()==Values.Wicked_Hood){
            return true;
        }
        return false;
    }
    public boolean hasEarthTiara(){
        if(ctx.equipment.itemAt(Equipment.Slot.HEAD).id()==Values.Earth_Tiara){
            return true;
        }
        return false;
    }
    public boolean hasSmallPouch(){
        return contains(Values.Small_Pouch);
    }
    public boolean hasMediumPouch(){
        return contains(Values.Medium_Pouch_Fixed)||contains(Values.Medium_Pouch_Damaged);
    }
    public boolean hasLargePouch(){
        return contains(Values.Large_Pouch_Fixed)||contains(Values.Large_Pouch_Damaged);
    }
    public boolean hasGiantPouch(){
        return contains(Values.Giant_Pouch_Fixed)||contains(Values.Giant_Pouch_Damaged);
    }
    public boolean hasMassivePouch(){
        return contains(Values.Massive_Pouch_Fixed)||contains(Values.Massive_Pouch_Damaged);
    }
    public boolean hasDamagedPouch(){
        return contains(Values.Medium_Pouch_Damaged)||contains(Values.Large_Pouch_Damaged)||contains(Values.Giant_Pouch_Damaged)||contains(Values.Massive_Pouch_Damaged);
    }
    public boolean hasPouch(){
        return contains(Values.Small_Pouch)||contains(Values.Medium_Pouch_Fixed)||contains(Values.Large_Pouch_Fixed)||contains(Values.Giant_Pouch_Fixed)||contains(Values.Massive_Pouch_Fixed)||contains(Values.Medium_Pouch_Damaged)||contains(Values.Large_Pouch_Damaged)||contains(Values.Giant_Pouch_Damaged)||contains(Values.Massive_Pouch_Damaged);
    }

    public boolean hasEssences(){
        return contains(Values.Essence);
    }
    public boolean hasMudRune(){return contains(Values.Mud_Rune);}
    public boolean hasWaterRunes(){
        return contains(Values.Water_Rune);
    }
    public boolean hasAirRune(){
        return contains(Values.Air_Rune);
    }
    public boolean hasCosmicRune(){
        return contains(Values.Cosmic_Rune);
    }
    public boolean hasAstrianRunes(){
        return contains(Values.Astrial_Rune);
    }
    public boolean hasWillowLog(){
        return contains(Values.Willow_Log);
    }
    public int count(int itemID) {
        return ctx.backpack.select().id(itemID).count();
    }

    public boolean contains(int itemID) {
        return count(itemID) > 0;
    }

    public void doneSetup(){
        doneSetup = true;
    }
    public MainFrame getFrame(){
        return frame;
    }
}
