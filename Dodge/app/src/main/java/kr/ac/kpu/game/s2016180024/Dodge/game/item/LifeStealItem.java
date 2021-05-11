package kr.ac.kpu.game.s2016180024.Dodge.game.item;

import kr.ac.kpu.game.s2016180024.Dodge.game.Enemy;
import kr.ac.kpu.game.s2016180024.Dodge.game.Player;

public class LifeStealItem extends Item {

    public float stealRatio;

    public LifeStealItem(float stealRatio){
        this.stealRatio = stealRatio;
    }

    public void onAttack(Player player, Enemy enemy){
        player.heal(enemy.getDamage() * stealRatio);
    }

    public String toString(){

        return getAmountString()+" Life Steal";
    }

    private String getAmountString(){
        float absAmount = Math.abs(stealRatio);
        float multiplier = 0.1f;
        if(absAmount >= 4*multiplier){
            return "a lot of";
        }
        if(absAmount >= 3*multiplier){
            return "a bunch of";
        }
        if(absAmount >= 2*multiplier){
            return "";
        }
        return "a little of";
    }
}
