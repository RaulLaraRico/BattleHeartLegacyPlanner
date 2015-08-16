/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

/**
 *
 * @author Raul
 */
public class Requirements {
    private String stat;
    private int amount;

    public Requirements(String stat, int amount) {
        this.stat = stat;
        this.amount = amount;
    }
    
    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
}
