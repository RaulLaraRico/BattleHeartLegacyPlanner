/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.util.List;

/**
 *
 * @author Raul
 */
public class ingameClass {
    private String className;
    List<Skills> skillList;
    
    public ingameClass(String className, Skills SK) {
        
    }

    public Skills getSkillList(String skillName) {
        return skillList.get(0);
    }

    public void setSkillList(Skills SK) {
        this.skillList.add(SK);
    }
    
    
}
