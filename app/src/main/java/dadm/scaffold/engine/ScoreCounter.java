package dadm.scaffold.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import dadm.scaffold.ScaffoldActivity;

public class ScoreCounter extends GameObject {

    private final float textWidth;
    private final float textHeight;

    private Paint paint;
    //private int scoreBarHeight = 0;
    public int score;
    //private int maxScore = 100;
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
        paint.setTextSize(textHeight / 3);
        Typeface font = Typeface.createFromAsset(gameEngine.getContext().getAssets(), "fonts/press_start_2p.ttf");
        paint.setTypeface(font);
        paint.setColor(Color.WHITE);
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

        if(lives <= 0) {
            gameEngine.stopGame();
            ((ScaffoldActivity)gameEngine.mainActivity).gameOver();
        }
        scoreText = "SCORE: " + score;
        livesText = "LIVES: " + lives + "/" + initLives;
        gameEngine.finalScore(score);
    }

    @Override
    public void onDraw(Canvas canvas) {


        canvas.drawText(livesText, canvas.getWidth()/2 - textWidth,  (textHeight / 2), paint);
        canvas.drawText(scoreText, canvas.getWidth()/2 + textWidth,  (textHeight / 2), paint);
    }
}
