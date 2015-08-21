/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Raul
 */
public class ingameClass {
    private final String className;
    private final List<Skills> skillList = new ArrayList<>();
    
    
    public ingameClass(String className, Skills SK) {
        this.className = className;
        this.skillList.add(SK);
    }

    public List<Skills> getSkillList() {
        return skillList;
    }

    public void setSkillList(Skills SK) {
        this.skillList.add(SK);
    }

    @Override
    public String toString() {
        return "ingameClass{" + "className=" + className + ", skillList=" + skillList + '}';
    }
    
}
