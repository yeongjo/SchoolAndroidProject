package kr.ac.kpu.game.s2016180024.Dodge.game;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.game.scene.GamePlayScene;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class FollowEnemy extends Enemy {

    protected float minAngle = 3.0f;
    protected Vector2 vel = new Vector2();

    protected FollowEnemy(){
        super();
    }

    public static FollowEnemy get(int level, int x, int y, int speed, float minAngle) {
        MainGame game = MainGame.get();
        FollowEnemy enemy = (FollowEnemy) Scene.getActiveScene().get(FollowEnemy.class);
        if (enemy == null) {
            enemy = new FollowEnemy();
        }

        enemy.init(level, x, y, speed);
        enemy.vel.y = 1;
        enemy.minAngle = minAngle;
        enemy.bitmap = new AnimationGameBitmap(R.mipmap.enemy_02, FRAMES_PER_SECOND, 0);
        return enemy;
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        Player player = GamePlayScene.get().getPlayer();
        float velocityAngle = vel.angle();
        float deltaAngle = player.getPos().cpy().sub(pos).angle() - velocityAngle;
        if(Math.abs(deltaAngle) > minAngle){
            deltaAngle = deltaAngle > 0 ? minAngle : -minAngle;
        }
        vel.rotate(deltaAngle * game.frameTime);
        pos.add(vel.cpy().mul(speed * game.frameTime));

        if (pos.y > GameView.self.getHeight()) {
            Scene.getActiveScene().remove(this);
        }
    }
}
