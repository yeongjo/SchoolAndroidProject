package kr.ac.kpu.game.s2016180024.Dodge.game.item;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.game.Enemy;
import kr.ac.kpu.game.s2016180024.Dodge.game.Player;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;

public class LifeStealItem extends Item {

    public float stealRatio;

    public LifeStealItem(float stealRatio){
        this.stealRatio = stealRatio;
    }

    public void onAttack(Player player, Enemy enemy){
        player.heal(enemy.getDamage() * stealRatio);
    }

    public String toString(){

        return getAmountString()+" "+MainActivity.self.getString(R.string.life_steal);
    }

    private String getAmountString(){
        float absAmount = Math.abs(stealRatio);
        float multiplier = 0.1f;
        if(absAmount >= 4*multiplier){
            return MainActivity.self.getString(R.string.a_lot_of);
        }
        if(absAmount >= 3*multiplier){
            return MainActivity.self.getString(R.string.a_bunch_of);
        }
        if(absAmount >= 2*multiplier){
            return "";
        }
        return MainActivity.self.getString(R.string.a_little_of);
    }
}
