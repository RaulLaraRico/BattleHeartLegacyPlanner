/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleheartlegacybuilder;

import classes.Requirements;
import classes.Skills;
import classes.ingameClass;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.plaf.IconUIResource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Raul
 */
public class mainWindow extends javax.swing.JFrame {

    //variables
    //sizes
    final int notPassiveSkillsNumber = 10;
    final int PassiveSkillsNumber = 5;
    //Current character builds
    List<JLabel> currentSkills = new ArrayList<>();
    List<JLabel> currentPassives = new ArrayList<>();
    //Labels with the skill images
    List<JLabel> ingameClassLabelsSkills = new ArrayList<>();
    // List of ingame class plus all the skills: Barbarian and 15 barbarian skills
    HashMap<String, ingameClass> dictionary = new HashMap<>();
    //List<ingameClass> ingameClassList = new ArrayList<>();

    //classes(ingame characters) array
    //the order of the classes inside the array is very important, we use the index from the combobox to load the correct info about skills
    String[] classes = {"Barbarian", "Bard", "Battlemage", "Knight",
        "Monk", "Necromancer", "Ninja", "Paladin", "Ranger", "Rogue", "Witch", "Wizzard"};

    /**
     * Creates new form mainWindow
     */
    public mainWindow() {
        createInGameClassesFromJson();
        initComponents();
        fillLabelLists();
        buildGridskills();
        fillComboClasses();

        this.pnl_skills.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.pnl_passives.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.pnl_skillsGrids.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

        hideSkillsPanel();

    }

    private void fillLabelLists() {
        //this sux cause i set the labels not programically 
        currentSkills.add(this.lbl_skillSlot1);
        currentSkills.add(this.lbl_skillSlot2);
        currentSkills.add(this.lbl_skillSlot3);
        currentSkills.add(this.lbl_skillSlot4);
        currentSkills.add(this.lbl_skillSlot5);
        currentSkills.add(this.lbl_skillSlot6);

        currentPassives.add(this.lbl_str);
        currentPassives.add(this.lbl_dex);
        currentPassives.add(this.lbl_int);
        currentPassives.add(this.lbl_skl);
        currentPassives.add(this.lbl_end);
        currentPassives.add(this.lbl_cha);
    }

    private void fillComboClasses() {
        for (String classe : this.classes) {
            this.cmb_clases.addItem(classe);
        }
    }

    //method to build de skillsGrid
    private void buildGridskills() {
        int xPosition = 0;
        int nextRow = 0;

        for (int i = 0; i < notPassiveSkillsNumber; i++) {
            JLabel label = new JLabel("");
            label.setSize(50, 50);
            if (i == 4 || i == 8) {
                nextRow += 60;
                xPosition = 0;
            }
            label.setLocation((xPosition * 60) + 5, 30 + nextRow);
            xPosition++;

            label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png")));
            this.pnl_skillsGrids.add(label);
            ingameClassLabelsSkills.add(label);
        }
    }

    private void fillGridSkills(String ingameclassName) {

        //getting the ingameClasses object
        System.out.println(ingameclassName);
        ingameClass singleClass = this.dictionary.get(ingameclassName);
        List<Skills> singleClassSkills = singleClass.getSkillList();
        System.out.println("SIZE " + singleClassSkills.size());
        int counter = 0;
        for (JLabel ingameClassLabelsSkill : this.ingameClassLabelsSkills) {
            System.out.println(counter);
            String iconPath = singleClass.getSkillList().get(counter).getImagePath();
            counter++;
            System.out.println("/media/images/" + iconPath);
            ingameClassLabelsSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/" + iconPath)));
        }
    }

