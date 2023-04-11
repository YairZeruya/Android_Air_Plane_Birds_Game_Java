package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.example.hw1.Logic.GameManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final int OBSTACLE_COLUMNS = 3;
    public static final int OBSTACLE_ROWS = 6;
    private ExtendedFloatingActionButton main_left_button;
    private ExtendedFloatingActionButton main_right_button;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_obstacles;
    private ShapeableImageView[] main_IMG_Air_Planes;
    private GameManager gameManager;
    private final Timer createObstacleTimer = new Timer();
    private final Timer moveObstacleTimer = new Timer();
    private final int MOVE_FREQUENCY = 1000;
    private final int CREATE_FREQUENCY = 2000;
    private final int DELAY = 500;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length, main_IMG_Air_Planes, main_IMG_obstacles);
        refreshUI();
    }

    private void gameTimer() {
        createObstacleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> gameManager.createObstacle(main_IMG_obstacles));
            }
        }, 0, CREATE_FREQUENCY);


        moveObstacleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> moveObstacle());
            }
        }, DELAY, MOVE_FREQUENCY);

    }

    private void refreshUI() {
        gameTimer();
        main_left_button.setOnClickListener(v -> gameManager.moveAirPlaneLeft(main_IMG_Air_Planes));
        main_right_button.setOnClickListener(v -> gameManager.moveAirPlaneRight(main_IMG_Air_Planes));
    }


    private void findViews() {
        main_left_button = findViewById(R.id.left_FAB_button);
        main_right_button = findViewById(R.id.right_FAB_button);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
        main_IMG_obstacles = new ShapeableImageView[][]{
                {findViewById(R.id.main_matrix_00),
                        findViewById(R.id.main_matrix_01),
                        findViewById(R.id.main_matrix_02)},
                {findViewById(R.id.main_matrix_10),
                        findViewById(R.id.main_matrix_11),
                        findViewById(R.id.main_matrix_12)},
                {findViewById(R.id.main_matrix_20),
                        findViewById(R.id.main_matrix_21),
                        findViewById(R.id.main_matrix_22)},
                {findViewById(R.id.main_matrix_30),
                        findViewById(R.id.main_matrix_31),
                        findViewById(R.id.main_matrix_32)},
                {findViewById(R.id.main_matrix_40),
                        findViewById(R.id.main_matrix_41),
                        findViewById(R.id.main_matrix_42)},
                {findViewById(R.id.main_matrix_50),
                        findViewById(R.id.main_matrix_51),
                        findViewById(R.id.main_matrix_52)}};
        main_IMG_Air_Planes = new ShapeableImageView[]{
                findViewById(R.id.main_matrix_60),
                findViewById(R.id.main_matrix_61),
                findViewById(R.id.main_matrix_62)};
    }

    private void moveObstacle() {
        for (int i = 0; i < gameManager.getObstaclesInGame(); i++) {
            if (gameManager.getObstaclesIndexArray().get(i) < (OBSTACLE_ROWS - 1) * 10) {//[5][j] is max row of obstacle
                gameManager.updateObstaclePlace(i, main_IMG_obstacles);
            } else if (gameManager.getObstaclesIndexArray().get(i) >= (OBSTACLE_ROWS - 1) * 10) { // going to the last row
                gameManager.setObstacleVisibility
                        (gameManager.getObstaclesIndexArray().get(i) / 10
                                , gameManager.getObstaclesIndexArray().get(i) % 10
                                , false);
                moveObstacleToLastRow(i);
                gameManager.getObstaclesIndexArray().remove(i);
                gameManager.updateObstaclePlace(i, main_IMG_obstacles);
                gameManager.updateObstacleUI(main_IMG_obstacles);
                gameManager.setObstaclesInGame(gameManager.getObstaclesInGame() - 1);
                handler.postDelayed(() -> gameManager.updateAirPlaneUI(main_IMG_Air_Planes), DELAY);
            }
        }
    }

    private void moveObstacleToLastRow(int i) {
        ShapeableImageView sim = main_IMG_Air_Planes[gameManager.getObstaclesIndexArray().get(i) % 10];
        sim.setImageResource(R.drawable.bird_svgrepo_com);
        sim.setVisibility(View.VISIBLE);
        if (gameManager.checkAirPlaneFlag(gameManager.getObstaclesIndexArray().get(i) % 10)) {
            crash(sim);
        }
    }

    private void crash(ShapeableImageView sim) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        gameManager.loseLife(main_IMG_hearts);
        sim.setImageResource(R.drawable.collision_svgrepo_com);
        sim.setVisibility(View.VISIBLE);
        if (gameManager.isLose()) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
            gameManager.vibrate(v, 1500);
            onDestroy();
        } else {
            Toast.makeText(this, "crash!", Toast.LENGTH_SHORT).show();
            gameManager.vibrate(v, 500);
            gameManager.setAirPlaneVisibility(gameManager.getAirPlaneLocation(), true);
        }
    }

    private void stopGame() {
        createObstacleTimer.cancel();
        moveObstacleTimer.cancel();
        handler.removeCallbacks(() -> gameManager.updateAirPlaneUI(main_IMG_Air_Planes), DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopGame();
    }

}

