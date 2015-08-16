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
public class Skills {

    //attributes
    private String skillName;
    private String description;
    private String ingameClassName;
    private String imagePath;
    private String skillType;//Passive or Active
    private String coolDown;//recharge skill time
    private List<Requirements> requirements = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(String coolDown) {
        this.coolDown = coolDown;
    }

    
    
    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getIngameClassName() {
        return ingameClassName;
    }

    public void setIngameClassName(String ingameClassName) {
        this.ingameClassName = ingameClassName;
    }

    public List<Requirements> getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements.add(requirements);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    
    
}
