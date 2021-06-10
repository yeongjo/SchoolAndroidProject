package kr.ac.kpu.game.s2016180024.Dodge.game.item;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.game.scene.GamePlayScene;
import kr.ac.kpu.game.s2016180024.Dodge.game.HitEffect;
import kr.ac.kpu.game.s2016180024.Dodge.game.MainGame;
import kr.ac.kpu.game.s2016180024.Dodge.game.Player;

public class ExpItem extends ColliableItem {
    private float exp = 1;

    protected ExpItem(int x){
        super(R.mipmap.exp_item);
    }

    public static ExpItem get(int x, float exp) {
        MainGame game = MainGame.get();
        ExpItem expItem = (ExpItem) Scene.getActiveScene().get(ExpItem.class);
        if (expItem == null) {
            expItem = new ExpItem(x);
        }

        expItem.init(x, exp);
        return expItem;
    }

    protected void init(int x, float exp){
        pos.y = 0;
        pos.x = x;
        this.exp = exp;
    }

    public void enterActiveEffect(Player player){
        Sound.play(R.raw.get_exp_item, 0);
        GamePlayScene game = GamePlayScene.get();
        Scene.getActiveScene().add(Scene.Layer.effect, HitEffect.get(R.mipmap.enemy_hit_effect, pos, 0.1f));
        player.addExp(exp);
        game.getScore().addScore(5);
    }
}
