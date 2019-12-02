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

    public Sprite parent;

    public Bullet(GameEngine gameEngine, int id, int bulletType){
        super(gameEngine, id);

        this.characterType = bulletType;
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
        if(lifeTime > 1000) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            parent.releaseBullet(this);
        }
    }


    public void init(Sprite parentPlayer, double initPositionX, double initPositionY, int dir) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
        direction = dir;
        lifeTime = 0;
    }

    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid && parent.getClass().equals(SpaceShipPlayer.class)) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Asteroid a = (Asteroid) otherObject;

            if(a.characterType != characterType) {
                a.removeObject(gameEngine);
                gameEngine.onGameEvent(GameEvent.AsteroidHit);
                gameEngine.setScoreToAdd(10);
            }
        }
        else if(otherObject instanceof SpaceShipPlayer && parent.getClass().equals(Asteroid.class)){
            removeObject(gameEngine);
            SpaceShipPlayer s = (SpaceShipPlayer) otherObject;
        }
    }
}
