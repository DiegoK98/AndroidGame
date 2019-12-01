package dadm.scaffold.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dadm.scaffold.R;
import dadm.scaffold.space.Bullet;

public abstract class Sprite extends ScreenGameObject {

    protected double rotation;

    protected double pixelFactor;

    //Enemy type
    protected int enemyType = 0;

    private final Bitmap bitmap;

    private final Matrix matrix = new Matrix();

    protected Sprite (GameEngine gameEngine, int drawableRes) {
        enemyType = new Random().nextInt(5);
        int id;

        //Enemy
        if(drawableRes == 0) {
            switch (enemyType) {
                case 1:
                    id = R.drawable.enemigo_beligerante_claro;
                    break;
                case 2:
                    id = R.drawable.enemigo_pacifico_claro;
                    break;
                case 3:
                    id = R.drawable.enemigo_beligerante_oscuro;
                    break;
                default:
                    id = R.drawable.enemigo_pacifico_oscuro;
                    break;
            }
        }
        //Bullet
        else{
            id = drawableRes;
        }

        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(id);

        this.pixelFactor = gameEngine.pixelFactor;

        this.width = (int) (spriteDrawable.getIntrinsicHeight() * this.pixelFactor);
        this.height = (int) (spriteDrawable.getIntrinsicWidth() * this.pixelFactor);

        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        radius = Math.max(height, width)/2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (positionX > canvas.getWidth()
                || positionY > canvas.getHeight()
                || positionX < - width
                || positionY < - height) {
            return;
        }
        matrix.reset();
        matrix.postScale((float) pixelFactor, (float) pixelFactor);
        matrix.postTranslate((float) positionX, (float) positionY);
        matrix.postRotate((float) rotation, (float) (positionX + width/2), (float) (positionY + height/2));
        canvas.drawBitmap(bitmap, matrix, null);
    }

    public void releaseBullet(Bullet bullet) {

    }
}
