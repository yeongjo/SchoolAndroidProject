package kr.ac.kpu.game.s2016180024.Dodge.game.item;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.game.Player;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;

public class statsItem extends Item {
    public Type type;
    public float amount;

    public enum Type{
        heal, addHp, addStamina, addRadius, addSpeed, subSpeedAddStamina
    }

    public statsItem(Type type, float amount){
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void enterActiveEffect(Player player){
        switch(type){
            case heal:
                player.heal(6);
                break;
            case addHp:
                player.addHp(amount);
                break;
            case addStamina:
                player.addStamina(amount);
                break;
            case addRadius:
                player.addRadius(amount);
                break;
            case addSpeed:
                player.addSpeed(amount);
                break;
            case subSpeedAddStamina:
                player.addSpeed(-amount*40);
                player.addStamina(amount);
                break;
        }
    }

    @Override
    public void onUpdateActivationEffect(Player player){

    }

    @Override
    public String toString() {
        if(type == Type.heal){
            return MainActivity.self.getString(R.string.heal);
        }
        return getTypeName()+" "+getAmountString()+" "+(amount>=0?MainActivity.self.getString(R.string.increase):MainActivity.self.getString(R.string.decrease));
    }

    private String getAmountString(){
        float absAmount = Math.abs(amount);
        float multiplier = 1;
        switch(type){
            case addRadius:
                multiplier = 10;
                break;
            case addSpeed:
                multiplier = 80;
                break;
        }
        if(absAmount >= 0.95*multiplier){
            return MainActivity.self.getString(R.string.a_lot_of);
        }
        if(absAmount >= 0.9*multiplier){
            return MainActivity.self.getString(R.string.a_bunch_of);
        }
        if(absAmount >= 0.7*multiplier){
            return "";
        }
        return MainActivity.self.getString(R.string.a_little_of);
    }

    private String getTypeName(){
        switch(type){
            case heal:
                return "";
            case addHp:
                return MainActivity.self.getString(R.string.hp);
            case addStamina:
                return MainActivity.self.getString(R.string.stamina);
            case addRadius:
                return MainActivity.self.getString(R.string.radius);
            case addSpeed:
                return MainActivity.self.getString(R.string.speed);
            case subSpeedAddStamina:
                return MainActivity.self.getString(R.string.speed_a_little_of_decrease_stamina);
        }
        return "";
    }
}
