package me.johngreen.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by johngreen on 04/04/15.
 */
public class MainFrame {
    private JFrame frame;
    private MuddyRunes main;
    public JButton startButton;
    private JPanel pnl1;
    private JComboBox runDropdown,talasmanType;
    private JCheckBox npcContact;

    public MainFrame(MuddyRunes main){
        this.frame = new JFrame("Muddy Runes Options");
        this.main = main;
        setupFrame();
    }
    public void setupFrame(){
        frame.setSize(250, 140);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(1, 1));
        pnl1 = new JPanel(new GridLayout(6,1));
        npcContact = new JCheckBox("Use NPC contact on pouch decay?");
        npcContact.setSelected(true);
        frame.add(pnl1);
        runDropdown = new JComboBox();
        String[] runValues = new String[]{"Castle Wars","Varrock East"};
        for(String s:runValues){
            runDropdown.addItem(s);
        }
        talasmanType = new JComboBox();
        String[] talasmanValues = new String[]{"Wicked Hood","Earth Tiara"};
        for(String s:talasmanValues){
            talasmanType.addItem(s);
        }

        startButton = new JButton();
        startButton.setText("Start");
        startButton.setPreferredSize(new Dimension(80, 30));
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                main.doneSetup();
                frame.setVisible(false);
            }
        });
        pnl1.add(new JLabel("Bank Location"));
        pnl1.add(runDropdown);
        pnl1.add(new JLabel("Talasman Type"));
        pnl1.add(talasmanType);
        pnl1.add(npcContact);
        pnl1.add(startButton);
        frame.setVisible(true);
    }


    public boolean npcContact(){
        return npcContact.isSelected();
    }
    public int getRoute(){
            return runDropdown.getSelectedIndex();
    }
    public int getTalasmanType(){
        return talasmanType.getSelectedIndex();
    }
}
