package com.example.hw1.Logic;

import static com.example.hw1.MainActivity.DEFAULT_VALUE;
import static com.example.hw1.MainActivity.OBSTACLE_COLUMNS;
import static com.example.hw1.MainActivity.OBSTACLE_ROWS;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.example.hw1.Objects.Record;
import com.example.hw1.R;
import com.example.hw1.Utilities.MySPv;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameManager {

    // Hearts
    private int life;
    private int startLife;
    private boolean[] isHeartVisible;

    // Airplane
    private int airPlaneLocation;
    private boolean[] isAirPlaneVisible;

    // Obstacles
    private int[][] isObstacleVisible;
    private int obstaclesInGame;
    private Random objectStartPlace;
    private ArrayList<Integer> obstaclesIndexArray;//10 = [1][0] 22 = [2][2]

    // coins
    private int score;
    private int countObstacles;
    public static final int EMPTY = 0;
    public static final int BIRD = 1;
    public static final int COIN = 2;


    // Records
    private ArrayList<Record> recordArrayList = new ArrayList<>();
    private ArrayList<Integer> scoresArrayList = new ArrayList<>();


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

    public boolean isCoin(int i) {
        int rowIndex = obstaclesIndexArray.get(i) / 10;
        int columnIndex = obstaclesIndexArray.get(i) % 10;
        return isObstacleVisible[rowIndex][columnIndex] == COIN;
    }

    public void initObstacles(ShapeableImageView[][] obstaclesUI) {
        isObstacleVisible = new int[OBSTACLE_ROWS][OBSTACLE_COLUMNS];
        objectStartPlace = new Random();
        obstaclesIndexArray = new ArrayList<>();
        updateObstacleUI(obstaclesUI);
        obstaclesInGame = 0;
        score = 100;
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
                if (isObstacleVisible[i][j] == EMPTY) {
                    sim.setVisibility(View.INVISIBLE);
                } else if (isObstacleVisible[i][j] == BIRD) {
                    sim.setVisibility(View.VISIBLE);
                    sim.setImageResource(R.drawable.bird_svgrepo_com);
                } else if (isObstacleVisible[i][j] == COIN) {
                    sim.setVisibility(View.VISIBLE);
                    sim.setImageResource(R.drawable.coin_svgrepo_com);
                }
            }
        }
    }

    public void updateObstaclePlace(int i, ShapeableImageView[][] obstaclesUI) {
        int rowIndex = obstaclesIndexArray.get(i) / 10;
        int columnIndex = obstaclesIndexArray.get(i) % 10;
        if (isObstacleVisible[rowIndex][columnIndex] == BIRD) {
            this.isObstacleVisible[rowIndex][columnIndex] = EMPTY;
            this.isObstacleVisible[rowIndex + 1][columnIndex] = BIRD;
            obstaclesIndexArray.set(i, obstaclesIndexArray.get(i) + 10);//next row
            updateObstacleUI(obstaclesUI);
        }
    }

    public ArrayList<Integer> getObstaclesIndexArray() {
        return obstaclesIndexArray;
    }

    public void setObstacleVisibility(int i, int j, int value) {
        this.isObstacleVisible[i][j] = value;
    }

    public void createObstacle(ShapeableImageView[][] obstaclesUI) {
        int randColumn = objectStartPlace.nextInt(OBSTACLE_COLUMNS);
        if (isObstacleVisible[0][randColumn] == EMPTY) {
            isObstacleVisible[0][randColumn] = BIRD;
            obstaclesIndexArray.add(randColumn);
        }
        obstaclesInGame++;
        updateObstacleUI(obstaclesUI);
    }

    //******************************* Coins Methods ****************************


    public void updateCoinsPlace(int i, ShapeableImageView[][] obstacleUI) {
        int rowIndex = obstaclesIndexArray.get(i) / 10;
        int columnIndex = obstaclesIndexArray.get(i) % 10;
        if (isObstacleVisible[rowIndex][columnIndex] == COIN) {
            this.isObstacleVisible[rowIndex][columnIndex] = EMPTY;
            this.isObstacleVisible[rowIndex + 1][columnIndex] = COIN;
            obstaclesIndexArray.set(i, obstaclesIndexArray.get(i) + 10);//next row
            updateObstacleUI(obstacleUI);
        }
    }


    public void createCoins(ShapeableImageView[][] obstaclesUI) {
        int randColumn = objectStartPlace.nextInt(OBSTACLE_COLUMNS);
        if (isObstacleVisible[0][randColumn] == EMPTY) {
            isObstacleVisible[0][randColumn] = COIN;
            obstaclesIndexArray.add(randColumn);
        }
        obstaclesInGame++;
        updateObstacleUI(obstaclesUI);
    }

    public void createObject(ShapeableImageView[][] obstaclesUI) {
        if (countObstacles == 5) {
            createCoins(obstaclesUI);
            countObstacles = 0;
        } else {
            createObstacle(obstaclesUI);
            countObstacles++;
        }
    }

    // ******************************* Record Methods ****************************
    public void updateScore(MaterialTextView scoreUI, int scoreToUpdate) {
        scoreUI.setText("" + score);
        score += scoreToUpdate;
    }


    public void updateRecordsTable(Context context) {
        Gson gson = new Gson();
        int numOfRecords = MySPv.getInstance().getInt("Num Of Records", DEFAULT_VALUE);
        numOfRecords++;
        // put the number of records and current score in MySPv
        MySPv.getInstance().putInt("Num Of Records", numOfRecords);
        MySPv.getInstance().putInt("Score: " + numOfRecords, score);
        double[] latLong = getRecordLocation(context);
        // Add the current score to the scores list and sort it
        for (int i = 0; i < numOfRecords; i++) {
            int currScore = MySPv.getInstance().getInt("Score: " + (i + 1), DEFAULT_VALUE);
            scoresArrayList.add(currScore);
        }
        Collections.sort(scoresArrayList, Collections.reverseOrder());
        // Create a list of the top 10 records
        int numTopScores = Math.min(10, numOfRecords);
        for (int i = 0; i < numTopScores; i++) {
            int currScore = scoresArrayList.get(i);
            Record record = new Record("" + (i + 1), "" + (currScore), latLong[0], latLong[1]);
            recordArrayList.add(record);
        }
        // Save the top 10 records to MySPv
        for (int i = 0; i < numTopScores; i++) {
            String json = gson.toJson(recordArrayList.get(i));
            MySPv.getInstance().putString("Rank: " + (i + 1), json);
        }
    }

    private double[] getRecordLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        double[] latlong = new double[2];
        latlong[0] = -1;
        latlong[1] = -1;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return latlong;
        }

        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location == null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location == null && locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        if (location != null) {
            latlong[0] = location.getLatitude();
            latlong[1] = location.getLongitude();
        } else {
            latlong[0] = 0.0;
            latlong[1] = 0.0;
        }
        return latlong;
    }

    public String getScore() {
        return score + "";
    }
}









