package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class Leaderboard implements GameObject {
    private static final String TAG = Leaderboard.class.getSimpleName();

    private StringBuilder leaderboardStringBuilder = new StringBuilder();
    private DatabaseReference rootRef;
    private ArrayList<SavedData> rankBoards = new ArrayList<>();
    private Typeface tf;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint textPaint = new TextPaint();
    private Vector2 pos = new Vector2();
    private StaticLayout staticLayout;

    public Leaderboard(float x, float y){
        pos.set(x,y);
        tf = Typeface.createFromAsset(GameView.self.getResources().getAssets(), "pfstardust.ttf");
        paint.setTextSize(GameView.MULTIPLIER * 10);
        paint.setTypeface(tf);
        paint.setColor(0xffffffff);
        paint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(GameView.MULTIPLIER * 8);
        textPaint.setTypeface(tf);
        textPaint.setColor(0xffffffff);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            staticLayout = StaticLayout.Builder
                    .obtain(leaderboardStringBuilder.toString(), 0, leaderboardStringBuilder.length(), textPaint, (int) (GameView.MULTIPLIER * 100)).build();
        }
        connectToServer();
    }

    // 연결 성공시 true 리턴
    boolean connectToServer(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://dodge-a8173-default-rtdb.asia-southeast1.firebasedatabase.app/");
        if(firebaseDatabase == null){
            Toast.makeText(MainActivity.self, MainActivity.self.getString(R.string.cant_connected_to_server),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        rootRef = firebaseDatabase.getReference();
        if(!isConnected()){
            Toast.makeText(MainActivity.self, MainActivity.self.getString(R.string.cant_connected_to_server),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isConnected(){
        return rootRef != null;
    }

    public void addToLeaderBoard(String name, int score){
        if(!isConnected()){
            if(!connectToServer()) {
                return;
            }
        }
        if(name.equals("")){
            name = "P"+new Random().nextInt(99);
        }
        SavedData savedData = new SavedData(name, score);
        String finalName = name;
        rootRef.child("user").child(name).child("score").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Object value = task.getResult().getValue();
                if(value != null){
                    savedData.score = Math.max(((Long)value).intValue(), savedData.score);
                }
                rootRef.child("user").child(finalName).setValue(savedData).addOnCompleteListener(task1 -> {
                    Toast.makeText(MainActivity.self,  savedData.score+" "+finalName,
                            Toast.LENGTH_SHORT).show();
                    addUpdateLeaderboardCallback();
                });
                Log.d(TAG, "add score to leaderboard: "+savedData);
            }
        });
    }

    public void addUpdateLeaderboardCallback(){
        if(!isConnected()){
            if(!connectToServer()) {
                return;
            }
            return;
        }
        rankBoards.clear();

        DatabaseReference user = rootRef.child("user");
        if(user == null) {
            Log.d(TAG, "addUpdateLeaderboardCallback: Can't find rootRef->User");
            return;
        }
        Query orderScoreQuery = user.orderByChild("score");
        if(orderScoreQuery == null){
            Log.d(TAG, "addUpdateLeaderboardCallback: Can't find rootRef->User->score");
            return;
        }
        Query rank = orderScoreQuery.limitToLast(10);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSnapshot score1 = snapshot.child("score");
                if(score1 == null){
                    return;
                }
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
                    leaderboardStringBuilder.insert(0, data.score +" " +data.id +"\n");
                    --i;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    staticLayout = StaticLayout.Builder
                            .obtain(leaderboardStringBuilder.toString(), 0, leaderboardStringBuilder.length(), textPaint, (int) (GameView.MULTIPLIER * 100)).build();
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
        if(!isConnected()){
            return;
        }
        // Leaderboard
        canvas.save();
        canvas.translate(pos.x, pos.y);
        staticLayout.draw(canvas);
        canvas.restore();
//        canvas.drawText(leaderboardStringBuilder.toString(), pos.x, pos.y, paint);
    }
}
