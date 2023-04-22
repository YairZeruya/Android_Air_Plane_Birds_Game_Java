package com.example.hw1.Logic;

import static com.example.hw1.MainActivity.OBSTACLE_COLUMNS;
import static com.example.hw1.MainActivity.OBSTACLE_ROWS;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.example.hw1.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {

    //Hearts
    private int life;
    private int startLife;
    private boolean[] isHeartVisible;

    //Airplane
    private int airPlaneLocation;
    private boolean[] isAirPlaneVisible;

    //Obstacles
    private boolean[][] isObstacleVisible;
    private int obstaclesInGame;
    private Random obstacleStartPlace;
    private ArrayList<Integer> obstaclesIndexArray;//10 = [1][0] 22 = [2][2]
    

    public GameManager(int life, ShapeableImageView[] airPlaneUI, ShapeableImageView[][] obstaclesUi) {
        initHearts(life);
        initAirplane(airPlaneUI);
        initObstacles(obstaclesUi);
    }

    //*********************** Hearts Methods *************************

    public void initHearts(int life) {
        this.life = life;
        this.startLife = 3;
        isHeartVisible = new boolean[life];
        for (int i = 0; i < life; i++) {
            isHeartVisible[i] = true;
        }
    }

    public void updateHeartUI(ShapeableImageView[] heartsUI) {
        for (int i = 0; i < startLife; i++) {
            ShapeableImageView sim = heartsUI[i];
            if (!isHeartVisible[i])
                sim.setVisibility(View.INVISIBLE);
            else {
                sim.setVisibility(View.VISIBLE);
                sim.setImageResource(R.drawable.ic_icon_heart);
            }
        }
    }

    public boolean isLose() {
        return life == 0;
    }

    public void loseLife(ShapeableImageView[] heartsUI) {
        isHeartVisible[life - 1] = false;
        life--;
        updateHeartUI(heartsUI);
    }


    //*********************** Airplane Methods ************************


    public void initAirplane(ShapeableImageView[] AirPlaneUI) {
        airPlaneLocation = OBSTACLE_COLUMNS / 2;
        isAirPlaneVisible = new boolean[OBSTACLE_COLUMNS];
        isAirPlaneVisible[OBSTACLE_COLUMNS / 2] = true;
        updateAirplaneUI(AirPlaneUI);
    }

    public boolean moveAirplaneLeft(ShapeableImageView[] AirPlaneUI) {
        if (airPlaneLocation == 0) {
            return false;
        }
        isAirPlaneVisible[airPlaneLocation] = false;
        airPlaneLocation -= 1;
        isAirPlaneVisible[airPlaneLocation] = true;
        updateAirplaneUI(AirPlaneUI);
        return true;
    }

    public boolean moveAirplaneRight(ShapeableImageView[] AirPlaneUI) {
        if (airPlaneLocation == OBSTACLE_COLUMNS - 1) {
            return false;
        }
        isAirPlaneVisible[airPlaneLocation] = false;
        airPlaneLocation += 1;
        isAirPlaneVisible[airPlaneLocation] = true;
        updateAirplaneUI(AirPlaneUI);
        return true;
    }

    public void updateAirplaneUI(ShapeableImageView[] AirPlaneUI) {
        for (int i = 0; i < OBSTACLE_COLUMNS; i++) {
            ShapeableImageView sim = AirPlaneUI[i];
            if (!isAirPlaneVisible[i])
                sim.setVisibility(View.INVISIBLE);
            else {
                sim.setVisibility(View.VISIBLE);
                sim.setImageResource(R.drawable.baseline_local_airport_24);
            }
        }
    }

    public int getAirplaneLocation() {
        return airPlaneLocation;
    }

    public boolean checkAirplaneFlag(int i) {
        return isAirPlaneVisible[i];
    }

    public void setAirplaneVisibility(int i, boolean value) {
        this.isAirPlaneVisible[i] = value;
    }


    //********************* Obstacle Methods **********************************


    public void initObstacles(ShapeableImageView[][] obstaclesUI) {
        isObstacleVisible = new boolean[OBSTACLE_ROWS][OBSTACLE_COLUMNS];
        obstacleStartPlace = new Random();
        obstaclesIndexArray = new ArrayList<>();
        updateObstacleUI(obstaclesUI);
        obstaclesInGame = 0;
    }

    public int getObstaclesInGame() {
        return obstaclesInGame;
    }

    public void setObstaclesInGame(int obstaclesInGame) {
        this.obstaclesInGame = obstaclesInGame;
    }

    public void updateObstacleUI(ShapeableImageView[][] obstaclesUI) {
        for (int i = 0; i < OBSTACLE_ROWS; i++) {
            for (int j = 0; j < OBSTACLE_COLUMNS; j++) {
                ShapeableImageView sim = obstaclesUI[i][j];
                if (!isObstacleVisible[i][j]) {
                    sim.setVisibility(View.INVISIBLE);
                } else {
                    sim.setVisibility(View.VISIBLE);
                    sim.setImageResource(R.drawable.bird_svgrepo_com);
                }
            }
        }
    }

    public void updateObstaclePlace(int i, ShapeableImageView[][] obstaclesUI) {

        int rowIndex = obstaclesIndexArray.get(i) / 10;
        int columnIndex = obstaclesIndexArray.get(i) % 10;
        if (isObstacleVisible[rowIndex][columnIndex]) {
            this.isObstacleVisible[rowIndex][columnIndex] = false;
            this.isObstacleVisible[rowIndex + 1][columnIndex] = true;
            obstaclesIndexArray.set(i, obstaclesIndexArray.get(i) + 10);//next row
            updateObstacleUI(obstaclesUI);
        }
    }

    public ArrayList<Integer> getObstaclesIndexArray() {
        return obstaclesIndexArray;
    }

    public void setObstacleVisibility(int i, int j, boolean value) {
        this.isObstacleVisible[i][j] = value;
    }

    public void createObstacle(ShapeableImageView[][] obstaclesUI) {
        int randColumn = obstacleStartPlace.nextInt(OBSTACLE_COLUMNS);
        if (!isObstacleVisible[0][randColumn]) {
            isObstacleVisible[0][randColumn] = true;
            obstaclesIndexArray.add(randColumn);
        }
        obstaclesInGame++;
        updateObstacleUI(obstaclesUI);
    }
}




