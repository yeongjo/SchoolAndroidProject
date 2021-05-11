package kr.ac.kpu.game.s2016180024.Dodge.game.item;

import kr.ac.kpu.game.s2016180024.Dodge.game.Player;

public class AttackRangeItem extends Item {
    float amount = 1;

    public AttackRangeItem(float amount){
        this.amount = amount;
    }

    public void startMove(Player player){
        player.addAdditiveRadius(amount);
    }

    public void stopMove(Player player){
        player.resetAdditiveRadius();
    }

    public String toString(){
        return "Attack range increase";
    }
}
