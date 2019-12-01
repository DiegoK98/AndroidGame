package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class Bullet extends Sprite {

    private double speedFactor;
    private int direction;
    private long lifeTime;
    private long maxLifeTime = 1500;

    private SpaceShipPlayer parent;

    public Bullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.proyectil_claro1);

        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        switch (direction) {
            case 0:
                positionY += speedFactor * elapsedMillis;
                break;
            case 1:
                positionY += speedFactor * elapsedMillis;
                positionX += speedFactor * elapsedMillis;
                break;
            case 2:
                positionX += speedFactor * elapsedMillis;
                break;
            case 3:
                positionX += speedFactor * elapsedMillis;
                positionY -= speedFactor * elapsedMillis;
                break;
            case 4:
                positionY -= speedFactor * elapsedMillis;
                break;
            case 5:
                positionY -= speedFactor * elapsedMillis;
                positionX -= speedFactor * elapsedMillis;
                break;
            case 6:
                positionX -= speedFactor * elapsedMillis;
                break;
            case 7:
                positionX -= speedFactor * elapsedMillis;
                positionY += speedFactor * elapsedMillis;
                break;
        }

        lifeTime += elapsedMillis;

        //if (positionY < -height) {
        if(lifeTime > maxLifeTime) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            parent.releaseBullet(this);
        }
    }


    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY, int dir) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
        direction = dir;
        lifeTime = 0;
    }

    private void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            gameEngine.setScoreToAdd(10);
        }
    }
}
