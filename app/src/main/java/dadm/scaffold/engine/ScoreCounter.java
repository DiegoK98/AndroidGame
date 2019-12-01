package dadm.scaffold.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import dadm.scaffold.ScaffoldActivity;

public class ScoreCounter extends GameObject {

    private final float textWidth;
    private final float textHeight;

    private Paint paint;
    private int scoreBarHeight = 0;
    private int score;
    private int maxScore = 100;
    private int initLives = 3;
    private int lives;

    private String scoreText = "";
    private String livesText = "";

    private boolean overMaxScore = false;

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

        if(!overMaxScore) {
            if (score >= maxScore) {
                gameEngine.enableFinalizer();
                scoreBarHeight = 290;
                overMaxScore = true;
            } else {
                scoreBarHeight = 290 * score / maxScore;
            }
        }

        if(lives <= 0) {
            gameEngine.stopGame();
            ((ScaffoldActivity)gameEngine.mainActivity).gameOver();
        }

        livesText = "LIVES LEFT: " + lives + " / " + initLives;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(score >= maxScore) {
            RectF rect = new RectF(canvas.getWidth() - 230, 260, canvas.getWidth() - 35, 590);
            paint.setColor(Color.YELLOW);
            canvas.drawRoundRect(rect, 15,15,paint);
        }
        paint.setColor(Color.BLACK);
        canvas.drawText(livesText, (int) (canvas.getWidth() - 20), 600 + (textHeight / 2), paint);
        paint.setColor(Color.BLUE);
        paint.setAlpha(100);
        canvas.drawRect(canvas.getWidth() - 220, 270, canvas.getWidth() - 45, 580, paint);
        paint.setAlpha(255);
        canvas.drawRect(canvas.getWidth() - 210, 570 - scoreBarHeight, canvas.getWidth() - 55, 570, paint);
    }
}
