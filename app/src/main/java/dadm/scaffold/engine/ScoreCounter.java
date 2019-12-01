package dadm.scaffold.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import dadm.scaffold.ScaffoldActivity;

public class ScoreCounter extends GameObject {

    private final float textWidth;
    private final float textHeight;

    private Paint paint;
    private int score;
    private int maxScore = 50;
    private int initLives = 3;
    private int lives;

    private String scoreText = "";
    private String livesText = "";

    public ScoreCounter(GameEngine gameEngine) {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.RIGHT);
        textHeight = (float) (50 * gameEngine.pixelFactor);
        textWidth = (float) (150 * gameEngine.pixelFactor);
        paint.setTextSize(textHeight / 2);
    }

    @Override
    public void startGame() {
        score = 0;
        lives = initLives;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        score += gameEngine.getScoreToAdd();
        lives -= gameEngine.getLivesToTake();
        gameEngine.setScoreToAdd(0);
        gameEngine.setLivesToTake(0);

        if(score >= maxScore){
            gameEngine.enableFinalizer(); //No funciona (Error: Only the original thread that created a view hierarchy can touch its views)
        }
        if(lives <= 0) {
            gameEngine.stopGame();
            ((ScaffoldActivity)gameEngine.mainActivity).gameOver();
        }

        scoreText = score + " / " + maxScore;
        livesText = "LIVES LEFT: " + lives + " / " + initLives;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawText(scoreText, (int) (canvas.getWidth() - 20), 400 + (textHeight / 2), paint);
        canvas.drawText(livesText, (int) (canvas.getWidth() - 20), 500 + (textHeight / 2), paint);
    }
}
