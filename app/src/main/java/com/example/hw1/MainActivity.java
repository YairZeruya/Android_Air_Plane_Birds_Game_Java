package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.hw1.Activities.RecordsActivity;
import com.example.hw1.Logic.GameManager;
import com.example.hw1.Utilities.SensorMode;
import com.example.hw1.Utilities.SignalGenerator;
import com.example.hw1.Interfaces.TiltCallBack;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final int OBSTACLE_COLUMNS = 5;
    public static final int OBSTACLE_ROWS = 9;
    public static final int DEFAULT_VALUE = 0;
    public static final int SCORE_BY_DISTANCE = 10;
    public static final int SCORE_BY_COIN = 100;
    public static final int GAME_OVER_VIBRATE_LENGTH = 1500;
    public static final int CRASH_VIBRATE_LENGTH = 500;
    public static final String KEY_MOVE_FREQUENCY = "KEY_MOVE_FREQUENCY";
    public static final String KEY_CREATE_FREQUENCY = "KEY_CREATE_FREQUENCY";
    public static final String KEY_DELAY = "KEY_DELAY";
    public static final String KEY_BUTTON_VISIBILITY = "KEY_BUTTON_VISIBILITY";
    private ExtendedFloatingActionButton main_left_button;
    private ExtendedFloatingActionButton main_right_button;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_obstacles;
    private ShapeableImageView[] main_IMG_Air_Planes;
    private MaterialTextView main_LBL_score;
    private GameManager gameManager;
    private final Timer createObstacleTimer = new Timer();
    private final Timer moveObstacleTimer = new Timer();
    private final Timer pointsByDistanceTimer = new Timer();
    private Handler handler = new Handler();
    private int createFrequency;
    private int moveFrequency;
    private int delay;
    private boolean isSensorMode;
    private SensorMode sensorMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length, main_IMG_Air_Planes, main_IMG_obstacles);
        Intent previousIntent = getIntent();
        createFrequency = previousIntent.getIntExtra(KEY_CREATE_FREQUENCY, DEFAULT_VALUE);
        moveFrequency = previousIntent.getIntExtra(KEY_MOVE_FREQUENCY, DEFAULT_VALUE);
        delay = previousIntent.getIntExtra(KEY_DELAY, DEFAULT_VALUE);
        isSensorMode = previousIntent.getBooleanExtra(KEY_BUTTON_VISIBILITY, false);
        refreshUI();
    }

    private void initSensorMode() {
        sensorMode = new SensorMode(this, new TiltCallBack() {
            @Override
            public void TiltLeft() {
                gameManager.moveAirplaneLeft(main_IMG_Air_Planes);
            }

            @Override
            public void TiltRight() {
                gameManager.moveAirplaneRight(main_IMG_Air_Planes);
            }

        });
    }

    private void gameTimer() {
        createObstacleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> gameManager.createObject(main_IMG_obstacles));
            }
        }, DEFAULT_VALUE, createFrequency);

        moveObstacleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> moveObstacle());
            }
        }, delay, moveFrequency);

        pointsByDistanceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> pointByDistance());
            }
        }, DEFAULT_VALUE, moveFrequency);
    }

    private void pointByDistance() {
        gameManager.updateScore(main_LBL_score, SCORE_BY_DISTANCE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorMode)
            sensorMode.start();
        SignalGenerator.getInstance().playBackgroundSound(R.raw.birds_background);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorMode)
            sensorMode.stop();
        SignalGenerator.getInstance().pauseBackgroundSound(R.raw.birds_background);
    }

    private void refreshUI() {
        gameTimer();
        if (!isSensorMode) {
            main_left_button.setOnClickListener(v -> gameManager.moveAirplaneLeft(main_IMG_Air_Planes));
            main_right_button.setOnClickListener(v -> gameManager.moveAirplaneRight(main_IMG_Air_Planes));
        } else {
            initSensorMode();
            main_right_button.setVisibility(View.INVISIBLE);
            main_left_button.setVisibility(View.INVISIBLE);
        }
    }


    private void findViews() {
        main_left_button = findViewById(R.id.left_FAB_button);
        main_right_button = findViewById(R.id.right_FAB_button);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
        main_IMG_obstacles = new ShapeableImageView[][]{
                {findViewById(R.id.main_obstacle_00),
                        findViewById(R.id.main_obstacle_01),
                        findViewById(R.id.main_obstacle_02),
                        findViewById(R.id.main_obstacle_03),
                        findViewById(R.id.main_obstacle_04)},
                {findViewById(R.id.main_obstacle_10),
                        findViewById(R.id.main_obstacle_11),
                        findViewById(R.id.main_obstacle_12),
                        findViewById(R.id.main_obstacle_13),
                        findViewById(R.id.main_obstacle_14)},
                {findViewById(R.id.main_obstacle_20),
                        findViewById(R.id.main_obstacle_21),
                        findViewById(R.id.main_obstacle_22),
                        findViewById(R.id.main_obstacle_23),
                        findViewById(R.id.main_obstacle_24)},
                {findViewById(R.id.main_obstacle_30),
                        findViewById(R.id.main_obstacle_31),
                        findViewById(R.id.main_obstacle_32),
                        findViewById(R.id.main_obstacle_33),
                        findViewById(R.id.main_obstacle_34)},
                {findViewById(R.id.main_obstacle_40),
                        findViewById(R.id.main_obstacle_41),
                        findViewById(R.id.main_obstacle_42),
                        findViewById(R.id.main_obstacle_43),
                        findViewById(R.id.main_obstacle_44)},
                {findViewById(R.id.main_obstacle_50),
                        findViewById(R.id.main_obstacle_51),
                        findViewById(R.id.main_obstacle_52),
                        findViewById(R.id.main_obstacle_53),
                        findViewById(R.id.main_obstacle_54)},
                {findViewById(R.id.main_obstacle_60),
                        findViewById(R.id.main_obstacle_61),
                        findViewById(R.id.main_obstacle_62),
                        findViewById(R.id.main_obstacle_63),
                        findViewById(R.id.main_obstacle_64)},
                {findViewById(R.id.main_obstacle_70),
                        findViewById(R.id.main_obstacle_71),
                        findViewById(R.id.main_obstacle_72),
                        findViewById(R.id.main_obstacle_73),
                        findViewById(R.id.main_obstacle_74)},
                {findViewById(R.id.main_obstacle_80),
                        findViewById(R.id.main_obstacle_81),
                        findViewById(R.id.main_obstacle_82),
                        findViewById(R.id.main_obstacle_83),
                        findViewById(R.id.main_obstacle_84)}};

        main_IMG_Air_Planes = new ShapeableImageView[]{
                findViewById(R.id.main_airplane_90),
                findViewById(R.id.main_airplane_91),
                findViewById(R.id.main_airplane_92),
                findViewById(R.id.main_airplane_93),
                findViewById(R.id.main_airplane_94)};

        main_LBL_score = findViewById(R.id.main_LBL_score);
    }

    private void moveObstacle() {
        for (int i = 0; i < gameManager.getObstaclesInGame(); i++) {
            boolean isCoin = gameManager.isCoin(i);
            if (gameManager.getObstaclesIndexArray().get(i) < (OBSTACLE_ROWS - 1) * 10) {//[8][j] is max row of obstacle
                if (isCoin) {
                    gameManager.updateCoinsPlace(i, main_IMG_obstacles);
                } else {
                    gameManager.updateObstaclePlace(i, main_IMG_obstacles);
                }
            } else if (gameManager.getObstaclesIndexArray().get(i) >= (OBSTACLE_ROWS - 1) * 10) { // going to the last row
                gameManager.setObstacleVisibility
                        (gameManager.getObstaclesIndexArray().get(i) / 10
                                , gameManager.getObstaclesIndexArray().get(i) % 10
                                , GameManager.EMPTY);
                boolean isAfterCoin = gameManager.isCoin(i + 1);
                if (isCoin) {
                    moveCoinToLastRow(i);
                } else {
                    moveObstacleToLastRow(i);
                }
                gameManager.getObstaclesIndexArray().remove(i);

                if (isAfterCoin) {
                    gameManager.updateCoinsPlace(i, main_IMG_obstacles);
                } else {
                    gameManager.updateObstaclePlace(i, main_IMG_obstacles);
                }
                gameManager.setObstaclesInGame(gameManager.getObstaclesInGame() - 1);
                handler.postDelayed(() -> gameManager.updateAirplaneUI(main_IMG_Air_Planes), delay);
            }
        }
    }

    private void moveCoinToLastRow(int i) {
        ShapeableImageView sim = main_IMG_Air_Planes[gameManager.getObstaclesIndexArray().get(i) % 10];
        sim.setImageResource(R.drawable.coin_svgrepo_com);
        sim.setVisibility(View.VISIBLE);
        if (gameManager.checkAirplaneFlag(gameManager.getObstaclesIndexArray().get(i) % 10)) {
            addScore();
        }
    }

    private void addScore() {
        gameManager.updateScore(main_LBL_score, SCORE_BY_COIN);
        SignalGenerator.getInstance().toast("+100", Toast.LENGTH_SHORT);
        gameManager.updateObstacleUI(main_IMG_obstacles);
        SignalGenerator.getInstance().playSound(R.raw.collect_coin);
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
            SignalGenerator.getInstance().playSound(R.raw.carsh_sound2);
            SignalGenerator.getInstance().toast("Game Over! Your Score: " + gameManager.getScore(), Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate(GAME_OVER_VIBRATE_LENGTH);
            gameManager.updateRecordsTable(this);
            stopGame();
            openRecordsActivity();
            finish();
        } else {
            SignalGenerator.getInstance().playSound(R.raw.crash);
            SignalGenerator.getInstance().toast("crash!", Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate(CRASH_VIBRATE_LENGTH);
            gameManager.setAirplaneVisibility(gameManager.getAirplaneLocation(), true);
        }
    }

    private void openRecordsActivity() {
        Intent intent = new Intent(this, RecordsActivity.class);
        startActivity(intent);
    }

    private void stopGame() {
        createObstacleTimer.cancel();
        moveObstacleTimer.cancel();
        pointsByDistanceTimer.cancel();
        handler.removeCallbacks(() -> gameManager.updateAirplaneUI(main_IMG_Air_Planes), delay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopGame();
    }

}

