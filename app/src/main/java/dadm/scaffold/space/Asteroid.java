package dadm.scaffold.space;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class Asteroid extends Sprite {

    private final GameController gameController;

    private double speed;
    private double speedX;
    private double speedY;
    private long shootTime;
    //private double rotationSpeed;

    private static final int INITIAL_BULLET_POOL_AMOUNT = 20;
    List<Bullet> bullets = new ArrayList<Bullet>();

    public Asteroid(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, 0);
        this.speed = 200d * pixelFactor/1000d;
        this.gameController = gameController;
    }

    public void init(GameEngine gameEngine) {
        // They initialize in a [-30, 30] degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/3d-Math.PI/6d;
        speedX = speed * Math.sin(angle);
        speedY = speed * Math.cos(angle);
        // Asteroids initialize in the central 50% of the screen horizontally
        positionX = gameEngine.random.nextInt(gameEngine.width/2)+gameEngine.width/4;
        // They initialize outside of the screen vertically
        positionY = -height;
        /*rotationSpeed = angle*(180d / Math.PI)/250d; // They rotate 4 times their ange in a second.
        rotation = gameEngine.random.nextInt(360);*/
        initBulletPool(gameEngine);
        shootTime = 1000;
    }


    private void initBulletPool(GameEngine gameEngine) {
        int id;
        switch (enemyType){
            case 1:
                id = R.drawable.proyectil_claro1;
                break;

            default:
                id = R.drawable.proyectil_oscuro1;
                break;
        }
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine, id));
        }
    }

    @Override
    public void startGame() {
    }

    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
        gameController.returnToPool(this);
    }

    @Override
    public void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
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

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;
        //rotation += rotationSpeed * elapsedMillis;
        /*if (rotation > 360) {
            rotation = 0;
        }
        else if (rotation < 0) {
            rotation = 360;
        }*/
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }
        shootTime -= elapsedMillis;

        if(shootTime <= 0 && (enemyType == 1 || enemyType == 3)){
            shootBullet(gameEngine, 4);
            shootTime = 1000;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }
}
