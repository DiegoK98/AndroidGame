package dadm.scaffold.engine;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class UpdateThread extends Thread {

    private final GameEngine theGameEngine;
    private boolean isGameRunning = true;
    private boolean isGamePaused = false;

    private Object synchroLock = new Object();

    //Parallax Parameters
    private ValueAnimator animator;
    private ImageView background1;
    private ImageView background2;

    public UpdateThread(GameEngine gameEngine) {
        theGameEngine = gameEngine;
    }

    @Override
    public void start() {
        isGameRunning = true;
        isGamePaused = false;
        super.start();
    }

    public void backgroundAnimation(final ImageView background1, final ImageView background2){
        //Store the views
        this.background1 = background1;
        this.background2 = background2;

        //Parallax
        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2500L);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float currentProgress = (float) valueAnimator.getAnimatedValue();
                final float height = background1.getHeight();
                final float translationY = height * currentProgress;
                background1.setTranslationY(translationY);
                background2.setTranslationY(translationY - height);
            }
        });

        animator.start();
    }

    public void stopGame() {
        isGameRunning = false;
        resumeGame();
    }

    @Override
    public void run() {
        long previousTimeMillis;
        long currentTimeMillis;
        long elapsedMillis;
        previousTimeMillis = System.currentTimeMillis();

        while (isGameRunning) {
            currentTimeMillis = System.currentTimeMillis();
            elapsedMillis = currentTimeMillis - previousTimeMillis;
            if (isGamePaused) {
                while (isGamePaused) {
                    try {
                        synchronized (synchroLock) {
                            synchroLock.wait();
                        }
                    } catch (InterruptedException e) {
                        // We stay on the loop
                    }
                }
                currentTimeMillis = System.currentTimeMillis();
            }
            theGameEngine.onUpdate(elapsedMillis);
            previousTimeMillis = currentTimeMillis;
        }
    }

    public void pauseGame() {
        isGamePaused = true;
        animator.end();
    }

    public void resumeGame() {
        if (isGamePaused == true) {
            isGamePaused = false;
            synchronized (synchroLock) {
                synchroLock.notify();
            }
            backgroundAnimation(background1, background2);
        }
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }
}
