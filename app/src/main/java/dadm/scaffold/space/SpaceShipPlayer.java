package dadm.scaffold.space;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;

public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 200;
    private static final long TIME_BETWEEN_BULLETS = 300;
    private static final long TIME_BETWEEN_BULLETS_ALT = 50;
    private List<Bullet> bullets = new ArrayList<Bullet>();
    private long timeSinceLastFire;
    private long timeSinceLastAltFire = TIME_BETWEEN_BULLETS_ALT;

    private int maxX;
    private int maxY;
    private double speedFactor;
    int i = 0;

    //Drawable ids
    private int ship_type;
    private int dark_ship_type;

    public SpaceShipPlayer(GameEngine gameEngine, int ship_type, int dark_ship_type){
        super(gameEngine, ship_type);

        this.ship_type = ship_type;
        this.dark_ship_type = dark_ship_type;
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;
        characterType = 0;

        initBulletPool(gameEngine, characterType);
    }

    private synchronized void initBulletPool(GameEngine gameEngine, int type) {
        bullets.clear();
        if(type == 0) {
            for (int i = 0; i < INITIAL_BULLET_POOL_AMOUNT; i++) {
                bullets.add(new Bullet(gameEngine, R.drawable.proyectil_claro1, characterType));
            }
        }
        else{
            for (int i = 0; i < INITIAL_BULLET_POOL_AMOUNT; i++) {
                bullets.add(new Bullet(gameEngine, R.drawable.proyectil_oscuro1, characterType));
            }
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    @Override
    public void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }


    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
    }

    private void shootBullet(GameEngine gameEngine, int direction) {
        Bullet bullet = getBullet();
        if (bullet == null) {
            return;
        }
        bullet.init(this, positionX + width/2, positionY + height/2, direction);
        gameEngine.addGameObject(bullet);
        gameEngine.onGameEvent(GameEvent.LaserFired);
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            shootBullet(gameEngine, 0);
            timeSinceLastFire = 0;
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }

        if (gameEngine.theInputController.isFiringAlt && timeSinceLastAltFire > TIME_BETWEEN_BULLETS_ALT) {
            if(i < 8) {
                shootBullet(gameEngine, i);
                i++;
            } else {
                gameEngine.theInputController.isFiringAlt = false;
                i = 0;
            }
            timeSinceLastAltFire = 0;
        } else {
            timeSinceLastAltFire += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            Asteroid a = (Asteroid) otherObject;

            //Solo le afectan si son de distinto tipo
            if (a.characterType != characterType) {
                a.removeObject(gameEngine);
                gameEngine.onGameEvent(GameEvent.SpaceshipHit);
                gameEngine.setLivesToTake(1);
            }
        }
        else if ((otherObject instanceof Bullet && (((Bullet) otherObject)).parent.getClass().equals(Asteroid.class))) {
            Bullet b = (Bullet) otherObject;

            //Solo le afectan si son del distinto tipo
            if(b.characterType != characterType) {
                b.removeObject(gameEngine);
                gameEngine.onGameEvent(GameEvent.SpaceshipHit);
                gameEngine.setLivesToTake(1);
            }
        }
    }

    public void switchType(GameEngine gameEngine){
        if(characterType == 0){
            characterType = 1;
            this.bitmap = BitmapFactory.decodeResource(r, dark_ship_type);

        }
        else{
            characterType = 0;
            this.bitmap = BitmapFactory.decodeResource(r, ship_type);
        }
        initBulletPool(gameEngine, characterType);
    }
}
