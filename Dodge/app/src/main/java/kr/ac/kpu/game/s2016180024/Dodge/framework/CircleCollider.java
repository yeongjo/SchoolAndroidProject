package kr.ac.kpu.game.s2016180024.Dodge.framework;

public class CircleCollider{
    public float radius;
    public Vector2 pos = new Vector2();

    public String toString(){
        return getClass().getSimpleName()+": pos("+pos.x+","+pos.y+") radius("+radius+")";
    }
}
