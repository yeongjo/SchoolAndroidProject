package kr.ac.kpu.game.s2016180024.Dodge.game.item;

import kr.ac.kpu.game.s2016180024.Dodge.game.Player;

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
                player.heal(10000);
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
                player.addSpeed(-amount*20);
                player.addStamina(amount);
                break;
        }
    }

    @Override
    public void updateActivateEffect(Player player){

    }

    @Override
    public String toString() {
        if(type == Type.heal){
            return "Heal";
        }
        return getTypeName()+" "+getAmountString()+" "+(amount>=0?"increase":"decrease");
    }

    private String getAmountString(){
        float absAmount = Math.abs(amount);
        float multiplier = 1;
        switch(type){
            case addRadius:
                multiplier = 8;
                break;
            case addSpeed:
                multiplier = 40;
                break;
        }
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

    private String getTypeName(){
        switch(type){
            case heal:
                return "";
            case addHp:
                return "HP";
            case addStamina:
                return "Stamina";
            case addRadius:
                return "Radius";
            case addSpeed:
                return "Speed";
            case subSpeedAddStamina:
                return "Speed a little of decrease, Stamina";
        }
        return "";
    }
}
