package com.example.tfg_nd;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final int UPDATE_MILLIS = 30;
    Bitmap background;
    Bitmap toptube, bottomtube;
    private Paint scorePaint = new Paint();
    Display display;
    Point point;
    int dWidth, dHeight;
    Rect rect;
    Bitmap[] birds;
    int birdFrame = 0;
    int velocity = 0, gravity = 3;
    int birdX, birdY;
    boolean gameState = false;
    boolean gameOver = false;
    int gap = 400;
    int minTubeOffset, maxTubeOffset;
    int numberOfTubes = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTubes];
    int[] topTubeY = new int[numberOfTubes];
    Random random;
    int tubeVelocity = 10;
    int colliderVerticalOffset = 100;
    int colliderHorizontalOffset = 70;

    int compareScore;
    public int score = 0;
    StartGame startGame;
    Boolean added = false;
    Boolean retrieved = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://nopou-7c660-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference highScore = database.getReference("HighScore");
    DatabaseReference latestScore = database.getReference("LatestScore");


    public GameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        scorePaint.setColor(Color.rgb(195, 40, 0));
        scorePaint.setTextSize(100);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        toptube = BitmapFactory.decodeResource(getResources(), R.drawable.pipetopresized);
        bottomtube = BitmapFactory.decodeResource(getResources(), R.drawable.piperesized);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0, 0, dWidth, dHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.flying_pouresized);
        birds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.flying_pou_2resized);
        birdX = dWidth / 2 - birds[0].getWidth() / 2;
        birdY = dHeight / 2 - birds[0].getHeight() / 2;
        distanceBetweenTubes = dWidth * 5 / 4;
        minTubeOffset = gap / 2;
        maxTubeOffset = dHeight - minTubeOffset - gap;
        random = new Random();
        for (int i = 0; i < numberOfTubes; i++) {
            tubeX[i] = dWidth + i * distanceBetweenTubes;
            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //crear la "animacion"
        canvas.drawBitmap(background, null, rect, null);
        if (gameState) {
            if (birdFrame == 0) {
                birdFrame = 1;
            } else {
                birdFrame = 0;
            }

            //impedir que el bicho se salga de la pantalla y moverlo abajo
            if (birdY < dHeight - birds[0].getHeight() || velocity < 0) {
                velocity += gravity;
                birdY += velocity;
            }
        }
        if (birdY <= 0) {
            birdY = 0;
        }

        //dibujar y desplazar tubos
        for (int i = 0; i < numberOfTubes; i++) {
            if (gameState) {
                tubeX[i] -= tubeVelocity;
                if (tubeX[i] < -toptube.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
                    score++;
                }
            }
            canvas.drawBitmap(toptube, tubeX[i], topTubeY[i] - toptube.getHeight(), null);
            canvas.drawBitmap(bottomtube, tubeX[i], topTubeY[i] + gap, null);

            //calcular colisiones
            if (((birdY + birds[0].getHeight() - colliderVerticalOffset <= topTubeY[i]) ||
                    (birdY + birds[0].getHeight() - colliderVerticalOffset >= topTubeY[i] + gap)) &&
                    (birdX >= tubeX[i] - toptube.getWidth() / 2 && birdX <= tubeX[i] + toptube.getWidth() / 2 + colliderHorizontalOffset)) {
                gameState = false;
                gameOver = true;
            }
            if (gameState) {
                //puntos
                canvas.drawText("Score :" + score, canvas.getWidth() / 3, 250, scorePaint);
            }
        }


        canvas.drawBitmap(birds[birdFrame], birdX, birdY, null);
        handler.postDelayed(runnable, UPDATE_MILLIS);

        if (gameOver) {

            if (!added) {
                highScore.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        compareScore = dataSnapshot.getValue(Integer.class);
                        if(score> compareScore){
                            highScore.setValue(score);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                        compareScore=100000000;
                    }
                });


                latestScore.setValue(score);
                added = true;
            }


            canvas.drawText("Current Score: " + score, canvas.getWidth() / 6, canvas.getHeight() / 2, scorePaint);
            canvas.drawText("Best Score: " + compareScore, canvas.getWidth() / 5, canvas.getHeight() / 3, scorePaint);


        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN && !gameOver) {
            velocity = -30;
            gameState = true;
        }
        if (gameOver) {
            score = 0;
            startGame = new StartGame();
            startGame.exitGame();
        }
        return true;
    }



}