    private void createInGameClassesFromJson() {
        String filePath = mainWindow.class.getResource("/classes/data.json").getPath();
        try {
            FileReader reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONArray ingameClasses = (JSONArray) jsonObject.get("ingameClass");
            //set object (hashMap)
            Set set = this.dictionary.entrySet();
            Iterator iteratorSet = set.iterator();

            for (Object ingameClasse : ingameClasses) {
                JSONObject currentJsonObject = (JSONObject) ingameClasse;
                String className = (String) currentJsonObject.get("className");

                //once we have the name of the ingameClass we need the skillsArray
                JSONArray skills = (JSONArray) currentJsonObject.get("skills");

                //list of skills
                List<Skills> skillList = new ArrayList<>();
                for (Object skill : skills) {
                    Skills classSkills = new Skills();
                    JSONObject currentSkillObject = (JSONObject) skill;

                    //adding skills data to skills object
                    classSkills.setSkillName((String) currentSkillObject.get("skillName"));
                    classSkills.setSkillType((String) currentSkillObject.get("type"));
                    classSkills.setImagePath((String) currentSkillObject.get("imgurl"));
                    classSkills.setDescription((String) currentSkillObject.get("description"));
                    classSkills.setCoolDown((String) currentSkillObject.get("cooldown"));
                    classSkills.setIngameClassName(className);

                    //we need a variable for the requirements array
                    JSONObject requirementsJson = (JSONObject) currentSkillObject.get("requirements");
                    //loop to get the requirements form each skill
                    for (Iterator iterator = requirementsJson.keySet().iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        String value = (String) requirementsJson.get(key);
                        Requirements requir = new Requirements(key, Integer.parseInt(value));
                        classSkills.setRequirements(requir);
                    }

                    skillList.add(classSkills);

                }//skills for
                //ingameClass Object
                ingameClass ingameClassObject = new ingameClass(className, skillList);
                this.dictionary.put(className, ingameClassObject);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(mainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(mainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_main = new javax.swing.JPanel();
        lbl_str = new javax.swing.JLabel();
        lbl_dex = new javax.swing.JLabel();
        lbl_int = new javax.swing.JLabel();
        lbl_skl = new javax.swing.JLabel();
        lbl_end = new javax.swing.JLabel();
        lbl_cha = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmb_clases = new javax.swing.JComboBox();
        pnl_passives = new javax.swing.JPanel();
        lbl_skillSlot6 = new javax.swing.JLabel();
        lbl_skillSlot5 = new javax.swing.JLabel();
        lbl_skillSlot4 = new javax.swing.JLabel();
        lbl_skillSlot3 = new javax.swing.JLabel();
        lbl_skillSlot2 = new javax.swing.JLabel();
        lbl_skillSlot1 = new javax.swing.JLabel();
        pnl_skillsGrids = new javax.swing.JPanel();
        pnl_skills = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lbl_background = new javax.swing.JLabel();
        btn_passives = new javax.swing.JButton();
        btn_skills = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_main.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_str.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lbl_str.setForeground(new java.awt.Color(255, 255, 255));
        lbl_str.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_str.setText("5");
        pnl_main.add(lbl_str, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 60, 30));

        lbl_dex.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lbl_dex.setForeground(new java.awt.Color(255, 255, 255));
        lbl_dex.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_dex.setText("5");
        pnl_main.add(lbl_dex, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 80, 40));

        lbl_int.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lbl_int.setForeground(new java.awt.Color(255, 255, 255));
        lbl_int.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_int.setText("5");
        pnl_main.add(lbl_int, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 80, 40));

        lbl_skl.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lbl_skl.setForeground(new java.awt.Color(255, 255, 255));
        lbl_skl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_skl.setText("5");
        pnl_main.add(lbl_skl, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 100, 50));

        lbl_end.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lbl_end.setForeground(new java.awt.Color(255, 255, 255));
        lbl_end.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_end.setText("5");
        pnl_main.add(lbl_end, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 100, 40));

