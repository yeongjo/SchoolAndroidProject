package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class Leaderboard implements GameObject {
    private static final String TAG = Leaderboard.class.getSimpleName();

    private StringBuilder leaderboardStringBuilder = new StringBuilder();
    private DatabaseReference rootRef;
    private ArrayList<SavedData> rankBoards = new ArrayList<>();
    private Typeface tf;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Vector2 pos = new Vector2();

    public Leaderboard(float x, float y){
        pos.set(x,y);
        tf = Typeface.createFromAsset(GameView.self.getResources().getAssets(), "pfstardust.ttf");
        rootRef = FirebaseDatabase.getInstance("https://dodge-a8173-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        paint.setTextSize(GameView.MULTIPLIER * 10);
        paint.setTypeface(tf);
        paint.setColor(0xffffffff);
        paint.setTextAlign(Paint.Align.RIGHT);
    }

    public void addToLeaderBoard(String name, int score){
        SavedData savedData = new SavedData(name, score);
        rootRef.child("user").child(name).child("score").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Object value = task.getResult().getValue();
                if(value != null){
                    savedData.score = Math.max(((Long)value).intValue(), savedData.score);
                }
                rootRef.child("user").child(name).setValue(savedData).addOnCompleteListener(task1 -> {
                    updateLeaderboard();
                });
                Log.d(TAG, "add score to leaderboard: "+savedData);
            }
        });
    }

    public void updateLeaderboard(){
        rankBoards.clear();

        Query rank = rootRef.child("user").orderByChild("score").limitToLast(10);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSnapshot score1 = snapshot.child("score");
                Object scoreValue = score1.getValue();
                if(scoreValue == null){
                    return;
                }
                rankBoards.add(new SavedData(snapshot.getKey(), ((Long)scoreValue).intValue()));

                while(rankBoards.size() > 10) {
                    rankBoards.remove(rankBoards.size() - 1);
                }
                int i = rankBoards.size()-1;
                leaderboardStringBuilder.setLength(0);
                for (SavedData data : rankBoards) {
                    int rankIndex = i;
                    leaderboardStringBuilder.append(data.score +" " +data.id +"\n");
                    --i;
                }
                Log.d(TAG, "onChildAdded: " + snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        rank.addChildEventListener(childEventListener);
    }

    class SavedData{
        public String id;
        public int score;

        public SavedData(String id, int score) {
            this.id = id;
            this.score = score;
        }

        public String toString(){
            return id+","+score;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        // Leaderboard
        canvas.drawText(leaderboardStringBuilder.toString(), pos.x, pos.y, paint);
    }
}
