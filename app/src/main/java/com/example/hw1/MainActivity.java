package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hw1.Logic.GameManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final int OBSTACLE_COLUMNS = 5;
    public static final int OBSTACLE_ROWS = 9;
    public static final String KEY_MOVE_FREQUENCY = "MOVE_FREQUENCY";
    public static final String KEY_CREATE_FREQUENCY = "CREATE_FREQUENCY";
    public static final int DELAY = 500;
    private ExtendedFloatingActionButton main_left_button;
    private ExtendedFloatingActionButton main_right_button;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_obstacles;
    private ShapeableImageView[] main_IMG_Air_Planes;
    private GameManager gameManager;
    private final Timer createObstacleTimer = new Timer();
    private final Timer moveObstacleTimer = new Timer();
    private Handler handler = new Handler();
    private int createFrequency;
    private int moveFrequency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length, main_IMG_Air_Planes, main_IMG_obstacles);
        Intent previousIntent = getIntent();
        createFrequency = previousIntent.getIntExtra(KEY_CREATE_FREQUENCY, 0);
        moveFrequency = previousIntent.getIntExtra(KEY_MOVE_FREQUENCY, 0);
        refreshUI();
    }

    private void gameTimer() {
        createObstacleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> gameManager.createObstacle(main_IMG_obstacles));
            }
        }, DELAY, createFrequency);


        moveObstacleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> moveObstacle());
            }
        }, DELAY, moveFrequency);

    }

    private void refreshUI() {
        gameTimer();
        main_left_button.setOnClickListener(v -> gameManager.moveAirplaneLeft(main_IMG_Air_Planes));
        main_right_button.setOnClickListener(v -> gameManager.moveAirplaneRight(main_IMG_Air_Planes));
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
                        findViewById(R.id.main_matrix_02),
                        findViewById(R.id.main_matrix_03),
                        findViewById(R.id.main_matrix_04)},
                {findViewById(R.id.main_matrix_10),
                        findViewById(R.id.main_matrix_11),
                        findViewById(R.id.main_matrix_12),
                        findViewById(R.id.main_matrix_13),
                        findViewById(R.id.main_matrix_14)},
                {findViewById(R.id.main_matrix_20),
                        findViewById(R.id.main_matrix_21),
                        findViewById(R.id.main_matrix_22),
                        findViewById(R.id.main_matrix_23),
                        findViewById(R.id.main_matrix_24)},
                {findViewById(R.id.main_matrix_30),
                        findViewById(R.id.main_matrix_31),
                        findViewById(R.id.main_matrix_32),
                        findViewById(R.id.main_matrix_33),
                        findViewById(R.id.main_matrix_34)},
                {findViewById(R.id.main_matrix_40),
                        findViewById(R.id.main_matrix_41),
                        findViewById(R.id.main_matrix_42),
                        findViewById(R.id.main_matrix_43),
                        findViewById(R.id.main_matrix_44)},
                {findViewById(R.id.main_matrix_50),
                        findViewById(R.id.main_matrix_51),
                        findViewById(R.id.main_matrix_52),
                        findViewById(R.id.main_matrix_53),
                        findViewById(R.id.main_matrix_54)},
                {findViewById(R.id.main_matrix_60),
                        findViewById(R.id.main_matrix_61),
                        findViewById(R.id.main_matrix_62),
                        findViewById(R.id.main_matrix_63),
                        findViewById(R.id.main_matrix_64)},
                {findViewById(R.id.main_matrix_70),
                        findViewById(R.id.main_matrix_71),
                        findViewById(R.id.main_matrix_72),
                        findViewById(R.id.main_matrix_73),
                        findViewById(R.id.main_matrix_74)},
                {findViewById(R.id.main_matrix_80),
                        findViewById(R.id.main_matrix_81),
                        findViewById(R.id.main_matrix_82),
                        findViewById(R.id.main_matrix_83),
                        findViewById(R.id.main_matrix_84)}};
        main_IMG_Air_Planes = new ShapeableImageView[]{
                findViewById(R.id.main_matrix_90),
                findViewById(R.id.main_matrix_91),
                findViewById(R.id.main_matrix_92),
                findViewById(R.id.main_matrix_93),
                findViewById(R.id.main_matrix_94)};
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
                handler.postDelayed(() -> gameManager.updateAirplaneUI(main_IMG_Air_Planes), DELAY);
            }
        }
    }

    private void moveObstacleToLastRow(int i) {
        ShapeableImageView sim = main_IMG_Air_Planes[gameManager.getObstaclesIndexArray().get(i) % 10];
        sim.setImageResource(R.drawable.bird_svgrepo_com);
        sim.setVisibility(View.VISIBLE);
        if (gameManager.checkAirplaneFlag(gameManager.getObstaclesIndexArray().get(i) % 10)) {
            crash(sim);
        }
    }

    private void crash(ShapeableImageView sim) {
        gameManager.loseLife(main_IMG_hearts);
        sim.setImageResource(R.drawable.collision_svgrepo_com);
        sim.setVisibility(View.VISIBLE);
        if (gameManager.isLose()) {
            SignalGenerator.getInstance().playSound();
            SignalGenerator.getInstance().toast("Game Over!", Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate(1500);
            stopGame();
        } else {
            SignalGenerator.getInstance().playSound();
            SignalGenerator.getInstance().toast("crash!", Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate(500);
            gameManager.setAirplaneVisibility(gameManager.getAirplaneLocation(), true);
        }
    }

    private void stopGame() {
        createObstacleTimer.cancel();
        moveObstacleTimer.cancel();
        handler.removeCallbacks(() -> gameManager.updateAirplaneUI(main_IMG_Air_Planes), DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopGame();
    }

}