        lbl_cha.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lbl_cha.setForeground(new java.awt.Color(255, 255, 255));
        lbl_cha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_cha.setText("5");
        pnl_main.add(lbl_cha, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 100, 40));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Class:");
        pnl_main.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 50, 30));

        cmb_clases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_clasesActionPerformed(evt);
            }
        });
        pnl_main.add(cmb_clases, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 54, 160, -1));

        pnl_passives.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnl_passives.setBackground(new Color(1.0f, 1.0f, 1.0f, 1.0f));

        lbl_skillSlot6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_skillSlot6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        pnl_passives.add(lbl_skillSlot6, new org.netbeans.lib.awtextra.AbsoluteConstraints(229, 139, 50, 60));

        lbl_skillSlot5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_skillSlot5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        pnl_passives.add(lbl_skillSlot5, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 71, 50, 60));

        lbl_skillSlot4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_skillSlot4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        pnl_passives.add(lbl_skillSlot4, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 10, 50, 50));

        lbl_skillSlot3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_skillSlot3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        pnl_passives.add(lbl_skillSlot3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 50, 50));

        lbl_skillSlot2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_skillSlot2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        pnl_passives.add(lbl_skillSlot2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 60, 60));

        lbl_skillSlot1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_skillSlot1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        lbl_skillSlot1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_skillSlot1MouseClicked(evt);
            }
        });
        pnl_passives.add(lbl_skillSlot1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 139, 60, 60));

        pnl_main.add(pnl_passives, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 300, 270));

        pnl_skillsGrids.setBackground(new java.awt.Color(0, 0, 0));
        pnl_skillsGrids.setLayout(null);
        pnl_main.add(pnl_skillsGrids, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 80, 240, 270));

        pnl_skills.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 169, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel3.setText("jLabel3");
        jLabel3.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 101, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        jLabel4.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 34, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel5.setText("jLabel5");
        jLabel5.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 12, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel6.setText("jLabel6");
        jLabel6.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 12, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel7.setText("jLabel7");
        jLabel7.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 34, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel8.setText("jLabel8");
        jLabel8.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 101, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/OtherImages/skillNotSlected.png"))); // NOI18N
        jLabel9.setText("jLabel9");
        jLabel9.setPreferredSize(new java.awt.Dimension(50, 50));
        pnl_skills.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 169, -1, -1));

        pnl_main.add(pnl_skills, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 300, 270));

        lbl_background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/Backgrounds/passivessmall.png"))); // NOI18N
        pnl_main.add(lbl_background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btn_passives.setText("Passives");
        btn_passives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_passivesActionPerformed(evt);
            }
        });
        pnl_main.add(btn_passives, new org.netbeans.lib.awtextra.AbsoluteConstraints(387, 0, 100, 40));

        btn_skills.setText("Skills");
        btn_skills.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_skillsActionPerformed(evt);
            }
        });
        pnl_main.add(btn_skills, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 100, 40));

        getContentPane().add(pnl_main, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_skillsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_skillsActionPerformed

        lbl_background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/Backgrounds/skillssmall.png"))); // NOI18N
        hidePassivesPanel();
    }//GEN-LAST:event_btn_skillsActionPerformed

    public void hidePassivesPanel() {
        this.pnl_skills.setVisible(true);
        this.pnl_passives.setVisible(false);
    }

    public final void hideSkillsPanel() {
        this.pnl_skills.setVisible(false);
        this.pnl_passives.setVisible(true);

    }

    private void btn_passivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_passivesActionPerformed
        lbl_background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/images/Backgrounds/passivessmall.png"))); // NOI18N
        hideSkillsPanel();
    }//GEN-LAST:event_btn_passivesActionPerformed

    private void lbl_skillSlot1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_skillSlot1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_skillSlot1MouseClicked

    private void cmb_clasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_clasesActionPerformed
        // TODO add your handling code here:
        String classSelected = this.cmb_clases.getSelectedItem().toString();
        fillGridSkills(classSelected);
    }//GEN-LAST:event_cmb_clasesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_passives;
    private javax.swing.JButton btn_skills;
    private javax.swing.JComboBox cmb_clases;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbl_background;
    private javax.swing.JLabel lbl_cha;
    private javax.swing.JLabel lbl_dex;
    private javax.swing.JLabel lbl_end;
    private javax.swing.JLabel lbl_int;
    private javax.swing.JLabel lbl_skillSlot1;
    private javax.swing.JLabel lbl_skillSlot2;
    private javax.swing.JLabel lbl_skillSlot3;
    private javax.swing.JLabel lbl_skillSlot4;
    private javax.swing.JLabel lbl_skillSlot5;
    private javax.swing.JLabel lbl_skillSlot6;
    private javax.swing.JLabel lbl_skl;
    private javax.swing.JLabel lbl_str;
    private javax.swing.JPanel pnl_main;
    private javax.swing.JPanel pnl_passives;
    private javax.swing.JPanel pnl_skills;
    private javax.swing.JPanel pnl_skillsGrids;
    // End of variables declaration//GEN-END:variables
}
